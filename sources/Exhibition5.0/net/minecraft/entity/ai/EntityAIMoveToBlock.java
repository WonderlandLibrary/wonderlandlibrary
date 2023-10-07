// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityCreature;

public abstract class EntityAIMoveToBlock extends EntityAIBase
{
    private final EntityCreature field_179495_c;
    private final double field_179492_d;
    protected int field_179496_a;
    private int field_179493_e;
    private int field_179490_f;
    protected BlockPos field_179494_b;
    private boolean field_179491_g;
    private int field_179497_h;
    private static final String __OBFID = "CL_00002252";
    
    public EntityAIMoveToBlock(final EntityCreature p_i45888_1_, final double p_i45888_2_, final int p_i45888_4_) {
        this.field_179494_b = BlockPos.ORIGIN;
        this.field_179495_c = p_i45888_1_;
        this.field_179492_d = p_i45888_2_;
        this.field_179497_h = p_i45888_4_;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.field_179496_a > 0) {
            --this.field_179496_a;
            return false;
        }
        this.field_179496_a = 200 + this.field_179495_c.getRNG().nextInt(200);
        return this.func_179489_g();
    }
    
    @Override
    public boolean continueExecuting() {
        return this.field_179493_e >= -this.field_179490_f && this.field_179493_e <= 1200 && this.func_179488_a(this.field_179495_c.worldObj, this.field_179494_b);
    }
    
    @Override
    public void startExecuting() {
        this.field_179495_c.getNavigator().tryMoveToXYZ(this.field_179494_b.getX() + 0.5, this.field_179494_b.getY() + 1, this.field_179494_b.getZ() + 0.5, this.field_179492_d);
        this.field_179493_e = 0;
        this.field_179490_f = this.field_179495_c.getRNG().nextInt(this.field_179495_c.getRNG().nextInt(1200) + 1200) + 1200;
    }
    
    @Override
    public void resetTask() {
    }
    
    @Override
    public void updateTask() {
        if (this.field_179495_c.func_174831_c(this.field_179494_b.offsetUp()) > 1.0) {
            this.field_179491_g = false;
            ++this.field_179493_e;
            if (this.field_179493_e % 40 == 0) {
                this.field_179495_c.getNavigator().tryMoveToXYZ(this.field_179494_b.getX() + 0.5, this.field_179494_b.getY() + 1, this.field_179494_b.getZ() + 0.5, this.field_179492_d);
            }
        }
        else {
            this.field_179491_g = true;
            --this.field_179493_e;
        }
    }
    
    protected boolean func_179487_f() {
        return this.field_179491_g;
    }
    
    private boolean func_179489_g() {
        final int var1 = this.field_179497_h;
        final boolean var2 = true;
        final BlockPos var3 = new BlockPos(this.field_179495_c);
        for (int var4 = 0; var4 <= 1; var4 = ((var4 > 0) ? (-var4) : (1 - var4))) {
            for (int var5 = 0; var5 < var1; ++var5) {
                for (int var6 = 0; var6 <= var5; var6 = ((var6 > 0) ? (-var6) : (1 - var6))) {
                    for (int var7 = (var6 < var5 && var6 > -var5) ? var5 : 0; var7 <= var5; var7 = ((var7 > 0) ? (-var7) : (1 - var7))) {
                        final BlockPos var8 = var3.add(var6, var4 - 1, var7);
                        if (this.field_179495_c.func_180485_d(var8) && this.func_179488_a(this.field_179495_c.worldObj, var8)) {
                            this.field_179494_b = var8;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    protected abstract boolean func_179488_a(final World p0, final BlockPos p1);
}
