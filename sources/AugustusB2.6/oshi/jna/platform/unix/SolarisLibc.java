// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Pointer;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.Native;

public interface SolarisLibc extends CLibrary
{
    public static final SolarisLibc INSTANCE = Native.load("c", SolarisLibc.class);
    public static final int UTX_USERSIZE = 32;
    public static final int UTX_LINESIZE = 32;
    public static final int UTX_IDSIZE = 4;
    public static final int UTX_HOSTSIZE = 257;
    public static final int PRCLSZ = 8;
    public static final int PRFNSZ = 16;
    public static final int PRLNSZ = 32;
    public static final int PRARGSZ = 80;
    
    SolarisUtmpx getutxent();
    
    @FieldOrder({ "ut_user", "ut_id", "ut_line", "ut_pid", "ut_type", "ut_tv", "ut_session", "ut_syslen", "ut_host" })
    public static class SolarisUtmpx extends Structure
    {
        public byte[] ut_user;
        public byte[] ut_id;
        public byte[] ut_line;
        public int ut_pid;
        public short ut_type;
        public Timeval ut_tv;
        public int ut_session;
        public short ut_syslen;
        public byte[] ut_host;
        
        public SolarisUtmpx() {
            this.ut_user = new byte[32];
            this.ut_id = new byte[4];
            this.ut_line = new byte[32];
            this.ut_host = new byte[257];
        }
    }
    
    @FieldOrder({ "e_termination", "e_exit" })
    public static class Exit_status extends Structure
    {
        public short e_termination;
        public short e_exit;
    }
    
    @FieldOrder({ "tv_sec", "tv_usec" })
    public static class Timeval extends Structure
    {
        public NativeLong tv_sec;
        public NativeLong tv_usec;
    }
    
    @FieldOrder({ "pr_flag", "pr_nlwp", "pr_pid", "pr_ppid", "pr_pgid", "pr_sid", "pr_uid", "pr_euid", "pr_gid", "pr_egid", "pr_addr", "pr_size", "pr_rssize", "pr_rssizepriv", "pr_ttydev", "pr_pctcpu", "pr_pctmem", "pr_start", "pr_time", "pr_ctime", "pr_fname", "pr_psargs", "pr_wstat", "pr_argc", "pr_argv", "pr_envp", "pr_dmodel", "pr_pad2", "pr_taskid", "pr_projid", "pr_nzomb", "pr_poolid", "pr_zoneid", "pr_contract", "pr_filler", "pr_lwp" })
    public static class SolarisPsInfo extends Structure
    {
        public int pr_flag;
        public int pr_nlwp;
        public int pr_pid;
        public int pr_ppid;
        public int pr_pgid;
        public int pr_sid;
        public int pr_uid;
        public int pr_euid;
        public int pr_gid;
        public int pr_egid;
        public Pointer pr_addr;
        public LibCAPI.size_t pr_size;
        public LibCAPI.size_t pr_rssize;
        public LibCAPI.size_t pr_rssizepriv;
        public NativeLong pr_ttydev;
        public short pr_pctcpu;
        public short pr_pctmem;
        public Timestruc pr_start;
        public Timestruc pr_time;
        public Timestruc pr_ctime;
        public byte[] pr_fname;
        public byte[] pr_psargs;
        public int pr_wstat;
        public int pr_argc;
        public Pointer pr_argv;
        public Pointer pr_envp;
        public byte pr_dmodel;
        public byte[] pr_pad2;
        public int pr_taskid;
        public int pr_projid;
        public int pr_nzomb;
        public int pr_poolid;
        public int pr_zoneid;
        public int pr_contract;
        public int[] pr_filler;
        public SolarisLwpsInfo pr_lwp;
        
        public SolarisPsInfo() {
            this.pr_fname = new byte[16];
            this.pr_psargs = new byte[80];
            this.pr_pad2 = new byte[3];
            this.pr_filler = new int[1];
        }
        
        public SolarisPsInfo(final byte[] bytes) {
            this.pr_fname = new byte[16];
            this.pr_psargs = new byte[80];
            this.pr_pad2 = new byte[3];
            this.pr_filler = new int[1];
            final byte[] structBytes = new byte[this.size()];
            System.arraycopy(bytes, 0, structBytes, 0, structBytes.length);
            this.getPointer().write(0L, structBytes, 0, structBytes.length);
            this.read();
        }
    }
    
    @FieldOrder({ "pr_flag", "pr_lwpid", "pr_addr", "pr_wchan", "pr_stype", "pr_state", "pr_sname", "pr_nice", "pr_syscall", "pr_oldpri", "pr_cpu", "pr_pri", "pr_pctcpu", "pr_pad", "pr_start", "pr_time", "pr_clname", "pr_oldname", "pr_onpro", "pr_bindpro", "pr_bindpset", "pr_lgrp", "pr_last_onproc", "pr_name" })
    public static class SolarisLwpsInfo extends Structure
    {
        public int pr_flag;
        public int pr_lwpid;
        public Pointer pr_addr;
        public Pointer pr_wchan;
        public byte pr_stype;
        public byte pr_state;
        public byte pr_sname;
        public byte pr_nice;
        public short pr_syscall;
        public byte pr_oldpri;
        public byte pr_cpu;
        public int pr_pri;
        public short pr_pctcpu;
        public short pr_pad;
        public Timestruc pr_start;
        public Timestruc pr_time;
        public byte[] pr_clname;
        public byte[] pr_oldname;
        public int pr_onpro;
        public int pr_bindpro;
        public int pr_bindpset;
        public int pr_lgrp;
        public long pr_last_onproc;
        public byte[] pr_name;
        
        public SolarisLwpsInfo() {
            this.pr_clname = new byte[8];
            this.pr_oldname = new byte[16];
            this.pr_name = new byte[32];
        }
        
        public SolarisLwpsInfo(final byte[] bytes) {
            this.pr_clname = new byte[8];
            this.pr_oldname = new byte[16];
            this.pr_name = new byte[32];
            final byte[] structBytes = new byte[this.size()];
            System.arraycopy(bytes, 0, structBytes, 0, structBytes.length);
            this.getPointer().write(0L, structBytes, 0, structBytes.length);
            this.read();
        }
    }
    
    @FieldOrder({ "pr_lwpid", "pr_count", "pr_tstamp", "pr_create", "pr_term", "pr_rtime", "pr_utime", "pr_stime", "pr_ttime", "pr_tftime", "pr_dftime", "pr_kftime", "pr_ltime", "pr_slptime", "pr_wtime", "pr_stoptime", "filltime", "pr_minf", "pr_majf", "pr_nswap", "pr_inblk", "pr_oublk", "pr_msnd", "pr_mrcv", "pr_sigs", "pr_vctx", "pr_ictx", "pr_sysc", "pr_ioch", "filler" })
    public static class SolarisPrUsage extends Structure
    {
        public int pr_lwpid;
        public int pr_count;
        public Timestruc pr_tstamp;
        public Timestruc pr_create;
        public Timestruc pr_term;
        public Timestruc pr_rtime;
        public Timestruc pr_utime;
        public Timestruc pr_stime;
        public Timestruc pr_ttime;
        public Timestruc pr_tftime;
        public Timestruc pr_dftime;
        public Timestruc pr_kftime;
        public Timestruc pr_ltime;
        public Timestruc pr_slptime;
        public Timestruc pr_wtime;
        public Timestruc pr_stoptime;
        public Timestruc[] filltime;
        public NativeLong pr_minf;
        public NativeLong pr_majf;
        public NativeLong pr_nswap;
        public NativeLong pr_inblk;
        public NativeLong pr_oublk;
        public NativeLong pr_msnd;
        public NativeLong pr_mrcv;
        public NativeLong pr_sigs;
        public NativeLong pr_vctx;
        public NativeLong pr_ictx;
        public NativeLong pr_sysc;
        public NativeLong pr_ioch;
        public NativeLong[] filler;
        
        public SolarisPrUsage() {
            this.filltime = new Timestruc[6];
            this.filler = new NativeLong[10];
        }
        
        public SolarisPrUsage(final byte[] bytes) {
            this.filltime = new Timestruc[6];
            this.filler = new NativeLong[10];
            final byte[] structBytes = new byte[this.size()];
            System.arraycopy(bytes, 0, structBytes, 0, structBytes.length);
            this.getPointer().write(0L, structBytes, 0, structBytes.length);
            this.read();
        }
    }
    
    @FieldOrder({ "tv_sec", "tv_nsec" })
    public static class Timestruc extends Structure
    {
        public NativeLong tv_sec;
        public NativeLong tv_nsec;
    }
}
