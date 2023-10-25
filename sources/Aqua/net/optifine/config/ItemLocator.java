package net.optifine.config;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.IObjectLocator;

public class ItemLocator
implements IObjectLocator {
    public Object getObject(ResourceLocation loc) {
        Item item = Item.getByNameOrId((String)loc.toString());
        return item;
    }
}
