package org.hrahman.ddp.ddpspark.utils

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

object SparkUtil {

  val defaultMaster = "local[2]"

  def removeHeader(rdd: RDD[Row]): RDD[Row] = {
    val header = rdd.first()
    rdd.filter(row => row != header)
  }

}
