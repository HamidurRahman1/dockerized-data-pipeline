package org.hrahman.ddp.ddpspark.failedbanks

import hrahman.ddp.hibernate.services.FailedBankFileInfoService
import org.springframework.context.support.ClassPathXmlApplicationContext

object FailedBanksFileProcessor {

  private val appContext = new ClassPathXmlApplicationContext("main-spark-config.xml")

  def main(args: Array[String]): Unit = {

    val saveToDatabase = System.getenv().getOrDefault("saveToDatabase", "false").toBoolean
    println(saveToDatabase)

    val failedBankService = appContext.getBean("failedBankFileInfoService").asInstanceOf[FailedBankFileInfoService]

    val unprocessedFiles = failedBankService.getAll

    unprocessedFiles.forEach(file => println(file))

    appContext.close()

  }

}
