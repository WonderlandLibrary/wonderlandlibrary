// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseDying extends PhaseBase
{
    private Vec3d targetLocation;
    private int time;
    
    public PhaseDying(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doClientRenderEffects() {
        if (this.time++ % 10 == 0) {
            final float f = (this.dragon.getRNG().nextFloat() - 0.5f) * 8.0f;
            final float f2 = (this.dragon.getRNG().nextFloat() - 0.5f) * 4.0f;
            final float f3 = (this.dragon.getRNG().nextFloat() - 0.5f) * 8.0f;
            this.dragon.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.dragon.posX + f, this.dragon.posY + 2.0 + f2, this.dragon.posZ + f3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public void doLocalUpdate() {
        ++this.time;
        if (this.targetLocation == null) {
            final BlockPos blockpos = this.dragon.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION);
            this.targetLocation = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
        final double d0 = this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        if (d0 >= 100.0 && d0 <= 22500.0 && !this.dragon.collidedHorizontally && !this.dragon.collidedVertically) {
            this.dragon.setHealth(1.0f);
        }
        else {
            this.dragon.setHealth(0.0f);
        }
    }
    
    @Override
    public void initPhase() {
        this.targetLocation = null;
        this.time = 0;
    }
    
    @Override
    public float getMaxRiseOrFall() {
        return 3.0f;
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    @Override
    public PhaseList<PhaseDying> getType() {
        return PhaseList.DYING;
    }
}
