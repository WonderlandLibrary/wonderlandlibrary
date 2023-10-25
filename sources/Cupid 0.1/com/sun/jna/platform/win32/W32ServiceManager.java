package com.sun.jna.platform.win32;

public class W32ServiceManager {
  Winsvc.SC_HANDLE _handle = null;
  
  String _machineName = null;
  
  String _databaseName = null;
  
  public W32ServiceManager() {}
  
  public W32ServiceManager(String machineName, String databaseName) {
    this._machineName = machineName;
    this._databaseName = databaseName;
  }
  
  public void open(int permissions) {
    close();
    this._handle = Advapi32.INSTANCE.OpenSCManager(this._machineName, this._databaseName, permissions);
    if (this._handle == null)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
  }
  
  public void close() {
    if (this._handle != null) {
      if (!Advapi32.INSTANCE.CloseServiceHandle(this._handle))
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
      this._handle = null;
    } 
  }
  
  public W32Service openService(String serviceName, int permissions) {
    Winsvc.SC_HANDLE serviceHandle = Advapi32.INSTANCE.OpenService(this._handle, serviceName, permissions);
    if (serviceHandle == null)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return new W32Service(serviceHandle);
  }
  
  public Winsvc.SC_HANDLE getHandle() {
    return this._handle;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\W32ServiceManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */