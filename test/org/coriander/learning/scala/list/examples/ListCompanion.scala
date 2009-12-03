package org.coriander.learning.scala.list.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class ListCompanion extends TestBase {
	@Test
	def use_range_to_create_list_of_odd_numbers {
		val expected = List(1, 3, 5, 7, 9)

		val from 		= 1
		val to 			= 11
		val stepSize 	= 2

		val theFirstNineOddNumbers = List range(from, to, stepSize);

		assertThat(theFirstNineOddNumbers, is(equalTo(expected)))
	}

	@Test
	def use_tabulate_to_create_a_list_of_numbers_list_from_successive_integers {
		val expected = List(0, 2, 4, 6, 8)

		val lengthOfResult = 5

		val theFirstFiveNumbersDoubled = List.tabulate(lengthOfResult, (2 * _));

		assertThat(theFirstFiveNumbersDoubled, is(equalTo(expected)))
	}
}