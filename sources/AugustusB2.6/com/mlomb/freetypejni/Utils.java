// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystems;

public class Utils
{
    public static byte[] loadFileToByteArray(final String s) throws IOException {
        return Files.readAllBytes(FileSystems.getDefault().getPath(s, new String[0]));
    }
    
    public static native ByteBuffer newBuffer(final int p0);
    
    public static native void fillBuffer(final byte[] p0, final ByteBuffer p1, final int p2);
    
    public static native void deleteBuffer(final ByteBuffer p0);
    
    public static class Pointer
    {
        protected long pointer;
        
        public Pointer(final long pointer) {
            this.pointer = pointer;
        }
        
        public long getPointer() {
            return this.pointer;
        }
    }
}
