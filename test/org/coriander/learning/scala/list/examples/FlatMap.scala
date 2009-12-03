package org.coriander.learning.scala.list.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class FlatMap extends TestBase {
	@Test
	def flatmap_does_what {
		val list = List(1, 2, 3)
		val result = list.flatMap(item => "(" + item + ")")

		Console println result
	}

	@Test
	def flatmap_does_interesting_thing_with_lists_of_strings {
		val list = List("the")

		val result = list.flatMap(item => item)
		val expected = "List(t, h, e)"
		
		assertThat(result toString, is(equalTo(expected)))
	}
}