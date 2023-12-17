// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.WorldServer;
import java.util.Random;
import net.minecraft.world.LockCode;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumHand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.ILockableContainer;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer
{
    private NonNullList<ItemStack> minecartContainerItems;
    private boolean dropContentsWhenDead;
    private ResourceLocation lootTable;
    private long lootTableSeed;
    
    public EntityMinecartContainer(final World worldIn) {
        super(worldIn);
        this.minecartContainerItems = NonNullList.withSize(36, ItemStack.EMPTY);
        this.dropContentsWhenDead = true;
    }
    
    public EntityMinecartContainer(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.minecartContainerItems = NonNullList.withSize(36, ItemStack.EMPTY);
        this.dropContentsWhenDead = true;
    }
    
    @Override
    public void killMinecart(final DamageSource source) {
        super.killMinecart(source);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.minecartContainerItems) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        this.addLoot(null);
        return this.minecartContainerItems.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        this.addLoot(null);
        return ItemStackHelper.getAndSplit(this.minecartContainerItems, index, count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        this.addLoot(null);
        final ItemStack itemstack = this.minecartContainerItems.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.minecartContainerItems.set(index, ItemStack.EMPTY);
        return itemstack;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.addLoot(null);
        this.minecartContainerItems.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return !this.isDead && player.getDistanceSq(this) <= 64.0;
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
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Nullable
    @Override
    public Entity changeDimension(final int dimensionIn) {
        this.dropContentsWhenDead = false;
        return super.changeDimension(dimensionIn);
    }
    
    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }
        super.setDead();
    }
    
    @Override
    public void setDropItemsWhenDead(final boolean dropWhenDead) {
        this.dropContentsWhenDead = dropWhenDead;
    }
    
    public static void addDataFixers(final DataFixer p_190574_0_, final Class<?> p_190574_1_) {
        EntityMinecart.registerFixesMinecart(p_190574_0_, p_190574_1_);
        p_190574_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_190574_1_, new String[] { "Items" }));
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.lootTable != null) {
            compound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }
        }
        else {
            ItemStackHelper.saveAllItems(compound, this.minecartContainerItems);
        }
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.minecartContainerItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (compound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        }
        else {
            ItemStackHelper.loadAllItems(compound, this.minecartContainerItems);
        }
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (!this.world.isRemote) {
            player.displayGUIChest(this);
        }
        return true;
    }
    
    @Override
    protected void applyDrag() {
        float f = 0.98f;
        if (this.lootTable == null) {
            final int i = 15 - Container.calcRedstoneFromInventory(this);
            f += i * 0.001f;
        }
        this.motionX *= f;
        this.motionY *= 0.0;
        this.motionZ *= f;
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
    public boolean isLocked() {
        return false;
    }
    
    @Override
    public void setLockCode(final LockCode code) {
    }
    
    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }
    
    public void addLoot(@Nullable final EntityPlayer player) {
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
    public void clear() {
        this.addLoot(null);
        this.minecartContainerItems.clear();
    }
    
    public void setLootTable(final ResourceLocation lootTableIn, final long lootTableSeedIn) {
        this.lootTable = lootTableIn;
        this.lootTableSeed = lootTableSeedIn;
    }
    
    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }
}
