/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.codec.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;

public final class HmacUtils {
    private static final int STREAM_BUFFER_LENGTH = 1024;

    public static Mac getHmacMd5(byte[] key) {
        return HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_MD5, key);
    }

    public static Mac getHmacSha1(byte[] key) {
        return HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_1, key);
    }

    public static Mac getHmacSha256(byte[] key) {
        return HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key);
    }

    public static Mac getHmacSha384(byte[] key) {
        return HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_384, key);
    }

    public static Mac getHmacSha512(byte[] key) {
        return HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_512, key);
    }

    public static Mac getInitializedMac(HmacAlgorithms algorithm, byte[] key) {
        return HmacUtils.getInitializedMac(algorithm.toString(), key);
    }

    public static Mac getInitializedMac(String algorithm, byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            return mac;
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacMd5(byte[] key, byte[] valueToDigest) {
        try {
            return HmacUtils.getHmacMd5(key).doFinal(valueToDigest);
        }
        catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacMd5(byte[] key, InputStream valueToDigest) throws IOException {
        return HmacUtils.updateHmac(HmacUtils.getHmacMd5(key), valueToDigest).doFinal();
    }

    public static byte[] hmacMd5(String key, String valueToDigest) {
        return HmacUtils.hmacMd5(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
    }

    public static String hmacMd5Hex(byte[] key, byte[] valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacMd5(key, valueToDigest));
    }

    public static String hmacMd5Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(HmacUtils.hmacMd5(key, valueToDigest));
    }

    public static String hmacMd5Hex(String key, String valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacMd5(key, valueToDigest));
    }

    public static byte[] hmacSha1(byte[] key, byte[] valueToDigest) {
        try {
            return HmacUtils.getHmacSha1(key).doFinal(valueToDigest);
        }
        catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacSha1(byte[] key, InputStream valueToDigest) throws IOException {
        return HmacUtils.updateHmac(HmacUtils.getHmacSha1(key), valueToDigest).doFinal();
    }

    public static byte[] hmacSha1(String key, String valueToDigest) {
        return HmacUtils.hmacSha1(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
    }

    public static String hmacSha1Hex(byte[] key, byte[] valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha1(key, valueToDigest));
    }

    public static String hmacSha1Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(HmacUtils.hmacSha1(key, valueToDigest));
    }

    public static String hmacSha1Hex(String key, String valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha1(key, valueToDigest));
    }

    public static byte[] hmacSha256(byte[] key, byte[] valueToDigest) {
        try {
            return HmacUtils.getHmacSha256(key).doFinal(valueToDigest);
        }
        catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacSha256(byte[] key, InputStream valueToDigest) throws IOException {
        return HmacUtils.updateHmac(HmacUtils.getHmacSha256(key), valueToDigest).doFinal();
    }

    public static byte[] hmacSha256(String key, String valueToDigest) {
        return HmacUtils.hmacSha256(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
    }

    public static String hmacSha256Hex(byte[] key, byte[] valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha256(key, valueToDigest));
    }

    public static String hmacSha256Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(HmacUtils.hmacSha256(key, valueToDigest));
    }

    public static String hmacSha256Hex(String key, String valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha256(key, valueToDigest));
    }

    public static byte[] hmacSha384(byte[] key, byte[] valueToDigest) {
        try {
            return HmacUtils.getHmacSha384(key).doFinal(valueToDigest);
        }
        catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacSha384(byte[] key, InputStream valueToDigest) throws IOException {
        return HmacUtils.updateHmac(HmacUtils.getHmacSha384(key), valueToDigest).doFinal();
    }

    public static byte[] hmacSha384(String key, String valueToDigest) {
        return HmacUtils.hmacSha384(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
    }

    public static String hmacSha384Hex(byte[] key, byte[] valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha384(key, valueToDigest));
    }

    public static String hmacSha384Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(HmacUtils.hmacSha384(key, valueToDigest));
    }

    public static String hmacSha384Hex(String key, String valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha384(key, valueToDigest));
    }

    public static byte[] hmacSha512(byte[] key, byte[] valueToDigest) {
        try {
            return HmacUtils.getHmacSha512(key).doFinal(valueToDigest);
        }
        catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hmacSha512(byte[] key, InputStream valueToDigest) throws IOException {
        return HmacUtils.updateHmac(HmacUtils.getHmacSha512(key), valueToDigest).doFinal();
    }

    public static byte[] hmacSha512(String key, String valueToDigest) {
        return HmacUtils.hmacSha512(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
    }

    public static String hmacSha512Hex(byte[] key, byte[] valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha512(key, valueToDigest));
    }

    public static String hmacSha512Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(HmacUtils.hmacSha512(key, valueToDigest));
    }

    public static String hmacSha512Hex(String key, String valueToDigest) {
        return Hex.encodeHexString(HmacUtils.hmacSha512(key, valueToDigest));
    }

    public static Mac updateHmac(Mac mac, byte[] valueToDigest) {
        mac.reset();
        mac.update(valueToDigest);
        return mac;
    }

    public static Mac updateHmac(Mac mac, InputStream valueToDigest) throws IOException {
        mac.reset();
        byte[] buffer = new byte[1024];
        int read = valueToDigest.read(buffer, 0, 1024);
        while (read > -1) {
            mac.update(buffer, 0, read);
            read = valueToDigest.read(buffer, 0, 1024);
        }
        return mac;
    }

    public static Mac updateHmac(Mac mac, String valueToDigest) {
        mac.reset();
        mac.update(StringUtils.getBytesUtf8(valueToDigest));
        return mac;
    }
}

