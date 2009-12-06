package org.coriander.learning.scala.xml.examples


import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

import scala.xml._
import org.coriander.learning.scala.TestBase

class Xml extends TestBase {
	@Test def xml_is_a_data_type {
		val anyXmlNode = <xml>No quotes required, it can be declared as is.</xml>;
	}

	@Test def xml_can_have_variable_expansion {
		val getNamedXml : String => Any = name => <name>{name}</name>

		val actual = getNamedXml("Ben")

		assertThat(actual toString, is(equalTo(<name>Ben</name> toString)))
	}

	@Test def xpath_is_available {
		val anyXml = <xml><title>Another kebab on a weekday</title></xml>
		val title = anyXml \\ "title" text

		assertThat(title toString, is(equalTo("Another kebab on a weekday")))
	}

	@Test def xpath_provides_attribute_selection_mechanism {
		val xml =
			<album>
				<title>Another kebab on a weekday</title>
				<artist aka="Akon">Andrew Conn</artist>
			</album>

		val andrewIsAlsoKnownAs : NodeSeq = (xml \\ "album" \ "artist" first) attribute("aka") get

		assertThat(andrewIsAlsoKnownAs toString, is(equalTo("Akon")))
	}
}