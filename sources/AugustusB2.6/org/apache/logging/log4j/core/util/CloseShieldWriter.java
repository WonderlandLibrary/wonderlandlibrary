// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Writer;

public class CloseShieldWriter extends Writer
{
    private final Writer delegate;
    
    public CloseShieldWriter(final Writer delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void close() throws IOException {
    }
    
    @Override
    public void flush() throws IOException {
        this.delegate.flush();
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.delegate.write(cbuf, off, len);
    }
}
