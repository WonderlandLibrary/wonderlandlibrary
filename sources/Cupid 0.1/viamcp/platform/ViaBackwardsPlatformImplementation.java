package viamcp.platform;

import java.io.File;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.ViaBackwardsConfig;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import us.myles.ViaVersion.api.Via;

public class ViaBackwardsPlatformImplementation implements ViaBackwardsPlatform {
  public ViaBackwardsPlatformImplementation() {
    ViaBackwards.init(this, new ViaBackwardsConfig() {
          public boolean addCustomEnchantsToLore() {
            return true;
          }
          
          public boolean addTeamColorTo1_13Prefix() {
            return true;
          }
          
          public boolean isFix1_13FacePlayer() {
            return true;
          }
          
          public boolean alwaysShowOriginalMobName() {
            return true;
          }
        });
    init((Minecraft.getMinecraft()).mcDataDir);
  }
  
  public Logger getLogger() {
    return Via.getPlatform().getLogger();
  }
  
  public void disable() {}
  
  public boolean isOutdated() {
    return false;
  }
  
  public File getDataFolder() {
    return (Minecraft.getMinecraft()).mcDataDir;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\viamcp\platform\ViaBackwardsPlatformImplementation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */