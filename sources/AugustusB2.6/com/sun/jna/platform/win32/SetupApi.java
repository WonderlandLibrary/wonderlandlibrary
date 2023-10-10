// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface SetupApi extends StdCallLibrary
{
    public static final SetupApi INSTANCE = Native.load("setupapi", SetupApi.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final Guid.GUID GUID_DEVINTERFACE_DISK = new Guid.GUID("53F56307-B6BF-11D0-94F2-00A0C91EFB8B");
    public static final Guid.GUID GUID_DEVINTERFACE_COMPORT = new Guid.GUID("86E0D1E0-8089-11D0-9CE4-08003E301F73");
    public static final int DIGCF_DEFAULT = 1;
    public static final int DIGCF_PRESENT = 2;
    public static final int DIGCF_ALLCLASSES = 4;
    public static final int DIGCF_PROFILE = 8;
    public static final int DIGCF_DEVICEINTERFACE = 16;
    public static final int SPDRP_REMOVAL_POLICY = 31;
    public static final int CM_DEVCAP_REMOVABLE = 4;
    public static final int DICS_FLAG_GLOBAL = 1;
    public static final int DICS_FLAG_CONFIGSPECIFIC = 2;
    public static final int DICS_FLAG_CONFIGGENERAL = 4;
    public static final int DIREG_DEV = 1;
    public static final int DIREG_DRV = 2;
    public static final int DIREG_BOTH = 4;
    public static final int SPDRP_DEVICEDESC = 0;
    
    WinNT.HANDLE SetupDiGetClassDevs(final Guid.GUID p0, final Pointer p1, final Pointer p2, final int p3);
    
    boolean SetupDiDestroyDeviceInfoList(final WinNT.HANDLE p0);
    
    boolean SetupDiEnumDeviceInterfaces(final WinNT.HANDLE p0, final Pointer p1, final Guid.GUID p2, final int p3, final SP_DEVICE_INTERFACE_DATA p4);
    
    boolean SetupDiGetDeviceInterfaceDetail(final WinNT.HANDLE p0, final SP_DEVICE_INTERFACE_DATA p1, final Pointer p2, final int p3, final IntByReference p4, final SP_DEVINFO_DATA p5);
    
    boolean SetupDiGetDeviceRegistryProperty(final WinNT.HANDLE p0, final SP_DEVINFO_DATA p1, final int p2, final IntByReference p3, final Pointer p4, final int p5, final IntByReference p6);
    
    WinReg.HKEY SetupDiOpenDevRegKey(final WinNT.HANDLE p0, final SP_DEVINFO_DATA p1, final int p2, final int p3, final int p4, final int p5);
    
    boolean SetupDiEnumDeviceInfo(final WinNT.HANDLE p0, final int p1, final SP_DEVINFO_DATA p2);
    
    @FieldOrder({ "cbSize", "InterfaceClassGuid", "Flags", "Reserved" })
    public static class SP_DEVICE_INTERFACE_DATA extends Structure
    {
        public int cbSize;
        public Guid.GUID InterfaceClassGuid;
        public int Flags;
        public Pointer Reserved;
        
        public SP_DEVICE_INTERFACE_DATA() {
            this.cbSize = this.size();
        }
        
        public SP_DEVICE_INTERFACE_DATA(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends SP_DEVINFO_DATA implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "cbSize", "InterfaceClassGuid", "DevInst", "Reserved" })
    public static class SP_DEVINFO_DATA extends Structure
    {
        public int cbSize;
        public Guid.GUID InterfaceClassGuid;
        public int DevInst;
        public Pointer Reserved;
        
        public SP_DEVINFO_DATA() {
            this.cbSize = this.size();
        }
        
        public SP_DEVINFO_DATA(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends SP_DEVINFO_DATA implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
