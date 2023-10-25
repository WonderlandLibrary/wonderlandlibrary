/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityShulkerBox
extends TileEntityLockableLoot
implements ITickable,
ISidedInventory {
    private static final int[] field_190595_a = new int[27];
    private NonNullList<ItemStack> field_190596_f = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
    private boolean field_190597_g;
    private int field_190598_h;
    private AnimationStatus field_190599_i = AnimationStatus.CLOSED;
    private float field_190600_j;
    private float field_190601_k;
    private EnumDyeColor field_190602_l;
    private boolean field_190594_p;

    public TileEntityShulkerBox() {
        this(null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor p_i47242_1_) {
        this.field_190602_l = p_i47242_1_;
    }

    @Override
    public void update() {
        this.func_190583_o();
        if (this.field_190599_i == AnimationStatus.OPENING || this.field_190599_i == AnimationStatus.CLOSING) {
            this.func_190589_G();
        }
    }

    protected void func_190583_o() {
        this.field_190601_k = this.field_190600_j;
        switch (this.field_190599_i) {
            case CLOSED: {
                this.field_190600_j = 0.0f;
                break;
            }
            case OPENING: {
                this.field_190600_j += 0.1f;
                if (!(this.field_190600_j >= 1.0f)) break;
                this.func_190589_G();
                this.field_190599_i = AnimationStatus.OPENED;
                this.field_190600_j = 1.0f;
                break;
            }
            case CLOSING: {
                this.field_190600_j -= 0.1f;
                if (!(this.field_190600_j <= 0.0f)) break;
                this.field_190599_i = AnimationStatus.CLOSED;
                this.field_190600_j = 0.0f;
                break;
            }
            case OPENED: {
                this.field_190600_j = 1.0f;
            }
        }
    }

    public AnimationStatus func_190591_p() {
        return this.field_190599_i;
    }

    public AxisAlignedBB func_190584_a(IBlockState p_190584_1_) {
        return this.func_190587_b(p_190584_1_.getValue(BlockShulkerBox.field_190957_a));
    }

    public AxisAlignedBB func_190587_b(EnumFacing p_190587_1_) {
        return Block.FULL_BLOCK_AABB.addCoord(0.5f * this.func_190585_a(1.0f) * (float)p_190587_1_.getXOffset(), 0.5f * this.func_190585_a(1.0f) * (float)p_190587_1_.getYOffset(), 0.5f * this.func_190585_a(1.0f) * (float)p_190587_1_.getZOffset());
    }

    private AxisAlignedBB func_190588_c(EnumFacing p_190588_1_) {
        EnumFacing enumfacing = p_190588_1_.getOpposite();
        return this.func_190587_b(p_190588_1_).func_191195_a(enumfacing.getXOffset(), enumfacing.getYOffset(), enumfacing.getZOffset());
    }

    private void func_190589_G() {
        EnumFacing enumfacing;
        AxisAlignedBB axisalignedbb;
        List<Entity> list;
        IBlockState iblockstate = this.world.getBlockState(this.getPos());
        if (iblockstate.getBlock() instanceof BlockShulkerBox && !(list = this.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb = this.func_190588_c(enumfacing = iblockstate.getValue(BlockShulkerBox.field_190957_a)).offset(this.pos))).isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = list.get(i);
                if (entity.getPushReaction() == EnumPushReaction.IGNORE) continue;
                double d0 = 0.0;
                double d1 = 0.0;
                double d2 = 0.0;
                AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();
                switch (enumfacing.getAxis()) {
                    case X: {
                        d0 = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxX - axisalignedbb1.minX : axisalignedbb1.maxX - axisalignedbb.minX;
                        d0 += 0.01;
                        break;
                    }
                    case Y: {
                        d1 = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxY - axisalignedbb1.minY : axisalignedbb1.maxY - axisalignedbb.minY;
                        d1 += 0.01;
                        break;
                    }
                    case Z: {
                        d2 = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxZ - axisalignedbb1.minZ : axisalignedbb1.maxZ - axisalignedbb.minZ;
                        d2 += 0.01;
                    }
                }
                entity.moveEntity(MoverType.SHULKER_BOX, d0 * (double)enumfacing.getXOffset(), d1 * (double)enumfacing.getYOffset(), d2 * (double)enumfacing.getZOffset());
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return this.field_190596_f.size();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.field_190598_h = type;
            if (type == 0) {
                this.field_190599_i = AnimationStatus.CLOSING;
            }
            if (type == 1) {
                this.field_190599_i = AnimationStatus.OPENING;
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.field_190598_h < 0) {
                this.field_190598_h = 0;
            }
            ++this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);
            if (this.field_190598_h == 1) {
                this.world.playSound(null, this.pos, SoundEvents.field_191262_fB, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            --this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);
            if (this.field_190598_h <= 0) {
                this.world.playSound(null, this.pos, SoundEvents.field_191261_fA, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerShulkerBox(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID() {
        return "minecraft:shulker_box";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.shulkerBox";
    }

    public static void func_190593_a(DataFixer p_190593_0_) {
        p_190593_0_.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, "Items"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.func_190586_e(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        return this.func_190580_f(compound);
    }

    public void func_190586_e(NBTTagCompound p_190586_1_) {
        this.field_190596_f = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (!this.checkLootAndRead(p_190586_1_) && p_190586_1_.hasKey("Items", 9)) {
            ItemStackHelper.func_191283_b(p_190586_1_, this.field_190596_f);
        }
        if (p_190586_1_.hasKey("CustomName", 8)) {
            this.field_190577_o = p_190586_1_.getString("CustomName");
        }
    }

    public NBTTagCompound func_190580_f(NBTTagCompound p_190580_1_) {
        if (!this.checkLootAndWrite(p_190580_1_)) {
            ItemStackHelper.func_191281_a(p_190580_1_, this.field_190596_f, false);
        }
        if (this.hasCustomName()) {
            p_190580_1_.setString("CustomName", this.field_190577_o);
        }
        if (!p_190580_1_.hasKey("Lock") && this.isLocked()) {
            this.getLockCode().toNBT(p_190580_1_);
        }
        return p_190580_1_;
    }

    @Override
    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_190596_f;
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.field_190596_f) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return field_190595_a;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public void clear() {
        this.field_190597_g = true;
        super.clear();
    }

    public boolean func_190590_r() {
        return this.field_190597_g;
    }

    public float func_190585_a(float p_190585_1_) {
        return this.field_190601_k + (this.field_190600_j - this.field_190601_k) * p_190585_1_;
    }

    public EnumDyeColor func_190592_s() {
        if (this.field_190602_l == null) {
            this.field_190602_l = BlockShulkerBox.func_190954_c(this.getBlockType());
        }
        return this.field_190602_l;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }

    public boolean func_190581_E() {
        return this.field_190594_p;
    }

    public void func_190579_a(boolean p_190579_1_) {
        this.field_190594_p = p_190579_1_;
    }

    public boolean func_190582_F() {
        return !this.func_190581_E() || !this.func_191420_l() || this.hasCustomName() || this.lootTable != null;
    }

    static {
        int i = 0;
        while (i < field_190595_a.length) {
            TileEntityShulkerBox.field_190595_a[i] = i++;
        }
    }

    public static enum AnimationStatus {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;

    }
}

