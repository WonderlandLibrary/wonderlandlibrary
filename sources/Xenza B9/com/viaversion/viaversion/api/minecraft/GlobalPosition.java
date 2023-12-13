// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public final class GlobalPosition extends Position
{
    private final String dimension;
    
    public GlobalPosition(final String dimension, final int x, final int y, final int z) {
        super(x, y, z);
        this.dimension = dimension;
    }
    
    public String dimension() {
        return this.dimension;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final GlobalPosition position = (GlobalPosition)o;
        return this.x == position.x && this.y == position.y && this.z == position.z && this.dimension.equals(position.dimension);
    }
    
    @Override
    public int hashCode() {
        int result = this.dimension.hashCode();
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }
    
    @Override
    public String toString() {
        return "GlobalPosition{dimension='" + this.dimension + '\'' + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
}
