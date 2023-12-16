// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;

public interface IImageBuffer
{
    BufferedImage parseUserSkin(final BufferedImage p0);
    
    void skinAvailable();
}
