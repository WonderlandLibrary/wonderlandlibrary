// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.windows;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.Structure;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;

public interface NtDll extends com.sun.jna.platform.win32.NtDll
{
    public static final NtDll INSTANCE = Native.load("NtDll", NtDll.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int PROCESS_BASIC_INFORMATION = 0;
    
    int NtQueryInformationProcess(final WinNT.HANDLE p0, final int p1, final Pointer p2, final int p3, final IntByReference p4);
    
    @FieldOrder({ "Reserved1", "PebBaseAddress", "Reserved2" })
    public static class PROCESS_BASIC_INFORMATION extends Structure
    {
        public Pointer Reserved1;
        public Pointer PebBaseAddress;
        public Pointer[] Reserved2;
        
        public PROCESS_BASIC_INFORMATION() {
            this.Reserved2 = new Pointer[4];
        }
    }
    
    @FieldOrder({ "pad", "pad2", "ProcessParameters" })
    public static class PEB extends Structure
    {
        public byte[] pad;
        public Pointer[] pad2;
        public Pointer ProcessParameters;
        
        public PEB() {
            this.pad = new byte[4];
            this.pad2 = new Pointer[3];
        }
    }
    
    @FieldOrder({ "MaximumLength", "Length", "Flags", "DebugFlags", "ConsoleHandle", "ConsoleFlags", "StandardInput", "StandardOutput", "StandardError", "CurrentDirectory", "DllPath", "ImagePathName", "CommandLine", "Environment", "StartingX", "StartingY", "CountX", "CountY", "CountCharsX", "CountCharsY", "FillAttribute", "WindowFlags", "ShowWindowFlags", "WindowTitle", "DesktopInfo", "ShellInfo", "RuntimeData", "CurrentDirectories", "EnvironmentSize", "EnvironmentVersion", "PackageDependencyData", "ProcessGroupId", "LoaderThreads", "RedirectionDllName", "HeapPartitionName", "DefaultThreadpoolCpuSetMasks", "DefaultThreadpoolCpuSetMaskCount" })
    public static class RTL_USER_PROCESS_PARAMETERS extends Structure
    {
        public int MaximumLength;
        public int Length;
        public int Flags;
        public int DebugFlags;
        public WinNT.HANDLE ConsoleHandle;
        public int ConsoleFlags;
        public WinNT.HANDLE StandardInput;
        public WinNT.HANDLE StandardOutput;
        public WinNT.HANDLE StandardError;
        public CURDIR CurrentDirectory;
        public UNICODE_STRING DllPath;
        public UNICODE_STRING ImagePathName;
        public UNICODE_STRING CommandLine;
        public Pointer Environment;
        public int StartingX;
        public int StartingY;
        public int CountX;
        public int CountY;
        public int CountCharsX;
        public int CountCharsY;
        public int FillAttribute;
        public int WindowFlags;
        public int ShowWindowFlags;
        public UNICODE_STRING WindowTitle;
        public UNICODE_STRING DesktopInfo;
        public UNICODE_STRING ShellInfo;
        public UNICODE_STRING RuntimeData;
        public RTL_DRIVE_LETTER_CURDIR[] CurrentDirectories;
        public BaseTSD.ULONG_PTR EnvironmentSize;
        public BaseTSD.ULONG_PTR EnvironmentVersion;
        public Pointer PackageDependencyData;
        public int ProcessGroupId;
        public int LoaderThreads;
        public UNICODE_STRING RedirectionDllName;
        public UNICODE_STRING HeapPartitionName;
        public BaseTSD.ULONG_PTR DefaultThreadpoolCpuSetMasks;
        public int DefaultThreadpoolCpuSetMaskCount;
        
        public RTL_USER_PROCESS_PARAMETERS() {
            this.CurrentDirectories = new RTL_DRIVE_LETTER_CURDIR[32];
        }
    }
    
    @FieldOrder({ "Length", "MaximumLength", "Buffer" })
    public static class UNICODE_STRING extends Structure
    {
        public short Length;
        public short MaximumLength;
        public Pointer Buffer;
    }
    
    @FieldOrder({ "Length", "MaximumLength", "Buffer" })
    public static class STRING extends Structure
    {
        public short Length;
        public short MaximumLength;
        public Pointer Buffer;
    }
    
    @FieldOrder({ "DosPath", "Handle" })
    public static class CURDIR extends Structure
    {
        public UNICODE_STRING DosPath;
        public Pointer Handle;
    }
    
    @FieldOrder({ "Flags", "Length", "TimeStamp", "DosPath" })
    public static class RTL_DRIVE_LETTER_CURDIR extends Structure
    {
        public short Flags;
        public short Length;
        public int TimeStamp;
        public STRING DosPath;
    }
}
