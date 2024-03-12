// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class IntArray
{
    private int[] array;
    private int position;
    private int limit;
    
    public IntArray(final int size) {
        this.array = null;
        this.position = 0;
        this.limit = 0;
        this.array = new int[size];
    }
    
    public void put(final int x) {
        this.array[this.position] = x;
        ++this.position;
        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }
    
    public void put(final int pos, final int x) {
        this.array[pos] = x;
        if (this.limit < pos) {
            this.limit = pos;
        }
    }
    
    public void position(final int pos) {
        this.position = pos;
    }
    
    public void put(final int[] ints) {
        for (int i = ints.length, j = 0; j < i; ++j) {
            this.array[this.position] = ints[j];
            ++this.position;
        }
        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }
    
    public int get(final int pos) {
        return this.array[pos];
    }
    
    public int[] getArray() {
        return this.array;
    }
    
    public void clear() {
        this.position = 0;
        this.limit = 0;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public int getPosition() {
        return this.position;
    }
}
