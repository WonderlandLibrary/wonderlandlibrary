// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Psapi extends StdCallLibrary
{
    public static final Psapi INSTANCE = Native.load("psapi", Psapi.class, W32APIOptions.DEFAULT_OPTIONS);
    
    int GetModuleFileNameExA(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final byte[] p2, final int p3);
    
    int GetModuleFileNameExW(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final char[] p2, final int p3);
    
    int GetModuleFileNameEx(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final Pointer p2, final int p3);
    
    boolean EnumProcessModules(final WinNT.HANDLE p0, final WinDef.HMODULE[] p1, final int p2, final IntByReference p3);
    
    boolean GetModuleInformation(final WinNT.HANDLE p0, final WinDef.HMODULE p1, final MODULEINFO p2, final int p3);
    
    int GetProcessImageFileName(final WinNT.HANDLE p0, final char[] p1, final int p2);
    
    boolean GetPerformanceInfo(final PERFORMANCE_INFORMATION p0, final int p1);
    
    boolean EnumProcesses(final int[] p0, final int p1, final IntByReference p2);
    
    @FieldOrder({ "lpBaseOfDll", "SizeOfImage", "EntryPoint" })
    public static class MODULEINFO extends Structure
    {
        public Pointer EntryPoint;
        public Pointer lpBaseOfDll;
        public int SizeOfImage;
    }
    
    @FieldOrder({ "cb", "CommitTotal", "CommitLimit", "CommitPeak", "PhysicalTotal", "PhysicalAvailable", "SystemCache", "KernelTotal", "KernelPaged", "KernelNonpaged", "PageSize", "HandleCount", "ProcessCount", "ThreadCount" })
    public static class PERFORMANCE_INFORMATION extends Structure
    {
        public WinDef.DWORD cb;
        public BaseTSD.SIZE_T CommitTotal;
        public BaseTSD.SIZE_T CommitLimit;
        public BaseTSD.SIZE_T CommitPeak;
        public BaseTSD.SIZE_T PhysicalTotal;
        public BaseTSD.SIZE_T PhysicalAvailable;
        public BaseTSD.SIZE_T SystemCache;
        public BaseTSD.SIZE_T KernelTotal;
        public BaseTSD.SIZE_T KernelPaged;
        public BaseTSD.SIZE_T KernelNonpaged;
        public BaseTSD.SIZE_T PageSize;
        public WinDef.DWORD HandleCount;
        public WinDef.DWORD ProcessCount;
        public WinDef.DWORD ThreadCount;
    }
}
