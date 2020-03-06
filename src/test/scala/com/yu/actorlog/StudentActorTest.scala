package com.yu.actorlog

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import com.yu.actorlog.actor.TeacherActor
import com.yu.actorlog.entity.QuoteResponse
import com.yu.actorlog.withlogactor.{StudentActor, TeacherLogActor}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by yumaoying on 2019/8/30.
  */
class StudentActorTest extends TestKit(ActorSystem("testLogSystem",
  ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  "StudentActor" must {
    "测试响应是否正确" in {
      //首先将 testProbe 给被测 Actor 发送消息，再看 testProbe 是否接受到期望的回应消息。
      val teacherActor = system.actorOf(Props[TeacherLogActor])
      val studentActor = system.actorOf(Props(new StudentActor(teacherActor)))
      val testProbe = new TestProbe(system)
      testProbe.send(studentActor, QuoteResponse)
      testProbe.expectMsg("收到")
    }
  }
}
