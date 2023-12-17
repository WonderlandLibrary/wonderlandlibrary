// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import java.util.Iterator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private static final int[] SLOTS;
    private NonNullList<ItemStack> items;
    private boolean hasBeenCleared;
    private int openCount;
    private AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    private EnumDyeColor color;
    private boolean destroyedByCreativePlayer;
    
    public TileEntityShulkerBox() {
        this(null);
    }
    
    public TileEntityShulkerBox(@Nullable final EnumDyeColor colorIn) {
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.animationStatus = AnimationStatus.CLOSED;
        this.color = colorIn;
    }
    
    @Override
    public void update() {
        this.updateAnimation();
        if (this.animationStatus == AnimationStatus.OPENING || this.animationStatus == AnimationStatus.CLOSING) {
            this.moveCollidedEntities();
        }
    }
    
    protected void updateAnimation() {
        this.progressOld = this.progress;
        switch (this.animationStatus) {
            case CLOSED: {
                this.progress = 0.0f;
                break;
            }
            case OPENING: {
                this.progress += 0.1f;
                if (this.progress >= 1.0f) {
                    this.moveCollidedEntities();
                    this.animationStatus = AnimationStatus.OPENED;
                    this.progress = 1.0f;
                    break;
                }
                break;
            }
            case CLOSING: {
                this.progress -= 0.1f;
                if (this.progress <= 0.0f) {
                    this.animationStatus = AnimationStatus.CLOSED;
                    this.progress = 0.0f;
                    break;
                }
                break;
            }
            case OPENED: {
                this.progress = 1.0f;
                break;
            }
        }
    }
    
    public AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }
    
    public AxisAlignedBB getBoundingBox(final IBlockState p_190584_1_) {
        return this.getBoundingBox(p_190584_1_.getValue(BlockShulkerBox.FACING));
    }
    
    public AxisAlignedBB getBoundingBox(final EnumFacing p_190587_1_) {
        return Block.FULL_BLOCK_AABB.expand(0.5f * this.getProgress(1.0f) * p_190587_1_.getXOffset(), 0.5f * this.getProgress(1.0f) * p_190587_1_.getYOffset(), 0.5f * this.getProgress(1.0f) * p_190587_1_.getZOffset());
    }
    
    private AxisAlignedBB getTopBoundingBox(final EnumFacing p_190588_1_) {
        final EnumFacing enumfacing = p_190588_1_.getOpposite();
        return this.getBoundingBox(p_190588_1_).contract(enumfacing.getXOffset(), enumfacing.getYOffset(), enumfacing.getZOffset());
    }
    
    private void moveCollidedEntities() {
        final IBlockState iblockstate = this.world.getBlockState(this.getPos());
        if (iblockstate.getBlock() instanceof BlockShulkerBox) {
            final EnumFacing enumfacing = iblockstate.getValue(BlockShulkerBox.FACING);
            final AxisAlignedBB axisalignedbb = this.getTopBoundingBox(enumfacing).offset(this.pos);
            final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    final Entity entity = list.get(i);
                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        double d0 = 0.0;
                        double d2 = 0.0;
                        double d3 = 0.0;
                        final AxisAlignedBB axisalignedbb2 = entity.getEntityBoundingBox();
                        switch (enumfacing.getAxis()) {
                            case X: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d0 = axisalignedbb.maxX - axisalignedbb2.minX;
                                }
                                else {
                                    d0 = axisalignedbb2.maxX - axisalignedbb.minX;
                                }
                                d0 += 0.01;
                                break;
                            }
                            case Y: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d2 = axisalignedbb.maxY - axisalignedbb2.minY;
                                }
                                else {
                                    d2 = axisalignedbb2.maxY - axisalignedbb.minY;
                                }
                                d2 += 0.01;
                                break;
                            }
                            case Z: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d3 = axisalignedbb.maxZ - axisalignedbb2.minZ;
                                }
                                else {
                                    d3 = axisalignedbb2.maxZ - axisalignedbb.minZ;
                                }
                                d3 += 0.01;
                                break;
                            }
                        }
                        entity.move(MoverType.SHULKER_BOX, d0 * enumfacing.getXOffset(), d2 * enumfacing.getYOffset(), d3 * enumfacing.getZOffset());
                    }
                }
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.items.size();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            if ((this.openCount = type) == 0) {
                this.animationStatus = AnimationStatus.CLOSING;
            }
            if (type == 1) {
                this.animationStatus = AnimationStatus.OPENING;
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            ++this.openCount;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
            if (this.openCount == 1) {
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            --this.openCount;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerShulkerBox(playerInventory, this, playerIn);
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:shulker_box";
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.shulkerBox";
    }
    
    public static void registerFixesShulkerBox(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.loadFromNbt(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        return this.saveToNbt(compound);
    }
    
    public void loadFromNbt(final NBTTagCompound compound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound) && compound.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(compound, this.items);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    public NBTTagCompound saveToNbt(final NBTTagCompound compound) {
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.items, false);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        if (!compound.hasKey("Lock") && this.isLocked()) {
            this.getLockCode().toNBT(compound);
        }
        return compound;
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return TileEntityShulkerBox.SLOTS;
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return true;
    }
    
    @Override
    public void clear() {
        this.hasBeenCleared = true;
        super.clear();
    }
    
    public boolean isCleared() {
        return this.hasBeenCleared;
    }
    
    public float getProgress(final float p_190585_1_) {
        return this.progressOld + (this.progress - this.progressOld) * p_190585_1_;
    }
    
    public EnumDyeColor getColor() {
        if (this.color == null) {
            this.color = BlockShulkerBox.getColorFromBlock(this.getBlockType());
        }
        return this.color;
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }
    
    public boolean isDestroyedByCreativePlayer() {
        return this.destroyedByCreativePlayer;
    }
    
    public void setDestroyedByCreativePlayer(final boolean p_190579_1_) {
        this.destroyedByCreativePlayer = p_190579_1_;
    }
    
    public boolean shouldDrop() {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }
    
    static {
        SLOTS = new int[27];
        for (int i = 0; i < TileEntityShulkerBox.SLOTS.length; TileEntityShulkerBox.SLOTS[i] = i++) {}
    }
    
    public enum AnimationStatus
    {
        CLOSED, 
        OPENING, 
        OPENED, 
        CLOSING;
    }
}
