package nl.matsv.viabackwards;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;
import net.fabricmc.loader.api.FabricLoader;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import nl.matsv.viabackwards.fabric.util.LoggerWrapper;
import org.apache.logging.log4j.LogManager;

public class ViaFabricAddon implements ViaBackwardsPlatform, Runnable {
  private final Logger logger = (Logger)new LoggerWrapper(LogManager.getLogger("ViaBackwards"));
  
  private File configDir;
  
  public void run() {
    Path configDirPath = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ViaBackwards");
    this.configDir = configDirPath.toFile();
    init(configDirPath.resolve("config.yml").toFile());
  }
  
  public void disable() {}
  
  public File getDataFolder() {
    return this.configDir;
  }
  
  public Logger getLogger() {
    return this.logger;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\ViaFabricAddon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */