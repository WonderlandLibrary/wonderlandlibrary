// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.LastErrorException;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT;

public abstract class COMUtils
{
    public static final int S_OK = 0;
    public static final int S_FALSE = 1;
    public static final int E_UNEXPECTED = -2147418113;
    
    public static boolean SUCCEEDED(final WinNT.HRESULT hr) {
        return SUCCEEDED(hr.intValue());
    }
    
    public static boolean SUCCEEDED(final int hr) {
        return hr >= 0;
    }
    
    public static boolean FAILED(final WinNT.HRESULT hr) {
        return FAILED(hr.intValue());
    }
    
    public static boolean FAILED(final int hr) {
        return hr < 0;
    }
    
    public static void checkRC(final WinNT.HRESULT hr) {
        if (FAILED(hr)) {
            String formatMessage;
            try {
                formatMessage = Kernel32Util.formatMessage(hr) + "(HRESULT: " + Integer.toHexString(hr.intValue()) + ")";
            }
            catch (LastErrorException ex) {
                formatMessage = "(HRESULT: " + Integer.toHexString(hr.intValue()) + ")";
            }
            throw new COMException(formatMessage, hr);
        }
    }
    
    public static void checkRC(final WinNT.HRESULT hr, final OaIdl.EXCEPINFO pExcepInfo, final IntByReference puArgErr) {
        final COMException resultException = null;
        if (FAILED(hr)) {
            final StringBuilder formatMessage = new StringBuilder();
            Integer errorArg = null;
            Integer wCode = null;
            Integer scode = null;
            String description = null;
            String helpFile = null;
            Integer helpCtx = null;
            String source = null;
            if (puArgErr != null) {
                errorArg = puArgErr.getValue();
            }
            try {
                formatMessage.append(Kernel32Util.formatMessage(hr));
            }
            catch (LastErrorException ex) {}
            formatMessage.append("(HRESULT: ");
            formatMessage.append(Integer.toHexString(hr.intValue()));
            formatMessage.append(")");
            if (pExcepInfo != null) {
                wCode = pExcepInfo.wCode.intValue();
                scode = pExcepInfo.scode.intValue();
                helpCtx = pExcepInfo.dwHelpContext.intValue();
                if (pExcepInfo.bstrSource != null) {
                    source = pExcepInfo.bstrSource.getValue();
                    formatMessage.append("\nSource:      ");
                    formatMessage.append(source);
                }
                if (pExcepInfo.bstrDescription != null) {
                    description = pExcepInfo.bstrDescription.getValue();
                    formatMessage.append("\nDescription: ");
                    formatMessage.append(description);
                }
                if (pExcepInfo.bstrHelpFile != null) {
                    helpFile = pExcepInfo.bstrHelpFile.getValue();
                }
            }
            throw new COMInvokeException(formatMessage.toString(), hr, errorArg, description, helpCtx, helpFile, scode, source, wCode);
        }
        if (pExcepInfo != null) {
            if (pExcepInfo.bstrSource != null) {
                OleAuto.INSTANCE.SysFreeString(pExcepInfo.bstrSource);
            }
            if (pExcepInfo.bstrDescription != null) {
                OleAuto.INSTANCE.SysFreeString(pExcepInfo.bstrDescription);
            }
            if (pExcepInfo.bstrHelpFile != null) {
                OleAuto.INSTANCE.SysFreeString(pExcepInfo.bstrHelpFile);
            }
        }
        if (resultException != null) {
            throw resultException;
        }
    }
    
    public static ArrayList<COMInfo> getAllCOMInfoOnSystem() {
        WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
        WinReg.HKEYByReference phkResult2 = new WinReg.HKEYByReference();
        final ArrayList<COMInfo> comInfos = new ArrayList<COMInfo>();
        try {
            phkResult = Advapi32Util.registryGetKey(WinReg.HKEY_CLASSES_ROOT, "CLSID", 131097);
            final Advapi32Util.InfoKey infoKey = Advapi32Util.registryQueryInfoKey(phkResult.getValue(), 131097);
            for (int i = 0; i < infoKey.lpcSubKeys.getValue(); ++i) {
                final Advapi32Util.EnumKey enumKey = Advapi32Util.registryRegEnumKey(phkResult.getValue(), i);
                final String subKey = Native.toString(enumKey.lpName);
                final COMInfo comInfo = new COMInfo(subKey);
                phkResult2 = Advapi32Util.registryGetKey(phkResult.getValue(), subKey, 131097);
                final Advapi32Util.InfoKey infoKey2 = Advapi32Util.registryQueryInfoKey(phkResult2.getValue(), 131097);
                for (int y = 0; y < infoKey2.lpcSubKeys.getValue(); ++y) {
                    final Advapi32Util.EnumKey enumKey2 = Advapi32Util.registryRegEnumKey(phkResult2.getValue(), y);
                    final String subKey2 = Native.toString(enumKey2.lpName);
                    if (subKey2.equals("InprocHandler32")) {
                        comInfo.inprocHandler32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("InprocServer32")) {
                        comInfo.inprocServer32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("LocalServer32")) {
                        comInfo.localServer32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("ProgID")) {
                        comInfo.progID = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("TypeLib")) {
                        comInfo.typeLib = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                }
                Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
                comInfos.add(comInfo);
            }
        }
        finally {
            Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
            Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
        }
        return comInfos;
    }
    
    public static boolean comIsInitialized() {
        final WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, 0);
        if (hr.equals(W32Errors.S_OK)) {
            Ole32.INSTANCE.CoUninitialize();
            return false;
        }
        if (hr.equals(W32Errors.S_FALSE)) {
            Ole32.INSTANCE.CoUninitialize();
            return true;
        }
        if (hr.intValue() == -2147417850) {
            return true;
        }
        checkRC(hr);
        return false;
    }
    
    public static class COMInfo
    {
        public String clsid;
        public String inprocHandler32;
        public String inprocServer32;
        public String localServer32;
        public String progID;
        public String typeLib;
        
        public COMInfo() {
        }
        
        public COMInfo(final String clsid) {
            this.clsid = clsid;
        }
    }
}
