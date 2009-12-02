package org.coriander.learning.scala.examples

import org.junit.Test
import collection.mutable.ListBuffer
import java.lang.String

class OperatorNotationExamples {
	val ANY_STRING =
		"""Lorem ipsum dolor sit amet, consectetur adipisicing elit,
		sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."""

	@Test
	def parentheses_are_optional_only_when_method_accepts_single_argument {
		val list = new StringList
		list add ANY_STRING
		list addAll(ANY_STRING, ANY_STRING)
	}

	@Test
	def parentheses_are_optional_only_when_method_is_fully_qualified {
		// The following does not compile
		// print ANY_STRING
		Console print ANY_STRING
	}
}

class StringList {
	val innerList = new ListBuffer[String]

	def addAll(strings : String*) = strings.foreach(string => this add string)
	
	def add(string : String) = innerList += string

	def join : String = innerList mkString "," 
}