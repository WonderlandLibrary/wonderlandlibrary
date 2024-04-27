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

public class ItemImpl<T extends Item>
implements IItem {
    private final T wrapped;

    @Override
    public String getUnlocalizedName() {
        return this.wrapped.func_77658_a();
    }

    @Override
    public IItemArmor asItemArmor() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
        }
        return new ItemArmorImpl((ItemArmor)t);
    }

    @Override
    public IItemPotion asItemPotion() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemPotion");
        }
        return new ItemPotionImpl((ItemPotion)t);
    }

    @Override
    public IItemBlock asItemBlock() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
        }
        return new ItemBlockImpl((ItemBlock)t);
    }

    @Override
    public IItemSword asItemSword() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemSword");
        }
        return new ItemSwordImpl((ItemSword)t);
    }

    @Override
    public IItemBucket asItemBucket() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBucket");
        }
        return new ItemBucketImpl((ItemBucket)t);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ItemImpl && ((ItemImpl)other).wrapped.equals(this.wrapped);
    }

    public final T getWrapped() {
        return this.wrapped;
    }

    public ItemImpl(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public IItem getItemByID(int id) {
        return IItem.DefaultImpls.getItemByID(this, id);
    }
}

