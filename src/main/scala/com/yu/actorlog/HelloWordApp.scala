package com.yu.actorlog

import akka.actor.{ActorSystem, Props}
import com.yu.actorlog.actor.HelloWordActor

/**
  * Created by yumaoying on
  */
object HelloWordApp {
  def main(args: Array[String]): Unit = {
    //actor启动方式
    //1.使用内置方法
    akka.Main.main(Array(classOf[HelloWordActor].getName))
    //2.手动创建ActorSystem
    val system = ActorSystem("helloApp")
    system.actorOf(Props[HelloWordActor], "helloWordActor")

    //在上面的例子里，我们使用的是system.actorOf来创建Actor，
    // actorOf返回的并不是Actor自身，而是一个ActorRef，
    // 它屏蔽了Actor的具体物理地址(可能是本jvm，也可以是其他jvm或另一台机器)。
    // 通过直接打印ActorRef看到Actor的path，比如本例是/helloApp/user/helloWordActor。
    //像这种直接由system创建出来的Actor被称为顶层Actor，
    // 一般系统设计的时候，顶层Actor数量往往不会太多，
    // 大都由顶层Actor通过getContext().actorOf()派生出来其他的Actor。

    //    它创建的Actor在guardian actor之下
    //    接着可以调用ActorContext.actorOf()
    //    在刚才创建的Actor内生成Actor树。
    //    这些方法会返回新创建的Actor的引用。
    //    每个Actor都可以直接访问Actor Context来或得它自身、Parent以及所有Children的引用。
  }
}
