package viamcp.platform;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.util.logging.Logger;
import us.myles.ViaVersion.api.Via;

public class ViaRewindPlatformImplementation implements ViaRewindPlatform {
  public ViaRewindPlatformImplementation() {
    init(new ViaRewindConfig() {
          public ViaRewindConfig.CooldownIndicator getCooldownIndicator() {
            return ViaRewindConfig.CooldownIndicator.TITLE;
          }
          
          public boolean isReplaceAdventureMode() {
            return true;
          }
          
          public boolean isReplaceParticles() {
            return true;
          }
        });
  }
  
  public Logger getLogger() {
    return Via.getPlatform().getLogger();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\viamcp\platform\ViaRewindPlatformImplementation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */