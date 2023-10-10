// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.Iterator;
import java.util.List;
import com.sun.jna.ptr.IntByReference;
import java.io.Closeable;

public class W32Service implements Closeable
{
    Winsvc.SC_HANDLE _handle;
    
    public W32Service(final Winsvc.SC_HANDLE handle) {
        this._handle = null;
        this._handle = handle;
    }
    
    @Override
    public void close() {
        if (this._handle != null) {
            if (!Advapi32.INSTANCE.CloseServiceHandle(this._handle)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            this._handle = null;
        }
    }
    
    private void addShutdownPrivilegeToProcess() {
        final WinNT.HANDLEByReference hToken = new WinNT.HANDLEByReference();
        final WinNT.LUID luid = new WinNT.LUID();
        Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 32, hToken);
        Advapi32.INSTANCE.LookupPrivilegeValue("", "SeShutdownPrivilege", luid);
        final WinNT.TOKEN_PRIVILEGES tp = new WinNT.TOKEN_PRIVILEGES(1);
        tp.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(luid, new WinDef.DWORD(2L));
        Advapi32.INSTANCE.AdjustTokenPrivileges(hToken.getValue(), false, tp, tp.size(), null, new IntByReference());
    }
    
    public void setFailureActions(final List<Winsvc.SC_ACTION> actions, final int resetPeriod, final String rebootMsg, final String command) {
        final Winsvc.SERVICE_FAILURE_ACTIONS.ByReference actionStruct = new Winsvc.SERVICE_FAILURE_ACTIONS.ByReference();
        actionStruct.dwResetPeriod = resetPeriod;
        actionStruct.lpRebootMsg = rebootMsg;
        actionStruct.lpCommand = command;
        actionStruct.cActions = actions.size();
        actionStruct.lpsaActions = new Winsvc.SC_ACTION.ByReference();
        final Winsvc.SC_ACTION[] actionArray = (Winsvc.SC_ACTION[])actionStruct.lpsaActions.toArray(actions.size());
        boolean hasShutdownPrivilege = false;
        int i = 0;
        for (final Winsvc.SC_ACTION action : actions) {
            if (!hasShutdownPrivilege && action.type == 2) {
                this.addShutdownPrivilegeToProcess();
                hasShutdownPrivilege = true;
            }
            actionArray[i].type = action.type;
            actionArray[i].delay = action.delay;
            ++i;
        }
        if (!Advapi32.INSTANCE.ChangeServiceConfig2(this._handle, 2, actionStruct)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    private Pointer queryServiceConfig2(final int type) {
        final IntByReference bufferSize = new IntByReference();
        Advapi32.INSTANCE.QueryServiceConfig2(this._handle, type, Pointer.NULL, 0, bufferSize);
        final Pointer buffer = new Memory(bufferSize.getValue());
        if (!Advapi32.INSTANCE.QueryServiceConfig2(this._handle, type, buffer, bufferSize.getValue(), new IntByReference())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return buffer;
    }
    
    public Winsvc.SERVICE_FAILURE_ACTIONS getFailureActions() {
        final Pointer buffer = this.queryServiceConfig2(2);
        final Winsvc.SERVICE_FAILURE_ACTIONS result = new Winsvc.SERVICE_FAILURE_ACTIONS(buffer);
        return result;
    }
    
    public void setFailureActionsFlag(final boolean flagValue) {
        final Winsvc.SERVICE_FAILURE_ACTIONS_FLAG flag = new Winsvc.SERVICE_FAILURE_ACTIONS_FLAG();
        flag.fFailureActionsOnNonCrashFailures = (flagValue ? 1 : 0);
        if (!Advapi32.INSTANCE.ChangeServiceConfig2(this._handle, 4, flag)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public boolean getFailureActionsFlag() {
        final Pointer buffer = this.queryServiceConfig2(4);
        final Winsvc.SERVICE_FAILURE_ACTIONS_FLAG result = new Winsvc.SERVICE_FAILURE_ACTIONS_FLAG(buffer);
        return result.fFailureActionsOnNonCrashFailures != 0;
    }
    
    public Winsvc.SERVICE_STATUS_PROCESS queryStatus() {
        final IntByReference size = new IntByReference();
        Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, null, 0, size);
        final Winsvc.SERVICE_STATUS_PROCESS status = new Winsvc.SERVICE_STATUS_PROCESS(size.getValue());
        if (!Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, status, status.size(), size)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return status;
    }
    
    public void startService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 4) {
            return;
        }
        if (!Advapi32.INSTANCE.StartService(this._handle, 0, null)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 4) {
            throw new RuntimeException("Unable to start the service");
        }
    }
    
    public void stopService() {
        this.stopService(30000L);
    }
    
    public void stopService(final long timeout) {
        final long startTime = System.currentTimeMillis();
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 1) {
            return;
        }
        final Winsvc.SERVICE_STATUS status = new Winsvc.SERVICE_STATUS();
        if (!Advapi32.INSTANCE.ControlService(this._handle, 1, status)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        while (status.dwCurrentState != 1) {
            final long msRemainingBeforeTimeout = timeout - (System.currentTimeMillis() - startTime);
            if (msRemainingBeforeTimeout < 0L) {
                throw new RuntimeException(String.format("Service stop exceeded timeout time of %d ms", timeout));
            }
            final long dwWaitTime = Math.min(this.sanitizeWaitTime(status.dwWaitHint), msRemainingBeforeTimeout);
            try {
                Thread.sleep(dwWaitTime);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!Advapi32.INSTANCE.QueryServiceStatus(this._handle, status)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }
    
    public void continueService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 4) {
            return;
        }
        if (!Advapi32.INSTANCE.ControlService(this._handle, 3, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 4) {
            throw new RuntimeException("Unable to continue the service");
        }
    }
    
    public void pauseService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 7) {
            return;
        }
        if (!Advapi32.INSTANCE.ControlService(this._handle, 2, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 7) {
            throw new RuntimeException("Unable to pause the service");
        }
    }
    
    int sanitizeWaitTime(final int dwWaitHint) {
        int dwWaitTime = dwWaitHint / 10;
        if (dwWaitTime < 1000) {
            dwWaitTime = 1000;
        }
        else if (dwWaitTime > 10000) {
            dwWaitTime = 10000;
        }
        return dwWaitTime;
    }
    
    public void waitForNonPendingState() {
        Winsvc.SERVICE_STATUS_PROCESS status = this.queryStatus();
        int previousCheckPoint = status.dwCheckPoint;
        int checkpointStartTickCount = Kernel32.INSTANCE.GetTickCount();
        while (this.isPendingState(status.dwCurrentState)) {
            if (status.dwCheckPoint != previousCheckPoint) {
                previousCheckPoint = status.dwCheckPoint;
                checkpointStartTickCount = Kernel32.INSTANCE.GetTickCount();
            }
            if (Kernel32.INSTANCE.GetTickCount() - checkpointStartTickCount > status.dwWaitHint) {
                throw new RuntimeException("Timeout waiting for service to change to a non-pending state.");
            }
            final int dwWaitTime = this.sanitizeWaitTime(status.dwWaitHint);
            try {
                Thread.sleep(dwWaitTime);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            status = this.queryStatus();
        }
    }
    
    private boolean isPendingState(final int state) {
        switch (state) {
            case 2:
            case 3:
            case 5:
            case 6: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public Winsvc.SC_HANDLE getHandle() {
        return this._handle;
    }
    
    public Winsvc.ENUM_SERVICE_STATUS[] enumDependentServices(final int dwServiceState) {
        final IntByReference pcbBytesNeeded = new IntByReference(0);
        final IntByReference lpServicesReturned = new IntByReference(0);
        Advapi32.INSTANCE.EnumDependentServices(this._handle, dwServiceState, Pointer.NULL, 0, pcbBytesNeeded, lpServicesReturned);
        final int lastError = Kernel32.INSTANCE.GetLastError();
        if (lastError != 234) {
            throw new Win32Exception(lastError);
        }
        final Memory buffer = new Memory(pcbBytesNeeded.getValue());
        final boolean result = Advapi32.INSTANCE.EnumDependentServices(this._handle, dwServiceState, buffer, (int)buffer.size(), pcbBytesNeeded, lpServicesReturned);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (lpServicesReturned.getValue() == 0) {
            return new Winsvc.ENUM_SERVICE_STATUS[0];
        }
        final Winsvc.ENUM_SERVICE_STATUS status = Structure.newInstance(Winsvc.ENUM_SERVICE_STATUS.class, buffer);
        status.read();
        return (Winsvc.ENUM_SERVICE_STATUS[])status.toArray(lpServicesReturned.getValue());
    }
}
