/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
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
implements IInventory,
ITickable {
    private int primaryEffect;
    private String customName;
    private long beamRenderCounter;
    private float field_146014_j;
    private final List<BeamSegment> beamSegments = Lists.newArrayList();
    private int levels = -1;
    private boolean isComplete;
    private int secondaryEffect;
    public static final Potion[][] effectsList = new Potion[][]{{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
    private ItemStack payment;

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        if (n == 0) {
            this.payment = itemStack;
        }
    }

    /*
     * Unable to fully structure code
     */
    private void updateSegmentColors() {
        var1_1 = this.levels;
        var2_2 = this.pos.getX();
        var3_3 = this.pos.getY();
        var4_4 = this.pos.getZ();
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        var5_5 = new BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
        this.beamSegments.add(var5_5);
        var6_6 = true;
        var7_7 = new BlockPos.MutableBlockPos();
        var8_8 = var3_3 + 1;
        while (var8_8 < 256) {
            block15: {
                var9_10 = this.worldObj.getBlockState(var7_7.func_181079_c(var2_2, var8_8, var4_4));
                if (var9_10.getBlock() != Blocks.stained_glass) break block15;
                var10_12 = EntitySheep.func_175513_a(var9_10.getValue(BlockStainedGlass.COLOR));
                ** GOTO lbl28
            }
            if (var9_10.getBlock() != Blocks.stained_glass_pane) {
                if (var9_10.getBlock().getLightOpacity() >= 15 && var9_10.getBlock() != Blocks.bedrock) {
                    this.isComplete = false;
                    this.beamSegments.clear();
                    break;
                }
                var5_5.incrementHeight();
            } else {
                var10_12 = EntitySheep.func_175513_a(var9_10.getValue(BlockStainedGlassPane.COLOR));
lbl28:
                // 2 sources

                if (!var6_6) {
                    var10_12 = new float[]{(var5_5.getColors()[0] + var10_12[0]) / 2.0f, (var5_5.getColors()[1] + var10_12[1]) / 2.0f, (var5_5.getColors()[2] + var10_12[2]) / 2.0f};
                }
                if (Arrays.equals(var10_12, var5_5.getColors())) {
                    var5_5.incrementHeight();
                } else {
                    var5_5 = new BeamSegment(var10_12);
                    this.beamSegments.add(var5_5);
                }
                var6_6 = false;
            }
            ++var8_8;
        }
        if (this.isComplete) {
            var8_8 = 1;
            while (var8_8 <= 4) {
                var9_11 = var3_3 - var8_8;
                if (var9_11 < 0) break;
                var10_14 = true;
                var11_15 = var2_2 - var8_8;
                while (var11_15 <= var2_2 + var8_8 && var10_14) {
                    var12_16 = var4_4 - var8_8;
                    while (var12_16 <= var4_4 + var8_8) {
                        var13_17 = this.worldObj.getBlockState(new BlockPos(var11_15, var9_11, var12_16)).getBlock();
                        if (var13_17 != Blocks.emerald_block && var13_17 != Blocks.gold_block && var13_17 != Blocks.diamond_block && var13_17 != Blocks.iron_block) {
                            var10_14 = false;
                            break;
                        }
                        ++var12_16;
                    }
                    ++var11_15;
                }
                if (!var10_14) break;
                this.levels = var8_8++;
            }
            if (this.levels == 0) {
                this.isComplete = false;
            }
        }
        if (!this.worldObj.isRemote && this.levels == 4 && var1_1 < this.levels) {
            for (EntityPlayer var8_9 : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(var2_2, var3_3, var4_4, var2_2, var3_3 - 4, var4_4).expand(10.0, 5.0, 10.0))) {
                var8_9.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }

    private void addEffectsToPlayers() {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
            double d = this.levels * 10 + 10;
            int n = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                n = 1;
            }
            int n2 = this.pos.getX();
            int n3 = this.pos.getY();
            int n4 = this.pos.getZ();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(n2, n3, n4, n2 + 1, n3 + 1, n4 + 1).expand(d, d, d).addCoord(0.0, this.worldObj.getHeight(), 0.0);
            List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
            for (EntityPlayer entityPlayer : list) {
                entityPlayer.addPotionEffect(new PotionEffect(this.primaryEffect, 180, n, true, true));
            }
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0) {
                for (EntityPlayer entityPlayer : list) {
                    entityPlayer.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
                }
            }
        }
    }

    @Override
    public String getGuiID() {
        return "minecraft:beacon";
    }

    private int func_183001_h(int n) {
        if (n >= 0 && n < Potion.potionTypes.length && Potion.potionTypes[n] != null) {
            Potion potion = Potion.potionTypes[n];
            return potion != Potion.moveSpeed && potion != Potion.digSpeed && potion != Potion.resistance && potion != Potion.jump && potion != Potion.damageBoost && potion != Potion.regeneration ? 0 : n;
        }
        return 0;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (n == 0 && this.payment != null) {
            if (n2 >= this.payment.stackSize) {
                ItemStack itemStack = this.payment;
                this.payment = null;
                return itemStack;
            }
            this.payment.stackSize -= n2;
            return new ItemStack(this.payment.getItem(), n2, this.payment.getMetadata());
        }
        return null;
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public int getField(int n) {
        switch (n) {
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

    public List<BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerBeacon(inventoryPlayer, this);
    }

    @Override
    public void update() {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L) {
            this.updateBeacon();
        }
    }

    public void setName(String string) {
        this.customName = string;
    }

    @Override
    public boolean receiveClientEvent(int n, int n2) {
        if (n == 1) {
            this.updateBeacon();
            return true;
        }
        return super.receiveClientEvent(n, n2);
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.primaryEffect = this.func_183001_h(nBTTagCompound.getInteger("Primary"));
        this.secondaryEffect = this.func_183001_h(nBTTagCompound.getInteger("Secondary"));
        this.levels = nBTTagCompound.getInteger("Levels");
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return itemStack.getItem() == Items.emerald || itemStack.getItem() == Items.diamond || itemStack.getItem() == Items.gold_ingot || itemStack.getItem() == Items.iron_ingot;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    public float shouldBeamRender() {
        if (!this.isComplete) {
            return 0.0f;
        }
        int n = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.worldObj.getTotalWorldTime();
        if (n > 1) {
            this.field_146014_j -= (float)n / 40.0f;
            if (this.field_146014_j < 0.0f) {
                this.field_146014_j = 0.0f;
            }
        }
        this.field_146014_j += 0.025f;
        if (this.field_146014_j > 1.0f) {
            this.field_146014_j = 1.0f;
        }
        return this.field_146014_j;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.beacon";
    }

    public void updateBeacon() {
        this.updateSegmentColors();
        this.addEffectsToPlayers();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.writeToNBT(nBTTagCompound);
        return new S35PacketUpdateTileEntity(this.pos, 3, nBTTagCompound);
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (n == 0 && this.payment != null) {
            ItemStack itemStack = this.payment;
            this.payment = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void clear() {
        this.payment = null;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return n == 0 ? this.payment : null;
    }

    @Override
    public void setField(int n, int n2) {
        switch (n) {
            case 0: {
                this.levels = n2;
                break;
            }
            case 1: {
                this.primaryEffect = this.func_183001_h(n2);
                break;
            }
            case 2: {
                this.secondaryEffect = this.func_183001_h(n2);
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Primary", this.primaryEffect);
        nBTTagCompound.setInteger("Secondary", this.secondaryEffect);
        nBTTagCompound.setInteger("Levels", this.levels);
    }

    public static class BeamSegment {
        private int height;
        private final float[] colors;

        protected void incrementHeight() {
            ++this.height;
        }

        public int getHeight() {
            return this.height;
        }

        public BeamSegment(float[] fArray) {
            this.colors = fArray;
            this.height = 1;
        }

        public float[] getColors() {
            return this.colors;
        }
    }
}

