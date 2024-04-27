/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.EnchantmentImpl;
import net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagListImpl;
import net.ccbluex.liquidbounce.injection.implementations.IMixinItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.Nullable;

public final class ItemStackImpl
implements IItemStack {
    private final ItemStack wrapped;

    @Override
    public int getStackSize() {
        return this.wrapped.field_77994_a;
    }

    @Override
    public int getItemDamage() {
        return this.wrapped.func_77952_i();
    }

    @Override
    public int getMaxItemUseDuration() {
        return this.wrapped.func_77988_m();
    }

    @Override
    public String getDisplayName() {
        return this.wrapped.func_82833_r();
    }

    public ItemStackImpl(ItemStack itemStack) {
        this.wrapped = itemStack;
    }

    public final ItemStack getWrapped() {
        return this.wrapped;
    }

    @Override
    public boolean isSplash() {
        return this.wrapped.func_77973_b().equals(Items.field_185155_bH);
    }

    @Override
    public INBTTagList getEnchantmentTagList() {
        INBTTagList iNBTTagList;
        NBTTagList nBTTagList = this.wrapped.func_77986_q();
        if (nBTTagList != null) {
            NBTTagList nBTTagList2 = nBTTagList;
            boolean bl = false;
            iNBTTagList = new NBTTagListImpl(nBTTagList2);
        } else {
            iNBTTagList = null;
        }
        return iNBTTagList;
    }

    @Override
    public float getStrVsBlock(IIBlockState iIBlockState) {
        IIBlockState iIBlockState2 = iIBlockState;
        ItemStack itemStack = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)iIBlockState2).getWrapped();
        return itemStack.func_150997_a(iBlockState);
    }

    @Override
    public IItemStack setStackDisplayName(String string) {
        ItemStack itemStack = this.wrapped.func_151001_c(string);
        boolean bl = false;
        return new ItemStackImpl(itemStack);
    }

    @Override
    public String getUnlocalizedName() {
        return this.wrapped.func_77977_a();
    }

    @Override
    public Collection getAttributeModifier(String string) {
        return new WrappedCollection(this.wrapped.func_111283_C(EntityEquipmentSlot.MAINHAND).get((Object)string), getAttributeModifier.1.INSTANCE, getAttributeModifier.2.INSTANCE);
    }

    @Override
    public void setTagInfo(String string, INBTBase iNBTBase) {
        INBTBase iNBTBase2 = iNBTBase;
        String string2 = string;
        ItemStack itemStack = this.wrapped;
        boolean bl = false;
        NBTBase nBTBase = ((NBTBaseImpl)iNBTBase2).getWrapped();
        itemStack.func_77983_a(string2, nBTBase);
    }

    @Override
    public INBTTagCompound getTagCompound() {
        INBTTagCompound iNBTTagCompound;
        NBTTagCompound nBTTagCompound = this.wrapped.func_77978_p();
        if (nBTTagCompound != null) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound;
            boolean bl = false;
            iNBTTagCompound = new NBTTagCompoundImpl(nBTTagCompound2);
        } else {
            iNBTTagCompound = null;
        }
        return iNBTTagCompound;
    }

    @Override
    public void setTagCompound(@Nullable INBTTagCompound iNBTTagCompound) {
        NBTTagCompound nBTTagCompound;
        ItemStack itemStack = this.wrapped;
        INBTTagCompound iNBTTagCompound2 = iNBTTagCompound;
        if (iNBTTagCompound2 != null) {
            INBTTagCompound iNBTTagCompound3 = iNBTTagCompound2;
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            INBTTagCompound iNBTTagCompound4 = iNBTTagCompound3;
            if (iNBTTagCompound4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl");
            }
            NBTTagCompound nBTTagCompound2 = (NBTTagCompound)((NBTTagCompoundImpl)iNBTTagCompound4).getWrapped();
            itemStack = itemStack2;
            nBTTagCompound = nBTTagCompound2;
        } else {
            nBTTagCompound = null;
        }
        itemStack.func_77982_d(nBTTagCompound);
    }

    @Override
    public IItem getItem() {
        IItem iItem;
        Item item = this.wrapped.func_77973_b();
        if (item != null) {
            Item item2 = item;
            boolean bl = false;
            iItem = new ItemImpl(item2);
        } else {
            iItem = null;
        }
        return iItem;
    }

    @Override
    public void addEnchantment(IEnchantment iEnchantment, int n) {
        IEnchantment iEnchantment2 = iEnchantment;
        ItemStack itemStack = this.wrapped;
        boolean bl = false;
        Enchantment enchantment = ((EnchantmentImpl)iEnchantment2).getWrapped();
        itemStack.func_77966_a(enchantment, n);
    }

    @Override
    public long getItemDelay() {
        ItemStack itemStack = this.wrapped;
        if (itemStack == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinItemStack");
        }
        return ((IMixinItemStack)itemStack).getItemDelay();
    }

    @Override
    public void setItemDamage(int n) {
        this.wrapped.func_77964_b(n);
    }
}

