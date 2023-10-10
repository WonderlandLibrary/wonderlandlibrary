// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class LongByReference extends ByReference
{
    public LongByReference() {
        this(0L);
    }
    
    public LongByReference(final long value) {
        super(8);
        this.setValue(value);
    }
    
    public void setValue(final long value) {
        this.getPointer().setLong(0L, value);
    }
    
    public long getValue() {
        return this.getPointer().getLong(0L);
    }
    
    @Override
    public String toString() {
        return String.format("long@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}
