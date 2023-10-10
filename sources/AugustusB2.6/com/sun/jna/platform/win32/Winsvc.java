// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.Structure;

public interface Winsvc
{
    public static final int SERVICE_RUNS_IN_SYSTEM_PROCESS = 1;
    public static final int SC_MANAGER_CONNECT = 1;
    public static final int SC_MANAGER_CREATE_SERVICE = 2;
    public static final int SC_MANAGER_ENUMERATE_SERVICE = 4;
    public static final int SC_MANAGER_LOCK = 8;
    public static final int SC_MANAGER_QUERY_LOCK_STATUS = 16;
    public static final int SC_MANAGER_MODIFY_BOOT_CONFIG = 32;
    public static final int SC_MANAGER_ALL_ACCESS = 983103;
    public static final int SERVICE_QUERY_CONFIG = 1;
    public static final int SERVICE_CHANGE_CONFIG = 2;
    public static final int SERVICE_QUERY_STATUS = 4;
    public static final int SERVICE_ENUMERATE_DEPENDENTS = 8;
    public static final int SERVICE_START = 16;
    public static final int SERVICE_STOP = 32;
    public static final int SERVICE_PAUSE_CONTINUE = 64;
    public static final int SERVICE_INTERROGATE = 128;
    public static final int SERVICE_USER_DEFINED_CONTROL = 256;
    public static final int SERVICE_ALL_ACCESS = 983551;
    public static final int SERVICE_ACTIVE = 1;
    public static final int SERVICE_INACTIVE = 2;
    public static final int SERVICE_STATE_ALL = 3;
    public static final int SERVICE_CONTROL_STOP = 1;
    public static final int SERVICE_CONTROL_PAUSE = 2;
    public static final int SERVICE_CONTROL_CONTINUE = 3;
    public static final int SERVICE_CONTROL_INTERROGATE = 4;
    public static final int SERVICE_CONTROL_SHUTDOWN = 5;
    public static final int SERVICE_CONTROL_PARAMCHANGE = 6;
    public static final int SERVICE_CONTROL_NETBINDADD = 7;
    public static final int SERVICE_CONTROL_NETBINDREMOVE = 8;
    public static final int SERVICE_CONTROL_NETBINDENABLE = 9;
    public static final int SERVICE_CONTROL_NETBINDDISABLE = 10;
    public static final int SERVICE_CONTROL_DEVICEEVENT = 11;
    public static final int SERVICE_CONTROL_HARDWAREPROFILECHANGE = 12;
    public static final int SERVICE_CONTROL_POWEREVENT = 13;
    public static final int SERVICE_CONTROL_SESSIONCHANGE = 14;
    public static final int SERVICE_CONTROL_PRESHUTDOWN = 15;
    public static final int SERVICE_CONTROL_TIMECHANGE = 16;
    public static final int SERVICE_CONTROL_TRIGGEREVENT = 32;
    public static final int SERVICE_CONTROL_USERMODEREBOOT = 64;
    public static final int SERVICE_STOPPED = 1;
    public static final int SERVICE_START_PENDING = 2;
    public static final int SERVICE_STOP_PENDING = 3;
    public static final int SERVICE_RUNNING = 4;
    public static final int SERVICE_CONTINUE_PENDING = 5;
    public static final int SERVICE_PAUSE_PENDING = 6;
    public static final int SERVICE_PAUSED = 7;
    public static final int SERVICE_ACCEPT_STOP = 1;
    public static final int SERVICE_ACCEPT_PAUSE_CONTINUE = 2;
    public static final int SERVICE_ACCEPT_SHUTDOWN = 4;
    public static final int SERVICE_ACCEPT_PARAMCHANGE = 8;
    public static final int SERVICE_ACCEPT_NETBINDCHANGE = 16;
    public static final int SERVICE_ACCEPT_HARDWAREPROFILECHANGE = 32;
    public static final int SERVICE_ACCEPT_POWEREVENT = 64;
    public static final int SERVICE_ACCEPT_SESSIONCHANGE = 128;
    public static final int SERVICE_ACCEPT_PRESHUTDOWN = 256;
    public static final int SERVICE_ACCEPT_TIMECHANGE = 512;
    public static final int SERVICE_ACCEPT_TRIGGEREVENT = 1024;
    public static final int SERVICE_CONFIG_DESCRIPTION = 1;
    public static final int SERVICE_CONFIG_FAILURE_ACTIONS = 2;
    public static final int SERVICE_CONFIG_DELAYED_AUTO_START_INFO = 3;
    public static final int SERVICE_CONFIG_FAILURE_ACTIONS_FLAG = 4;
    public static final int SERVICE_CONFIG_SERVICE_SID_INFO = 5;
    public static final int SERVICE_CONFIG_REQUIRED_PRIVILEGES_INFO = 6;
    public static final int SERVICE_CONFIG_PRESHUTDOWN_INFO = 7;
    public static final int SERVICE_CONFIG_TRIGGER_INFO = 8;
    public static final int SERVICE_CONFIG_PREFERRED_NODE = 9;
    public static final int SERVICE_CONFIG_LAUNCH_PROTECTED = 12;
    public static final int SC_ACTION_NONE = 0;
    public static final int SC_ACTION_RESTART = 1;
    public static final int SC_ACTION_REBOOT = 2;
    public static final int SC_ACTION_RUN_COMMAND = 3;
    public static final int SC_ENUM_PROCESS_INFO = 0;
    
    @FieldOrder({ "dwServiceType", "dwCurrentState", "dwControlsAccepted", "dwWin32ExitCode", "dwServiceSpecificExitCode", "dwCheckPoint", "dwWaitHint" })
    public static class SERVICE_STATUS extends Structure
    {
        public int dwServiceType;
        public int dwCurrentState;
        public int dwControlsAccepted;
        public int dwWin32ExitCode;
        public int dwServiceSpecificExitCode;
        public int dwCheckPoint;
        public int dwWaitHint;
    }
    
    @FieldOrder({ "dwServiceType", "dwCurrentState", "dwControlsAccepted", "dwWin32ExitCode", "dwServiceSpecificExitCode", "dwCheckPoint", "dwWaitHint", "dwProcessId", "dwServiceFlags" })
    public static class SERVICE_STATUS_PROCESS extends Structure
    {
        public int dwServiceType;
        public int dwCurrentState;
        public int dwControlsAccepted;
        public int dwWin32ExitCode;
        public int dwServiceSpecificExitCode;
        public int dwCheckPoint;
        public int dwWaitHint;
        public int dwProcessId;
        public int dwServiceFlags;
        
        public SERVICE_STATUS_PROCESS() {
        }
        
        public SERVICE_STATUS_PROCESS(final int size) {
            super(new Memory(size));
        }
    }
    
    public abstract static class ChangeServiceConfig2Info extends Structure
    {
        public ChangeServiceConfig2Info() {
            super(Boolean.getBoolean("w32.ascii") ? W32APITypeMapper.ASCII : W32APITypeMapper.UNICODE);
        }
        
        public ChangeServiceConfig2Info(final Pointer p) {
            super(p, 0, Boolean.getBoolean("w32.ascii") ? W32APITypeMapper.ASCII : W32APITypeMapper.UNICODE);
        }
    }
    
    @FieldOrder({ "dwResetPeriod", "lpRebootMsg", "lpCommand", "cActions", "lpsaActions" })
    public static class SERVICE_FAILURE_ACTIONS extends ChangeServiceConfig2Info
    {
        public int dwResetPeriod;
        public String lpRebootMsg;
        public String lpCommand;
        public int cActions;
        public SC_ACTION.ByReference lpsaActions;
        
        public SERVICE_FAILURE_ACTIONS() {
        }
        
        public SERVICE_FAILURE_ACTIONS(final Pointer p) {
            super(p);
            this.read();
        }
        
        public static class ByReference extends SERVICE_FAILURE_ACTIONS implements Structure.ByReference
        {
        }
    }
    
    @FieldOrder({ "type", "delay" })
    public static class SC_ACTION extends Structure
    {
        public int type;
        public int delay;
        
        public static class ByReference extends SC_ACTION implements Structure.ByReference
        {
        }
    }
    
    @FieldOrder({ "fFailureActionsOnNonCrashFailures" })
    public static class SERVICE_FAILURE_ACTIONS_FLAG extends ChangeServiceConfig2Info
    {
        public int fFailureActionsOnNonCrashFailures;
        
        public SERVICE_FAILURE_ACTIONS_FLAG() {
        }
        
        public SERVICE_FAILURE_ACTIONS_FLAG(final Pointer p) {
            super(p);
            this.read();
        }
    }
    
    public static class SC_HANDLE extends WinNT.HANDLE
    {
    }
    
    public abstract static class SC_STATUS_TYPE
    {
        public static final int SC_STATUS_PROCESS_INFO = 0;
    }
    
    @FieldOrder({ "lpServiceName", "lpServiceProc" })
    public static class SERVICE_TABLE_ENTRY extends Structure
    {
        public String lpServiceName;
        public SERVICE_MAIN_FUNCTION lpServiceProc;
        
        public SERVICE_TABLE_ENTRY() {
            super(W32APITypeMapper.DEFAULT);
        }
    }
    
    @FieldOrder({ "lpDescription" })
    public static class SERVICE_DESCRIPTION extends ChangeServiceConfig2Info
    {
        public String lpDescription;
    }
    
    public static class SERVICE_STATUS_HANDLE extends WinNT.HANDLE
    {
        public SERVICE_STATUS_HANDLE() {
        }
        
        public SERVICE_STATUS_HANDLE(final Pointer p) {
            super(p);
        }
    }
    
    @FieldOrder({ "lpServiceName", "lpDisplayName", "ServiceStatus" })
    public static class ENUM_SERVICE_STATUS extends Structure
    {
        public String lpServiceName;
        public String lpDisplayName;
        public SERVICE_STATUS ServiceStatus;
        
        public ENUM_SERVICE_STATUS() {
            super(W32APITypeMapper.DEFAULT);
        }
    }
    
    @FieldOrder({ "lpServiceName", "lpDisplayName", "ServiceStatusProcess" })
    public static class ENUM_SERVICE_STATUS_PROCESS extends Structure
    {
        public String lpServiceName;
        public String lpDisplayName;
        public SERVICE_STATUS_PROCESS ServiceStatusProcess;
        
        public ENUM_SERVICE_STATUS_PROCESS() {
            super(W32APITypeMapper.DEFAULT);
        }
    }
    
    public interface SERVICE_MAIN_FUNCTION extends StdCallLibrary.StdCallCallback
    {
        void callback(final int p0, final Pointer p1);
    }
    
    public interface HandlerEx extends StdCallLibrary.StdCallCallback
    {
        int callback(final int p0, final int p1, final Pointer p2, final Pointer p3);
    }
    
    public interface Handler extends StdCallLibrary.StdCallCallback
    {
        void callback(final int p0);
    }
}
