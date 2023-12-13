// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.entity.item.EntityMinecart;

public class EntityMinecartMobSpawner extends EntityMinecart
{
    private final MobSpawnerBaseLogic mobSpawnerLogic;
    private static final String __OBFID = "CL_00001678";
    
    public EntityMinecartMobSpawner(final World worldIn) {
        super(worldIn);
        this.mobSpawnerLogic = new MobSpawnerBaseLogic() {
            private static final String __OBFID = "CL_00001679";
            
            @Override
            public void func_98267_a(final int p_98267_1_) {
                EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte)p_98267_1_);
            }
            
            @Override
            public World getSpawnerWorld() {
                return EntityMinecartMobSpawner.this.worldObj;
            }
            
            @Override
            public BlockPos func_177221_b() {
                return new BlockPos(EntityMinecartMobSpawner.this);
            }
        };
    }
    
    public EntityMinecartMobSpawner(final World worldIn, final double p_i1726_2_, final double p_i1726_4_, final double p_i1726_6_) {
        super(worldIn, p_i1726_2_, p_i1726_4_, p_i1726_6_);
        this.mobSpawnerLogic = new MobSpawnerBaseLogic() {
            private static final String __OBFID = "CL_00001679";
            
            @Override
            public void func_98267_a(final int p_98267_1_) {
                EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte)p_98267_1_);
            }
            
            @Override
            public World getSpawnerWorld() {
                return EntityMinecartMobSpawner.this.worldObj;
            }
            
            @Override
            public BlockPos func_177221_b() {
                return new BlockPos(EntityMinecartMobSpawner.this);
            }
        };
    }
    
    @Override
    public EnumMinecartType func_180456_s() {
        return EnumMinecartType.SPAWNER;
    }
    
    @Override
    public IBlockState func_180457_u() {
        return Blocks.mob_spawner.getDefaultState();
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.mobSpawnerLogic.readFromNBT(tagCompund);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        this.mobSpawnerLogic.writeToNBT(tagCompound);
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        this.mobSpawnerLogic.setDelayToMin(p_70103_1_);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }
    
    public MobSpawnerBaseLogic func_98039_d() {
        return this.mobSpawnerLogic;
    }
}
