// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Sets;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Arrays;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.EnumDyeColor;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import net.minecraft.potion.Potion;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityBeacon extends TileEntityLockable implements ITickable, ISidedInventory
{
    public static final Potion[][] EFFECTS_LIST;
    private static final Set<Potion> VALID_EFFECTS;
    private final List<BeamSegment> beamSegments;
    private long beamRenderCounter;
    private float beamRenderScale;
    private boolean isComplete;
    private int levels;
    @Nullable
    private Potion primaryEffect;
    @Nullable
    private Potion secondaryEffect;
    private ItemStack payment;
    private String customName;
    
    public TileEntityBeacon() {
        this.beamSegments = (List<BeamSegment>)Lists.newArrayList();
        this.levels = -1;
        this.payment = ItemStack.EMPTY;
    }
    
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
            final double d0 = this.levels * 10 + 10;
            int i = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                i = 1;
            }
            final int j = (9 + this.levels * 2) * 20;
            final int k = this.pos.getX();
            final int l = this.pos.getY();
            final int i2 = this.pos.getZ();
            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(k, l, i2, k + 1, l + 1, i2 + 1).grow(d0).expand(0.0, this.world.getHeight(), 0.0);
            final List<EntityPlayer> list = this.world.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, axisalignedbb);
            for (final EntityPlayer entityplayer : list) {
                entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, j, i, true, true));
            }
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
                for (final EntityPlayer entityplayer2 : list) {
                    entityplayer2.addPotionEffect(new PotionEffect(this.secondaryEffect, j, 0, true, true));
                }
            }
        }
    }
    
    private void updateSegmentColors() {
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        final int l = this.levels;
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EnumDyeColor.WHITE.getColorComponentValues());
        this.beamSegments.add(tileentitybeacon$beamsegment);
        boolean flag = true;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i2 = j + 1; i2 < 256; ++i2) {
            final IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos.setPos(i, i2, k));
            float[] afloat;
            if (iblockstate.getBlock() == Blocks.STAINED_GLASS) {
                afloat = iblockstate.getValue(BlockStainedGlass.COLOR).getColorComponentValues();
            }
            else if (iblockstate.getBlock() != Blocks.STAINED_GLASS_PANE) {
                if (iblockstate.getLightOpacity() >= 15 && iblockstate.getBlock() != Blocks.BEDROCK) {
                    this.isComplete = false;
                    this.beamSegments.clear();
                    break;
                }
                tileentitybeacon$beamsegment.incrementHeight();
                continue;
            }
            else {
                afloat = iblockstate.getValue(BlockStainedGlassPane.COLOR).getColorComponentValues();
            }
            if (!flag) {
                afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0f };
            }
            if (Arrays.equals(afloat, tileentitybeacon$beamsegment.getColors())) {
                tileentitybeacon$beamsegment.incrementHeight();
            }
            else {
                tileentitybeacon$beamsegment = new BeamSegment(afloat);
                this.beamSegments.add(tileentitybeacon$beamsegment);
            }
            flag = false;
        }
        if (this.isComplete) {
            for (int l2 = 1; l2 <= 4; this.levels = l2++) {
                final int i3 = j - l2;
                if (i3 < 0) {
                    break;
                }
                boolean flag2 = true;
                for (int j2 = i - l2; j2 <= i + l2 && flag2; ++j2) {
                    for (int k2 = k - l2; k2 <= k + l2; ++k2) {
                        final Block block = this.world.getBlockState(new BlockPos(j2, i3, k2)).getBlock();
                        if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
                            flag2 = false;
                            break;
                        }
                    }
                }
                if (!flag2) {
                    break;
                }
            }
            if (this.levels == 0) {
                this.isComplete = false;
            }
        }
        if (!this.world.isRemote && l < this.levels) {
            for (final EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, new AxisAlignedBB(i, j, k, i, j - 4, k).grow(10.0, 5.0, 10.0))) {
                CriteriaTriggers.CONSTRUCT_BEACON.trigger(entityplayermp, this);
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
        final int i = (int)(this.world.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.world.getTotalWorldTime();
        if (i > 1) {
            this.beamRenderScale -= i / 40.0f;
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
    
    @Nullable
    @Override
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
    private static Potion isBeaconEffect(final int p_184279_0_) {
        final Potion potion = Potion.getPotionById(p_184279_0_);
        return TileEntityBeacon.VALID_EFFECTS.contains(potion) ? potion : null;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.primaryEffect = isBeaconEffect(compound.getInteger("Primary"));
        this.secondaryEffect = isBeaconEffect(compound.getInteger("Secondary"));
        this.levels = compound.getInteger("Levels");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
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
    public boolean isEmpty() {
        return this.payment.isEmpty();
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index == 0) ? this.payment : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (index != 0 || this.payment.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (count >= this.payment.getCount()) {
            final ItemStack itemstack = this.payment;
            this.payment = ItemStack.EMPTY;
            return itemstack;
        }
        return this.payment.splitStack(count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (index == 0) {
            final ItemStack itemstack = this.payment;
            this.payment = ItemStack.EMPTY;
            return itemstack;
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
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
    
    public void setName(final String name) {
        this.customName = name;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return stack.getItem() == Items.EMERALD || stack.getItem() == Items.DIAMOND || stack.getItem() == Items.GOLD_INGOT || stack.getItem() == Items.IRON_INGOT;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:beacon";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerBeacon(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
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
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void setField(final int id, final int value) {
        switch (id) {
            case 0: {
                this.levels = value;
                break;
            }
            case 1: {
                this.primaryEffect = isBeaconEffect(value);
                break;
            }
            case 2: {
                this.secondaryEffect = isBeaconEffect(value);
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 3;
    }
    
    @Override
    public void clear() {
        this.payment = ItemStack.EMPTY;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.updateBeacon();
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return new int[0];
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return false;
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return false;
    }
    
    static {
        EFFECTS_LIST = new Potion[][] { { MobEffects.SPEED, MobEffects.HASTE }, { MobEffects.RESISTANCE, MobEffects.JUMP_BOOST }, { MobEffects.STRENGTH }, { MobEffects.REGENERATION } };
        VALID_EFFECTS = Sets.newHashSet();
        for (final Potion[] apotion : TileEntityBeacon.EFFECTS_LIST) {
            Collections.addAll(TileEntityBeacon.VALID_EFFECTS, apotion);
        }
    }
    
    public static class BeamSegment
    {
        private final float[] colors;
        private int height;
        
        public BeamSegment(final float[] colorsIn) {
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
