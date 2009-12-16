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
	def parent_can_block_until_actors_all_complete {
		val elapsed = time({
			newSortingActor ! randomIntegers(1000)
			newSortingActor ! randomIntegers(1000)
			newSortingActor ! randomIntegers(1000)
		})

		val sortedList = reduce(3)

		assertSorted(sortedList)
	}

	private def reduce(howMany : Int) : List[Int] = {
		var result 		= new ListBuffer[Int]
		var sortedCount = 0

		while (sortedCount < howMany) {
		  	receive {
				case list : List[Int] => {
					result.appendAll(list)
					sortedCount += 1
				}
			}
		}

		result.toList
	}

	private def assertSorted(list : List[Int]) {
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