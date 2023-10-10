// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Pointer;

public class RunningObjectTable extends Unknown implements IRunningObjectTable
{
    public RunningObjectTable() {
    }
    
    public RunningObjectTable(final Pointer pointer) {
        super(pointer);
    }
    
    @Override
    public WinNT.HRESULT Register(final WinDef.DWORD grfFlags, final Pointer punkObject, final Pointer pmkObjectName, final WinDef.DWORDByReference pdwRegister) {
        final int vTableId = 3;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), grfFlags, punkObject, pmkObjectName, pdwRegister }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT Revoke(final WinDef.DWORD dwRegister) {
        final int vTableId = 4;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), dwRegister }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT IsRunning(final Pointer pmkObjectName) {
        final int vTableId = 5;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer(), pmkObjectName }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT GetObject(final Pointer pmkObjectName, final PointerByReference ppunkObject) {
        final int vTableId = 6;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), pmkObjectName, ppunkObject }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT NoteChangeTime(final WinDef.DWORD dwRegister, final WinBase.FILETIME pfiletime) {
        final int vTableId = 7;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(7, new Object[] { this.getPointer(), dwRegister, pfiletime }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT GetTimeOfLastChange(final Pointer pmkObjectName, final WinBase.FILETIME.ByReference pfiletime) {
        final int vTableId = 8;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(8, new Object[] { this.getPointer(), pmkObjectName, pfiletime }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT EnumRunning(final PointerByReference ppenumMoniker) {
        final int vTableId = 9;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(9, new Object[] { this.getPointer(), ppenumMoniker }, WinNT.HRESULT.class);
        return hr;
    }
    
    public static class ByReference extends RunningObjectTable implements Structure.ByReference
    {
    }
}
