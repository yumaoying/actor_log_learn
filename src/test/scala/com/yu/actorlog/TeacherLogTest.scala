package com.yu.actorlog

import akka.actor.ActorSystem
import akka.testkit.{EventFilter, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import com.yu.actorlog.entity.{InitSignal, QuoteRequest}
import com.yu.actorlog.withlogactor.{StudentActor, TeacherLogActor}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by yumaoying
  *
  * TestKit 它接收一个ActorSystem对象，通过ActorSystem我们可以创建Actors
  * WordSpecLike 可以用must,in等语句描述测试方法
  * BeforeAndAfterAll 运行完毕后关闭ActorSystem
  *
  * 检查日志消息
  * 所有的日志消息将发送到EventStream 中，而SLF4JLogger将订阅它，
  * 然后将那些消息输入到日志文件/控制台中。
  * 如果我们直接在测试用例中直接订阅EventStream,
  * 那个检查日志就可实现
  */
class TeacherLogTest extends TestKit(ActorSystem("testLogSystem",
  ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  "A teacher log" must {
    "Verifying log messages from eventStream" in {
      val teacherRef = TestActorRef[TeacherLogActor]
      //EventFilter.info块仅仅接受以QuoteResponse (pattern='QuoteResponse*)开始的日志,
      //如果没有消息发送到TeacherLogActor，那么这个测试用例将会失败
      EventFilter.info(pattern = "QuoteResponse*", occurrences = 1) intercept {
        teacherRef ! QuoteRequest
      }
    }
  }

  "A student" must {
    "log a QuoteResponse eventually when an InitSignal is sent to it" in {
      val teacherRef = TestActorRef[TeacherLogActor]
      //val teacherRef = system.actorOf(Props[TeacherLogActor], "teacherActor")
      val studentRef = TestActorRef(new StudentActor(teacherRef))
      //  val studentRef =system.actorOf(Props(new StudentActor(teacherRef)), "studentActor")
      //通过underlyingActor 方法进入到Actor的内部,进行检查
      teacherRef.underlyingActor.quoteListList must have size (4)
      //检查日志
      EventFilter.info(start = "Printing from Student Actor", occurrences = 1).intercept {
        studentRef ! InitSignal
      }
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
