package net.minecraft.util;

public class MovementInput
{
    public float moveStrafe;
    public float moveForward;
    public boolean jump;
    public boolean sneak;
    
    public void updatePlayerMoveState() {
    }
    
    public float getStrafe() {
        return this.moveStrafe;
    }
    
    public void setStrafe(final float moveStrafe) {
        this.moveStrafe = moveStrafe;
    }
    
    public float getForward() {
        return this.moveForward;
    }
    
    public void setForward(final float moveForward) {
        this.moveForward = moveForward;
    }
    
    public boolean isJumping() {
        return this.jump;
    }
    
    public void setJumping(final boolean jump) {
        this.jump = jump;
    }
    
    public boolean isSneaking() {
        return this.sneak;
    }
    
    public void setSneaking(final boolean sneak) {
        this.sneak = sneak;
    }
}
