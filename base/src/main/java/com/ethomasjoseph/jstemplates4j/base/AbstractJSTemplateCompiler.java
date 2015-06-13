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
package com.ethomasjoseph.jstemplates4j.base;

import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Abstracts common functionalities required for a JSTemplateCompiler.
 * 
 * @author Thomas Joseph
 *
 */
public abstract class AbstractJSTemplateCompiler implements JSTemplateCompiler {
	private static final String NASHORN = "nashorn";

	protected JSONParser jsonParser;

	protected TemplateCache templateCache;

	protected ScriptEngine engine;

	public AbstractJSTemplateCompiler(final Reader scriptSrc)
			throws ScriptException {
		preInit(scriptSrc);
		init();
	}

	protected void preInit(final Reader scriptSrc) throws ScriptException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		engine = engineManager.getEngineByName(NASHORN);
		registerJavaScript(scriptSrc);
	}

	protected abstract void init() throws ScriptException;

	@Override
	public void registerJavaScript(final Reader reader) throws ScriptException {
		engine.eval(reader);
	}

	@Override
	public CharSequence renderWithJSON(JSTemplate jSTemplate, String json) {
		return renderWithData(jSTemplate, jsonParser.parse(json));
	}

	@Override
	public JSTemplateCompiler withJsonParser(final JSONParser jsonParser) {
		if (null == jsonParser) {
			throw new NullPointerException("Supplied JSONParsor is null");
		}
		this.jsonParser = jsonParser;
		return this;
	}

	@Override
	public JSTemplateCompiler withTemplateCache(TemplateCache templateCache) {
		if (null == templateCache) {
			throw new NullPointerException("Supplied Template Cache is null.");
		}
		this.templateCache = templateCache;
		return this;
	}

}
