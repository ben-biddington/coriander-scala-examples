package org.coriander.learning.scala.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class Lists extends TestBase {
	var even 		= List(0, 2, 4, 6, 8)
	var odd 		= List(1, 3, 5, 7, 9)

	@Test
	def zip_joins_two_lists_together_by_combining_items_at_the_same_index_into_a_tuple_2 {
		val zipped = even.zip(odd)

		val expectedFirstItem = (0, 1)
		
		assertThat(
			"The first tuple did not meet expectation.",
			zipped first, is(equalTo(expectedFirstItem))
		)
	}

	@Test
	def zipped_list_is_length_of_shortest_input_list {
		odd = List(0, 1, 2, 3)
		
		val zipped = even.zip(odd)

		val expectedSize = Math.min(even.size, odd.size)

		assertThat(
			"""The zipped list should have the same number of
			items as the smallest source list.""",
			zipped.size, is(equalTo(expectedSize))
		)
	}

	@Test
	def zipped_ignores_extra_items_in_the_longest_input_list {
		val expectedIgnoredValue = 5;

		even 	= List(0, 2)
		odd 	= List(1, 3, expectedIgnoredValue)

		val zipped = even.zip(odd)

		zipped.foreach(pair => {
			assertThat(pair._1, is(not(equalTo(expectedIgnoredValue))))
			assertThat(pair._2, is(not(equalTo(expectedIgnoredValue))))
		})
	}

}