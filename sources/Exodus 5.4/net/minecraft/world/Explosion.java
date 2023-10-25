/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Explosion {
    private final Random explosionRNG = new Random();
    private final double explosionZ;
    private final boolean isFlaming;
    private final World worldObj;
    private final double explosionX;
    private final Entity exploder;
    private final boolean isSmoking;
    private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
    private final Map<EntityPlayer, Vec3> playerKnockbackMap = Maps.newHashMap();
    private final float explosionSize;
    private final double explosionY;

    public void func_180342_d() {
        this.affectedBlockPositions.clear();
    }

    public void doExplosionA() {
        int n;
        int n2;
        HashSet hashSet = Sets.newHashSet();
        int n3 = 16;
        int n4 = 0;
        while (n4 < 16) {
            n2 = 0;
            while (n2 < 16) {
                n = 0;
                while (n < 16) {
                    if (n4 == 0 || n4 == 15 || n2 == 0 || n2 == 15 || n == 0 || n == 15) {
                        double d = (float)n4 / 15.0f * 2.0f - 1.0f;
                        double d2 = (float)n2 / 15.0f * 2.0f - 1.0f;
                        double d3 = (float)n / 15.0f * 2.0f - 1.0f;
                        double d4 = Math.sqrt(d * d + d2 * d2 + d3 * d3);
                        d /= d4;
                        d2 /= d4;
                        d3 /= d4;
                        float f = this.explosionSize * (0.7f + this.worldObj.rand.nextFloat() * 0.6f);
                        double d5 = this.explosionX;
                        double d6 = this.explosionY;
                        double d7 = this.explosionZ;
                        float f2 = 0.3f;
                        while (f > 0.0f) {
                            BlockPos blockPos = new BlockPos(d5, d6, d7);
                            IBlockState iBlockState = this.worldObj.getBlockState(blockPos);
                            if (iBlockState.getBlock().getMaterial() != Material.air) {
                                float f3 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockPos, iBlockState) : iBlockState.getBlock().getExplosionResistance(null);
                                f -= (f3 + 0.3f) * 0.3f;
                            }
                            if (f > 0.0f && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockPos, iBlockState, f))) {
                                hashSet.add(blockPos);
                            }
                            d5 += d * (double)0.3f;
                            d6 += d2 * (double)0.3f;
                            d7 += d3 * (double)0.3f;
                            f -= 0.22500001f;
                        }
                    }
                    ++n;
                }
                ++n2;
            }
            ++n4;
        }
        this.affectedBlockPositions.addAll(hashSet);
        float f = this.explosionSize * 2.0f;
        n2 = MathHelper.floor_double(this.explosionX - (double)f - 1.0);
        n = MathHelper.floor_double(this.explosionX + (double)f + 1.0);
        int n5 = MathHelper.floor_double(this.explosionY - (double)f - 1.0);
        int n6 = MathHelper.floor_double(this.explosionY + (double)f + 1.0);
        int n7 = MathHelper.floor_double(this.explosionZ - (double)f - 1.0);
        int n8 = MathHelper.floor_double(this.explosionZ + (double)f + 1.0);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(n2, n5, n7, n, n6, n8));
        Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);
        int n9 = 0;
        while (n9 < list.size()) {
            double d;
            double d8;
            double d9;
            double d10;
            double d11;
            Entity entity = list.get(n9);
            if (!entity.isImmuneToExplosions() && (d11 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f) <= 1.0 && (d10 = (double)MathHelper.sqrt_double((d9 = entity.posX - this.explosionX) * d9 + (d8 = entity.posY + (double)entity.getEyeHeight() - this.explosionY) * d8 + (d = entity.posZ - this.explosionZ) * d)) != 0.0) {
                d9 /= d10;
                d8 /= d10;
                d /= d10;
                double d12 = this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                double d13 = (1.0 - d11) * d12;
                entity.attackEntityFrom(DamageSource.setExplosionSource(this), (int)((d13 * d13 + d13) / 2.0 * 8.0 * (double)f + 1.0));
                double d14 = EnchantmentProtection.func_92092_a(entity, d13);
                entity.motionX += d9 * d14;
                entity.motionY += d8 * d14;
                entity.motionZ += d * d14;
                if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.disableDamage) {
                    this.playerKnockbackMap.put((EntityPlayer)entity, new Vec3(d9 * d13, d8 * d13, d * d13));
                }
            }
            ++n9;
        }
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    public Map<EntityPlayer, Vec3> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    public Explosion(World world, Entity entity, double d, double d2, double d3, float f, List<BlockPos> list) {
        this(world, entity, d, d2, d3, f, false, true, list);
    }

    public void doExplosionB(boolean bl) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
        if (this.explosionSize >= 2.0f && this.isSmoking) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        } else {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.isSmoking) {
            for (BlockPos blockPos : this.affectedBlockPositions) {
                Block block = this.worldObj.getBlockState(blockPos).getBlock();
                if (bl) {
                    double d = (float)blockPos.getX() + this.worldObj.rand.nextFloat();
                    double d2 = (float)blockPos.getY() + this.worldObj.rand.nextFloat();
                    double d3 = (float)blockPos.getZ() + this.worldObj.rand.nextFloat();
                    double d4 = d - this.explosionX;
                    double d5 = d2 - this.explosionY;
                    double d6 = d3 - this.explosionZ;
                    double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
                    d4 /= d7;
                    d5 /= d7;
                    d6 /= d7;
                    double d8 = 0.5 / (d7 / (double)this.explosionSize + 0.1);
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d + this.explosionX * 1.0) / 2.0, (d2 + this.explosionY * 1.0) / 2.0, (d3 + this.explosionZ * 1.0) / 2.0, d4 *= (d8 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f)), d5 *= d8, d6 *= d8, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d2, d3, d4, d5, d6, new int[0]);
                }
                if (block.getMaterial() == Material.air) continue;
                if (block.canDropFromExplosion(this)) {
                    block.dropBlockAsItemWithChance(this.worldObj, blockPos, this.worldObj.getBlockState(blockPos), 1.0f / this.explosionSize, 0);
                }
                this.worldObj.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
                block.onBlockDestroyedByExplosion(this.worldObj, blockPos, this);
            }
        }
        if (this.isFlaming) {
            for (BlockPos blockPos : this.affectedBlockPositions) {
                if (this.worldObj.getBlockState(blockPos).getBlock().getMaterial() != Material.air || !this.worldObj.getBlockState(blockPos.down()).getBlock().isFullBlock() || this.explosionRNG.nextInt(3) != 0) continue;
                this.worldObj.setBlockState(blockPos, Blocks.fire.getDefaultState());
            }
        }
    }

    public Explosion(World world, Entity entity, double d, double d2, double d3, float f, boolean bl, boolean bl2) {
        this.worldObj = world;
        this.exploder = entity;
        this.explosionSize = f;
        this.explosionX = d;
        this.explosionY = d2;
        this.explosionZ = d3;
        this.isFlaming = bl;
        this.isSmoking = bl2;
    }

    public EntityLivingBase getExplosivePlacedBy() {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
    }

    public Explosion(World world, Entity entity, double d, double d2, double d3, float f, boolean bl, boolean bl2, List<BlockPos> list) {
        this(world, entity, d, d2, d3, f, bl, bl2);
        this.affectedBlockPositions.addAll(list);
    }
}

