package com.ethomasjoseph.jstemplates4j.handlebars.compiler;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import com.ethomasjoseph.jstemplates4j.base.AbstractJSTemplateCompiler;
import com.ethomasjoseph.jstemplates4j.base.CompilerUtils;
import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.base.TemplateCache;
import com.ethomasjoseph.jstemplates4j.base.cache.MapBasedTemplateCache;
import com.ethomasjoseph.jstemplates4j.handlebars.compiler.helper.HandlebarsHelper;

/**
 * Handlebars templating engine's compiler that runs on Nashorn script engine.
 * 
 * @author Thomas Joseph
 *
 */
@SuppressWarnings("restriction")
public class Handlebars extends AbstractJSTemplateCompiler {
	private static final String HANDLEBARS_MIN_JS = "handlebars-v3.0.3.js";
	
	private static final String HANDLEBARS_WRAPPER_JS = "handlebars-nashorn-extension.js";

	private static final String HANDLEBARS = "Handlebars";
	
	private static final String HB_WRAPPER = "hbwrapper";
	
	private static final String JSON = "JSON";

	private static final String NASHORN = "nashorn";

	private static final String METHOD_COMPILE = "compile";
	
	private static final String METHOD_REGISTER_HELPER = "registerHelper";
	
	private static final String METHOD_UNREGISTER_HELPER = "unregisterHelper";
	
	/** The Handlebars object */
	private ScriptObjectMirror handlebars = null;
	
	/** The Handlebars Wrapper object */
	private ScriptObjectMirror hbwrapper = null;
	
	/** The default JSON parser - the JavaScript JSON object. */
	private ScriptObjectMirror json = null;
	
	private ScriptEngine engine;
	
	private TemplateCache templateCache = null;
	
	/**
	 * Creates a Handlebars compiler with a built-in Handlebars version.
	 * 
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	public Handlebars() throws ScriptException {
		this(new InputStreamReader(Handlebars.class.getClassLoader().getResourceAsStream(HANDLEBARS_MIN_JS)));
	}
	
	/**
	 * Creates a Handlebars compiler with a given script source which represents
	 * the Handlebars JavaScript library.
	 * 
	 * @param scriptSrc
	 *            represents the Handlebars JavaScript library source.
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	public Handlebars(final Reader scriptSrc) throws ScriptException {
		init(scriptSrc);
	}
	
	/**
	 * Initializes the Handlebars compiler.
	 * 
	 * @param scriptSrc
	 *            represents the Handlebars JavaScript library source.
	 * @throws ScriptException
	 *             when some error occurs in reading the script or initializing
	 *             the script.
	 */
	void init(final Reader scriptSrc) throws ScriptException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
	    engine = engineManager.getEngineByName(NASHORN);
	    
	    //engine.eval("load(\"nashorn:mozilla_compat.js\");");

    	// evaluate handlebars script
		engine.eval(scriptSrc);
		
		// evaluate the handlebars wrapper script
		InputStreamReader wrapperScriptSrc = new InputStreamReader(Handlebars.class.getClassLoader().getResourceAsStream(HANDLEBARS_WRAPPER_JS));
		engine.eval(wrapperScriptSrc);

		// now extracting handlebars variable from the script engine
		handlebars = ScriptObjectMirror.class.cast(engine.eval(HANDLEBARS));
		hbwrapper = ScriptObjectMirror.class.cast(engine.eval(HB_WRAPPER));
		json = ScriptObjectMirror.class.cast(engine.eval(JSON));
		
		// Default JSON parser implementation with JavaScript parser
		jsonParser  = (s) -> {
			return json.callMember("parse", s);
		};
		templateCache = new MapBasedTemplateCache();
	}
	
	// Compile Functions
	public JSTemplate compile(final String templateSrc) {
		if (null == templateSrc) {
			throw new NullPointerException("Supplied JSTemplate Source for compilation is null");
		}
		String messageHash = CompilerUtils.hash(templateSrc);
		JSTemplate jSTemplate = templateCache.get(messageHash);
		if (jSTemplate == null) {
			jSTemplate = new JSTemplate(handlebars.callMember(METHOD_COMPILE, templateSrc), messageHash);
			templateCache.put(messageHash, jSTemplate);
		}
		return jSTemplate;
	}

	@Override
	public CharSequence renderWithData(JSTemplate jSTemplate, Object data) {
		return String.class.cast(jSTemplate.getNativeTemplateExecutable().call(null, data));
	}
	
	public <T> Handlebars registerHelper(final String helperName, final HandlebarsHelper<T> helper) {
		hbwrapper.callMember(METHOD_REGISTER_HELPER, helperName, helper);
		return this;
	}
	
	public <T> Handlebars unregisterHelper(final String helperName) {
		handlebars.callMember(METHOD_UNREGISTER_HELPER, helperName);
		return this;
	}
}
