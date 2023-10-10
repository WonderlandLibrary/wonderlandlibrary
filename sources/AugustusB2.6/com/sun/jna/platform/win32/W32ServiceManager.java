// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.io.Closeable;

public class W32ServiceManager implements Closeable
{
    Winsvc.SC_HANDLE _handle;
    String _machineName;
    String _databaseName;
    
    public W32ServiceManager() {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
    }
    
    public W32ServiceManager(final int permissions) {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
        this.open(permissions);
    }
    
    public W32ServiceManager(final String machineName, final String databaseName) {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
        this._machineName = machineName;
        this._databaseName = databaseName;
    }
    
    public W32ServiceManager(final String machineName, final String databaseName, final int permissions) {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
        this._machineName = machineName;
        this._databaseName = databaseName;
        this.open(permissions);
    }
    
    public void open(final int permissions) {
        this.close();
        this._handle = Advapi32.INSTANCE.OpenSCManager(this._machineName, this._databaseName, permissions);
        if (this._handle == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
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
    
    public W32Service openService(final String serviceName, final int permissions) {
        final Winsvc.SC_HANDLE serviceHandle = Advapi32.INSTANCE.OpenService(this._handle, serviceName, permissions);
        if (serviceHandle == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new W32Service(serviceHandle);
    }
    
    public Winsvc.SC_HANDLE getHandle() {
        return this._handle;
    }
    
    public Winsvc.ENUM_SERVICE_STATUS_PROCESS[] enumServicesStatusExProcess(final int dwServiceType, final int dwServiceState, final String groupName) {
        final IntByReference pcbBytesNeeded = new IntByReference(0);
        final IntByReference lpServicesReturned = new IntByReference(0);
        final IntByReference lpResumeHandle = new IntByReference(0);
        Advapi32.INSTANCE.EnumServicesStatusEx(this._handle, 0, dwServiceType, dwServiceState, Pointer.NULL, 0, pcbBytesNeeded, lpServicesReturned, lpResumeHandle, groupName);
        final int lastError = Kernel32.INSTANCE.GetLastError();
        if (lastError != 234) {
            throw new Win32Exception(lastError);
        }
        final Memory buffer = new Memory(pcbBytesNeeded.getValue());
        final boolean result = Advapi32.INSTANCE.EnumServicesStatusEx(this._handle, 0, dwServiceType, dwServiceState, buffer, (int)buffer.size(), pcbBytesNeeded, lpServicesReturned, lpResumeHandle, groupName);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (lpServicesReturned.getValue() == 0) {
            return new Winsvc.ENUM_SERVICE_STATUS_PROCESS[0];
        }
        final Winsvc.ENUM_SERVICE_STATUS_PROCESS status = Structure.newInstance(Winsvc.ENUM_SERVICE_STATUS_PROCESS.class, buffer);
        status.read();
        return (Winsvc.ENUM_SERVICE_STATUS_PROCESS[])status.toArray(lpServicesReturned.getValue());
    }
}
