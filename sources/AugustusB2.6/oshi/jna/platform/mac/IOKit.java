// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.mac;

import com.sun.jna.Native;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

public interface IOKit extends com.sun.jna.platform.mac.IOKit
{
    public static final IOKit INSTANCE = Native.load("IOKit", IOKit.class);
    
    int IOConnectCallStructMethod(final IOConnect p0, final int p1, final Structure p2, final NativeLong p3, final Structure p4, final NativeLongByReference p5);
    
    @FieldOrder({ "major", "minor", "build", "reserved", "release" })
    public static class SMCKeyDataVers extends Structure
    {
        public byte major;
        public byte minor;
        public byte build;
        public byte reserved;
        public short release;
    }
    
    @FieldOrder({ "version", "length", "cpuPLimit", "gpuPLimit", "memPLimit" })
    public static class SMCKeyDataPLimitData extends Structure
    {
        public short version;
        public short length;
        public int cpuPLimit;
        public int gpuPLimit;
        public int memPLimit;
    }
    
    @FieldOrder({ "dataSize", "dataType", "dataAttributes" })
    public static class SMCKeyDataKeyInfo extends Structure
    {
        public int dataSize;
        public int dataType;
        public byte dataAttributes;
    }
    
    @FieldOrder({ "key", "vers", "pLimitData", "keyInfo", "result", "status", "data8", "data32", "bytes" })
    public static class SMCKeyData extends Structure
    {
        public int key;
        public SMCKeyDataVers vers;
        public SMCKeyDataPLimitData pLimitData;
        public SMCKeyDataKeyInfo keyInfo;
        public byte result;
        public byte status;
        public byte data8;
        public int data32;
        public byte[] bytes;
        
        public SMCKeyData() {
            this.bytes = new byte[32];
        }
    }
    
    @FieldOrder({ "key", "dataSize", "dataType", "bytes" })
    public static class SMCVal extends Structure
    {
        public byte[] key;
        public int dataSize;
        public byte[] dataType;
        public byte[] bytes;
        
        public SMCVal() {
            this.key = new byte[5];
            this.dataType = new byte[5];
            this.bytes = new byte[32];
        }
    }
}
