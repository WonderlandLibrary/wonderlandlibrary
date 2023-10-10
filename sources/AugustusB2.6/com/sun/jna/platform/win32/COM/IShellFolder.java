// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Native;
import com.sun.jna.Function;
import com.sun.jna.platform.win32.ShTypes;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IShellFolder
{
    public static final Guid.IID IID_ISHELLFOLDER = new Guid.IID("{000214E6-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT QueryInterface(final Guid.REFIID p0, final PointerByReference p1);
    
    int AddRef();
    
    int Release();
    
    WinNT.HRESULT ParseDisplayName(final WinDef.HWND p0, final Pointer p1, final String p2, final IntByReference p3, final PointerByReference p4, final IntByReference p5);
    
    WinNT.HRESULT EnumObjects(final WinDef.HWND p0, final int p1, final PointerByReference p2);
    
    WinNT.HRESULT BindToObject(final Pointer p0, final Pointer p1, final Guid.REFIID p2, final PointerByReference p3);
    
    WinNT.HRESULT BindToStorage(final Pointer p0, final Pointer p1, final Guid.REFIID p2, final PointerByReference p3);
    
    WinNT.HRESULT CompareIDs(final WinDef.LPARAM p0, final Pointer p1, final Pointer p2);
    
    WinNT.HRESULT CreateViewObject(final WinDef.HWND p0, final Guid.REFIID p1, final PointerByReference p2);
    
    WinNT.HRESULT GetAttributesOf(final int p0, final Pointer p1, final IntByReference p2);
    
    WinNT.HRESULT GetUIObjectOf(final WinDef.HWND p0, final int p1, final Pointer p2, final Guid.REFIID p3, final IntByReference p4, final PointerByReference p5);
    
    WinNT.HRESULT GetDisplayNameOf(final Pointer p0, final int p1, final ShTypes.STRRET p2);
    
    WinNT.HRESULT SetNameOf(final WinDef.HWND p0, final Pointer p1, final String p2, final int p3, final PointerByReference p4);
    
    public static class Converter
    {
        public static IShellFolder PointerToIShellFolder(final PointerByReference ptr) {
            final Pointer interfacePointer = ptr.getValue();
            final Pointer vTablePointer = interfacePointer.getPointer(0L);
            final Pointer[] vTable = new Pointer[13];
            vTablePointer.read(0L, vTable, 0, 13);
            return new IShellFolder() {
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
                public WinNT.HRESULT ParseDisplayName(final WinDef.HWND hwnd, final Pointer pbc, final String pszDisplayName, final IntByReference pchEaten, final PointerByReference ppidl, final IntByReference pdwAttributes) {
                    final Function f = Function.getFunction(vTable[3], 63);
                    final char[] pszDisplayNameNative = Native.toCharArray(pszDisplayName);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, hwnd, pbc, pszDisplayNameNative, pchEaten, ppidl, pdwAttributes }));
                }
                
                @Override
                public WinNT.HRESULT EnumObjects(final WinDef.HWND hwnd, final int grfFlags, final PointerByReference ppenumIDList) {
                    final Function f = Function.getFunction(vTable[4], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, hwnd, grfFlags, ppenumIDList }));
                }
                
                @Override
                public WinNT.HRESULT BindToObject(final Pointer pidl, final Pointer pbc, final Guid.REFIID riid, final PointerByReference ppv) {
                    final Function f = Function.getFunction(vTable[5], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, pidl, pbc, riid, ppv }));
                }
                
                @Override
                public WinNT.HRESULT BindToStorage(final Pointer pidl, final Pointer pbc, final Guid.REFIID riid, final PointerByReference ppv) {
                    final Function f = Function.getFunction(vTable[6], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, pidl, pbc, riid, ppv }));
                }
                
                @Override
                public WinNT.HRESULT CompareIDs(final WinDef.LPARAM lParam, final Pointer pidl1, final Pointer pidl2) {
                    final Function f = Function.getFunction(vTable[7], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, lParam, pidl1, pidl2 }));
                }
                
                @Override
                public WinNT.HRESULT CreateViewObject(final WinDef.HWND hwndOwner, final Guid.REFIID riid, final PointerByReference ppv) {
                    final Function f = Function.getFunction(vTable[8], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, hwndOwner, riid, ppv }));
                }
                
                @Override
                public WinNT.HRESULT GetAttributesOf(final int cidl, final Pointer apidl, final IntByReference rgfInOut) {
                    final Function f = Function.getFunction(vTable[9], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, cidl, apidl, rgfInOut }));
                }
                
                @Override
                public WinNT.HRESULT GetUIObjectOf(final WinDef.HWND hwndOwner, final int cidl, final Pointer apidl, final Guid.REFIID riid, final IntByReference rgfReserved, final PointerByReference ppv) {
                    final Function f = Function.getFunction(vTable[10], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, hwndOwner, cidl, apidl, riid, rgfReserved, ppv }));
                }
                
                @Override
                public WinNT.HRESULT GetDisplayNameOf(final Pointer pidl, final int flags, final ShTypes.STRRET pName) {
                    final Function f = Function.getFunction(vTable[11], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, pidl, flags, pName }));
                }
                
                @Override
                public WinNT.HRESULT SetNameOf(final WinDef.HWND hwnd, final Pointer pidl, final String pszName, final int uFlags, final PointerByReference ppidlOut) {
                    final Function f = Function.getFunction(vTable[12], 63);
                    return new WinNT.HRESULT(f.invokeInt(new Object[] { interfacePointer, hwnd, pidl, pszName, uFlags, ppidlOut }));
                }
            };
        }
    }
}
