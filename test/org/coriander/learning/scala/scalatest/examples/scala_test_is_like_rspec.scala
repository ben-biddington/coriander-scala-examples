package org.coriander.learning.scala.scalatest.examples

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import collection.mutable.Stack

// @See: http://www.scalatest.org/
class scala_test_is_like_rspec extends FlatSpec with ShouldMatchers  {
	"A Stack" should "pop values in last-in-first-out order" in {
		val stack = new Stack[Int]
		stack.push(1)
		stack.push(2)
		stack.pop() should equal (2)
		stack.pop() should equal (1)
	}

	it should "throw NoSuchElementException if an empty stack is popped" in {
		val emptyStack = new Stack[String]
		evaluating { emptyStack.pop() } should produce [NoSuchElementException]
	}
}