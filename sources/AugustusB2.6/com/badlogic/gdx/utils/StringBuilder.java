// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

import java.io.IOException;
import java.util.Arrays;

public final class StringBuilder implements Appendable, CharSequence
{
    private char[] chars;
    private int length;
    private static final char[] digits;
    
    private static int numChars(int value, int radix) {
        radix = ((value < 0) ? 2 : 1);
        while ((value /= 10) != 0) {
            ++radix;
        }
        return radix;
    }
    
    public StringBuilder() {
        this.chars = new char[16];
    }
    
    public StringBuilder(final int capacity) {
        this.chars = new char[32];
    }
    
    private void enlargeBuffer(final int min) {
        final int newSize = (this.chars.length >> 1) + this.chars.length + 2;
        final char[] newData = new char[(min > newSize) ? min : newSize];
        System.arraycopy(this.chars, 0, newData, 0, this.length);
        this.chars = newData;
    }
    
    private void appendNull() {
        final int newSize;
        if ((newSize = this.length + 4) > this.chars.length) {
            this.enlargeBuffer(newSize);
        }
        this.chars[this.length++] = 'n';
        this.chars[this.length++] = 'u';
        this.chars[this.length++] = 'l';
        this.chars[this.length++] = 'l';
    }
    
    private void append0(final char ch) {
        if (this.length == this.chars.length) {
            this.enlargeBuffer(this.length + 1);
        }
        this.chars[this.length++] = ch;
    }
    
    private void append0(final String string) {
        if (string == null) {
            this.appendNull();
            return;
        }
        final int adding = string.length();
        final int newSize;
        if ((newSize = this.length + adding) > this.chars.length) {
            this.enlargeBuffer(newSize);
        }
        string.getChars(0, adding, this.chars, this.length);
        this.length = newSize;
    }
    
    @Override
    public final char charAt(final int index) {
        if (index < 0 || index >= this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return this.chars[index];
    }
    
    @Override
    public final int length() {
        return this.length;
    }
    
    @Override
    public final String toString() {
        if (this.length == 0) {
            return "";
        }
        return new String(this.chars, 0, this.length);
    }
    
    @Override
    public final CharSequence subSequence(final int start, int end) {
        end = start;
        if (end < 0 || end > end || end > this.length) {
            throw new StringIndexOutOfBoundsException();
        }
        if (end == end) {
            return "";
        }
        return new String(this.chars, end, end - end);
    }
    
    @Override
    public final StringBuilder append(final char c) {
        this.append0(c);
        return this;
    }
    
    public final StringBuilder append(final int value, final int minLength) {
        return this.append(value, 0, '0');
    }
    
    private StringBuilder append(int value, int minLength, final char prefix) {
        if (value == Integer.MIN_VALUE) {
            this.append0("-2147483648");
            return this;
        }
        if (value < 0) {
            this.append0('-');
            value = -value;
        }
        if (minLength > 1) {
            for (minLength -= numChars(value, 10); minLength > 0; --minLength) {
                this.append0('0');
            }
        }
        if (value >= 10000) {
            if (value >= 1000000000) {
                this.append0(StringBuilder.digits[(int)(value % 10000000000L / 1000000000L)]);
            }
            if (value >= 100000000) {
                this.append0(StringBuilder.digits[value % 1000000000 / 100000000]);
            }
            if (value >= 10000000) {
                this.append0(StringBuilder.digits[value % 100000000 / 10000000]);
            }
            if (value >= 1000000) {
                this.append0(StringBuilder.digits[value % 10000000 / 1000000]);
            }
            if (value >= 100000) {
                this.append0(StringBuilder.digits[value % 1000000 / 100000]);
            }
            this.append0(StringBuilder.digits[value % 100000 / 10000]);
        }
        if (value >= 1000) {
            this.append0(StringBuilder.digits[value % 10000 / 1000]);
        }
        if (value >= 100) {
            this.append0(StringBuilder.digits[value % 1000 / 100]);
        }
        if (value >= 10) {
            this.append0(StringBuilder.digits[value % 100 / 10]);
        }
        this.append0(StringBuilder.digits[value % 10]);
        return this;
    }
    
    public final StringBuilder append(final float f) {
        this.append0(Float.toString(f));
        return this;
    }
    
    public final StringBuilder append(final Object obj) {
        if (obj == null) {
            this.appendNull();
        }
        else {
            this.append0(obj.toString());
        }
        return this;
    }
    
    public final StringBuilder append(final String str) {
        this.append0(str);
        return this;
    }
    
    @Override
    public final int hashCode() {
        final int result = 31 + this.length;
        return result * 31 + Arrays.hashCode(this.chars);
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final StringBuilder other = (StringBuilder)obj;
        final int length;
        if ((length = this.length) != other.length) {
            return false;
        }
        final char[] chars = this.chars;
        final char[] chars2 = other.chars;
        for (int i = 0; i < length; ++i) {
            if (chars[i] != chars2[i]) {
                return false;
            }
        }
        return true;
    }
    
    static {
        digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    }
}
