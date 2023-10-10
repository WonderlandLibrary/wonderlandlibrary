// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class VersionUtil
{
    public static VerRsrc.VS_FIXEDFILEINFO getFileVersionInfo(final String filePath) {
        final IntByReference dwDummy = new IntByReference();
        final int versionLength = Version.INSTANCE.GetFileVersionInfoSize(filePath, dwDummy);
        if (versionLength == 0) {
            throw new Win32Exception(Native.getLastError());
        }
        final Pointer lpData = new Memory(versionLength);
        final PointerByReference lplpBuffer = new PointerByReference();
        if (!Version.INSTANCE.GetFileVersionInfo(filePath, 0, versionLength, lpData)) {
            throw new Win32Exception(Native.getLastError());
        }
        final IntByReference puLen = new IntByReference();
        if (!Version.INSTANCE.VerQueryValue(lpData, "\\", lplpBuffer, puLen)) {
            throw new UnsupportedOperationException("Unable to extract version info from the file: \"" + filePath + "\"");
        }
        final VerRsrc.VS_FIXEDFILEINFO fileInfo = new VerRsrc.VS_FIXEDFILEINFO(lplpBuffer.getValue());
        fileInfo.read();
        return fileInfo;
    }
}
