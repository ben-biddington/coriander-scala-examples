package org.coriander.learning.scala.regex.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class RegexExamples {
    lazy val SAMPLE_TEXT = "2009-12-06 DEBUG Tiger woods in trouble"

	@Test def group_captures_can_be_assigned_directly_to_variables {
		val fullDatePattern = """^(\d{4}-\d{2}-\d{2}).+""".r

		val fullDatePattern(date) = SAMPLE_TEXT

		assertThat(date, is(equalTo("2009-12-06")))
	}

	@Test def multiple_group_captures_can_be_assigned_to_variables {
		val dayMonthYearPattern = """^(\d{4})-(\d{2})-(\d{2}).+""".r

		val dayMonthYearPattern(year, month, day) = SAMPLE_TEXT

		assertThat(year, 	is(equalTo("2009")))
		assertThat(month, 	is(equalTo("12")))
		assertThat(day, 	is(equalTo("06")))
	}
}