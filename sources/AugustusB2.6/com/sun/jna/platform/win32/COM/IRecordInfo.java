// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Variant;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Guid;

public interface IRecordInfo extends IUnknown
{
    public static final Guid.IID IID_IRecordInfo = new Guid.IID("{0000002F-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT RecordInit(final WinDef.PVOID p0);
    
    WinNT.HRESULT RecordClear(final WinDef.PVOID p0);
    
    WinNT.HRESULT RecordCopy(final WinDef.PVOID p0, final WinDef.PVOID p1);
    
    WinNT.HRESULT GetGuid(final Guid.GUID p0);
    
    WinNT.HRESULT GetName(final WTypes.BSTR p0);
    
    WinNT.HRESULT GetSize(final WinDef.ULONG p0);
    
    WinNT.HRESULT GetTypeInfo(final ITypeInfo p0);
    
    WinNT.HRESULT GetField(final WinDef.PVOID p0, final WString p1, final Variant.VARIANT p2);
    
    WinNT.HRESULT GetFieldNoCopy(final WinDef.PVOID p0, final WString p1, final Variant.VARIANT p2, final WinDef.PVOID p3);
    
    WinNT.HRESULT PutField(final WinDef.ULONG p0, final WinDef.PVOID p1, final WString p2, final Variant.VARIANT p3);
    
    WinNT.HRESULT PutFieldNoCopy(final WinDef.ULONG p0, final WinDef.PVOID p1, final WString p2, final Variant.VARIANT p3);
    
    WinNT.HRESULT GetFieldNames(final WinDef.ULONG p0, final WTypes.BSTR p1);
    
    WinDef.BOOL IsMatchingType(final IRecordInfo p0);
    
    WinDef.PVOID RecordCreate();
    
    WinNT.HRESULT RecordCreateCopy(final WinDef.PVOID p0, final WinDef.PVOID p1);
    
    WinNT.HRESULT RecordDestroy(final WinDef.PVOID p0);
}
