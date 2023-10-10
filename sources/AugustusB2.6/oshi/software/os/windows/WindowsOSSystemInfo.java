// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import org.slf4j.Logger;

public class WindowsOSSystemInfo
{
    private static final Logger LOG;
    private WinBase.SYSTEM_INFO systemInfo;
    
    public WindowsOSSystemInfo() {
        this.systemInfo = null;
        this.init();
    }
    
    public WindowsOSSystemInfo(final WinBase.SYSTEM_INFO si) {
        this.systemInfo = null;
        this.systemInfo = si;
    }
    
    private void init() {
        final WinBase.SYSTEM_INFO si = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetSystemInfo(si);
        try {
            final IntByReference isWow64 = new IntByReference();
            final WinNT.HANDLE hProcess = Kernel32.INSTANCE.GetCurrentProcess();
            if (Kernel32.INSTANCE.IsWow64Process(hProcess, isWow64) && isWow64.getValue() > 0) {
                Kernel32.INSTANCE.GetNativeSystemInfo(si);
            }
        }
        catch (UnsatisfiedLinkError e) {
            WindowsOSSystemInfo.LOG.trace("No WOW64 support: {}", e.getMessage());
        }
        this.systemInfo = si;
        WindowsOSSystemInfo.LOG.debug("Initialized OSNativeSystemInfo");
    }
    
    public int getNumberOfProcessors() {
        return this.systemInfo.dwNumberOfProcessors.intValue();
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsOSSystemInfo.class);
    }
}
