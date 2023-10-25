/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;

public abstract class Secur32Util {
    public static String getUserNameEx(int format) {
        char[] buffer = new char[128];
        IntByReference len = new IntByReference(buffer.length);
        boolean result = Secur32.INSTANCE.GetUserNameEx(format, buffer, len);
        if (!result) {
            int rc = Kernel32.INSTANCE.GetLastError();
            switch (rc) {
                case 234: {
                    buffer = new char[len.getValue() + 1];
                    break;
                }
                default: {
                    throw new Win32Exception(Native.getLastError());
                }
            }
            result = Secur32.INSTANCE.GetUserNameEx(format, buffer, len);
        }
        if (!result) {
            throw new Win32Exception(Native.getLastError());
        }
        return Native.toString(buffer);
    }

    public static SecurityPackage[] getSecurityPackages() {
        IntByReference pcPackages = new IntByReference();
        Sspi.PSecPkgInfo.ByReference pPackageInfo = new Sspi.PSecPkgInfo.ByReference();
        int rc = Secur32.INSTANCE.EnumerateSecurityPackages(pcPackages, pPackageInfo);
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        Sspi.SecPkgInfo.ByReference[] packagesInfo = pPackageInfo.toArray(pcPackages.getValue());
        ArrayList<SecurityPackage> packages = new ArrayList<SecurityPackage>(pcPackages.getValue());
        for (Sspi.SecPkgInfo.ByReference packageInfo : packagesInfo) {
            SecurityPackage securityPackage = new SecurityPackage();
            securityPackage.name = packageInfo.Name.toString();
            securityPackage.comment = packageInfo.Comment.toString();
            packages.add(securityPackage);
        }
        rc = Secur32.INSTANCE.FreeContextBuffer(pPackageInfo.pPkgInfo.getPointer());
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        return packages.toArray(new SecurityPackage[0]);
    }

    public static class SecurityPackage {
        public String name;
        public String comment;
    }
}

