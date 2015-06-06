/*
 * Copyright 2015 Thomas Joseph
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ethomasjoseph.jstemplates4j.dustjs.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.HelperException;

/**
 * 
 * @author Thomas Joseph
 *
 */
public class DustTest {

	private Dust dust;
	
	
	@Before
	public void setup() throws ScriptException {
		dust = new Dust();
	}
	
	@Test
	/**
	 * Testing the constructor which accepts a user supplied dust script
	 * 
	 * @throws ScriptException
	 */
	public void testConstructorWithSuppliedHandlebarsScript() throws ScriptException {
		String handlebarsMinJS = "dust-full.min-v2.7.1.js";
		InputStreamReader scriptSrc = new InputStreamReader(DustTest.class.getClassLoader().getResourceAsStream(handlebarsMinJS));
		new Dust(scriptSrc);
	}
	
	@Test(expected = ScriptException.class)
	/**
	 * Testing the constructor which accepts a user supplied wrong script.
	 * 
	 * @throws ScriptException
	 */
	public void testConstructorWithSuppliedWrongScript() throws ScriptException {
		InputStreamReader scriptSrc = new InputStreamReader(new ByteArrayInputStream("a wrong script".getBytes()));
		new Dust(scriptSrc);
	}
	
	@Test
	public void testComments() {
		SimpleBindings bindings = new SimpleBindings();
		bindings.put("script", "DustJS");
		JSTemplate jsTemplate = dust.compile("Hello {! commented code!} {script}");
		assertEquals("Hello  DustJS", dust.renderWithData(jsTemplate, bindings));
	}
	
	@Test
	public void testNewLine() {
		JSTemplate jsTemplate = dust.compile("Hello {~n} DustJS");
		assertEquals("Hello \n DustJS", dust.renderWithData(jsTemplate, null));
	}
	
	@Test
	public void testRenderingWithString() {
		JSTemplate jsTemplate = dust.compile("Hello {.}");
		assertEquals("Hello DustJS", dust.renderWithData(jsTemplate, "DustJS"));
	}
	
	@Test
	public void testRenderingWithSimpleBinding() {
		SimpleBindings bindings = new SimpleBindings();
		bindings.put("script", "DustJS");
		JSTemplate jsTemplate = dust.compile("Hello {script}");
		assertEquals("Hello DustJS", dust.renderWithData(jsTemplate, bindings));
	}
	
	@Test
	public void testRenderingWithSuppliedTemplateName() {
		SimpleBindings bindings = new SimpleBindings();
		bindings.put("script", "DustJS");
		JSTemplate jsTemplate = dust.compile("Hello {script}", "mytemplate");
		assertEquals("mytemplate", jsTemplate.getName());
		assertEquals("Hello DustJS", dust.renderWithData(jsTemplate, bindings));
	}
	
	@Test
	public void testRenderingWithJSON() {
		String json = "{\"name\" : \"DustJS\"}";
		JSTemplate jsTemplate = dust.compile("Hello {name}!");
		assertEquals("Hello DustJS!", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testSections() {
		String json = "{\"name\" : \"DustJS\", \"year\" : \"2010\", \"author\" : { \"name\" : \"Aleksander Williams\"}}";
		JSTemplate jsTemplate = dust.compile("{name} was originally authored by {#author}{name}{/author} at year {year}.");
		assertEquals("DustJS was originally authored by Aleksander Williams at year 2010.", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testSectionsWithElse() {
		String json = "{\"name\" : \"DustJS\", \"year\" : \"2010\"}";
		JSTemplate jsTemplate = dust.compile("{name} was {#author}originally authored by {name}{:else}a great scrpting language even{/author} at year {year}.");
		assertEquals("DustJS was a great scrpting language even at year 2010.", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testExistsSection() {
		String json = "{ \"isReady\": false }";
		JSTemplate jsTemplate = dust.compile("{?isReady}Ready!{:else}Wait a minute...{/isReady}");
		assertEquals("Wait a minute...", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testNotExistsSection() {
		String json = "{ \"isReady\": false }";
		JSTemplate jsTemplate = dust.compile("{^isReady}Not ready yet.{:else}I'm ready to go!{/isReady}");
		assertEquals("Not ready yet.", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testLogicHelper() {
		String json = "{\"level\":\"master\",\"age\":27,\"starfighterRentalAge\":25}";
		JSTemplate jsTemplate = dust.compile("{@eq key=level value=\"master\"}You are no longer a Padawan. {/eq}{@gt key=age value=starfighterRentalAge}Rent a Starfighter!{/gt}");
		assertEquals("You are no longer a Padawan. Rent a Starfighter!", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testLogicHelpersWithElse() {
		String json = "{\"level\":\"padawan\"}";
		JSTemplate jsTemplate = dust.compile("{@eq key=level value=\"master\"}You are no longer a Padawan.{:else}You have much to learn, young Padawan.{/eq}");
		assertEquals("You have much to learn, young Padawan.", dust.renderWithJSON(jsTemplate, json));
		
		json = "{\"level\":\"master\"}";
		assertEquals("You are no longer a Padawan.", dust.renderWithJSON(jsTemplate, json));
		
	}

	@Test
	public void testBuiltInHelpersWithCasting() {
		String json = "{\"bilbosAge\":50,\"gandalfsAge\":12345}";
		JSTemplate jsTemplate = dust.compile("{@eq key=bilbosAge value=\"50\" type=\"number\"}Looking nifty at fifty, Bilbo! {/eq}{@gt key=gandalfsAge value=\"10000\"}Gandalf is really old...{/gt}");
		assertEquals("Looking nifty at fifty, Bilbo! Gandalf is really old...", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testSeparatorHelpers() {
		String json = "{\"guests\":[\"Alice\",\"Bob\",\"Charlie\"]}";
		JSTemplate jsTemplate = dust.compile("{#guests}{@first}Hello {/first}{@last}and {/last}{.}{@sep}, {/sep}{@last}!{/last}{/guests}");
		assertEquals("Hello Alice, Bob, and Charlie!", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testSelectHelpers() {
		String json = "{\"testEnabled\":\"bunnies\"}";
		JSTemplate jsTemplate = dust.compile("<span class=\"\r\n" + 
				"  {@select key=testEnabled}\r\n" + 
				"    {@any}test-enabled {/any}\r\n" + 
				"    {@none}test-disabled {/none}\r\n" + 
				"    {@eq value=\"puppies\"}test-puppies{/eq}\r\n" + 
				"    {@eq value=\"bunnies\"}test-bunnies{/eq}\r\n" + 
				"  {/select}\r\n" + 
				"\">");
		assertEquals("<span class=\"test-enabled test-bunnies\">", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testMathHelpers() {
		String json = "{\"flavors\":[{\"name\":\"red bean\"},{\"name\":\"green tea\"},{\"name\":\"mango\"},{\"name\":\"peanut\"}]}";
		JSTemplate jsTemplate = dust.compile("<ul>\r\n" + 
				"  {#flavors}\r\n" + 
				"    <li\r\n" + 
				"      {@math key=$idx method=\"mod\" operand=\"2\"}\r\n" + 
				"        {@eq value=\"0\" type=\"number\"} class=\"alt-row\"{/eq}\r\n" + 
				"      {/math}>\r\n" + 
				"      {name}\r\n" + 
				"    </li>\r\n" + 
				"  {/flavors}\r\n" + 
				"</ul>");
		assertEquals("<ul><li class=\"alt-row\">red bean</li><li>green tea</li><li class=\"alt-row\">mango</li><li>peanut</li></ul>", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testMathHelpersSelfClosing() {
		String json = "{\"progress\":70}";
		JSTemplate jsTemplate = dust.compile("There is {@math key=100 method=\"subtract\" operand=progress/}% left to do.");
		assertEquals("There is 30% left to do.", dust.renderWithJSON(jsTemplate, json));
	}
	
	@Test
	public void testDebuggingWithContextDump() {
		String json = "{\"houses\":{\"gryffindor\":{\"founder\":\"Godric Gryffindor\"},\"hufflepuff\":{\"founder\":\"Helga Hufflepuff\"}}}";
		JSTemplate jsTemplate = dust.compile("{#houses.gryffindor}\r\n" + 
				"  {! Default: key=\"current\" and to=\"output\" !}\r\n" + 
				"  {@contextDump/}\r\n" + 
				"  {! Check your console for the full context !}\r\n" + 
				"  {@contextDump key=\"full\" to=\"console\"/}\r\n" + 
				"{/houses.gryffindor}");
		assertThat(dust.renderWithJSON(jsTemplate, json), new IgnoreStringWhiteSpaceMatcher("{\r\n" + 
				"  \"founder\": \"Godric Gryffindor\"\r\n" + 
				"}"));
	}

	@Test
	public void testRegisterCustomHelper() throws HelperException {
		String json = "{\"houses\":{\"gryffindor\":{\"founder\":\"Godric Gryffindor\"},\"hufflepuff\":{\"founder\":\"Helga Hufflepuff\"}}}";
		InputStreamReader helperScript = new InputStreamReader(Dust.class.getClassLoader().getResourceAsStream("dust-helper-test.js"));
		dust.registerHelper("dusthelper", helperScript, "testhelper");
		JSTemplate jsTemplate = dust.compile("There is {@testhelper key=\"yes\"/} "
				+ "{@testhelper some=\"valll\"} is a great {@eq value=\"0\" type=\"number\"} class=\"alt-row\"{/eq} thought {/testhelper}"
				+ "{@testhelper}first case{:else}second case{/testhelper} left to do.");
		System.out.println(dust.renderWithJSON(jsTemplate, json));
	}
	
	private class IgnoreStringWhiteSpaceMatcher extends BaseMatcher<CharSequence> {
		
		private String left;
		
		public IgnoreStringWhiteSpaceMatcher(final String left) {
			if (left != null) {
				this.left = left.replaceAll("\\s+","");
			}
		}

		@Override
		public boolean matches(final Object right) {
			if (right instanceof String) {
				return left.equals(String.class.cast(right).replaceAll("\\s+",""));
			}
			return false;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText(left).appendText(" after removing whitespaces");
		}
	}
}
