// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import java.util.Random;

public class TileEntityDispenser extends TileEntityLockableLoot
{
    private static final Random RNG;
    private NonNullList<ItemStack> stacks;
    
    public TileEntityDispenser() {
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    }
    
    @Override
    public int getSizeInventory() {
        return 9;
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public int getDispenseSlot() {
        this.fillWithLoot(null);
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.stacks.size(); ++k) {
            if (!this.stacks.get(k).isEmpty() && TileEntityDispenser.RNG.nextInt(j++) == 0) {
                i = k;
            }
        }
        return i;
    }
    
    public int addItemStack(final ItemStack stack) {
        for (int i = 0; i < this.stacks.size(); ++i) {
            if (this.stacks.get(i).isEmpty()) {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dispenser";
    }
    
    public static void registerFixes(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.stacks);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:dispenser";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerDispenser(playerInventory, this);
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }
    
    static {
        RNG = new Random();
    }
}
