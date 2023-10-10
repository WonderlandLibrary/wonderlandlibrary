/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityBrewingStand
extends TileEntityLockable
implements ITickable,
ISidedInventory {
    private static final int[] inputSlots = new int[]{3};
    private static final int[] outputSlots = new int[]{0, 1, 2};
    private ItemStack[] brewingItemStacks = new ItemStack[4];
    private int brewTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;

    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.brewing";
        String string = this.customName;
        return string;
    }

    @Override
    public boolean hasCustomName() {
        if (this.customName == null) return false;
        if (this.customName.length() <= 0) return false;
        return true;
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.length;
    }

    @Override
    public void update() {
        if (this.brewTime > 0) {
            --this.brewTime;
            if (this.brewTime == 0) {
                this.brewPotions();
                this.markDirty();
            } else if (!this.canBrew()) {
                this.brewTime = 0;
                this.markDirty();
            } else if (this.ingredientID != this.brewingItemStacks[3].getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        } else if (this.canBrew()) {
            this.brewTime = 400;
            this.ingredientID = this.brewingItemStacks[3].getItem();
        }
        if (this.worldObj.isRemote) return;
        boolean[] aboolean = this.func_174902_m();
        if (Arrays.equals(aboolean, this.filledSlots)) return;
        this.filledSlots = aboolean;
        IBlockState iblockstate = this.worldObj.getBlockState(this.getPos());
        if (!(iblockstate.getBlock() instanceof BlockBrewingStand)) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= BlockBrewingStand.HAS_BOTTLE.length) {
                this.worldObj.setBlockState(this.pos, iblockstate, 2);
                return;
            }
            iblockstate = iblockstate.withProperty(BlockBrewingStand.HAS_BOTTLE[i], aboolean[i]);
            ++i;
        }
    }

    private boolean canBrew() {
        if (this.brewingItemStacks[3] == null) return false;
        if (this.brewingItemStacks[3].stackSize <= 0) return false;
        ItemStack itemstack = this.brewingItemStacks[3];
        if (!itemstack.getItem().isPotionIngredient(itemstack)) {
            return false;
        }
        boolean flag = false;
        int i = 0;
        while (i < 3) {
            if (this.brewingItemStacks[i] != null && this.brewingItemStacks[i].getItem() == Items.potionitem) {
                int j = this.brewingItemStacks[i].getMetadata();
                int k = this.getPotionResult(j, itemstack);
                if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
                    return true;
                }
                List<PotionEffect> list = Items.potionitem.getEffects(j);
                List<PotionEffect> list1 = Items.potionitem.getEffects(k);
                if (!(j > 0 && list == list1 || list != null && (list.equals(list1) || list1 == null) || j == k)) {
                    return true;
                }
            }
            ++i;
        }
        return flag;
    }

    private void brewPotions() {
        if (!this.canBrew()) return;
        ItemStack itemstack = this.brewingItemStacks[3];
        for (int i = 0; i < 3; ++i) {
            if (this.brewingItemStacks[i] == null || this.brewingItemStacks[i].getItem() != Items.potionitem) continue;
            int j = this.brewingItemStacks[i].getMetadata();
            int k = this.getPotionResult(j, itemstack);
            List<PotionEffect> list = Items.potionitem.getEffects(j);
            List<PotionEffect> list1 = Items.potionitem.getEffects(k);
            if (j > 0 && list == list1 || list != null && (list.equals(list1) || list1 == null)) {
                if (ItemPotion.isSplash(j) || !ItemPotion.isSplash(k)) continue;
                this.brewingItemStacks[i].setItemDamage(k);
                continue;
            }
            if (j == k) continue;
            this.brewingItemStacks[i].setItemDamage(k);
        }
        if (itemstack.getItem().hasContainerItem()) {
            this.brewingItemStacks[3] = new ItemStack(itemstack.getItem().getContainerItem());
            return;
        }
        --this.brewingItemStacks[3].stackSize;
        if (this.brewingItemStacks[3].stackSize > 0) return;
        this.brewingItemStacks[3] = null;
    }

    private int getPotionResult(int meta, ItemStack stack) {
        int n;
        if (stack == null) {
            n = meta;
            return n;
        }
        if (stack.getItem().isPotionIngredient(stack)) {
            n = PotionHelper.applyIngredient(meta, stack.getItem().getPotionEffect(stack));
            return n;
        }
        n = meta;
        return n;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.brewingItemStacks = new ItemStack[this.getSizeInventory()];
        int i = 0;
        while (true) {
            if (i >= nbttaglist.tagCount()) {
                this.brewTime = compound.getShort("BrewTime");
                if (!compound.hasKey("CustomName", 8)) return;
                this.customName = compound.getString("CustomName");
                return;
            }
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            byte j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.brewingItemStacks.length) {
                this.brewingItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.brewTime);
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.brewingItemStacks.length) {
                compound.setTag("Items", nbttaglist);
                if (!this.hasCustomName()) return;
                compound.setString("CustomName", this.customName);
                return;
            }
            if (this.brewingItemStacks[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.brewingItemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0) return null;
        if (index >= this.brewingItemStacks.length) return null;
        ItemStack itemStack = this.brewingItemStacks[index];
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index < 0) return null;
        if (index >= this.brewingItemStacks.length) return null;
        ItemStack itemstack = this.brewingItemStacks[index];
        this.brewingItemStacks[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index < 0) return null;
        if (index >= this.brewingItemStacks.length) return null;
        ItemStack itemstack = this.brewingItemStacks[index];
        this.brewingItemStacks[index] = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0) return;
        if (index >= this.brewingItemStacks.length) return;
        this.brewingItemStacks[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj.getTileEntity(this.pos) != this) {
            return false;
        }
        if (!(player.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0)) return false;
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
        if (index == 3) {
            boolean bl = stack.getItem().isPotionIngredient(stack);
            return bl;
        }
        if (stack.getItem() == Items.potionitem) return true;
        if (stack.getItem() == Items.glass_bottle) return true;
        return false;
    }

    public boolean[] func_174902_m() {
        boolean[] aboolean = new boolean[3];
        int i = 0;
        while (i < 3) {
            if (this.brewingItemStacks[i] != null) {
                aboolean[i] = true;
            }
            ++i;
        }
        return aboolean;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] nArray;
        if (side == EnumFacing.UP) {
            nArray = inputSlots;
            return nArray;
        }
        nArray = outputSlots;
        return nArray;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
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
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.brewTime = value;
                return;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        int i = 0;
        while (i < this.brewingItemStacks.length) {
            this.brewingItemStacks[i] = null;
            ++i;
        }
    }
}

