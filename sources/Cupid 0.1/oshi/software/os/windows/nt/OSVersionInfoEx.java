package oshi.software.os.windows.nt;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion {
  private WinNT.OSVERSIONINFOEX _versionInfo;
  
  public OSVersionInfoEx() {
    this._versionInfo = new WinNT.OSVERSIONINFOEX();
    if (!Kernel32.INSTANCE.GetVersionEx(this._versionInfo))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
  }
  
  public int getMajor() {
    return this._versionInfo.dwMajorVersion.intValue();
  }
  
  public int getMinor() {
    return this._versionInfo.dwMinorVersion.intValue();
  }
  
  public int getBuildNumber() {
    return this._versionInfo.dwBuildNumber.intValue();
  }
  
  public int getPlatformId() {
    return this._versionInfo.dwPlatformId.intValue();
  }
  
  public String getServicePack() {
    return Native.toString(this._versionInfo.szCSDVersion);
  }
  
  public int getSuiteMask() {
    return this._versionInfo.wSuiteMask.intValue();
  }
  
  public byte getProductType() {
    return this._versionInfo.wProductType;
  }
  
  public String toString() {
    String version = null;
    if (getPlatformId() == 2) {
      if (getMajor() == 6 && 
        getMinor() == 3 && 
        getProductType() == 1) {
        version = "8.1";
      } else if (getMajor() == 6 && 
        getMinor() == 3 && 
        getProductType() != 1) {
        version = "Server 2012 R2";
      } else if (getMajor() == 6 && 
        getMinor() == 2 && 
        getProductType() == 1) {
        version = "8";
      } else if (getMajor() == 6 && 
        getMinor() == 2 && 
        getProductType() != 1) {
        version = "Server 2012";
      } else if (getMajor() == 6 && 
        getMinor() == 1 && 
        getProductType() == 1) {
        version = "7";
      } else if (getMajor() == 6 && 
        getMinor() == 1 && 
        getProductType() != 1) {
        version = "Server 2008 R2";
      } else if (getMajor() == 6 && 
        getMinor() == 0 && 
        getProductType() != 1) {
        version = "Server 2008";
      } else if (getMajor() == 6 && 
        getMinor() == 0 && 
        getProductType() == 1) {
        version = "Vista";
      } else if (getMajor() == 5 && 
        getMinor() == 2 && 
        getProductType() != 1 && User32.INSTANCE
        .GetSystemMetrics(89) != 0) {
        version = "Server 2003";
      } else if (getMajor() == 5 && 
        getMinor() == 2 && 
        getProductType() != 1 && User32.INSTANCE
        .GetSystemMetrics(89) == 0) {
        version = "Server 2003 R2";
      } else if (getMajor() == 5 && 
        getMinor() == 2 && 
        getProductType() == 1) {
        version = "XP";
      } else if (getMajor() == 5 && 
        getMinor() == 1) {
        version = "XP";
      } else if (getMajor() == 5 && 
        getMinor() == 0) {
        version = "2000";
      } else if (getMajor() == 4) {
        version = "NT 4";
        if ("Service Pack 6".equals(getServicePack()) && 
          Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Hotfix\\Q246009"))
          return "NT4 SP6a"; 
      } else {
        throw new RuntimeException("Unsupported Windows NT version: " + this._versionInfo
            .toString());
      } 
      if (this._versionInfo.wServicePackMajor.intValue() > 0)
        version = version + " SP" + this._versionInfo.wServicePackMajor.intValue(); 
    } else if (getPlatformId() == 1) {
      if (getMajor() == 4 && 
        getMinor() == 90) {
        version = "ME";
      } else if (getMajor() == 4 && 
        getMinor() == 10) {
        if (this._versionInfo.szCSDVersion[1] == 'A') {
          version = "98 SE";
        } else {
          version = "98";
        } 
      } else if (getMajor() == 4 && 
        getMinor() == 0) {
        if (this._versionInfo.szCSDVersion[1] == 'C' || this._versionInfo.szCSDVersion[1] == 'B') {
          version = "95 OSR2";
        } else {
          version = "95";
        } 
      } else {
        throw new RuntimeException("Unsupported Windows 9x version: " + this._versionInfo
            .toString());
      } 
    } else {
      throw new RuntimeException("Unsupported Windows platform: " + this._versionInfo
          .toString());
    } 
    return version;
  }
  
  public OSVersionInfoEx(WinNT.OSVERSIONINFOEX versionInfo) {
    this._versionInfo = versionInfo;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\oshi\software\os\windows\nt\OSVersionInfoEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */