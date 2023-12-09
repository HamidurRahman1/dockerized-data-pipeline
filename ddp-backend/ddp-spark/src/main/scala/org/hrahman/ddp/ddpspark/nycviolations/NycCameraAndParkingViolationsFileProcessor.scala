package org.hrahman.ddp.ddpspark.nycviolations

import hrahman.ddp.hibernate.models.NycViolationAnalytics
import hrahman.ddp.hibernate.services.NycViolationAnalyticsService
import org.apache.spark.sql.functions.{avg, count, desc}
import org.apache.spark.sql.{Encoder, SparkSession}
import org.hrahman.ddp.ddpspark.utils.{SparkUtil, Utils}
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

case class CameraParkingViolationFile(
                                       plate: Option[String],
                                       state: Option[String],
                                       license_type: Option[String],
                                       summons_number: Option[String],
                                       issue_date: Option[String],
                                       violation_time: Option[String],
                                       violation: Option[String],
                                       fine_amount: Option[String],
                                       penalty_amount: Option[String],
                                       interest_amount: Option[String],
                                       reduction_amount: Option[String],
                                       payment_amount: Option[String],
                                       amount_due: Option[String],
                                       precinct: Option[String],
                                       county: Option[String],
                                       issuing_agency: Option[String],
                                       summons_image: Option[SummonsImageInfo]
                                     )

case class SummonsImageInfo(url: Option[String], description: Option[String])

case class VehicleInfo(plate: Option[String], state: Option[String], license_type: Option[String])

object NycCameraAndParkingViolationsFileProcessor {

  private val defaultOutputFileCount = 8

  private val appContext = new ClassPathXmlApplicationContext("main-spark-config.xml")

  def main(args: Array[String]): Unit = {

    process()

    appContext.close()

  }

  private def process(): Unit = {

    val nycFilePath = System.getenv().getOrDefault("filePath", "")
    val archivedDir = System.getenv().getOrDefault("archivedDir", SparkUtil.defaultArchiveDir)
    val processedDir = System.getenv().getOrDefault("processedDir", SparkUtil.defaultSaveDir)

    if (nycFilePath.isEmpty || !Files.isRegularFile(Paths.get(nycFilePath))) {
      throw new RuntimeException("Invalid file path or dir. Found: " + nycFilePath)
    }

    if (!Files.isDirectory(Paths.get(archivedDir)))
      throw new RuntimeException("Invalid archived dir. Found: " + archivedDir)

    val spark = SparkSession
      .builder
      .appName("Process Json File")
      .master("local[2]")
      .getOrCreate()

    val violationFileEncoder: Encoder[CameraParkingViolationFile] = org.apache.spark.sql.Encoders.product[CameraParkingViolationFile]

    val violationsDataset = spark.read.option("multiline", "true").json(nycFilePath).as[CameraParkingViolationFile](violationFileEncoder)

    violationsDataset.createOrReplaceTempView("violations")

    val countyWithHighestViolations = violationsDataset
      .groupBy("county")
      .agg(count("violation").as("totalViolations"))
      .orderBy(desc("totalViolations"))
      .select("county", "totalViolations")
      .first()

    val precinctWithHighestAverageFine = violationsDataset
      .groupBy("precinct")
      .agg(avg("fine_amount").as("averageFineAmount"))
      .orderBy(desc("averageFineAmount"))
      .select("precinct", "averageFineAmount")
      .first()

    val nycViolationAnalyticsService = appContext.getBean("nycViolationAnalyticsService").asInstanceOf[NycViolationAnalyticsService]

    val nycViolationAnalytics = new NycViolationAnalytics
    nycViolationAnalytics.setInputFile(nycFilePath)
    nycViolationAnalytics.setCountyName(countyWithHighestViolations.getString(0))
    nycViolationAnalytics.setCountyViolations(countyWithHighestViolations.getLong(1))
    nycViolationAnalytics.setPrecinct(precinctWithHighestAverageFine.getString(0))
    nycViolationAnalytics.setPrecinctAvgFine(precinctWithHighestAverageFine.getDouble(1))

    nycViolationAnalyticsService.save(nycViolationAnalytics)

    val vehicleFileEncoder: Encoder[VehicleInfo] = org.apache.spark.sql.Encoders.product[VehicleInfo]

    val uniqueVehicleInfoDataset = violationsDataset
      .select("plate", "state", "license_type")
      .distinct()
      .as[VehicleInfo](vehicleFileEncoder)

    val outputPath = Utils.getJoinedPath(processedDir, Paths.get(nycFilePath).getFileName.toString).toString

    val writeModePath = if (Files.isDirectory(Paths.get(outputPath))) outputPath.concat(LocalDateTime.now().toString) else outputPath

    uniqueVehicleInfoDataset
      .repartition(defaultOutputFileCount)
      .write
      .parquet(writeModePath)

    Files.move(Paths.get(nycFilePath), Utils.getJoinedPath(archivedDir, Paths.get(nycFilePath).getFileName.toString + "_archived_" + LocalDateTime.now()))
    Files.deleteIfExists(Utils.getJoinedPath(Paths.get(nycFilePath).getParent.toString, "_success"))

    spark.stop()
  }
}
