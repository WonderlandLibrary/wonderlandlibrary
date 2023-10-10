// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import com.sun.jna.Pointer;

public abstract class Crypt32Util
{
    public static byte[] cryptProtectData(final byte[] data) {
        return cryptProtectData(data, 0);
    }
    
    public static byte[] cryptProtectData(final byte[] data, final int flags) {
        return cryptProtectData(data, null, flags, "", null);
    }
    
    public static byte[] cryptProtectData(final byte[] data, final byte[] entropy, final int flags, final String description, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        final WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        final WinCrypt.DATA_BLOB pDataProtected = new WinCrypt.DATA_BLOB();
        final WinCrypt.DATA_BLOB pEntropy = (entropy == null) ? null : new WinCrypt.DATA_BLOB(entropy);
        Win32Exception err = null;
        byte[] protectedData = null;
        try {
            if (!Crypt32.INSTANCE.CryptProtectData(pDataIn, description, pEntropy, null, prompt, flags, pDataProtected)) {
                err = new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            else {
                protectedData = pDataProtected.getData();
            }
        }
        finally {
            if (pDataIn.pbData != null) {
                pDataIn.pbData.clear(pDataIn.cbData);
            }
            if (pEntropy != null && pEntropy.pbData != null) {
                pEntropy.pbData.clear(pEntropy.cbData);
            }
            if (pDataProtected.pbData != null) {
                pDataProtected.pbData.clear(pDataProtected.cbData);
                try {
                    Kernel32Util.freeLocalMemory(pDataProtected.pbData);
                }
                catch (Win32Exception e) {
                    if (err == null) {
                        err = e;
                    }
                    else {
                        err.addSuppressedReflected(e);
                    }
                }
            }
        }
        if (err != null) {
            if (protectedData != null) {
                Arrays.fill(protectedData, (byte)0);
            }
            throw err;
        }
        return protectedData;
    }
    
    public static byte[] cryptUnprotectData(final byte[] data) {
        return cryptUnprotectData(data, 0);
    }
    
    public static byte[] cryptUnprotectData(final byte[] data, final int flags) {
        return cryptUnprotectData(data, null, flags, null);
    }
    
    public static byte[] cryptUnprotectData(final byte[] data, final byte[] entropy, final int flags, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        final WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        final WinCrypt.DATA_BLOB pDataUnprotected = new WinCrypt.DATA_BLOB();
        final WinCrypt.DATA_BLOB pEntropy = (entropy == null) ? null : new WinCrypt.DATA_BLOB(entropy);
        Win32Exception err = null;
        byte[] unProtectedData = null;
        try {
            if (!Crypt32.INSTANCE.CryptUnprotectData(pDataIn, null, pEntropy, null, prompt, flags, pDataUnprotected)) {
                err = new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            else {
                unProtectedData = pDataUnprotected.getData();
            }
        }
        finally {
            if (pDataIn.pbData != null) {
                pDataIn.pbData.clear(pDataIn.cbData);
            }
            if (pEntropy != null && pEntropy.pbData != null) {
                pEntropy.pbData.clear(pEntropy.cbData);
            }
            if (pDataUnprotected.pbData != null) {
                pDataUnprotected.pbData.clear(pDataUnprotected.cbData);
                try {
                    Kernel32Util.freeLocalMemory(pDataUnprotected.pbData);
                }
                catch (Win32Exception e) {
                    if (err == null) {
                        err = e;
                    }
                    else {
                        err.addSuppressedReflected(e);
                    }
                }
            }
        }
        if (err != null) {
            if (unProtectedData != null) {
                Arrays.fill(unProtectedData, (byte)0);
            }
            throw err;
        }
        return unProtectedData;
    }
    
    public static String CertNameToStr(final int dwCertEncodingType, final int dwStrType, final WinCrypt.DATA_BLOB pName) {
        final int charToBytes = Boolean.getBoolean("w32.ascii") ? 1 : Native.WCHAR_SIZE;
        final int requiredSize = Crypt32.INSTANCE.CertNameToStr(dwCertEncodingType, pName, dwStrType, Pointer.NULL, 0);
        final Memory mem = new Memory(requiredSize * charToBytes);
        final int resultBytes = Crypt32.INSTANCE.CertNameToStr(dwCertEncodingType, pName, dwStrType, mem, requiredSize);
        assert resultBytes == requiredSize;
        if (Boolean.getBoolean("w32.ascii")) {
            return mem.getString(0L);
        }
        return mem.getWideString(0L);
    }
}
