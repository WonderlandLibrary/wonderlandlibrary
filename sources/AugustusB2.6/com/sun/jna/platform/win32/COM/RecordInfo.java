// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Pointer;

public class RecordInfo extends Unknown implements IRecordInfo
{
    public RecordInfo() {
    }
    
    public RecordInfo(final Pointer pvInstance) {
        super(pvInstance);
    }
    
    @Override
    public WinNT.HRESULT RecordInit(final WinDef.PVOID pvNew) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT RecordClear(final WinDef.PVOID pvExisting) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT RecordCopy(final WinDef.PVOID pvExisting, final WinDef.PVOID pvNew) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetGuid(final Guid.GUID pguid) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetName(final WTypes.BSTR pbstrName) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetSize(final WinDef.ULONG pcbSize) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final ITypeInfo ppTypeInfo) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetField(final WinDef.PVOID pvData, final WString szFieldName, final Variant.VARIANT pvarField) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetFieldNoCopy(final WinDef.PVOID pvData, final WString szFieldName, final Variant.VARIANT pvarField, final WinDef.PVOID ppvDataCArray) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT PutField(final WinDef.ULONG wFlags, final WinDef.PVOID pvData, final WString szFieldName, final Variant.VARIANT pvarField) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT PutFieldNoCopy(final WinDef.ULONG wFlags, final WinDef.PVOID pvData, final WString szFieldName, final Variant.VARIANT pvarField) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT GetFieldNames(final WinDef.ULONG pcNames, final WTypes.BSTR rgBstrNames) {
        return null;
    }
    
    @Override
    public WinDef.BOOL IsMatchingType(final IRecordInfo pRecordInfo) {
        return null;
    }
    
    @Override
    public WinDef.PVOID RecordCreate() {
        return null;
    }
    
    @Override
    public WinNT.HRESULT RecordCreateCopy(final WinDef.PVOID pvSource, final WinDef.PVOID ppvDest) {
        return null;
    }
    
    @Override
    public WinNT.HRESULT RecordDestroy(final WinDef.PVOID pvRecord) {
        return null;
    }
    
    public static class ByReference extends RecordInfo implements Structure.ByReference
    {
    }
}
