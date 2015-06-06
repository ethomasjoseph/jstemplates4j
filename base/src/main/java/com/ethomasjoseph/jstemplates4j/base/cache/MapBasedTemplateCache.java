package com.ethomasjoseph.jstemplates4j.base.cache;

import java.util.HashMap;
import java.util.Map;

import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.base.TemplateCache;

public class MapBasedTemplateCache implements TemplateCache {
	Map<String, JSTemplate> templateMap = new HashMap<>();

	@Override
	public JSTemplate get(String templateSrc) {
		return templateMap.get(templateSrc);
	}

	@Override
	public boolean put(String templateSrc, JSTemplate jSTemplate) {
		return false;
	}
	
}
