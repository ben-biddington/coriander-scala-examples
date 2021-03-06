package org.coriander.learning.scala.syntax.examples

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._

class BlockExamples {
	var disposable : Disposable = null

	@Test def
	for_example_blocks_can_be_used_for_automatic_resource_management {
		given_a_disposable_resource
		even_when_an_exception_is_thrown_inside_using_block
		then_resource_is_still_disposed
	}

	@Test def
	blocks_can_clean_up_a_splat_of_resources {
		val aDisposableObject = mock(classOf[Disposable])
		val anotherDisposableObject = mock(classOf[Disposable])

		using (aDisposableObject, anotherDisposableObject) { () =>
			
		}

		verify(aDisposableObject, times(1)).dispose
		verify(anotherDisposableObject, times(1)).dispose
	}

	def even_when_an_exception_is_thrown_inside_using_block {
		try {
			using (disposable) { () =>
				//
				// Any other processing with the resource
				//
				throw new RuntimeException("This shows that resource management works.")
			}
		} catch {
			case e : RuntimeException => { }
			case _ => fail("Unexpected exception")
		}
	}

	def given_a_disposable_resource {
		disposable = mock(classOf[Disposable])
	}

	def then_resource_is_still_disposed {
		verify(disposable, times(1)).dispose
	}
}

trait Disposable {
	def dispose()
}

object using {
	def apply(resources : Disposable*) (block : => Unit) {
		try {
			block
		} finally {
			if (resources != null) {
				resources.foreach(_.dispose)
			}
		}
	}
}