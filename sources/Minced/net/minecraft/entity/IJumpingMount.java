// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

public interface IJumpingMount
{
    void setJumpPower(final int p0);
    
    boolean canJump();
    
    void handleStartJump(final int p0);
    
    void handleStopJump();
}
