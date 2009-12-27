package org.coriander.learning.scala.concurrency.examples

import _root_.scala.actors.Actor._
import _root_.scala.actors.Futures._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase
import collection.mutable.ListBuffer
import actors.{Actor, Future}
import org.junit.{Ignore, Test}

// @See: http://scala-tools.org/scaladocs/scala-library/2.7.1/actors/Future.scala.html#Some(38)
class FutureExamples extends TestBase {
	val TIMEOUT = 1000 * 1000

	@Test
	def double_bang_sends_message_to_actor_and_returns_new_future_representing_reply {
		val aFuture = anActorThatReturnsItsThreadId !! ""

		val result = aFuture().asInstanceOf[String]

		assertThat(result, is(not(equalTo(currentThreadId))))
	}

	@Test
	def invoking_a_future_blocks_until_result_is_available {
		val aFuture : Future[Any] = anActorThatSleepsAndReturnsItsThreadId(500) !! ""

		val theThreadId = aFuture().asInstanceOf[String]

		assertThat(theThreadId, is(not(equalTo(currentThreadId))))
	}

	@Test
	def ad_doc_future_defines_an_operation_to_be_invoked_asynchronously {
		val aFuture = future[String] {
			currentThreadId
		};

		assertThat(aFuture(), is(not(equalTo(currentThreadId))))
	}

	@Test
	def future_can_return_function_also_anf_this_function_is_invoked_on_parent_thread {
		val theFuture = future(() => {
			currentThreadId
		})

		val theFutureCallback : Function0[String] = theFuture()

		assertThat("Expected that the callback should be invoked on parent thread.",
			theFutureCallback(),
			is(equalTo(currentThreadId))
		)
	}

	@Test
	def await_all_blocks_until_all_actors_have_completed {
		val aFuture 		= future[String] { Thread.sleep(500); "x" }
		val anotherFuture 	= future[String] { Thread.sleep(500); "y" }

		val results : List[Option[Any]] = awaitAll(TIMEOUT, aFuture, anotherFuture);

		assertThat("Expected two results", results.size, is(equalTo(2)))

		results.count((result : Option[Any]) => result == None)

		results.foreach((option : Option[Any]) => println(option.get))
	}

	@Test
	def you_can_also_wait_in_loop_for_futures_to_complete {
		val futures = new ListBuffer[Future[String]]

		futures += future[String] { "x" }
		futures += future[String] { "y" }

		val temp = new ListBuffer[String]

		futures.foreach(f => temp += f())

		val actual = temp.toList
		val expected = List("x", "y")
		assertThat(actual, is(equalTo(expected)))
	}

	@Test
	def future_is_not_run_until_invoked {
		var count = 0

		val f = future { count = 1 }

		assertThat(count, is(equalTo(0)))
	}

	@Test
	@Ignore("Can't get it to pass")
	def await_all_returns_None_if_a_future_times_out {
		val aFuture = future { Thread.sleep(500); "x" }
		val results : List[Option[Any]] = awaitAll(2000, aFuture);

		assertTrue(
			"Expected <None> but was <" +  results.first + ">",
			results.first == None
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

	def anActorThatReturnsItsThreadId = anActorThatSleepsAndReturnsItsThreadId(0)

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