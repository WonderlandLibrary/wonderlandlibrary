// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseHover extends PhaseBase
{
    private Vec3d targetLocation;
    
    public PhaseHover(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3d(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        }
    }
    
    @Override
    public boolean getIsStationary() {
        return true;
    }
    
    @Override
    public void initPhase() {
        this.targetLocation = null;
    }
    
    @Override
    public float getMaxRiseOrFall() {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    @Override
    public PhaseList<PhaseHover> getType() {
        return PhaseList.HOVER;
    }
}
