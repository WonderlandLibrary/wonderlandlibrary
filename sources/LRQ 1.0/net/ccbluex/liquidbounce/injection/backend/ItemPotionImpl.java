/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionUtils
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemPotion;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemPotionImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

public final class ItemPotionImpl
extends ItemImpl<ItemPotion>
implements IItemPotion {
    @Override
    public Collection<IPotionEffect> getEffects(IItemStack stack) {
        IItemStack $this$unwrap$iv = stack;
        boolean $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getEffects.2.INSTANCE;
        Function1 function12 = getEffects.1.INSTANCE;
        Collection collection = PotionUtils.func_185189_a((ItemStack)itemStack);
        return new WrappedCollection(collection, function12, function1);
    }

    public ItemPotionImpl(ItemPotion wrapped) {
        super((Item)wrapped);
    }
}

