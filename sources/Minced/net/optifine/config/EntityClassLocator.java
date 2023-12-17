// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class EntityClassLocator implements IObjectLocator
{
    @Override
    public Object getObject(final ResourceLocation loc) {
        final Class oclass = EntityList.getClassFromName(loc.toString());
        return oclass;
    }
}
