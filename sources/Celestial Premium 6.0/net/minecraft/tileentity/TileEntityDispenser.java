/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser
extends TileEntityLockableLoot {
    private static final Random RNG = new Random();
    private NonNullList<ItemStack> stacks = NonNullList.func_191197_a(9, ItemStack.field_190927_a);

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.stacks) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public int getDispenseSlot() {
        this.fillWithLoot(null);
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.stacks.size(); ++k) {
            if (this.stacks.get(k).isEmpty() || RNG.nextInt(j++) != 0) continue;
            i = k;
        }
        return i;
    }

    public int addItemStack(ItemStack stack) {
        for (int i = 0; i < this.stacks.size(); ++i) {
            if (!this.stacks.get(i).isEmpty()) continue;
            this.setInventorySlotContents(i, stack);
            return i;
        }
        return -1;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.dispenser";
    }

    public static void registerFixes(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, "Items"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stacks = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.func_191283_b(compound, this.stacks);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.field_190577_o = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.func_191282_a(compound, this.stacks);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_190577_o);
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
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerDispenser(playerInventory, this);
    }

    @Override
    protected NonNullList<ItemStack> func_190576_q() {
        return this.stacks;
    }
}

