/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;

public class TileEntityBeacon
extends TileEntityLockable
implements ITickable,
IInventory {
    public static final Potion[][] effectsList = new Potion[][]{{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
    private final List<BeamSegment> beamSegments = Lists.newArrayList();
    private long beamRenderCounter;
    private float field_146014_j;
    private boolean isComplete;
    private int levels = -1;
    private int primaryEffect;
    private int secondaryEffect;
    private ItemStack payment;
    private String customName;

    @Override
    public void update() {
        if (this.worldObj.getTotalWorldTime() % 80L != 0L) return;
        this.updateBeacon();
    }

    public void updateBeacon() {
        this.updateSegmentColors();
        this.addEffectsToPlayers();
    }

    private void addEffectsToPlayers() {
        if (!this.isComplete) return;
        if (this.levels <= 0) return;
        if (this.worldObj.isRemote) return;
        if (this.primaryEffect <= 0) return;
        double d0 = this.levels * 10 + 10;
        int i = 0;
        if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
            i = 1;
        }
        int j = this.pos.getX();
        int k = this.pos.getY();
        int l = this.pos.getZ();
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(j, k, l, j + 1, k + 1, l + 1).expand(d0, d0, d0).addCoord(0.0, this.worldObj.getHeight(), 0.0);
        List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        for (EntityPlayer entityplayer : list) {
            entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, 180, i, true, true));
        }
        if (this.levels < 4) return;
        if (this.primaryEffect == this.secondaryEffect) return;
        if (this.secondaryEffect <= 0) return;
        Iterator<EntityPlayer> iterator = list.iterator();
        while (iterator.hasNext()) {
            EntityPlayer entityplayer1 = iterator.next();
            entityplayer1.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
        }
    }

    private void updateSegmentColors() {
        int i = this.levels;
        int j = this.pos.getX();
        int k = this.pos.getY();
        int l = this.pos.getZ();
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
        this.beamSegments.add(tileentitybeacon$beamsegment);
        boolean flag = true;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i1 = k + 1; i1 < 256; ++i1) {
            float[] afloat;
            IBlockState iblockstate = this.worldObj.getBlockState(blockpos$mutableblockpos.func_181079_c(j, i1, l));
            if (iblockstate.getBlock() == Blocks.stained_glass) {
                afloat = EntitySheep.func_175513_a(iblockstate.getValue(BlockStainedGlass.COLOR));
            } else {
                if (iblockstate.getBlock() != Blocks.stained_glass_pane) {
                    if (iblockstate.getBlock().getLightOpacity() >= 15 && iblockstate.getBlock() != Blocks.bedrock) {
                        this.isComplete = false;
                        this.beamSegments.clear();
                        break;
                    }
                    tileentitybeacon$beamsegment.incrementHeight();
                    continue;
                }
                afloat = EntitySheep.func_175513_a(iblockstate.getValue(BlockStainedGlassPane.COLOR));
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
            block1: while (l1 <= 4 && (i2 = k - l1) >= 0) {
                boolean flag1 = true;
                int j1 = j - l1;
                while (true) {
                    if (j1 <= j + l1 && flag1) {
                    } else {
                        if (flag1) {
                            this.levels = l1++;
                            continue block1;
                        }
                        if (this.levels != 0) break block1;
                        this.isComplete = false;
                        break block1;
                    }
                    for (int k1 = l - l1; k1 <= l + l1; ++k1) {
                        Block block = this.worldObj.getBlockState(new BlockPos(j1, i2, k1)).getBlock();
                        if (block == Blocks.emerald_block || block == Blocks.gold_block || block == Blocks.diamond_block || block == Blocks.iron_block) continue;
                        flag1 = false;
                        break;
                    }
                    ++j1;
                }
            }
        }
        if (this.worldObj.isRemote) return;
        if (this.levels != 4) return;
        if (i >= this.levels) return;
        Iterator<EntityPlayer> iterator = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(j, k, l, j, k - 4, l).expand(10.0, 5.0, 10.0)).iterator();
        while (iterator.hasNext()) {
            EntityPlayer entityplayer = iterator.next();
            entityplayer.triggerAchievement(AchievementList.fullBeacon);
        }
    }

    public List<BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }

    public float shouldBeamRender() {
        if (!this.isComplete) {
            return 0.0f;
        }
        int i = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.worldObj.getTotalWorldTime();
        if (i > 1) {
            this.field_146014_j -= (float)i / 40.0f;
            if (this.field_146014_j < 0.0f) {
                this.field_146014_j = 0.0f;
            }
        }
        this.field_146014_j += 0.025f;
        if (!(this.field_146014_j > 1.0f)) return this.field_146014_j;
        this.field_146014_j = 1.0f;
        return this.field_146014_j;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    private int func_183001_h(int p_183001_1_) {
        if (p_183001_1_ < 0) return 0;
        if (p_183001_1_ >= Potion.potionTypes.length) return 0;
        if (Potion.potionTypes[p_183001_1_] == null) return 0;
        Potion potion = Potion.potionTypes[p_183001_1_];
        if (potion != Potion.moveSpeed && potion != Potion.digSpeed && potion != Potion.resistance && potion != Potion.jump && potion != Potion.damageBoost && potion != Potion.regeneration) {
            return 0;
        }
        int n = p_183001_1_;
        return n;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.primaryEffect = this.func_183001_h(compound.getInteger("Primary"));
        this.secondaryEffect = this.func_183001_h(compound.getInteger("Secondary"));
        this.levels = compound.getInteger("Levels");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Primary", this.primaryEffect);
        compound.setInteger("Secondary", this.secondaryEffect);
        compound.setInteger("Levels", this.levels);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index != 0) return null;
        ItemStack itemStack = this.payment;
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index != 0) return null;
        if (this.payment == null) return null;
        if (count >= this.payment.stackSize) {
            ItemStack itemstack = this.payment;
            this.payment = null;
            return itemstack;
        }
        this.payment.stackSize -= count;
        return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index != 0) return null;
        if (this.payment == null) return null;
        ItemStack itemstack = this.payment;
        this.payment = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index != 0) return;
        this.payment = stack;
    }

    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.beacon";
        String string = this.customName;
        return string;
    }

    @Override
    public boolean hasCustomName() {
        if (this.customName == null) return false;
        if (this.customName.length() <= 0) return false;
        return true;
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
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
        if (stack.getItem() == Items.emerald) return true;
        if (stack.getItem() == Items.diamond) return true;
        if (stack.getItem() == Items.gold_ingot) return true;
        if (stack.getItem() == Items.iron_ingot) return true;
        return false;
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
                return this.primaryEffect;
            }
            case 2: {
                return this.secondaryEffect;
            }
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.levels = value;
                return;
            }
            case 1: {
                this.primaryEffect = this.func_183001_h(value);
                return;
            }
            case 2: {
                this.secondaryEffect = this.func_183001_h(value);
                return;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        this.payment = null;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id != 1) return super.receiveClientEvent(id, type);
        this.updateBeacon();
        return true;
    }

    public static class BeamSegment {
        private final float[] colors;
        private int height;

        public BeamSegment(float[] p_i45669_1_) {
            this.colors = p_i45669_1_;
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

