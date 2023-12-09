package org.hrahman.ddp.ddpspark.failedbanks

import hrahman.ddp.hibernate.models.FailedBankFileInfo
import hrahman.ddp.hibernate.services.FailedBankFileInfoService
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.hrahman.ddp.ddpspark.utils.{SparkUtil, Utils}
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

case class FailedBankSchema(bankName: String, state: String, acquiringInstitution: String, closingDate: String, funds: Double)

object FailedBanksFileProcessor {

  private val defaultOutputFileCount = 16

  private val appContext = new ClassPathXmlApplicationContext("main-spark-config.xml")

  def main(args: Array[String]): Unit = {

    process()

    appContext.close()
  }

  private def process(): Unit = {

    val fileCount = System.getenv().getOrDefault("fileCount", s"$defaultOutputFileCount").toInt
    val processedDir = System.getenv().getOrDefault("processedDir", SparkUtil.defaultSaveDir)
    val archivedDir = System.getenv().getOrDefault("archivedDir", SparkUtil.defaultArchiveDir)
    val masterUrl = System.getenv().getOrDefault("masterUrl", SparkUtil.defaultMaster)

    val failedBankService = appContext.getBean("failedBankFileInfoService").asInstanceOf[FailedBankFileInfoService]

    import scala.collection.JavaConverters._

    val unprocessedFiles = failedBankService.getUnprocessedFiles.asScala.toSet

    val spark = SparkSession.builder()
      .appName("Failed Banks")
      .master(masterUrl)
      .getOrCreate()

    println("Total unprocessed files in DB: " + unprocessedFiles.size)

    unprocessedFiles.foreach(file => {

      println("Processing file: " + file)

      val linesRdd = SparkUtil.removeHeader(readFile(spark, file))

      val csvFileDataRdd = linesRdd
        .map(line => {
        FailedBankSchema(
          bankName = line(0).toString,
          state = line(2).toString,
          acquiringInstitution = line(4).toString,
          closingDate = Utils.formatDate(line(5).toString),
          funds = line(6).toString.toDouble
        )
      })
        .map(data => s"${data.bankName},${data.state},${data.acquiringInstitution},${data.closingDate},${data.funds}")

      val outputPath = Utils.getJoinedPath(processedDir, file.getFileName.replace("unprocessed", "processed")).toString

      val writeModePath = if (Files.isDirectory(Paths.get(outputPath))) outputPath.concat(LocalDateTime.now().toString) else outputPath

      val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
      fs.delete(new Path(outputPath), true)

      csvFileDataRdd
        .repartition(fileCount)
        .saveAsTextFile(writeModePath)

      file.setProcessorFlag('P')
      file.setProcessedDir(writeModePath)

      failedBankService.update(file)

      val sourcePath = Utils.getJoinedPath(file.getDownloadDir, file.getFileName)
      val archivedPath = Utils.getJoinedPath(archivedDir, file.getFileName.replace("unprocessed", "archived"))
      Files.move(sourcePath, archivedPath)
    })

    spark.stop()
  }

  private def readFile(spark: SparkSession, fileInfo: FailedBankFileInfo): RDD[Row] = {

    val fileOptions = Map("inferSchema" -> "true", "delimiter" -> ",", "header" -> "true")

    val localFilePath = Utils.getJoinedPath(fileInfo.getDownloadDir, fileInfo.getFileName).toString

    spark.read
      .options(fileOptions)
      .csv("file://" + localFilePath)
      .rdd
  }
}
