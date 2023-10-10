// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.IOException;

public class Library extends Utils.Pointer
{
    public Library(final long n) {
        super(n);
    }
    
    public boolean delete() {
        return FreeType.FT_Done_FreeType(this.pointer);
    }
    
    public Face newFace(final String s, final int n) {
        try {
            return this.newFace(Utils.loadFileToByteArray(s), n);
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public Face newFace(final byte[] array, final int n) {
        final ByteBuffer buffer = Utils.newBuffer(array.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.limit(buffer.position() + array.length);
        Utils.fillBuffer(array, buffer, array.length);
        return this.newFace(buffer, n);
    }
    
    public Face newFace(final ByteBuffer byteBuffer, final int n) {
        final long ft_New_Memory_Face = FreeType.FT_New_Memory_Face(this.pointer, byteBuffer, byteBuffer.remaining(), n);
        if (ft_New_Memory_Face <= 0L) {
            Utils.deleteBuffer(byteBuffer);
            return null;
        }
        return new Face(ft_New_Memory_Face, byteBuffer);
    }
    
    public LibraryVersion getVersion() {
        return FreeType.FT_Library_Version(this.pointer);
    }
}
