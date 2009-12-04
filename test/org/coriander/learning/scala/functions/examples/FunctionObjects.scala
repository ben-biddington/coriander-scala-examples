package org.coriander.learning.scala.functions.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

// @See: http://creativekarma.com/ee.php/weblog/comments/scala_function_objects_from_a_java_perspective/
class FunctionObjects {
    @Test
	def a_function_object_has_one_or_more_apply_methods {
		val actual = Square(2)
		assertThat(actual, is(equalTo(4)))
    }

	@Test
	def function_objects_conceal_their_apply_methods_with_sugar {
		assertThat(Square(2), is(equalTo(Square.apply(2))))
    }

	// TEST: Why can't I write Cube 2?

	@Test
	def methods_that_take_functions_as_args_can_be_declared_using_either_the_Function_type_or_lambda {
		val expected = 8
		
		assertThat(cubeByFunction1(	2, Cube), is(equalTo(expected)))
		assertThat(cubeByLambda(	2, Cube), is(equalTo(expected)))
    }

	@Test
	def lambdas_can_be_declared_with_anonymous_arguments {
		val functionOfTwoIntegers 		: (Int, Int) => Int = (x : Int, y : Int) => x * y;
		val anonFunctionOfTwoIntegers 	: (Int, Int) => Int = (_*_)

		assertThat(
			"The two different representations should be equivalent",
			functionOfTwoIntegers(2, 2), is(equalTo(anonFunctionOfTwoIntegers(2, 2)))
		)
	}

	private def cubeByFunction1(number : Int, using : Function1[Int, Int]) : Int = using (number)

	private def cubeByLambda(number : Int, using : Int => Int) : Int = using (number)
}

object Square {
	def apply(in : Int) = in * in
}

object Cube extends Function1[Int, Int] {
	def apply(in : Int) = in * in * in
}