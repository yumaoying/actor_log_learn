package com.yu.actorlog.actor

import akka.actor.{Actor, ActorLogging, ActorRef}

class HelloACtor(otherActor: ActorRef) extends Actor with ActorLogging {
  var day = 0

  //重写接受消息的偏函数，其功能是接受消息并处理
  override def receive: Receive = {
    case "Hello" =>
      sender ! "Hi"
    case "Add" =>
      sender ! "关闭闹钟"
      day += 1
      log.info("day is %d".format(day))
      otherActor ! "is continue ?"
    case "Stop" =>
      context.stop(self) //停止自己的actorRef
      context.system.terminate() //关闭ActorSystem,即关闭其内部的线程池(ExcutorService)
    case _ =>
      sender ! "你是？"
  }

}