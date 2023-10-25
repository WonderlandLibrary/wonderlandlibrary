/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class TileEntityLockableLoot
extends TileEntityLockable
implements ILootContainer {
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    protected String field_190577_o;

    protected boolean checkLootAndRead(NBTTagCompound compound) {
        if (compound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        return false;
    }

    protected boolean checkLootAndWrite(NBTTagCompound compound) {
        if (this.lootTable != null) {
            compound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }
            return true;
        }
        return false;
    }

    public void fillWithLoot(@Nullable EntityPlayer player) {
        if (this.lootTable != null) {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random = this.lootTableSeed == 0L ? new Random() : new Random(this.lootTableSeed);
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
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

    public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
        this.lootTable = p_189404_1_;
        this.lootTableSeed = p_189404_2_;
    }

    @Override
    public boolean hasCustomName() {
        return this.field_190577_o != null && !this.field_190577_o.isEmpty();
    }

    public void func_190575_a(String p_190575_1_) {
        this.field_190577_o = p_190575_1_;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        this.fillWithLoot(null);
        return this.func_190576_q().get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        this.fillWithLoot(null);
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.func_190576_q(), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        this.fillWithLoot(null);
        return ItemStackHelper.getAndRemove(this.func_190576_q(), index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        this.fillWithLoot(null);
        this.func_190576_q().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        return player.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
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
        this.fillWithLoot(null);
        this.func_190576_q().clear();
    }

    protected abstract NonNullList<ItemStack> func_190576_q();
}

