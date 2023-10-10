// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@FieldOrder({ "vtbl" })
public class DispatchListener extends Structure
{
    public DispatchVTable.ByReference vtbl;
    
    public DispatchListener(final IDispatchCallback callback) {
        this.vtbl = this.constructVTable();
        this.initVTable(callback);
        super.write();
    }
    
    protected DispatchVTable.ByReference constructVTable() {
        return new DispatchVTable.ByReference();
    }
    
    protected void initVTable(final IDispatchCallback callback) {
        this.vtbl.QueryInterfaceCallback = new DispatchVTable.QueryInterfaceCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final Guid.REFIID refid, final PointerByReference ppvObject) {
                return callback.QueryInterface(refid, ppvObject);
            }
        };
        this.vtbl.AddRefCallback = new DispatchVTable.AddRefCallback() {
            @Override
            public int invoke(final Pointer thisPointer) {
                return callback.AddRef();
            }
        };
        this.vtbl.ReleaseCallback = new DispatchVTable.ReleaseCallback() {
            @Override
            public int invoke(final Pointer thisPointer) {
                return callback.Release();
            }
        };
        this.vtbl.GetTypeInfoCountCallback = new DispatchVTable.GetTypeInfoCountCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final WinDef.UINTByReference pctinfo) {
                return callback.GetTypeInfoCount(pctinfo);
            }
        };
        this.vtbl.GetTypeInfoCallback = new DispatchVTable.GetTypeInfoCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final WinDef.UINT iTInfo, final WinDef.LCID lcid, final PointerByReference ppTInfo) {
                return callback.GetTypeInfo(iTInfo, lcid, ppTInfo);
            }
        };
        this.vtbl.GetIDsOfNamesCallback = new DispatchVTable.GetIDsOfNamesCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final Guid.REFIID riid, final WString[] rgszNames, final int cNames, final WinDef.LCID lcid, final OaIdl.DISPIDByReference rgDispId) {
                return callback.GetIDsOfNames(riid, rgszNames, cNames, lcid, rgDispId);
            }
        };
        this.vtbl.InvokeCallback = new DispatchVTable.InvokeCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
                return callback.Invoke(dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr);
            }
        };
    }
}
