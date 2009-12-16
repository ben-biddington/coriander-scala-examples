package org.coriander.learning.scala

import org.junit.Assert

class TestBase extends Assert {
	protected def time(what : => Unit) : Long = {
    	val start = System currentTimeMillis

		what

		(System currentTimeMillis) - start
	}
}