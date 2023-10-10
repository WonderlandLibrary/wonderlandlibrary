// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix.aix;

import com.sun.jna.Union;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.Library;

public interface Perfstat extends Library
{
    public static final Perfstat INSTANCE = SharedObjectLoader.getPerfstatInstance();
    public static final int IDENTIFIER_LENGTH = 64;
    
    int perfstat_cpu_total(final perfstat_id_t p0, final perfstat_cpu_total_t p1, final int p2, final int p3);
    
    int perfstat_cpu(final perfstat_id_t p0, final perfstat_cpu_t[] p1, final int p2, final int p3);
    
    int perfstat_memory_total(final perfstat_id_t p0, final perfstat_memory_total_t p1, final int p2, final int p3);
    
    int perfstat_process(final perfstat_id_t p0, final perfstat_process_t[] p1, final int p2, final int p3);
    
    int perfstat_disk(final perfstat_id_t p0, final perfstat_disk_t[] p1, final int p2, final int p3);
    
    int perfstat_partition_config(final perfstat_id_t p0, final perfstat_partition_config_t p1, final int p2, final int p3);
    
    int perfstat_netinterface(final perfstat_id_t p0, final perfstat_netinterface_t[] p1, final int p2, final int p3);
    
    int perfstat_protocol(final perfstat_id_t p0, final perfstat_protocol_t[] p1, final int p2, final int p3);
    
    @FieldOrder({ "name" })
    public static class perfstat_id_t extends Structure
    {
        public byte[] name;
        
        public perfstat_id_t() {
            this.name = new byte[64];
        }
    }
    
    @FieldOrder({ "ncpus", "ncpus_cfg", "description", "processorHZ", "user", "sys", "idle", "wait", "pswitch", "syscall", "sysread", "syswrite", "sysfork", "sysexec", "readch", "writech", "devintrs", "softintrs", "lbolt", "loadavg", "runque", "swpque", "bread", "bwrite", "lread", "lwrite", "phread", "phwrite", "runocc", "swpocc", "iget", "namei", "dirblk", "msg", "sema", "rcvint", "xmtint", "mdmint", "tty_rawinch", "tty_caninch", "tty_rawoutch", "ksched", "koverf", "kexit", "rbread", "rcread", "rbwrt", "rcwrt", "traps", "ncpus_high", "puser", "psys", "pidle", "pwait", "decrintrs", "mpcrintrs", "mpcsintrs", "phantintrs", "idle_donated_purr", "idle_donated_spurr", "busy_donated_purr", "busy_donated_spurr", "idle_stolen_purr", "idle_stolen_spurr", "busy_stolen_purr", "busy_stolen_spurr", "iowait", "physio", "twait", "hpi", "hpit", "puser_spurr", "psys_spurr", "pidle_spurr", "pwait_spurr", "spurrflag", "version", "tb_last", "purr_coalescing", "spurr_coalescing" })
    public static class perfstat_cpu_total_t extends Structure
    {
        public int ncpus;
        public int ncpus_cfg;
        public byte[] description;
        public long processorHZ;
        public long user;
        public long sys;
        public long idle;
        public long wait;
        public long pswitch;
        public long syscall;
        public long sysread;
        public long syswrite;
        public long sysfork;
        public long sysexec;
        public long readch;
        public long writech;
        public long devintrs;
        public long softintrs;
        public NativeLong lbolt;
        public long[] loadavg;
        public long runque;
        public long swpque;
        public long bread;
        public long bwrite;
        public long lread;
        public long lwrite;
        public long phread;
        public long phwrite;
        public long runocc;
        public long swpocc;
        public long iget;
        public long namei;
        public long dirblk;
        public long msg;
        public long sema;
        public long rcvint;
        public long xmtint;
        public long mdmint;
        public long tty_rawinch;
        public long tty_caninch;
        public long tty_rawoutch;
        public long ksched;
        public long koverf;
        public long kexit;
        public long rbread;
        public long rcread;
        public long rbwrt;
        public long rcwrt;
        public long traps;
        public int ncpus_high;
        public long puser;
        public long psys;
        public long pidle;
        public long pwait;
        public long decrintrs;
        public long mpcrintrs;
        public long mpcsintrs;
        public long phantintrs;
        public long idle_donated_purr;
        public long idle_donated_spurr;
        public long busy_donated_purr;
        public long busy_donated_spurr;
        public long idle_stolen_purr;
        public long idle_stolen_spurr;
        public long busy_stolen_purr;
        public long busy_stolen_spurr;
        public short iowait;
        public short physio;
        public long twait;
        public long hpi;
        public long hpit;
        public long puser_spurr;
        public long psys_spurr;
        public long pidle_spurr;
        public long pwait_spurr;
        public int spurrflag;
        public long version;
        public long tb_last;
        public long purr_coalescing;
        public long spurr_coalescing;
        
        public perfstat_cpu_total_t() {
            this.description = new byte[64];
            this.loadavg = new long[3];
        }
    }
    
    @FieldOrder({ "name", "user", "sys", "idle", "wait", "pswitch", "syscall", "sysread", "syswrite", "sysfork", "sysexec", "readch", "writech", "bread", "bwrite", "lread", "lwrite", "phread", "phwrite", "iget", "namei", "dirblk", "msg", "sema", "minfaults", "majfaults", "puser", "psys", "pidle", "pwait", "redisp_sd0", "redisp_sd1", "redisp_sd2", "redisp_sd3", "redisp_sd4", "redisp_sd5", "migration_push", "migration_S3grq", "migration_S3pul", "invol_cswitch", "vol_cswitch", "runque", "bound", "decrintrs", "mpcrintrs", "mpcsintrs", "devintrs", "softintrs", "phantintrs", "idle_donated_purr", "idle_donated_spurr", "busy_donated_purr", "busy_donated_spurr", "idle_stolen_purr", "idle_stolen_spurr", "busy_stolen_purr", "busy_stolen_spurr", "hpi", "hpit", "puser_spurr", "psys_spurr", "pidle_spurr", "pwait_spurr", "spurrflag", "localdispatch", "neardispatch", "fardispatch", "cswitches", "version", "tb_last" })
    public static class perfstat_cpu_t extends Structure
    {
        public byte[] name;
        public long user;
        public long sys;
        public long idle;
        public long wait;
        public long pswitch;
        public long syscall;
        public long sysread;
        public long syswrite;
        public long sysfork;
        public long sysexec;
        public long readch;
        public long writech;
        public long bread;
        public long bwrite;
        public long lread;
        public long lwrite;
        public long phread;
        public long phwrite;
        public long iget;
        public long namei;
        public long dirblk;
        public long msg;
        public long sema;
        public long minfaults;
        public long majfaults;
        public long puser;
        public long psys;
        public long pidle;
        public long pwait;
        public long redisp_sd0;
        public long redisp_sd1;
        public long redisp_sd2;
        public long redisp_sd3;
        public long redisp_sd4;
        public long redisp_sd5;
        public long migration_push;
        public long migration_S3grq;
        public long migration_S3pul;
        public long invol_cswitch;
        public long vol_cswitch;
        public long runque;
        public long bound;
        public long decrintrs;
        public long mpcrintrs;
        public long mpcsintrs;
        public long devintrs;
        public long softintrs;
        public long phantintrs;
        public long idle_donated_purr;
        public long idle_donated_spurr;
        public long busy_donated_purr;
        public long busy_donated_spurr;
        public long idle_stolen_purr;
        public long idle_stolen_spurr;
        public long busy_stolen_purr;
        public long busy_stolen_spurr;
        public long hpi;
        public long hpit;
        public long puser_spurr;
        public long psys_spurr;
        public long pidle_spurr;
        public long pwait_spurr;
        public int spurrflag;
        public long localdispatch;
        public long neardispatch;
        public long fardispatch;
        public long cswitches;
        public long version;
        public long tb_last;
        
        public perfstat_cpu_t() {
            this.name = new byte[64];
        }
    }
    
    @FieldOrder({ "virt_total", "real_total", "real_free", "real_pinned", "real_inuse", "pgbad", "pgexct", "pgins", "pgouts", "pgspins", "pgspouts", "scans", "cycles", "pgsteals", "numperm", "pgsp_total", "pgsp_free", "pgsp_rsvd", "real_system", "real_user", "real_process", "virt_active", "iome", "iomu", "iohwm", "pmem", "comprsd_total", "comprsd_wseg_pgs", "cpgins", "cpgouts", "true_size", "expanded_memory", "comprsd_wseg_size", "target_cpool_size", "max_cpool_size", "min_ucpool_size", "cpool_size", "ucpool_size", "cpool_inuse", "ucpool_inuse", "version", "real_avail", "bytes_coalesced", "bytes_coalesced_mempool" })
    public static class perfstat_memory_total_t extends Structure
    {
        public long virt_total;
        public long real_total;
        public long real_free;
        public long real_pinned;
        public long real_inuse;
        public long pgbad;
        public long pgexct;
        public long pgins;
        public long pgouts;
        public long pgspins;
        public long pgspouts;
        public long scans;
        public long cycles;
        public long pgsteals;
        public long numperm;
        public long pgsp_total;
        public long pgsp_free;
        public long pgsp_rsvd;
        public long real_system;
        public long real_user;
        public long real_process;
        public long virt_active;
        public long iome;
        public long iomu;
        public long iohwm;
        public long pmem;
        public long comprsd_total;
        public long comprsd_wseg_pgs;
        public long cpgins;
        public long cpgouts;
        public long true_size;
        public long expanded_memory;
        public long comprsd_wseg_size;
        public long target_cpool_size;
        public long max_cpool_size;
        public long min_ucpool_size;
        public long cpool_size;
        public long ucpool_size;
        public long cpool_inuse;
        public long ucpool_inuse;
        public long version;
        public long real_avail;
        public long bytes_coalesced;
        public long bytes_coalesced_mempool;
    }
    
    @FieldOrder({ "version", "pid", "proc_name", "proc_priority", "num_threads", "proc_uid", "proc_classid", "proc_size", "proc_real_mem_data", "proc_real_mem_text", "proc_virt_mem_data", "proc_virt_mem_text", "shared_lib_data_size", "heap_size", "real_inuse", "virt_inuse", "pinned", "pgsp_inuse", "filepages", "real_inuse_map", "virt_inuse_map", "pinned_inuse_map", "ucpu_time", "scpu_time", "last_timebase", "inBytes", "outBytes", "inOps", "outOps" })
    public static class perfstat_process_t extends Structure
    {
        public long version;
        public long pid;
        public byte[] proc_name;
        public int proc_priority;
        public long num_threads;
        public long proc_uid;
        public long proc_classid;
        public long proc_size;
        public long proc_real_mem_data;
        public long proc_real_mem_text;
        public long proc_virt_mem_data;
        public long proc_virt_mem_text;
        public long shared_lib_data_size;
        public long heap_size;
        public long real_inuse;
        public long virt_inuse;
        public long pinned;
        public long pgsp_inuse;
        public long filepages;
        public long real_inuse_map;
        public long virt_inuse_map;
        public long pinned_inuse_map;
        public double ucpu_time;
        public double scpu_time;
        public long last_timebase;
        public long inBytes;
        public long outBytes;
        public long inOps;
        public long outOps;
        
        public perfstat_process_t() {
            this.proc_name = new byte[64];
        }
    }
    
    @FieldOrder({ "name", "description", "vgname", "size", "free", "bsize", "xrate", "xfers", "wblks", "rblks", "qdepth", "time", "adapter", "paths_count", "q_full", "rserv", "rtimeout", "rfailed", "min_rserv", "max_rserv", "wserv", "wtimeout", "wfailed", "min_wserv", "max_wserv", "wq_depth", "wq_sampled", "wq_time", "wq_min_time", "wq_max_time", "q_sampled", "wpar_id", "version", "dk_type" })
    public static class perfstat_disk_t extends Structure
    {
        public byte[] name;
        public byte[] description;
        public byte[] vgname;
        public long size;
        public long free;
        public long bsize;
        public long xrate;
        public long xfers;
        public long wblks;
        public long rblks;
        public long qdepth;
        public long time;
        public byte[] adapter;
        public int paths_count;
        public long q_full;
        public long rserv;
        public long rtimeout;
        public long rfailed;
        public long min_rserv;
        public long max_rserv;
        public long wserv;
        public long wtimeout;
        public long wfailed;
        public long min_wserv;
        public long max_wserv;
        public long wq_depth;
        public long wq_sampled;
        public long wq_time;
        public long wq_min_time;
        public long wq_max_time;
        public long q_sampled;
        public short wpar_id;
        public long version;
        public int dk_type;
        
        public perfstat_disk_t() {
            this.name = new byte[64];
            this.description = new byte[64];
            this.vgname = new byte[64];
            this.adapter = new byte[64];
        }
    }
    
    @FieldOrder({ "online", "max", "min", "desired" })
    public static class perfstat_value_t extends Structure
    {
        public long online;
        public long max;
        public long min;
        public long desired;
    }
    
    @FieldOrder({ "version", "partitionname", "nodename", "conf", "partitionnum", "groupid", "processorFamily", "processorModel", "machineID", "processorMHz", "numProcessors", "OSName", "OSVersion", "OSBuild", "lcpus", "smtthreads", "drives", "nw_adapters", "cpucap", "cpucap_weightage", "entitled_proc_capacity", "vcpus", "processor_poolid", "activecpusinpool", "cpupool_weightage", "sharedpcpu", "maxpoolcap", "entpoolcap", "mem", "mem_weightage", "totiomement", "mempoolid", "hyperpgsize", "exp_mem", "targetmemexpfactor", "targetmemexpsize" })
    public static class perfstat_partition_config_t extends Structure
    {
        public long version;
        public byte[] partitionname;
        public byte[] nodename;
        public int conf;
        public int partitionnum;
        public int groupid;
        public byte[] processorFamily;
        public byte[] processorModel;
        public byte[] machineID;
        public double processorMHz;
        public perfstat_value_t numProcessors;
        public byte[] OSName;
        public byte[] OSVersion;
        public byte[] OSBuild;
        public int lcpus;
        public int smtthreads;
        public int drives;
        public int nw_adapters;
        public perfstat_value_t cpucap;
        public int cpucap_weightage;
        public int entitled_proc_capacity;
        public perfstat_value_t vcpus;
        public int processor_poolid;
        public int activecpusinpool;
        public int cpupool_weightage;
        public int sharedpcpu;
        public int maxpoolcap;
        public int entpoolcap;
        public perfstat_value_t mem;
        public int mem_weightage;
        public long totiomement;
        public int mempoolid;
        public long hyperpgsize;
        public perfstat_value_t exp_mem;
        public long targetmemexpfactor;
        public long targetmemexpsize;
        
        public perfstat_partition_config_t() {
            this.partitionname = new byte[64];
            this.nodename = new byte[64];
            this.processorFamily = new byte[64];
            this.processorModel = new byte[64];
            this.machineID = new byte[64];
            this.OSName = new byte[64];
            this.OSVersion = new byte[64];
            this.OSBuild = new byte[64];
        }
    }
    
    @FieldOrder({ "name", "description", "type", "mtu", "ipackets", "ibytes", "ierrors", "opackets", "obytes", "oerrors", "collisions", "bitrate", "xmitdrops", "version", "if_iqdrops", "if_arpdrops" })
    public static class perfstat_netinterface_t extends Structure
    {
        public byte[] name;
        public byte[] description;
        public byte type;
        public long mtu;
        public long ipackets;
        public long ibytes;
        public long ierrors;
        public long opackets;
        public long obytes;
        public long oerrors;
        public long collisions;
        public long bitrate;
        public long xmitdrops;
        public long version;
        public long if_iqdrops;
        public long if_arpdrops;
        
        public perfstat_netinterface_t() {
            this.name = new byte[64];
            this.description = new byte[64];
        }
    }
    
    @FieldOrder({ "name", "u", "version" })
    public static class perfstat_protocol_t extends Structure
    {
        public byte[] name;
        public AnonymousUnionPayload u;
        public long version;
        
        public perfstat_protocol_t() {
            this.name = new byte[64];
        }
        
        @Override
        public void read() {
            super.read();
            final String type = Native.toString(this.name);
            if (!type.isEmpty()) {
                this.u.setType(type);
            }
            this.u.read();
        }
        
        public static class AnonymousUnionPayload extends Union
        {
            public AnonymousStructIP ip;
            public AnonymousStructIPv6 ipv6;
            public AnonymousStructICMP icmp;
            public AnonymousStructICMPv6 icmpv6;
            public AnonymousStructUDP udp;
            public AnonymousStructTCP tcp;
            public AnonymousStructRPC rpc;
            public AnonymousStructNFS nfs;
            public AnonymousStructNFSv2 nfsv2;
            public AnonymousStructNFSv3 nfsv3;
            public AnonymousStructNFSv4 nfsv4;
        }
        
        @FieldOrder({ "ipackets", "ierrors", "iqueueoverflow", "opackets", "oerrors" })
        public static class AnonymousStructIP extends Structure
        {
            public long ipackets;
            public long ierrors;
            public long iqueueoverflow;
            public long opackets;
            public long oerrors;
        }
        
        @FieldOrder({ "ipackets", "ierrors", "iqueueoverflow", "opackets", "oerrors" })
        public static class AnonymousStructIPv6 extends Structure
        {
            public long ipackets;
            public long ierrors;
            public long iqueueoverflow;
            public long opackets;
            public long oerrors;
        }
        
        @FieldOrder({ "received", "sent", "errors" })
        public static class AnonymousStructICMP extends Structure
        {
            public long received;
            public long sent;
            public long errors;
        }
        
        @FieldOrder({ "received", "sent", "errors" })
        public static class AnonymousStructICMPv6 extends Structure
        {
            public long received;
            public long sent;
            public long errors;
        }
        
        @FieldOrder({ "ipackets", "ierrors", "opackets", "no_socket" })
        public static class AnonymousStructUDP extends Structure
        {
            public long ipackets;
            public long ierrors;
            public long opackets;
            public long no_socket;
        }
        
        @FieldOrder({ "ipackets", "ierrors", "opackets", "initiated", "accepted", "established", "dropped" })
        public static class AnonymousStructTCP extends Structure
        {
            public long ipackets;
            public long ierrors;
            public long opackets;
            public long initiated;
            public long accepted;
            public long established;
            public long dropped;
        }
        
        @FieldOrder({ "client", "server" })
        public static class AnonymousStructRPC extends Structure
        {
            public AnonymousStructRPCclient client;
            public AnonymousStructRPCserver server;
        }
        
        @FieldOrder({ "stream", "dgram" })
        public static class AnonymousStructRPCclient extends Structure
        {
            public AnonymousStructRPCclientstream stream;
            public AnonymousStructRPCclientdgram dgram;
        }
        
        @FieldOrder({ "calls", "badcalls", "badxids", "timeouts", "newcreds", "badverfs", "timers", "nomem", "cantconn", "interrupts" })
        public static class AnonymousStructRPCclientstream extends Structure
        {
            public long calls;
            public long badcalls;
            public long badxids;
            public long timeouts;
            public long newcreds;
            public long badverfs;
            public long timers;
            public long nomem;
            public long cantconn;
            public long interrupts;
        }
        
        @FieldOrder({ "calls", "badcalls", "retrans", "badxids", "timeouts", "newcreds", "badverfs", "timers", "nomem", "cantsend" })
        public static class AnonymousStructRPCclientdgram extends Structure
        {
            public long calls;
            public long badcalls;
            public long retrans;
            public long badxids;
            public long timeouts;
            public long newcreds;
            public long badverfs;
            public long timers;
            public long nomem;
            public long cantsend;
        }
        
        @FieldOrder({ "stream", "dgram" })
        public static class AnonymousStructRPCserver extends Structure
        {
            public AnonymousStructRPCserverstream stream;
            public AnonymousStructRPCserverdgram dgram;
        }
        
        @FieldOrder({ "calls", "badcalls", "nullrecv", "badlen", "xdrcall", "dupchecks", "dupreqs" })
        public static class AnonymousStructRPCserverstream extends Structure
        {
            public long calls;
            public long badcalls;
            public long nullrecv;
            public long badlen;
            public long xdrcall;
            public long dupchecks;
            public long dupreqs;
        }
        
        @FieldOrder({ "calls", "badcalls", "nullrecv", "badlen", "xdrcall", "dupchecks", "dupreqs" })
        public static class AnonymousStructRPCserverdgram extends Structure
        {
            public long calls;
            public long badcalls;
            public long nullrecv;
            public long badlen;
            public long xdrcall;
            public long dupchecks;
            public long dupreqs;
        }
        
        @FieldOrder({ "client", "server" })
        public static class AnonymousStructNFS extends Structure
        {
            public AnonymousStructNFSclient client;
            public AnonymousStructNFSserver server;
        }
        
        @FieldOrder({ "calls", "badcalls", "clgets", "cltoomany" })
        public static class AnonymousStructNFSclient extends Structure
        {
            public long calls;
            public long badcalls;
            public long clgets;
            public long cltoomany;
        }
        
        @FieldOrder({ "calls", "badcalls", "public_v2", "public_v3" })
        public static class AnonymousStructNFSserver extends Structure
        {
            public long calls;
            public long badcalls;
            public long public_v2;
            public long public_v3;
        }
        
        @FieldOrder({ "client", "server" })
        public static class AnonymousStructNFSv2 extends Structure
        {
            public AnonymousStructNFSv2client client;
            public AnonymousStructNFSv2server server;
        }
        
        @FieldOrder({ "calls", "nullreq", "getattr", "setattr", "root", "lookup", "readlink", "read", "writecache", "write", "create", "remove", "rename", "link", "symlink", "mkdir", "rmdir", "readdir", "statfs" })
        public static class AnonymousStructNFSv2client extends Structure
        {
            public long calls;
            public long nullreq;
            public long getattr;
            public long setattr;
            public long root;
            public long lookup;
            public long readlink;
            public long read;
            public long writecache;
            public long write;
            public long create;
            public long remove;
            public long rename;
            public long link;
            public long symlink;
            public long mkdir;
            public long rmdir;
            public long readdir;
            public long statfs;
        }
        
        @FieldOrder({ "calls", "nullreq", "getattr", "setattr", "root", "lookup", "readlink", "read", "writecache", "write", "create", "remove", "rename", "link", "symlink", "mkdir", "rmdir", "readdir", "statfs" })
        public static class AnonymousStructNFSv2server extends Structure
        {
            public long calls;
            public long nullreq;
            public long getattr;
            public long setattr;
            public long root;
            public long lookup;
            public long readlink;
            public long read;
            public long writecache;
            public long write;
            public long create;
            public long remove;
            public long rename;
            public long link;
            public long symlink;
            public long mkdir;
            public long rmdir;
            public long readdir;
            public long statfs;
        }
        
        @FieldOrder({ "client", "server" })
        public static class AnonymousStructNFSv3 extends Structure
        {
            public AnonymousStructNFSv3client client;
            public AnonymousStructNFSv3server server;
        }
        
        @FieldOrder({ "calls", "nullreq", "getattr", "setattr", "lookup", "access", "readlink", "read", "write", "create", "mkdir", "symlink", "mknod", "remove", "rmdir", "rename", "link", "readdir", "readdirplus", "fsstat", "fsinfo", "pathconf", "commit" })
        public static class AnonymousStructNFSv3client extends Structure
        {
            public long calls;
            public long nullreq;
            public long getattr;
            public long setattr;
            public long lookup;
            public long access;
            public long readlink;
            public long read;
            public long write;
            public long create;
            public long mkdir;
            public long symlink;
            public long mknod;
            public long remove;
            public long rmdir;
            public long rename;
            public long link;
            public long readdir;
            public long readdirplus;
            public long fsstat;
            public long fsinfo;
            public long pathconf;
            public long commit;
        }
        
        @FieldOrder({ "calls", "nullreq", "getattr", "setattr", "lookup", "access", "readlink", "read", "write", "create", "mkdir", "symlink", "mknod", "remove", "rmdir", "rename", "link", "readdir", "readdirplus", "fsstat", "fsinfo", "pathconf", "commit" })
        public static class AnonymousStructNFSv3server extends Structure
        {
            public long calls;
            public long nullreq;
            public long getattr;
            public long setattr;
            public long lookup;
            public long access;
            public long readlink;
            public long read;
            public long write;
            public long create;
            public long mkdir;
            public long symlink;
            public long mknod;
            public long remove;
            public long rmdir;
            public long rename;
            public long link;
            public long readdir;
            public long readdirplus;
            public long fsstat;
            public long fsinfo;
            public long pathconf;
            public long commit;
        }
        
        @FieldOrder({ "client", "server" })
        public static class AnonymousStructNFSv4 extends Structure
        {
            public AnonymousStructNFSv4client client;
            public AnonymousStructNFSv4server server;
        }
        
        @FieldOrder({ "operations", "nullreq", "getattr", "setattr", "lookup", "access", "readlink", "read", "write", "create", "mkdir", "symlink", "mknod", "remove", "rmdir", "rename", "link", "readdir", "statfs", "finfo", "commit", "open", "open_confirm", "open_downgrade", "close", "lock", "unlock", "lock_test", "set_clientid", "renew", "client_confirm", "secinfo", "release_lock", "replicate", "pcl_stat", "acl_stat_l", "pcl_stat_l", "acl_read", "pcl_read", "acl_write", "pcl_write", "delegreturn" })
        public static class AnonymousStructNFSv4client extends Structure
        {
            public long operations;
            public long nullreq;
            public long getattr;
            public long setattr;
            public long lookup;
            public long access;
            public long readlink;
            public long read;
            public long write;
            public long create;
            public long mkdir;
            public long symlink;
            public long mknod;
            public long remove;
            public long rmdir;
            public long rename;
            public long link;
            public long readdir;
            public long statfs;
            public long finfo;
            public long commit;
            public long open;
            public long open_confirm;
            public long open_downgrade;
            public long close;
            public long lock;
            public long unlock;
            public long lock_test;
            public long set_clientid;
            public long renew;
            public long client_confirm;
            public long secinfo;
            public long release_lock;
            public long replicate;
            public long pcl_stat;
            public long acl_stat_l;
            public long pcl_stat_l;
            public long acl_read;
            public long pcl_read;
            public long acl_write;
            public long pcl_write;
            public long delegreturn;
        }
        
        @FieldOrder({ "nullreq", "compound", "operations", "access", "close", "commit", "create", "delegpurge", "delegreturn", "getattr", "getfh", "link", "lock", "lockt", "locku", "lookup", "lookupp", "nverify", "open", "openattr", "open_confirm", "open_downgrade", "putfh", "putpubfh", "putrootfh", "read", "readdir", "readlink", "remove", "rename", "renew", "restorefh", "savefh", "secinfo", "setattr", "set_clientid", "clientid_confirm", "verify", "write", "release_lock" })
        public static class AnonymousStructNFSv4server extends Structure
        {
            public long nullreq;
            public long compound;
            public long operations;
            public long access;
            public long close;
            public long commit;
            public long create;
            public long delegpurge;
            public long delegreturn;
            public long getattr;
            public long getfh;
            public long link;
            public long lock;
            public long lockt;
            public long locku;
            public long lookup;
            public long lookupp;
            public long nverify;
            public long open;
            public long openattr;
            public long open_confirm;
            public long open_downgrade;
            public long putfh;
            public long putpubfh;
            public long putrootfh;
            public long read;
            public long readdir;
            public long readlink;
            public long remove;
            public long rename;
            public long renew;
            public long restorefh;
            public long savefh;
            public long secinfo;
            public long setattr;
            public long set_clientid;
            public long clientid_confirm;
            public long verify;
            public long write;
            public long release_lock;
        }
    }
}
