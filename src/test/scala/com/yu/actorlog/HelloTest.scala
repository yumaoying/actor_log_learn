package com.yu.actorlog

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.yu.actorlog.actor.{HelloACtor, TeacherActor}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by yumaoying
  * TestKit 它接收一个ActorSystem对象，通过ActorSystem我们可以创建Actors
  * WordSpecLike 可以用must,in等语句描述测试方法
  * BeforeAndAfterAll 运行完毕后关闭ActorSystem
  */
class HelloTest extends TestKit(ActorSystem("helloSystem"))
  with ImplicitSender //加这句，把testActor 设置为消息发出 Actor
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {


  /** *
    * TestProbe-测试响应是否预期
    */
  "helloACtor" must {
    "reponse a correct answer" in {
      //首先将 testProbe 给被测 Actor 发送消息，再看 testProbe 是否接受到期望的回应消息。
      val teacherActor = system.actorOf(Props[TeacherActor])
      val helloACtor = system.actorOf(Props(new HelloACtor(teacherActor)))
      val testProb = new TestProbe(system)
      testProb.send(helloACtor, "Hello")
      testProb.send(helloACtor, "other")
      //测试是否收到预期消息
      testProb.expectMsg("Hi")
      testProb.expectMsg("你是？")
    }
  }


  /** *
    * TestProbe-测试是否正确的发出消息
    */
  "helloACtor" must {
    "response a correct answer test" in {
      //将 testProbe 设置为被测 Actor 发出消息的目标
      val otherActor = TestProbe()
      val helloACtor = system.actorOf(Props(new HelloACtor(otherActor.ref)))
      helloACtor ! "Add"
      //测试是否收到预期消息
      otherActor.expectMsg("is continue ?")
      expectMsg("关闭闹钟")
    }
  }

  /** *
    * testActor-测试内部状态
    */
  "helloACtor" must {
    "response correctly" in {
      val teacherActor = system.actorOf(Props[TeacherActor])
      val helloACtor = TestActorRef(new HelloACtor(teacherActor))
      //从testActor发来
      helloACtor ! "Add"
      //测试是否符合预期
      expectMsg("Hi")
      assert(helloACtor.underlyingActor.day == 1)
    }
  }


  "helloACtor" must {
    "response correctly" in {
      val teacherActor = system.actorOf(Props[TeacherActor])
      val helloACtor = system.actorOf(Props(new HelloACtor(teacherActor)))
      //从testActor发来
      helloACtor ! "Hello"
      //测试是否符合预期
      expectMsg("Hi")
    }
  }

  /** *
    * 关闭ActorSystem
    */
  override def afterAll() {
    super.afterAll()
    system.terminate()
  }

}
