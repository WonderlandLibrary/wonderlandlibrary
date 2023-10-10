// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.mac;

import com.sun.jna.Union;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import oshi.jna.platform.unix.CLibrary;

public interface SystemB extends com.sun.jna.platform.mac.SystemB, CLibrary
{
    public static final SystemB INSTANCE = Native.load("System", SystemB.class);
    public static final int PROC_PIDLISTFDS = 1;
    public static final int PROX_FDTYPE_SOCKET = 2;
    public static final int PROC_PIDFDSOCKETINFO = 3;
    public static final int TSI_T_NTIMERS = 4;
    public static final int SOCKINFO_IN = 1;
    public static final int SOCKINFO_TCP = 2;
    public static final int UTX_USERSIZE = 256;
    public static final int UTX_LINESIZE = 32;
    public static final int UTX_IDSIZE = 4;
    public static final int UTX_HOSTSIZE = 256;
    public static final int AF_INET = 2;
    public static final int AF_INET6 = 30;
    
    MacUtmpx getutxent();
    
    int proc_pidfdinfo(final int p0, final int p1, final int p2, final Structure p3, final int p4);
    
    @FieldOrder({ "ut_user", "ut_id", "ut_line", "ut_pid", "ut_type", "ut_tv", "ut_host", "ut_pad" })
    public static class MacUtmpx extends Structure
    {
        public byte[] ut_user;
        public byte[] ut_id;
        public byte[] ut_line;
        public int ut_pid;
        public short ut_type;
        public Timeval ut_tv;
        public byte[] ut_host;
        public byte[] ut_pad;
        
        public MacUtmpx() {
            this.ut_user = new byte[256];
            this.ut_id = new byte[4];
            this.ut_line = new byte[32];
            this.ut_host = new byte[256];
            this.ut_pad = new byte[16];
        }
    }
    
    @FieldOrder({ "proc_fd", "proc_fdtype" })
    public static class ProcFdInfo extends Structure
    {
        public int proc_fd;
        public int proc_fdtype;
    }
    
    @FieldOrder({ "insi_fport", "insi_lport", "insi_gencnt", "insi_flags", "insi_flow", "insi_vflag", "insi_ip_ttl", "rfu_1", "insi_faddr", "insi_laddr", "insi_v4", "insi_v6" })
    public static class InSockInfo extends Structure
    {
        public int insi_fport;
        public int insi_lport;
        public long insi_gencnt;
        public int insi_flags;
        public int insi_flow;
        public byte insi_vflag;
        public byte insi_ip_ttl;
        public int rfu_1;
        public int[] insi_faddr;
        public int[] insi_laddr;
        public byte insi_v4;
        public byte[] insi_v6;
        
        public InSockInfo() {
            this.insi_faddr = new int[4];
            this.insi_laddr = new int[4];
            this.insi_v6 = new byte[9];
        }
    }
    
    @FieldOrder({ "tcpsi_ini", "tcpsi_state", "tcpsi_timer", "tcpsi_mss", "tcpsi_flags", "rfu_1", "tcpsi_tp" })
    public static class TcpSockInfo extends Structure
    {
        public InSockInfo tcpsi_ini;
        public int tcpsi_state;
        public int[] tcpsi_timer;
        public int tcpsi_mss;
        public int tcpsi_flags;
        public int rfu_1;
        public long tcpsi_tp;
        
        public TcpSockInfo() {
            this.tcpsi_timer = new int[4];
        }
    }
    
    @FieldOrder({ "soi_stat", "soi_so", "soi_pcb", "soi_type", "soi_protocol", "soi_family", "soi_options", "soi_linger", "soi_state", "soi_qlen", "soi_incqlen", "soi_qlimit", "soi_timeo", "soi_error", "soi_oobmark", "soi_rcv", "soi_snd", "soi_kind", "rfu_1", "soi_proto" })
    public static class SocketInfo extends Structure
    {
        public long[] soi_stat;
        public long soi_so;
        public long soi_pcb;
        public int soi_type;
        public int soi_protocol;
        public int soi_family;
        public short soi_options;
        public short soi_linger;
        public short soi_state;
        public short soi_qlen;
        public short soi_incqlen;
        public short soi_qlimit;
        public short soi_timeo;
        public short soi_error;
        public int soi_oobmark;
        public int[] soi_rcv;
        public int[] soi_snd;
        public int soi_kind;
        public int rfu_1;
        public Pri soi_proto;
        
        public SocketInfo() {
            this.soi_stat = new long[17];
            this.soi_rcv = new int[6];
            this.soi_snd = new int[6];
        }
    }
    
    @FieldOrder({ "fi_openflags", "fi_status", "fi_offset", "fi_type", "fi_guardflags" })
    public static class ProcFileInfo extends Structure
    {
        public int fi_openflags;
        public int fi_status;
        public long fi_offset;
        public int fi_type;
        public int fi_guardflags;
    }
    
    @FieldOrder({ "pfi", "psi" })
    public static class SocketFdInfo extends Structure
    {
        public ProcFileInfo pfi;
        public SocketInfo psi;
    }
    
    public static class Pri extends Union
    {
        public InSockInfo pri_in;
        public TcpSockInfo pri_tcp;
        public byte[] max_size;
        
        public Pri() {
            this.max_size = new byte[524];
        }
    }
}
