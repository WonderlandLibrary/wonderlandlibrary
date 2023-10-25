package viamcp.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public class ProtocolSorter {
  private static final LinkedList<ProtocolVersion> protocolVersions = new LinkedList<>();
  
  private static int count = 0;
  
  static {
    for (Field f : ProtocolVersion.class.getDeclaredFields()) {
      if (f.getType().equals(ProtocolVersion.class)) {
        count++;
        try {
          ProtocolVersion ver = (ProtocolVersion)f.get(null);
          if (count >= 8 && !ver.getName().equals("UNKNOWN"))
            getProtocolVersions().add(ver); 
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } 
      } 
    } 
    Collections.reverse(getProtocolVersions());
  }
  
  public static LinkedList<ProtocolVersion> getProtocolVersions() {
    return protocolVersions;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\viamc\\utils\ProtocolSorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */