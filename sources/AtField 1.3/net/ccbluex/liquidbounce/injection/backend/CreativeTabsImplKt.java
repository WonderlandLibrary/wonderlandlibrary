/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.ccbluex.liquidbounce.injection.backend.CreativeTabsImpl;
import net.minecraft.creativetab.CreativeTabs;

public final class CreativeTabsImplKt {
    public static final CreativeTabs unwrap(ICreativeTabs iCreativeTabs) {
        boolean bl = false;
        return ((CreativeTabsImpl)iCreativeTabs).getWrapped();
    }

    public static final ICreativeTabs wrap(CreativeTabs creativeTabs) {
        boolean bl = false;
        return new CreativeTabsImpl(creativeTabs);
    }
}

