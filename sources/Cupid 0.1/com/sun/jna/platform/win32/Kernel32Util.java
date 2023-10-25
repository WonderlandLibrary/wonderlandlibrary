package com.sun.jna.platform.win32;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.List;

public abstract class Kernel32Util implements WinDef {
  public static String getComputerName() {
    char[] buffer = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
    IntByReference lpnSize = new IntByReference(buffer.length);
    if (!Kernel32.INSTANCE.GetComputerName(buffer, lpnSize))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return Native.toString(buffer);
  }
  
  public static String formatMessageFromHR(WinNT.HRESULT code) {
    PointerByReference buffer = new PointerByReference();
    if (0 == Kernel32.INSTANCE.FormatMessage(4864, (Pointer)null, code.intValue(), 0, buffer, 0, (Pointer)null))
      throw new LastErrorException(Kernel32.INSTANCE.GetLastError()); 
    String s = buffer.getValue().getString(0L, !Boolean.getBoolean("w32.ascii"));
    Kernel32.INSTANCE.LocalFree(buffer.getValue());
    return s.trim();
  }
  
  public static String formatMessageFromLastErrorCode(int code) {
    return formatMessageFromHR(W32Errors.HRESULT_FROM_WIN32(code));
  }
  
  public static String getTempPath() {
    WinDef.DWORD nBufferLength = new WinDef.DWORD(260L);
    char[] buffer = new char[nBufferLength.intValue()];
    if (Kernel32.INSTANCE.GetTempPath(nBufferLength, buffer).intValue() == 0)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return Native.toString(buffer);
  }
  
  public static void deleteFile(String filename) {
    if (!Kernel32.INSTANCE.DeleteFile(filename))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
  }
  
  public static String[] getLogicalDriveStrings() {
    WinDef.DWORD dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(new WinDef.DWORD(0L), null);
    if (dwSize.intValue() <= 0)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    char[] buf = new char[dwSize.intValue()];
    dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(dwSize, buf);
    if (dwSize.intValue() <= 0)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    List<String> drives = new ArrayList<String>();
    String drive = "";
    for (int i = 0; i < buf.length - 1; i++) {
      if (buf[i] == '\000') {
        drives.add(drive);
        drive = "";
      } else {
        drive = drive + buf[i];
      } 
    } 
    return drives.<String>toArray(new String[0]);
  }
  
  public static int getFileAttributes(String fileName) {
    int fileAttributes = Kernel32.INSTANCE.GetFileAttributes(fileName);
    if (fileAttributes == -1)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return fileAttributes;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Kernel32Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */