package com.yu.actorlog.actor

import akka.actor.{Actor, Props}

/**
  * Created by yumaoying
  * HelloWordActor是我们这个应用的中心,它会启动和关闭所有actor(Greeter)
  */
class HelloWordActor extends Actor {
  //该方法在Actor对象构造方法执行后执行，整个Actor生命周期中仅执行一次。在 Actor启动之后但在处理其第一条消息之前调用
  override def preStart(): Unit = {
    //创建一个Greet actor
    val greeter = context.actorOf(Props[Greeter], "greeter")
    greeter ! Greeter.Greet
  }

  override def receive: Receive = {
    case Greeter.Done =>
      context.stop(self)
  }
}
