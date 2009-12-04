package org.coriander.learning.scala.why.scala

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class Ftw extends TestBase {
    @Test
	def backside_is_a_valid_anonymous_function {
		val buttFunction : (Int, Int) => Int = (_*_);

		assertThat(buttFunction(7, 191), is(equalTo(1337)))
	}
}