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

import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethomasjoseph.jstemplates4j.base.AbstractJSTemplateCompiler;
import com.ethomasjoseph.jstemplates4j.base.CompilerUtils;
import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.base.cache.MapBasedTemplateCache;
import com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.HelperException;
import com.ethomasjoseph.jstemplates4j.dustjs.compiler.internal.RenderedOutput;

/**
 * Dust templating engine's compiler that runs on Nashorn script engine.
 * 
 * @author Thomas Joseph
 *
 */
@SuppressWarnings("restriction")
public class Dust extends AbstractJSTemplateCompiler {
	private static final Logger LOG = LoggerFactory.getLogger(Dust.class);

	private static final String DUST_MIN_JS = "dust-full.min-v2.7.1.js";
	
	private static final String DUST_WRAPPER_JS = "dust-nashorn-extension.js";
	
	private static final String DUST_HELPER_JS = "dust-helpers.min-v1.7.1.js";

	private static final String DUST = "dust";
	
	private static final String DUST_WRAPPER = "dustwrapper";
	
	private static final String JSON = "JSON";

	private static final String METHOD_COMPILE = "compile";

	private static final String METHOD_LOAD_SOURCE = "loadSource";

	private static final String METHOD_RENDER = "render";

	private static final String METHOD_REGISTER_HELPER = "registerHelper";

	private static final String METHOD_UNREGISTER_HELPER = "unregisterHelper";
	
	/** The dust object */
	private ScriptObjectMirror dust;
	
	private ScriptObjectMirror dustWrapper;
	
	/** The default JSON parser - the JavaScript JSON object. */
	private ScriptObjectMirror json;


	
	/**
	 * Creates a Dust compiler with a built-in Dust version.
	 * 
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	public Dust() throws ScriptException {
		super(new InputStreamReader(Dust.class.getClassLoader().getResourceAsStream(DUST_MIN_JS)));
	}
	
	/**
	 * Creates a Dust compiler with a given script source which represents
	 * the Dust JavaScript library.
	 * 
	 * @param scriptSrc
	 *            represents the Dust JavaScript library source.
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	public Dust(final Reader scriptSrc) throws ScriptException {
		super(scriptSrc);
	}
	
	/**
	 * Initializes the Dust compiler.
	 * 
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	protected void init() throws ScriptException {
		// evaluate dust-helpers script
		InputStreamReader helperScript = new InputStreamReader(Dust.class.getClassLoader().getResourceAsStream(DUST_HELPER_JS));
		engine.eval(helperScript);
		
		// evaluate the dust wrapper script
		InputStreamReader wrapperScriptSrc = new InputStreamReader(Dust.class.getClassLoader().getResourceAsStream(DUST_WRAPPER_JS));
		engine.eval(wrapperScriptSrc);

		// now extracting dust variable from the script engine
		this.dust = ScriptObjectMirror.class.cast(engine.eval(DUST));
		this.dustWrapper = ScriptObjectMirror.class.cast(engine.eval(DUST_WRAPPER));

		json = ScriptObjectMirror.class.cast(engine.eval(JSON));
		
		// Default JSON parser implementation with JavaScript parser
		jsonParser  = (s) -> {
			return json.callMember("parse", s);
		};
		templateCache = new MapBasedTemplateCache();
	}

	// Compile Functions
	@Override
	public JSTemplate compile(final String templateSrc) {
		return compile(templateSrc, CompilerUtils.hash(templateSrc));
	}
	
	public JSTemplate compile(final String templateSrc, final String name) {
		if (null == templateSrc) {
			throw new NullPointerException("Supplied template source for compilation is null");
		}
		
		if (null == name) {
			throw new NullPointerException("Supplied template name for compilation is null");
		}
		JSTemplate jSTemplate = templateCache.get(name);
		if (jSTemplate == null) {
			Object nativeTemplate = this.dust.callMember(METHOD_COMPILE, templateSrc, name);
			jSTemplate = new JSTemplate(nativeTemplate, name);
			this.dust.callMember(METHOD_LOAD_SOURCE, nativeTemplate);
			templateCache.put(name, jSTemplate);
		}
		return jSTemplate;
	}

	@Override
	public CharSequence renderWithData(JSTemplate jSTemplate, Object data) {
		Object renderedOut = dustWrapper.callMember(METHOD_RENDER, jSTemplate, data);
		RenderedOutput renderedOutput = RenderedOutput.class.cast(renderedOut);
		if (renderedOutput.getError() != null && !renderedOutput.getError().trim().isEmpty()) {
			LOG.error(renderedOutput.getError());
		}
		return renderedOutput.getOutput();
	}

	public <T> Dust registerJSHelper(final Reader source, final String namespace, final String... helperName) throws HelperException {
		try {
			registerJavaScript(source);
			ScriptObjectMirror helpers = ScriptObjectMirror.class.cast(engine.eval(namespace));
			this.dustWrapper.callMember(METHOD_REGISTER_HELPER, helpers, (Object[])helperName);
			return this;
		} catch (ScriptException e) {
			throw new HelperException(e);
		}
	}
	
	public <T> Dust unregisterHelper(final String helperName) {
		this.dustWrapper.callMember(METHOD_UNREGISTER_HELPER, helperName);
		return this;
	}
}
