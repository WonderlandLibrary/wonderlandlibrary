package nl.matsv.viabackwards;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import nl.matsv.viabackwards.api.ViaBackwardsConfig;
import us.myles.ViaVersion.util.Config;

public class ViaBackwardsConfig extends Config implements ViaBackwardsConfig {
  private boolean addCustomEnchantsToLore;
  
  private boolean addTeamColorToPrefix;
  
  private boolean fix1_13FacePlayer;
  
  private boolean alwaysShowOriginalMobName;
  
  public ViaBackwardsConfig(File configFile) {
    super(configFile);
  }
  
  public void reloadConfig() {
    super.reloadConfig();
    loadFields();
  }
  
  private void loadFields() {
    this.addCustomEnchantsToLore = getBoolean("add-custom-enchants-into-lore", true);
    this.addTeamColorToPrefix = getBoolean("add-teamcolor-to-prefix", true);
    this.fix1_13FacePlayer = getBoolean("fix-1_13-face-player", false);
    this.alwaysShowOriginalMobName = getBoolean("always-show-original-mob-name", true);
  }
  
  public boolean addCustomEnchantsToLore() {
    return this.addCustomEnchantsToLore;
  }
  
  public boolean addTeamColorTo1_13Prefix() {
    return this.addTeamColorToPrefix;
  }
  
  public boolean isFix1_13FacePlayer() {
    return this.fix1_13FacePlayer;
  }
  
  public boolean alwaysShowOriginalMobName() {
    return this.alwaysShowOriginalMobName;
  }
  
  public URL getDefaultConfigURL() {
    return getClass().getClassLoader().getResource("assets/viabackwards/config.yml");
  }
  
  protected void handleConfig(Map<String, Object> map) {}
  
  public List<String> getUnsupportedOptions() {
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\ViaBackwardsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */