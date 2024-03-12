// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemLocator implements IObjectLocator
{
    @Override
    public Object getObject(final ResourceLocation loc) {
        final Item item = Item.getByNameOrId(loc.toString());
        return item;
    }
}
