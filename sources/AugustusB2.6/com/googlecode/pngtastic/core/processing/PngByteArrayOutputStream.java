// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import java.io.ByteArrayOutputStream;

public final class PngByteArrayOutputStream extends ByteArrayOutputStream
{
    private final int initialSize;
    
    public PngByteArrayOutputStream() {
        this(32);
    }
    
    public PngByteArrayOutputStream(final int size) {
        super(size);
        this.initialSize = size;
    }
    
    public PngByteArrayOutputStream(final byte[] initial) {
        this.buf = initial;
        this.count = initial.length;
        this.initialSize = this.count;
    }
    
    public final byte[] get() {
        return this.buf;
    }
    
    @Override
    public final void reset() {
        super.reset();
        if (this.buf.length > this.initialSize) {
            this.buf = new byte[this.initialSize];
        }
    }
    
    public final int len() {
        return this.count;
    }
}
