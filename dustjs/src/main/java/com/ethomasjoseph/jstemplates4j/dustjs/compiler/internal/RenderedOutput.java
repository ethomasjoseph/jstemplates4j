package com.ethomasjoseph.jstemplates4j.dustjs.compiler.internal;

public class RenderedOutput {
	private String error;
	private String output;
	
	public RenderedOutput(String error, String output) {
		super();
		this.error = error;
		this.output = output;
	}
	
	public String getError() {
		return error;
	}
	public String getOutput() {
		return output;
	}
}
