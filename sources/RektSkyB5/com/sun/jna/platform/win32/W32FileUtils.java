/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.WString;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShellAPI;
import java.io.File;
import java.io.IOException;

public class W32FileUtils
extends FileUtils {
    public boolean hasTrash() {
        return true;
    }

    public void moveToTrash(File[] files) throws IOException {
        Shell32 shell = Shell32.INSTANCE;
        ShellAPI.SHFILEOPSTRUCT fileop = new ShellAPI.SHFILEOPSTRUCT();
        fileop.wFunc = 3;
        String[] paths = new String[files.length];
        for (int i2 = 0; i2 < paths.length; ++i2) {
            paths[i2] = files[i2].getAbsolutePath();
        }
        fileop.pFrom = new WString(fileop.encodePaths(paths));
        fileop.fFlags = (short)1620;
        int ret = shell.SHFileOperation(fileop);
        if (ret != 0) {
            throw new IOException("Move to trash failed: " + fileop.pFrom + ": " + Kernel32Util.formatMessageFromLastErrorCode(ret));
        }
        if (fileop.fAnyOperationsAborted) {
            throw new IOException("Move to trash aborted");
        }
    }
}

