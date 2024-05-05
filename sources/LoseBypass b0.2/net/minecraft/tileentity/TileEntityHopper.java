/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper
extends TileEntityLockable
implements IHopper,
ITickable {
    private ItemStack[] inventory = new ItemStack[5];
    private String customName;
    private int transferCooldown = -1;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            byte j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.inventory.length) {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.inventory.length) {
                compound.setTag("Items", nbttaglist);
                compound.setInteger("TransferCooldown", this.transferCooldown);
                if (!this.hasCustomName()) return;
                compound.setString("CustomName", this.customName);
                return;
            }
            if (this.inventory[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventory[index] == null) return null;
        if (this.inventory[index].stackSize <= count) {
            ItemStack itemstack1 = this.inventory[index];
            this.inventory[index] = null;
            return itemstack1;
        }
        ItemStack itemstack = this.inventory[index].splitStack(count);
        if (this.inventory[index].stackSize != 0) return itemstack;
        this.inventory[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.inventory[index] == null) return null;
        ItemStack itemstack = this.inventory[index];
        this.inventory[index] = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory[index] = stack;
        if (stack == null) return;
        if (stack.stackSize <= this.getInventoryStackLimit()) return;
        stack.stackSize = this.getInventoryStackLimit();
    }

    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.hopper";
        String string = this.customName;
        return string;
    }

    @Override
    public boolean hasCustomName() {
        if (this.customName == null) return false;
        if (this.customName.length() <= 0) return false;
        return true;
    }

    public void setCustomName(String customNameIn) {
        this.customName = customNameIn;
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
        return true;
    }

    @Override
    public void update() {
        if (this.worldObj == null) return;
        if (this.worldObj.isRemote) return;
        --this.transferCooldown;
        if (this.isOnTransferCooldown()) return;
        this.setTransferCooldown(0);
        this.updateHopper();
    }

    public boolean updateHopper() {
        if (this.worldObj == null) return false;
        if (this.worldObj.isRemote) return false;
        if (this.isOnTransferCooldown()) return false;
        if (!BlockHopper.isEnabled(this.getBlockMetadata())) return false;
        boolean flag = false;
        if (!this.isEmpty()) {
            flag = this.transferItemsOut();
        }
        if (!this.isFull()) {
            flag = TileEntityHopper.captureDroppedItems(this) || flag;
        }
        if (!flag) return false;
        this.setTransferCooldown(8);
        this.markDirty();
        return true;
    }

    private boolean isEmpty() {
        ItemStack[] itemStackArray = this.inventory;
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            if (itemstack != null) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    private boolean isFull() {
        ItemStack[] itemStackArray = this.inventory;
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            if (itemstack == null) return false;
            if (itemstack.stackSize != itemstack.getMaxStackSize()) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    private boolean transferItemsOut() {
        IInventory iinventory = this.getInventoryForHopperTransfer();
        if (iinventory == null) {
            return false;
        }
        EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
        if (this.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }
        int i = 0;
        while (i < this.getSizeInventory()) {
            if (this.getStackInSlot(i) != null) {
                ItemStack itemstack = this.getStackInSlot(i).copy();
                ItemStack itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(iinventory, this.decrStackSize(i, 1), enumfacing);
                if (itemstack1 == null || itemstack1.stackSize == 0) {
                    iinventory.markDirty();
                    return true;
                }
                this.setInventorySlotContents(i, itemstack);
            }
            ++i;
        }
        return false;
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);
            int k = 0;
            while (k < aint.length) {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[k]);
                if (itemstack1 == null) return false;
                if (itemstack1.stackSize != itemstack1.getMaxStackSize()) {
                    return false;
                }
                ++k;
            }
            return true;
        }
        int i = inventoryIn.getSizeInventory();
        int j = 0;
        while (j < i) {
            ItemStack itemstack = inventoryIn.getStackInSlot(j);
            if (itemstack == null) return false;
            if (itemstack.stackSize != itemstack.getMaxStackSize()) {
                return false;
            }
            ++j;
        }
        return true;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);
            int i = 0;
            while (i < aint.length) {
                if (isidedinventory.getStackInSlot(aint[i]) != null) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        int j = inventoryIn.getSizeInventory();
        int k = 0;
        while (k < j) {
            if (inventoryIn.getStackInSlot(k) != null) {
                return false;
            }
            ++k;
        }
        return true;
    }

    public static boolean captureDroppedItems(IHopper p_145891_0_) {
        EnumFacing enumfacing;
        IInventory iinventory;
        block8: {
            block7: {
                EntityItem entityitem;
                block6: {
                    iinventory = TileEntityHopper.getHopperInventory(p_145891_0_);
                    if (iinventory == null) break block6;
                    enumfacing = EnumFacing.DOWN;
                    if (TileEntityHopper.isInventoryEmpty(iinventory, enumfacing)) {
                        return false;
                    }
                    if (iinventory instanceof ISidedInventory) break block7;
                    break block8;
                }
                Iterator<EntityItem> iterator = TileEntityHopper.func_181556_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0, p_145891_0_.getZPos()).iterator();
                do {
                    if (!iterator.hasNext()) return false;
                } while (!TileEntityHopper.putDropInInventoryAllSlots(p_145891_0_, entityitem = iterator.next()));
                return true;
            }
            ISidedInventory isidedinventory = (ISidedInventory)iinventory;
            int[] aint = isidedinventory.getSlotsForFace(enumfacing);
            int i = 0;
            while (i < aint.length) {
                if (TileEntityHopper.pullItemFromSlot(p_145891_0_, iinventory, aint[i], enumfacing)) {
                    return true;
                }
                ++i;
            }
            return false;
        }
        int j = iinventory.getSizeInventory();
        int k = 0;
        while (k < j) {
            if (TileEntityHopper.pullItemFromSlot(p_145891_0_, iinventory, k, enumfacing)) {
                return true;
            }
            ++k;
        }
        return false;
    }

    private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (itemstack == null) return false;
        if (!TileEntityHopper.canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) return false;
        ItemStack itemstack1 = itemstack.copy();
        ItemStack itemstack2 = TileEntityHopper.putStackInInventoryAllSlots(hopper, inventoryIn.decrStackSize(index, 1), null);
        if (itemstack2 != null && itemstack2.stackSize != 0) {
            inventoryIn.setInventorySlotContents(index, itemstack1);
            return false;
        }
        inventoryIn.markDirty();
        return true;
    }

    public static boolean putDropInInventoryAllSlots(IInventory p_145898_0_, EntityItem itemIn) {
        boolean flag = false;
        if (itemIn == null) {
            return false;
        }
        ItemStack itemstack = itemIn.getEntityItem().copy();
        ItemStack itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(p_145898_0_, itemstack, null);
        if (itemstack1 != null && itemstack1.stackSize != 0) {
            itemIn.setEntityItemStack(itemstack1);
            return flag;
        }
        flag = true;
        itemIn.setDead();
        return flag;
    }

    public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory && side != null) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);
            for (int k = 0; k < aint.length && stack != null && stack.stackSize > 0; ++k) {
                stack = TileEntityHopper.insertStack(inventoryIn, stack, aint[k], side);
            }
        } else {
            int i = inventoryIn.getSizeInventory();
            for (int j = 0; j < i && stack != null && stack.stackSize > 0; ++j) {
                stack = TileEntityHopper.insertStack(inventoryIn, stack, j, side);
            }
        }
        if (stack == null) return stack;
        if (stack.stackSize != 0) return stack;
        return null;
    }

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        if (!inventoryIn.isItemValidForSlot(index, stack)) {
            return false;
        }
        if (!(inventoryIn instanceof ISidedInventory)) return true;
        if (((ISidedInventory)inventoryIn).canInsertItem(index, stack, side)) return true;
        return false;
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        if (!(inventoryIn instanceof ISidedInventory)) return true;
        if (((ISidedInventory)inventoryIn).canExtractItem(index, stack, side)) return true;
        return false;
    }

    private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (!TileEntityHopper.canInsertItemInSlot(inventoryIn, stack, index, side)) return stack;
        boolean flag = false;
        if (itemstack == null) {
            inventoryIn.setInventorySlotContents(index, stack);
            stack = null;
            flag = true;
        } else if (TileEntityHopper.canCombine(itemstack, stack)) {
            int i = stack.getMaxStackSize() - itemstack.stackSize;
            int j = Math.min(stack.stackSize, i);
            stack.stackSize -= j;
            itemstack.stackSize += j;
            flag = j > 0;
        }
        if (!flag) return stack;
        if (inventoryIn instanceof TileEntityHopper) {
            TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
            if (tileentityhopper.mayTransfer()) {
                tileentityhopper.setTransferCooldown(8);
            }
            inventoryIn.markDirty();
        }
        inventoryIn.markDirty();
        return stack;
    }

    private IInventory getInventoryForHopperTransfer() {
        EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.pos.getX() + enumfacing.getFrontOffsetX(), this.pos.getY() + enumfacing.getFrontOffsetY(), this.pos.getZ() + enumfacing.getFrontOffsetZ());
    }

    public static IInventory getHopperInventory(IHopper hopper) {
        return TileEntityHopper.getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos());
    }

    public static List<EntityItem> func_181556_a(World p_181556_0_, double p_181556_1_, double p_181556_3_, double p_181556_5_) {
        return p_181556_0_.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_181556_1_ - 0.5, p_181556_3_ - 0.5, p_181556_5_ - 0.5, p_181556_1_ + 0.5, p_181556_3_ + 0.5, p_181556_5_ + 0.5), EntitySelectors.selectAnything);
    }

    public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
        TileEntity tileentity;
        int k;
        int j;
        IInventory iinventory = null;
        int i = MathHelper.floor_double(x);
        BlockPos blockpos = new BlockPos(i, j = MathHelper.floor_double(y), k = MathHelper.floor_double(z));
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity() && (tileentity = worldIn.getTileEntity(blockpos)) instanceof IInventory && (iinventory = (IInventory)((Object)tileentity)) instanceof TileEntityChest && block instanceof BlockChest) {
            iinventory = ((BlockChest)block).getLockableContainer(worldIn, blockpos);
        }
        if (iinventory != null) return iinventory;
        List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.selectInventories);
        if (list.size() <= 0) return iinventory;
        return (IInventory)((Object)list.get(worldIn.rand.nextInt(list.size())));
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        }
        if (stack1.getMetadata() != stack2.getMetadata()) {
            return false;
        }
        if (stack1.stackSize > stack1.getMaxStackSize()) {
            return false;
        }
        boolean bl = ItemStack.areItemStackTagsEqual(stack1, stack2);
        return bl;
    }

    @Override
    public double getXPos() {
        return (double)this.pos.getX() + 0.5;
    }

    @Override
    public double getYPos() {
        return (double)this.pos.getY() + 0.5;
    }

    @Override
    public double getZPos() {
        return (double)this.pos.getZ() + 0.5;
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    public boolean isOnTransferCooldown() {
        if (this.transferCooldown <= 0) return false;
        return true;
    }

    public boolean mayTransfer() {
        if (this.transferCooldown > 1) return false;
        return true;
    }

    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
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
    public void clear() {
        int i = 0;
        while (i < this.inventory.length) {
            this.inventory[i] = null;
            ++i;
        }
    }
}

