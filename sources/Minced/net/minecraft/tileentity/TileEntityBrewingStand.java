// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.potion.PotionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBrewingStand;
import java.util.Arrays;
import net.minecraft.init.Items;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_FOR_UP;
    private static final int[] SLOTS_FOR_DOWN;
    private static final int[] OUTPUT_SLOTS;
    private NonNullList<ItemStack> brewingItemStacks;
    private int brewTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;
    private int fuel;
    
    public TileEntityBrewingStand() {
        this.brewingItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.brewing";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }
    
    public void setName(final String name) {
        this.customName = name;
    }
    
    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.brewingItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void update() {
        final ItemStack itemstack = this.brewingItemStacks.get(4);
        if (this.fuel <= 0 && itemstack.getItem() == Items.BLAZE_POWDER) {
            this.fuel = 20;
            itemstack.shrink(1);
            this.markDirty();
        }
        final boolean flag = this.canBrew();
        final boolean flag2 = this.brewTime > 0;
        final ItemStack itemstack2 = this.brewingItemStacks.get(3);
        if (flag2) {
            --this.brewTime;
            final boolean flag3 = this.brewTime == 0;
            if (flag3 && flag) {
                this.brewPotions();
                this.markDirty();
            }
            else if (!flag) {
                this.brewTime = 0;
                this.markDirty();
            }
            else if (this.ingredientID != itemstack2.getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        }
        else if (flag && this.fuel > 0) {
            --this.fuel;
            this.brewTime = 400;
            this.ingredientID = itemstack2.getItem();
            this.markDirty();
        }
        if (!this.world.isRemote) {
            final boolean[] aboolean = this.createFilledSlotsArray();
            if (!Arrays.equals(aboolean, this.filledSlots)) {
                this.filledSlots = aboolean;
                IBlockState iblockstate = this.world.getBlockState(this.getPos());
                if (!(iblockstate.getBlock() instanceof BlockBrewingStand)) {
                    return;
                }
                for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; ++i) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[i], aboolean[i]);
                }
                this.world.setBlockState(this.pos, iblockstate, 2);
            }
        }
    }
    
    public boolean[] createFilledSlotsArray() {
        final boolean[] aboolean = new boolean[3];
        for (int i = 0; i < 3; ++i) {
            if (!this.brewingItemStacks.get(i).isEmpty()) {
                aboolean[i] = true;
            }
        }
        return aboolean;
    }
    
    private boolean canBrew() {
        final ItemStack itemstack = this.brewingItemStacks.get(3);
        if (itemstack.isEmpty()) {
            return false;
        }
        if (!PotionHelper.isReagent(itemstack)) {
            return false;
        }
        for (int i = 0; i < 3; ++i) {
            final ItemStack itemstack2 = this.brewingItemStacks.get(i);
            if (!itemstack2.isEmpty() && PotionHelper.hasConversions(itemstack2, itemstack)) {
                return true;
            }
        }
        return false;
    }
    
    private void brewPotions() {
        ItemStack itemstack = this.brewingItemStacks.get(3);
        for (int i = 0; i < 3; ++i) {
            this.brewingItemStacks.set(i, PotionHelper.doReaction(itemstack, this.brewingItemStacks.get(i)));
        }
        itemstack.shrink(1);
        final BlockPos blockpos = this.getPos();
        if (itemstack.getItem().hasContainerItem()) {
            final ItemStack itemstack2 = new ItemStack(itemstack.getItem().getContainerItem());
            if (itemstack.isEmpty()) {
                itemstack = itemstack2;
            }
            else {
                InventoryHelper.spawnItemStack(this.world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), itemstack2);
            }
        }
        this.brewingItemStacks.set(3, itemstack);
        this.world.playEvent(1035, blockpos, 0);
    }
    
    public static void registerFixesBrewingStand(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBrewingStand.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, this.brewingItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY));
        this.brewTime = compound.getShort("BrewTime");
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.fuel = compound.getByte("Fuel");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.brewTime);
        ItemStackHelper.saveAllItems(compound, this.brewingItemStacks);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        compound.setByte("Fuel", (byte)this.fuel);
        return compound;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= 0 && index < this.brewingItemStacks.size()) ? this.brewingItemStacks.get(index) : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return ItemStackHelper.getAndSplit(this.brewingItemStacks, index, count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.brewingItemStacks, index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        if (index >= 0 && index < this.brewingItemStacks.size()) {
            this.brewingItemStacks.set(index, stack);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
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
        if (index == 3) {
            return PotionHelper.isReagent(stack);
        }
        final Item item = stack.getItem();
        if (index == 4) {
            return item == Items.BLAZE_POWDER;
        }
        return (item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE) && this.getStackInSlot(index).isEmpty();
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        if (side == EnumFacing.UP) {
            return TileEntityBrewingStand.SLOTS_FOR_UP;
        }
        return (side == EnumFacing.DOWN) ? TileEntityBrewingStand.SLOTS_FOR_DOWN : TileEntityBrewingStand.OUTPUT_SLOTS;
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return index != 3 || stack.getItem() == Items.GLASS_BOTTLE;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:brewing_stand";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerBrewingStand(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        switch (id) {
            case 0: {
                return this.brewTime;
            }
            case 1: {
                return this.fuel;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void setField(final int id, final int value) {
        switch (id) {
            case 0: {
                this.brewTime = value;
                break;
            }
            case 1: {
                this.fuel = value;
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 2;
    }
    
    @Override
    public void clear() {
        this.brewingItemStacks.clear();
    }
    
    static {
        SLOTS_FOR_UP = new int[] { 3 };
        SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };
        OUTPUT_SLOTS = new int[] { 0, 1, 2, 4 };
    }
}
