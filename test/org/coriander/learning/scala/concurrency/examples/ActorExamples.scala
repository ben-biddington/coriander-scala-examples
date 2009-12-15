package org.coriander.learning.scala.concurrency.examples

import _root_.scala.actors.Actor._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import java.lang.Thread._
import actors.Actor
import org.junit.{Test}
import collection.mutable.ListBuffer

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

		anActorThatReturnsItsThreadId !? "xxx" match {
			case reply : String => {
				val actualThreadUsedByChildActor = reply
				assertThat(currentThreadId, is(not(equalTo(actualThreadUsedByChildActor))))
			}
		}
	}

	@Test
	def parent_can_block_until_actors_all_complete {
		var result 		= new ListBuffer[Int]
		var sortedCount = 0

		newParallelSorter ! randomIntegers(100)
		newParallelSorter ! randomIntegers(100)
		newParallelSorter ! randomIntegers(100)

		while (sortedCount < 3) {
		  	receive {
				case list : List[Int] => {
					result.appendAll(list)
					sortedCount += 1
				}
			}
		}

		val sortedList = result.toList

		console("Sort complete, and full list contains " + sortedList.size + " items")

		assert(sortedList.first < sortedList.last)
		assertThat(sortedList.length, is(equalTo(300)))
	}

	private def randomIntegers(howMany : Int) = {
		val random = new java.util.Random()
		var result = new ListBuffer[Int]

		for (val i <- 0 until howMany) result += random.nextInt

		result.toList
	}

	private def console(message : String) {
		println("[" + currentThreadId + "] " + message)
	}

	val newParallelSorter : Actor = actor {
		loop {
			receive {
				case list : List[Int] => {
					reply(sort(list))
				}
			}
		}
	}

	private def sort(list : List[Int]) : List[Int] = {
		list sort(_<_)
	}

	private def currentThreadId = currentThread.getId.toString
}