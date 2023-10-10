/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.codec.digest;

import java.util.Random;

class B64 {
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    B64() {
    }

    static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer) {
        int w2 = b2 << 16 & 0xFFFFFF | b1 << 8 & 0xFFFF | b0 & 0xFF;
        int n2 = outLen;
        while (n2-- > 0) {
            buffer.append(B64T.charAt(w2 & 0x3F));
            w2 >>= 6;
        }
    }

    static String getRandomSalt(int num) {
        StringBuilder saltString = new StringBuilder();
        for (int i2 = 1; i2 <= num; ++i2) {
            saltString.append(B64T.charAt(new Random().nextInt(B64T.length())));
        }
        return saltString.toString();
    }
}

