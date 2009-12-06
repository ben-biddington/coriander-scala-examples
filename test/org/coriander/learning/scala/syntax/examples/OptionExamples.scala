package org.coriander.learning.scala.syntax.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class OptionExamples {
    val simpleData = Map("a" -> "Apple", "b" -> "Ball")
	
	@Test def missing_data_returns_none {
		val missingData = simpleData get ("c")

		assertTrue(missingData == None)
	}

	@Test def successful_search_returns_some {
		val someData : Option[String] = simpleData get("a")

		assertThat(someData.get, is(equalTo("Apple")))
	}
}