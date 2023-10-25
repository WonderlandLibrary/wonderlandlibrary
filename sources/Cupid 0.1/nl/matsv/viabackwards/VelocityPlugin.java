package nl.matsv.viabackwards;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import org.slf4j.Logger;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;

@Plugin(id = "viabackwards", name = "ViaBackwards", version = "3.3.0-20w45a", authors = {"Matsv", "KennyTV", "Gerrygames", "creeper123123321", "ForceUpdate1"}, description = "Allow older Minecraft versions to connect to a newer server version.", dependencies = {@Dependency(id = "viaversion")})
public class VelocityPlugin implements ViaBackwardsPlatform {
  private Logger logger;
  
  @Inject
  private Logger loggerSlf4j;
  
  @Inject
  @DataDirectory
  private Path configPath;
  
  @Subscribe(order = PostOrder.LATE)
  public void onProxyStart(ProxyInitializeEvent e) {
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\VelocityPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */