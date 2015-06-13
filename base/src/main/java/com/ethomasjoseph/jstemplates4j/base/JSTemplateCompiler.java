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

import javax.script.ScriptException;

/**
 * 
 * @author Thomas Joseph
 *
 */
public interface JSTemplateCompiler {
	
	/**
	 * Compiles the specified JavaScript template string to the compiled
	 * {@link JSTemplate}.
	 * 
	 * @param templateSrc
	 *            - the JavaScript template source to be compiled.
	 * @return the compiled {@link JSTemplate}.
	 */
	JSTemplate compile(final String templateSrc);
	
	/**
	 * Registers and evaluates the supplied JavaScript against the core
	 * scripting engine (Nashorn).
	 * 
	 * @param src
	 *            - the JavaScript source to be registered
	 * @throws ScriptException
	 */
	void registerJavaScript(final Reader src) throws ScriptException ;
	
	/**
	 * Renders the output against the given compiled {@link JSTemplate} and
	 * context data.
	 * 
	 * @param jSTemplate
	 *            - the compiled {@link JSTemplate}.
	 * @param context
	 *            - the context data.
	 * @return the output after applying the context data to the given
	 *         {@link JSTemplate}.
	 */
	CharSequence renderWithData(final JSTemplate jSTemplate, final Object context);
	
	/**
	 * Renders the output against the given compiled {@link JSTemplate} and
	 * context data as a JSON String.
	 * 
	 * @param jSTemplate
	 *            - the compiled {@link JSTemplate}.
	 * @param context
	 *            - the context data as JSON String.
	 * @return the output after applying the context data JSON to the given
	 *         {@link JSTemplate}.
	 */
	CharSequence renderWithJSON(final JSTemplate jSTemplate, final String json);

	/**
	 * Assigns a {@link JSONParser} to the {@link JSTemplateCompiler}, which
	 * would be used to parse physical JSON so that it could be used for
	 * scripting, since JavaScript require an object representation.
	 * 
	 * @param jsonParser
	 *            {@link JSONParser} which would be used for parsing any string
	 *            representation of JSON to a JavaScript object.
	 * @return The JSTemplateCompiler initialized with the given
	 *         {@link JSONParser}
	 */
	JSTemplateCompiler withJsonParser(final JSONParser jsonParser);

	/**
	 * Assigns a {@link TemplateCache} to the {@link JSTemplateCompiler}, which
	 * would be used to lookup compiled {@link JSTemplate}s, when a template
	 * source is provided for compilation. If the compiled {@link JSTemplate} is
	 * found in the cache, it is used, otherwise a new {@link JSTemplate} is
	 * compiled and added to the cache for further use.
	 * 
	 * @param templateCache
	 *            {@link TemplateCache} which would be used to perform cache
	 *            lookup for a compiled {@link JSTemplate} when the compiler is
	 *            supplied with a template source.
	 * @return the {@link JSTemplateCompiler} initialized with the given
	 *         {@link TemplateCache}.
	 */
	JSTemplateCompiler withTemplateCache(final TemplateCache templateCache);
}
