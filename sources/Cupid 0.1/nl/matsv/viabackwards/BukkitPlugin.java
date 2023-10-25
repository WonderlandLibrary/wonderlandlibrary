package nl.matsv.viabackwards;

import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import nl.matsv.viabackwards.listener.FireExtinguishListener;
import nl.matsv.viabackwards.listener.LecternInteractListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.bukkit.platform.BukkitViaLoader;

public class BukkitPlugin extends JavaPlugin implements ViaBackwardsPlatform {
  public void onEnable() {
    init(getDataFolder());
    Via.getPlatform().runSync(this::onServerLoaded);
  }
  
  private void onServerLoaded() {
    BukkitViaLoader loader = (BukkitViaLoader)Via.getManager().getLoader();
    if (ProtocolRegistry.SERVER_PROTOCOL >= ProtocolVersion.v1_16.getVersion())
      ((FireExtinguishListener)loader.storeListener((Listener)new FireExtinguishListener(this))).register(); 
    if (ProtocolRegistry.SERVER_PROTOCOL >= ProtocolVersion.v1_14.getVersion())
      ((LecternInteractListener)loader.storeListener((Listener)new LecternInteractListener(this))).register(); 
  }
  
  public void disable() {
    getPluginLoader().disablePlugin((Plugin)this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\BukkitPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */