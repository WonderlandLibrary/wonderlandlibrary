/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull
extends EntityFireball {
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityWitherSkull.class, DataSerializers.BOOLEAN);

    public EntityWitherSkull(World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }

    public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }

    public static void registerFixesWitherSkull(DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "WitherSkull");
    }

    @Override
    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73f : super.getMotionFactor();
    }

    public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        Block block = blockStateIn.getBlock();
        if (this.isInvulnerable() && EntityWither.canDestroyBlock(block)) {
            f = Math.min(0.8f, f);
        }
        return f;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0f)) {
                        if (result.entityHit.isEntityAlive()) {
                            this.applyEnchantments(this.shootingEntity, result.entityHit);
                        } else {
                            this.shootingEntity.heal(5.0f);
                        }
                    }
                } else {
                    result.entityHit.attackEntityFrom(DamageSource.magic, 5.0f);
                }
                if (result.entityHit instanceof EntityLivingBase) {
                    int i = 0;
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                        i = 10;
                    } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        i = 40;
                    }
                    if (i > 0) {
                        ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * i, 1));
                    }
                }
            }
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1.0f, false, this.world.getGameRules().getBoolean("mobGriefing"));
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(INVULNERABLE, false);
    }

    public boolean isInvulnerable() {
        return this.dataManager.get(INVULNERABLE);
    }

    public void setInvulnerable(boolean invulnerable) {
        this.dataManager.set(INVULNERABLE, invulnerable);
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }
}

