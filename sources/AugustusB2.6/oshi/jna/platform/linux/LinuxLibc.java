// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.linux;

import com.sun.jna.Structure;
import com.sun.jna.Native;
import oshi.jna.platform.unix.CLibrary;
import com.sun.jna.platform.linux.LibC;

public interface LinuxLibc extends LibC, CLibrary
{
    public static final LinuxLibc INSTANCE = Native.load("c", LinuxLibc.class);
    
    LinuxUtmpx getutxent();
    
    @FieldOrder({ "ut_type", "ut_pid", "ut_line", "ut_id", "ut_user", "ut_host", "ut_exit", "ut_session", "ut_tv", "ut_addr_v6", "reserved" })
    public static class LinuxUtmpx extends Structure
    {
        public short ut_type;
        public int ut_pid;
        public byte[] ut_line;
        public byte[] ut_id;
        public byte[] ut_user;
        public byte[] ut_host;
        public Exit_status ut_exit;
        public int ut_session;
        public Ut_Tv ut_tv;
        public int[] ut_addr_v6;
        public byte[] reserved;
        
        public LinuxUtmpx() {
            this.ut_line = new byte[32];
            this.ut_id = new byte[4];
            this.ut_user = new byte[32];
            this.ut_host = new byte[256];
            this.ut_addr_v6 = new int[4];
            this.reserved = new byte[20];
        }
    }
    
    @FieldOrder({ "e_termination", "e_exit" })
    public static class Exit_status extends Structure
    {
        public short e_termination;
        public short e_exit;
    }
    
    @FieldOrder({ "tv_sec", "tv_usec" })
    public static class Ut_Tv extends Structure
    {
        public int tv_sec;
        public int tv_usec;
    }
}
