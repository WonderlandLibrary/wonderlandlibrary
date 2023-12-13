// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import java.util.Arrays;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.potion.Potion;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityBeacon extends TileEntityLockable implements IUpdatePlayerListBox, IInventory
{
    public static final Potion[][] effectsList;
    private final List field_174909_f;
    private long field_146016_i;
    private float field_146014_j;
    private boolean isComplete;
    private int levels;
    private int primaryEffect;
    private int secondaryEffect;
    private ItemStack payment;
    private String field_146008_p;
    private static final String __OBFID = "CL_00000339";
    
    public TileEntityBeacon() {
        this.field_174909_f = Lists.newArrayList();
        this.levels = -1;
    }
    
    @Override
    public void update() {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L) {
            this.func_174908_m();
        }
    }
    
    public void func_174908_m() {
        this.func_146003_y();
        this.func_146000_x();
    }
    
    private void func_146000_x() {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
            final double var1 = this.levels * 10 + 10;
            byte var2 = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                var2 = 1;
            }
            final int var3 = this.pos.getX();
            final int var4 = this.pos.getY();
            final int var5 = this.pos.getZ();
            final AxisAlignedBB var6 = new AxisAlignedBB(var3, var4, var5, var3 + 1, var4 + 1, var5 + 1).expand(var1, var1, var1).addCoord(0.0, this.worldObj.getHeight(), 0.0);
            final List var7 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var6);
            for (final EntityPlayer var9 : var7) {
                var9.addPotionEffect(new PotionEffect(this.primaryEffect, 180, var2, true, true));
            }
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0) {
                for (final EntityPlayer var9 : var7) {
                    var9.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
                }
            }
        }
    }
    
    private void func_146003_y() {
        final int var1 = this.levels;
        final int var2 = this.pos.getX();
        final int var3 = this.pos.getY();
        final int var4 = this.pos.getZ();
        this.levels = 0;
        this.field_174909_f.clear();
        this.isComplete = true;
        BeamSegment var5 = new BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
        this.field_174909_f.add(var5);
        boolean var6 = true;
        for (int var7 = var3 + 1; var7 < this.worldObj.getActualHeight(); ++var7) {
            final BlockPos var8 = new BlockPos(var2, var7, var4);
            final IBlockState var9 = this.worldObj.getBlockState(var8);
            float[] var10;
            if (var9.getBlock() == Blocks.stained_glass) {
                var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlass.field_176547_a));
            }
            else if (var9.getBlock() != Blocks.stained_glass_pane) {
                if (var9.getBlock().getLightOpacity() >= 15) {
                    this.isComplete = false;
                    this.field_174909_f.clear();
                    break;
                }
                var5.func_177262_a();
                continue;
            }
            else {
                var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlassPane.field_176245_a));
            }
            if (!var6) {
                var10 = new float[] { (var5.func_177263_b()[0] + var10[0]) / 2.0f, (var5.func_177263_b()[1] + var10[1]) / 2.0f, (var5.func_177263_b()[2] + var10[2]) / 2.0f };
            }
            if (Arrays.equals(var10, var5.func_177263_b())) {
                var5.func_177262_a();
            }
            else {
                var5 = new BeamSegment(var10);
                this.field_174909_f.add(var5);
            }
            var6 = false;
        }
        if (this.isComplete) {
            for (int var7 = 1; var7 <= 4; this.levels = var7++) {
                final int var11 = var3 - var7;
                if (var11 < 0) {
                    break;
                }
                boolean var12 = true;
                for (int var13 = var2 - var7; var13 <= var2 + var7 && var12; ++var13) {
                    for (int var14 = var4 - var7; var14 <= var4 + var7; ++var14) {
                        final Block var15 = this.worldObj.getBlockState(new BlockPos(var13, var11, var14)).getBlock();
                        if (var15 != Blocks.emerald_block && var15 != Blocks.gold_block && var15 != Blocks.diamond_block && var15 != Blocks.iron_block) {
                            var12 = false;
                            break;
                        }
                    }
                }
                if (!var12) {
                    break;
                }
            }
            if (this.levels == 0) {
                this.isComplete = false;
            }
        }
        if (!this.worldObj.isRemote && this.levels == 4 && var1 < this.levels) {
            for (final EntityPlayer var17 : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(var2, var3, var4, var2, var3 - 4, var4).expand(10.0, 5.0, 10.0))) {
                var17.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }
    
    public List func_174907_n() {
        return this.field_174909_f;
    }
    
    public float shouldBeamRender() {
        if (!this.isComplete) {
            return 0.0f;
        }
        final int var1 = (int)(this.worldObj.getTotalWorldTime() - this.field_146016_i);
        this.field_146016_i = this.worldObj.getTotalWorldTime();
        if (var1 > 1) {
            this.field_146014_j -= var1 / 40.0f;
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
    public Packet getDescriptionPacket() {
        final NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 3, var1);
    }
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.primaryEffect = compound.getInteger("Primary");
        this.secondaryEffect = compound.getInteger("Secondary");
        this.levels = compound.getInteger("Levels");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
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
    public ItemStack getStackInSlot(final int slotIn) {
        return (slotIn == 0) ? this.payment : null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (index != 0 || this.payment == null) {
            return null;
        }
        if (count >= this.payment.stackSize) {
            final ItemStack var3 = this.payment;
            this.payment = null;
            return var3;
        }
        final ItemStack payment = this.payment;
        payment.stackSize -= count;
        return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (index == 0 && this.payment != null) {
            final ItemStack var2 = this.payment;
            this.payment = null;
            return var2;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        if (index == 0) {
            this.payment = stack;
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_146008_p : "container.beacon";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.field_146008_p != null && this.field_146008_p.length() > 0;
    }
    
    public void func_145999_a(final String p_145999_1_) {
        this.field_146008_p = p_145999_1_;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) == this && playerIn.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot;
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
                return this.primaryEffect;
            }
            case 2: {
                return this.secondaryEffect;
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
                this.primaryEffect = value;
                break;
            }
            case 2: {
                this.secondaryEffect = value;
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 3;
    }
    
    @Override
    public void clearInventory() {
        this.payment = null;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.func_174908_m();
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    static {
        effectsList = new Potion[][] { { Potion.moveSpeed, Potion.digSpeed }, { Potion.resistance, Potion.jump }, { Potion.damageBoost }, { Potion.regeneration } };
    }
    
    public static class BeamSegment
    {
        private final float[] field_177266_a;
        private int field_177265_b;
        private static final String __OBFID = "CL_00002042";
        
        public BeamSegment(final float[] p_i45669_1_) {
            this.field_177266_a = p_i45669_1_;
            this.field_177265_b = 1;
        }
        
        protected void func_177262_a() {
            ++this.field_177265_b;
        }
        
        public float[] func_177263_b() {
            return this.field_177266_a;
        }
        
        public int func_177264_c() {
            return this.field_177265_b;
        }
    }
}
