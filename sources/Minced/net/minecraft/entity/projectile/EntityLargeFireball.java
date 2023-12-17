// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball
{
    public int explosionPower;
    
    public EntityLargeFireball(final World worldIn) {
        super(worldIn);
        this.explosionPower = 1;
    }
    
    public EntityLargeFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.explosionPower = 1;
    }
    
    public EntityLargeFireball(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.explosionPower = 1;
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0f);
                this.applyEnchantments(this.shootingEntity, result.entityHit);
            }
            final boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
    }
    
    public static void registerFixesLargeFireball(final DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "Fireball");
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ExplosionPower", this.explosionPower);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("ExplosionPower", 99)) {
            this.explosionPower = compound.getInteger("ExplosionPower");
        }
    }
}
