// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Union;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface ShTypes
{
    @FieldOrder({ "uType", "u" })
    public static class STRRET extends Structure
    {
        public static final int TYPE_WSTR = 0;
        public static final int TYPE_OFFSET = 1;
        public static final int TYPE_CSTR = 2;
        public int uType;
        public UNION u;
        
        public STRRET() {
        }
        
        public STRRET(final Pointer p) {
            super(p);
            this.read();
        }
        
        @Override
        public void read() {
            super.read();
            switch (this.uType) {
                default: {
                    this.u.setType("pOleStr");
                    break;
                }
                case 1: {
                    this.u.setType("uOffset");
                    break;
                }
                case 2: {
                    this.u.setType("cStr");
                    break;
                }
            }
            this.u.read();
        }
        
        public static class UNION extends Union
        {
            public WTypes.LPWSTR pOleStr;
            public int uOffset;
            public byte[] cStr;
            
            public UNION() {
                this.cStr = new byte[260];
            }
            
            public static class ByReference extends UNION implements Structure.ByReference
            {
            }
        }
    }
}
