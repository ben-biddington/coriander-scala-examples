package org.coriander.learning.scala.list.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class Flatten extends TestBase {
	@Test
	def flatten_flattens_a_nested_list_structure {
		val list = List(List(1, 2, 3), List(4, 5, 6))
		val result = List flatten(list)

		assertThat(result, is(equalTo(List(1, 2, 3, 4, 5, 6))))
	} 
}