// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface DBT
{
    public static final int DBT_NO_DISK_SPACE = 71;
    public static final int DBT_LOW_DISK_SPACE = 72;
    public static final int DBT_CONFIGMGPRIVATE = 32767;
    public static final int DBT_DEVICEARRIVAL = 32768;
    public static final int DBT_DEVICEQUERYREMOVE = 32769;
    public static final int DBT_DEVICEQUERYREMOVEFAILED = 32770;
    public static final int DBT_DEVICEREMOVEPENDING = 32771;
    public static final int DBT_DEVICEREMOVECOMPLETE = 32772;
    public static final int DBT_DEVNODES_CHANGED = 7;
    public static final int DBT_DEVICETYPESPECIFIC = 32773;
    public static final int DBT_CUSTOMEVENT = 32774;
    public static final Guid.GUID GUID_DEVINTERFACE_USB_DEVICE = new Guid.GUID("{A5DCBF10-6530-11D2-901F-00C04FB951ED}");
    public static final Guid.GUID GUID_DEVINTERFACE_HID = new Guid.GUID("{4D1E55B2-F16F-11CF-88CB-001111000030}");
    public static final Guid.GUID GUID_DEVINTERFACE_VOLUME = new Guid.GUID("{53F5630D-B6BF-11D0-94F2-00A0C91EFB8B}");
    public static final Guid.GUID GUID_DEVINTERFACE_KEYBOARD = new Guid.GUID("{884b96c3-56ef-11d1-bc8c-00a0c91405dd}");
    public static final Guid.GUID GUID_DEVINTERFACE_MOUSE = new Guid.GUID("{378DE44C-56EF-11D1-BC8C-00A0C91405DD}");
    public static final int DBT_DEVTYP_OEM = 0;
    public static final int DBT_DEVTYP_DEVNODE = 1;
    public static final int DBT_DEVTYP_VOLUME = 2;
    public static final int DBT_DEVTYP_PORT = 3;
    public static final int DBT_DEVTYP_NET = 4;
    public static final int DBT_DEVTYP_DEVICEINTERFACE = 5;
    public static final int DBT_DEVTYP_HANDLE = 6;
    public static final int DBTF_MEDIA = 1;
    public static final int DBTF_NET = 2;
    
    @FieldOrder({ "dbch_size", "dbch_devicetype", "dbch_reserved" })
    public static class DEV_BROADCAST_HDR extends Structure
    {
        public int dbch_size;
        public int dbch_devicetype;
        public int dbch_reserved;
        
        public DEV_BROADCAST_HDR() {
        }
        
        public DEV_BROADCAST_HDR(final long pointer) {
            this(new Pointer(pointer));
        }
        
        public DEV_BROADCAST_HDR(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
    
    @FieldOrder({ "dbco_size", "dbco_devicetype", "dbco_reserved", "dbco_identifier", "dbco_suppfunc" })
    public static class DEV_BROADCAST_OEM extends Structure
    {
        public int dbco_size;
        public int dbco_devicetype;
        public int dbco_reserved;
        public int dbco_identifier;
        public int dbco_suppfunc;
        
        public DEV_BROADCAST_OEM() {
        }
        
        public DEV_BROADCAST_OEM(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
    
    @FieldOrder({ "dbcd_size", "dbcd_devicetype", "dbcd_reserved", "dbcd_devnode" })
    public static class DEV_BROADCAST_DEVNODE extends Structure
    {
        public int dbcd_size;
        public int dbcd_devicetype;
        public int dbcd_reserved;
        public int dbcd_devnode;
        
        public DEV_BROADCAST_DEVNODE() {
        }
        
        public DEV_BROADCAST_DEVNODE(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
    
    @FieldOrder({ "dbcv_size", "dbcv_devicetype", "dbcv_reserved", "dbcv_unitmask", "dbcv_flags" })
    public static class DEV_BROADCAST_VOLUME extends Structure
    {
        public int dbcv_size;
        public int dbcv_devicetype;
        public int dbcv_reserved;
        public int dbcv_unitmask;
        public short dbcv_flags;
        
        public DEV_BROADCAST_VOLUME() {
        }
        
        public DEV_BROADCAST_VOLUME(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
    
    @FieldOrder({ "dbcp_size", "dbcp_devicetype", "dbcp_reserved", "dbcp_name" })
    public static class DEV_BROADCAST_PORT extends Structure
    {
        public int dbcp_size;
        public int dbcp_devicetype;
        public int dbcp_reserved;
        public byte[] dbcp_name;
        
        public DEV_BROADCAST_PORT() {
            this.dbcp_name = new byte[1];
        }
        
        public DEV_BROADCAST_PORT(final Pointer memory) {
            super(memory);
            this.dbcp_name = new byte[1];
            this.read();
        }
        
        @Override
        public void read() {
            final int size = this.getPointer().getInt(0L);
            this.dbcp_name = new byte[size - this.fieldOffset("dbcp_name")];
            super.read();
        }
        
        public String getDbcpName() {
            if (W32APITypeMapper.DEFAULT == W32APITypeMapper.ASCII) {
                return Native.toString(this.dbcp_name);
            }
            return new String(this.dbcp_name, StandardCharsets.UTF_16LE);
        }
    }
    
    @FieldOrder({ "dbcn_size", "dbcn_devicetype", "dbcn_reserved", "dbcn_resource", "dbcn_flags" })
    public static class DEV_BROADCAST_NET extends Structure
    {
        public int dbcn_size;
        public int dbcn_devicetype;
        public int dbcn_reserved;
        public int dbcn_resource;
        public int dbcn_flags;
        
        public DEV_BROADCAST_NET() {
        }
        
        public DEV_BROADCAST_NET(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
    
    @FieldOrder({ "dbcc_size", "dbcc_devicetype", "dbcc_reserved", "dbcc_classguid", "dbcc_name" })
    public static class DEV_BROADCAST_DEVICEINTERFACE extends Structure
    {
        public int dbcc_size;
        public int dbcc_devicetype;
        public int dbcc_reserved;
        public Guid.GUID dbcc_classguid;
        public char[] dbcc_name;
        
        public DEV_BROADCAST_DEVICEINTERFACE() {
            this.dbcc_name = new char[1];
        }
        
        public DEV_BROADCAST_DEVICEINTERFACE(final long pointer) {
            this(new Pointer(pointer));
        }
        
        public DEV_BROADCAST_DEVICEINTERFACE(final Pointer memory) {
            super(memory);
            this.dbcc_name = new char[1];
            this.read();
        }
        
        @Override
        public void read() {
            if (W32APITypeMapper.DEFAULT == W32APITypeMapper.ASCII) {
                Logger.getLogger(DBT.class.getName()).warning("DEV_BROADCAST_DEVICEINTERFACE must not be used with w32.ascii = true!");
            }
            final int size = this.getPointer().getInt(0L);
            final int len = (size - this.fieldOffset("dbcc_name")) / Native.WCHAR_SIZE;
            this.dbcc_name = new char[len];
            super.read();
        }
        
        public String getDbcc_name() {
            return Native.toString(this.dbcc_name);
        }
    }
    
    @FieldOrder({ "dbch_size", "dbch_devicetype", "dbch_reserved", "dbch_handle", "dbch_hdevnotify", "dbch_eventguid", "dbch_nameoffset", "dbch_data" })
    public static class DEV_BROADCAST_HANDLE extends Structure
    {
        public int dbch_size;
        public int dbch_devicetype;
        public int dbch_reserved;
        public WinNT.HANDLE dbch_handle;
        public WinUser.HDEVNOTIFY dbch_hdevnotify;
        public Guid.GUID dbch_eventguid;
        public WinDef.LONG dbch_nameoffset;
        public byte[] dbch_data;
        
        public DEV_BROADCAST_HANDLE() {
            this.dbch_data = new byte[1];
        }
        
        public DEV_BROADCAST_HANDLE(final Pointer memory) {
            super(memory);
            this.dbch_data = new byte[1];
            this.read();
        }
    }
}
