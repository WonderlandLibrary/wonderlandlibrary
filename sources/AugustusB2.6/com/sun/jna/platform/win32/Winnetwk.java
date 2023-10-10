// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Structure;

public abstract class Winnetwk
{
    public static int UNIVERSAL_NAME_INFO_LEVEL;
    public static int REMOTE_NAME_INFO_LEVEL;
    
    static {
        Winnetwk.UNIVERSAL_NAME_INFO_LEVEL = 1;
        Winnetwk.REMOTE_NAME_INFO_LEVEL = 2;
    }
    
    public class RESOURCESCOPE
    {
        public static final int RESOURCE_CONNECTED = 1;
        public static final int RESOURCE_GLOBALNET = 2;
        public static final int RESOURCE_REMEMBERED = 3;
        public static final int RESOURCE_RECENT = 4;
        public static final int RESOURCE_CONTEXT = 5;
    }
    
    public class RESOURCETYPE
    {
        public static final int RESOURCETYPE_ANY = 0;
        public static final int RESOURCETYPE_DISK = 1;
        public static final int RESOURCETYPE_PRINT = 2;
        public static final int RESOURCETYPE_RESERVED = 8;
        public static final int RESOURCETYPE_UNKNOWN = -1;
    }
    
    public class RESOURCEDISPLAYTYPE
    {
        public static final int RESOURCEDISPLAYTYPE_GENERIC = 0;
        public static final int RESOURCEDISPLAYTYPE_DOMAIN = 1;
        public static final int RESOURCEDISPLAYTYPE_SERVER = 2;
        public static final int RESOURCEDISPLAYTYPE_SHARE = 3;
        public static final int RESOURCEDISPLAYTYPE_FILE = 4;
    }
    
    public class RESOURCEUSAGE
    {
        public static final int RESOURCEUSAGE_CONNECTABLE = 1;
        public static final int RESOURCEUSAGE_CONTAINER = 2;
        public static final int RESOURCEUSAGE_NOLOCALDEVICE = 4;
        public static final int RESOURCEUSAGE_SIBLING = 8;
        public static final int RESOURCEUSAGE_ATTACHED = 16;
        public static final int RESOURCEUSAGE_ALL = 19;
    }
    
    public class ConnectFlag
    {
        public static final int CONNECT_UPDATE_PROFILE = 1;
        public static final int CONNECT_INTERACTIVE = 8;
        public static final int CONNECT_PROMPT = 16;
        public static final int CONNECT_REDIRECT = 128;
        public static final int CONNECT_LOCALDRIVE = 256;
        public static final int CONNECT_COMMANDLINE = 2048;
        public static final int CONNECT_CMD_SAVECRED = 4096;
    }
    
    @FieldOrder({ "dwScope", "dwType", "dwDisplayType", "dwUsage", "lpLocalName", "lpRemoteName", "lpComment", "lpProvider" })
    public static class NETRESOURCE extends Structure
    {
        public int dwScope;
        public int dwType;
        public int dwDisplayType;
        public int dwUsage;
        public String lpLocalName;
        public String lpRemoteName;
        public String lpComment;
        public String lpProvider;
        
        public NETRESOURCE() {
            super(W32APITypeMapper.DEFAULT);
        }
        
        public NETRESOURCE(final Pointer address) {
            super(address, 0, W32APITypeMapper.DEFAULT);
            this.read();
        }
        
        public static class ByReference extends NETRESOURCE implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "lpUniversalName" })
    public static class UNIVERSAL_NAME_INFO extends Structure
    {
        public String lpUniversalName;
        
        public UNIVERSAL_NAME_INFO() {
            super(W32APITypeMapper.DEFAULT);
        }
        
        public UNIVERSAL_NAME_INFO(final Pointer address) {
            super(address, 0, W32APITypeMapper.DEFAULT);
            this.read();
        }
        
        public static class ByReference extends REMOTE_NAME_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "lpUniversalName", "lpConnectionName", "lpRemainingPath" })
    public static class REMOTE_NAME_INFO extends Structure
    {
        public String lpUniversalName;
        public String lpConnectionName;
        public String lpRemainingPath;
        
        public REMOTE_NAME_INFO() {
            super(W32APITypeMapper.DEFAULT);
        }
        
        public REMOTE_NAME_INFO(final Pointer address) {
            super(address, 0, W32APITypeMapper.DEFAULT);
            this.read();
        }
        
        public static class ByReference extends REMOTE_NAME_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
