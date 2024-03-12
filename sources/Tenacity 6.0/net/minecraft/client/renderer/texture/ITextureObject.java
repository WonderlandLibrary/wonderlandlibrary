// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.optifine.shaders.MultiTexID;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public interface ITextureObject
{
    void setBlurMipmap(final boolean p0, final boolean p1);
    
    void restoreLastBlurMipmap();
    
    void loadTexture(final IResourceManager p0) throws IOException;
    
    int getGlTextureId();
    
    MultiTexID getMultiTexID();
}
