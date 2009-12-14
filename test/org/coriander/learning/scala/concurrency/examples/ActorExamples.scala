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
	def each_actor_represents_a_thread {
		println("[" + currentThread.getId.toString + "] Starting...")
		startProcess(List(1, 2, 3, 4, 5))
		startProcess(List(1, 2, 3, 4, 5))
		startProcess(List(6, 7, 8, 9, 10))
		startProcess(List(6, 7, 8, 9, 10))

		Thread.sleep(2000)

		println("[" + currentThread.getId.toString + "] Done ")
    }

	private def startProcess(list : Iterable[Int]) {
		val newThread = actor {
			loop {
				receive {
					case list : List[Int] => {
						Thread.sleep(1000)
						self ! done ("[" + currentThread.getId.toString + "] Received: " + list)
					}
				}
			}
		}

		newThread ! list
	}

	private def done(message : String) {
		println(message)
	}
}