package com.ethomasjoseph.jstemplates4j.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CompilerUtils {
	
    /** 
     * Used building output as Hex 
     */
    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
           '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    
	public static final String hash(final String message) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] data = digest.digest(message.getBytes());

			int l = data.length;
			char[] out = new char[l << 1];

			// two characters form the hex value.
			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
				out[j++] = DIGITS[0x0F & data[i]];
			}
			return new String(out);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
