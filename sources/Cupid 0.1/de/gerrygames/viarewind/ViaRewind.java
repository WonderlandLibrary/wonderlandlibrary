package de.gerrygames.viarewind;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

public class ViaRewind {
  private static ViaRewindPlatform platform;
  
  private static ViaRewindConfig config;
  
  public static ViaRewindPlatform getPlatform() {
    return platform;
  }
  
  public static ViaRewindConfig getConfig() {
    return config;
  }
  
  public static void init(ViaRewindPlatform platform, ViaRewindConfig config) {
    ViaRewind.platform = platform;
    ViaRewind.config = config;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\ViaRewind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */