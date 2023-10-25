package de.gerrygames.viarewind;

import com.google.inject.Inject;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;

@Plugin(id = "viarewind", name = "ViaRewind", version = "1.5.3-SNAPSHOT", authors = {"Gerrygames"}, dependencies = {@Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true)}, url = "https://viaversion.com/rewind")
public class SpongePlugin implements ViaRewindPlatform {
  private Logger logger;
  
  @Inject
  private Logger loggerSlf4j;
  
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;
  
  public Logger getLogger() {
    return this.logger;
  }
  
  @Listener(order = Order.LATE)
  public void onGameStart(GameInitializationEvent e) {
    this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
    ViaRewindConfigImpl conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile());
    conf.reloadConfig();
    init((ViaRewindConfig)conf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\SpongePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */