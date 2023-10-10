// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

public enum PngFilterType
{
    ADAPTIVE(-1), 
    NONE(0), 
    SUB(1), 
    UP(2), 
    AVERAGE(3), 
    PAETH(4);
    
    private byte value;
    
    public final byte getValue() {
        return this.value;
    }
    
    private PngFilterType(final int i) {
        this.value = (byte)i;
    }
    
    public static PngFilterType forValue(final byte value) {
        PngFilterType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final PngFilterType type;
            if ((type = values[i]).value == value) {
                return type;
            }
        }
        return PngFilterType.NONE;
    }
    
    public static PngFilterType[] standardValues() {
        return new PngFilterType[] { PngFilterType.NONE, PngFilterType.SUB, PngFilterType.UP, PngFilterType.AVERAGE, PngFilterType.PAETH };
    }
}
