package org.hrahman.ddp.ddpspark.utils

import java.nio.file.{FileSystems, Path, Paths}
import java.text.SimpleDateFormat

object Utils {

  def getJoinedPath(dir: String, filename: String): Path = Paths.get(dir.concat(FileSystems.getDefault.getSeparator).concat(filename))

  def formatDate(str: String, inputPattern: String = "dd-MMM-yy", outputPattern: String = "MM/dd/yyyy"): String = {
    val inputDateFormat = new SimpleDateFormat(inputPattern)
    val outputDateFormat = new SimpleDateFormat(outputPattern)
    outputDateFormat.format(inputDateFormat.parse(str))
  }
}
