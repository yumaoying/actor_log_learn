package com.yu.actorlog.withlogactor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.yu.actorlog.entity.{InitSignal, QuoteRequest, QuoteResponse}

/**
  * Created by yumaoying
  * 双向通讯
  */
class StudentActor(teacherActor: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case InitSignal =>
      teacherActor ! QuoteRequest
    case QuoteResponse(quoteString) =>
      log.info("Received QuoteResponse from Teacher")
      log.info(s"Printing from Student Actor $quoteString")
      sender() ! "收到"
  }
}
