// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.effect;

import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect
{
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private static final String __OBFID = "CL_00001666";
    
    public EntityLightningBolt(final World worldIn, final double p_i1703_2_, final double p_i1703_4_, final double p_i1703_6_) {
        super(worldIn);
        this.setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0f, 0.0f);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(new BlockPos(this), 10)) {
            final BlockPos var8 = new BlockPos(this);
            if (worldIn.getBlockState(var8).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, var8)) {
                worldIn.setBlockState(var8, Blocks.fire.getDefaultState());
            }
            for (int var9 = 0; var9 < 4; ++var9) {
                final BlockPos var10 = var8.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
                if (worldIn.getBlockState(var10).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, var10)) {
                    worldIn.setBlockState(var10, Blocks.fire.getDefaultState());
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.lightningState == 2) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0f, 0.8f + this.rand.nextFloat() * 0.2f);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0f, 0.5f + this.rand.nextFloat() * 0.2f);
        }
        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                final BlockPos var1 = new BlockPos(this);
                if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.isAreaLoaded(var1, 10) && this.worldObj.getBlockState(var1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, var1)) {
                    this.worldObj.setBlockState(var1, Blocks.fire.getDefaultState());
                }
            }
        }
        if (this.lightningState >= 0) {
            if (this.worldObj.isRemote) {
                this.worldObj.setLastLightningBolt(2);
            }
            else {
                final double var2 = 3.0;
                final List var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - var2, this.posY - var2, this.posZ - var2, this.posX + var2, this.posY + 6.0 + var2, this.posZ + var2));
                for (int var4 = 0; var4 < var3.size(); ++var4) {
                    final Entity var5 = var3.get(var4);
                    var5.onStruckByLightning(this);
                }
            }
        }
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
}
