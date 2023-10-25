package oshi.software.os.windows;

import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.windows.nt.OSVersionInfoEx;

public class WindowsOperatingSystem implements OperatingSystem {
  private OperatingSystemVersion _version = null;
  
  public OperatingSystemVersion getVersion() {
    if (this._version == null)
      this._version = (OperatingSystemVersion)new OSVersionInfoEx(); 
    return this._version;
  }
  
  public String getFamily() {
    return "Windows";
  }
  
  public String getManufacturer() {
    return "Microsoft";
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getManufacturer());
    sb.append(" ");
    sb.append(getFamily());
    sb.append(" ");
    sb.append(getVersion().toString());
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\oshi\software\os\windows\WindowsOperatingSystem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */