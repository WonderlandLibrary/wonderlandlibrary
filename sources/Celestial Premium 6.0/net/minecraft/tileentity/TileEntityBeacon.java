/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityBeacon
extends TileEntityLockable
implements ITickable,
ISidedInventory {
    public static final Potion[][] EFFECTS_LIST = new Potion[][]{{MobEffects.SPEED, MobEffects.HASTE}, {MobEffects.RESISTANCE, MobEffects.JUMP_BOOST}, {MobEffects.STRENGTH}, {MobEffects.REGENERATION}};
    private static final Set<Potion> VALID_EFFECTS = Sets.newHashSet();
    private final List<BeamSegment> beamSegments = Lists.newArrayList();
    private long beamRenderCounter;
    private float beamRenderScale;
    private boolean isComplete;
    private int levels = -1;
    @Nullable
    private Potion primaryEffect;
    @Nullable
    private Potion secondaryEffect;
    private ItemStack payment = ItemStack.field_190927_a;
    private String customName;

    @Override
    public void update() {
        if (this.world.getTotalWorldTime() % 80L == 0L) {
            this.updateBeacon();
        }
    }

    public void updateBeacon() {
        if (this.world != null) {
            this.updateSegmentColors();
            this.addEffectsToPlayers();
        }
    }

    private void addEffectsToPlayers() {
        if (this.isComplete && this.levels > 0 && !this.world.isRemote && this.primaryEffect != null) {
            double d0 = this.levels * 10 + 10;
            int i = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                i = 1;
            }
            int j = (9 + this.levels * 2) * 20;
            int k = this.pos.getX();
            int l = this.pos.getY();
            int i1 = this.pos.getZ();
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(k, l, i1, k + 1, l + 1, i1 + 1).expandXyz(d0).addCoord(0.0, this.world.getHeight(), 0.0);
            List<EntityPlayer> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
            for (EntityPlayer entityplayer : list) {
                entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, j, i, true, true));
            }
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
                for (EntityPlayer entityplayer1 : list) {
                    entityplayer1.addPotionEffect(new PotionEffect(this.secondaryEffect, j, 0, true, true));
                }
            }
        }
    }

    private void updateSegmentColors() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        int l = this.levels;
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EnumDyeColor.WHITE.func_193349_f());
        this.beamSegments.add(tileentitybeacon$beamsegment);
        boolean flag = true;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i1 = j + 1; i1 < 256; ++i1) {
            float[] afloat;
            IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos.setPos(i, i1, k));
            if (iblockstate.getBlock() == Blocks.STAINED_GLASS) {
                afloat = iblockstate.getValue(BlockStainedGlass.COLOR).func_193349_f();
            } else {
                if (iblockstate.getBlock() != Blocks.STAINED_GLASS_PANE) {
                    if (iblockstate.getLightOpacity() >= 15 && iblockstate.getBlock() != Blocks.BEDROCK) {
                        this.isComplete = false;
                        this.beamSegments.clear();
                        break;
                    }
                    tileentitybeacon$beamsegment.incrementHeight();
                    continue;
                }
                afloat = iblockstate.getValue(BlockStainedGlassPane.COLOR).func_193349_f();
            }
            if (!flag) {
                afloat = new float[]{(tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0f};
            }
            if (Arrays.equals(afloat, tileentitybeacon$beamsegment.getColors())) {
                tileentitybeacon$beamsegment.incrementHeight();
            } else {
                tileentitybeacon$beamsegment = new BeamSegment(afloat);
                this.beamSegments.add(tileentitybeacon$beamsegment);
            }
            flag = false;
        }
        if (this.isComplete) {
            int i2;
            int l1 = 1;
            while (l1 <= 4 && (i2 = j - l1) >= 0) {
                boolean flag1 = true;
                block2: for (int j1 = i - l1; j1 <= i + l1 && flag1; ++j1) {
                    for (int k1 = k - l1; k1 <= k + l1; ++k1) {
                        Block block = this.world.getBlockState(new BlockPos(j1, i2, k1)).getBlock();
                        if (block == Blocks.EMERALD_BLOCK || block == Blocks.GOLD_BLOCK || block == Blocks.DIAMOND_BLOCK || block == Blocks.IRON_BLOCK) continue;
                        flag1 = false;
                        continue block2;
                    }
                }
                if (!flag1) break;
                this.levels = l1++;
            }
            if (this.levels == 0) {
                this.isComplete = false;
            }
        }
        if (!this.world.isRemote && l < this.levels) {
            for (EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(i, j, k, i, j - 4, k).expand(10.0, 5.0, 10.0))) {
                CriteriaTriggers.field_192131_k.func_192180_a(entityplayermp, this);
            }
        }
    }

    public List<BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }

    public float shouldBeamRender() {
        if (!this.isComplete) {
            return 0.0f;
        }
        int i = (int)(this.world.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.world.getTotalWorldTime();
        if (i > 1) {
            this.beamRenderScale -= (float)i / 40.0f;
            if (this.beamRenderScale < 0.0f) {
                this.beamRenderScale = 0.0f;
            }
        }
        this.beamRenderScale += 0.025f;
        if (this.beamRenderScale > 1.0f) {
            this.beamRenderScale = 1.0f;
        }
        return this.beamRenderScale;
    }

    public int getLevels() {
        return this.levels;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @Nullable
    private static Potion isBeaconEffect(int p_184279_0_) {
        Potion potion = Potion.getPotionById(p_184279_0_);
        return VALID_EFFECTS.contains(potion) ? potion : null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.primaryEffect = TileEntityBeacon.isBeaconEffect(compound.getInteger("Primary"));
        this.secondaryEffect = TileEntityBeacon.isBeaconEffect(compound.getInteger("Secondary"));
        this.levels = compound.getInteger("Levels");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Primary", Potion.getIdFromPotion(this.primaryEffect));
        compound.setInteger("Secondary", Potion.getIdFromPotion(this.secondaryEffect));
        compound.setInteger("Levels", this.levels);
        return compound;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean func_191420_l() {
        return this.payment.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? this.payment : ItemStack.field_190927_a;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 0 && !this.payment.isEmpty()) {
            if (count >= this.payment.getCount()) {
                ItemStack itemstack = this.payment;
                this.payment = ItemStack.field_190927_a;
                return itemstack;
            }
            return this.payment.splitStack(count);
        }
        return ItemStack.field_190927_a;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index == 0) {
            ItemStack itemstack = this.payment;
            this.payment = ItemStack.field_190927_a;
            return itemstack;
        }
        return ItemStack.field_190927_a;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            this.payment = stack;
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.beacon";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        return player.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() == Items.EMERALD || stack.getItem() == Items.DIAMOND || stack.getItem() == Items.GOLD_INGOT || stack.getItem() == Items.IRON_INGOT;
    }

    @Override
    public String getGuiID() {
        return "minecraft:beacon";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerBeacon(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: {
                return this.levels;
            }
            case 1: {
                return Potion.getIdFromPotion(this.primaryEffect);
            }
            case 2: {
                return Potion.getIdFromPotion(this.secondaryEffect);
            }
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.levels = value;
                break;
            }
            case 1: {
                this.primaryEffect = TileEntityBeacon.isBeaconEffect(value);
                break;
            }
            case 2: {
                this.secondaryEffect = TileEntityBeacon.isBeaconEffect(value);
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        this.payment = ItemStack.field_190927_a;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.updateBeacon();
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    static {
        for (Potion[] apotion : EFFECTS_LIST) {
            Collections.addAll(VALID_EFFECTS, apotion);
        }
    }

    public static class BeamSegment {
        private final float[] colors;
        private int height;

        public BeamSegment(float[] colorsIn) {
            this.colors = colorsIn;
            this.height = 1;
        }

        protected void incrementHeight() {
            ++this.height;
        }

        public float[] getColors() {
            return this.colors;
        }

        public int getHeight() {
            return this.height;
        }
    }
}

