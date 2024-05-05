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

    @Override
    public String getBackgroundImageName() {
        return this.wrapped.func_78015_f();
    }

    @Override
    public void setBackgroundImageName(String value) {
        this.wrapped.func_78025_a(value);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof CreativeTabsImpl && ((CreativeTabsImpl)other).wrapped.equals(this.wrapped);
    }

    public final CreativeTabs getWrapped() {
        return this.wrapped;
    }

    public CreativeTabsImpl(CreativeTabs wrapped) {
        this.wrapped = wrapped;
    }
}

