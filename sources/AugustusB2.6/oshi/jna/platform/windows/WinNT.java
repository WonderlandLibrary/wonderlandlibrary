// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.windows;

import com.sun.jna.Structure;

public interface WinNT extends com.sun.jna.platform.win32.WinNT
{
    @FieldOrder({ "TokenIsElevated" })
    public static class TOKEN_ELEVATION extends Structure
    {
        public int TokenIsElevated;
    }
}
