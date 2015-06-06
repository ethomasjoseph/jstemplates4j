package com.ethomasjoseph.jstemplates4j.handlebars.compiler.testvo;

import java.util.List;

public class TestObject {
	private String username;
	private List<?> list;
	private boolean bool;

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
	
}
