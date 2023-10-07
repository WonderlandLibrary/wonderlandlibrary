// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public interface ITextureObject
{
    void func_174936_b(final boolean p0, final boolean p1);
    
    void func_174935_a();
    
    void loadTexture(final IResourceManager p0) throws IOException;
    
    int getGlTextureId();
}
