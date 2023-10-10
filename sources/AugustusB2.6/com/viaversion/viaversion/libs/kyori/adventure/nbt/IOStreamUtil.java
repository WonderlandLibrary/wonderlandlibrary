// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

final class IOStreamUtil
{
    private IOStreamUtil() {
    }
    
    static InputStream closeShield(final InputStream stream) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return stream.read();
            }
            
            @Override
            public int read(final byte[] b) throws IOException {
                return stream.read(b);
            }
            
            @Override
            public int read(final byte[] b, final int off, final int len) throws IOException {
                return stream.read(b, off, len);
            }
        };
    }
    
    static OutputStream closeShield(final OutputStream stream) {
        return new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                stream.write(b);
            }
            
            @Override
            public void write(final byte[] b) throws IOException {
                stream.write(b);
            }
            
            @Override
            public void write(final byte[] b, final int off, final int len) throws IOException {
                stream.write(b, off, len);
            }
        };
    }
}
