package com.yu.actorlog.actor

import akka.actor.Actor

/**
  * Created by yumaoying
  */
object Greeter {

  case object Greet

  case object Done

}

/**
  * Greeter的内容很简单，一旦它被创建，
  * 等待接受消息，打印Hello World!，
  * 并告诉调用者Greeter.Done
  */
class Greeter extends Actor {
  override def receive: Receive = {
    case Greeter.Greet =>
      println("Hello World!")
      sender() ! Greeter.Done
  }
}
