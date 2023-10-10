// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Pdh extends StdCallLibrary
{
    public static final Pdh INSTANCE = Native.load("Pdh", Pdh.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int PDH_MAX_COUNTER_NAME = 1024;
    public static final int PDH_MAX_INSTANCE_NAME = 1024;
    public static final int PDH_MAX_COUNTER_PATH = 2048;
    public static final int PDH_MAX_DATASOURCE_PATH = 1024;
    public static final int PDH_MORE_DATA = -2147481646;
    public static final int PDH_INSUFFICIENT_BUFFER = -1073738814;
    public static final int PDH_INVALID_ARGUMENT = -1073738819;
    public static final int PDH_MEMORY_ALLOCATION_FAILURE = -1073738821;
    public static final int PDH_CSTATUS_NO_MACHINE = -2147481648;
    public static final int PDH_CSTATUS_NO_OBJECT = -1073738824;
    public static final int PDH_CVERSION_WIN40 = 1024;
    public static final int PDH_CVERSION_WIN50 = 1280;
    public static final int PDH_VERSION = 1283;
    public static final int PDH_PATH_WBEM_RESULT = 1;
    public static final int PDH_PATH_WBEM_INPUT = 2;
    public static final int PDH_FMT_RAW = 16;
    public static final int PDH_FMT_ANSI = 32;
    public static final int PDH_FMT_UNICODE = 64;
    public static final int PDH_FMT_LONG = 256;
    public static final int PDH_FMT_DOUBLE = 512;
    public static final int PDH_FMT_LARGE = 1024;
    public static final int PDH_FMT_NOSCALE = 4096;
    public static final int PDH_FMT_1000 = 8192;
    public static final int PDH_FMT_NODATA = 16384;
    public static final int PDH_FMT_NOCAP100 = 32768;
    public static final int PERF_DETAIL_COSTLY = 65536;
    public static final int PERF_DETAIL_STANDARD = 65535;
    
    int PdhConnectMachine(final String p0);
    
    int PdhGetDllVersion(final WinDef.DWORDByReference p0);
    
    int PdhOpenQuery(final String p0, final BaseTSD.DWORD_PTR p1, final WinNT.HANDLEByReference p2);
    
    int PdhCloseQuery(final WinNT.HANDLE p0);
    
    int PdhMakeCounterPath(final PDH_COUNTER_PATH_ELEMENTS p0, final char[] p1, final WinDef.DWORDByReference p2, final int p3);
    
    int PdhAddCounter(final WinNT.HANDLE p0, final String p1, final BaseTSD.DWORD_PTR p2, final WinNT.HANDLEByReference p3);
    
    int PdhAddEnglishCounter(final WinNT.HANDLE p0, final String p1, final BaseTSD.DWORD_PTR p2, final WinNT.HANDLEByReference p3);
    
    int PdhRemoveCounter(final WinNT.HANDLE p0);
    
    int PdhGetRawCounterValue(final WinNT.HANDLE p0, final WinDef.DWORDByReference p1, final PDH_RAW_COUNTER p2);
    
    int PdhValidatePath(final String p0);
    
    int PdhCollectQueryData(final WinNT.HANDLE p0);
    
    int PdhCollectQueryDataEx(final WinNT.HANDLE p0, final int p1, final WinNT.HANDLE p2);
    
    int PdhCollectQueryDataWithTime(final WinNT.HANDLE p0, final WinDef.LONGLONGByReference p1);
    
    int PdhSetQueryTimeRange(final WinNT.HANDLE p0, final PDH_TIME_INFO p1);
    
    int PdhEnumObjectItems(final String p0, final String p1, final String p2, final Pointer p3, final WinDef.DWORDByReference p4, final Pointer p5, final WinDef.DWORDByReference p6, final int p7, final int p8);
    
    int PdhLookupPerfIndexByName(final String p0, final String p1, final WinDef.DWORDByReference p2);
    
    int PdhLookupPerfNameByIndex(final String p0, final int p1, final Pointer p2, final WinDef.DWORDByReference p3);
    
    @FieldOrder({ "szMachineName", "szObjectName", "szInstanceName", "szParentInstance", "dwInstanceIndex", "szCounterName" })
    public static class PDH_COUNTER_PATH_ELEMENTS extends Structure
    {
        public String szMachineName;
        public String szObjectName;
        public String szInstanceName;
        public String szParentInstance;
        public int dwInstanceIndex;
        public String szCounterName;
    }
    
    @FieldOrder({ "CStatus", "TimeStamp", "FirstValue", "SecondValue", "MultiCount" })
    public static class PDH_RAW_COUNTER extends Structure
    {
        public int CStatus;
        public WinBase.FILETIME TimeStamp;
        public long FirstValue;
        public long SecondValue;
        public int MultiCount;
        
        public PDH_RAW_COUNTER() {
            this.TimeStamp = new WinBase.FILETIME();
        }
    }
    
    @FieldOrder({ "StartTime", "EndTime", "SampleCount" })
    public static class PDH_TIME_INFO extends Structure
    {
        public long StartTime;
        public long EndTime;
        public int SampleCount;
    }
}
