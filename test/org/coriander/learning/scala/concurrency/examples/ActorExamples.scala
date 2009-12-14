package org.coriander.learning.scala.concurrency.examples

import _root_.scala.actors.Actor._
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import java.lang.Thread._
import collection.mutable.ArrayBuffer

class ActorExamples {
    @Test
	def example_concurrently_processing_a_list { 
		val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

		var result : List[Int] = List()

		val newThread = actor {
			loop {
				react {
					case array : ArrayBuffer[Int] => {
						val threadId = currentThread.getId

						println("[" + threadId.toString + "] Received: " + array)
					}
				}
			}
		}

		println(currentThread.getId)

		val iterator = list.elements

		val str = Stream.fromIterator(iterator)

		val (listA, listB) = str.partition(_ > 5)

		newThread ! listA
		newThread ! listB

		println(currentThread.getId)
    }
}