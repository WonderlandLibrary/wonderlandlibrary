/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Explosion {
    private final boolean isFlaming;
    private final boolean isSmoking;
    private final Random explosionRNG = new Random();
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap = Maps.newHashMap();

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, flaming, smoking);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking) {
        this.worldObj = worldIn;
        this.exploder = entityIn;
        this.explosionSize = size;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
        this.isFlaming = flaming;
        this.isSmoking = smoking;
    }

    public void doExplosionA() {
        HashSet<BlockPos> set = Sets.newHashSet();
        int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d0 = (float)j / 15.0f * 2.0f - 1.0f;
                    double d1 = (float)k / 15.0f * 2.0f - 1.0f;
                    double d2 = (float)l / 15.0f * 2.0f - 1.0f;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    double d4 = this.explosionX;
                    double d6 = this.explosionY;
                    double d8 = this.explosionZ;
                    float f1 = 0.3f;
                    for (float f = this.explosionSize * (0.7f + this.worldObj.rand.nextFloat() * 0.6f); f > 0.0f; f -= 0.22500001f) {
                        BlockPos blockpos = new BlockPos(d4, d6, d8);
                        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                        if (iblockstate.getMaterial() != Material.AIR) {
                            float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
                            f -= (f2 + 0.3f) * 0.3f;
                        }
                        if (f > 0.0f && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
                            set.add(blockpos);
                        }
                        d4 += d0 * (double)0.3f;
                        d6 += d1 * (double)0.3f;
                        d8 += d2 * (double)0.3f;
                    }
                }
            }
        }
        this.affectedBlockPositions.addAll(set);
        float f3 = this.explosionSize * 2.0f;
        int k1 = MathHelper.floor(this.explosionX - (double)f3 - 1.0);
        int l1 = MathHelper.floor(this.explosionX + (double)f3 + 1.0);
        int i2 = MathHelper.floor(this.explosionY - (double)f3 - 1.0);
        int i1 = MathHelper.floor(this.explosionY + (double)f3 + 1.0);
        int j2 = MathHelper.floor(this.explosionZ - (double)f3 - 1.0);
        int j1 = MathHelper.floor(this.explosionZ + (double)f3 + 1.0);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
        Vec3d vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);
        for (int k2 = 0; k2 < list.size(); ++k2) {
            EntityPlayer entityplayer;
            double d9;
            double d7;
            double d5;
            double d13;
            double d12;
            Entity entity = list.get(k2);
            if (entity.isImmuneToExplosions() || !((d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f3) <= 1.0) || (d13 = (double)MathHelper.sqrt((d5 = entity.posX - this.explosionX) * d5 + (d7 = entity.posY + (double)entity.getEyeHeight() - this.explosionY) * d7 + (d9 = entity.posZ - this.explosionZ) * d9)) == 0.0) continue;
            d5 /= d13;
            d7 /= d13;
            d9 /= d13;
            double d14 = this.worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
            double d10 = (1.0 - d12) * d14;
            entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (int)((d10 * d10 + d10) / 2.0 * 7.0 * (double)f3 + 1.0));
            double d11 = d10;
            if (entity instanceof EntityLivingBase) {
                d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
            }
            entity.motionX += d5 * d11;
            entity.motionY += d7 * d11;
            entity.motionZ += d9 * d11;
            if (!(entity instanceof EntityPlayer) || (entityplayer = (EntityPlayer)entity).isSpectator() || entityplayer.isCreative() && entityplayer.capabilities.isFlying) continue;
            this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
        }
    }

    public void doExplosionB(boolean spawnParticles) {
        this.worldObj.playSound(null, this.explosionX, this.explosionY, this.explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
        if (this.explosionSize >= 2.0f && this.isSmoking) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        } else {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.isSmoking) {
            for (BlockPos blockpos : this.affectedBlockPositions) {
                IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (spawnParticles) {
                    double d0 = (float)blockpos.getX() + this.worldObj.rand.nextFloat();
                    double d1 = (float)blockpos.getY() + this.worldObj.rand.nextFloat();
                    double d2 = (float)blockpos.getZ() + this.worldObj.rand.nextFloat();
                    double d3 = d0 - this.explosionX;
                    double d4 = d1 - this.explosionY;
                    double d5 = d2 - this.explosionZ;
                    double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5 / (d6 / (double)this.explosionSize + 0.1);
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX) / 2.0, (d1 + this.explosionY) / 2.0, (d2 + this.explosionZ) / 2.0, d3 *= (d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f)), d4 *= d7, d5 *= d7, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }
                if (iblockstate.getMaterial() == Material.AIR) continue;
                if (block.canDropFromExplosion(this)) {
                    block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0f / this.explosionSize, 0);
                }
                this.worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
            }
        }
        if (this.isFlaming) {
            for (BlockPos blockpos1 : this.affectedBlockPositions) {
                if (this.worldObj.getBlockState(blockpos1).getMaterial() != Material.AIR || !this.worldObj.getBlockState(blockpos1.down()).isFullBlock() || this.explosionRNG.nextInt(3) != 0) continue;
                this.worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
            }
        }
    }

    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    @Nullable
    public EntityLivingBase getExplosivePlacedBy() {
        if (this.exploder == null) {
            return null;
        }
        if (this.exploder instanceof EntityTNTPrimed) {
            return ((EntityTNTPrimed)this.exploder).getTntPlacedBy();
        }
        return this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null;
    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}

