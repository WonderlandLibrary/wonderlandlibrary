/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.UnknownSocksRequest;
import io.netty.handler.codec.socks.UnknownSocksResponse;
import io.netty.util.internal.StringUtil;

final class SocksCommonUtils {
    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final char ipv6hextetSeparator = ':';
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final char[] ipv6conseqZeroFiller;
    public static final SocksResponse UNKNOWN_SOCKS_RESPONSE;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int XOR_DEFAULT_VALUE = 255;
    public static final SocksRequest UNKNOWN_SOCKS_REQUEST;

    public static String intToIp(int n) {
        return String.valueOf(n >> 24 & 0xFF) + '.' + (n >> 16 & 0xFF) + '.' + (n >> 8 & 0xFF) + '.' + (n & 0xFF);
    }

    private static void ipv6toStr(StringBuilder stringBuilder, byte[] byArray, int n, int n2) {
        int n3;
        --n2;
        for (n3 = n; n3 < n2; ++n3) {
            SocksCommonUtils.appendHextet(stringBuilder, byArray, n3);
            stringBuilder.append(':');
        }
        SocksCommonUtils.appendHextet(stringBuilder, byArray, n3);
    }

    public static String ipv6toCompressedForm(byte[] byArray) {
        assert (byArray.length == 16);
        int n = -1;
        int n2 = 0;
        int n3 = 0;
        while (n3 < 8) {
            int n4 = n3 * 2;
            int n5 = 0;
            while (n4 < byArray.length && byArray[n4] == 0 && byArray[n4 + 1] == 0) {
                n4 += 2;
                ++n5;
            }
            if (n5 > n2) {
                n = n3;
                n2 = n5;
            }
            n3 = n4 / 2 + 1;
        }
        if (n == -1 || n2 < 2) {
            return SocksCommonUtils.ipv6toStr(byArray);
        }
        StringBuilder stringBuilder = new StringBuilder(39);
        SocksCommonUtils.ipv6toStr(stringBuilder, byArray, 0, n);
        stringBuilder.append(ipv6conseqZeroFiller);
        SocksCommonUtils.ipv6toStr(stringBuilder, byArray, n + n2, 8);
        return stringBuilder.toString();
    }

    private SocksCommonUtils() {
    }

    static {
        UNKNOWN_SOCKS_REQUEST = new UnknownSocksRequest();
        UNKNOWN_SOCKS_RESPONSE = new UnknownSocksResponse();
        ipv6conseqZeroFiller = new char[]{':', ':'};
    }

    private static void appendHextet(StringBuilder stringBuilder, byte[] byArray, int n) {
        StringUtil.toHexString(stringBuilder, byArray, n << 1, 2);
    }

    public static String ipv6toStr(byte[] byArray) {
        assert (byArray.length == 16);
        StringBuilder stringBuilder = new StringBuilder(39);
        SocksCommonUtils.ipv6toStr(stringBuilder, byArray, 0, 8);
        return stringBuilder.toString();
    }
}

