// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class GrassColorReloadListener implements IResourceManagerReloadListener
{
    private static final ResourceLocation field_130078_a;
    private static final String __OBFID = "CL_00001078";
    
    @Override
    public void onResourceManagerReload(final IResourceManager p_110549_1_) {
        try {
            ColorizerGrass.setGrassBiomeColorizer(TextureUtil.readImageData(p_110549_1_, GrassColorReloadListener.field_130078_a));
        }
        catch (IOException ex) {}
    }
    
    static {
        field_130078_a = new ResourceLocation("textures/colormap/grass.png");
    }
}
