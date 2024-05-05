/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;

public abstract class WrappedCreativeTabs {
    public ICreativeTabs representedType;
    private final String name;

    public final ICreativeTabs getRepresentedType() {
        return this.representedType;
    }

    public final void setRepresentedType(ICreativeTabs iCreativeTabs) {
        this.representedType = iCreativeTabs;
    }

    public void displayAllReleventItems(List<IItemStack> items) {
    }

    public String getTranslatedTabLabel() {
        return "asdf";
    }

    public IItem getTabIconItem() {
        return WrapperImpl.INSTANCE.getClassProvider().getItemEnum(ItemType.WRITABLE_BOOK);
    }

    public boolean hasSearchBar() {
        return true;
    }

    public final String getName() {
        return this.name;
    }

    public WrappedCreativeTabs(String name) {
        this.name = name;
        LiquidBounce.INSTANCE.getWrapper().getClassProvider().wrapCreativeTab(this.name, this);
    }
}

