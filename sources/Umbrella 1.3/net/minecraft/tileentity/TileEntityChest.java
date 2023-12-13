/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileEntityChest
extends TileEntityLockable
implements IUpdatePlayerListBox,
IInventory {
    private ItemStack[] chestContents = new ItemStack[27];
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg;
    public TileEntityChest adjacentChestXPos;
    public TileEntityChest adjacentChestXNeg;
    public TileEntityChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private int cachedChestType;
    private String customName;
    private static final String __OBFID = "CL_00000346";

    public TileEntityChest() {
        this.cachedChestType = -1;
    }

    public TileEntityChest(int p_i2350_1_) {
        this.cachedChestType = p_i2350_1_;
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return this.chestContents[slotIn];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.chestContents[index] != null) {
            if (this.chestContents[index].stackSize <= count) {
                ItemStack var3 = this.chestContents[index];
                this.chestContents[index] = null;
                this.markDirty();
                return var3;
            }
            ItemStack var3 = this.chestContents[index].splitStack(count);
            if (this.chestContents[index].stackSize == 0) {
                this.chestContents[index] = null;
            }
            this.markDirty();
            return var3;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.chestContents[index] != null) {
            ItemStack var2 = this.chestContents[index];
            this.chestContents[index] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.chestContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chest";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String p_145976_1_) {
        this.customName = p_145976_1_;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 0xFF;
            if (var5 < 0 || var5 >= this.chestContents.length) continue;
            this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.chestContents.length; ++var3) {
            if (this.chestContents[var3] == null) continue;
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.chestContents[var3].writeToNBT(var4);
            var2.appendTag(var4);
        }
        compound.setTag("Items", var2);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    private void func_174910_a(TileEntityChest p_174910_1_, EnumFacing p_174910_2_) {
        if (p_174910_1_.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (SwitchEnumFacing.field_177366_a[p_174910_2_.ordinal()]) {
                case 1: {
                    if (this.adjacentChestZNeg == p_174910_1_) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case 2: {
                    if (this.adjacentChestZPos == p_174910_1_) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case 3: {
                    if (this.adjacentChestXPos == p_174910_1_) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case 4: {
                    if (this.adjacentChestXNeg == p_174910_1_) break;
                    this.adjacentChestChecked = false;
                }
            }
        }
    }

    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.func_174911_a(EnumFacing.WEST);
            this.adjacentChestXPos = this.func_174911_a(EnumFacing.EAST);
            this.adjacentChestZNeg = this.func_174911_a(EnumFacing.NORTH);
            this.adjacentChestZPos = this.func_174911_a(EnumFacing.SOUTH);
        }
    }

    protected TileEntityChest func_174911_a(EnumFacing p_174911_1_) {
        TileEntity var3;
        BlockPos var2 = this.pos.offset(p_174911_1_);
        if (this.func_174912_b(var2) && (var3 = this.worldObj.getTileEntity(var2)) instanceof TileEntityChest) {
            TileEntityChest var4 = (TileEntityChest)var3;
            var4.func_174910_a(this, p_174911_1_.getOpposite());
            return var4;
        }
        return null;
    }

    private boolean func_174912_b(BlockPos p_174912_1_) {
        if (this.worldObj == null) {
            return false;
        }
        Block var2 = this.worldObj.getBlockState(p_174912_1_).getBlock();
        return var2 instanceof BlockChest && ((BlockChest)var2).chestType == this.getChestType();
    }

    @Override
    public void update() {
        float var4;
        this.checkForAdjacentChests();
        int var1 = this.pos.getX();
        int var2 = this.pos.getY();
        int var3 = this.pos.getZ();
        ++this.ticksSinceSync;
        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + var1 + var2 + var3) % 200 == 0) {
            this.numPlayersUsing = 0;
            var4 = 5.0f;
            List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((float)var1 - var4, (float)var2 - var4, (float)var3 - var4, (float)(var1 + 1) + var4, (float)(var2 + 1) + var4, (float)(var3 + 1) + var4));
            for (EntityPlayer var7 : var5) {
                IInventory var8;
                if (!(var7.openContainer instanceof ContainerChest) || (var8 = ((ContainerChest)var7.openContainer).getLowerChestInventory()) != this && (!(var8 instanceof InventoryLargeChest) || !((InventoryLargeChest)var8).isPartOfLargeChest(this))) continue;
                ++this.numPlayersUsing;
            }
        }
        this.prevLidAngle = this.lidAngle;
        var4 = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double var11 = (double)var1 + 0.5;
            double var14 = (double)var3 + 0.5;
            if (this.adjacentChestZPos != null) {
                var14 += 0.5;
            }
            if (this.adjacentChestXPos != null) {
                var11 += 0.5;
            }
            this.worldObj.playSoundEffect(var11, (double)var2 + 0.5, var14, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0f || this.numPlayersUsing > 0 && this.lidAngle < 1.0f) {
            float var13;
            float var12 = this.lidAngle;
            this.lidAngle = this.numPlayersUsing > 0 ? (this.lidAngle += var4) : (this.lidAngle -= var4);
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            if (this.lidAngle < (var13 = 0.5f) && var12 >= var13 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double var14 = (double)var1 + 0.5;
                double var9 = (double)var3 + 0.5;
                if (this.adjacentChestZPos != null) {
                    var9 += 0.5;
                }
                if (this.adjacentChestXPos != null) {
                    var14 += 0.5;
                }
                this.worldObj.playSoundEffect(var14, (double)var2 + 0.5, var9, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
        if (!playerIn.func_175149_v()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), this.getBlockType());
        }
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
        if (!playerIn.func_175149_v() && this.getBlockType() instanceof BlockChest) {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), this.getBlockType());
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    public int getChestType() {
        if (this.cachedChestType == -1) {
            if (this.worldObj == null || !(this.getBlockType() instanceof BlockChest)) {
                return 0;
            }
            this.cachedChestType = ((BlockChest)this.getBlockType()).chestType;
        }
        return this.cachedChestType;
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
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
    public void clearInventory() {
        for (int var1 = 0; var1 < this.chestContents.length; ++var1) {
            this.chestContents[var1] = null;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_177366_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002041";

        static {
            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

