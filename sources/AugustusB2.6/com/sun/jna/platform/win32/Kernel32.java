// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.LastErrorException;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary, WinNT, Wincon
{
    public static final Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int LOAD_LIBRARY_AS_DATAFILE = 2;
    public static final int MAX_PIPE_NAME_LENGTH = 256;
    
    boolean ReadFile(final HANDLE p0, final byte[] p1, final int p2, final IntByReference p3, final WinBase.OVERLAPPED p4);
    
    Pointer LocalFree(final Pointer p0);
    
    Pointer GlobalFree(final Pointer p0);
    
    WinDef.HMODULE GetModuleHandle(final String p0);
    
    void GetSystemTime(final WinBase.SYSTEMTIME p0);
    
    boolean SetSystemTime(final WinBase.SYSTEMTIME p0);
    
    void GetLocalTime(final WinBase.SYSTEMTIME p0);
    
    boolean SetLocalTime(final WinBase.SYSTEMTIME p0);
    
    boolean GetSystemTimes(final WinBase.FILETIME p0, final WinBase.FILETIME p1, final WinBase.FILETIME p2);
    
    int GetTickCount();
    
    long GetTickCount64();
    
    int GetCurrentThreadId();
    
    HANDLE GetCurrentThread();
    
    int GetCurrentProcessId();
    
    HANDLE GetCurrentProcess();
    
    int GetProcessId(final HANDLE p0);
    
    int GetProcessVersion(final int p0);
    
    boolean GetProcessAffinityMask(final HANDLE p0, final BaseTSD.ULONG_PTRByReference p1, final BaseTSD.ULONG_PTRByReference p2);
    
    boolean SetProcessAffinityMask(final HANDLE p0, final BaseTSD.ULONG_PTR p1);
    
    boolean GetExitCodeProcess(final HANDLE p0, final IntByReference p1);
    
    boolean TerminateProcess(final HANDLE p0, final int p1);
    
    int GetLastError();
    
    void SetLastError(final int p0);
    
    int GetDriveType(final String p0);
    
    int FormatMessage(final int p0, final Pointer p1, final int p2, final int p3, final PointerByReference p4, final int p5, final Pointer p6);
    
    HANDLE CreateFile(final String p0, final int p1, final int p2, final WinBase.SECURITY_ATTRIBUTES p3, final int p4, final int p5, final HANDLE p6);
    
    boolean CopyFile(final String p0, final String p1, final boolean p2);
    
    boolean MoveFile(final String p0, final String p1);
    
    boolean MoveFileEx(final String p0, final String p1, final WinDef.DWORD p2);
    
    boolean CreateDirectory(final String p0, final WinBase.SECURITY_ATTRIBUTES p1);
    
    HANDLE CreateIoCompletionPort(final HANDLE p0, final HANDLE p1, final Pointer p2, final int p3);
    
    boolean GetQueuedCompletionStatus(final HANDLE p0, final IntByReference p1, final BaseTSD.ULONG_PTRByReference p2, final PointerByReference p3, final int p4);
    
    boolean PostQueuedCompletionStatus(final HANDLE p0, final int p1, final Pointer p2, final WinBase.OVERLAPPED p3);
    
    int WaitForSingleObject(final HANDLE p0, final int p1);
    
    int WaitForMultipleObjects(final int p0, final HANDLE[] p1, final boolean p2, final int p3);
    
    boolean DuplicateHandle(final HANDLE p0, final HANDLE p1, final HANDLE p2, final HANDLEByReference p3, final int p4, final boolean p5, final int p6);
    
    boolean CloseHandle(final HANDLE p0);
    
    boolean ReadDirectoryChangesW(final HANDLE p0, final FILE_NOTIFY_INFORMATION p1, final int p2, final boolean p3, final int p4, final IntByReference p5, final WinBase.OVERLAPPED p6, final OVERLAPPED_COMPLETION_ROUTINE p7);
    
    int GetShortPathName(final String p0, final char[] p1, final int p2);
    
    Pointer LocalAlloc(final int p0, final int p1);
    
    boolean WriteFile(final HANDLE p0, final byte[] p1, final int p2, final IntByReference p3, final WinBase.OVERLAPPED p4);
    
    boolean FlushFileBuffers(final HANDLE p0);
    
    HANDLE CreateEvent(final WinBase.SECURITY_ATTRIBUTES p0, final boolean p1, final boolean p2, final String p3);
    
    HANDLE OpenEvent(final int p0, final boolean p1, final String p2);
    
    boolean SetEvent(final HANDLE p0);
    
    boolean ResetEvent(final HANDLE p0);
    
    boolean PulseEvent(final HANDLE p0);
    
    HANDLE CreateFileMapping(final HANDLE p0, final WinBase.SECURITY_ATTRIBUTES p1, final int p2, final int p3, final int p4, final String p5);
    
    HANDLE OpenFileMapping(final int p0, final boolean p1, final String p2);
    
    Pointer MapViewOfFile(final HANDLE p0, final int p1, final int p2, final int p3, final int p4);
    
    boolean UnmapViewOfFile(final Pointer p0);
    
    boolean GetComputerName(final char[] p0, final IntByReference p1);
    
    boolean GetComputerNameEx(final int p0, final char[] p1, final IntByReference p2);
    
    HANDLE OpenThread(final int p0, final boolean p1, final int p2);
    
    boolean CreateProcess(final String p0, final String p1, final WinBase.SECURITY_ATTRIBUTES p2, final WinBase.SECURITY_ATTRIBUTES p3, final boolean p4, final WinDef.DWORD p5, final Pointer p6, final String p7, final WinBase.STARTUPINFO p8, final WinBase.PROCESS_INFORMATION p9);
    
    boolean CreateProcessW(final String p0, final char[] p1, final WinBase.SECURITY_ATTRIBUTES p2, final WinBase.SECURITY_ATTRIBUTES p3, final boolean p4, final WinDef.DWORD p5, final Pointer p6, final String p7, final WinBase.STARTUPINFO p8, final WinBase.PROCESS_INFORMATION p9);
    
    HANDLE OpenProcess(final int p0, final boolean p1, final int p2);
    
    boolean QueryFullProcessImageName(final HANDLE p0, final int p1, final char[] p2, final IntByReference p3);
    
    WinDef.DWORD GetTempPath(final WinDef.DWORD p0, final char[] p1);
    
    WinDef.DWORD GetVersion();
    
    boolean GetVersionEx(final OSVERSIONINFO p0);
    
    boolean GetVersionEx(final OSVERSIONINFOEX p0);
    
    boolean VerifyVersionInfoW(final OSVERSIONINFOEX p0, final int p1, final long p2);
    
    long VerSetConditionMask(final long p0, final int p1, final byte p2);
    
    void GetSystemInfo(final WinBase.SYSTEM_INFO p0);
    
    void GetNativeSystemInfo(final WinBase.SYSTEM_INFO p0);
    
    boolean IsWow64Process(final HANDLE p0, final IntByReference p1);
    
    boolean GetLogicalProcessorInformation(final Pointer p0, final WinDef.DWORDByReference p1);
    
    boolean GetLogicalProcessorInformationEx(final int p0, final Pointer p1, final WinDef.DWORDByReference p2);
    
    boolean GlobalMemoryStatusEx(final WinBase.MEMORYSTATUSEX p0);
    
    boolean GetFileInformationByHandleEx(final HANDLE p0, final int p1, final Pointer p2, final WinDef.DWORD p3);
    
    boolean SetFileInformationByHandle(final HANDLE p0, final int p1, final Pointer p2, final WinDef.DWORD p3);
    
    boolean GetFileTime(final HANDLE p0, final WinBase.FILETIME p1, final WinBase.FILETIME p2, final WinBase.FILETIME p3);
    
    int SetFileTime(final HANDLE p0, final WinBase.FILETIME p1, final WinBase.FILETIME p2, final WinBase.FILETIME p3);
    
    boolean SetFileAttributes(final String p0, final WinDef.DWORD p1);
    
    WinDef.DWORD GetLogicalDriveStrings(final WinDef.DWORD p0, final char[] p1);
    
    boolean GetDiskFreeSpace(final String p0, final WinDef.DWORDByReference p1, final WinDef.DWORDByReference p2, final WinDef.DWORDByReference p3, final WinDef.DWORDByReference p4);
    
    boolean GetDiskFreeSpaceEx(final String p0, final LARGE_INTEGER p1, final LARGE_INTEGER p2, final LARGE_INTEGER p3);
    
    boolean DeleteFile(final String p0);
    
    boolean CreatePipe(final HANDLEByReference p0, final HANDLEByReference p1, final WinBase.SECURITY_ATTRIBUTES p2, final int p3);
    
    boolean CallNamedPipe(final String p0, final byte[] p1, final int p2, final byte[] p3, final int p4, final IntByReference p5, final int p6);
    
    boolean ConnectNamedPipe(final HANDLE p0, final WinBase.OVERLAPPED p1);
    
    HANDLE CreateNamedPipe(final String p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final WinBase.SECURITY_ATTRIBUTES p7);
    
    boolean DisconnectNamedPipe(final HANDLE p0);
    
    boolean GetNamedPipeClientComputerName(final HANDLE p0, final char[] p1, final int p2);
    
    boolean GetNamedPipeClientProcessId(final HANDLE p0, final WinDef.ULONGByReference p1);
    
    boolean GetNamedPipeClientSessionId(final HANDLE p0, final WinDef.ULONGByReference p1);
    
    boolean GetNamedPipeHandleState(final HANDLE p0, final IntByReference p1, final IntByReference p2, final IntByReference p3, final IntByReference p4, final char[] p5, final int p6);
    
    boolean GetNamedPipeInfo(final HANDLE p0, final IntByReference p1, final IntByReference p2, final IntByReference p3, final IntByReference p4);
    
    boolean GetNamedPipeServerProcessId(final HANDLE p0, final WinDef.ULONGByReference p1);
    
    boolean GetNamedPipeServerSessionId(final HANDLE p0, final WinDef.ULONGByReference p1);
    
    boolean PeekNamedPipe(final HANDLE p0, final byte[] p1, final int p2, final IntByReference p3, final IntByReference p4, final IntByReference p5);
    
    boolean SetNamedPipeHandleState(final HANDLE p0, final IntByReference p1, final IntByReference p2, final IntByReference p3);
    
    boolean TransactNamedPipe(final HANDLE p0, final byte[] p1, final int p2, final byte[] p3, final int p4, final IntByReference p5, final WinBase.OVERLAPPED p6);
    
    boolean WaitNamedPipe(final String p0, final int p1);
    
    boolean SetHandleInformation(final HANDLE p0, final int p1, final int p2);
    
    int GetFileAttributes(final String p0);
    
    int GetFileType(final HANDLE p0);
    
    boolean DeviceIoControl(final HANDLE p0, final int p1, final Pointer p2, final int p3, final Pointer p4, final int p5, final IntByReference p6, final Pointer p7);
    
    HANDLE CreateToolhelp32Snapshot(final WinDef.DWORD p0, final WinDef.DWORD p1);
    
    boolean Process32First(final HANDLE p0, final Tlhelp32.PROCESSENTRY32 p1);
    
    boolean Process32Next(final HANDLE p0, final Tlhelp32.PROCESSENTRY32 p1);
    
    boolean Thread32First(final HANDLE p0, final Tlhelp32.THREADENTRY32 p1);
    
    boolean Thread32Next(final HANDLE p0, final Tlhelp32.THREADENTRY32 p1);
    
    boolean SetEnvironmentVariable(final String p0, final String p1);
    
    int GetEnvironmentVariable(final String p0, final char[] p1, final int p2);
    
    Pointer GetEnvironmentStrings();
    
    boolean FreeEnvironmentStrings(final Pointer p0);
    
    WinDef.LCID GetSystemDefaultLCID();
    
    WinDef.LCID GetUserDefaultLCID();
    
    int GetPrivateProfileInt(final String p0, final String p1, final int p2, final String p3);
    
    WinDef.DWORD GetPrivateProfileString(final String p0, final String p1, final String p2, final char[] p3, final WinDef.DWORD p4, final String p5);
    
    boolean WritePrivateProfileString(final String p0, final String p1, final String p2, final String p3);
    
    WinDef.DWORD GetPrivateProfileSection(final String p0, final char[] p1, final WinDef.DWORD p2, final String p3);
    
    WinDef.DWORD GetPrivateProfileSectionNames(final char[] p0, final WinDef.DWORD p1, final String p2);
    
    boolean WritePrivateProfileSection(final String p0, final String p1, final String p2);
    
    boolean FileTimeToLocalFileTime(final WinBase.FILETIME p0, final WinBase.FILETIME p1);
    
    boolean SystemTimeToTzSpecificLocalTime(final WinBase.TIME_ZONE_INFORMATION p0, final WinBase.SYSTEMTIME p1, final WinBase.SYSTEMTIME p2);
    
    boolean SystemTimeToFileTime(final WinBase.SYSTEMTIME p0, final WinBase.FILETIME p1);
    
    boolean FileTimeToSystemTime(final WinBase.FILETIME p0, final WinBase.SYSTEMTIME p1);
    
    @Deprecated
    HANDLE CreateRemoteThread(final HANDLE p0, final WinBase.SECURITY_ATTRIBUTES p1, final int p2, final WinBase.FOREIGN_THREAD_START_ROUTINE p3, final Pointer p4, final WinDef.DWORD p5, final Pointer p6);
    
    HANDLE CreateRemoteThread(final HANDLE p0, final WinBase.SECURITY_ATTRIBUTES p1, final int p2, final Pointer p3, final Pointer p4, final int p5, final WinDef.DWORDByReference p6);
    
    boolean WriteProcessMemory(final HANDLE p0, final Pointer p1, final Pointer p2, final int p3, final IntByReference p4);
    
    boolean ReadProcessMemory(final HANDLE p0, final Pointer p1, final Pointer p2, final int p3, final IntByReference p4);
    
    BaseTSD.SIZE_T VirtualQueryEx(final HANDLE p0, final Pointer p1, final MEMORY_BASIC_INFORMATION p2, final BaseTSD.SIZE_T p3);
    
    boolean DefineDosDevice(final int p0, final String p1, final String p2);
    
    int QueryDosDevice(final String p0, final char[] p1, final int p2);
    
    HANDLE FindFirstFile(final String p0, final Pointer p1);
    
    HANDLE FindFirstFileEx(final String p0, final int p1, final Pointer p2, final int p3, final Pointer p4, final WinDef.DWORD p5);
    
    boolean FindNextFile(final HANDLE p0, final Pointer p1);
    
    boolean FindClose(final HANDLE p0);
    
    HANDLE FindFirstVolumeMountPoint(final String p0, final char[] p1, final int p2);
    
    boolean FindNextVolumeMountPoint(final HANDLE p0, final char[] p1, final int p2);
    
    boolean FindVolumeMountPointClose(final HANDLE p0);
    
    boolean GetVolumeNameForVolumeMountPoint(final String p0, final char[] p1, final int p2);
    
    boolean SetVolumeLabel(final String p0, final String p1);
    
    boolean SetVolumeMountPoint(final String p0, final String p1);
    
    boolean DeleteVolumeMountPoint(final String p0);
    
    boolean GetVolumeInformation(final String p0, final char[] p1, final int p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final char[] p6, final int p7);
    
    boolean GetVolumePathName(final String p0, final char[] p1, final int p2);
    
    boolean GetVolumePathNamesForVolumeName(final String p0, final char[] p1, final int p2, final IntByReference p3);
    
    HANDLE FindFirstVolume(final char[] p0, final int p1);
    
    boolean FindNextVolume(final HANDLE p0, final char[] p1, final int p2);
    
    boolean FindVolumeClose(final HANDLE p0);
    
    boolean GetCommState(final HANDLE p0, final WinBase.DCB p1);
    
    boolean GetCommTimeouts(final HANDLE p0, final WinBase.COMMTIMEOUTS p1);
    
    boolean SetCommState(final HANDLE p0, final WinBase.DCB p1);
    
    boolean SetCommTimeouts(final HANDLE p0, final WinBase.COMMTIMEOUTS p1);
    
    boolean ProcessIdToSessionId(final int p0, final IntByReference p1);
    
    WinDef.HMODULE LoadLibraryEx(final String p0, final HANDLE p1, final int p2);
    
    WinDef.HRSRC FindResource(final WinDef.HMODULE p0, final Pointer p1, final Pointer p2);
    
    HANDLE LoadResource(final WinDef.HMODULE p0, final WinDef.HRSRC p1);
    
    Pointer LockResource(final HANDLE p0);
    
    int SizeofResource(final WinDef.HMODULE p0, final HANDLE p1);
    
    boolean FreeLibrary(final WinDef.HMODULE p0);
    
    boolean EnumResourceTypes(final WinDef.HMODULE p0, final WinBase.EnumResTypeProc p1, final Pointer p2);
    
    boolean EnumResourceNames(final WinDef.HMODULE p0, final Pointer p1, final WinBase.EnumResNameProc p2, final Pointer p3);
    
    boolean Module32FirstW(final HANDLE p0, final Tlhelp32.MODULEENTRY32W p1);
    
    boolean Module32NextW(final HANDLE p0, final Tlhelp32.MODULEENTRY32W p1);
    
    int SetErrorMode(final int p0);
    
    Pointer GetProcAddress(final WinDef.HMODULE p0, final int p1) throws LastErrorException;
    
    int SetThreadExecutionState(final int p0);
    
    int ExpandEnvironmentStrings(final String p0, final Pointer p1, final int p2);
    
    boolean GetProcessTimes(final HANDLE p0, final WinBase.FILETIME p1, final WinBase.FILETIME p2, final WinBase.FILETIME p3, final WinBase.FILETIME p4);
    
    boolean GetProcessIoCounters(final HANDLE p0, final IO_COUNTERS p1);
    
    HANDLE CreateMutex(final WinBase.SECURITY_ATTRIBUTES p0, final boolean p1, final String p2);
    
    HANDLE OpenMutex(final int p0, final boolean p1, final String p2);
    
    boolean ReleaseMutex(final HANDLE p0);
    
    void ExitProcess(final int p0);
    
    Pointer VirtualAllocEx(final HANDLE p0, final Pointer p1, final BaseTSD.SIZE_T p2, final int p3, final int p4);
    
    boolean GetExitCodeThread(final HANDLE p0, final IntByReference p1);
    
    boolean VirtualFreeEx(final HANDLE p0, final Pointer p1, final BaseTSD.SIZE_T p2, final int p3);
    
    HRESULT RegisterApplicationRestart(final char[] p0, final int p1);
    
    HRESULT UnregisterApplicationRestart();
    
    HRESULT GetApplicationRestartSettings(final HANDLE p0, final char[] p1, final IntByReference p2, final IntByReference p3);
}
