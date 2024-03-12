// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders;

import java.io.InputStream;

public interface IShaderPack
{
    String getName();
    
    InputStream getResourceAsStream(final String p0);
    
    boolean hasDirectory(final String p0);
    
    void close();
}
