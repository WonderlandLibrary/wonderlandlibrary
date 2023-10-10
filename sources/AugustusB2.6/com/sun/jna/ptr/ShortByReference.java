// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class ShortByReference extends ByReference
{
    public ShortByReference() {
        this((short)0);
    }
    
    public ShortByReference(final short value) {
        super(2);
        this.setValue(value);
    }
    
    public void setValue(final short value) {
        this.getPointer().setShort(0L, value);
    }
    
    public short getValue() {
        return this.getPointer().getShort(0L);
    }
    
    @Override
    public String toString() {
        return String.format("short@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}
