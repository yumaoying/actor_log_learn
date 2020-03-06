package com.yu.actorlog

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import com.yu.actorlog.actor.TeacherActor
import com.yu.actorlog.entity.{QuoteRequest, QuoteResponse}
import com.yu.actorlog.withlogactor.TeacherLogActor
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by yumaoying
  * TestKit 它接收一个ActorSystem对象，通过ActorSystem我们可以创建Actors
  * WordSpecLike 可以用must,in等语句描述测试方法
  * BeforeAndAfterAll 运行完毕后关闭ActorSystem
  */
class TeacherPreTest extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {


  /**
    * 仅仅将消息发送给PrintActor，不做任何的检查
    */
  "A teacher" must {
    "发送一个消息给teacher,不做检查" in {
      val teacherRef = TestActorRef[TeacherActor]
      teacherRef ! QuoteRequest
    }
  }

  /** *
    * 将消息发送到 Log actor，而 Log actor用ActorLogging 的相应方法将消息发送到EventStream
    */
  "A teacher with ActorLogging" must {
    "发送一个消息给teacherLog ,不做检查" in {
      val teacherLogRef = TestActorRef[TeacherLogActor]
      teacherLogRef ! QuoteRequest
    }
  }


  /** *
    * 检查actor内部状态
    */
  "A teacher with ActorLogging" must {
    "have a quote list of size 4" in {
      //actor是不能直接访问的，所以使用TestActorRef允许我们直接访问Actor内部
      val teacherLogRef = TestActorRef[TeacherLogActor]
      //通过underlyingActor 方法进入到Actor的内部
      teacherLogRef.underlyingActor.quoteListList must have size (4)
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
