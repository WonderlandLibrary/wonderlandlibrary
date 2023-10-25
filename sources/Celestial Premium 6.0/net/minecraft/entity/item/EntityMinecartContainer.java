/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityMinecartContainer
extends EntityMinecart
implements ILockableContainer,
ILootContainer {
    private NonNullList<ItemStack> minecartContainerItems = NonNullList.func_191197_a(36, ItemStack.field_190927_a);
    private boolean dropContentsWhenDead = true;
    private ResourceLocation lootTable;
    private long lootTableSeed;

    public EntityMinecartContainer(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartContainer(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.dropInventoryItems(this.world, this, (IInventory)this);
        }
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.minecartContainerItems) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        this.addLoot(null);
        return this.minecartContainerItems.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        this.addLoot(null);
        return ItemStackHelper.getAndSplit(this.minecartContainerItems, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        this.addLoot(null);
        ItemStack itemstack = this.minecartContainerItems.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.field_190927_a;
        }
        this.minecartContainerItems.set(index, ItemStack.field_190927_a);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.addLoot(null);
        this.minecartContainerItems.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.isDead) {
            return false;
        }
        return player.getDistanceSqToEntity(this) <= 64.0;
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    @Nullable
    public Entity changeDimension(int dimensionIn) {
        this.dropContentsWhenDead = false;
        return super.changeDimension(dimensionIn);
    }

    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.dropInventoryItems(this.world, this, (IInventory)this);
        }
        super.setDead();
    }

    @Override
    public void setDropItemsWhenDead(boolean dropWhenDead) {
        this.dropContentsWhenDead = dropWhenDead;
    }

    public static void func_190574_b(DataFixer p_190574_0_, Class<?> p_190574_1_) {
        EntityMinecart.registerFixesMinecart(p_190574_0_, p_190574_1_);
        p_190574_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_190574_1_, "Items"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.lootTable != null) {
            compound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }
        } else {
            ItemStackHelper.func_191282_a(compound, this.minecartContainerItems);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.minecartContainerItems = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (compound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        } else {
            ItemStackHelper.func_191283_b(compound, this.minecartContainerItems);
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        if (!this.world.isRemote) {
            player.displayGUIChest(this);
        }
        return true;
    }

    @Override
    protected void applyDrag() {
        float f = 0.98f;
        if (this.lootTable == null) {
            int i = 15 - Container.calcRedstoneFromInventory(this);
            f += (float)i * 0.001f;
        }
        this.motionX *= (double)f;
        this.motionY *= 0.0;
        this.motionZ *= (double)f;
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
    public boolean isLocked() {
        return false;
    }

    @Override
    public void setLockCode(LockCode code) {
    }

    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    public void addLoot(@Nullable EntityPlayer player) {
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
    public void clear() {
        this.addLoot(null);
        this.minecartContainerItems.clear();
    }

    public void setLootTable(ResourceLocation lootTableIn, long lootTableSeedIn) {
        this.lootTable = lootTableIn;
        this.lootTableSeed = lootTableSeedIn;
    }

    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }
}

