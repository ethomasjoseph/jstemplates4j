package com.ethomasjoseph.jstemplates4j.base.cache;

import com.ethomasjoseph.jstemplates4j.base.JSTemplate;
import com.ethomasjoseph.jstemplates4j.base.TemplateCache;

public class NoopTemplateCache implements TemplateCache {

	@Override
	public JSTemplate get(String templateSrc) {
		return null;
	}

	@Override
	public boolean put(String templateSrc, JSTemplate jSTemplate) {
		return false;
	}

}
