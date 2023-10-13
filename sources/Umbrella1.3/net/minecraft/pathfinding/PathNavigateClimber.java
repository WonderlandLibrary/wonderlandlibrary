/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber
extends PathNavigateGround {
    private BlockPos field_179696_f;
    private static final String __OBFID = "CL_00002245";

    public PathNavigateClimber(EntityLiving p_i45874_1_, World worldIn) {
        super(p_i45874_1_, worldIn);
    }

    @Override
    public PathEntity func_179680_a(BlockPos p_179680_1_) {
        this.field_179696_f = p_179680_1_;
        return super.func_179680_a(p_179680_1_);
    }

    @Override
    public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
        this.field_179696_f = new BlockPos(p_75494_1_);
        return super.getPathToEntityLiving(p_75494_1_);
    }

    @Override
    public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
        PathEntity var4 = this.getPathToEntityLiving(p_75497_1_);
        if (var4 != null) {
            return this.setPath(var4, p_75497_2_);
        }
        this.field_179696_f = new BlockPos(p_75497_1_);
        this.speed = p_75497_2_;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onUpdateNavigation() {
        block4: {
            block5: {
                if (!this.noPath()) {
                    super.onUpdateNavigation();
                    return;
                }
                if (this.field_179696_f == null) return;
                double var1 = this.theEntity.width * this.theEntity.width;
                if (!(this.theEntity.func_174831_c(this.field_179696_f) >= var1)) break block4;
                if (this.theEntity.posY <= (double)this.field_179696_f.getY()) break block5;
                BlockPos blockPos = new BlockPos(this.field_179696_f.getX(), MathHelper.floor_double(this.theEntity.posY), this.field_179696_f.getZ());
                if (!(this.theEntity.func_174831_c(blockPos) >= var1)) break block4;
            }
            this.theEntity.getMoveHelper().setMoveTo(this.field_179696_f.getX(), this.field_179696_f.getY(), this.field_179696_f.getZ(), this.speed);
            return;
        }
        this.field_179696_f = null;
    }
}

