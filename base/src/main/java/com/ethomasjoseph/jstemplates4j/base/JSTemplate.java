package com.ethomasjoseph.jstemplates4j.base;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Represents the compiled nativeTemplate of the JSTemplateCompiler in consideration.
 * 
 * @author Thomas Joseph
 *
 */
@SuppressWarnings("restriction")
public class JSTemplate {
	protected Object nativeTemplate = null;
	private final String name;
	
	public JSTemplate(final Object nativeTemplate, final String name) {
		if (null == nativeTemplate || null == name)	{
			throw new IllegalArgumentException("Either of nativeTemplate or name is invalid.");
		}
		this.nativeTemplate = nativeTemplate;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ScriptObjectMirror getNativeTemplateExecutable() {
		if ( nativeTemplate instanceof ScriptObjectMirror) {
			return (ScriptObjectMirror) nativeTemplate;
		}
		return null;
	}

	public Object getNativeTemplate() {
		return nativeTemplate;
	}
}