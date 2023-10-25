/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityChest
extends TileEntityLockable
implements IInventory,
ITickable {
    public boolean adjacentChestChecked;
    public float lidAngle;
    public TileEntityChest adjacentChestZNeg;
    private ItemStack[] chestContents = new ItemStack[27];
    public TileEntityChest adjacentChestXPos;
    public float prevLidAngle;
    private int cachedChestType;
    public TileEntityChest adjacentChestXNeg;
    private String customName;
    public int numPlayersUsing;
    private int ticksSinceSync;
    public TileEntityChest adjacentChestZPos;

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean receiveClientEvent(int n, int n2) {
        if (n == 1) {
            this.numPlayersUsing = n2;
            return true;
        }
        return super.receiveClientEvent(n, n2);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
        if (!entityPlayer.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        if (!entityPlayer.isSpectator() && this.getBlockType() instanceof BlockChest) {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    private void func_174910_a(TileEntityChest tileEntityChest, EnumFacing enumFacing) {
        if (tileEntityChest.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (enumFacing) {
                case NORTH: {
                    if (this.adjacentChestZNeg == tileEntityChest) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case SOUTH: {
                    if (this.adjacentChestZPos == tileEntityChest) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case EAST: {
                    if (this.adjacentChestXPos == tileEntityChest) break;
                    this.adjacentChestChecked = false;
                    break;
                }
                case WEST: {
                    if (this.adjacentChestXNeg == tileEntityChest) break;
                    this.adjacentChestChecked = false;
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerChest(inventoryPlayer, this, entityPlayer);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.chestContents[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.chestContents[n] != null) {
            if (this.chestContents[n].stackSize <= n2) {
                ItemStack itemStack = this.chestContents[n];
                this.chestContents[n] = null;
                this.markDirty();
                return itemStack;
            }
            ItemStack itemStack = this.chestContents[n].splitStack(n2);
            if (this.chestContents[n].stackSize == 0) {
                this.chestContents[n] = null;
            }
            this.markDirty();
            return itemStack;
        }
        return null;
    }

    @Override
    public void update() {
        float f;
        this.checkForAdjacentChests();
        int n = this.pos.getX();
        int n2 = this.pos.getY();
        int n3 = this.pos.getZ();
        ++this.ticksSinceSync;
        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + n + n2 + n3) % 200 == 0) {
            this.numPlayersUsing = 0;
            f = 5.0f;
            for (EntityPlayer entityPlayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((float)n - f, (float)n2 - f, (float)n3 - f, (float)(n + 1) + f, (float)(n2 + 1) + f, (float)(n3 + 1) + f))) {
                IInventory iInventory;
                if (!(entityPlayer.openContainer instanceof ContainerChest) || (iInventory = ((ContainerChest)entityPlayer.openContainer).getLowerChestInventory()) != this && (!(iInventory instanceof InventoryLargeChest) || !((InventoryLargeChest)iInventory).isPartOfLargeChest(this))) continue;
                ++this.numPlayersUsing;
            }
        }
        this.prevLidAngle = this.lidAngle;
        f = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double d = (double)n + 0.5;
            double d2 = (double)n3 + 0.5;
            if (this.adjacentChestZPos != null) {
                d2 += 0.5;
            }
            if (this.adjacentChestXPos != null) {
                d += 0.5;
            }
            this.worldObj.playSoundEffect(d, (double)n2 + 0.5, d2, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0f || this.numPlayersUsing > 0 && this.lidAngle < 1.0f) {
            float f2;
            float f3 = this.lidAngle;
            this.lidAngle = this.numPlayersUsing > 0 ? (this.lidAngle += f) : (this.lidAngle -= f);
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            if (this.lidAngle < (f2 = 0.5f) && f3 >= f2 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double d = (double)n + 0.5;
                double d3 = (double)n3 + 0.5;
                if (this.adjacentChestZPos != null) {
                    d3 += 0.5;
                }
                if (this.adjacentChestXPos != null) {
                    d += 0.5;
                }
                this.worldObj.playSoundEffect(d, (double)n2 + 0.5, d3, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
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

    public void setCustomName(String string) {
        this.customName = string;
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.chestContents[n] != null) {
            ItemStack itemStack = this.chestContents[n];
            this.chestContents[n] = null;
            return itemStack;
        }
        return null;
    }

    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
            this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
            this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
            this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    private boolean isChestAt(BlockPos blockPos) {
        if (this.worldObj == null) {
            return false;
        }
        Block block = this.worldObj.getBlockState(blockPos).getBlock();
        return block instanceof BlockChest && ((BlockChest)block).chestType == this.getChestType();
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.chestContents.length) {
            if (this.chestContents[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.chestContents[n].writeToNBT(nBTTagCompound2);
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
    public void clear() {
        int n = 0;
        while (n < this.chestContents.length) {
            this.chestContents[n] = null;
            ++n;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            int n2 = nBTTagCompound2.getByte("Slot") & 0xFF;
            if (n2 >= 0 && n2 < this.chestContents.length) {
                this.chestContents[n2] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
    }

    public TileEntityChest(int n) {
        this.cachedChestType = n;
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public void setField(int n, int n2) {
    }

    protected TileEntityChest getAdjacentChest(EnumFacing enumFacing) {
        TileEntity tileEntity;
        BlockPos blockPos = this.pos.offset(enumFacing);
        if (this.isChestAt(blockPos) && (tileEntity = this.worldObj.getTileEntity(blockPos)) instanceof TileEntityChest) {
            TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            tileEntityChest.func_174910_a(this, enumFacing.getOpposite());
            return tileEntityChest;
        }
        return null;
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.chestContents[n];
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chest";
    }

    public TileEntityChest() {
        this.cachedChestType = -1;
    }
}

