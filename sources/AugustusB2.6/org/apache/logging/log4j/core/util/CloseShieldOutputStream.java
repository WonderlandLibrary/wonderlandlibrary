// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class CloseShieldOutputStream extends OutputStream
{
    private final OutputStream delegate;
    
    public CloseShieldOutputStream(final OutputStream delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() throws IOException {
        this.delegate.flush();
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.delegate.write(b);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.delegate.write(b, off, len);
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.delegate.write(b);
    }
}
