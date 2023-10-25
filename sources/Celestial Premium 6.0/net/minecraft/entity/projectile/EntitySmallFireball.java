/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireball
extends EntityFireball {
    public EntitySmallFireball(World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }

    public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }

    public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }

    public static void registerFixesSmallFireball(DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "SmallFireball");
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                boolean flag;
                if (!result.entityHit.isImmuneToFire() && (flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0f))) {
                    this.applyEnchantments(this.shootingEntity, result.entityHit);
                    result.entityHit.setFire(5);
                }
            } else {
                BlockPos blockpos;
                boolean flag1 = true;
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
                    flag1 = this.world.getGameRules().getBoolean("mobGriefing");
                }
                if (flag1 && this.world.isAirBlock(blockpos = result.getBlockPos().offset(result.sideHit))) {
                    this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                }
            }
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
}

