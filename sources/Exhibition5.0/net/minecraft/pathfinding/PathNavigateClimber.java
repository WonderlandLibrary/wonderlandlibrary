// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;

public class PathNavigateClimber extends PathNavigateGround
{
    private BlockPos field_179696_f;
    private static final String __OBFID = "CL_00002245";
    
    public PathNavigateClimber(final EntityLiving p_i45874_1_, final World worldIn) {
        super(p_i45874_1_, worldIn);
    }
    
    @Override
    public PathEntity func_179680_a(final BlockPos p_179680_1_) {
        this.field_179696_f = p_179680_1_;
        return super.func_179680_a(p_179680_1_);
    }
    
    @Override
    public PathEntity getPathToEntityLiving(final Entity p_75494_1_) {
        this.field_179696_f = new BlockPos(p_75494_1_);
        return super.getPathToEntityLiving(p_75494_1_);
    }
    
    @Override
    public boolean tryMoveToEntityLiving(final Entity p_75497_1_, final double p_75497_2_) {
        final PathEntity var4 = this.getPathToEntityLiving(p_75497_1_);
        if (var4 != null) {
            return this.setPath(var4, p_75497_2_);
        }
        this.field_179696_f = new BlockPos(p_75497_1_);
        this.speed = p_75497_2_;
        return true;
    }
    
    @Override
    public void onUpdateNavigation() {
        if (!this.noPath()) {
            super.onUpdateNavigation();
        }
        else if (this.field_179696_f != null) {
            final double var1 = this.theEntity.width * this.theEntity.width;
            if (this.theEntity.func_174831_c(this.field_179696_f) >= var1 && (this.theEntity.posY <= this.field_179696_f.getY() || this.theEntity.func_174831_c(new BlockPos(this.field_179696_f.getX(), MathHelper.floor_double(this.theEntity.posY), this.field_179696_f.getZ())) >= var1)) {
                this.theEntity.getMoveHelper().setMoveTo(this.field_179696_f.getX(), this.field_179696_f.getY(), this.field_179696_f.getZ(), this.speed);
            }
            else {
                this.field_179696_f = null;
            }
        }
    }
}
