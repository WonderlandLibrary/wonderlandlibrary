/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.UnknownSocksRequest;
import io.netty.handler.codec.socks.UnknownSocksResponse;

final class SocksCommonUtils {
    public static final SocksRequest UNKNOWN_SOCKS_REQUEST = new UnknownSocksRequest();
    public static final SocksResponse UNKNOWN_SOCKS_RESPONSE = new UnknownSocksResponse();
    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final int XOR_DEFAULT_VALUE = 255;
    private static final char[] ipv6conseqZeroFiller = new char[]{':', ':'};
    private static final char ipv6hextetSeparator = ':';

    private SocksCommonUtils() {
    }

    public static String intToIp(int i) {
        return String.valueOf(i >> 24 & 0xFF) + '.' + (i >> 16 & 0xFF) + '.' + (i >> 8 & 0xFF) + '.' + (i & 0xFF);
    }

    public static String ipv6toCompressedForm(byte[] src) {
        assert (src.length == 16);
        int cmprHextet = -1;
        int cmprSize = 0;
        int hextet = 0;
        while (hextet < 8) {
            int curByte = hextet * 2;
            int size = 0;
            while (curByte < src.length && src[curByte] == 0 && src[curByte + 1] == 0) {
                curByte += 2;
                ++size;
            }
            if (size > cmprSize) {
                cmprHextet = hextet;
                cmprSize = size;
            }
            hextet = curByte / 2 + 1;
        }
        if (cmprHextet == -1 || cmprSize < 2) {
            return SocksCommonUtils.ipv6toStr(src);
        }
        StringBuilder sb = new StringBuilder(39);
        SocksCommonUtils.ipv6toStr(sb, src, 0, cmprHextet);
        sb.append(ipv6conseqZeroFiller);
        SocksCommonUtils.ipv6toStr(sb, src, cmprHextet + cmprSize, 8);
        return sb.toString();
    }

    public static String ipv6toStr(byte[] src) {
        assert (src.length == 16);
        StringBuilder sb = new StringBuilder(39);
        SocksCommonUtils.ipv6toStr(sb, src, 0, 8);
        return sb.toString();
    }

    private static void ipv6toStr(StringBuilder sb, byte[] src, int fromHextet, int toHextet) {
        for (int i = fromHextet; i < toHextet; ++i) {
            sb.append(Integer.toHexString(src[i << 1] << 8 & 0xFF00 | src[(i << 1) + 1] & 0xFF));
            if (i >= toHextet - 1) continue;
            sb.append(':');
        }
    }
}

