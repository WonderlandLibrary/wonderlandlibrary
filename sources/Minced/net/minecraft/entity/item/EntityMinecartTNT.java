// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart
{
    private int minecartTNTFuse;
    
    public EntityMinecartTNT(final World worldIn) {
        super(worldIn);
        this.minecartTNTFuse = -1;
    }
    
    public EntityMinecartTNT(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.minecartTNTFuse = -1;
    }
    
    public static void registerFixesMinecartTNT(final DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartTNT.class);
    }
    
    @Override
    public Type getType() {
        return Type.TNT;
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.TNT.getDefaultState();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.minecartTNTFuse > 0) {
            --this.minecartTNTFuse;
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        else if (this.minecartTNTFuse == 0) {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }
        if (this.collidedHorizontally) {
            final double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (d0 >= 0.009999999776482582) {
                this.explodeCart(d0);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        final Entity entity = source.getImmediateSource();
        if (entity instanceof EntityArrow) {
            final EntityArrow entityarrow = (EntityArrow)entity;
            if (entityarrow.isBurning()) {
                this.explodeCart(entityarrow.motionX * entityarrow.motionX + entityarrow.motionY * entityarrow.motionY + entityarrow.motionZ * entityarrow.motionZ);
            }
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void killMinecart(final DamageSource source) {
        final double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (!source.isFireDamage() && !source.isExplosion() && d0 < 0.009999999776482582) {
            super.killMinecart(source);
            if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
                this.entityDropItem(new ItemStack(Blocks.TNT, 1), 0.0f);
            }
        }
        else if (this.minecartTNTFuse < 0) {
            this.ignite();
            this.minecartTNTFuse = this.rand.nextInt(20) + this.rand.nextInt(20);
        }
    }
    
    protected void explodeCart(final double p_94103_1_) {
        if (!this.world.isRemote) {
            double d0 = Math.sqrt(p_94103_1_);
            if (d0 > 5.0) {
                d0 = 5.0;
            }
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0 + this.rand.nextDouble() * 1.5 * d0), true);
            this.setDead();
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        if (distance >= 3.0f) {
            final float f = distance / 10.0f;
            this.explodeCart(f * f);
        }
        super.fall(distance, damageMultiplier);
    }
    
    @Override
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
        if (receivingPower && this.minecartTNTFuse < 0) {
            this.ignite();
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 10) {
            this.ignite();
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public void ignite() {
        this.minecartTNTFuse = 80;
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)10);
            if (!this.isSilent()) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
    }
    
    public int getFuseTicks() {
        return this.minecartTNTFuse;
    }
    
    public boolean isIgnited() {
        return this.minecartTNTFuse > -1;
    }
    
    @Override
    public float getExplosionResistance(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn) {
        return (!this.isIgnited() || (!BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()))) ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 0.0f;
    }
    
    @Override
    public boolean canExplosionDestroyBlock(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn, final float p_174816_5_) {
        return (!this.isIgnited() || (!BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()))) && super.canExplosionDestroyBlock(explosionIn, worldIn, pos, blockStateIn, p_174816_5_);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("TNTFuse", 99)) {
            this.minecartTNTFuse = compound.getInteger("TNTFuse");
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
