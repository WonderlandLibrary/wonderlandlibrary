package de.gerrygames.viarewind;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin implements ViaRewindPlatform {
  public void onEnable() {
    ViaRewindConfigImpl conf = new ViaRewindConfigImpl(new File(getDataFolder(), "config.yml"));
    conf.reloadConfig();
    init((ViaRewindConfig)conf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\BukkitPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */