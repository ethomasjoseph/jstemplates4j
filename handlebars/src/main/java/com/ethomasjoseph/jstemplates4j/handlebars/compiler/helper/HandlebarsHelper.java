package com.ethomasjoseph.jstemplates4j.handlebars.compiler.helper;

import com.ethomasjoseph.jstemplates4j.handlebars.compiler.Handlebars;


/**
 * /**
 * Handlebars helpers can be accessed from any context in a nativeTemplate. You can
 * register a helper with the {@link Handlebars#registerHelper(String, HandlebarsHelper)}
 * method.
 * 
 * @author Thomas Joseph
 *
 * @param <T>
 */
public interface HandlebarsHelper<T> {
	
	/**
	 * 
	 * @param context The context object.
	 * @param options The options object.
	 * @return A string result.
	 * @throws HelperException If a nativeTemplate cannot be loaded.
	 */
	CharSequence apply(T context, Options options) throws HelperException;

}
