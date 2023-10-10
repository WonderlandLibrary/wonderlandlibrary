/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class BufferTools {
    protected static final String defaultCharsetName = "ISO-8859-1";

    public static String byteBufferToStringIgnoringEncodingIssues(byte[] byArray, int n2, int n3) {
        try {
            return BufferTools.byteBufferToString(byArray, n2, n3, defaultCharsetName);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
    }

    public static String byteBufferToString(byte[] byArray, int n2, int n3) throws UnsupportedEncodingException {
        return BufferTools.byteBufferToString(byArray, n2, n3, defaultCharsetName);
    }

    public static String byteBufferToString(byte[] byArray, int n2, int n3, String string) throws UnsupportedEncodingException {
        if (n3 < 1) {
            return "";
        }
        return new String(byArray, n2, n3, string);
    }

    public static byte[] stringToByteBufferIgnoringEncodingIssues(String string, int n2, int n3) {
        try {
            return BufferTools.stringToByteBuffer(string, n2, n3);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
    }

    public static byte[] stringToByteBuffer(String string, int n2, int n3) throws UnsupportedEncodingException {
        return BufferTools.stringToByteBuffer(string, n2, n3, defaultCharsetName);
    }

    public static byte[] stringToByteBuffer(String string, int n2, int n3, String string2) throws UnsupportedEncodingException {
        String string3 = string.substring(n2, n2 + n3);
        byte[] byArray = string3.getBytes(string2);
        return byArray;
    }

    public static void stringIntoByteBuffer(String string, int n2, int n3, byte[] byArray, int n4) throws UnsupportedEncodingException {
        BufferTools.stringIntoByteBuffer(string, n2, n3, byArray, n4, defaultCharsetName);
    }

    public static void stringIntoByteBuffer(String string, int n2, int n3, byte[] byArray, int n4, String string2) throws UnsupportedEncodingException {
        String string3 = string.substring(n2, n2 + n3);
        byte[] byArray2 = string3.getBytes(string2);
        if (byArray2.length > 0) {
            System.arraycopy(byArray2, 0, byArray, n4, byArray2.length);
        }
    }

    public static String trimStringRight(String string) {
        char c2;
        int n2;
        for (n2 = string.length() - 1; n2 >= 0 && (c2 = string.charAt(n2)) <= ' '; --n2) {
        }
        if (n2 == string.length() - 1) {
            return string;
        }
        if (n2 < 0) {
            return "";
        }
        return string.substring(0, n2 + 1);
    }

    public static String padStringRight(String string, int n2, char c2) {
        if (string.length() >= n2) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        while (stringBuilder.length() < n2) {
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    public static boolean checkBit(byte by, int n2) {
        return (by & 1 << n2) != 0;
    }

    public static byte setBit(byte by, int n2, boolean bl) {
        byte by2 = bl ? (byte)(by | 1 << n2) : (byte)(by & ~(1 << n2));
        return by2;
    }

    public static int shiftByte(byte by, int n2) {
        int n3 = by & 0xFF;
        if (n2 < 0) {
            return n3 << -n2;
        }
        if (n2 > 0) {
            return n3 >> n2;
        }
        return n3;
    }

    public static int unpackInteger(byte by, byte by2, byte by3, byte by4) {
        int n2 = by4 & 0xFF;
        n2 += BufferTools.shiftByte(by3, -8);
        n2 += BufferTools.shiftByte(by2, -16);
        return n2 += BufferTools.shiftByte(by, -24);
    }

    public static byte[] packInteger(int n2) {
        byte[] byArray = new byte[4];
        byArray[3] = (byte)(n2 & 0xFF);
        byArray[2] = (byte)(n2 >> 8 & 0xFF);
        byArray[1] = (byte)(n2 >> 16 & 0xFF);
        byArray[0] = (byte)(n2 >> 24 & 0xFF);
        return byArray;
    }

    public static int unpackSynchsafeInteger(byte by, byte by2, byte by3, byte by4) {
        int n2 = by4 & 0x7F;
        n2 += BufferTools.shiftByte((byte)(by3 & 0x7F), -7);
        n2 += BufferTools.shiftByte((byte)(by2 & 0x7F), -14);
        return n2 += BufferTools.shiftByte((byte)(by & 0x7F), -21);
    }

    public static byte[] packSynchsafeInteger(int n2) {
        byte[] byArray = new byte[4];
        BufferTools.packSynchsafeInteger(n2, byArray, 0);
        return byArray;
    }

    public static void packSynchsafeInteger(int n2, byte[] byArray, int n3) {
        byArray[n3 + 3] = (byte)(n2 & 0x7F);
        byArray[n3 + 2] = (byte)(n2 >> 7 & 0x7F);
        byArray[n3 + 1] = (byte)(n2 >> 14 & 0x7F);
        byArray[n3 + 0] = (byte)(n2 >> 21 & 0x7F);
    }

    public static byte[] copyBuffer(byte[] byArray, int n2, int n3) {
        byte[] byArray2 = new byte[n3];
        if (n3 > 0) {
            System.arraycopy(byArray, n2, byArray2, 0, n3);
        }
        return byArray2;
    }

    public static void copyIntoByteBuffer(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (n3 > 0) {
            System.arraycopy(byArray, n2, byArray2, n4, n3);
        }
    }

    public static int sizeUnsynchronisationWouldAdd(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length - 1; ++i2) {
            if (byArray[i2] != -1 || (byArray[i2 + 1] & 0xFFFFFFE0) != -32 && byArray[i2 + 1] != 0) continue;
            ++n2;
        }
        if (byArray.length > 0 && byArray[byArray.length - 1] == -1) {
            ++n2;
        }
        return n2;
    }

    public static byte[] unsynchroniseBuffer(byte[] byArray) {
        int n2 = BufferTools.sizeUnsynchronisationWouldAdd(byArray);
        if (n2 == 0) {
            return byArray;
        }
        byte[] byArray2 = new byte[byArray.length + n2];
        int n3 = 0;
        for (int i2 = 0; i2 < byArray.length - 1; ++i2) {
            byArray2[n3++] = byArray[i2];
            if (byArray[i2] != -1 || (byArray[i2 + 1] & 0xFFFFFFE0) != -32 && byArray[i2 + 1] != 0) continue;
            byArray2[n3++] = 0;
        }
        byArray2[n3++] = byArray[byArray.length - 1];
        if (byArray[byArray.length - 1] == -1) {
            byArray2[n3++] = 0;
        }
        return byArray2;
    }

    public static int sizeSynchronisationWouldSubtract(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length - 2; ++i2) {
            if (byArray[i2] != -1 || byArray[i2 + 1] != 0 || (byArray[i2 + 2] & 0xFFFFFFE0) != -32 && byArray[i2 + 2] != 0) continue;
            ++n2;
        }
        if (byArray.length > 1 && byArray[byArray.length - 2] == -1 && byArray[byArray.length - 1] == 0) {
            ++n2;
        }
        return n2;
    }

    public static byte[] synchroniseBuffer(byte[] byArray) {
        int n2 = BufferTools.sizeSynchronisationWouldSubtract(byArray);
        if (n2 == 0) {
            return byArray;
        }
        byte[] byArray2 = new byte[byArray.length - n2];
        int n3 = 0;
        for (int i2 = 0; i2 < byArray2.length - 1; ++i2) {
            byArray2[i2] = byArray[n3];
            if (byArray[n3] == -1 && byArray[n3 + 1] == 0 && ((byArray[n3 + 2] & 0xFFFFFFE0) == -32 || byArray[n3 + 2] == 0)) {
                ++n3;
            }
            ++n3;
        }
        byArray2[byArray2.length - 1] = byArray[n3];
        return byArray2;
    }

    public static String substitute(String string, String string2, String string3) {
        if (string2.length() < 1 || !string.contains(string2)) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = 0;
        while ((n3 = string.indexOf(string2, n3)) >= 0) {
            if (n3 > n2) {
                stringBuilder.append(string.substring(n2, n3));
            }
            if (string3 != null) {
                stringBuilder.append(string3);
            }
            n2 = n3 + string2.length();
            ++n3;
        }
        if (n2 < string.length()) {
            stringBuilder.append(string.substring(n2));
        }
        return stringBuilder.toString();
    }

    public static String asciiOnly(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 < ' ' || c2 > '~') {
                stringBuilder.append('?');
                continue;
            }
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    public static int indexOfTerminator(byte[] byArray) {
        return BufferTools.indexOfTerminator(byArray, 0);
    }

    public static int indexOfTerminator(byte[] byArray, int n2) {
        return BufferTools.indexOfTerminator(byArray, 0, 1);
    }

    public static int indexOfTerminator(byte[] byArray, int n2, int n3) {
        int n4 = -1;
        for (int i2 = n2; i2 <= byArray.length - n3; ++i2) {
            int n5;
            if ((i2 - n2) % n3 != 0) continue;
            for (n5 = 0; n5 < n3 && byArray[i2 + n5] == 0; ++n5) {
            }
            if (n5 != n3) continue;
            n4 = i2;
            break;
        }
        return n4;
    }

    public static int indexOfTerminatorForEncoding(byte[] byArray, int n2, int n3) {
        int n4 = n3 == 1 || n3 == 2 ? 2 : 1;
        return BufferTools.indexOfTerminator(byArray, n2, n4);
    }
}

