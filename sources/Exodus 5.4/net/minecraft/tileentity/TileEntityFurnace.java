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
    private int cookTime;
    private int totalCookTime;
    private static final int[] slotsBottom;
    private String furnaceCustomName;
    private int furnaceBurnTime;
    private static final int[] slotsTop;
    private int currentItemBurnTime;
    private static final int[] slotsSides;
    private ItemStack[] furnaceItemStacks = new ItemStack[3];

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemStack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = itemStack.copy();
            } else if (this.furnaceItemStacks[2].getItem() == itemStack.getItem()) {
                ++this.furnaceItemStacks[2].stackSize;
            }
            if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket) {
                this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
            }
            --this.furnaceItemStacks[0].stackSize;
            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        }
        ItemStack itemStack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
        return itemStack == null ? false : (this.furnaceItemStacks[2] == null ? true : (!this.furnaceItemStacks[2].isItemEqual(itemStack) ? false : (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize() ? true : this.furnaceItemStacks[2].stackSize < itemStack.getMaxStackSize())));
    }

    public static int getItemBurnTime(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        Item item = itemStack.getItem();
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
        return item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD") ? 200 : (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD") ? 200 : (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD") ? 200 : (item == Items.stick ? 100 : (item == Items.coal ? 1600 : (item == Items.lava_bucket ? 20000 : (item == Item.getItemFromBlock(Blocks.sapling) ? 100 : (item == Items.blaze_rod ? 2400 : 0)))))));
    }

    @Override
    public boolean canInsertItem(int n, ItemStack itemStack, EnumFacing enumFacing) {
        return this.isItemValidForSlot(n, itemStack);
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public static boolean isBurning(IInventory iInventory) {
        return iInventory.getField(0) > 0;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN ? slotsBottom : (enumFacing == EnumFacing.UP ? slotsTop : slotsSides);
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public String getGuiID() {
        return "minecraft:furnace";
    }

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        boolean bl = itemStack != null && itemStack.isItemEqual(this.furnaceItemStacks[n]) && ItemStack.areItemStackTagsEqual(itemStack, this.furnaceItemStacks[n]);
        this.furnaceItemStacks[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        if (n == 0 && !bl) {
            this.totalCookTime = this.getCookTime(itemStack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean canExtractItem(int n, ItemStack itemStack, EnumFacing enumFacing) {
        Item item;
        return enumFacing != EnumFacing.DOWN || n != 1 || (item = itemStack.getItem()) == Items.water_bucket || item == Items.bucket;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
        nBTTagCompound.setShort("CookTime", (short)this.cookTime);
        nBTTagCompound.setShort("CookTimeTotal", (short)this.totalCookTime);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.furnaceItemStacks.length) {
            if (this.furnaceItemStacks[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.furnaceItemStacks[n].writeToNBT(nBTTagCompound2);
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n;
        }
        nBTTagCompound.setTag("Items", nBTTagList);
        if (this.hasCustomName()) {
            nBTTagCompound.setString("CustomName", this.furnaceCustomName);
        }
    }

    static {
        slotsTop = new int[1];
        slotsBottom = new int[]{2, 1};
        slotsSides = new int[]{1};
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.furnaceItemStacks[n] != null) {
            if (this.furnaceItemStacks[n].stackSize <= n2) {
                ItemStack itemStack = this.furnaceItemStacks[n];
                this.furnaceItemStacks[n] = null;
                return itemStack;
            }
            ItemStack itemStack = this.furnaceItemStacks[n].splitStack(n2);
            if (this.furnaceItemStacks[n].stackSize == 0) {
                this.furnaceItemStacks[n] = null;
            }
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.furnaceItemStacks[n] != null) {
            ItemStack itemStack = this.furnaceItemStacks[n];
            this.furnaceItemStacks[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean hasCustomName() {
        return this.furnaceCustomName != null && this.furnaceCustomName.length() > 0;
    }

    @Override
    public int getField(int n) {
        switch (n) {
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
    public void clear() {
        int n = 0;
        while (n < this.furnaceItemStacks.length) {
            this.furnaceItemStacks[n] = null;
            ++n;
        }
    }

    public void setCustomInventoryName(String string) {
        this.furnaceCustomName = string;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerFurnace(inventoryPlayer, this);
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.furnaceItemStacks[n];
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            byte by = nBTTagCompound2.getByte("Slot");
            if (by >= 0 && by < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[by] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
        this.furnaceBurnTime = nBTTagCompound.getShort("BurnTime");
        this.cookTime = nBTTagCompound.getShort("CookTime");
        this.totalCookTime = nBTTagCompound.getShort("CookTimeTotal");
        this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = nBTTagCompound.getString("CustomName");
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return n == 2 ? false : (n != 1 ? true : TileEntityFurnace.isItemFuel(itemStack) || SlotFurnaceFuel.isBucket(itemStack));
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void update() {
        boolean bl = this.isBurning();
        boolean bl2 = false;
        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if (!this.worldObj.isRemote) {
            if (this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
                    if (this.isBurning()) {
                        bl2 = true;
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
                        bl2 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (bl != this.isBurning()) {
                bl2 = true;
                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }
        if (bl2) {
            this.markDirty();
        }
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
    }

    public int getCookTime(ItemStack itemStack) {
        return 200;
    }

    @Override
    public void setField(int n, int n2) {
        switch (n) {
            case 0: {
                this.furnaceBurnTime = n2;
                break;
            }
            case 1: {
                this.currentItemBurnTime = n2;
                break;
            }
            case 2: {
                this.cookTime = n2;
                break;
            }
            case 3: {
                this.totalCookTime = n2;
            }
        }
    }
}

