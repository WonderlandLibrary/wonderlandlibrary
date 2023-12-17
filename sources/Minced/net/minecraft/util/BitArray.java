// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;

public class BitArray
{
    private final long[] longArray;
    private final int bitsPerEntry;
    private final long maxEntryValue;
    private final int arraySize;
    
    public BitArray(final int bitsPerEntryIn, final int arraySizeIn) {
        Validate.inclusiveBetween(1L, 32L, (long)bitsPerEntryIn);
        this.arraySize = arraySizeIn;
        this.bitsPerEntry = bitsPerEntryIn;
        this.maxEntryValue = (1L << bitsPerEntryIn) - 1L;
        this.longArray = new long[MathHelper.roundUp(arraySizeIn * bitsPerEntryIn, 64) / 64];
    }
    
    public void setAt(final int index, final int value) {
        Validate.inclusiveBetween(0L, (long)(this.arraySize - 1), (long)index);
        Validate.inclusiveBetween(0L, this.maxEntryValue, (long)value);
        final int i = index * this.bitsPerEntry;
        final int j = i / 64;
        final int k = ((index + 1) * this.bitsPerEntry - 1) / 64;
        final int l = i % 64;
        this.longArray[j] = ((this.longArray[j] & ~(this.maxEntryValue << l)) | ((long)value & this.maxEntryValue) << l);
        if (j != k) {
            final int i2 = 64 - l;
            final int j2 = this.bitsPerEntry - i2;
            this.longArray[k] = (this.longArray[k] >>> j2 << j2 | ((long)value & this.maxEntryValue) >> i2);
        }
    }
    
    public int getAt(final int index) {
        Validate.inclusiveBetween(0L, (long)(this.arraySize - 1), (long)index);
        final int i = index * this.bitsPerEntry;
        final int j = i / 64;
        final int k = ((index + 1) * this.bitsPerEntry - 1) / 64;
        final int l = i % 64;
        if (j == k) {
            return (int)(this.longArray[j] >>> l & this.maxEntryValue);
        }
        final int i2 = 64 - l;
        return (int)((this.longArray[j] >>> l | this.longArray[k] << i2) & this.maxEntryValue);
    }
    
    public long[] getBackingLongArray() {
        return this.longArray;
    }
    
    public int size() {
        return this.arraySize;
    }
}
