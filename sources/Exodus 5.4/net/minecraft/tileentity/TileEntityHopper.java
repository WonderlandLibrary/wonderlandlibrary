/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
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
implements ITickable,
IHopper {
    private String customName;
    private ItemStack[] inventory = new ItemStack[5];
    private int transferCooldown = -1;

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }

    private static ItemStack insertStack(IInventory iInventory, ItemStack itemStack, int n, EnumFacing enumFacing) {
        ItemStack itemStack2 = iInventory.getStackInSlot(n);
        if (TileEntityHopper.canInsertItemInSlot(iInventory, itemStack, n, enumFacing)) {
            boolean bl = false;
            if (itemStack2 == null) {
                iInventory.setInventorySlotContents(n, itemStack);
                itemStack = null;
                bl = true;
            } else if (TileEntityHopper.canCombine(itemStack2, itemStack)) {
                int n2 = itemStack.getMaxStackSize() - itemStack2.stackSize;
                int n3 = Math.min(itemStack.stackSize, n2);
                itemStack.stackSize -= n3;
                itemStack2.stackSize += n3;
                boolean bl2 = bl = n3 > 0;
            }
            if (bl) {
                if (iInventory instanceof TileEntityHopper) {
                    TileEntityHopper tileEntityHopper = (TileEntityHopper)iInventory;
                    if (tileEntityHopper.mayTransfer()) {
                        tileEntityHopper.setTransferCooldown(8);
                    }
                    iInventory.markDirty();
                }
                iInventory.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    private IInventory getInventoryForHopperTransfer() {
        EnumFacing enumFacing = BlockHopper.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.pos.getX() + enumFacing.getFrontOffsetX(), this.pos.getY() + enumFacing.getFrontOffsetY(), this.pos.getZ() + enumFacing.getFrontOffsetZ());
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.inventory[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    private boolean isInventoryFull(IInventory iInventory, EnumFacing enumFacing) {
        if (iInventory instanceof ISidedInventory) {
            ISidedInventory iSidedInventory = (ISidedInventory)iInventory;
            int[] nArray = iSidedInventory.getSlotsForFace(enumFacing);
            int n = 0;
            while (n < nArray.length) {
                ItemStack itemStack = iSidedInventory.getStackInSlot(nArray[n]);
                if (itemStack == null || itemStack.stackSize != itemStack.getMaxStackSize()) {
                    return false;
                }
                ++n;
            }
        } else {
            int n = iInventory.getSizeInventory();
            int n2 = 0;
            while (n2 < n) {
                ItemStack itemStack = iInventory.getStackInSlot(n2);
                if (itemStack == null || itemStack.stackSize != itemStack.getMaxStackSize()) {
                    return false;
                }
                ++n2;
            }
        }
        return true;
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.inventory[n] != null) {
            if (this.inventory[n].stackSize <= n2) {
                ItemStack itemStack = this.inventory[n];
                this.inventory[n] = null;
                return itemStack;
            }
            ItemStack itemStack = this.inventory[n].splitStack(n2);
            if (this.inventory[n].stackSize == 0) {
                this.inventory[n] = null;
            }
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.inventory[n];
    }

    public static boolean putDropInInventoryAllSlots(IInventory iInventory, EntityItem entityItem) {
        boolean bl = false;
        if (entityItem == null) {
            return false;
        }
        ItemStack itemStack = entityItem.getEntityItem().copy();
        ItemStack itemStack2 = TileEntityHopper.putStackInInventoryAllSlots(iInventory, itemStack, null);
        if (itemStack2 != null && itemStack2.stackSize != 0) {
            entityItem.setEntityItemStack(itemStack2);
        } else {
            bl = true;
            entityItem.setDead();
        }
        return bl;
    }

    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerHopper(inventoryPlayer, this, entityPlayer);
    }

    public void setTransferCooldown(int n) {
        this.transferCooldown = n;
    }

    private boolean isEmpty() {
        ItemStack[] itemStackArray = this.inventory;
        int n = this.inventory.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemStack = itemStackArray[n2];
            if (itemStack != null) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
        this.transferCooldown = nBTTagCompound.getInteger("TransferCooldown");
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            byte by = nBTTagCompound2.getByte("Slot");
            if (by >= 0 && by < this.inventory.length) {
                this.inventory[by] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCooldown;
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.updateHopper();
            }
        }
    }

    private static boolean pullItemFromSlot(IHopper iHopper, IInventory iInventory, int n, EnumFacing enumFacing) {
        ItemStack itemStack = iInventory.getStackInSlot(n);
        if (itemStack != null && TileEntityHopper.canExtractItemFromSlot(iInventory, itemStack, n, enumFacing)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = TileEntityHopper.putStackInInventoryAllSlots(iHopper, iInventory.decrStackSize(n, 1), null);
            if (itemStack3 == null || itemStack3.stackSize == 0) {
                iInventory.markDirty();
                return true;
            }
            iInventory.setInventorySlotContents(n, itemStack2);
        }
        return false;
    }

    public static IInventory getInventoryAtPosition(World world, double d, double d2, double d3) {
        Object object;
        int n;
        int n2;
        IInventory iInventory = null;
        int n3 = MathHelper.floor_double(d);
        BlockPos blockPos = new BlockPos(n3, n2 = MathHelper.floor_double(d2), n = MathHelper.floor_double(d3));
        Block block = world.getBlockState(blockPos).getBlock();
        if (block.hasTileEntity() && (object = world.getTileEntity(blockPos)) instanceof IInventory && (iInventory = (IInventory)object) instanceof TileEntityChest && block instanceof BlockChest) {
            iInventory = ((BlockChest)block).getLockableContainer(world, blockPos);
        }
        if (iInventory == null && (object = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(d - 0.5, d2 - 0.5, d3 - 0.5, d + 0.5, d2 + 0.5, d3 + 0.5), EntitySelectors.selectInventories)).size() > 0) {
            iInventory = (IInventory)object.get(world.rand.nextInt(object.size()));
        }
        return iInventory;
    }

    @Override
    public double getZPos() {
        return (double)this.pos.getZ() + 0.5;
    }

    @Override
    public double getYPos() {
        return (double)this.pos.getY() + 0.5;
    }

    private boolean transferItemsOut() {
        IInventory iInventory = this.getInventoryForHopperTransfer();
        if (iInventory == null) {
            return false;
        }
        EnumFacing enumFacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
        if (this.isInventoryFull(iInventory, enumFacing)) {
            return false;
        }
        int n = 0;
        while (n < this.getSizeInventory()) {
            if (this.getStackInSlot(n) != null) {
                ItemStack itemStack = this.getStackInSlot(n).copy();
                ItemStack itemStack2 = TileEntityHopper.putStackInInventoryAllSlots(iInventory, this.decrStackSize(n, 1), enumFacing);
                if (itemStack2 == null || itemStack2.stackSize == 0) {
                    iInventory.markDirty();
                    return true;
                }
                this.setInventorySlotContents(n, itemStack);
            }
            ++n;
        }
        return false;
    }

    public static ItemStack putStackInInventoryAllSlots(IInventory iInventory, ItemStack itemStack, EnumFacing enumFacing) {
        if (iInventory instanceof ISidedInventory && enumFacing != null) {
            ISidedInventory iSidedInventory = (ISidedInventory)iInventory;
            int[] nArray = iSidedInventory.getSlotsForFace(enumFacing);
            int n = 0;
            while (n < nArray.length && itemStack != null && itemStack.stackSize > 0) {
                itemStack = TileEntityHopper.insertStack(iInventory, itemStack, nArray[n], enumFacing);
                ++n;
            }
        } else {
            int n = iInventory.getSizeInventory();
            int n2 = 0;
            while (n2 < n && itemStack != null && itemStack.stackSize > 0) {
                itemStack = TileEntityHopper.insertStack(iInventory, itemStack, n2, enumFacing);
                ++n2;
            }
        }
        if (itemStack != null && itemStack.stackSize == 0) {
            itemStack = null;
        }
        return itemStack;
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.inventory.length) {
            this.inventory[n] = null;
            ++n;
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public static List<EntityItem> func_181556_a(World world, double d, double d2, double d3) {
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(d - 0.5, d2 - 0.5, d3 - 0.5, d + 0.5, d2 + 0.5, d3 + 0.5), EntitySelectors.selectAnything);
    }

    private static boolean canCombine(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.getItem() != itemStack2.getItem() ? false : (itemStack.getMetadata() != itemStack2.getMetadata() ? false : (itemStack.stackSize > itemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(itemStack, itemStack2)));
    }

    public void setCustomName(String string) {
        this.customName = string;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public double getXPos() {
        return (double)this.pos.getX() + 0.5;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.inventory[n] != null) {
            ItemStack itemStack = this.inventory[n];
            this.inventory[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    private static boolean isInventoryEmpty(IInventory iInventory, EnumFacing enumFacing) {
        if (iInventory instanceof ISidedInventory) {
            ISidedInventory iSidedInventory = (ISidedInventory)iInventory;
            int[] nArray = iSidedInventory.getSlotsForFace(enumFacing);
            int n = 0;
            while (n < nArray.length) {
                if (iSidedInventory.getStackInSlot(nArray[n]) != null) {
                    return false;
                }
                ++n;
            }
        } else {
            int n = iInventory.getSizeInventory();
            int n2 = 0;
            while (n2 < n) {
                if (iInventory.getStackInSlot(n2) != null) {
                    return false;
                }
                ++n2;
            }
        }
        return true;
    }

    private static boolean canInsertItemInSlot(IInventory iInventory, ItemStack itemStack, int n, EnumFacing enumFacing) {
        return !iInventory.isItemValidForSlot(n, itemStack) ? false : !(iInventory instanceof ISidedInventory) || ((ISidedInventory)iInventory).canInsertItem(n, itemStack, enumFacing);
    }

    private static boolean canExtractItemFromSlot(IInventory iInventory, ItemStack itemStack, int n, EnumFacing enumFacing) {
        return !(iInventory instanceof ISidedInventory) || ((ISidedInventory)iInventory).canExtractItem(n, itemStack, enumFacing);
    }

    public boolean mayTransfer() {
        return this.transferCooldown <= 1;
    }

    public static IInventory getHopperInventory(IHopper iHopper) {
        return TileEntityHopper.getInventoryAtPosition(iHopper.getWorld(), iHopper.getXPos(), iHopper.getYPos() + 1.0, iHopper.getZPos());
    }

    public static boolean captureDroppedItems(IHopper iHopper) {
        IInventory iInventory = TileEntityHopper.getHopperInventory(iHopper);
        if (iInventory != null) {
            EnumFacing enumFacing = EnumFacing.DOWN;
            if (TileEntityHopper.isInventoryEmpty(iInventory, enumFacing)) {
                return false;
            }
            if (iInventory instanceof ISidedInventory) {
                ISidedInventory iSidedInventory = (ISidedInventory)iInventory;
                int[] nArray = iSidedInventory.getSlotsForFace(enumFacing);
                int n = 0;
                while (n < nArray.length) {
                    if (TileEntityHopper.pullItemFromSlot(iHopper, iInventory, nArray[n], enumFacing)) {
                        return true;
                    }
                    ++n;
                }
            } else {
                int n = iInventory.getSizeInventory();
                int n2 = 0;
                while (n2 < n) {
                    if (TileEntityHopper.pullItemFromSlot(iHopper, iInventory, n2, enumFacing)) {
                        return true;
                    }
                    ++n2;
                }
            }
        } else {
            for (EntityItem entityItem : TileEntityHopper.func_181556_a(iHopper.getWorld(), iHopper.getXPos(), iHopper.getYPos() + 1.0, iHopper.getZPos())) {
                if (!TileEntityHopper.putDropInInventoryAllSlots(iHopper, entityItem)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isFull() {
        ItemStack[] itemStackArray = this.inventory;
        int n = this.inventory.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemStack = itemStackArray[n2];
            if (itemStack == null || itemStack.stackSize != itemStack.getMaxStackSize()) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    public boolean updateHopper() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
                boolean bl = false;
                if (!this.isEmpty()) {
                    bl = this.transferItemsOut();
                }
                if (!this.isFull()) {
                    boolean bl2 = bl = TileEntityHopper.captureDroppedItems(this) || bl;
                }
                if (bl) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.inventory.length) {
            if (this.inventory[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.inventory[n].writeToNBT(nBTTagCompound2);
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n;
        }
        nBTTagCompound.setTag("Items", nBTTagList);
        nBTTagCompound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            nBTTagCompound.setString("CustomName", this.customName);
        }
    }
}

