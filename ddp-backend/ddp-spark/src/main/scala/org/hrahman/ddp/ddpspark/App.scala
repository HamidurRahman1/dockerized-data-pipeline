package org.hrahman.ddp.ddpspark

import hrahman.ddp.hibernate.models.TestEntity
import hrahman.ddp.hibernate.services.TestService
import org.springframework.context.support.ClassPathXmlApplicationContext;

object App
{
    def main(args: Array[String]): Unit = {

        println("test")

        val ctx = new ClassPathXmlApplicationContext("main-spark-config.xml")
        val bankService = ctx.getBean("testService").asInstanceOf[TestService]

        println("get all")
        bankService.getAll.forEach(e => println(e))

        val testEntity = new TestEntity
        testEntity.setValue("testing spark");

        bankService.save(testEntity)

        println("get all after save")
        bankService.getAll.forEach(e => println(e))

        ctx.close()
    }
}
