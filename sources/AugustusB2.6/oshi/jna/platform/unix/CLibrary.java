// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Structure;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Library;
import com.sun.jna.platform.unix.LibCAPI;

public interface CLibrary extends LibCAPI, Library
{
    public static final int AI_CANONNAME = 2;
    public static final int UT_LINESIZE = 32;
    public static final int UT_NAMESIZE = 32;
    public static final int UT_HOSTSIZE = 256;
    public static final int LOGIN_PROCESS = 6;
    public static final int USER_PROCESS = 7;
    
    int getpid();
    
    int getaddrinfo(final String p0, final String p1, final Addrinfo p2, final PointerByReference p3);
    
    void freeaddrinfo(final Pointer p0);
    
    String gai_strerror(final int p0);
    
    void setutxent();
    
    void endutxent();
    
    int sysctl(final int[] p0, final int p1, final Pointer p2, final size_t.ByReference p3, final Pointer p4, final size_t p5);
    
    int sysctlbyname(final String p0, final Pointer p1, final size_t.ByReference p2, final Pointer p3, final size_t p4);
    
    int sysctlnametomib(final String p0, final Pointer p1, final size_t.ByReference p2);
    
    int open(final String p0, final int p1);
    
    ssize_t pread(final int p0, final Pointer p1, final size_t p2, final NativeLong p3);
    
    @FieldOrder({ "sa_family", "sa_data" })
    public static class Sockaddr extends Structure
    {
        public short sa_family;
        public byte[] sa_data;
        
        public Sockaddr() {
            this.sa_data = new byte[14];
        }
        
        public static class ByReference extends Sockaddr implements Structure.ByReference
        {
        }
    }
    
    @FieldOrder({ "ai_flags", "ai_family", "ai_socktype", "ai_protocol", "ai_addrlen", "ai_addr", "ai_canonname", "ai_next" })
    public static class Addrinfo extends Structure
    {
        public int ai_flags;
        public int ai_family;
        public int ai_socktype;
        public int ai_protocol;
        public int ai_addrlen;
        public Sockaddr.ByReference ai_addr;
        public String ai_canonname;
        public ByReference ai_next;
        
        public Addrinfo() {
        }
        
        public Addrinfo(final Pointer p) {
            super(p);
            this.read();
        }
        
        public static class ByReference extends Addrinfo implements Structure.ByReference
        {
        }
    }
    
    public static class BsdTcpstat
    {
        public int tcps_connattempt;
        public int tcps_accepts;
        public int tcps_drops;
        public int tcps_conndrops;
        public int tcps_sndpack;
        public int tcps_sndrexmitpack;
        public int tcps_rcvpack;
        public int tcps_rcvbadsum;
        public int tcps_rcvbadoff;
        public int tcps_rcvmemdrop;
        public int tcps_rcvshort;
    }
    
    public static class BsdUdpstat
    {
        public int udps_ipackets;
        public int udps_hdrops;
        public int udps_badsum;
        public int udps_badlen;
        public int udps_opackets;
        public int udps_noportmcast;
        public int udps_rcv6_swcsum;
        public int udps_snd6_swcsum;
    }
    
    public static class BsdIpstat
    {
        public int ips_total;
        public int ips_badsum;
        public int ips_tooshort;
        public int ips_toosmall;
        public int ips_badhlen;
        public int ips_badlen;
        public int ips_delivered;
    }
    
    public static class BsdIp6stat
    {
        public long ip6s_total;
        public long ip6s_localout;
    }
}
