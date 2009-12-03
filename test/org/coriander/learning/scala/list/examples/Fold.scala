package org.coriander.learning.scala.list.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class Fold extends TestBase {
	@Test
	def fold_left_combines_list_elements_from_start_to_end_using_binary_function {
		val startingValue = 0
		val list = 1 to 3

		val expected = (startingValue + (1 + 2)) + 3

		val actual = list.foldLeft(startingValue)(_+_)

		assertThat(actual, is(equalTo(expected)))
	}
	@Test
	def fold_right_combines_list_elements_from_end_to_start_zero_using_binary_function {
		val startingValue = 0
		val list = 1 to 3

		val expected = (startingValue + (3 + 2)) + 1

		val actual = list.foldRight(startingValue)(_+_)

		assertThat(actual, is(equalTo(expected)))
	}
}