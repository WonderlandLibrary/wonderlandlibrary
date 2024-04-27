/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedListArrayAdapter;
import net.ccbluex.liquidbounce.injection.backend.InventoryPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0018\u001a\u00020\u000bH\u0016J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0096\u0002J\n\u0010\u001d\u001a\u0004\u0018\u00010\u0007H\u0016J\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0018\u001a\u00020\u000bH\u0016R\u001c\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR$\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\tR\u001c\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006\u001f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/InventoryPlayerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "wrapped", "Lnet/minecraft/entity/player/InventoryPlayer;", "(Lnet/minecraft/entity/player/InventoryPlayer;)V", "armorInventory", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getArmorInventory", "()Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "value", "", "currentItem", "getCurrentItem", "()I", "setCurrentItem", "(I)V", "mainInventory", "getMainInventory", "offHandInventory", "getOffHandInventory", "getWrapped", "()Lnet/minecraft/entity/player/InventoryPlayer;", "armorItemInSlot", "slot", "equals", "", "other", "", "getCurrentItemInHand", "getStackInSlot", "LiKingSense"})
public final class InventoryPlayerImpl
implements IInventoryPlayer {
    @NotNull
    private final InventoryPlayer wrapped;

    @Override
    @NotNull
    public IWrappedArray<IItemStack> getMainInventory() {
        NonNullList nonNullList = this.wrapped.field_70462_a;
        Intrinsics.checkExpressionValueIsNotNull((Object)nonNullList, (String)"wrapped.mainInventory");
        return new WrappedListArrayAdapter((List)nonNullList, mainInventory.1.INSTANCE, mainInventory.2.INSTANCE);
    }

    @Override
    @NotNull
    public IWrappedArray<IItemStack> getArmorInventory() {
        NonNullList nonNullList = this.wrapped.field_70460_b;
        Intrinsics.checkExpressionValueIsNotNull((Object)nonNullList, (String)"wrapped.armorInventory");
        return new WrappedListArrayAdapter((List)nonNullList, armorInventory.1.INSTANCE, armorInventory.2.INSTANCE);
    }

    @Override
    @NotNull
    public IWrappedArray<IItemStack> getOffHandInventory() {
        NonNullList nonNullList = this.wrapped.field_184439_c;
        Intrinsics.checkExpressionValueIsNotNull((Object)nonNullList, (String)"wrapped.offHandInventory");
        return new WrappedListArrayAdapter((List)nonNullList, offHandInventory.1.INSTANCE, offHandInventory.2.INSTANCE);
    }

    @Override
    public int getCurrentItem() {
        return this.wrapped.field_70461_c;
    }

    @Override
    public void setCurrentItem(int value) {
        this.wrapped.field_70461_c = value;
    }

    @Override
    @Nullable
    public IItemStack getStackInSlot(int slot) {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70301_a(slot);
        if (itemStack != null) {
            ItemStack $this$wrap$iv = itemStack;
            boolean $i$f$wrap = false;
            iItemStack = new ItemStackImpl($this$wrap$iv);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    @Nullable
    public IItemStack armorItemInSlot(int slot) {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70440_f(3 - slot);
        if (itemStack != null) {
            ItemStack $this$wrap$iv = itemStack;
            boolean $i$f$wrap = false;
            iItemStack = new ItemStackImpl($this$wrap$iv);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    @Nullable
    public IItemStack getCurrentItemInHand() {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70448_g();
        if (itemStack != null) {
            ItemStack $this$wrap$iv = itemStack;
            boolean $i$f$wrap = false;
            iItemStack = new ItemStackImpl($this$wrap$iv);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof InventoryPlayerImpl && Intrinsics.areEqual((Object)((InventoryPlayerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final InventoryPlayer getWrapped() {
        return this.wrapped;
    }

    public InventoryPlayerImpl(@NotNull InventoryPlayer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

