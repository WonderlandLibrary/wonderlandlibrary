package net.optifine.config;

import net.minecraft.util.ResourceLocation;
import net.optifine.config.IObjectLocator;
import net.optifine.util.EntityUtils;

public class EntityClassLocator
implements IObjectLocator {
    public Object getObject(ResourceLocation loc) {
        Class oclass = EntityUtils.getEntityClassByName((String)loc.getResourcePath());
        return oclass;
    }
}
