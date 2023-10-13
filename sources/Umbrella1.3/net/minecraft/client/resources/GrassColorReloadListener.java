/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerGrass;

public class GrassColorReloadListener
implements IResourceManagerReloadListener {
    private static final ResourceLocation field_130078_a = new ResourceLocation("textures/colormap/grass.png");
    private static final String __OBFID = "CL_00001078";

    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        try {
            ColorizerGrass.setGrassBiomeColorizer(TextureUtil.readImageData(p_110549_1_, field_130078_a));
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}

