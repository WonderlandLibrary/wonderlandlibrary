// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.util.ResourceLocation;

public class CustomEntityRenderer
{
    private String name;
    private String basePath;
    private ResourceLocation textureLocation;
    private CustomModelRenderer[] customModelRenderers;
    private float shadowSize;
    
    public CustomEntityRenderer(final String name, final String basePath, final ResourceLocation textureLocation, final CustomModelRenderer[] customModelRenderers, final float shadowSize) {
        this.name = null;
        this.basePath = null;
        this.textureLocation = null;
        this.customModelRenderers = null;
        this.shadowSize = 0.0f;
        this.name = name;
        this.basePath = basePath;
        this.textureLocation = textureLocation;
        this.customModelRenderers = customModelRenderers;
        this.shadowSize = shadowSize;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getBasePath() {
        return this.basePath;
    }
    
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }
    
    public CustomModelRenderer[] getCustomModelRenderers() {
        return this.customModelRenderers;
    }
    
    public float getShadowSize() {
        return this.shadowSize;
    }
}
