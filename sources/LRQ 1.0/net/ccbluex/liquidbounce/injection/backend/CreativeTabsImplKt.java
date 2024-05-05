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
    public static final CreativeTabs unwrap(ICreativeTabs $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((CreativeTabsImpl)$this$unwrap).getWrapped();
    }

    public static final ICreativeTabs wrap(CreativeTabs $this$wrap) {
        int $i$f$wrap = 0;
        return new CreativeTabsImpl($this$wrap);
    }
}

