package net.optifine.config;

import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class EntityClassLocator implements IObjectLocator
{
    public Object getObject(ResourceLocation loc)
    {
        Class oclass = EntityList.getClassFromName(loc.toString());
        return oclass;
    }
}
