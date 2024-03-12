// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations;

public enum Direction
{
    FORWARDS, 
    BACKWARDS;
    
    public Direction opposite() {
        if (this == Direction.FORWARDS) {
            return Direction.BACKWARDS;
        }
        return Direction.FORWARDS;
    }
    
    public boolean forwards() {
        return this == Direction.FORWARDS;
    }
    
    public boolean backwards() {
        return this == Direction.BACKWARDS;
    }
}
