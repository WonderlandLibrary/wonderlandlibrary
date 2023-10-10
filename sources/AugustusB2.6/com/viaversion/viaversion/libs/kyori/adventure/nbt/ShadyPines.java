// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

final class ShadyPines
{
    private ShadyPines() {
    }
    
    static int floor(final double dv) {
        final int iv = (int)dv;
        return (dv < iv) ? (iv - 1) : iv;
    }
    
    static int floor(final float fv) {
        final int iv = (int)fv;
        return (fv < iv) ? (iv - 1) : iv;
    }
}
