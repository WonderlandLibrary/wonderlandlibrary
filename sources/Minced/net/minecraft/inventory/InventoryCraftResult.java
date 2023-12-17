// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryCraftResult implements IInventory
{
    private final NonNullList<ItemStack> stackResult;
    private IRecipe recipeUsed;
    
    public InventoryCraftResult() {
        this.stackResult = NonNullList.withSize(1, ItemStack.EMPTY);
    }
    
    @Override
    public int getSizeInventory() {
        return 1;
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.stackResult) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
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
    public ItemStack decrStackSize(final int index, final int count) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
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
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        this.stackResult.clear();
    }
    
    public void setRecipeUsed(@Nullable final IRecipe p_193056_1_) {
        this.recipeUsed = p_193056_1_;
    }
    
    @Nullable
    public IRecipe getRecipeUsed() {
        return this.recipeUsed;
    }
}
