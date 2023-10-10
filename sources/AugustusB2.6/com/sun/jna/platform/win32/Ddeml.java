// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Structure;
import com.sun.jna.PointerType;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Ddeml extends StdCallLibrary
{
    public static final Ddeml INSTANCE = Native.load("user32", Ddeml.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int XST_NULL = 0;
    public static final int XST_INCOMPLETE = 1;
    public static final int XST_CONNECTED = 2;
    public static final int XST_INIT1 = 3;
    public static final int XST_INIT2 = 4;
    public static final int XST_REQSENT = 5;
    public static final int XST_DATARCVD = 6;
    public static final int XST_POKESENT = 7;
    public static final int XST_POKEACKRCVD = 8;
    public static final int XST_EXECSENT = 9;
    public static final int XST_EXECACKRCVD = 10;
    public static final int XST_ADVSENT = 11;
    public static final int XST_UNADVSENT = 12;
    public static final int XST_ADVACKRCVD = 13;
    public static final int XST_UNADVACKRCVD = 14;
    public static final int XST_ADVDATASENT = 15;
    public static final int XST_ADVDATAACKRCVD = 16;
    public static final int CADV_LATEACK = 65535;
    public static final int ST_CONNECTED = 1;
    public static final int ST_ADVISE = 2;
    public static final int ST_ISLOCAL = 4;
    public static final int ST_BLOCKED = 8;
    public static final int ST_CLIENT = 16;
    public static final int ST_TERMINATED = 32;
    public static final int ST_INLIST = 64;
    public static final int ST_BLOCKNEXT = 128;
    public static final int ST_ISSELF = 256;
    public static final int DDE_FACK = 32768;
    public static final int DDE_FBUSY = 16384;
    public static final int DDE_FDEFERUPD = 16384;
    public static final int DDE_FACKREQ = 32768;
    public static final int DDE_FRELEASE = 8192;
    public static final int DDE_FREQUESTED = 4096;
    public static final int DDE_FAPPSTATUS = 255;
    public static final int DDE_FNOTPROCESSED = 0;
    public static final int DDE_FACKRESERVED = -49408;
    public static final int DDE_FADVRESERVED = -49153;
    public static final int DDE_FDATRESERVED = -45057;
    public static final int DDE_FPOKRESERVED = -8193;
    public static final int MSGF_DDEMGR = 32769;
    public static final int CP_WINANSI = 1004;
    public static final int CP_WINUNICODE = 1200;
    public static final int CP_WINNEUTRAL = 1200;
    public static final int XTYPF_NOBLOCK = 2;
    public static final int XTYPF_NODATA = 4;
    public static final int XTYPF_ACKREQ = 8;
    public static final int XCLASS_MASK = 64512;
    public static final int XCLASS_BOOL = 4096;
    public static final int XCLASS_DATA = 8192;
    public static final int XCLASS_FLAGS = 16384;
    public static final int XCLASS_NOTIFICATION = 32768;
    public static final int XTYP_ERROR = 32770;
    public static final int XTYP_ADVDATA = 16400;
    public static final int XTYP_ADVREQ = 8226;
    public static final int XTYP_ADVSTART = 4144;
    public static final int XTYP_ADVSTOP = 32832;
    public static final int XTYP_EXECUTE = 16464;
    public static final int XTYP_CONNECT = 4194;
    public static final int XTYP_CONNECT_CONFIRM = 32882;
    public static final int XTYP_XACT_COMPLETE = 32896;
    public static final int XTYP_POKE = 16528;
    public static final int XTYP_REGISTER = 32930;
    public static final int XTYP_REQUEST = 8368;
    public static final int XTYP_DISCONNECT = 32962;
    public static final int XTYP_UNREGISTER = 32978;
    public static final int XTYP_WILDCONNECT = 8418;
    public static final int XTYP_MONITOR = 33010;
    public static final int XTYP_MASK = 240;
    public static final int XTYP_SHIFT = 4;
    public static final int TIMEOUT_ASYNC = -1;
    public static final int QID_SYNC = -1;
    public static final String SZDDESYS_TOPIC = "System";
    public static final String SZDDESYS_ITEM_TOPICS = "Topics";
    public static final String SZDDESYS_ITEM_SYSITEMS = "SysItems";
    public static final String SZDDESYS_ITEM_RTNMSG = "ReturnMessage";
    public static final String SZDDESYS_ITEM_STATUS = "Status";
    public static final String SZDDESYS_ITEM_FORMATS = "Formats";
    public static final String SZDDESYS_ITEM_HELP = "Help";
    public static final String SZDDE_ITEM_ITEMLIST = "TopicItemList";
    public static final int DMLERR_NO_ERROR = 0;
    public static final int DMLERR_FIRST = 16384;
    public static final int DMLERR_ADVACKTIMEOUT = 16384;
    public static final int DMLERR_BUSY = 16385;
    public static final int DMLERR_DATAACKTIMEOUT = 16386;
    public static final int DMLERR_DLL_NOT_INITIALIZED = 16387;
    public static final int DMLERR_DLL_USAGE = 16388;
    public static final int DMLERR_EXECACKTIMEOUT = 16389;
    public static final int DMLERR_INVALIDPARAMETER = 16390;
    public static final int DMLERR_LOW_MEMORY = 16391;
    public static final int DMLERR_MEMORY_ERROR = 16392;
    public static final int DMLERR_NOTPROCESSED = 16393;
    public static final int DMLERR_NO_CONV_ESTABLISHED = 16394;
    public static final int DMLERR_POKEACKTIMEOUT = 16395;
    public static final int DMLERR_POSTMSG_FAILED = 16396;
    public static final int DMLERR_REENTRANCY = 16397;
    public static final int DMLERR_SERVER_DIED = 16398;
    public static final int DMLERR_SYS_ERROR = 16399;
    public static final int DMLERR_UNADVACKTIMEOUT = 16400;
    public static final int DMLERR_UNFOUND_QUEUE_ID = 16401;
    public static final int DMLERR_LAST = 16401;
    public static final int HDATA_APPOWNED = 1;
    public static final int CBF_FAIL_SELFCONNECTIONS = 4096;
    public static final int CBF_FAIL_CONNECTIONS = 8192;
    public static final int CBF_FAIL_ADVISES = 16384;
    public static final int CBF_FAIL_EXECUTES = 32768;
    public static final int CBF_FAIL_POKES = 65536;
    public static final int CBF_FAIL_REQUESTS = 131072;
    public static final int CBF_FAIL_ALLSVRXACTIONS = 258048;
    public static final int CBF_SKIP_CONNECT_CONFIRMS = 262144;
    public static final int CBF_SKIP_REGISTRATIONS = 524288;
    public static final int CBF_SKIP_UNREGISTRATIONS = 1048576;
    public static final int CBF_SKIP_DISCONNECTS = 2097152;
    public static final int CBF_SKIP_ALLNOTIFICATIONS = 3932160;
    public static final int APPCMD_CLIENTONLY = 16;
    public static final int APPCMD_FILTERINITS = 32;
    public static final int APPCMD_MASK = 4080;
    public static final int APPCLASS_STANDARD = 0;
    public static final int APPCLASS_MONITOR = 1;
    public static final int APPCLASS_MASK = 15;
    public static final int MF_HSZ_INFO = 16777216;
    public static final int MF_SENDMSGS = 33554432;
    public static final int MF_POSTMSGS = 67108864;
    public static final int MF_CALLBACKS = 134217728;
    public static final int MF_ERRORS = 268435456;
    public static final int MF_LINKS = 536870912;
    public static final int MF_CONV = 1073741824;
    public static final int MF_MASK = -16777216;
    public static final int EC_ENABLEALL = 0;
    public static final int EC_ENABLEONE = 128;
    public static final int EC_DISABLE = 8;
    public static final int EC_QUERYWAITING = 2;
    public static final int DNS_REGISTER = 1;
    public static final int DNS_UNREGISTER = 2;
    public static final int DNS_FILTERON = 4;
    public static final int DNS_FILTEROFF = 8;
    
    int DdeInitialize(final WinDef.DWORDByReference p0, final DdeCallback p1, final int p2, final int p3);
    
    boolean DdeUninitialize(final int p0);
    
    HCONVLIST DdeConnectList(final int p0, final HSZ p1, final HSZ p2, final HCONVLIST p3, final CONVCONTEXT p4);
    
    HCONV DdeQueryNextServer(final HCONVLIST p0, final HCONV p1);
    
    boolean DdeDisconnectList(final HCONVLIST p0);
    
    HCONV DdeConnect(final int p0, final HSZ p1, final HSZ p2, final CONVCONTEXT p3);
    
    boolean DdeDisconnect(final HCONV p0);
    
    HCONV DdeReconnect(final HCONV p0);
    
    int DdeQueryConvInfo(final HCONV p0, final int p1, final CONVINFO p2);
    
    boolean DdeSetUserHandle(final HCONV p0, final int p1, final BaseTSD.DWORD_PTR p2);
    
    boolean DdeAbandonTransaction(final int p0, final HCONV p1, final int p2);
    
    boolean DdePostAdvise(final int p0, final HSZ p1, final HSZ p2);
    
    boolean DdeEnableCallback(final int p0, final HCONV p1, final int p2);
    
    boolean DdeImpersonateClient(final HCONV p0);
    
    HDDEDATA DdeNameService(final int p0, final HSZ p1, final HSZ p2, final int p3);
    
    HDDEDATA DdeClientTransaction(final Pointer p0, final int p1, final HCONV p2, final HSZ p3, final int p4, final int p5, final int p6, final WinDef.DWORDByReference p7);
    
    HDDEDATA DdeCreateDataHandle(final int p0, final Pointer p1, final int p2, final int p3, final HSZ p4, final int p5, final int p6);
    
    HDDEDATA DdeAddData(final HDDEDATA p0, final Pointer p1, final int p2, final int p3);
    
    int DdeGetData(final HDDEDATA p0, final Pointer p1, final int p2, final int p3);
    
    Pointer DdeAccessData(final HDDEDATA p0, final WinDef.DWORDByReference p1);
    
    boolean DdeUnaccessData(final HDDEDATA p0);
    
    boolean DdeFreeDataHandle(final HDDEDATA p0);
    
    int DdeGetLastError(final int p0);
    
    HSZ DdeCreateStringHandle(final int p0, final String p1, final int p2);
    
    int DdeQueryString(final int p0, final HSZ p1, final Pointer p2, final int p3, final int p4);
    
    boolean DdeFreeStringHandle(final int p0, final HSZ p1);
    
    boolean DdeKeepStringHandle(final int p0, final HSZ p1);
    
    public static class HCONVLIST extends PointerType
    {
    }
    
    public static class HCONV extends PointerType
    {
    }
    
    public static class HSZ extends PointerType
    {
    }
    
    public static class HDDEDATA extends WinDef.PVOID
    {
    }
    
    @FieldOrder({ "service", "topic" })
    public static class HSZPAIR extends Structure
    {
        public HSZ service;
        public HSZ topic;
        
        public HSZPAIR() {
        }
        
        public HSZPAIR(final HSZ service, final HSZ topic) {
            this.service = service;
            this.topic = topic;
        }
    }
    
    @FieldOrder({ "cb", "wFlags", "wCountryID", "iCodePage", "dwLangID", "dwSecurity", "qos" })
    public static class CONVCONTEXT extends Structure
    {
        public int cb;
        public int wFlags;
        public int wCountryID;
        public int iCodePage;
        public int dwLangID;
        public int dwSecurity;
        public WinNT.SECURITY_QUALITY_OF_SERVICE qos;
        
        public CONVCONTEXT() {
        }
        
        public CONVCONTEXT(final Pointer p) {
            super(p);
        }
        
        @Override
        public void write() {
            this.cb = this.size();
            super.write();
        }
    }
    
    @FieldOrder({ "cb", "hUser", "hConvPartner", "hszSvcPartner", "hszServiceReq", "hszTopic", "hszItem", "wFmt", "wType", "wStatus", "wConvst", "wLastError", "hConvList", "ConvCtxt", "hwnd", "hwndPartner" })
    public static class CONVINFO extends Structure
    {
        public int cb;
        public BaseTSD.DWORD_PTR hUser;
        public HCONV hConvPartner;
        public HSZ hszSvcPartner;
        public HSZ hszServiceReq;
        public HSZ hszTopic;
        public HSZ hszItem;
        public int wFmt;
        public int wType;
        public int wStatus;
        public int wConvst;
        public int wLastError;
        public HCONVLIST hConvList;
        public CONVCONTEXT ConvCtxt;
        public WinDef.HWND hwnd;
        public WinDef.HWND hwndPartner;
        
        @Override
        public void write() {
            this.cb = this.size();
            super.write();
        }
    }
    
    @FieldOrder({ "cb", "dwTime", "hTask", "dwRet", "wType", "wFmt", "hConv", "hsz1", "hsz2", "hData", "dwData1", "dwData2", "cc", "cbData", "Data" })
    public static class MONCBSTRUCT extends Structure
    {
        public int cb;
        public int dwTime;
        public WinNT.HANDLE hTask;
        public WinDef.DWORD dwRet;
        public int wType;
        public int wFmt;
        public HCONV hConv;
        public HSZ hsz1;
        public HSZ hsz2;
        public HDDEDATA hData;
        public BaseTSD.ULONG_PTR dwData1;
        public BaseTSD.ULONG_PTR dwData2;
        public CONVCONTEXT cc;
        public int cbData;
        public byte[] Data;
        
        public MONCBSTRUCT() {
            this.Data = new byte[32];
        }
    }
    
    @FieldOrder({ "cb", "fConnect", "dwTime", "hTask", "hszSvc", "hszTopic", "hConvClient", "hConvServer" })
    public static class MONCONVSTRUCT extends Structure
    {
        public WinDef.UINT cb;
        public WinDef.BOOL fConnect;
        public WinDef.DWORD dwTime;
        public WinNT.HANDLE hTask;
        public HSZ hszSvc;
        public HSZ hszTopic;
        public HCONV hConvClient;
        public HCONV hConvServer;
    }
    
    @FieldOrder({ "cb", "wLastError", "dwTime", "hTask" })
    public static class MONERRSTRUCT extends Structure
    {
        public int cb;
        public int wLastError;
        public int dwTime;
        public WinNT.HANDLE hTask;
    }
    
    @FieldOrder({ "cb", "fsAction", "dwTime", "hsz", "hTask", "str" })
    public static class MONHSZSTRUCT extends Structure
    {
        public int cb;
        public int fsAction;
        public int dwTime;
        public HSZ hsz;
        public WinNT.HANDLE hTask;
        public byte[] str;
        
        public MONHSZSTRUCT() {
            this.str = new byte[1];
        }
        
        @Override
        public void write() {
            this.cb = this.calculateSize(true);
            super.write();
        }
        
        @Override
        public void read() {
            this.readField("cb");
            this.allocateMemory(this.cb);
            super.read();
        }
        
        public String getStr() {
            final int offset = this.fieldOffset("str");
            if (W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) {
                return this.getPointer().getWideString(offset);
            }
            return this.getPointer().getString(offset);
        }
    }
    
    @FieldOrder({ "cb", "dwTime", "hTask", "fEstablished", "fNoData", "hszSvc", "hszTopic", "hszItem", "wFmt", "fServer", "hConvServer", "hConvClient" })
    public static class MONLINKSTRUCT extends Structure
    {
        public int cb;
        public int dwTime;
        public WinNT.HANDLE hTask;
        public WinDef.BOOL fEstablished;
        public WinDef.BOOL fNoData;
        public HSZ hszSvc;
        public HSZ hszTopic;
        public HSZ hszItem;
        public int wFmt;
        public WinDef.BOOL fServer;
        public HCONV hConvServer;
        public HCONV hConvClient;
    }
    
    @FieldOrder({ "cb", "hwndTo", "dwTime", "hTask", "wMsg", "wParam", "lParam", "dmhd" })
    public static class MONMSGSTRUCT extends Structure
    {
        public int cb;
        public WinDef.HWND hwndTo;
        public int dwTime;
        public WinNT.HANDLE hTask;
        public int wMsg;
        public WinDef.WPARAM wParam;
        public WinDef.LPARAM lParam;
        public DDEML_MSG_HOOK_DATA dmhd;
    }
    
    @FieldOrder({ "uiLo", "uiHi", "cbData", "Data" })
    public static class DDEML_MSG_HOOK_DATA extends Structure
    {
        public WinDef.UINT_PTR uiLo;
        public WinDef.UINT_PTR uiHi;
        public int cbData;
        public byte[] Data;
        
        public DDEML_MSG_HOOK_DATA() {
            this.Data = new byte[32];
        }
    }
    
    public interface DdeCallback extends StdCallCallback
    {
        WinDef.PVOID ddeCallback(final int p0, final int p1, final HCONV p2, final HSZ p3, final HSZ p4, final HDDEDATA p5, final BaseTSD.ULONG_PTR p6, final BaseTSD.ULONG_PTR p7);
    }
}
