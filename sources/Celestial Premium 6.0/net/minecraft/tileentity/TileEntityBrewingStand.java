/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.Arrays;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;

public class TileEntityBrewingStand
extends TileEntityLockable
implements ITickable,
ISidedInventory {
    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2, 4};
    private NonNullList<ItemStack> brewingItemStacks = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
    private int brewTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;
    private int fuel;

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.brewing";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.size();
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.brewingItemStacks) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public void update() {
        boolean[] aboolean;
        ItemStack itemstack = this.brewingItemStacks.get(4);
        if (this.fuel <= 0 && itemstack.getItem() == Items.BLAZE_POWDER) {
            this.fuel = 20;
            itemstack.func_190918_g(1);
            this.markDirty();
        }
        boolean flag = this.canBrew();
        boolean flag1 = this.brewTime > 0;
        ItemStack itemstack1 = this.brewingItemStacks.get(3);
        if (flag1) {
            boolean flag2;
            --this.brewTime;
            boolean bl = flag2 = this.brewTime == 0;
            if (flag2 && flag) {
                this.brewPotions();
                this.markDirty();
            } else if (!flag) {
                this.brewTime = 0;
                this.markDirty();
            } else if (this.ingredientID != itemstack1.getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        } else if (flag && this.fuel > 0) {
            --this.fuel;
            this.brewTime = 400;
            this.ingredientID = itemstack1.getItem();
            this.markDirty();
        }
        if (!this.world.isRemote && !Arrays.equals(aboolean = this.createFilledSlotsArray(), this.filledSlots)) {
            this.filledSlots = aboolean;
            IBlockState iblockstate = this.world.getBlockState(this.getPos());
            if (!(iblockstate.getBlock() instanceof BlockBrewingStand)) {
                return;
            }
            for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; ++i) {
                iblockstate = iblockstate.withProperty(BlockBrewingStand.HAS_BOTTLE[i], aboolean[i]);
            }
            this.world.setBlockState(this.pos, iblockstate, 2);
        }
    }

    public boolean[] createFilledSlotsArray() {
        boolean[] aboolean = new boolean[3];
        for (int i = 0; i < 3; ++i) {
            if (this.brewingItemStacks.get(i).isEmpty()) continue;
            aboolean[i] = true;
        }
        return aboolean;
    }

    private boolean canBrew() {
        ItemStack itemstack = this.brewingItemStacks.get(3);
        if (itemstack.isEmpty()) {
            return false;
        }
        if (!PotionHelper.isReagent(itemstack)) {
            return false;
        }
        for (int i = 0; i < 3; ++i) {
            ItemStack itemstack1 = this.brewingItemStacks.get(i);
            if (itemstack1.isEmpty() || !PotionHelper.hasConversions(itemstack1, itemstack)) continue;
            return true;
        }
        return false;
    }

    private void brewPotions() {
        ItemStack itemstack = this.brewingItemStacks.get(3);
        for (int i = 0; i < 3; ++i) {
            this.brewingItemStacks.set(i, PotionHelper.doReaction(itemstack, this.brewingItemStacks.get(i)));
        }
        itemstack.func_190918_g(1);
        BlockPos blockpos = this.getPos();
        if (itemstack.getItem().hasContainerItem()) {
            ItemStack itemstack1 = new ItemStack(itemstack.getItem().getContainerItem());
            if (itemstack.isEmpty()) {
                itemstack = itemstack1;
            } else {
                InventoryHelper.spawnItemStack(this.world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), itemstack1);
            }
        }
        this.brewingItemStacks.set(3, itemstack);
        this.world.playEvent(1035, blockpos, 0);
    }

    public static void registerFixesBrewingStand(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBrewingStand.class, "Items"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.brewingItemStacks = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        ItemStackHelper.func_191283_b(compound, this.brewingItemStacks);
        this.brewTime = compound.getShort("BrewTime");
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.fuel = compound.getByte("Fuel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.brewTime);
        ItemStackHelper.func_191282_a(compound, this.brewingItemStacks);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        compound.setByte("Fuel", (byte)this.fuel);
        return compound;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.brewingItemStacks.size() ? this.brewingItemStacks.get(index) : ItemStack.field_190927_a;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.brewingItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.brewingItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.brewingItemStacks.size()) {
            this.brewingItemStacks.set(index, stack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
        if (index == 3) {
            return PotionHelper.isReagent(stack);
        }
        Item item = stack.getItem();
        if (index == 4) {
            return item == Items.BLAZE_POWDER;
        }
        return (item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE) && this.getStackInSlot(index).isEmpty();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return SLOTS_FOR_UP;
        }
        return side == EnumFacing.DOWN ? SLOTS_FOR_DOWN : OUTPUT_SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 3) {
            return stack.getItem() == Items.GLASS_BOTTLE;
        }
        return true;
    }

    @Override
    public String getGuiID() {
        return "minecraft:brewing_stand";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerBrewingStand(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: {
                return this.brewTime;
            }
            case 1: {
                return this.fuel;
            }
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.brewTime = value;
                break;
            }
            case 1: {
                this.fuel = value;
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
}

