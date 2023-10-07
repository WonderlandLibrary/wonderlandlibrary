// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

public abstract class EntityAIBase
{
    private int mutexBits;
    private static final String __OBFID = "CL_00001587";
    
    public abstract boolean shouldExecute();
    
    public boolean continueExecuting() {
        return this.shouldExecute();
    }
    
    public boolean isInterruptible() {
        return true;
    }
    
    public void startExecuting() {
    }
    
    public void resetTask() {
    }
    
    public void updateTask() {
    }
    
    public void setMutexBits(final int p_75248_1_) {
        this.mutexBits = p_75248_1_;
    }
    
    public int getMutexBits() {
        return this.mutexBits;
    }
}
