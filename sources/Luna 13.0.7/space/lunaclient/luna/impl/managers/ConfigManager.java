package space.lunaclient.luna.impl.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.config.Config;
import space.lunaclient.luna.api.manager.Manager;

public class ConfigManager
  extends Manager<Config>
{
  private Gson gson;
  
  public ConfigManager()
  {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    makeDir();
  }
  
  private void makeDir()
  {
    File directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + Luna.INSTANCE.NAME + "/Configs");
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }
  
  public void addConfig(String name)
  {
    Config config = new Config(name, new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + Luna.INSTANCE.NAME + "/Configs/" + name), this.gson);
    
    config.saveConfig();
    getContents().add(config);
  }
  
  public boolean removeConfig(String name)
  {
    Config config = getConfigByName(name);
    if (config == null) {
      return false;
    }
    getContents().remove(config);
    try
    {
      config.delete();
    }
    catch (IOException localIOException) {}
    return true;
  }
  
  public boolean configExists(String name)
  {
    for (Config config : getContents()) {
      if (config.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
  
  public Config getConfigByName(String name)
  {
    for (Config config : getContents()) {
      if (config.getName().equalsIgnoreCase(name)) {
        return config;
      }
    }
    return null;
  }
}
