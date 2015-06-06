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
package com.ethomasjoseph.jstemplates4j.handlebars.compiler;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.junit.Before;
import org.junit.Test;

import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.handlebars.compiler.Handlebars;
import com.ethomasjoseph.jstemplates4j.handlebars.compiler.testvo.TestObject;

/**
 * 
 * @author Thomas Joseph
 *
 */
public class HandlebarsTest {
	
	
	private Handlebars handlebars;
	private TestObject to;
	
	@Before
	public void setup() throws ScriptException {
		handlebars = new Handlebars();
		to = new TestObject();
	}

	@Test
	/**
	 * Testing the constructor which accepts a user supplied handlebars script
	 * 
	 * @throws ScriptException
	 */
	public void testConstructorWithSuppliedHandlebarsScript() throws ScriptException {
		String handlebarsMinJS = "handlebars-v3.0.3.js";
		InputStreamReader scriptSrc = new InputStreamReader(
				HandlebarsTest.class.getClassLoader().getResourceAsStream(handlebarsMinJS));
		new Handlebars(scriptSrc);
	}
	
	@Test(expected = ScriptException.class)
	/**
	 * Testing the constructor which accepts a user supplied wrong script.
	 * 
	 * @throws ScriptException
	 */
	public void testConstructorWithSuppliedWrongScript() throws ScriptException {
		InputStreamReader scriptSrc = new InputStreamReader(new ByteArrayInputStream("a wrong script".getBytes()));
		new Handlebars(scriptSrc);
	}
	
	
	@Test
	public void testRenderingWithString() {
		JSTemplate template = handlebars.compile("Hello {{this}}");
		assertEquals("Hello Handlebars" , handlebars.renderWithData(template, "Handlebars"));
	}
	
	@Test
	public void testRenderingWithSimpleObject() {
		to.setUsername("handlebars");
		JSTemplate template = handlebars.compile("Hello {{username}}");
		assertEquals("Hello handlebars" , handlebars.renderWithData(template, to));
	}
	
	@Test
	public void testRenderingWithJSONString() {
		JSTemplate template = handlebars.compile("Hello {{username}}");
		assertEquals("Hello JSON Data" , handlebars.renderWithJSON(template, "{ \"username\" : \"JSON Data\"}"));
	}
	
	@Test
	public void testRenderingWithMap() {
		Map<String, String> map = new HashMap<>();
		map.put("username", "Mapped Value");
		JSTemplate template = handlebars.compile("Hello {{username}}");
		assertEquals("Hello Mapped Value" ,handlebars.renderWithData(template, map));
		
	}
	
	@Test
	public void testRegisterSimpleHelper() {
		List<String> list = new ArrayList<>();
		list.add("Delhi");
		list.add("Mumbai");
		list.add("Chennai");
		list.add("Kolkota");
		handlebars.registerHelper("echonow", (context, options) -> {
			assertEquals(list, context);
			assertEquals("echonow", options.getName());
			return context.toString();
		});

		to.setList(list);
		JSTemplate template = handlebars.compile("Hello {{echonow list}}");
		assertEquals("Hello " + list.toString() ,handlebars.renderWithData(template, to));
	}
	
	@Test
	public void testRegisterBlockHelper() {
		List<String> list = new ArrayList<>();
		list.add("Delhi");
		list.add("Mumbai");
		list.add("Chennai");
		list.add("Kolkota");
		handlebars.registerHelper("testblock", (context, options) -> {
			assertEquals(list, context);
			assertEquals("testblock", options.getName());
			return "boom!";
		});

		to.setList(list);
		JSTemplate template = handlebars.compile("Hello {{#testblock list}}{{this}}{{/testblock}}");
		assertEquals("Hello boom!" ,handlebars.renderWithData(template, to));
	}
	
	// Tests for various built-in helpers
	@Test
	public void testEachHelperWithList() {
		List<String> list = new ArrayList<>();
		list.add("Delhi");
		list.add("Mumbai");
		list.add("Chennai");
		list.add("Kolkota");
		to.setList(list);
		JSTemplate template = handlebars.compile("Hello list {{#each list}}{{@this}}, {{/each}}!");
		assertEquals("Hello list Delhi, Mumbai, Chennai, Kolkota, !", handlebars.renderWithData(template, to));
		template = handlebars.compile("Hello list {{#each list}}{{this}}, {{/each}}!");
		assertEquals("Hello list Delhi, Mumbai, Chennai, Kolkota, !", handlebars.renderWithData(template, to));
		template = handlebars.compile("Hello list {{#each list}}{{.}}, {{/each}}!");
		assertEquals("Hello list Delhi, Mumbai, Chennai, Kolkota, !", handlebars.renderWithData(template, to));
	}
	
	@Test
	public void testBuiltinLoopingWithList() {
		SimpleBindings bindings = new SimpleBindings();
		List<String> list = new ArrayList<>();
		list.add("Delhi");
		list.add("Mumbai");
		list.add("Chennai");
		list.add("Kolkota");
		to.setList(list);
		bindings.put("context", to);
		
		JSTemplate template = handlebars.compile("Hello list {{#context.list}}{{this}}, {{/context.list}}!");
		assertEquals("Hello list Delhi, Mumbai, Chennai, Kolkota, !", handlebars.renderWithData(template, bindings));
		
		template = handlebars.compile("Hello list {{#list}}{{this}}, {{/list}}!");
		assertEquals("Hello list Delhi, Mumbai, Chennai, Kolkota, !", handlebars.renderWithData(template, to));
	}
	
	@Test
	public void testIfHelper() {
		to.setBool(true);
		JSTemplate template = handlebars.compile("Hello bool {{#if bool}}smile{{/if}}");
		assertEquals("Hello bool smile", handlebars.renderWithData(template, to));
		
		to.setBool(false);
		assertEquals("Hello bool ", handlebars.renderWithData(template, to));
	}
	

}
