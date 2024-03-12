// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.optifine.util.EntityUtils;
import net.minecraft.util.ResourceLocation;

public class EntityClassLocator implements IObjectLocator
{
    @Override
    public Object getObject(final ResourceLocation loc) {
        final Class oclass = EntityUtils.getEntityClassByName(loc.getResourcePath());
        return oclass;
    }
}
