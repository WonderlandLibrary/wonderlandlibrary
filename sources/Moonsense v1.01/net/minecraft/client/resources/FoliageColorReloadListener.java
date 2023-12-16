// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class FoliageColorReloadListener implements IResourceManagerReloadListener
{
    private static final ResourceLocation field_130079_a;
    private static final String __OBFID = "CL_00001077";
    
    static {
        field_130079_a = new ResourceLocation("textures/colormap/foliage.png");
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager p_110549_1_) {
        try {
            ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(p_110549_1_, FoliageColorReloadListener.field_130079_a));
        }
        catch (IOException ex) {}
    }
}
