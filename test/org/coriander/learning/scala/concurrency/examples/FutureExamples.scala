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
import actors.{Actor, Future}

// @See: http://scala-tools.org/scaladocs/scala-library/2.7.1/actors/Future.scala.html#Some(38)
class FutureExamples extends TestBase {
	val TIMEOUT = 1000 * 1000

	@Test
	def double_bang_sends_message_to_actor_and_returns_new_future_representing_reply {
		val aFuture = anActorThatSleepsAndReturnsItsThreadId(0) !! ""

		val results = awaitAll(TIMEOUT, aFuture);

		assertThat(results.first.get.toString, is(not(equalTo(currentThreadId))))
	}

	@Test
	def invoking_a_future_blocks_until_result_is_available {
		val aFuture : Future[Any] = anActorThatSleepsAndReturnsItsThreadId(500) !! ""

		val theThreadId = aFuture().asInstanceOf[String]

		assertThat(theThreadId, is(not(equalTo(currentThreadId))))
	}

	@Test
	def await_all_blocks_until_all_actors_have_completed {
		val aFuture 		= anActorThatSleepsAndReturnsItsThreadId(0) !! ""
		val anotherFuture 	= anActorThatSleepsAndReturnsItsThreadId(0) !! ""

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

	def anActorThatSleepsAndReturnsItsThreadId(sleepPeriodInMilliseconds : Int) = actor {
		loop {
			receive {
				case _ => {
					Thread.sleep(sleepPeriodInMilliseconds)
					reply(currentThreadId)
				}
			}
		}
	}

	private def currentThreadId = currentThread.getId.toString
}