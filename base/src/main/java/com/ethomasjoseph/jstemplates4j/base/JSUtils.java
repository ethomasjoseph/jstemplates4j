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
package com.ethomasjoseph.jstemplates4j.base;

import java.util.List;

/**
 * @author Thomas Joseph
 *
 */
public class JSUtils {
	private static final String JOIN_SEPARATOR = ",";
	
	public static final String arrayJoin(List<String> arrayAsList) {
		return arrayJoin(arrayAsList, JOIN_SEPARATOR);
	}
	
	public static final String arrayJoin(List<String> arrayAsList, String separator) {
		return arrayJoin(arrayAsList.toArray(new String[]{}));
	}
	
	public static final String arrayJoin(String... strings) {
		return arrayJoin(strings, JOIN_SEPARATOR);
	}
	
	public static final String arrayJoin(String[] strings, String separator) {
        StringBuilder b = new StringBuilder();
        if (null != strings && strings.length > 1) {
            int max = strings.length - 1;
            for (int i = 0; ; i++) {
                b.append(strings[i]);
                if (i == max) {
                    break;
                }
                b.append(separator);
            }
        }
        return b.toString();
	}
}
