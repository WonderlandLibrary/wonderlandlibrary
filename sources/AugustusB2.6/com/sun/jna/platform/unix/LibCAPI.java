// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix;

import com.sun.jna.ptr.ByReference;
import com.sun.jna.Native;
import com.sun.jna.IntegerType;
import com.sun.jna.Pointer;

public interface LibCAPI extends Reboot, Resource
{
    public static final int HOST_NAME_MAX = 255;
    
    int getuid();
    
    int geteuid();
    
    int getgid();
    
    int getegid();
    
    int setuid(final int p0);
    
    int seteuid(final int p0);
    
    int setgid(final int p0);
    
    int setegid(final int p0);
    
    int gethostname(final byte[] p0, final int p1);
    
    int sethostname(final String p0, final int p1);
    
    int getdomainname(final byte[] p0, final int p1);
    
    int setdomainname(final String p0, final int p1);
    
    String getenv(final String p0);
    
    int setenv(final String p0, final String p1, final int p2);
    
    int unsetenv(final String p0);
    
    int getloadavg(final double[] p0, final int p1);
    
    int close(final int p0);
    
    int msync(final Pointer p0, final size_t p1, final int p2);
    
    int munmap(final Pointer p0, final size_t p1);
    
    public static class size_t extends IntegerType
    {
        public static final size_t ZERO;
        private static final long serialVersionUID = 1L;
        
        public size_t() {
            this(0L);
        }
        
        public size_t(final long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }
        
        static {
            ZERO = new size_t();
        }
        
        public static class ByReference extends com.sun.jna.ptr.ByReference
        {
            public ByReference() {
                this(0L);
            }
            
            public ByReference(final long value) {
                this(new size_t(value));
            }
            
            public ByReference(final size_t value) {
                super(Native.SIZE_T_SIZE);
                this.setValue(value);
            }
            
            public void setValue(final long value) {
                this.setValue(new size_t(value));
            }
            
            public void setValue(final size_t value) {
                if (Native.SIZE_T_SIZE > 4) {
                    this.getPointer().setLong(0L, value.longValue());
                }
                else {
                    this.getPointer().setInt(0L, value.intValue());
                }
            }
            
            public long longValue() {
                return (Native.SIZE_T_SIZE > 4) ? this.getPointer().getLong(0L) : this.getPointer().getInt(0L);
            }
            
            public size_t getValue() {
                return new size_t(this.longValue());
            }
        }
    }
    
    public static class ssize_t extends IntegerType
    {
        public static final ssize_t ZERO;
        private static final long serialVersionUID = 1L;
        
        public ssize_t() {
            this(0L);
        }
        
        public ssize_t(final long value) {
            super(Native.SIZE_T_SIZE, value, false);
        }
        
        static {
            ZERO = new ssize_t();
        }
    }
}
