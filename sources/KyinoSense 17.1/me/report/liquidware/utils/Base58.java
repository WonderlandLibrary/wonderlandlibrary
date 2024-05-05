/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import obfuscator.NativeMethod;

public class Base58 {
    private char[] ALPHABET;
    private int BASE_58;
    private int BASE_256;
    private int[] INDEXES;

    public Base58(int i) {
        this.init(i);
    }

    @NativeMethod.Entry
    public void init(int key) {
        int i;
        if (key == 14514) {
            this.ALPHABET = "tBMoi7Whp9aUrcJxKjkqVnbFHgL3C1N6e2AfmSwE5vTPQzYuG8dZR4ysXD".toCharArray();
            this.BASE_58 = this.ALPHABET.length;
            this.BASE_256 = 256;
            this.INDEXES = new int[128];
        } else {
            this.ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
            this.BASE_58 = this.ALPHABET.length;
            this.BASE_256 = 256;
            this.INDEXES = new int[128];
        }
        for (i = 0; i < this.INDEXES.length; ++i) {
            this.INDEXES[i] = -1;
        }
        for (i = 0; i < this.ALPHABET.length; ++i) {
            this.INDEXES[this.ALPHABET[i]] = i;
        }
    }

    @NativeMethod.Entry
    public String encode(byte[] input) {
        int zeroCount;
        if (input.length == 0) {
            return "";
        }
        input = this.copyOfRange(input, 0, input.length);
        for (zeroCount = 0; zeroCount < input.length && input[zeroCount] == 0; ++zeroCount) {
        }
        byte[] temp = new byte[input.length * 2];
        int j = temp.length;
        int startAt = zeroCount;
        while (startAt < input.length) {
            byte mod = this.divmod58(input, startAt);
            if (input[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = (byte)this.ALPHABET[mod];
        }
        while (j < temp.length && temp[j] == this.ALPHABET[0]) {
            ++j;
        }
        while (--zeroCount >= 0) {
            temp[--j] = (byte)this.ALPHABET[0];
        }
        byte[] output = this.copyOfRange(temp, j, temp.length);
        return new String(output);
    }

    @NativeMethod.Entry
    public byte[] decode(String input) {
        int zeroCount;
        if (input.length() == 0) {
            return new byte[0];
        }
        byte[] input58 = new byte[input.length()];
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            int digit58 = -1;
            if (c >= '\u0000' && c < '\u0080') {
                digit58 = this.INDEXES[c];
            }
            if (digit58 < 0) {
                throw new RuntimeException("Not a Base58 input: " + input);
            }
            input58[i] = (byte)digit58;
        }
        for (zeroCount = 0; zeroCount < input58.length && input58[zeroCount] == 0; ++zeroCount) {
        }
        byte[] temp = new byte[input.length()];
        int j = temp.length;
        int startAt = zeroCount;
        while (startAt < input58.length) {
            byte mod = this.divmod256(input58, startAt);
            if (input58[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = mod;
        }
        while (j < temp.length && temp[j] == 0) {
            ++j;
        }
        return this.copyOfRange(temp, j - zeroCount, temp.length);
    }

    @NativeMethod.Entry
    private byte divmod58(byte[] number, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number.length; ++i) {
            int digit256 = number[i] & 0xFF;
            int temp = remainder * this.BASE_256 + digit256;
            number[i] = (byte)(temp / this.BASE_58);
            remainder = temp % this.BASE_58;
        }
        return (byte)remainder;
    }

    @NativeMethod.Entry
    private byte divmod256(byte[] number58, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number58.length; ++i) {
            int digit58 = number58[i] & 0xFF;
            int temp = remainder * this.BASE_58 + digit58;
            number58[i] = (byte)(temp / this.BASE_256);
            remainder = temp % this.BASE_256;
        }
        return (byte)remainder;
    }

    @NativeMethod.Entry
    private byte[] copyOfRange(byte[] source, int from, int to) {
        byte[] range = new byte[to - from];
        System.arraycopy(source, from, range, 0, range.length);
        return range;
    }
}

