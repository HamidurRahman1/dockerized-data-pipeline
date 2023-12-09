package org.hrahman.ddp.ddpspark.nycviolations

import org.apache.spark.sql.{Encoder, SparkSession}

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


object NycCameraAndParkingViolationsFileProcessor {

  def main(args: Array[String]): Unit = {

    process()

  }

  private def process(): Unit = {

    val spark = SparkSession
      .builder
      .appName("Process Json File")
      .master("local[2]")
      .getOrCreate()

    val jsonFilePath = "nc67-uf89.json"

    implicit val violationFileEncoder: Encoder[CameraParkingViolationFile] = org.apache.spark.sql.Encoders.product[CameraParkingViolationFile]

    val jsonDataset = spark.read.option("multiline", "true").json(jsonFilePath).as[CameraParkingViolationFile]

    jsonDataset.printSchema()

    jsonDataset.foreach(vf => {
      println(vf)
    })

    spark.stop()
  }
}
