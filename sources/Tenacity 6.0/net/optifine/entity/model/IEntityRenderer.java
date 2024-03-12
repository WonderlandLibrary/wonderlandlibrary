// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.util.ResourceLocation;

public interface IEntityRenderer
{
    Class getEntityClass();
    
    void setEntityClass(final Class p0);
    
    ResourceLocation getLocationTextureCustom();
    
    void setLocationTextureCustom(final ResourceLocation p0);
}
