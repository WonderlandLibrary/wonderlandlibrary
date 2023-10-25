/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper
extends TileEntityLockableLoot
implements IHopper,
ITickable {
    private NonNullList<ItemStack> inventory = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
    private int transferCooldown = -1;
    private long field_190578_g;

    public static void registerFixesHopper(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityHopper.class, "Items"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.func_191283_b(compound, this.inventory);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.field_190577_o = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.func_191282_a(compound, this.inventory);
        }
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_190577_o);
        }
        return compound;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        this.fillWithLoot(null);
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.func_190576_q(), index, count);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.fillWithLoot(null);
        this.func_190576_q().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.hopper";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.field_190578_g = this.world.getTotalWorldTime();
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
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (!this.isFull()) {
                    boolean bl = flag = TileEntityHopper.captureDroppedItems(this) || flag;
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

    private boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean func_191420_l() {
        return this.isEmpty();
    }

    private boolean isFull() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty() && itemstack.getCount() == itemstack.getMaxStackSize()) continue;
            return false;
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
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i).isEmpty()) continue;
            ItemStack itemstack = this.getStackInSlot(i).copy();
            ItemStack itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1), enumfacing);
            if (itemstack1.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
            this.setInventorySlotContents(i, itemstack);
        }
        return false;
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            int[] aint;
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            for (int k : aint = isidedinventory.getSlotsForFace(side)) {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);
                if (!itemstack1.isEmpty() && itemstack1.getCount() == itemstack1.getMaxStackSize()) continue;
                return false;
            }
        } else {
            int i = inventoryIn.getSizeInventory();
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);
                if (!itemstack.isEmpty() && itemstack.getCount() == itemstack.getMaxStackSize()) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            int[] aint;
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            for (int i : aint = isidedinventory.getSlotsForFace(side)) {
                if (isidedinventory.getStackInSlot(i).isEmpty()) continue;
                return false;
            }
        } else {
            int j = inventoryIn.getSizeInventory();
            for (int k = 0; k < j; ++k) {
                if (inventoryIn.getStackInSlot(k).isEmpty()) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean captureDroppedItems(IHopper hopper) {
        IInventory iinventory = TileEntityHopper.getHopperInventory(hopper);
        if (iinventory != null) {
            EnumFacing enumfacing = EnumFacing.DOWN;
            if (TileEntityHopper.isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }
            if (iinventory instanceof ISidedInventory) {
                int[] aint;
                ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                for (int i : aint = isidedinventory.getSlotsForFace(enumfacing)) {
                    if (!TileEntityHopper.pullItemFromSlot(hopper, iinventory, i, enumfacing)) continue;
                    return true;
                }
            } else {
                int j = iinventory.getSizeInventory();
                for (int k = 0; k < j; ++k) {
                    if (!TileEntityHopper.pullItemFromSlot(hopper, iinventory, k, enumfacing)) continue;
                    return true;
                }
            }
        } else {
            for (EntityItem entityitem : TileEntityHopper.getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos())) {
                if (!TileEntityHopper.putDropInInventoryAllSlots(null, hopper, entityitem)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (!itemstack.isEmpty() && TileEntityHopper.canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = TileEntityHopper.putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), null);
            if (itemstack2.isEmpty()) {
                inventoryIn.markDirty();
                return true;
            }
            inventoryIn.setInventorySlotContents(index, itemstack1);
        }
        return false;
    }

    public static boolean putDropInInventoryAllSlots(IInventory p_145898_0_, IInventory itemIn, EntityItem p_145898_2_) {
        boolean flag = false;
        if (p_145898_2_ == null) {
            return false;
        }
        ItemStack itemstack = p_145898_2_.getItem().copy();
        ItemStack itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(p_145898_0_, itemIn, itemstack, null);
        if (itemstack1.isEmpty()) {
            flag = true;
            p_145898_2_.setDead();
        } else {
            p_145898_2_.setEntityItemStack(itemstack1);
        }
        return flag;
    }

    public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, IInventory stack, ItemStack side, @Nullable EnumFacing p_174918_3_) {
        if (stack instanceof ISidedInventory && p_174918_3_ != null) {
            ISidedInventory isidedinventory = (ISidedInventory)stack;
            int[] aint = isidedinventory.getSlotsForFace(p_174918_3_);
            for (int k = 0; k < aint.length && !side.isEmpty(); ++k) {
                side = TileEntityHopper.insertStack(inventoryIn, stack, side, aint[k], p_174918_3_);
            }
        } else {
            int i = stack.getSizeInventory();
            for (int j = 0; j < i && !side.isEmpty(); ++j) {
                side = TileEntityHopper.insertStack(inventoryIn, stack, side, j, p_174918_3_);
            }
        }
        return side;
    }

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        if (!inventoryIn.isItemValidForSlot(index, stack)) {
            return false;
        }
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }

    private static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side, EnumFacing p_174916_4_) {
        ItemStack itemstack = stack.getStackInSlot(side);
        if (TileEntityHopper.canInsertItemInSlot(stack, index, side, p_174916_4_)) {
            boolean flag = false;
            boolean flag1 = stack.func_191420_l();
            if (itemstack.isEmpty()) {
                stack.setInventorySlotContents(side, index);
                index = ItemStack.field_190927_a;
                flag = true;
            } else if (TileEntityHopper.canCombine(itemstack, index)) {
                int i = index.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(index.getCount(), i);
                index.func_190918_g(j);
                itemstack.func_190917_f(j);
                boolean bl = flag = j > 0;
            }
            if (flag) {
                TileEntityHopper tileentityhopper1;
                if (flag1 && stack instanceof TileEntityHopper && !(tileentityhopper1 = (TileEntityHopper)stack).mayTransfer()) {
                    int k = 0;
                    if (inventoryIn != null && inventoryIn instanceof TileEntityHopper) {
                        TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
                        if (tileentityhopper1.field_190578_g >= tileentityhopper.field_190578_g) {
                            k = 1;
                        }
                    }
                    tileentityhopper1.setTransferCooldown(8 - k);
                }
                stack.markDirty();
            }
        }
        return index;
    }

    private IInventory getInventoryForHopperTransfer() {
        EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.getXPos() + (double)enumfacing.getXOffset(), this.getYPos() + (double)enumfacing.getYOffset(), this.getZPos() + (double)enumfacing.getZOffset());
    }

    public static IInventory getHopperInventory(IHopper hopper) {
        return TileEntityHopper.getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos());
    }

    public static List<EntityItem> getCaptureItems(World worldIn, double p_184292_1_, double p_184292_3_, double p_184292_5_) {
        return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_184292_1_ - 0.5, p_184292_3_, p_184292_5_ - 0.5, p_184292_1_ + 0.5, p_184292_3_ + 1.5, p_184292_5_ + 0.5), EntitySelectors.IS_ALIVE);
    }

    public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
        List<Entity> list;
        TileEntity tileentity;
        int k;
        int j;
        IInventory iinventory = null;
        int i = MathHelper.floor(x);
        BlockPos blockpos = new BlockPos(i, j = MathHelper.floor(y), k = MathHelper.floor(z));
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity() && (tileentity = worldIn.getTileEntity(blockpos)) instanceof IInventory && (iinventory = (IInventory)((Object)tileentity)) instanceof TileEntityChest && block instanceof BlockChest) {
            iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
        }
        if (iinventory == null && !(list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.HAS_INVENTORY)).isEmpty()) {
            iinventory = (IInventory)((Object)list.get(worldIn.rand.nextInt(list.size())));
        }
        return iinventory;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        }
        if (stack1.getMetadata() != stack2.getMetadata()) {
            return false;
        }
        if (stack1.getCount() > stack1.getMaxStackSize()) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(stack1, stack2);
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

    private void setTransferCooldown(int ticks) {
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
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerHopper(playerInventory, this, playerIn);
    }

    @Override
    protected NonNullList<ItemStack> func_190576_q() {
        return this.inventory;
    }
}

