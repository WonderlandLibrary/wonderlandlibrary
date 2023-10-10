// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Native;

public interface OpenBsdLibc extends CLibrary
{
    public static final OpenBsdLibc INSTANCE = Native.load(null, OpenBsdLibc.class);
    public static final int CTL_KERN = 1;
    public static final int CTL_VM = 1;
    public static final int CTL_HW = 6;
    public static final int CTL_MACHDEP = 7;
    public static final int CTL_VFS = 10;
    public static final int KERN_OSTYPE = 1;
    public static final int KERN_OSRELEASE = 2;
    public static final int KERN_OSREV = 3;
    public static final int KERN_VERSION = 4;
    public static final int KERN_MAXVNODES = 5;
    public static final int KERN_MAXPROC = 6;
    public static final int KERN_ARGMAX = 8;
    public static final int KERN_CPTIME = 40;
    public static final int KERN_CPTIME2 = 71;
    public static final int VM_UVMEXP = 4;
    public static final int HW_MACHINE = 1;
    public static final int HW_MODEL = 2;
    public static final int HW_PAGESIZE = 7;
    public static final int HW_CPUSPEED = 12;
    public static final int HW_NCPUFOUND = 21;
    public static final int HW_SMT = 24;
    public static final int HW_NCPUONLINE = 25;
    public static final int VFS_GENERIC = 0;
    public static final int VFS_BCACHESTAT = 3;
    public static final int CPUSTATES = 5;
    public static final int CP_USER = 0;
    public static final int CP_NICE = 1;
    public static final int CP_SYS = 2;
    public static final int CP_INTR = 3;
    public static final int CP_IDLE = 4;
    public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
    public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
    
    @FieldOrder({ "numbufs", "numbufpages", "numdirtypages", "numcleanpages", "pendingwrites", "pendingreads", "numwrites", "numreads", "cachehits", "busymapped", "dmapages", "highpages", "delwribufs", "kvaslots", "kvaslots_avail", "highflips", "highflops", "dmaflips" })
    public static class Bcachestats extends Structure
    {
        public long numbufs;
        public long numbufpages;
        public long numdirtypages;
        public long numcleanpages;
        public long pendingwrites;
        public long pendingreads;
        public long numwrites;
        public long numreads;
        public long cachehits;
        public long busymapped;
        public long dmapages;
        public long highpages;
        public long delwribufs;
        public long kvaslots;
        public long kvaslots_avail;
        public long highflips;
        public long highflops;
        public long dmaflips;
        
        public Bcachestats(final Pointer p) {
            super(p);
            this.read();
        }
    }
    
    @FieldOrder({ "tv_sec", "tv_usec" })
    public static class Timeval extends Structure
    {
        public long tv_sec;
        public long tv_usec;
    }
}
