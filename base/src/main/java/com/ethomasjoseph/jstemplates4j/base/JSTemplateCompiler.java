package com.ethomasjoseph.jstemplates4j.base;

public interface JSTemplateCompiler {
	
	JSTemplate compile(final String templateSrc);
	
	CharSequence renderWithData(final JSTemplate jSTemplate, final Object data);
	
	CharSequence renderWithJSON(final JSTemplate jSTemplate, final String json);
	
	JSONParser getJsonParser();

	/**
	 * Assigns a JSON parser to the JSTemplateCompiler, which would be used to
	 * parse physical JSON so that it could be used for scripting, since
	 * JavaScript require an object representation.
	 * 
	 * @param jsonParser
	 *            JSONParser which would be used for parsing any string
	 *            representation of JSON to a JavaScript object.
	 * @return The JSTemplateCompiler initialized with the given JSONParser
	 */
	JSTemplateCompiler withJsonParser(final JSONParser jsonParser);
	
	JSTemplateCompiler withTemplateCache(final TemplateCache templateCache);
}
