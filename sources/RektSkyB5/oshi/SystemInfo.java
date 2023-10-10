/*
 * Decompiled with CFR 0.152.
 */
package oshi;

import com.sun.jna.Platform;
import oshi.PlatformEnum;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.software.os.linux.LinuxHardwareAbstractionLayer;
import oshi.software.os.linux.LinuxOperatingSystem;
import oshi.software.os.mac.MacHardwareAbstractionLayer;
import oshi.software.os.mac.MacOperatingSystem;
import oshi.software.os.windows.WindowsHardwareAbstractionLayer;
import oshi.software.os.windows.WindowsOperatingSystem;

public class SystemInfo {
    private OperatingSystem _os = null;
    private HardwareAbstractionLayer _hardware = null;
    private PlatformEnum currentPlatformEnum = Platform.isWindows() ? PlatformEnum.WINDOWS : (Platform.isLinux() ? PlatformEnum.LINUX : (Platform.isMac() ? PlatformEnum.MACOSX : PlatformEnum.UNKNOWN));

    public OperatingSystem getOperatingSystem() {
        if (this._os == null) {
            switch (this.currentPlatformEnum) {
                case WINDOWS: {
                    this._os = new WindowsOperatingSystem();
                    break;
                }
                case LINUX: {
                    this._os = new LinuxOperatingSystem();
                    break;
                }
                case MACOSX: {
                    this._os = new MacOperatingSystem();
                    break;
                }
                default: {
                    throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
                }
            }
        }
        return this._os;
    }

    public HardwareAbstractionLayer getHardware() {
        if (this._hardware == null) {
            switch (this.currentPlatformEnum) {
                case WINDOWS: {
                    this._hardware = new WindowsHardwareAbstractionLayer();
                    break;
                }
                case LINUX: {
                    this._hardware = new LinuxHardwareAbstractionLayer();
                    break;
                }
                case MACOSX: {
                    this._hardware = new MacHardwareAbstractionLayer();
                    break;
                }
                default: {
                    throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
                }
            }
        }
        return this._hardware;
    }
}

