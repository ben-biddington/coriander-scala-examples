package org.coriander.learning.scala.concurrency.examples

import java.lang.Thread._
import _root_.scala.actors.Actor._
import collection.mutable.ListBuffer
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.junit.Test
import org.coriander.learning.scala.TestBase
import actors.Actor

class ActorExamples extends TestBase {
	@Test
	def bang_query_blocks_caller_until_actor_returns {
		anActorThatReturnsItsThreadId !? "xxx" match {
			case reply : String => {
				val actualThreadUsedByChildActor = reply
				assertThat(currentThreadId, is(not(equalTo(actualThreadUsedByChildActor))))
			}
		}
	}

	@Test
	def exceptions_need_to_be_specifically_communicated {
		val anActorThatThrowsAnException = actor {
			loop {
				receive {
					case _ => reply(new RuntimeException("An error occured on purpose."))
				}
			}
		}

		anActorThatThrowsAnException !? "xxx" match {
			case error : Exception => { } 
		}
	}

	@Test
	def parent_can_block_until_actors_all_complete {

		anActorThatReturnsItsThreadId ! ""
		anActorThatReturnsItsThreadId ! ""
		anActorThatReturnsItsThreadId ! ""

		var theNumberOfRunningActors = 3

		while (theNumberOfRunningActors > 1) {
			receive {
				case threadId : String => theNumberOfRunningActors -= 1
				case _ => throw new RuntimeException()
			}
		}
	}

	@Test
	def the_actor_core_thread_pool_size_can_be_set_using_system {
		val theValue = "5"

		System.setProperty("actors.corePoolSize", theValue)

		assertThat(System.getProperty("actors.corePoolSize"), is(equalTo(theValue)))
	}

	@Test
	def the_actor_max_thread_pool_size_can_be_set_using_system {
		val theValue = "200"

		System.setProperty("actors.maxPoolSize", theValue)

		assertThat(System.getProperty("actors.maxPoolSize"), is(equalTo(theValue)))
	}

	@Test
	def messages_are_always_received_on_parent_thread {
		val expectedThreadId = currentThreadId

		anActorThatReturnsItsThreadId ! ""
		anActorThatReturnsItsThreadId ! ""
		anActorThatReturnsItsThreadId ! ""

		var theNumberOfRunningActors = 3

		while (theNumberOfRunningActors > 0) {
			receive {
				case threadId : String => {
					theNumberOfRunningActors -= 1
					assertThat(currentThreadId, is(equalTo(expectedThreadId)))
					assertThat(currentThreadId, is(not(equalTo(threadId))))
				}
			}
		}
	}

	// TEST: replies are buffered in sender's mailbox

	val anActorThatReturnsItsThreadId = actor {
		loop {
			receive {
				case _ => reply(currentThreadId)
			}
		}
	}

	private def console(message : String) = println("[" + currentThreadId + "] " + message);
	private def currentThreadId = currentThread.getId.toString
}