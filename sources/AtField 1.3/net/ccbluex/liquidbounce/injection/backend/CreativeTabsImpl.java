/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import org.jetbrains.annotations.Nullable;

public final class CreativeTabsImpl
implements ICreativeTabs {
    private final CreativeTabs wrapped;

    public boolean equals(@Nullable Object object) {
        return object instanceof CreativeTabsImpl && ((CreativeTabsImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public String getBackgroundImageName() {
        return this.wrapped.func_78015_f();
    }

    @Override
    public void setBackgroundImageName(String string) {
        this.wrapped.func_78025_a(string);
    }

    public CreativeTabsImpl(CreativeTabs creativeTabs) {
        this.wrapped = creativeTabs;
    }

    public final CreativeTabs getWrapped() {
        return this.wrapped;
    }
}

