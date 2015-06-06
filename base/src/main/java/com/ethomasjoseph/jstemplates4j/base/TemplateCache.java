package com.ethomasjoseph.jstemplates4j.base;


public interface TemplateCache {
	JSTemplate get(final String templateSrc);
	
	boolean put(final String templateSrc, final JSTemplate jSTemplate);
}