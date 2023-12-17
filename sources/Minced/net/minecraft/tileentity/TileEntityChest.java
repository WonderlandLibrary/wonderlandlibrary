// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import java.util.Iterator;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ITickable;

public class TileEntityChest extends TileEntityLockableLoot implements ITickable
{
    private NonNullList<ItemStack> chestContents;
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg;
    public TileEntityChest adjacentChestXPos;
    public TileEntityChest adjacentChestXNeg;
    public TileEntityChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private BlockChest.Type cachedChestType;
    
    public TileEntityChest() {
        this.chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    }
    
    public TileEntityChest(final BlockChest.Type typeIn) {
        this.chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
        this.cachedChestType = typeIn;
    }
    
    @Override
    public int getSizeInventory() {
        return 27;
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.chestContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chest";
    }
    
    public static void registerFixesChest(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChest.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }
    
    private void setNeighbor(final TileEntityChest chestTe, final EnumFacing side) {
        if (chestTe.isInvalid()) {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked) {
            switch (side) {
                case NORTH: {
                    if (this.adjacentChestZNeg != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case SOUTH: {
                    if (this.adjacentChestZPos != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case EAST: {
                    if (this.adjacentChestXPos != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case WEST: {
                    if (this.adjacentChestXNeg != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
            }
        }
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
    
    @Nullable
    protected TileEntityChest getAdjacentChest(final EnumFacing side) {
        final BlockPos blockpos = this.pos.offset(side);
        if (this.isChestAt(blockpos)) {
            final TileEntity tileentity = this.world.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityChest) {
                final TileEntityChest tileentitychest = (TileEntityChest)tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }
        return null;
    }
    
    private boolean isChestAt(final BlockPos posIn) {
        if (this.world == null) {
            return false;
        }
        final Block block = this.world.getBlockState(posIn).getBlock();
        return block instanceof BlockChest && ((BlockChest)block).chestType == this.getChestType();
    }
    
    @Override
    public void update() {
        this.checkForAdjacentChests();
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        ++this.ticksSinceSync;
        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing = 0;
            final float f = 5.0f;
            for (final EntityPlayer entityplayer : this.world.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, new AxisAlignedBB(i - 5.0f, j - 5.0f, k - 5.0f, i + 1 + 5.0f, j + 1 + 5.0f, k + 1 + 5.0f))) {
                if (entityplayer.openContainer instanceof ContainerChest) {
                    final IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
                    if (iinventory != this && (!(iinventory instanceof InventoryLargeChest) || !((InventoryLargeChest)iinventory).isPartOfLargeChest(this))) {
                        continue;
                    }
                    ++this.numPlayersUsing;
                }
            }
        }
        this.prevLidAngle = this.lidAngle;
        final float f2 = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double d1 = i + 0.5;
            double d2 = k + 0.5;
            if (this.adjacentChestZPos != null) {
                d2 += 0.5;
            }
            if (this.adjacentChestXPos != null) {
                d1 += 0.5;
            }
            this.world.playSound(null, d1, j + 0.5, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0f) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0f)) {
            final float f3 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1f;
            }
            else {
                this.lidAngle -= 0.1f;
            }
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            final float f4 = 0.5f;
            if (this.lidAngle < 0.5f && f3 >= 0.5f && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double d3 = i + 0.5;
                double d4 = k + 0.5;
                if (this.adjacentChestZPos != null) {
                    d4 += 0.5;
                }
                if (this.adjacentChestXPos != null) {
                    d3 += 0.5;
                }
                this.world.playSound(null, d3, j + 0.5, d4, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
            if (this.getChestType() == BlockChest.Type.TRAP) {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChest) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
            if (this.getChestType() == BlockChest.Type.TRAP) {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }
    
    public BlockChest.Type getChestType() {
        if (this.cachedChestType == null) {
            if (this.world == null || !(this.getBlockType() instanceof BlockChest)) {
                return BlockChest.Type.BASIC;
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
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }
}
