// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import java.io.IOException;
import java.io.InputStream;

class InputStreamWrapper extends InputStream
{
    private final String description;
    private final InputStream input;
    
    public InputStreamWrapper(final InputStream input, final String description) {
        this.input = input;
        this.description = description;
    }
    
    @Override
    public int available() throws IOException {
        return this.input.available();
    }
    
    @Override
    public void close() throws IOException {
        this.input.close();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this.input.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return this.input.hashCode();
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.input.mark(readlimit);
    }
    
    @Override
    public boolean markSupported() {
        return this.input.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        return this.input.read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.input.read(b);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.input.read(b, off, len);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.input.reset();
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.input.skip(n);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [description=" + this.description + ", input=" + this.input + "]";
    }
}
