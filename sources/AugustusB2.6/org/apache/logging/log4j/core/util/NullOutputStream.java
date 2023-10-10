// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream
{
    private static final NullOutputStream INSTANCE;
    @Deprecated
    public static final NullOutputStream NULL_OUTPUT_STREAM;
    
    public static NullOutputStream getInstance() {
        return NullOutputStream.INSTANCE;
    }
    
    private NullOutputStream() {
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) {
    }
    
    @Override
    public void write(final int b) {
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
    }
    
    static {
        INSTANCE = new NullOutputStream();
        NULL_OUTPUT_STREAM = NullOutputStream.INSTANCE;
    }
}
