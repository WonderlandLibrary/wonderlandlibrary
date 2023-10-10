// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

class NativeString implements CharSequence, Comparable
{
    static final String WIDE_STRING = "--WIDE-STRING--";
    private Pointer pointer;
    private String encoding;
    
    public NativeString(final String string) {
        this(string, Native.getDefaultStringEncoding());
    }
    
    public NativeString(final String string, final boolean wide) {
        this(string, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }
    
    public NativeString(final WString string) {
        this(string.toString(), "--WIDE-STRING--");
    }
    
    public NativeString(final String string, final String encoding) {
        if (string == null) {
            throw new NullPointerException("String must not be null");
        }
        this.encoding = encoding;
        if ("--WIDE-STRING--".equals(this.encoding)) {
            final int len = (string.length() + 1) * Native.WCHAR_SIZE;
            (this.pointer = new StringMemory(len)).setWideString(0L, string);
        }
        else {
            final byte[] data = Native.getBytes(string, encoding);
            (this.pointer = new StringMemory(data.length + 1)).write(0L, data, 0, data.length);
            this.pointer.setByte(data.length, (byte)0);
        }
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof CharSequence && this.compareTo(other) == 0;
    }
    
    @Override
    public String toString() {
        final boolean wide = "--WIDE-STRING--".equals(this.encoding);
        return wide ? this.pointer.getWideString(0L) : this.pointer.getString(0L, this.encoding);
    }
    
    public Pointer getPointer() {
        return this.pointer;
    }
    
    @Override
    public char charAt(final int index) {
        return this.toString().charAt(index);
    }
    
    @Override
    public int length() {
        return this.toString().length();
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.toString().subSequence(start, end);
    }
    
    @Override
    public int compareTo(final Object other) {
        if (other == null) {
            return 1;
        }
        return this.toString().compareTo(other.toString());
    }
    
    private class StringMemory extends Memory
    {
        public StringMemory(final long size) {
            super(size);
        }
        
        @Override
        public String toString() {
            return NativeString.this.toString();
        }
    }
}
