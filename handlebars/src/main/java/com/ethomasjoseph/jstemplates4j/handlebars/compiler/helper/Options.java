package com.ethomasjoseph.jstemplates4j.handlebars.compiler.helper;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("restriction")
public class Options {
	private ScriptObjectMirror scriptObjectMirror;
	
	private String name;
	private Object data;
	private Object hash;
	
	public Options(final ScriptObjectMirror scriptObjectMirror) {
		this.scriptObjectMirror = scriptObjectMirror;
	}

	public String getName() {
		return null == name ? name = (String) scriptObjectMirror.getMember("name") : name;
	}

	public Object getHash() {
		return null == hash ? hash = (String) scriptObjectMirror.getMember("hash") : hash;
	}

	public Object getData() {
		return null == data ? data = (String) scriptObjectMirror.getMember("data") : data;
	}	

}
