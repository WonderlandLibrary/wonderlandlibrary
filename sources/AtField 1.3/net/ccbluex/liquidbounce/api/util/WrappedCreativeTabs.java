/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;

public abstract class WrappedCreativeTabs {
    public ICreativeTabs representedType;
    private final String name;

    public IItem getTabIconItem() {
        return WrapperImpl.INSTANCE.getClassProvider().getItemEnum(ItemType.WRITABLE_BOOK);
    }

    public final ICreativeTabs getRepresentedType() {
        return this.representedType;
    }

    public void displayAllReleventItems(List list) {
    }

    public boolean hasSearchBar() {
        return true;
    }

    public WrappedCreativeTabs(String string) {
        this.name = string;
        LiquidBounce.INSTANCE.getWrapper().getClassProvider().wrapCreativeTab(this.name, this);
    }

    public String getTranslatedTabLabel() {
        return "asdf";
    }

    public final void setRepresentedType(ICreativeTabs iCreativeTabs) {
        this.representedType = iCreativeTabs;
    }

    public final String getName() {
        return this.name;
    }
}

