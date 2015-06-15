/*
 * Copyright 2015 Thomas Joseph
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * @author Thomas Joseph
 *
 */
@SuppressWarnings("restriction")
public class Chunk {
	private ScriptObjectMirror mirror;
	
    private Stub root;
    private Chunk next;
    private List<String> data = null;
    private boolean flushable = false;
    private List<Tap> taps;
    private String error;
    
    /**
     * 
     * @param mirror
     */
	public Chunk(final ScriptObjectMirror mirror) {
		super();
		this.mirror = mirror;
	}

	/**
	 * @param stub
	 */
	public Chunk(Stub root) {
		this.root = root;
	}
	
	/**
	 * 
	 * @param root
	 * @param next
	 * @param taps
	 */
	public Chunk(Stub root, Chunk next, List<Tap> taps) {
	    this.root = root;
	    this.next = next;
	    this.data = new ArrayList<>();
	    this.flushable = false;
	    this.taps = taps;
	}
	
	  /*
	   * 
	write	function	 	function (a){var b=this.taps;return b&&(a=b.go(a)),this.data.push(a),this}
	end	function	 	function (a){return a&&this.write(a),this.flushable=!0,this.root.flush(),this}
	map	function	 	function (a){var b=new Chunk(this.root,this.next,this.taps),c=new Chunk(this.root,b,this.taps);this.next=c,this.flushable=!0;try{a(c)}catch(d){dust.log(d,ERROR),c.setError(d)}return b}
	tap	function	 	function (a){var b=this.taps;return this.taps=b?b.push(a):new Tap(a),this}
	untap	function	 	function (){return this.taps=this.taps.tail,this}
	render	function	 	function (a,b){return a(this,b)}
	reference	function	 	function (a,b,c,d){return"function"==typeof a?(a=a.apply(b.current(),[this,b,null,{auto:c,filters:d}]),a instanceof Chunk?a:this.reference(a,b,c,d)):dust.isThenable(a)?this.await(a,b,null,c,d):dust.isStreamable(a)?this.stream(a,b,null,c,d):dust.isEmpty(a)?this:this.write(dust.filter(a,c,d,b))}
	section	function	 	function (a,b,c,d){var e,f,g,h=c.block,i=c["else"],j=this;if("function"==typeof a&&!dust.isTemplateFn(a)){try{a=a.apply(b.current(),[this,b,c,d])}catch(k){return dust.log(k,ERROR),this.setError(k)}if(a instanceof Chunk)return a}if(dust.isEmptyObject(c))return j;if(dust.isEmptyObject(d)||(b=b.push(d)),dust.isArray(a)){if(h){if(f=a.length,f>0){for(g=b.stack&&b.stack.head||{},g.$len=f,e=0;f>e;e++)g.$idx=e,j=h(j,b.push(a[e],e,f));return g.$idx=void 0,g.$len=void 0,j}if(i)return i(this,b)}}else{if(dust.isThenable(a))return this.await(a,b,c);if(dust.isStreamable(a))return this.stream(a,b,c);if(a===!0){if(h)return h(this,b)}else if(a||0===a){if(h)return h(this,b.push(a))}else if(i)return i(this,b)}return dust.log("Section without corresponding key in template `"+b.getTemplateName()+"`",DEBUG),this}
	exists	function	 	function (a,b,c){var d=c.block,e=c["else"];if(dust.isEmpty(a)){if(e)return e(this,b)}else{if(d)return d(this,b);dust.log("No block for exists check in template `"+b.getTemplateName()+"`",DEBUG)}return this}
	notexists	function	 	function (a,b,c){var d=c.block,e=c["else"];if(dust.isEmpty(a)){if(d)return d(this,b);dust.log("No block for not-exists check in template `"+b.getTemplateName()+"`",DEBUG)}else if(e)return e(this,b);return this}
	block	function	 	function (a,b,c){var d=a||c.block;return d?d(this,b):this}
	partial	function	 	function (a,b,c,d){var e;return void 0===d&&(d=c,c=b),dust.isEmptyObject(d)||(c=c.clone(),e=c.pop(),c=c.push(d).push(e)),dust.isTemplateFn(a)?this.capture(a,b,function(a,b){c.templateName=a,load(a,b,c).end()}):(c.templateName=a,load(a,this,c))}
	helper	function	 	function (a,b,c,d,e){var f,g=this,h=d.filters;if(void 0===e&&(e="h"),!dust.helpers[a])return dust.log("Helper `"+a+"` does not exist",WARN),g;try{return f=dust.helpers[a](g,b,c,d),f instanceof Chunk?f:("string"==typeof h&&(h=h.split("|")),dust.isEmptyObject(c)?g.reference(f,b,e,h):g.section(f,b,c,d))}catch(i){return dust.log("Error in helper `"+a+"`: "+i.message,ERROR),g.setError(i)}}
	await	function	 	function (a,b,c,d,e){return this.map(function(f){a.then(function(a){f=c?f.section(a,b,c):f.reference(a,b,d,e),f.end()},function(a){var d=c&&c.error;d?f.render(d,b.push(a)).end():(dust.log("Unhandled promise rejection in `"+b.getTemplateName()+"`",INFO),f.end())})})}
	stream	function	 	function (a,b,c,d,e){var f=c&&c.block,g=c&&c.error;return this.map(function(h){var i=!1;a.on("data",function(a){i||(f?h=h.map(function(c){c.render(f,b.push(a)).end()}):c||(h=h.reference(a,b,d,e)))}).on("error",function(a){i||(g?h.render(g,b.push(a)):dust.log("Unhandled stream error in `"+b.getTemplateName()+"`",INFO),i||(i=!0,h.end()))}).on("end",function(){i||(i=!0,h.end())})})}
	capture	function	 	function (a,b,c){return this.map(function(d){var e=new Stub(function(a,b){a?d.setError(a):c(b,d)});a(e.head,b).end()})}
	setError	function	 	function (a){return this.error=a,this.root.flush(),this}
	   */
	
	public Chunk write(String data) {
		mirror.callMember("write", data);
		return this;
	}
	
	// ********************** GETTERS & SETTERS ********************************
	public ScriptObjectMirror getMirror() {
		return mirror;
	}

	public Stub getRoot() {
		return root;
	}

	public Chunk getNext() {
		return next;
	}

	public List<String> getData() {
		return data;
	}

	public boolean isFlushable() {
		return flushable;
	}

	public List<Tap> getTaps() {
		return taps;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
