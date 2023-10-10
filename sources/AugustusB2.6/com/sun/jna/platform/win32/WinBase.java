// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.Callback;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.Union;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Structure;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Native;

public interface WinBase extends WinDef, BaseTSD
{
    public static final WinNT.HANDLE INVALID_HANDLE_VALUE = new WinNT.HANDLE(Pointer.createConstant((Native.POINTER_SIZE == 8) ? -1L : 4294967295L));
    public static final int WAIT_FAILED = -1;
    public static final int WAIT_OBJECT_0 = 0;
    public static final int WAIT_ABANDONED = 128;
    public static final int WAIT_ABANDONED_0 = 128;
    public static final int MAX_COMPUTERNAME_LENGTH = Platform.isMac() ? 15 : 31;
    public static final int LOGON32_LOGON_INTERACTIVE = 2;
    public static final int LOGON32_LOGON_NETWORK = 3;
    public static final int LOGON32_LOGON_BATCH = 4;
    public static final int LOGON32_LOGON_SERVICE = 5;
    public static final int LOGON32_LOGON_UNLOCK = 7;
    public static final int LOGON32_LOGON_NETWORK_CLEARTEXT = 8;
    public static final int LOGON32_LOGON_NEW_CREDENTIALS = 9;
    public static final int LOGON32_PROVIDER_DEFAULT = 0;
    public static final int LOGON32_PROVIDER_WINNT35 = 1;
    public static final int LOGON32_PROVIDER_WINNT40 = 2;
    public static final int LOGON32_PROVIDER_WINNT50 = 3;
    public static final int HANDLE_FLAG_INHERIT = 1;
    public static final int HANDLE_FLAG_PROTECT_FROM_CLOSE = 2;
    public static final int STARTF_USESHOWWINDOW = 1;
    public static final int STARTF_USESIZE = 2;
    public static final int STARTF_USEPOSITION = 4;
    public static final int STARTF_USECOUNTCHARS = 8;
    public static final int STARTF_USEFILLATTRIBUTE = 16;
    public static final int STARTF_RUNFULLSCREEN = 32;
    public static final int STARTF_FORCEONFEEDBACK = 64;
    public static final int STARTF_FORCEOFFFEEDBACK = 128;
    public static final int STARTF_USESTDHANDLES = 256;
    public static final int DEBUG_PROCESS = 1;
    public static final int DEBUG_ONLY_THIS_PROCESS = 2;
    public static final int CREATE_SUSPENDED = 4;
    public static final int DETACHED_PROCESS = 8;
    public static final int CREATE_NEW_CONSOLE = 16;
    public static final int CREATE_NEW_PROCESS_GROUP = 512;
    public static final int CREATE_UNICODE_ENVIRONMENT = 1024;
    public static final int CREATE_SEPARATE_WOW_VDM = 2048;
    public static final int CREATE_SHARED_WOW_VDM = 4096;
    public static final int CREATE_FORCEDOS = 8192;
    public static final int INHERIT_PARENT_AFFINITY = 65536;
    public static final int CREATE_PROTECTED_PROCESS = 262144;
    public static final int EXTENDED_STARTUPINFO_PRESENT = 524288;
    public static final int CREATE_BREAKAWAY_FROM_JOB = 16777216;
    public static final int CREATE_PRESERVE_CODE_AUTHZ_LEVEL = 33554432;
    public static final int CREATE_DEFAULT_ERROR_MODE = 67108864;
    public static final int CREATE_NO_WINDOW = 134217728;
    public static final int FILE_ENCRYPTABLE = 0;
    public static final int FILE_IS_ENCRYPTED = 1;
    public static final int FILE_SYSTEM_ATTR = 2;
    public static final int FILE_ROOT_DIR = 3;
    public static final int FILE_SYSTEM_DIR = 4;
    public static final int FILE_UNKNOWN = 5;
    public static final int FILE_SYSTEM_NOT_SUPPORT = 6;
    public static final int FILE_USER_DISALLOWED = 7;
    public static final int FILE_READ_ONLY = 8;
    public static final int FILE_DIR_DISALOWED = 9;
    public static final int CREATE_FOR_IMPORT = 1;
    public static final int CREATE_FOR_DIR = 2;
    public static final int OVERWRITE_HIDDEN = 4;
    public static final int INVALID_FILE_SIZE = -1;
    public static final int INVALID_SET_FILE_POINTER = -1;
    public static final int INVALID_FILE_ATTRIBUTES = -1;
    public static final int STILL_ACTIVE = 259;
    public static final int FileBasicInfo = 0;
    public static final int FileStandardInfo = 1;
    public static final int FileNameInfo = 2;
    public static final int FileRenameInfo = 3;
    public static final int FileDispositionInfo = 4;
    public static final int FileAllocationInfo = 5;
    public static final int FileEndOfFileInfo = 6;
    public static final int FileStreamInfo = 7;
    public static final int FileCompressionInfo = 8;
    public static final int FileAttributeTagInfo = 9;
    public static final int FileIdBothDirectoryInfo = 10;
    public static final int FileIdBothDirectoryRestartInfo = 11;
    public static final int FileIoPriorityHintInfo = 12;
    public static final int FileRemoteProtocolInfo = 13;
    public static final int FileFullDirectoryInfo = 14;
    public static final int FileFullDirectoryRestartInfo = 15;
    public static final int FileStorageInfo = 16;
    public static final int FileAlignmentInfo = 17;
    public static final int FileIdInfo = 18;
    public static final int FileIdExtdDirectoryInfo = 19;
    public static final int FileIdExtdDirectoryRestartInfo = 20;
    public static final int FILE_MAP_COPY = 1;
    public static final int FILE_MAP_WRITE = 2;
    public static final int FILE_MAP_READ = 4;
    public static final int FILE_MAP_ALL_ACCESS = 983071;
    public static final int FILE_MAP_EXECUTE = 32;
    public static final int FindExInfoStandard = 0;
    public static final int FindExInfoBasic = 1;
    public static final int FindExInfoMaxInfoLevel = 2;
    public static final int FindExSearchNameMatch = 0;
    public static final int FindExSearchLimitToDirectories = 1;
    public static final int FindExSearchLimitToDevices = 2;
    public static final int LMEM_FIXED = 0;
    public static final int LMEM_MOVEABLE = 2;
    public static final int LMEM_NOCOMPACT = 16;
    public static final int LMEM_NODISCARD = 32;
    public static final int LMEM_ZEROINIT = 64;
    public static final int LMEM_MODIFY = 128;
    public static final int LMEM_DISCARDABLE = 3840;
    public static final int LMEM_VALID_FLAGS = 3954;
    public static final int LMEM_INVALID_HANDLE = 32768;
    public static final int LHND = 66;
    public static final int LPTR = 64;
    public static final int LMEM_DISCARDED = 16384;
    public static final int LMEM_LOCKCOUNT = 255;
    public static final int FORMAT_MESSAGE_ALLOCATE_BUFFER = 256;
    public static final int FORMAT_MESSAGE_IGNORE_INSERTS = 512;
    public static final int FORMAT_MESSAGE_FROM_STRING = 1024;
    public static final int FORMAT_MESSAGE_FROM_HMODULE = 2048;
    public static final int FORMAT_MESSAGE_FROM_SYSTEM = 4096;
    public static final int FORMAT_MESSAGE_ARGUMENT_ARRAY = 8192;
    public static final int DRIVE_UNKNOWN = 0;
    public static final int DRIVE_NO_ROOT_DIR = 1;
    public static final int DRIVE_REMOVABLE = 2;
    public static final int DRIVE_FIXED = 3;
    public static final int DRIVE_REMOTE = 4;
    public static final int DRIVE_CDROM = 5;
    public static final int DRIVE_RAMDISK = 6;
    public static final int INFINITE = -1;
    public static final int MOVEFILE_COPY_ALLOWED = 2;
    public static final int MOVEFILE_CREATE_HARDLINK = 16;
    public static final int MOVEFILE_DELAY_UNTIL_REBOOT = 4;
    public static final int MOVEFILE_FAIL_IF_NOT_TRACKABLE = 32;
    public static final int MOVEFILE_REPLACE_EXISTING = 1;
    public static final int MOVEFILE_WRITE_THROUGH = 8;
    public static final int PIPE_CLIENT_END = 0;
    public static final int PIPE_SERVER_END = 1;
    public static final int PIPE_ACCESS_DUPLEX = 3;
    public static final int PIPE_ACCESS_INBOUND = 1;
    public static final int PIPE_ACCESS_OUTBOUND = 2;
    public static final int PIPE_TYPE_BYTE = 0;
    public static final int PIPE_TYPE_MESSAGE = 4;
    public static final int PIPE_READMODE_BYTE = 0;
    public static final int PIPE_READMODE_MESSAGE = 2;
    public static final int PIPE_WAIT = 0;
    public static final int PIPE_NOWAIT = 1;
    public static final int PIPE_ACCEPT_REMOTE_CLIENTS = 0;
    public static final int PIPE_REJECT_REMOTE_CLIENTS = 8;
    public static final int PIPE_UNLIMITED_INSTANCES = 255;
    public static final int NMPWAIT_USE_DEFAULT_WAIT = 0;
    public static final int NMPWAIT_NOWAIT = 1;
    public static final int NMPWAIT_WAIT_FOREVER = -1;
    public static final int NOPARITY = 0;
    public static final int ODDPARITY = 1;
    public static final int EVENPARITY = 2;
    public static final int MARKPARITY = 3;
    public static final int SPACEPARITY = 4;
    public static final int ONESTOPBIT = 0;
    public static final int ONE5STOPBITS = 1;
    public static final int TWOSTOPBITS = 2;
    public static final int CBR_110 = 110;
    public static final int CBR_300 = 300;
    public static final int CBR_600 = 600;
    public static final int CBR_1200 = 1200;
    public static final int CBR_2400 = 2400;
    public static final int CBR_4800 = 4800;
    public static final int CBR_9600 = 9600;
    public static final int CBR_14400 = 14400;
    public static final int CBR_19200 = 19200;
    public static final int CBR_38400 = 38400;
    public static final int CBR_56000 = 56000;
    public static final int CBR_128000 = 128000;
    public static final int CBR_256000 = 256000;
    public static final int DTR_CONTROL_DISABLE = 0;
    public static final int DTR_CONTROL_ENABLE = 1;
    public static final int DTR_CONTROL_HANDSHAKE = 2;
    public static final int RTS_CONTROL_DISABLE = 0;
    public static final int RTS_CONTROL_ENABLE = 1;
    public static final int RTS_CONTROL_HANDSHAKE = 2;
    public static final int RTS_CONTROL_TOGGLE = 3;
    public static final int ES_AWAYMODE_REQUIRED = 64;
    public static final int ES_CONTINUOUS = Integer.MIN_VALUE;
    public static final int ES_DISPLAY_REQUIRED = 2;
    public static final int ES_SYSTEM_REQUIRED = 1;
    public static final int ES_USER_PRESENT = 4;
    public static final int MUTEX_MODIFY_STATE = 1;
    public static final int MUTEX_ALL_ACCESS = 2031617;
    
    @FieldOrder({ "CreationTime", "LastAccessTime", "LastWriteTime", "ChangeTime", "FileAttributes" })
    public static class FILE_BASIC_INFO extends Structure
    {
        public WinNT.LARGE_INTEGER CreationTime;
        public WinNT.LARGE_INTEGER LastAccessTime;
        public WinNT.LARGE_INTEGER LastWriteTime;
        public WinNT.LARGE_INTEGER ChangeTime;
        public int FileAttributes;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_BASIC_INFO.class, null);
        }
        
        public FILE_BASIC_INFO() {
        }
        
        public FILE_BASIC_INFO(final Pointer memory) {
            super(memory);
            this.read();
            this.CreationTime = new WinNT.LARGE_INTEGER(this.CreationTime.getValue());
            this.LastAccessTime = new WinNT.LARGE_INTEGER(this.LastAccessTime.getValue());
            this.LastWriteTime = new WinNT.LARGE_INTEGER(this.LastWriteTime.getValue());
            this.ChangeTime = new WinNT.LARGE_INTEGER(this.ChangeTime.getValue());
        }
        
        public FILE_BASIC_INFO(final FILETIME CreationTime, final FILETIME LastAccessTime, final FILETIME LastWriteTime, final FILETIME ChangeTime, final int FileAttributes) {
            this.CreationTime = new WinNT.LARGE_INTEGER(CreationTime.toTime());
            this.LastAccessTime = new WinNT.LARGE_INTEGER(LastAccessTime.toTime());
            this.LastWriteTime = new WinNT.LARGE_INTEGER(LastWriteTime.toTime());
            this.ChangeTime = new WinNT.LARGE_INTEGER(ChangeTime.toTime());
            this.FileAttributes = FileAttributes;
            this.write();
        }
        
        public FILE_BASIC_INFO(final WinNT.LARGE_INTEGER CreationTime, final WinNT.LARGE_INTEGER LastAccessTime, final WinNT.LARGE_INTEGER LastWriteTime, final WinNT.LARGE_INTEGER ChangeTime, final int FileAttributes) {
            this.CreationTime = CreationTime;
            this.LastAccessTime = LastAccessTime;
            this.LastWriteTime = LastWriteTime;
            this.ChangeTime = ChangeTime;
            this.FileAttributes = FileAttributes;
            this.write();
        }
        
        public static class ByReference extends FILE_BASIC_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "AllocationSize", "EndOfFile", "NumberOfLinks", "DeletePending", "Directory" })
    public static class FILE_STANDARD_INFO extends Structure
    {
        public WinNT.LARGE_INTEGER AllocationSize;
        public WinNT.LARGE_INTEGER EndOfFile;
        public int NumberOfLinks;
        public boolean DeletePending;
        public boolean Directory;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_STANDARD_INFO.class, null);
        }
        
        public FILE_STANDARD_INFO() {
        }
        
        public FILE_STANDARD_INFO(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public FILE_STANDARD_INFO(final WinNT.LARGE_INTEGER AllocationSize, final WinNT.LARGE_INTEGER EndOfFile, final int NumberOfLinks, final boolean DeletePending, final boolean Directory) {
            this.AllocationSize = AllocationSize;
            this.EndOfFile = EndOfFile;
            this.NumberOfLinks = NumberOfLinks;
            this.DeletePending = DeletePending;
            this.Directory = Directory;
            this.write();
        }
        
        public static class ByReference extends FILE_STANDARD_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "DeleteFile" })
    public static class FILE_DISPOSITION_INFO extends Structure
    {
        public boolean DeleteFile;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_DISPOSITION_INFO.class, null);
        }
        
        public FILE_DISPOSITION_INFO() {
        }
        
        public FILE_DISPOSITION_INFO(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public FILE_DISPOSITION_INFO(final boolean DeleteFile) {
            this.DeleteFile = DeleteFile;
            this.write();
        }
        
        public static class ByReference extends FILE_DISPOSITION_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "CompressedFileSize", "CompressionFormat", "CompressionUnitShift", "ChunkShift", "ClusterShift", "Reserved" })
    public static class FILE_COMPRESSION_INFO extends Structure
    {
        public WinNT.LARGE_INTEGER CompressedFileSize;
        public short CompressionFormat;
        public byte CompressionUnitShift;
        public byte ChunkShift;
        public byte ClusterShift;
        public byte[] Reserved;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_COMPRESSION_INFO.class, null);
        }
        
        public FILE_COMPRESSION_INFO() {
            super(W32APITypeMapper.DEFAULT);
            this.Reserved = new byte[3];
        }
        
        public FILE_COMPRESSION_INFO(final Pointer memory) {
            super(memory, 0, W32APITypeMapper.DEFAULT);
            this.Reserved = new byte[3];
            this.read();
        }
        
        public FILE_COMPRESSION_INFO(final WinNT.LARGE_INTEGER CompressedFileSize, final short CompressionFormat, final byte CompressionUnitShift, final byte ChunkShift, final byte ClusterShift) {
            this.Reserved = new byte[3];
            this.CompressedFileSize = CompressedFileSize;
            this.CompressionFormat = CompressionFormat;
            this.CompressionUnitShift = CompressionUnitShift;
            this.ChunkShift = ChunkShift;
            this.ClusterShift = ClusterShift;
            this.Reserved = new byte[3];
            this.write();
        }
        
        public static class ByReference extends FILE_COMPRESSION_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "FileAttributes", "ReparseTag" })
    public static class FILE_ATTRIBUTE_TAG_INFO extends Structure
    {
        public int FileAttributes;
        public int ReparseTag;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_ATTRIBUTE_TAG_INFO.class, null);
        }
        
        public FILE_ATTRIBUTE_TAG_INFO() {
        }
        
        public FILE_ATTRIBUTE_TAG_INFO(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public FILE_ATTRIBUTE_TAG_INFO(final int FileAttributes, final int ReparseTag) {
            this.FileAttributes = FileAttributes;
            this.ReparseTag = ReparseTag;
            this.write();
        }
        
        public static class ByReference extends FILE_ATTRIBUTE_TAG_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "VolumeSerialNumber", "FileId" })
    public static class FILE_ID_INFO extends Structure
    {
        public long VolumeSerialNumber;
        public FILE_ID_128 FileId;
        
        public static int sizeOf() {
            return Native.getNativeSize(FILE_ID_INFO.class, null);
        }
        
        public FILE_ID_INFO() {
        }
        
        public FILE_ID_INFO(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public FILE_ID_INFO(final long VolumeSerialNumber, final FILE_ID_128 FileId) {
            this.VolumeSerialNumber = VolumeSerialNumber;
            this.FileId = FileId;
            this.write();
        }
        
        public static class ByReference extends FILE_ID_INFO implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
        
        @FieldOrder({ "Identifier" })
        public static class FILE_ID_128 extends Structure
        {
            public BYTE[] Identifier;
            
            public FILE_ID_128() {
                this.Identifier = new BYTE[16];
            }
            
            public FILE_ID_128(final Pointer memory) {
                super(memory);
                this.Identifier = new BYTE[16];
                this.read();
            }
            
            public FILE_ID_128(final BYTE[] Identifier) {
                this.Identifier = new BYTE[16];
                this.Identifier = Identifier;
                this.write();
            }
        }
    }
    
    @FieldOrder({ "dwFileAttributes", "ftCreationTime", "ftLastAccessTime", "ftLastWriteTime", "nFileSizeHigh", "nFileSizeLow", "dwReserved0", "dwReserved1", "cFileName", "cAlternateFileName" })
    public static class WIN32_FIND_DATA extends Structure
    {
        public int dwFileAttributes;
        public FILETIME ftCreationTime;
        public FILETIME ftLastAccessTime;
        public FILETIME ftLastWriteTime;
        public int nFileSizeHigh;
        public int nFileSizeLow;
        public int dwReserved0;
        public int dwReserved1;
        public char[] cFileName;
        public char[] cAlternateFileName;
        
        public static int sizeOf() {
            return Native.getNativeSize(WIN32_FIND_DATA.class, null);
        }
        
        public WIN32_FIND_DATA() {
            super(W32APITypeMapper.DEFAULT);
            this.cFileName = new char[260];
            this.cAlternateFileName = new char[14];
        }
        
        public WIN32_FIND_DATA(final Pointer memory) {
            super(memory, 0, W32APITypeMapper.DEFAULT);
            this.cFileName = new char[260];
            this.cAlternateFileName = new char[14];
            this.read();
        }
        
        public WIN32_FIND_DATA(final int dwFileAttributes, final FILETIME ftCreationTime, final FILETIME ftLastAccessTime, final FILETIME ftLastWriteTime, final int nFileSizeHigh, final int nFileSizeLow, final int dwReserved0, final int dwReserved1, final char[] cFileName, final char[] cAlternateFileName) {
            this.cFileName = new char[260];
            this.cAlternateFileName = new char[14];
            this.dwFileAttributes = dwFileAttributes;
            this.ftCreationTime = ftCreationTime;
            this.ftLastAccessTime = ftLastAccessTime;
            this.ftLastWriteTime = ftLastWriteTime;
            this.nFileSizeHigh = nFileSizeHigh;
            this.nFileSizeLow = nFileSizeLow;
            this.dwReserved0 = dwReserved0;
            this.cFileName = cFileName;
            this.cAlternateFileName = cAlternateFileName;
            this.write();
        }
        
        public String getFileName() {
            return Native.toString(this.cFileName);
        }
        
        public String getAlternateFileName() {
            return Native.toString(this.cAlternateFileName);
        }
        
        public static class ByReference extends WIN32_FIND_DATA implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "dwLowDateTime", "dwHighDateTime" })
    public static class FILETIME extends Structure
    {
        public int dwLowDateTime;
        public int dwHighDateTime;
        private static final long EPOCH_DIFF = 11644473600000L;
        
        public FILETIME(final Date date) {
            final long rawValue = dateToFileTime(date);
            this.dwHighDateTime = (int)(rawValue >> 32 & 0xFFFFFFFFL);
            this.dwLowDateTime = (int)(rawValue & 0xFFFFFFFFL);
        }
        
        public FILETIME(final WinNT.LARGE_INTEGER ft) {
            this.dwHighDateTime = ft.getHigh().intValue();
            this.dwLowDateTime = ft.getLow().intValue();
        }
        
        public FILETIME() {
        }
        
        public FILETIME(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static Date filetimeToDate(final int high, final int low) {
            final long filetime = (long)high << 32 | ((long)low & 0xFFFFFFFFL);
            final long ms_since_16010101 = filetime / 10000L;
            final long ms_since_16010102 = ms_since_16010101 - 11644473600000L;
            return new Date(ms_since_16010102);
        }
        
        public static long dateToFileTime(final Date date) {
            final long ms_since_19700101 = date.getTime();
            final long ms_since_19700102 = ms_since_19700101 + 11644473600000L;
            return ms_since_19700102 * 1000L * 10L;
        }
        
        public Date toDate() {
            return filetimeToDate(this.dwHighDateTime, this.dwLowDateTime);
        }
        
        public long toTime() {
            return this.toDate().getTime();
        }
        
        public DWORDLONG toDWordLong() {
            return new DWORDLONG((long)this.dwHighDateTime << 32 | ((long)this.dwLowDateTime & 0xFFFFFFFFL));
        }
        
        @Override
        public String toString() {
            return super.toString() + ": " + this.toDate().toString();
        }
        
        public static class ByReference extends FILETIME implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds" })
    public static class SYSTEMTIME extends Structure
    {
        public short wYear;
        public short wMonth;
        public short wDayOfWeek;
        public short wDay;
        public short wHour;
        public short wMinute;
        public short wSecond;
        public short wMilliseconds;
        
        public SYSTEMTIME() {
        }
        
        public SYSTEMTIME(final Date date) {
            this(date.getTime());
        }
        
        public SYSTEMTIME(final long timestamp) {
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            this.fromCalendar(cal);
        }
        
        public SYSTEMTIME(final Calendar cal) {
            this.fromCalendar(cal);
        }
        
        public void fromCalendar(final Calendar cal) {
            this.wYear = (short)cal.get(1);
            this.wMonth = (short)(1 + cal.get(2) - 0);
            this.wDay = (short)cal.get(5);
            this.wHour = (short)cal.get(11);
            this.wMinute = (short)cal.get(12);
            this.wSecond = (short)cal.get(13);
            this.wMilliseconds = (short)cal.get(14);
            this.wDayOfWeek = (short)(cal.get(7) - 1);
        }
        
        public Calendar toCalendar() {
            final Calendar cal = Calendar.getInstance();
            cal.set(1, this.wYear);
            cal.set(2, 0 + (this.wMonth - 1));
            cal.set(5, this.wDay);
            cal.set(11, this.wHour);
            cal.set(12, this.wMinute);
            cal.set(13, this.wSecond);
            cal.set(14, this.wMilliseconds);
            return cal;
        }
        
        @Override
        public String toString() {
            if (this.wYear == 0 && this.wMonth == 0 && this.wDay == 0 && this.wHour == 0 && this.wMinute == 0 && this.wSecond == 0 && this.wMilliseconds == 0) {
                return super.toString();
            }
            final DateFormat dtf = DateFormat.getDateTimeInstance();
            final Calendar cal = this.toCalendar();
            return dtf.format(cal.getTime());
        }
    }
    
    @FieldOrder({ "Bias", "StandardName", "StandardDate", "StandardBias", "DaylightName", "DaylightDate", "DaylightBias" })
    public static class TIME_ZONE_INFORMATION extends Structure
    {
        public LONG Bias;
        public String StandardName;
        public SYSTEMTIME StandardDate;
        public LONG StandardBias;
        public String DaylightName;
        public SYSTEMTIME DaylightDate;
        public LONG DaylightBias;
        
        public TIME_ZONE_INFORMATION() {
            super(W32APITypeMapper.DEFAULT);
        }
    }
    
    @FieldOrder({ "Internal", "InternalHigh", "Offset", "OffsetHigh", "hEvent" })
    public static class OVERLAPPED extends Structure
    {
        public ULONG_PTR Internal;
        public ULONG_PTR InternalHigh;
        public int Offset;
        public int OffsetHigh;
        public WinNT.HANDLE hEvent;
    }
    
    @FieldOrder({ "processorArchitecture", "dwPageSize", "lpMinimumApplicationAddress", "lpMaximumApplicationAddress", "dwActiveProcessorMask", "dwNumberOfProcessors", "dwProcessorType", "dwAllocationGranularity", "wProcessorLevel", "wProcessorRevision" })
    public static class SYSTEM_INFO extends Structure
    {
        public UNION processorArchitecture;
        public DWORD dwPageSize;
        public Pointer lpMinimumApplicationAddress;
        public Pointer lpMaximumApplicationAddress;
        public DWORD_PTR dwActiveProcessorMask;
        public DWORD dwNumberOfProcessors;
        public DWORD dwProcessorType;
        public DWORD dwAllocationGranularity;
        public WORD wProcessorLevel;
        public WORD wProcessorRevision;
        
        @FieldOrder({ "wProcessorArchitecture", "wReserved" })
        public static class PI extends Structure
        {
            public WORD wProcessorArchitecture;
            public WORD wReserved;
            
            public static class ByReference extends PI implements Structure.ByReference
            {
            }
        }
        
        public static class UNION extends Union
        {
            public DWORD dwOemID;
            public PI pi;
            
            @Override
            public void read() {
                this.setType("dwOemID");
                super.read();
                this.setType("pi");
                super.read();
            }
            
            public static class ByReference extends UNION implements Structure.ByReference
            {
            }
        }
    }
    
    @FieldOrder({ "dwLength", "dwMemoryLoad", "ullTotalPhys", "ullAvailPhys", "ullTotalPageFile", "ullAvailPageFile", "ullTotalVirtual", "ullAvailVirtual", "ullAvailExtendedVirtual" })
    public static class MEMORYSTATUSEX extends Structure
    {
        public DWORD dwLength;
        public DWORD dwMemoryLoad;
        public DWORDLONG ullTotalPhys;
        public DWORDLONG ullAvailPhys;
        public DWORDLONG ullTotalPageFile;
        public DWORDLONG ullAvailPageFile;
        public DWORDLONG ullTotalVirtual;
        public DWORDLONG ullAvailVirtual;
        public DWORDLONG ullAvailExtendedVirtual;
        
        public MEMORYSTATUSEX() {
            this.dwLength = new DWORD((long)this.size());
        }
    }
    
    @FieldOrder({ "dwLength", "lpSecurityDescriptor", "bInheritHandle" })
    public static class SECURITY_ATTRIBUTES extends Structure
    {
        public DWORD dwLength;
        public Pointer lpSecurityDescriptor;
        public boolean bInheritHandle;
        
        public SECURITY_ATTRIBUTES() {
            this.dwLength = new DWORD((long)this.size());
        }
    }
    
    @FieldOrder({ "cb", "lpReserved", "lpDesktop", "lpTitle", "dwX", "dwY", "dwXSize", "dwYSize", "dwXCountChars", "dwYCountChars", "dwFillAttribute", "dwFlags", "wShowWindow", "cbReserved2", "lpReserved2", "hStdInput", "hStdOutput", "hStdError" })
    public static class STARTUPINFO extends Structure
    {
        public DWORD cb;
        public String lpReserved;
        public String lpDesktop;
        public String lpTitle;
        public DWORD dwX;
        public DWORD dwY;
        public DWORD dwXSize;
        public DWORD dwYSize;
        public DWORD dwXCountChars;
        public DWORD dwYCountChars;
        public DWORD dwFillAttribute;
        public int dwFlags;
        public WORD wShowWindow;
        public WORD cbReserved2;
        public ByteByReference lpReserved2;
        public WinNT.HANDLE hStdInput;
        public WinNT.HANDLE hStdOutput;
        public WinNT.HANDLE hStdError;
        
        public STARTUPINFO() {
            super(W32APITypeMapper.DEFAULT);
            this.cb = new DWORD((long)this.size());
        }
    }
    
    @FieldOrder({ "hProcess", "hThread", "dwProcessId", "dwThreadId" })
    public static class PROCESS_INFORMATION extends Structure
    {
        public WinNT.HANDLE hProcess;
        public WinNT.HANDLE hThread;
        public DWORD dwProcessId;
        public DWORD dwThreadId;
        
        public PROCESS_INFORMATION() {
        }
        
        public PROCESS_INFORMATION(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends PROCESS_INFORMATION implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "foreignLocation" })
    public static class FOREIGN_THREAD_START_ROUTINE extends Structure
    {
        public LPVOID foreignLocation;
    }
    
    @FieldOrder({ "ReadIntervalTimeout", "ReadTotalTimeoutMultiplier", "ReadTotalTimeoutConstant", "WriteTotalTimeoutMultiplier", "WriteTotalTimeoutConstant" })
    public static class COMMTIMEOUTS extends Structure
    {
        public DWORD ReadIntervalTimeout;
        public DWORD ReadTotalTimeoutMultiplier;
        public DWORD ReadTotalTimeoutConstant;
        public DWORD WriteTotalTimeoutMultiplier;
        public DWORD WriteTotalTimeoutConstant;
    }
    
    @FieldOrder({ "DCBlength", "BaudRate", "controllBits", "wReserved", "XonLim", "XoffLim", "ByteSize", "Parity", "StopBits", "XonChar", "XoffChar", "ErrorChar", "EofChar", "EvtChar", "wReserved1" })
    public static class DCB extends Structure
    {
        public DWORD DCBlength;
        public DWORD BaudRate;
        public DCBControllBits controllBits;
        public WORD wReserved;
        public WORD XonLim;
        public WORD XoffLim;
        public BYTE ByteSize;
        public BYTE Parity;
        public BYTE StopBits;
        public char XonChar;
        public char XoffChar;
        public char ErrorChar;
        public char EofChar;
        public char EvtChar;
        public WORD wReserved1;
        
        public DCB() {
            this.DCBlength = new DWORD((long)this.size());
        }
        
        public static class DCBControllBits extends DWORD
        {
            private static final long serialVersionUID = 8574966619718078579L;
            
            @Override
            public String toString() {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('<');
                stringBuilder.append("fBinary:1=");
                stringBuilder.append(this.getfBinary() ? '1' : '0');
                stringBuilder.append(", fParity:1=");
                stringBuilder.append(this.getfParity() ? '1' : '0');
                stringBuilder.append(", fOutxCtsFlow:1=");
                stringBuilder.append(this.getfOutxCtsFlow() ? '1' : '0');
                stringBuilder.append(", fOutxDsrFlow:1=");
                stringBuilder.append(this.getfOutxDsrFlow() ? '1' : '0');
                stringBuilder.append(", fDtrControl:2=");
                stringBuilder.append(this.getfDtrControl());
                stringBuilder.append(", fDsrSensitivity:1=");
                stringBuilder.append(this.getfDsrSensitivity() ? '1' : '0');
                stringBuilder.append(", fTXContinueOnXoff:1=");
                stringBuilder.append(this.getfTXContinueOnXoff() ? '1' : '0');
                stringBuilder.append(", fOutX:1=");
                stringBuilder.append(this.getfOutX() ? '1' : '0');
                stringBuilder.append(", fInX:1=");
                stringBuilder.append(this.getfInX() ? '1' : '0');
                stringBuilder.append(", fErrorChar:1=");
                stringBuilder.append(this.getfErrorChar() ? '1' : '0');
                stringBuilder.append(", fNull:1=");
                stringBuilder.append(this.getfNull() ? '1' : '0');
                stringBuilder.append(", fRtsControl:2=");
                stringBuilder.append(this.getfRtsControl());
                stringBuilder.append(", fAbortOnError:1=");
                stringBuilder.append(this.getfAbortOnError() ? '1' : '0');
                stringBuilder.append(", fDummy2:17=");
                stringBuilder.append(this.getfDummy2());
                stringBuilder.append('>');
                return stringBuilder.toString();
            }
            
            public boolean getfAbortOnError() {
                return (this.intValue() & 0x4000) != 0x0;
            }
            
            public boolean getfBinary() {
                return (this.intValue() & 0x1) != 0x0;
            }
            
            public boolean getfDsrSensitivity() {
                return (this.intValue() & 0x40) != 0x0;
            }
            
            public int getfDtrControl() {
                return this.intValue() >>> 4 & 0x3;
            }
            
            public boolean getfErrorChar() {
                return (this.intValue() & 0x400) != 0x0;
            }
            
            public boolean getfInX() {
                return (this.intValue() & 0x200) != 0x0;
            }
            
            public boolean getfNull() {
                return (this.intValue() & 0x800) != 0x0;
            }
            
            public boolean getfOutX() {
                return (this.intValue() & 0x100) != 0x0;
            }
            
            public boolean getfOutxCtsFlow() {
                return (this.intValue() & 0x4) != 0x0;
            }
            
            public boolean getfOutxDsrFlow() {
                return (this.intValue() & 0x8) != 0x0;
            }
            
            public boolean getfParity() {
                return (this.intValue() & 0x2) != 0x0;
            }
            
            public int getfRtsControl() {
                return this.intValue() >>> 12 & 0x3;
            }
            
            public int getfDummy2() {
                return this.intValue() >>> 15 & 0x1FFFF;
            }
            
            public boolean getfTXContinueOnXoff() {
                return (this.intValue() & 0x80) != 0x0;
            }
            
            public void setfAbortOnError(final boolean fAbortOnError) {
                final int tmp = leftShiftMask(fAbortOnError ? 1 : 0, (byte)14, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfBinary(final boolean fBinary) {
                final int tmp = leftShiftMask(fBinary ? 1 : 0, (byte)0, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfDsrSensitivity(final boolean fDsrSensitivity) {
                final int tmp = leftShiftMask(fDsrSensitivity ? 1 : 0, (byte)6, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfDtrControl(final int fOutxDsrFlow) {
                final int tmp = leftShiftMask(fOutxDsrFlow, (byte)4, 3, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfErrorChar(final boolean fErrorChar) {
                final int tmp = leftShiftMask(fErrorChar ? 1 : 0, (byte)10, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfInX(final boolean fInX) {
                final int tmp = leftShiftMask(fInX ? 1 : 0, (byte)9, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfNull(final boolean fNull) {
                final int tmp = leftShiftMask(fNull ? 1 : 0, (byte)11, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfOutX(final boolean fOutX) {
                final int tmp = leftShiftMask(fOutX ? 1 : 0, (byte)8, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfOutxCtsFlow(final boolean fOutxCtsFlow) {
                final int tmp = leftShiftMask(fOutxCtsFlow ? 1 : 0, (byte)2, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfOutxDsrFlow(final boolean fOutxDsrFlow) {
                final int tmp = leftShiftMask(fOutxDsrFlow ? 1 : 0, (byte)3, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfParity(final boolean fParity) {
                final int tmp = leftShiftMask(fParity ? 1 : 0, (byte)1, 1, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfRtsControl(final int fRtsControl) {
                final int tmp = leftShiftMask(fRtsControl, (byte)12, 3, this.intValue());
                this.setValue(tmp);
            }
            
            public void setfTXContinueOnXoff(final boolean fTXContinueOnXoff) {
                final int tmp = leftShiftMask(fTXContinueOnXoff ? 1 : 0, (byte)7, 1, this.intValue());
                this.setValue(tmp);
            }
            
            private static int leftShiftMask(final int valuetoset, final byte shift, final int mask, final int storage) {
                int tmp = storage;
                tmp &= ~(mask << shift);
                tmp |= (valuetoset & mask) << shift;
                return tmp;
            }
        }
    }
    
    public interface EnumResNameProc extends Callback
    {
        boolean invoke(final HMODULE p0, final Pointer p1, final Pointer p2, final Pointer p3);
    }
    
    public interface EnumResTypeProc extends Callback
    {
        boolean invoke(final HMODULE p0, final Pointer p1, final Pointer p2);
    }
    
    public interface FE_IMPORT_FUNC extends StdCallLibrary.StdCallCallback
    {
        DWORD callback(final Pointer p0, final Pointer p1, final ULONGByReference p2);
    }
    
    public interface FE_EXPORT_FUNC extends StdCallLibrary.StdCallCallback
    {
        DWORD callback(final Pointer p0, final Pointer p1, final ULONG p2);
    }
    
    public interface COMPUTER_NAME_FORMAT
    {
        public static final int ComputerNameNetBIOS = 0;
        public static final int ComputerNameDnsHostname = 1;
        public static final int ComputerNameDnsDomain = 2;
        public static final int ComputerNameDnsFullyQualified = 3;
        public static final int ComputerNamePhysicalNetBIOS = 4;
        public static final int ComputerNamePhysicalDnsHostname = 5;
        public static final int ComputerNamePhysicalDnsDomain = 6;
        public static final int ComputerNamePhysicalDnsFullyQualified = 7;
        public static final int ComputerNameMax = 8;
    }
    
    public interface THREAD_START_ROUTINE extends StdCallLibrary.StdCallCallback
    {
        DWORD apply(final LPVOID p0);
    }
}
