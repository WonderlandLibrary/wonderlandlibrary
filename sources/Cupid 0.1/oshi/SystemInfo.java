package oshi;

import com.sun.jna.Platform;
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
  
  private PlatformEnum currentPlatformEnum;
  
  public SystemInfo() {
    if (Platform.isWindows()) {
      this.currentPlatformEnum = PlatformEnum.WINDOWS;
    } else if (Platform.isLinux()) {
      this.currentPlatformEnum = PlatformEnum.LINUX;
    } else if (Platform.isMac()) {
      this.currentPlatformEnum = PlatformEnum.MACOSX;
    } else {
      this.currentPlatformEnum = PlatformEnum.UNKNOWN;
    } 
  }
  
  public OperatingSystem getOperatingSystem() {
    if (this._os == null) {
      switch (this.currentPlatformEnum) {
        case WINDOWS:
          this._os = (OperatingSystem)new WindowsOperatingSystem();
          return this._os;
        case LINUX:
          this._os = (OperatingSystem)new LinuxOperatingSystem();
          return this._os;
        case MACOSX:
          this._os = (OperatingSystem)new MacOperatingSystem();
          return this._os;
      } 
      throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
    } 
    return this._os;
  }
  
  public HardwareAbstractionLayer getHardware() {
    if (this._hardware == null) {
      switch (this.currentPlatformEnum) {
        case WINDOWS:
          this._hardware = (HardwareAbstractionLayer)new WindowsHardwareAbstractionLayer();
          return this._hardware;
        case LINUX:
          this._hardware = (HardwareAbstractionLayer)new LinuxHardwareAbstractionLayer();
          return this._hardware;
        case MACOSX:
          this._hardware = (HardwareAbstractionLayer)new MacHardwareAbstractionLayer();
          return this._hardware;
      } 
      throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
    } 
    return this._hardware;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\oshi\SystemInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */