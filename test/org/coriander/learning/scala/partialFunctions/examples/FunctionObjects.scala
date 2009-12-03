package org.coriander.learning.scala.partialFunctions.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class FunctionObjects {
    @Test
	def a_function_object_has_one_or_more_apply_methods {
		val actual = Square(2)
		assertThat(actual, is(equalTo(4)))
    }

	@Test
	def objects_conceal_their_apply_methods_with_sugar {
		val withSugar = Square(2)
		val withoutSugar = Square.apply(2)

		assertThat(withSugar, is(equalTo(withoutSugar)))
    }

	// TEST: Why can't I write Cube 2?

	@Test
	def methods_that_take_functions_as_args_can_be_declared_using_either_the_Function_type_or_lambda {
		val expected = 8
		
		assertThat(cubeByFunction1(	2, Cube), is(equalTo(expected)))
		assertThat(cubeByLambda(	2, Cube), is(equalTo(expected)))
    }

	def cubeByFunction1(
		number : Int,
		using : Function1[Int, Int]
	) = {
		using (number)
	}

	def cubeByLambda(
		number : Int,
		using : Int => Int
	) = {
		using (number)	
	}
}

object Square {
	def apply(in : Int) = in * in
}

object Cube extends Function1[Int, Int] {
	def apply(in : Int) = in * in * in
}