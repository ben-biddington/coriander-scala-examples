package org.coriander.learning.scala.functions.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.coriander.learning.scala.TestBase

class EtaExpansion extends TestBase {
	@Test
	def methods_may_be_used_as_functions_by_automatic_eta_expansion {
		val availableToEtaExpandedClosure = 10

		val cubed : Int = cubeByLambda(2, cubeAsEta)

		assertThat(cubed, is(equalTo(8)))			
	}

	@Test
	def methods_may_be_used_as_closures {
		def cubeByClosure = getCubeClosure

		val cubed = cubeByClosure(2)

		assertThat(cubed, is(equalTo(80)))
	}

	def getCubeClosure : Int => Int = {
		val multiplier = 10

		in => (in * in * in) * multiplier
	}

	def cubeAsEta(in : Int) : Int = in * in * in

	def cubeByLambda(number : Int, using : Int => Int) : Int = using(number)
}