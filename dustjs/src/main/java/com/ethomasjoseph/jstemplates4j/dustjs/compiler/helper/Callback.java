package com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper;

import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Callback {
	private static final Logger LOG = LoggerFactory.getLogger(Callback.class);
	
	private Writer output;
	private Writer error;

	public Callback(Writer output, Writer error) {
		super();
		this.output = output;
		this.error = error;
	}
	
	public void write(String out, String err) {
		writeOutput(out);
		writeError(err);
	}
	
	public void writeOutput(String text) {
		if (output instanceof Writer) {
			try {
				output.write(text);
			} catch (IOException e) {
				LOG.error("Error with writing output stream.", e);
			}
		}
	}
	
	public void writeError(String text) {
		if (error instanceof Writer) {
			try {
				error.write(text);
			} catch (IOException e) {
				LOG.error("Error with writing error stream.", e);
			}
		}
	}
}
