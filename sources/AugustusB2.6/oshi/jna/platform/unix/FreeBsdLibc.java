// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Structure;
import com.sun.jna.Native;

public interface FreeBsdLibc extends CLibrary
{
    public static final FreeBsdLibc INSTANCE = Native.load("libc", FreeBsdLibc.class);
    public static final int UTX_USERSIZE = 32;
    public static final int UTX_LINESIZE = 16;
    public static final int UTX_IDSIZE = 8;
    public static final int UTX_HOSTSIZE = 128;
    public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
    public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
    public static final int CPUSTATES = 5;
    public static final int CP_USER = 0;
    public static final int CP_NICE = 1;
    public static final int CP_SYS = 2;
    public static final int CP_INTR = 3;
    public static final int CP_IDLE = 4;
    
    FreeBsdUtmpx getutxent();
    
    @FieldOrder({ "ut_type", "ut_tv", "ut_id", "ut_pid", "ut_user", "ut_line", "ut_host", "ut_spare" })
    public static class FreeBsdUtmpx extends Structure
    {
        public short ut_type;
        public Timeval ut_tv;
        public byte[] ut_id;
        public int ut_pid;
        public byte[] ut_user;
        public byte[] ut_line;
        public byte[] ut_host;
        public byte[] ut_spare;
        
        public FreeBsdUtmpx() {
            this.ut_id = new byte[8];
            this.ut_user = new byte[32];
            this.ut_line = new byte[16];
            this.ut_host = new byte[128];
            this.ut_spare = new byte[64];
        }
    }
    
    @FieldOrder({ "tv_sec", "tv_usec" })
    public static class Timeval extends Structure
    {
        public long tv_sec;
        public long tv_usec;
    }
    
    @FieldOrder({ "cpu_ticks" })
    public static class CpTime extends Structure
    {
        public long[] cpu_ticks;
        
        public CpTime() {
            this.cpu_ticks = new long[5];
        }
    }
}
