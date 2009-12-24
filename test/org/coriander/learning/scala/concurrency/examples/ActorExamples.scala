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

class ActorExamples extends TestBase {
	@Test
	def bang_query_blocks_caller_until_actor_returns {
		val anActorThatReturnsItsThreadId = actor {
			loop {
				receive {
					case _ => reply(currentThreadId)
				}
			}
		}

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
		val elapsed = time({
			newSortingActor ! randomIntegers(1000)
			newSortingActor ! randomIntegers(1000)
			newSortingActor ! randomIntegers(1000)
		})

		val sortedList = reduce(3)

		assertSorted(sortedList)
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

	private def reduce(howMany : Int) : List[Int] = {
		var sortedCount 		= 0
		var result : List[Int] = List()

		while (sortedCount < howMany) {
		  	receive {
				case list : List[Int] => {
					result 		= merge(list, result, (_<_))
					sortedCount += 1
				}
			}
		}

		result
	}

	private def merge(
		left 	: List[Int],
		right 	: List[Int],
		less 	: (Int, Int) => Boolean
	) : List[Int] = {
		if (left.isEmpty) 						right
		else if (right.isEmpty) 				left
		else if (less(left.head, right.head)) 	left.head :: merge(left.tail, right, less)
		else 									right.head :: merge(left, right.tail, less)
	}

	private def assertSorted(list : List[Int]) {
		assertFalse("List is empty, is that intentional?", list.isEmpty)
		assert(list.first < list.last)
	}

	private def randomIntegers(howMany : Int) = {
		val random = new java.util.Random()
		var result = new ListBuffer[Int]

		for (val i <- 0 until howMany) result += random.nextInt

		result.toList
	}

	val newSortingActor = actor {
		loop {
			receive {
				case list : List[Int] => reply(sort(list))
			}
		}
	}

	private def console(message : String) = println("[" + currentThreadId + "] " + message);
	private def sort(list : List[Int]) = list sort(_<_)
	private def currentThreadId = currentThread.getId.toString
}