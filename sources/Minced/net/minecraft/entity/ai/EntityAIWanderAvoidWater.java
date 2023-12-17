// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityCreature;

public class EntityAIWanderAvoidWater extends EntityAIWander
{
    protected final float probability;
    
    public EntityAIWanderAvoidWater(final EntityCreature p_i47301_1_, final double p_i47301_2_) {
        this(p_i47301_1_, p_i47301_2_, 0.001f);
    }
    
    public EntityAIWanderAvoidWater(final EntityCreature p_i47302_1_, final double p_i47302_2_, final float p_i47302_4_) {
        super(p_i47302_1_, p_i47302_2_);
        this.probability = p_i47302_4_;
    }
    
    @Nullable
    @Override
    protected Vec3d getPosition() {
        if (this.entity.isInWater()) {
            final Vec3d vec3d = RandomPositionGenerator.getLandPos(this.entity, 15, 7);
            return (vec3d == null) ? super.getPosition() : vec3d;
        }
        return (this.entity.getRNG().nextFloat() >= this.probability) ? RandomPositionGenerator.getLandPos(this.entity, 10, 7) : super.getPosition();
    }
}
