package nl.matsv.viabackwards;

import com.google.common.base.Preconditions;
import nl.matsv.viabackwards.api.ViaBackwardsConfig;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;

public class ViaBackwards {
  private static ViaBackwardsPlatform platform;
  
  private static ViaBackwardsConfig config;
  
  public static void init(ViaBackwardsPlatform platform, ViaBackwardsConfig config) {
    Preconditions.checkArgument((platform != null), "ViaBackwards is already initialized");
    ViaBackwards.platform = platform;
    ViaBackwards.config = config;
  }
  
  public static ViaBackwardsPlatform getPlatform() {
    return platform;
  }
  
  public static ViaBackwardsConfig getConfig() {
    return config;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\ViaBackwards.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */