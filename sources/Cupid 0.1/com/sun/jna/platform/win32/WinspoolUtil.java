package com.sun.jna.platform.win32;

import com.sun.jna.ptr.IntByReference;

public abstract class WinspoolUtil {
  public static Winspool.PRINTER_INFO_1[] getPrinterInfo1() {
    IntByReference pcbNeeded = new IntByReference();
    IntByReference pcReturned = new IntByReference();
    Winspool.INSTANCE.EnumPrinters(2, null, 1, null, 0, pcbNeeded, pcReturned);
    if (pcbNeeded.getValue() <= 0)
      return new Winspool.PRINTER_INFO_1[0]; 
    Winspool.PRINTER_INFO_1 pPrinterEnum = new Winspool.PRINTER_INFO_1(pcbNeeded.getValue());
    if (!Winspool.INSTANCE.EnumPrinters(2, null, 1, pPrinterEnum.getPointer(), pcbNeeded.getValue(), pcbNeeded, pcReturned))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return (Winspool.PRINTER_INFO_1[])pPrinterEnum.toArray(pcReturned.getValue());
  }
  
  public static Winspool.PRINTER_INFO_4[] getPrinterInfo4() {
    IntByReference pcbNeeded = new IntByReference();
    IntByReference pcReturned = new IntByReference();
    Winspool.INSTANCE.EnumPrinters(2, null, 4, null, 0, pcbNeeded, pcReturned);
    if (pcbNeeded.getValue() <= 0)
      return new Winspool.PRINTER_INFO_4[0]; 
    Winspool.PRINTER_INFO_4 pPrinterEnum = new Winspool.PRINTER_INFO_4(pcbNeeded.getValue());
    if (!Winspool.INSTANCE.EnumPrinters(2, null, 4, pPrinterEnum.getPointer(), pcbNeeded.getValue(), pcbNeeded, pcReturned))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError()); 
    return (Winspool.PRINTER_INFO_4[])pPrinterEnum.toArray(pcReturned.getValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\WinspoolUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */