package org.coriander.learning.scala.how.to

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class time_an_operation {
    @Test
	def using_system_current_time_and_a_function { 
		val THE_TIME_TO_SLEEP_IN_MILLISECONDS = 200

		val elapsed = time({
			Thread.sleep(THE_TIME_TO_SLEEP_IN_MILLISECONDS)
		})

		assertEquals(THE_TIME_TO_SLEEP_IN_MILLISECONDS, elapsed, 100)
    }

	private def time(what : => Unit) : Long = {
    	val start = System currentTimeMillis

		what

		(System currentTimeMillis) - start
	}
}