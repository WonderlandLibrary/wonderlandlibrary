/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.tabs;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/tabs/BlocksTab;", "Lnet/minecraft/creativetab/CreativeTabs;", "()V", "displayAllReleventItems", "", "itemList", "", "Lnet/minecraft/item/ItemStack;", "getTabIconItem", "Lnet/minecraft/item/Item;", "getTranslatedTabLabel", "", "hasSearchBar", "", "KyinoClient"})
public final class BlocksTab
extends CreativeTabs {
    public void func_78018_a(@NotNull List<ItemStack> itemList) {
        Intrinsics.checkParameterIsNotNull(itemList, "itemList");
        itemList.add(new ItemStack(Blocks.field_150483_bI));
        itemList.add(new ItemStack(Items.field_151095_cc));
        itemList.add(new ItemStack(Blocks.field_180401_cv));
        itemList.add(new ItemStack(Blocks.field_150380_bt));
        itemList.add(new ItemStack(Blocks.field_150420_aW));
        itemList.add(new ItemStack(Blocks.field_150419_aX));
        itemList.add(new ItemStack(Blocks.field_150458_ak));
        itemList.add(new ItemStack(Blocks.field_150474_ac));
        itemList.add(new ItemStack(Blocks.field_150470_am));
    }

    @NotNull
    public Item func_78016_d() {
        Item item = new ItemStack(Blocks.field_150483_bI).func_77973_b();
        Intrinsics.checkExpressionValueIsNotNull(item, "ItemStack(Blocks.command_block).item");
        return item;
    }

    @NotNull
    public String func_78024_c() {
        return "Special blocks";
    }

    public boolean hasSearchBar() {
        return true;
    }

    public BlocksTab() {
        super("Special blocks");
        this.func_78025_a("item_search.png");
    }
}

