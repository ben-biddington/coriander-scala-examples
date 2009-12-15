package org.coriander.learning.scala.concurrency.examples

import _root_.scala.actors.Actor._
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import java.lang.Thread._

class ActorExamples {
	@Test
	def bang_query_blocks_caller_until_actor_returns {
		val anActorThatReturnsItsThreadId = actor {
			loop {
				receive {
					case _ => reply(currentThreadId)
				}
			}
		}

		var actualThreadUsed : String = null

		anActorThatReturnsItsThreadId !? "xxx" match {
			case reply : String => actualThreadUsed = reply
		}

		assertThat(currentThreadId, is(not(equalTo(actualThreadUsed))))
	}

	private def console(message : String) {
		println("[" + currentThreadId + "] " + message)
	}

	private def currentThreadId = currentThread.getId.toString
}