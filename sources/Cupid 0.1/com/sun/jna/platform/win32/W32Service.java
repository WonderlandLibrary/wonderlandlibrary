package com.sun.jna.platform.win32;

import com.sun.jna.ptr.IntByReference;

public class W32Service {
  Winsvc.SC_HANDLE _handle = null;
  
  public W32Service(Winsvc.SC_HANDLE handle) {
    this._handle = handle;
  }
  
  public void close() {
    if (this._handle != null) {
      if (!Advapi32.INSTANCE.CloseServiceHandle(this._handle))
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
      this._handle = null;
    } 
  }
  
  public Winsvc.SERVICE_STATUS_PROCESS queryStatus() {
    IntByReference size = new IntByReference();
    Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, null, 0, size);
    Winsvc.SERVICE_STATUS_PROCESS status = new Winsvc.SERVICE_STATUS_PROCESS(size.getValue());
    if (!Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, status, status.size(), size))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return status;
  }
  
  public void startService() {
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState == 4)
      return; 
    if (!Advapi32.INSTANCE.StartService(this._handle, 0, null))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState != 4)
      throw new RuntimeException("Unable to start the service"); 
  }
  
  public void stopService() {
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState == 1)
      return; 
    if (!Advapi32.INSTANCE.ControlService(this._handle, 1, new Winsvc.SERVICE_STATUS()))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState != 1)
      throw new RuntimeException("Unable to stop the service"); 
  }
  
  public void continueService() {
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState == 4)
      return; 
    if (!Advapi32.INSTANCE.ControlService(this._handle, 3, new Winsvc.SERVICE_STATUS()))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState != 4)
      throw new RuntimeException("Unable to continue the service"); 
  }
  
  public void pauseService() {
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState == 7)
      return; 
    if (!Advapi32.INSTANCE.ControlService(this._handle, 2, new Winsvc.SERVICE_STATUS()))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    waitForNonPendingState();
    if ((queryStatus()).dwCurrentState != 7)
      throw new RuntimeException("Unable to pause the service"); 
  }
  
  public void waitForNonPendingState() {
    Winsvc.SERVICE_STATUS_PROCESS status = queryStatus();
    int previousCheckPoint = status.dwCheckPoint;
    int checkpointStartTickCount = Kernel32.INSTANCE.GetTickCount();
    while (isPendingState(status.dwCurrentState)) {
      if (status.dwCheckPoint != previousCheckPoint) {
        previousCheckPoint = status.dwCheckPoint;
        checkpointStartTickCount = Kernel32.INSTANCE.GetTickCount();
      } 
      if (Kernel32.INSTANCE.GetTickCount() - checkpointStartTickCount > status.dwWaitHint)
        throw new RuntimeException("Timeout waiting for service to change to a non-pending state."); 
      int dwWaitTime = status.dwWaitHint / 10;
      if (dwWaitTime < 1000) {
        dwWaitTime = 1000;
      } else if (dwWaitTime > 10000) {
        dwWaitTime = 10000;
      } 
      try {
        Thread.sleep(dwWaitTime);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } 
      status = queryStatus();
    } 
  }
  
  private boolean isPendingState(int state) {
    switch (state) {
      case 2:
      case 3:
      case 5:
      case 6:
        return true;
    } 
    return false;
  }
  
  public Winsvc.SC_HANDLE getHandle() {
    return this._handle;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\W32Service.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */