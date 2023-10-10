// 
// Decompiled by Procyon v0.5.36
// 

package oshi;

import com.sun.jna.Platform;
import oshi.hardware.platform.unix.openbsd.OpenBsdHardwareAbstractionLayer;
import oshi.hardware.platform.unix.aix.AixHardwareAbstractionLayer;
import oshi.hardware.platform.unix.freebsd.FreeBsdHardwareAbstractionLayer;
import oshi.hardware.platform.unix.solaris.SolarisHardwareAbstractionLayer;
import oshi.hardware.platform.mac.MacHardwareAbstractionLayer;
import oshi.hardware.platform.linux.LinuxHardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;
import oshi.software.os.unix.openbsd.OpenBsdOperatingSystem;
import oshi.software.os.unix.aix.AixOperatingSystem;
import oshi.software.os.unix.freebsd.FreeBsdOperatingSystem;
import oshi.software.os.unix.solaris.SolarisOperatingSystem;
import oshi.software.os.mac.MacOperatingSystem;
import oshi.software.os.linux.LinuxOperatingSystem;
import oshi.software.os.windows.WindowsOperatingSystem;
import oshi.util.Memoizer;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import java.util.function.Supplier;

public class SystemInfo
{
    private static final PlatformEnum CURRENT_PLATFORM;
    private static final String NOT_SUPPORTED = "Operating system not supported: ";
    private final Supplier<OperatingSystem> os;
    private final Supplier<HardwareAbstractionLayer> hardware;
    
    public SystemInfo() {
        this.os = Memoizer.memoize(SystemInfo::createOperatingSystem);
        this.hardware = Memoizer.memoize(SystemInfo::createHardware);
    }
    
    public static PlatformEnum getCurrentPlatform() {
        return SystemInfo.CURRENT_PLATFORM;
    }
    
    public OperatingSystem getOperatingSystem() {
        return this.os.get();
    }
    
    private static OperatingSystem createOperatingSystem() {
        switch (SystemInfo.CURRENT_PLATFORM) {
            case WINDOWS: {
                return new WindowsOperatingSystem();
            }
            case LINUX: {
                return new LinuxOperatingSystem();
            }
            case MACOS: {
                return new MacOperatingSystem();
            }
            case SOLARIS: {
                return new SolarisOperatingSystem();
            }
            case FREEBSD: {
                return new FreeBsdOperatingSystem();
            }
            case AIX: {
                return new AixOperatingSystem();
            }
            case OPENBSD: {
                return new OpenBsdOperatingSystem();
            }
            default: {
                throw new UnsupportedOperationException("Operating system not supported: " + SystemInfo.CURRENT_PLATFORM.getName());
            }
        }
    }
    
    public HardwareAbstractionLayer getHardware() {
        return this.hardware.get();
    }
    
    private static HardwareAbstractionLayer createHardware() {
        switch (SystemInfo.CURRENT_PLATFORM) {
            case WINDOWS: {
                return new WindowsHardwareAbstractionLayer();
            }
            case LINUX: {
                return new LinuxHardwareAbstractionLayer();
            }
            case MACOS: {
                return new MacHardwareAbstractionLayer();
            }
            case SOLARIS: {
                return new SolarisHardwareAbstractionLayer();
            }
            case FREEBSD: {
                return new FreeBsdHardwareAbstractionLayer();
            }
            case AIX: {
                return new AixHardwareAbstractionLayer();
            }
            case OPENBSD: {
                return new OpenBsdHardwareAbstractionLayer();
            }
            default: {
                throw new UnsupportedOperationException("Operating system not supported: " + SystemInfo.CURRENT_PLATFORM.getName());
            }
        }
    }
    
    static {
        CURRENT_PLATFORM = PlatformEnum.getValue(Platform.getOSType());
    }
}
