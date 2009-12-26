package org.coriander.learning.scala.concurrency.examples

import _root_.scala.actors.Actor._
import _root_.scala.actors.Futures._
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase
import collection.mutable.ListBuffer
import actors.Future

// @See: http://lampsvn.epfl.ch/trac/scala/browser/scala/tags/R_2_7_7_final/src/actors/scala/actors/Future.scala?view=markup
class FutureExamples extends TestBase {
	val TIMEOUT = 1000 * 1000

	@Test def double_bang_sends_message_to_actor_and_returns_new_future_representing_reply {
		val aFuture = anActorThatSleepsAndReturnsItsThreadId !! ""

		val results = awaitAll(TIMEOUT, aFuture);

		assertThat(results.first.get.toString, is(not(equalTo(currentThreadId))))
	}

	@Test def await_all_blocks_until_all_actors_have_completed {
		val aFuture 		= anActorThatSleepsAndReturnsItsThreadId !! ""
		val anotherFuture 	= anActorThatSleepsAndReturnsItsThreadId !! ""

		val results : List[Option[Any]] = awaitAll(TIMEOUT, aFuture, anotherFuture);

		assertThat("Expected two results", results.size, is(equalTo(2)))
	}

	@Test
	def ad_doc_future_defines_an_operation_to_be_invoked_asynchronously {
		val aFuture = future[String] {
			currentThreadId
		};

		val results : List[Option[Any]] = awaitAll(TIMEOUT, aFuture);

		assertThat(results.first.get.toString, is(not(equalTo(currentThreadId))))
	}

	@Test
	def by_creating_a_future_of_function_type_a_callback_can_be_supplied_and_that_callback_is_invoked_on_parent_thread {
		val theCallback : () => String = () => {currentThreadId}

		val theFuture = future[() => String](theCallback)

		val results : List[Option[Any]] = awaitAll(TIMEOUT, theFuture)

		val f = results.first.get.asInstanceOf[() => String]

		assertThat("Expected that the callback should be invoked on parent thread.",
			f(),
			is(equalTo(currentThreadId))
		)
	}

	@Test
	def an_example_with_multiple_ad_hoc_futures {
		val results = awaitAll(TIMEOUT,
			newSimpleFuture,
			newSimpleFuture,
			newSimpleFuture
		)

		results.foreach((result : Option[Any]) => {
			println(result.get.toString)
		});
	}

	private def newSimpleFuture = {
		future[String] { Thread.sleep(1000); currentThreadId}
	}

	//	TEST: Applying a future blocks the current actor (self) until the future's value is available.
	//	TEST: Requests are handled asynchronously, but return a representation (the future) that allows to await the reply.
	//	TEST: Defining a future invokes actor internally

	def anActorThatSleepsAndReturnsItsThreadId = actor {
		loop {
			receive {
				case _ => {
					reply(currentThreadId)
				}
			}
		}
	}

	private def currentThreadId = currentThread.getId.toString
}