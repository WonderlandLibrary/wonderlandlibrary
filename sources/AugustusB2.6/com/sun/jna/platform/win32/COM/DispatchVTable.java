// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.Structure;

@FieldOrder({ "QueryInterfaceCallback", "AddRefCallback", "ReleaseCallback", "GetTypeInfoCountCallback", "GetTypeInfoCallback", "GetIDsOfNamesCallback", "InvokeCallback" })
public class DispatchVTable extends Structure
{
    public QueryInterfaceCallback QueryInterfaceCallback;
    public AddRefCallback AddRefCallback;
    public ReleaseCallback ReleaseCallback;
    public GetTypeInfoCountCallback GetTypeInfoCountCallback;
    public GetTypeInfoCallback GetTypeInfoCallback;
    public GetIDsOfNamesCallback GetIDsOfNamesCallback;
    public InvokeCallback InvokeCallback;
    
    public static class ByReference extends DispatchVTable implements Structure.ByReference
    {
    }
    
    public interface InvokeCallback extends StdCallLibrary.StdCallCallback
    {
        WinNT.HRESULT invoke(final Pointer p0, final OaIdl.DISPID p1, final Guid.REFIID p2, final WinDef.LCID p3, final WinDef.WORD p4, final OleAuto.DISPPARAMS.ByReference p5, final Variant.VARIANT.ByReference p6, final OaIdl.EXCEPINFO.ByReference p7, final IntByReference p8);
    }
    
    public interface GetIDsOfNamesCallback extends StdCallLibrary.StdCallCallback
    {
        WinNT.HRESULT invoke(final Pointer p0, final Guid.REFIID p1, final WString[] p2, final int p3, final WinDef.LCID p4, final OaIdl.DISPIDByReference p5);
    }
    
    public interface GetTypeInfoCallback extends StdCallLibrary.StdCallCallback
    {
        WinNT.HRESULT invoke(final Pointer p0, final WinDef.UINT p1, final WinDef.LCID p2, final PointerByReference p3);
    }
    
    public interface GetTypeInfoCountCallback extends StdCallLibrary.StdCallCallback
    {
        WinNT.HRESULT invoke(final Pointer p0, final WinDef.UINTByReference p1);
    }
    
    public interface ReleaseCallback extends StdCallLibrary.StdCallCallback
    {
        int invoke(final Pointer p0);
    }
    
    public interface AddRefCallback extends StdCallLibrary.StdCallCallback
    {
        int invoke(final Pointer p0);
    }
    
    public interface QueryInterfaceCallback extends StdCallLibrary.StdCallCallback
    {
        WinNT.HRESULT invoke(final Pointer p0, final Guid.REFIID p1, final PointerByReference p2);
    }
}
