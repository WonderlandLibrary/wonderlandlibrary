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
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.Nullable;

public final class ItemStackImpl
implements IItemStack {
    private final ItemStack wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public float getStrVsBlock(IIBlockState block) {
        void $this$unwrap$iv;
        IIBlockState iIBlockState = block;
        ItemStack itemStack = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        return itemStack.func_150997_a(iBlockState);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setTagInfo(String key, INBTBase nbt) {
        void $this$unwrap$iv;
        INBTBase iNBTBase = nbt;
        String string = key;
        ItemStack itemStack = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((NBTBaseImpl)$this$unwrap$iv).getWrapped();
        itemStack.func_77983_a(string, t);
    }

    @Override
    public IItemStack setStackDisplayName(String displayName) {
        ItemStack $this$wrap$iv = this.wrapped.func_151001_c(displayName);
        boolean $i$f$wrap = false;
        return new ItemStackImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void addEnchantment(IEnchantment enchantment, int level) {
        void $this$unwrap$iv;
        IEnchantment iEnchantment = enchantment;
        ItemStack itemStack = this.wrapped;
        boolean $i$f$unwrap = false;
        Enchantment enchantment2 = ((EnchantmentImpl)$this$unwrap$iv).getWrapped();
        itemStack.func_77966_a(enchantment2, level);
    }

    @Override
    public Collection<IAttributeModifier> getAttributeModifier(String key) {
        return new WrappedCollection(this.wrapped.func_111283_C(EntityEquipmentSlot.MAINHAND).get((Object)key), getAttributeModifier.1.INSTANCE, getAttributeModifier.2.INSTANCE);
    }

    @Override
    public boolean isSplash() {
        return this.wrapped.func_77973_b().equals(Items.field_185155_bH);
    }

    @Override
    public String getDisplayName() {
        return this.wrapped.func_82833_r();
    }

    @Override
    public String getUnlocalizedName() {
        return this.wrapped.func_77977_a();
    }

    @Override
    public int getMaxItemUseDuration() {
        return this.wrapped.func_77988_m();
    }

    @Override
    public INBTTagList getEnchantmentTagList() {
        INBTTagList iNBTTagList;
        NBTTagList nBTTagList = this.wrapped.func_77986_q();
        if (nBTTagList != null) {
            NBTTagList $this$wrap$iv = nBTTagList;
            boolean $i$f$wrap = false;
            iNBTTagList = new NBTTagListImpl($this$wrap$iv);
        } else {
            iNBTTagList = null;
        }
        return iNBTTagList;
    }

    @Override
    public INBTTagCompound getTagCompound() {
        INBTTagCompound iNBTTagCompound;
        NBTTagCompound nBTTagCompound = this.wrapped.func_77978_p();
        if (nBTTagCompound != null) {
            NBTTagCompound $this$wrap$iv = nBTTagCompound;
            boolean $i$f$wrap = false;
            iNBTTagCompound = new NBTTagCompoundImpl($this$wrap$iv);
        } else {
            iNBTTagCompound = null;
        }
        return iNBTTagCompound;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setTagCompound(@Nullable INBTTagCompound value) {
        NBTTagCompound nBTTagCompound;
        ItemStack itemStack = this.wrapped;
        INBTTagCompound iNBTTagCompound = value;
        if (iNBTTagCompound != null) {
            void $this$unwrap$iv;
            INBTTagCompound iNBTTagCompound2 = iNBTTagCompound;
            ItemStack itemStack2 = itemStack;
            boolean $i$f$unwrap = false;
            void v2 = $this$unwrap$iv;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl");
            }
            NBTTagCompound nBTTagCompound2 = (NBTTagCompound)((NBTTagCompoundImpl)v2).getWrapped();
            itemStack = itemStack2;
            nBTTagCompound = nBTTagCompound2;
        } else {
            nBTTagCompound = null;
        }
        itemStack.func_77982_d(nBTTagCompound);
    }

    @Override
    public int getStackSize() {
        return this.wrapped.field_77994_a;
    }

    @Override
    public int getItemDamage() {
        return this.wrapped.func_77952_i();
    }

    @Override
    public void setItemDamage(int value) {
        this.wrapped.func_77964_b(value);
    }

    @Override
    public IItem getItem() {
        IItem iItem;
        Item item = this.wrapped.func_77973_b();
        if (item != null) {
            Item $this$wrap$iv = item;
            boolean $i$f$wrap = false;
            iItem = new ItemImpl<Item>($this$wrap$iv);
        } else {
            iItem = null;
        }
        return iItem;
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
    public int getMaxDamage() {
        return this.wrapped.func_77958_k();
    }

    @Override
    public void setMaxDamage(int value) {
    }

    public final ItemStack getWrapped() {
        return this.wrapped;
    }

    public ItemStackImpl(ItemStack wrapped) {
        this.wrapped = wrapped;
    }
}

