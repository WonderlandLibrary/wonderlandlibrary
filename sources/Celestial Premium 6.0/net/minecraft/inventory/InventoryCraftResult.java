/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryCraftResult
implements IInventory {
    private final NonNullList<ItemStack> stackResult = NonNullList.func_191197_a(1, ItemStack.field_190927_a);
    private IRecipe field_193057_b;

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.stackResult) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.stackResult.get(0);
    }

    @Override
    public String getName() {
        return "Result";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackResult.set(0, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.stackResult.clear();
    }

    public void func_193056_a(@Nullable IRecipe p_193056_1_) {
        this.field_193057_b = p_193056_1_;
    }

    @Nullable
    public IRecipe func_193055_i() {
        return this.field_193057_b;
    }
}

