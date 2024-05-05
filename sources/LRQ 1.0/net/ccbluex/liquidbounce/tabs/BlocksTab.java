/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.tabs;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;

public final class BlocksTab
extends WrappedCreativeTabs {
    @Override
    public void displayAllReleventItems(List<IItemStack> itemList) {
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.COMMAND_BLOCK)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getItemEnum(ItemType.COMMAND_BLOCK_MINECART)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.BARRIER)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.DRAGON_EGG)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.BROWN_MUSHROOM_BLOCK)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.RED_MUSHROOM_BLOCK)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.FARMLAND)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.MOB_SPAWNER)));
        itemList.add(WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.LIT_FURNACE)));
    }

    @Override
    public IItem getTabIconItem() {
        IItem iItem = WrapperImpl.INSTANCE.getClassProvider().createItemStack(WrapperImpl.INSTANCE.getClassProvider().getBlockEnum(BlockType.COMMAND_BLOCK)).getItem();
        if (iItem == null) {
            Intrinsics.throwNpe();
        }
        return iItem;
    }

    @Override
    public String getTranslatedTabLabel() {
        return "Special blocks";
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    public BlocksTab() {
        super("Special blocks");
        this.getRepresentedType().setBackgroundImageName("item_search.png");
    }
}

