// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class CharMap extends Utils.Pointer
{
    public CharMap(final long n) {
        super(n);
    }
    
    public static int getCharmapIndex(final CharMap charMap) {
        return FreeType.FT_Get_Charmap_Index(charMap.getPointer());
    }
}
