package org.coriander.learning.scala.list.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class Reduce extends TestBase {
	@Test
	def reduce_right_combines_list_items_right_to_left {
		val list = List(1, 2, 3, 4, 5)
		val expectedResult = (5 + 4) + (3 + 2) + (1)

		val result = list reduceRight((left, right) => left + right)

		assertThat(result, is(equalTo(expectedResult)))
	}

	@Test
	def reduce_left_combines_list_items_right_to_left {
		val list = List(1, 2, 3, 4, 5)
		val expectedResult = (1 + 2) + (3 + 4) + 5

		val result = list reduceLeft((left, right) => left + right)

		assertThat(result, is(equalTo(expectedResult)))
	}
}