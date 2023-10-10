// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.Structure;

@FieldOrder({ "QueryInterfaceCallback", "AddRefCallback", "ReleaseCallback" })
public class UnknownVTable extends Structure
{
    public QueryInterfaceCallback QueryInterfaceCallback;
    public AddRefCallback AddRefCallback;
    public ReleaseCallback ReleaseCallback;
    
    public static class ByReference extends UnknownVTable implements Structure.ByReference
    {
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
