package de.gerrygames.viarewind;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.slf4j.Logger;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;

@Plugin(id = "viarewind", name = "ViaRewind", version = "1.5.3-SNAPSHOT", authors = {"Gerrygames"}, dependencies = {@Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true)}, url = "https://viaversion.com/rewind")
public class VelocityPlugin implements ViaRewindPlatform {
  private Logger logger;
  
  @Inject
  private Logger loggerSlf4j;
  
  @Inject
  @DataDirectory
  private Path configDir;
  
  public Logger getLogger() {
    return this.logger;
  }
  
  @Subscribe(order = PostOrder.LATE)
  public void onProxyStart(ProxyInitializeEvent e) {
    this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
    ViaRewindConfigImpl conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile());
    conf.reloadConfig();
    init((ViaRewindConfig)conf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\VelocityPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */