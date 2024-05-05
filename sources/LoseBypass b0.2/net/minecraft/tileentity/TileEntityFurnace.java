/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;

public class TileEntityFurnace
extends TileEntityLockable
implements ITickable,
ISidedInventory {
    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsBottom = new int[]{2, 1};
    private static final int[] slotsSides = new int[]{1};
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.furnaceItemStacks[index] == null) return null;
        if (this.furnaceItemStacks[index].stackSize <= count) {
            ItemStack itemstack1 = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return itemstack1;
        }
        ItemStack itemstack = this.furnaceItemStacks[index].splitStack(count);
        if (this.furnaceItemStacks[index].stackSize != 0) return itemstack;
        this.furnaceItemStacks[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.furnaceItemStacks[index] == null) return null;
        ItemStack itemstack = this.furnaceItemStacks[index];
        this.furnaceItemStacks[index] = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]);
        this.furnaceItemStacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (index != 0) return;
        if (flag) return;
        this.totalCookTime = this.getCookTime(stack);
        this.cookTime = 0;
        this.markDirty();
    }

    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.furnace";
        String string = this.furnaceCustomName;
        return string;
    }

    @Override
    public boolean hasCustomName() {
        if (this.furnaceCustomName == null) return false;
        if (this.furnaceCustomName.length() <= 0) return false;
        return true;
    }

    public void setCustomInventoryName(String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
        int i = 0;
        while (true) {
            if (i >= nbttaglist.tagCount()) {
                this.furnaceBurnTime = compound.getShort("BurnTime");
                this.cookTime = compound.getShort("CookTime");
                this.totalCookTime = compound.getShort("CookTimeTotal");
                this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
                if (!compound.hasKey("CustomName", 8)) return;
                this.furnaceCustomName = compound.getString("CustomName");
                return;
            }
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            byte j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.cookTime);
        compound.setShort("CookTimeTotal", (short)this.totalCookTime);
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.furnaceItemStacks.length) {
                compound.setTag("Items", nbttaglist);
                if (!this.hasCustomName()) return;
                compound.setString("CustomName", this.furnaceCustomName);
                return;
            }
            if (this.furnaceItemStacks[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning() {
        if (this.furnaceBurnTime <= 0) return false;
        return true;
    }

    public static boolean isBurning(IInventory p_174903_0_) {
        if (p_174903_0_.getField(0) <= 0) return false;
        return true;
    }

    @Override
    public void update() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if (!this.worldObj.isRemote) {
            if (this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
                    if (this.isBurning()) {
                        flag1 = true;
                        if (this.furnaceItemStacks[1] != null) {
                            --this.furnaceItemStacks[1].stackSize;
                            if (this.furnaceItemStacks[1].stackSize == 0) {
                                Item item = this.furnaceItemStacks[1].getItem().getContainerItem();
                                ItemStack itemStack = this.furnaceItemStacks[1] = item != null ? new ItemStack(item) : null;
                            }
                        }
                    }
                }
                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;
                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks[0]);
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (flag != this.isBurning()) {
                flag1 = true;
                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }
        if (!flag1) return;
        this.markDirty();
    }

    public int getCookTime(ItemStack stack) {
        return 200;
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        }
        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
        if (itemstack == null) {
            return false;
        }
        if (this.furnaceItemStacks[2] == null) {
            return true;
        }
        if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) {
            return false;
        }
        if (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize()) {
            return true;
        }
        if (this.furnaceItemStacks[2].stackSize >= itemstack.getMaxStackSize()) return false;
        return true;
    }

    public void smeltItem() {
        if (!this.canSmelt()) return;
        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
        if (this.furnaceItemStacks[2] == null) {
            this.furnaceItemStacks[2] = itemstack.copy();
        } else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
            ++this.furnaceItemStacks[2].stackSize;
        }
        if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket) {
            this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
        }
        --this.furnaceItemStacks[0].stackSize;
        if (this.furnaceItemStacks[0].stackSize > 0) return;
        this.furnaceItemStacks[0] = null;
    }

    public static int getItemBurnTime(ItemStack p_145952_0_) {
        if (p_145952_0_ == null) {
            return 0;
        }
        Item item = p_145952_0_.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);
            if (block == Blocks.wooden_slab) {
                return 150;
            }
            if (block.getMaterial() == Material.wood) {
                return 300;
            }
            if (block == Blocks.coal_block) {
                return 16000;
            }
        }
        if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) {
            return 200;
        }
        if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) {
            return 200;
        }
        if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) {
            return 200;
        }
        if (item == Items.stick) {
            return 100;
        }
        if (item == Items.coal) {
            return 1600;
        }
        if (item == Items.lava_bucket) {
            return 20000;
        }
        if (item == Item.getItemFromBlock(Blocks.sapling)) {
            return 100;
        }
        if (item != Items.blaze_rod) return 0;
        return 2400;
    }

    public static boolean isItemFuel(ItemStack p_145954_0_) {
        if (TileEntityFurnace.getItemBurnTime(p_145954_0_) <= 0) return false;
        return true;
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
        if (index == 2) {
            return false;
        }
        if (index != 1) {
            return true;
        }
        if (TileEntityFurnace.isItemFuel(stack)) return true;
        if (SlotFurnaceFuel.isBucket(stack)) return true;
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] nArray;
        if (side == EnumFacing.DOWN) {
            nArray = slotsBottom;
            return nArray;
        }
        if (side == EnumFacing.UP) {
            nArray = slotsTop;
            return nArray;
        }
        nArray = slotsSides;
        return nArray;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction != EnumFacing.DOWN) return true;
        if (index != 1) return true;
        Item item = stack.getItem();
        if (item == Items.water_bucket) return true;
        if (item == Items.bucket) return true;
        return false;
    }

    @Override
    public String getGuiID() {
        return "minecraft:furnace";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: {
                return this.furnaceBurnTime;
            }
            case 1: {
                return this.currentItemBurnTime;
            }
            case 2: {
                return this.cookTime;
            }
            case 3: {
                return this.totalCookTime;
            }
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.furnaceBurnTime = value;
                return;
            }
            case 1: {
                this.currentItemBurnTime = value;
                return;
            }
            case 2: {
                this.cookTime = value;
                return;
            }
            case 3: {
                this.totalCookTime = value;
                return;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        int i = 0;
        while (i < this.furnaceItemStacks.length) {
            this.furnaceItemStacks[i] = null;
            ++i;
        }
    }
}

