// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Structure;
import com.sun.jna.Native;

public interface AixLibc extends CLibrary
{
    public static final AixLibc INSTANCE = Native.load("c", AixLibc.class);
    public static final int PRCLSZ = 8;
    public static final int PRFNSZ = 16;
    public static final int PRARGSZ = 80;
    
    @FieldOrder({ "pr_flag", "pr_flag2", "pr_nlwp", "pr__pad1", "pr_uid", "pr_euid", "pr_gid", "pr_egid", "pr_pid", "pr_ppid", "pr_pgid", "pr_sid", "pr_ttydev", "pr_addr", "pr_size", "pr_rssize", "pr_start", "pr_time", "pr_cid", "pr__pad2", "pr_argc", "pr_argv", "pr_envp", "pr_fname", "pr_psargs", "pr__pad", "pr_lwp" })
    public static class AixPsInfo extends Structure
    {
        public int pr_flag;
        public int pr_flag2;
        public int pr_nlwp;
        public int pr__pad1;
        public long pr_uid;
        public long pr_euid;
        public long pr_gid;
        public long pr_egid;
        public long pr_pid;
        public long pr_ppid;
        public long pr_pgid;
        public long pr_sid;
        public long pr_ttydev;
        public long pr_addr;
        public long pr_size;
        public long pr_rssize;
        public Timestruc pr_start;
        public Timestruc pr_time;
        public short pr_cid;
        public short pr__pad2;
        public int pr_argc;
        public long pr_argv;
        public long pr_envp;
        public byte[] pr_fname;
        public byte[] pr_psargs;
        public long[] pr__pad;
        public AIXLwpsInfo pr_lwp;
        
        public AixPsInfo() {
            this.pr_fname = new byte[16];
            this.pr_psargs = new byte[80];
            this.pr__pad = new long[8];
        }
        
        public AixPsInfo(final byte[] bytes) {
            this.pr_fname = new byte[16];
            this.pr_psargs = new byte[80];
            this.pr__pad = new long[8];
            final byte[] structBytes = new byte[this.size()];
            System.arraycopy(bytes, 0, structBytes, 0, structBytes.length);
            this.getPointer().write(0L, structBytes, 0, structBytes.length);
            this.read();
        }
    }
    
    @FieldOrder({ "pr_lwpid", "pr_addr", "pr_wchan", "pr_flag", "pr_wtype", "pr_state", "pr_sname", "pr_nice", "pr_pri", "pr_policy", "pr_clname", "pr_onpro", "pr_bindpro" })
    public static class AIXLwpsInfo extends Structure
    {
        public long pr_lwpid;
        public long pr_addr;
        public long pr_wchan;
        public int pr_flag;
        public byte pr_wtype;
        public byte pr_state;
        public byte pr_sname;
        public byte pr_nice;
        public int pr_pri;
        public int pr_policy;
        public byte[] pr_clname;
        public int pr_onpro;
        public int pr_bindpro;
        
        public AIXLwpsInfo() {
            this.pr_clname = new byte[8];
        }
        
        public AIXLwpsInfo(final byte[] bytes) {
            this.pr_clname = new byte[8];
            final byte[] structBytes = new byte[this.size()];
            System.arraycopy(bytes, 0, structBytes, 0, structBytes.length);
            this.getPointer().write(0L, structBytes, 0, structBytes.length);
            this.read();
        }
    }
    
    @FieldOrder({ "tv_sec", "tv_nsec", "pad" })
    public static class Timestruc extends Structure
    {
        public long tv_sec;
        public int tv_nsec;
        public int pad;
    }
}
