/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import java.util.Arrays;

public final class AppendableCharSequence
implements CharSequence,
Appendable {
    private int pos;
    private char[] chars;

    @Override
    public int length() {
        return this.pos;
    }

    public AppendableCharSequence(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("length: " + n + " (length: >= 1)");
        }
        this.chars = new char[n];
    }

    @Override
    public AppendableCharSequence append(CharSequence charSequence, int n, int n2) {
        if (charSequence.length() < n2) {
            throw new IndexOutOfBoundsException();
        }
        int n3 = n2 - n;
        if (n3 > this.chars.length - this.pos) {
            this.chars = AppendableCharSequence.expand(this.chars, this.pos + n3, this.pos);
        }
        if (charSequence instanceof AppendableCharSequence) {
            AppendableCharSequence appendableCharSequence = (AppendableCharSequence)charSequence;
            char[] cArray = appendableCharSequence.chars;
            System.arraycopy(cArray, n, this.chars, this.pos, n3);
            this.pos += n3;
            return this;
        }
        for (int i = n; i < n2; ++i) {
            this.chars[this.pos++] = charSequence.charAt(i);
        }
        return this;
    }

    @Override
    public AppendableCharSequence append(CharSequence charSequence) {
        return this.append(charSequence, 0, charSequence.length());
    }

    private AppendableCharSequence(char[] cArray) {
        this.chars = cArray;
        this.pos = cArray.length;
    }

    public void reset() {
        this.pos = 0;
    }

    @Override
    public AppendableCharSequence append(char c) {
        if (this.pos == this.chars.length) {
            char[] cArray = this.chars;
            int n = cArray.length << 1;
            if (n < 0) {
                throw new IllegalStateException();
            }
            this.chars = new char[n];
            System.arraycopy(cArray, 0, this.chars, 0, cArray.length);
        }
        this.chars[this.pos++] = c;
        return this;
    }

    @Override
    public char charAt(int n) {
        if (n > this.pos) {
            throw new IndexOutOfBoundsException();
        }
        return this.chars[n];
    }

    @Override
    public String toString() {
        return new String(this.chars, 0, this.pos);
    }

    private static char[] expand(char[] cArray, int n, int n2) {
        int n3 = cArray.length;
        do {
            if ((n3 <<= 1) >= 0) continue;
            throw new IllegalStateException();
        } while (n > n3);
        char[] cArray2 = new char[n3];
        System.arraycopy(cArray, 0, cArray2, 0, n2);
        return cArray2;
    }

    public String substring(int n, int n2) {
        int n3 = n2 - n;
        if (n > this.pos || n3 > this.pos) {
            throw new IndexOutOfBoundsException();
        }
        return new String(this.chars, n, n3);
    }

    @Override
    public AppendableCharSequence subSequence(int n, int n2) {
        return new AppendableCharSequence(Arrays.copyOfRange(this.chars, n, n2));
    }
}

