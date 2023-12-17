// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.NonNullList;
import java.util.List;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.WorldServer;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ILootContainer;

public abstract class TileEntityLockableLoot extends TileEntityLockable implements ILootContainer
{
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    protected String customName;
    
    protected boolean checkLootAndRead(final NBTTagCompound compound) {
        if (compound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        return false;
    }
    
    protected boolean checkLootAndWrite(final NBTTagCompound compound) {
        if (this.lootTable != null) {
            compound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }
            return true;
        }
        return false;
    }
    
    public void fillWithLoot(@Nullable final EntityPlayer player) {
        if (this.lootTable != null) {
            final LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random;
            if (this.lootTableSeed == 0L) {
                random = new Random();
            }
            else {
                random = new Random(this.lootTableSeed);
            }
            final LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
            if (player != null) {
                lootcontext$builder.withLuck(player.getLuck());
            }
            loottable.fillInventory(this, random, lootcontext$builder.build());
        }
    }
    
    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }
    
    public void setLootTable(final ResourceLocation p_189404_1_, final long p_189404_2_) {
        this.lootTable = p_189404_1_;
        this.lootTableSeed = p_189404_2_;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }
    
    public void setCustomName(final String p_190575_1_) {
        this.customName = p_190575_1_;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        this.fillWithLoot(null);
        return this.getItems().get(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        this.fillWithLoot(null);
        final ItemStack itemstack = ItemStackHelper.getAndSplit(this.getItems(), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        this.fillWithLoot(null);
        return ItemStackHelper.getAndRemove(this.getItems(), index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, @Nullable final ItemStack stack) {
        this.fillWithLoot(null);
        this.getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
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
        this.fillWithLoot(null);
        this.getItems().clear();
    }
    
    protected abstract NonNullList<ItemStack> getItems();
}
