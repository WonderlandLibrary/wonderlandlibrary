// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import oshi.hardware.Display;
import java.util.List;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.SetupApi;
import org.slf4j.Logger;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractDisplay;

@Immutable
final class WindowsDisplay extends AbstractDisplay
{
    private static final Logger LOG;
    private static final SetupApi SU;
    private static final Advapi32 ADV;
    private static final Guid.GUID GUID_DEVINTERFACE_MONITOR;
    
    WindowsDisplay(final byte[] edid) {
        super(edid);
        WindowsDisplay.LOG.debug("Initialized WindowsDisplay");
    }
    
    public static List<Display> getDisplays() {
        final List<Display> displays = new ArrayList<Display>();
        final WinNT.HANDLE hDevInfo = WindowsDisplay.SU.SetupDiGetClassDevs(WindowsDisplay.GUID_DEVINTERFACE_MONITOR, null, null, 18);
        if (!hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
            final SetupApi.SP_DEVICE_INTERFACE_DATA deviceInterfaceData = new SetupApi.SP_DEVICE_INTERFACE_DATA();
            deviceInterfaceData.cbSize = deviceInterfaceData.size();
            final SetupApi.SP_DEVINFO_DATA info = new SetupApi.SP_DEVINFO_DATA();
            for (int memberIndex = 0; WindowsDisplay.SU.SetupDiEnumDeviceInfo(hDevInfo, memberIndex, info); ++memberIndex) {
                final WinReg.HKEY key = WindowsDisplay.SU.SetupDiOpenDevRegKey(hDevInfo, info, 1, 0, 1, 1);
                byte[] edid = { 0 };
                final IntByReference pType = new IntByReference();
                final IntByReference lpcbData = new IntByReference();
                if (WindowsDisplay.ADV.RegQueryValueEx(key, "EDID", 0, pType, edid, lpcbData) == 234) {
                    edid = new byte[lpcbData.getValue()];
                    if (WindowsDisplay.ADV.RegQueryValueEx(key, "EDID", 0, pType, edid, lpcbData) == 0) {
                        final Display display = new WindowsDisplay(edid);
                        displays.add(display);
                    }
                }
                Advapi32.INSTANCE.RegCloseKey(key);
            }
            WindowsDisplay.SU.SetupDiDestroyDeviceInfoList(hDevInfo);
        }
        return displays;
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsDisplay.class);
        SU = SetupApi.INSTANCE;
        ADV = Advapi32.INSTANCE;
        GUID_DEVINTERFACE_MONITOR = new Guid.GUID("E6F07B5F-EE97-4a90-B076-33F57BF4EAA7");
    }
}
