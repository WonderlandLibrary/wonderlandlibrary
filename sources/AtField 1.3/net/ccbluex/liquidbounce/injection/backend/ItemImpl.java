/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemSword
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBucket;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemPotion;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemSword;
import net.ccbluex.liquidbounce.injection.backend.ItemArmorImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemBlockImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemBucketImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemPotionImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemSwordImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import org.jetbrains.annotations.Nullable;

public class ItemImpl
implements IItem {
    private final Item wrapped;

    @Override
    public IItemSword asItemSword() {
        Item item = this.wrapped;
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemSword");
        }
        return new ItemSwordImpl((ItemSword)item);
    }

    @Override
    public IItemBucket asItemBucket() {
        Item item = this.wrapped;
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBucket");
        }
        return new ItemBucketImpl((ItemBucket)item);
    }

    @Override
    public IItemPotion asItemPotion() {
        Item item = this.wrapped;
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemPotion");
        }
        return new ItemPotionImpl((ItemPotion)item);
    }

    @Override
    public IItemBlock asItemBlock() {
        Item item = this.wrapped;
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
        }
        return new ItemBlockImpl((ItemBlock)item);
    }

    @Override
    public String getUnlocalizedName() {
        return this.wrapped.func_77658_a();
    }

    @Override
    public IItem getItemByID(int n) {
        return IItem.DefaultImpls.getItemByID(this, n);
    }

    public ItemImpl(Item item) {
        this.wrapped = item;
    }

    @Override
    public IItemArmor asItemArmor() {
        Item item = this.wrapped;
        if (item == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
        }
        return new ItemArmorImpl((ItemArmor)item);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).wrapped.equals(this.wrapped);
    }

    public final Item getWrapped() {
        return this.wrapped;
    }
}

