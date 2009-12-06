package org.coriander.learning.scala.syntax.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class OptionExamples {
    val simpleData 					= Map("a" -> "Apple", "b" -> "Ball")
	val A_KEY_THAT_DOES_NOT_EXIST 	= "c"
	val A_KEY_THAT_EXISTS 			= "a"

	@Test def missing_data_returns_none_and_is_empty {
		val missingData : Option[String] = simpleData get (A_KEY_THAT_DOES_NOT_EXIST)

		assertTrue(missingData == None)		
		assertTrue(missingData isEmpty)
	}

	@Test { val expected = classOf[NoSuchElementException] }
	def invoking_get_on_none_throws_exception {
		val missingData : Option[String] = simpleData get (A_KEY_THAT_DOES_NOT_EXIST)

		val theValue = missingData get;
	}

	@Test def successful_search_returns_some {
		val someData : Option[String] = simpleData get("a")

		assertThat(someData.get, is(equalTo("Apple")))
		assertFalse(someData isEmpty)
	}
}