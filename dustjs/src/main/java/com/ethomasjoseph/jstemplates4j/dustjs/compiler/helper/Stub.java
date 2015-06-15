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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethomasjoseph.jstemplates4j.base.JSUtils;

/**
 * @author Thomas Joseph
 *
 */
public class Stub {
	private static final Logger LOG = LoggerFactory.getLogger(Stub.class);
	private Callback callback;
	private Chunk head;
	private String out;
	private boolean disableFlush = false;

	public Stub(final Callback callback) {
		this.callback = callback;
		this.head = new Chunk(this);
		this.out = "";
	}

	public Callback getCallback() {
		return callback;
	}
	
	public Chunk getHead() {
		return head;
	}
	
	public void flush() {
		if (disableFlush) {
			return;
		}
	    Chunk chunk = this.head;

	    while (chunk != null) {
	      if (chunk.isFlushable()) {
	        this.out.concat(JSUtils.arrayJoin(chunk.getData(), ""));
	      } else if (chunk.getError() != null) {
	        this.getCallback().writeOutput(chunk.getError());
	        LOG.error("Rendering failed with error `" + chunk.getError() + "`");
	        this.disableFlush = true;
	        return;
	      } else {
	        return;
	      }
	      chunk = chunk.getNext();
	      this.head = chunk;
	    }
	    this.getCallback().writeError(this.out);
	}
}

