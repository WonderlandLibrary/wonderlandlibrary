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
implements ISidedInventory,
ITickable {
    private int brewTime;
    private static final int[] inputSlots = new int[]{3};
    private boolean[] filledSlots;
    private ItemStack[] brewingItemStacks = new ItemStack[4];
    private static final int[] outputSlots;
    private Item ingredientID;
    private String customName;

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.length;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (n >= 0 && n < this.brewingItemStacks.length) {
            ItemStack itemStack = this.brewingItemStacks[n];
            this.brewingItemStacks[n] = null;
            return itemStack;
        }
        return null;
    }

    static {
        int[] nArray = new int[3];
        nArray[1] = 1;
        nArray[2] = 2;
        outputSlots = nArray;
    }

    private boolean canBrew() {
        if (this.brewingItemStacks[3] != null && this.brewingItemStacks[3].stackSize > 0) {
            ItemStack itemStack = this.brewingItemStacks[3];
            if (!itemStack.getItem().isPotionIngredient(itemStack)) {
                return false;
            }
            boolean bl = false;
            int n = 0;
            while (n < 3) {
                if (this.brewingItemStacks[n] != null && this.brewingItemStacks[n].getItem() == Items.potionitem) {
                    int n2 = this.brewingItemStacks[n].getMetadata();
                    int n3 = this.getPotionResult(n2, itemStack);
                    if (!ItemPotion.isSplash(n2) && ItemPotion.isSplash(n3)) {
                        bl = true;
                        break;
                    }
                    List<PotionEffect> list = Items.potionitem.getEffects(n2);
                    List<PotionEffect> list2 = Items.potionitem.getEffects(n3);
                    if (!(n2 > 0 && list == list2 || list != null && (list.equals(list2) || list2 == null) || n2 == n3)) {
                        bl = true;
                        break;
                    }
                }
                ++n;
            }
            return bl;
        }
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return n >= 0 && n < this.brewingItemStacks.length ? this.brewingItemStacks[n] : null;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (n >= 0 && n < this.brewingItemStacks.length) {
            ItemStack itemStack = this.brewingItemStacks[n];
            this.brewingItemStacks[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.brewingItemStacks.length) {
            this.brewingItemStacks[n] = null;
            ++n;
        }
    }

    @Override
    public boolean canExtractItem(int n, ItemStack itemStack, EnumFacing enumFacing) {
        return true;
    }

    private int getPotionResult(int n, ItemStack itemStack) {
        return itemStack == null ? n : (itemStack.getItem().isPotionIngredient(itemStack) ? PotionHelper.applyIngredient(n, itemStack.getItem().getPotionEffect(itemStack)) : n);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        if (n >= 0 && n < this.brewingItemStacks.length) {
            this.brewingItemStacks[n] = itemStack;
        }
    }

    @Override
    public String getGuiID() {
        return "minecraft:brewing_stand";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void update() {
        boolean[] blArray;
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
        if (!this.worldObj.isRemote && !Arrays.equals(blArray = this.func_174902_m(), this.filledSlots)) {
            this.filledSlots = blArray;
            IBlockState iBlockState = this.worldObj.getBlockState(this.getPos());
            if (!(iBlockState.getBlock() instanceof BlockBrewingStand)) {
                return;
            }
            int n = 0;
            while (n < BlockBrewingStand.HAS_BOTTLE.length) {
                iBlockState = iBlockState.withProperty(BlockBrewingStand.HAS_BOTTLE[n], blArray[n]);
                ++n;
            }
            this.worldObj.setBlockState(this.pos, iBlockState, 2);
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing enumFacing) {
        return enumFacing == EnumFacing.UP ? inputSlots : outputSlots;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return n == 3 ? itemStack.getItem().isPotionIngredient(itemStack) : itemStack.getItem() == Items.potionitem || itemStack.getItem() == Items.glass_bottle;
    }

    public void setName(String string) {
        this.customName = string;
    }

    @Override
    public boolean canInsertItem(int n, ItemStack itemStack, EnumFacing enumFacing) {
        return this.isItemValidForSlot(n, itemStack);
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setShort("BrewTime", (short)this.brewTime);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.brewingItemStacks.length) {
            if (this.brewingItemStacks[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.brewingItemStacks[n].writeToNBT(nBTTagCompound2);
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n;
        }
        nBTTagCompound.setTag("Items", nBTTagList);
        if (this.hasCustomName()) {
            nBTTagCompound.setString("CustomName", this.customName);
        }
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerBrewingStand(inventoryPlayer, this);
    }

    @Override
    public int getField(int n) {
        switch (n) {
            case 0: {
                return this.brewTime;
            }
        }
        return 0;
    }

    public boolean[] func_174902_m() {
        boolean[] blArray = new boolean[3];
        int n = 0;
        while (n < 3) {
            if (this.brewingItemStacks[n] != null) {
                blArray[n] = true;
            }
            ++n;
        }
        return blArray;
    }

    private void brewPotions() {
        if (this.canBrew()) {
            ItemStack itemStack = this.brewingItemStacks[3];
            int n = 0;
            while (n < 3) {
                if (this.brewingItemStacks[n] != null && this.brewingItemStacks[n].getItem() == Items.potionitem) {
                    int n2 = this.brewingItemStacks[n].getMetadata();
                    int n3 = this.getPotionResult(n2, itemStack);
                    List<PotionEffect> list = Items.potionitem.getEffects(n2);
                    List<PotionEffect> list2 = Items.potionitem.getEffects(n3);
                    if (n2 > 0 && list == list2 || list != null && (list.equals(list2) || list2 == null)) {
                        if (!ItemPotion.isSplash(n2) && ItemPotion.isSplash(n3)) {
                            this.brewingItemStacks[n].setItemDamage(n3);
                        }
                    } else if (n2 != n3) {
                        this.brewingItemStacks[n].setItemDamage(n3);
                    }
                }
                ++n;
            }
            if (itemStack.getItem().hasContainerItem()) {
                this.brewingItemStacks[3] = new ItemStack(itemStack.getItem().getContainerItem());
            } else {
                --this.brewingItemStacks[3].stackSize;
                if (this.brewingItemStacks[3].stackSize <= 0) {
                    this.brewingItemStacks[3] = null;
                }
            }
        }
    }

    @Override
    public void setField(int n, int n2) {
        switch (n) {
            case 0: {
                this.brewTime = n2;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.brewingItemStacks = new ItemStack[this.getSizeInventory()];
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            byte by = nBTTagCompound2.getByte("Slot");
            if (by >= 0 && by < this.brewingItemStacks.length) {
                this.brewingItemStacks[by] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
        this.brewTime = nBTTagCompound.getShort("BrewTime");
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.brewing";
    }
}

