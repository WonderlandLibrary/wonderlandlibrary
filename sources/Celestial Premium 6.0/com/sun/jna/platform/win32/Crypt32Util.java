/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Crypt32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinCrypt;
import com.sun.jna.ptr.PointerByReference;

public abstract class Crypt32Util {
    public static byte[] cryptProtectData(byte[] data) {
        return Crypt32Util.cryptProtectData(data, 0);
    }

    public static byte[] cryptProtectData(byte[] data, int flags) {
        return Crypt32Util.cryptProtectData(data, null, flags, "", null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] cryptProtectData(byte[] data, byte[] entropy, int flags, String description, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        WinCrypt.DATA_BLOB pDataProtected = new WinCrypt.DATA_BLOB();
        WinCrypt.DATA_BLOB pEntropy = entropy == null ? null : new WinCrypt.DATA_BLOB(entropy);
        try {
            if (!Crypt32.INSTANCE.CryptProtectData(pDataIn, description, pEntropy, null, prompt, flags, pDataProtected)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            byte[] arrby = pDataProtected.getData();
            return arrby;
        }
        finally {
            if (pDataProtected.pbData != null) {
                Kernel32.INSTANCE.LocalFree(pDataProtected.pbData);
            }
        }
    }

    public static byte[] cryptUnprotectData(byte[] data) {
        return Crypt32Util.cryptUnprotectData(data, 0);
    }

    public static byte[] cryptUnprotectData(byte[] data, int flags) {
        return Crypt32Util.cryptUnprotectData(data, null, flags, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] cryptUnprotectData(byte[] data, byte[] entropy, int flags, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        WinCrypt.DATA_BLOB pDataUnprotected = new WinCrypt.DATA_BLOB();
        WinCrypt.DATA_BLOB pEntropy = entropy == null ? null : new WinCrypt.DATA_BLOB(entropy);
        PointerByReference pDescription = new PointerByReference();
        try {
            if (!Crypt32.INSTANCE.CryptUnprotectData(pDataIn, pDescription, pEntropy, null, prompt, flags, pDataUnprotected)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            byte[] arrby = pDataUnprotected.getData();
            return arrby;
        }
        finally {
            if (pDataUnprotected.pbData != null) {
                Kernel32.INSTANCE.LocalFree(pDataUnprotected.pbData);
            }
            if (pDescription.getValue() != null) {
                Kernel32.INSTANCE.LocalFree(pDescription.getValue());
            }
        }
    }
}

