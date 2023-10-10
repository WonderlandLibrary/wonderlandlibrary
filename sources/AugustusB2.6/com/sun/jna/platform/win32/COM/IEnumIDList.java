// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IEnumIDList
{
    public static final Guid.IID IID_IEnumIDList = new Guid.IID("{000214F2-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT QueryInterface(final Guid.REFIID p0, final PointerByReference p1);
    
    int AddRef();
    
    int Release();
    
    WinNT.HRESULT Next(final int p0, final PointerByReference p1, final IntByReference p2);
    
    WinNT.HRESULT Skip(final int p0);
    
    WinNT.HRESULT Reset();
    
    WinNT.HRESULT Clone(final PointerByReference p0);
    
    public static class Converter
    {
        public static IEnumIDList PointerToIEnumIDList(final PointerByReference ptr) {
            final Pointer interfacePointer = ptr.getValue();
            final Pointer vTablePointer = interfacePointer.getPointer(0L);
            final Pointer[] vTable = new Pointer[7];
            vTablePointer.read(0L, vTable, 0, 7);
            return new IEnumIDList() {
                @Override
                public WinNT.HRESULT QueryInterface(final Guid.REFIID byValue, final PointerByReference pointerByReference) {
                    final Function f = Function.getFunction(vTable[0], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, byValue, pointerByReference }));
                }
                
                @Override
                public int AddRef() {
                    final Function f = Function.getFunction(vTable[1], 63);
                    return f.invokeInt(new Object[] { interfacePointer });
                }
                
                @Override
                public int Release() {
                    final Function f = Function.getFunction(vTable[2], 63);
                    return f.invokeInt(new Object[] { interfacePointer });
                }
                
                @Override
                public WinNT.HRESULT Next(final int celt, final PointerByReference rgelt, final IntByReference pceltFetched) {
                    final Function f = Function.getFunction(vTable[3], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, celt, rgelt, pceltFetched }));
                }
                
                @Override
                public WinNT.HRESULT Skip(final int celt) {
                    final Function f = Function.getFunction(vTable[4], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, celt }));
                }
                
                @Override
                public WinNT.HRESULT Reset() {
                    final Function f = Function.getFunction(vTable[5], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer }));
                }
                
                @Override
                public WinNT.HRESULT Clone(final PointerByReference ppenum) {
                    final Function f = Function.getFunction(vTable[6], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, ppenum }));
                }
            };
        }
    }
}
