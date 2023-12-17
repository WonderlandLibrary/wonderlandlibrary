// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityWitherSkull extends EntityFireball
{
    private static final DataParameter<Boolean> INVULNERABLE;
    
    public EntityWitherSkull(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntityWitherSkull(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public static void registerFixesWitherSkull(final DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "WitherSkull");
    }
    
    @Override
    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73f : super.getMotionFactor();
    }
    
    public EntityWitherSkull(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    public boolean isBurning() {
        return false;
    }
    
    @Override
    public float getExplosionResistance(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn) {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        final Block block = blockStateIn.getBlock();
        if (this.isInvulnerable() && EntityWither.canDestroyBlock(block)) {
            f = Math.min(0.8f, f);
        }
        return f;
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0f)) {
                        if (result.entityHit.isEntityAlive()) {
                            this.applyEnchantments(this.shootingEntity, result.entityHit);
                        }
                        else {
                            this.shootingEntity.heal(5.0f);
                        }
                    }
                }
                else {
                    result.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0f);
                }
                if (result.entityHit instanceof EntityLivingBase) {
                    int i = 0;
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                        i = 10;
                    }
                    else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
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
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityWitherSkull.INVULNERABLE, false);
    }
    
    public boolean isInvulnerable() {
        return this.dataManager.get(EntityWitherSkull.INVULNERABLE);
    }
    
    public void setInvulnerable(final boolean invulnerable) {
        this.dataManager.set(EntityWitherSkull.INVULNERABLE, invulnerable);
    }
    
    @Override
    protected boolean isFireballFiery() {
        return false;
    }
    
    static {
        INVULNERABLE = EntityDataManager.createKey(EntityWitherSkull.class, DataSerializers.BOOLEAN);
    }
}
