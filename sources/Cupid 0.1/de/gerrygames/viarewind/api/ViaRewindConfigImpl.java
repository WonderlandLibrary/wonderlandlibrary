package de.gerrygames.viarewind.api;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import us.myles.ViaVersion.util.Config;

public class ViaRewindConfigImpl extends Config implements ViaRewindConfig {
  public ViaRewindConfigImpl(File configFile) {
    super(configFile);
    reloadConfig();
  }
  
  public ViaRewindConfig.CooldownIndicator getCooldownIndicator() {
    return ViaRewindConfig.CooldownIndicator.valueOf(getString("cooldown-indicator", "TITLE").toUpperCase());
  }
  
  public boolean isReplaceAdventureMode() {
    return getBoolean("replace-adventure", false);
  }
  
  public boolean isReplaceParticles() {
    return getBoolean("replace-particles", false);
  }
  
  public URL getDefaultConfigURL() {
    return getClass().getClassLoader().getResource("assets/viarewind/config.yml");
  }
  
  protected void handleConfig(Map<String, Object> map) {}
  
  public List<String> getUnsupportedOptions() {
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\api\ViaRewindConfigImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */