// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.IInventory;
import java.util.Iterator;
import net.minecraft.block.BlockHopper;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ITickable;

public class TileEntityHopper extends TileEntityLockableLoot implements IHopper, ITickable
{
    private NonNullList<ItemStack> inventory;
    private int transferCooldown;
    private long tickedGameTime;
    
    public TileEntityHopper() {
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.transferCooldown = -1;
    }
    
    public static void registerFixesHopper(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityHopper.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
    
    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        this.fillWithLoot(null);
        final ItemStack itemstack = ItemStackHelper.getAndSplit(this.getItems(), index, count);
        return itemstack;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.fillWithLoot(null);
        this.getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.updateHopper();
            }
        }
    }
    
    private boolean updateHopper() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;
                if (!this.isInventoryEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (!this.isFull()) {
                    flag = (pullItems(this) || flag);
                }
                if (flag) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean isInventoryEmpty() {
        for (final ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isEmpty() {
        return this.isInventoryEmpty();
    }
    
    private boolean isFull() {
        for (final ItemStack itemstack : this.inventory) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean transferItemsOut() {
        final IInventory iinventory = this.getInventoryForHopperTransfer();
        if (iinventory == null) {
            return false;
        }
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
        if (this.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                final ItemStack itemstack = this.getStackInSlot(i).copy();
                final ItemStack itemstack2 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1), enumfacing);
                if (itemstack2.isEmpty()) {
                    iinventory.markDirty();
                    return true;
                }
                this.setInventorySlotContents(i, itemstack);
            }
        }
        return false;
    }
    
    private boolean isInventoryFull(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] slotsForFace;
            final int[] aint = slotsForFace = isidedinventory.getSlotsForFace(side);
            for (final int k : slotsForFace) {
                final ItemStack itemstack1 = isidedinventory.getStackInSlot(k);
                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize()) {
                    return false;
                }
            }
        }
        else {
            for (int i = inventoryIn.getSizeInventory(), j = 0; j < i; ++j) {
                final ItemStack itemstack2 = inventoryIn.getStackInSlot(j);
                if (itemstack2.isEmpty() || itemstack2.getCount() != itemstack2.getMaxStackSize()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isInventoryEmpty(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] slotsForFace;
            final int[] aint = slotsForFace = isidedinventory.getSlotsForFace(side);
            for (final int i : slotsForFace) {
                if (!isidedinventory.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
        }
        else {
            for (int j = inventoryIn.getSizeInventory(), k = 0; k < j; ++k) {
                if (!inventoryIn.getStackInSlot(k).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean pullItems(final IHopper hopper) {
        final IInventory iinventory = getSourceInventory(hopper);
        if (iinventory != null) {
            final EnumFacing enumfacing = EnumFacing.DOWN;
            if (isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }
            if (iinventory instanceof ISidedInventory) {
                final ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                final int[] slotsForFace;
                final int[] aint = slotsForFace = isidedinventory.getSlotsForFace(enumfacing);
                for (final int i : slotsForFace) {
                    if (pullItemFromSlot(hopper, iinventory, i, enumfacing)) {
                        return true;
                    }
                }
            }
            else {
                for (int j = iinventory.getSizeInventory(), k = 0; k < j; ++k) {
                    if (pullItemFromSlot(hopper, iinventory, k, enumfacing)) {
                        return true;
                    }
                }
            }
        }
        else {
            for (final EntityItem entityitem : getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos())) {
                if (putDropInInventoryAllSlots(null, hopper, entityitem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean pullItemFromSlot(final IHopper hopper, final IInventory inventoryIn, final int index, final EnumFacing direction) {
        final ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (!itemstack.isEmpty() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            final ItemStack itemstack2 = itemstack.copy();
            final ItemStack itemstack3 = putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), null);
            if (itemstack3.isEmpty()) {
                inventoryIn.markDirty();
                return true;
            }
            inventoryIn.setInventorySlotContents(index, itemstack2);
        }
        return false;
    }
    
    public static boolean putDropInInventoryAllSlots(final IInventory source, final IInventory destination, final EntityItem entity) {
        boolean flag = false;
        if (entity == null) {
            return false;
        }
        final ItemStack itemstack = entity.getItem().copy();
        final ItemStack itemstack2 = putStackInInventoryAllSlots(source, destination, itemstack, null);
        if (itemstack2.isEmpty()) {
            flag = true;
            entity.setDead();
        }
        else {
            entity.setItem(itemstack2);
        }
        return flag;
    }
    
    public static ItemStack putStackInInventoryAllSlots(final IInventory source, final IInventory destination, ItemStack stack, @Nullable final EnumFacing direction) {
        if (destination instanceof ISidedInventory && direction != null) {
            final ISidedInventory isidedinventory = (ISidedInventory)destination;
            final int[] aint = isidedinventory.getSlotsForFace(direction);
            for (int k = 0; k < aint.length && !stack.isEmpty(); stack = insertStack(source, destination, stack, aint[k], direction), ++k) {}
        }
        else {
            for (int i = destination.getSizeInventory(), j = 0; j < i && !stack.isEmpty(); stack = insertStack(source, destination, stack, j, direction), ++j) {}
        }
        return stack;
    }
    
    private static boolean canInsertItemInSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return inventoryIn.isItemValidForSlot(index, stack) && (!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side));
    }
    
    private static boolean canExtractItemFromSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }
    
    private static ItemStack insertStack(final IInventory source, final IInventory destination, ItemStack stack, final int index, final EnumFacing direction) {
        final ItemStack itemstack = destination.getStackInSlot(index);
        if (canInsertItemInSlot(destination, stack, index, direction)) {
            boolean flag = false;
            final boolean flag2 = destination.isEmpty();
            if (itemstack.isEmpty()) {
                destination.setInventorySlotContents(index, stack);
                stack = ItemStack.EMPTY;
                flag = true;
            }
            else if (canCombine(itemstack, stack)) {
                final int i = stack.getMaxStackSize() - itemstack.getCount();
                final int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
                flag = (j > 0);
            }
            if (flag) {
                if (flag2 && destination instanceof TileEntityHopper) {
                    final TileEntityHopper tileentityhopper1 = (TileEntityHopper)destination;
                    if (!tileentityhopper1.mayTransfer()) {
                        int k = 0;
                        if (source != null && source instanceof TileEntityHopper) {
                            final TileEntityHopper tileentityhopper2 = (TileEntityHopper)source;
                            if (tileentityhopper1.tickedGameTime >= tileentityhopper2.tickedGameTime) {
                                k = 1;
                            }
                        }
                        tileentityhopper1.setTransferCooldown(8 - k);
                    }
                }
                destination.markDirty();
            }
        }
        return stack;
    }
    
    private IInventory getInventoryForHopperTransfer() {
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return getInventoryAtPosition(this.getWorld(), this.getXPos() + enumfacing.getXOffset(), this.getYPos() + enumfacing.getYOffset(), this.getZPos() + enumfacing.getZOffset());
    }
    
    public static IInventory getSourceInventory(final IHopper hopper) {
        return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos());
    }
    
    public static List<EntityItem> getCaptureItems(final World worldIn, final double p_184292_1_, final double p_184292_3_, final double p_184292_5_) {
        return worldIn.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, new AxisAlignedBB(p_184292_1_ - 0.5, p_184292_3_, p_184292_5_ - 0.5, p_184292_1_ + 0.5, p_184292_3_ + 1.5, p_184292_5_ + 0.5), (com.google.common.base.Predicate<? super EntityItem>)EntitySelectors.IS_ALIVE);
    }
    
    public static IInventory getInventoryAtPosition(final World worldIn, final double x, final double y, final double z) {
        IInventory iinventory = null;
        final int i = MathHelper.floor(x);
        final int j = MathHelper.floor(y);
        final int k = MathHelper.floor(z);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity()) {
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;
                if (iinventory instanceof TileEntityChest && block instanceof BlockChest) {
                    iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
                }
            }
        }
        if (iinventory == null) {
            final List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.HAS_INVENTORY);
            if (!list.isEmpty()) {
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }
        return iinventory;
    }
    
    private static boolean canCombine(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata() && stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
    
    @Override
    public double getXPos() {
        return this.pos.getX() + 0.5;
    }
    
    @Override
    public double getYPos() {
        return this.pos.getY() + 0.5;
    }
    
    @Override
    public double getZPos() {
        return this.pos.getZ() + 0.5;
    }
    
    private void setTransferCooldown(final int ticks) {
        this.transferCooldown = ticks;
    }
    
    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }
    
    private boolean mayTransfer() {
        return this.transferCooldown > 8;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerHopper(playerInventory, this, playerIn);
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }
}
