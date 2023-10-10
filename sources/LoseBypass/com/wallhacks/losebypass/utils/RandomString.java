/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomString {
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + lower + "0123456789";
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    public String nextString() {
        int idx = 0;
        while (idx < this.buf.length) {
            this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
            ++idx;
        }
        return new String(this.buf);
    }

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    public RandomString() {
        this(21);
    }
}

