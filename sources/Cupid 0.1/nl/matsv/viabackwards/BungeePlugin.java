package nl.matsv.viabackwards;

import net.md_5.bungee.api.plugin.Plugin;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import us.myles.ViaVersion.api.Via;

public class BungeePlugin extends Plugin implements ViaBackwardsPlatform {
  public void onLoad() {
    Via.getManager().addEnableListener(() -> init(getDataFolder()));
  }
  
  public void disable() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\BungeePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */