package nl.matsv.viabackwards;

import com.google.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;

@Plugin(id = "viabackwards", name = "ViaBackwards", version = "3.3.0-20w45a", authors = {"Matsv", "KennyTV", "Gerrygames", "creeper123123321", "ForceUpdate1"}, description = "Allow older Minecraft versions to connect to a newer server version.", dependencies = {@Dependency(id = "viaversion")})
public class SpongePlugin implements ViaBackwardsPlatform {
  private Logger logger;
  
  @Inject
  private Logger loggerSlf4j;
  
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configPath;
  
  @Listener(order = Order.LATE)
  public void onGameStart(GameInitializationEvent e) {
    this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
    Via.getManager().addEnableListener(() -> init(this.configPath.resolve("config.yml").toFile()));
  }
  
  public void disable() {}
  
  public File getDataFolder() {
    return this.configPath.toFile();
  }
  
  public Logger getLogger() {
    return this.logger;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\SpongePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */