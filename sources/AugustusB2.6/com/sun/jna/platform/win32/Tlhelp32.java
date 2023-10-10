// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Tlhelp32
{
    public static final WinDef.DWORD TH32CS_SNAPHEAPLIST = new WinDef.DWORD(1L);
    public static final WinDef.DWORD TH32CS_SNAPPROCESS = new WinDef.DWORD(2L);
    public static final WinDef.DWORD TH32CS_SNAPTHREAD = new WinDef.DWORD(4L);
    public static final WinDef.DWORD TH32CS_SNAPMODULE = new WinDef.DWORD(8L);
    public static final WinDef.DWORD TH32CS_SNAPMODULE32 = new WinDef.DWORD(16L);
    public static final WinDef.DWORD TH32CS_SNAPALL = new WinDef.DWORD((long)(Tlhelp32.TH32CS_SNAPHEAPLIST.intValue() | Tlhelp32.TH32CS_SNAPPROCESS.intValue() | Tlhelp32.TH32CS_SNAPTHREAD.intValue() | Tlhelp32.TH32CS_SNAPMODULE.intValue()));
    public static final WinDef.DWORD TH32CS_INHERIT = new WinDef.DWORD(-2147483648L);
    public static final int MAX_MODULE_NAME32 = 255;
    
    @FieldOrder({ "dwSize", "cntUsage", "th32ProcessID", "th32DefaultHeapID", "th32ModuleID", "cntThreads", "th32ParentProcessID", "pcPriClassBase", "dwFlags", "szExeFile" })
    public static class PROCESSENTRY32 extends Structure
    {
        public WinDef.DWORD dwSize;
        public WinDef.DWORD cntUsage;
        public WinDef.DWORD th32ProcessID;
        public BaseTSD.ULONG_PTR th32DefaultHeapID;
        public WinDef.DWORD th32ModuleID;
        public WinDef.DWORD cntThreads;
        public WinDef.DWORD th32ParentProcessID;
        public WinDef.LONG pcPriClassBase;
        public WinDef.DWORD dwFlags;
        public char[] szExeFile;
        
        public PROCESSENTRY32() {
            this.szExeFile = new char[260];
            this.dwSize = new WinDef.DWORD((long)this.size());
        }
        
        public PROCESSENTRY32(final Pointer memory) {
            super(memory);
            this.szExeFile = new char[260];
            this.read();
        }
        
        public static class ByReference extends PROCESSENTRY32 implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "dwSize", "cntUsage", "th32ThreadID", "th32OwnerProcessID", "tpBasePri", "tpDeltaPri", "dwFlags" })
    public static class THREADENTRY32 extends Structure
    {
        public int dwSize;
        public int cntUsage;
        public int th32ThreadID;
        public int th32OwnerProcessID;
        public NativeLong tpBasePri;
        public NativeLong tpDeltaPri;
        public int dwFlags;
        
        public THREADENTRY32() {
            this.dwSize = this.size();
        }
        
        public THREADENTRY32(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends THREADENTRY32 implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "dwSize", "th32ModuleID", "th32ProcessID", "GlblcntUsage", "ProccntUsage", "modBaseAddr", "modBaseSize", "hModule", "szModule", "szExePath" })
    public static class MODULEENTRY32W extends Structure
    {
        public WinDef.DWORD dwSize;
        public WinDef.DWORD th32ModuleID;
        public WinDef.DWORD th32ProcessID;
        public WinDef.DWORD GlblcntUsage;
        public WinDef.DWORD ProccntUsage;
        public Pointer modBaseAddr;
        public WinDef.DWORD modBaseSize;
        public WinDef.HMODULE hModule;
        public char[] szModule;
        public char[] szExePath;
        
        public MODULEENTRY32W() {
            this.szModule = new char[256];
            this.szExePath = new char[260];
            this.dwSize = new WinDef.DWORD((long)this.size());
        }
        
        public MODULEENTRY32W(final Pointer memory) {
            super(memory);
            this.szModule = new char[256];
            this.szExePath = new char[260];
            this.read();
        }
        
        public String szModule() {
            return Native.toString(this.szModule);
        }
        
        public String szExePath() {
            return Native.toString(this.szExePath);
        }
        
        public static class ByReference extends MODULEENTRY32W implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
