// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.utils;

public class Block
{
    private final int id;
    private final short data;
    
    public Block(final int id, final int data) {
        this.id = id;
        this.data = (short)data;
    }
    
    public Block(final int id) {
        this.id = id;
        this.data = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getData() {
        return this.data;
    }
    
    public Block withData(final int data) {
        return new Block(this.id, data);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Block block = (Block)o;
        return this.id == block.id && this.data == block.data;
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.data;
        return result;
    }
}
