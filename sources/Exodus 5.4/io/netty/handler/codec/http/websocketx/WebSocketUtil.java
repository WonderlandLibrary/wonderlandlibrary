/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class WebSocketUtil {
    static int randomNumber(int n, int n2) {
        return (int)(Math.random() * (double)n2 + (double)n);
    }

    private WebSocketUtil() {
    }

    static byte[] md5(byte[] byArray) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return messageDigest.digest(byArray);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InternalError("MD5 not supported on this platform - Outdated?");
        }
    }

    static String base64(byte[] byArray) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(byArray);
        ByteBuf byteBuf2 = Base64.encode(byteBuf);
        String string = byteBuf2.toString(CharsetUtil.UTF_8);
        byteBuf2.release();
        return string;
    }

    static byte[] randomBytes(int n) {
        byte[] byArray = new byte[n];
        for (int i = 0; i < n; ++i) {
            byArray[i] = (byte)WebSocketUtil.randomNumber(0, 255);
        }
        return byArray;
    }

    static byte[] sha1(byte[] byArray) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            return messageDigest.digest(byArray);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InternalError("SHA-1 is not supported on this platform - Outdated?");
        }
    }
}

