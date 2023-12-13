// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.animations;

public enum Direction
{
    FORWARDS("FORWARDS", 0, new int[2]), 
    BACKWARDS("BACKWARDS", 1, new int[2]), 
    UP("UP", 2, new int[] { 0, -1 }), 
    DOWN("DOWN", 3, new int[] { 0, 1 }), 
    LEFT("LEFT", 4, new int[] { -1, 0 }), 
    RIGHT("RIGHT", 5, new int[] { 1, 0 });
    
    private final int[] xy;
    
    private Direction(final String name, final int ordinal, final int[] xy) {
        this.xy = xy;
    }
    
    public int[] getXy() {
        return this.xy;
    }
}
