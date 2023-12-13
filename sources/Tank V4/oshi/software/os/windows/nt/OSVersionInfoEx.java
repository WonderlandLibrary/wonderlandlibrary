package oshi.software.os.windows.nt;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinNT.OSVERSIONINFOEX;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion {
   private OSVERSIONINFOEX _versionInfo;

   public OSVersionInfoEx() {
      this._versionInfo = new OSVERSIONINFOEX();
      if (!Kernel32.INSTANCE.GetVersionEx(this._versionInfo)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
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
      String var1 = null;
      if (this.getPlatformId() == 2) {
         if (this.getMajor() == 6 && this.getMinor() == 3 && this.getProductType() == 1) {
            var1 = "8.1";
         } else if (this.getMajor() == 6 && this.getMinor() == 3 && this.getProductType() != 1) {
            var1 = "Server 2012 R2";
         } else if (this.getMajor() == 6 && this.getMinor() == 2 && this.getProductType() == 1) {
            var1 = "8";
         } else if (this.getMajor() == 6 && this.getMinor() == 2 && this.getProductType() != 1) {
            var1 = "Server 2012";
         } else if (this.getMajor() == 6 && this.getMinor() == 1 && this.getProductType() == 1) {
            var1 = "7";
         } else if (this.getMajor() == 6 && this.getMinor() == 1 && this.getProductType() != 1) {
            var1 = "Server 2008 R2";
         } else if (this.getMajor() == 6 && this.getMinor() == 0 && this.getProductType() != 1) {
            var1 = "Server 2008";
         } else if (this.getMajor() == 6 && this.getMinor() == 0 && this.getProductType() == 1) {
            var1 = "Vista";
         } else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() != 1 && User32.INSTANCE.GetSystemMetrics(89) != 0) {
            var1 = "Server 2003";
         } else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() != 1 && User32.INSTANCE.GetSystemMetrics(89) == 0) {
            var1 = "Server 2003 R2";
         } else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() == 1) {
            var1 = "XP";
         } else if (this.getMajor() == 5 && this.getMinor() == 1) {
            var1 = "XP";
         } else if (this.getMajor() == 5 && this.getMinor() == 0) {
            var1 = "2000";
         } else {
            if (this.getMajor() != 4) {
               throw new RuntimeException("Unsupported Windows NT version: " + this._versionInfo.toString());
            }

            var1 = "NT 4";
            if ("Service Pack 6".equals(this.getServicePack()) && Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Hotfix\\Q246009")) {
               return "NT4 SP6a";
            }
         }

         if (this._versionInfo.wServicePackMajor.intValue() > 0) {
            var1 = var1 + " SP" + this._versionInfo.wServicePackMajor.intValue();
         }
      } else {
         if (this.getPlatformId() != 1) {
            throw new RuntimeException("Unsupported Windows platform: " + this._versionInfo.toString());
         }

         if (this.getMajor() == 4 && this.getMinor() == 90) {
            var1 = "ME";
         } else if (this.getMajor() == 4 && this.getMinor() == 10) {
            if (this._versionInfo.szCSDVersion[1] == 'A') {
               var1 = "98 SE";
            } else {
               var1 = "98";
            }
         } else {
            if (this.getMajor() != 4 || this.getMinor() != 0) {
               throw new RuntimeException("Unsupported Windows 9x version: " + this._versionInfo.toString());
            }

            if (this._versionInfo.szCSDVersion[1] != 'C' && this._versionInfo.szCSDVersion[1] != 'B') {
               var1 = "95";
            } else {
               var1 = "95 OSR2";
            }
         }
      }

      return var1;
   }

   public OSVersionInfoEx(OSVERSIONINFOEX var1) {
      this._versionInfo = var1;
   }
}
