package oshi.software.os.linux;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.linux.proc.OSVersionInfoEx;

public class LinuxOperatingSystem implements OperatingSystem {
  private OperatingSystemVersion _version = null;
  
  private String _family = null;
  
  public String getFamily() {
    if (this._family == null) {
      Scanner in;
      try {
        in = new Scanner(new FileReader("/etc/os-release"));
      } catch (FileNotFoundException e) {
        return "";
      } 
      in.useDelimiter("\n");
      while (in.hasNext()) {
        String[] splittedLine = in.next().split("=");
        if (splittedLine[0].equals("NAME")) {
          this._family = splittedLine[1].replaceAll("^\"|\"$", "");
          break;
        } 
      } 
      in.close();
    } 
    return this._family;
  }
  
  public String getManufacturer() {
    return "GNU/Linux";
  }
  
  public OperatingSystemVersion getVersion() {
    if (this._version == null)
      this._version = (OperatingSystemVersion)new OSVersionInfoEx(); 
    return this._version;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\oshi\software\os\linux\LinuxOperatingSystem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */