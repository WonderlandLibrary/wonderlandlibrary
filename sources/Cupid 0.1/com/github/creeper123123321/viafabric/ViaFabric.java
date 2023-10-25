package com.github.creeper123123321.viafabric;

import com.github.creeper123123321.viafabric.platform.VRInjector;
import com.github.creeper123123321.viafabric.platform.VRLoader;
import com.github.creeper123123321.viafabric.platform.VRPlatform;
import com.github.creeper123123321.viafabric.util.JLoggerToLog4j;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import sun.misc.URLClassPath;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.api.platform.ViaPlatform;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import viamcp.platform.ViaBackwardsPlatformImplementation;
import viamcp.platform.ViaRewindPlatformImplementation;

public class ViaFabric {
  public static int clientSideVersion = 47;
  
  public static final Logger JLOGGER = (Logger)new JLoggerToLog4j(LogManager.getLogger("ViaFabric"));
  
  public static final ExecutorService ASYNC_EXECUTOR;
  
  public static final EventLoop EVENT_LOOP;
  
  public static CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();
  
  static {
    ThreadFactory factory = (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("ViaFabric-%d").build();
    ASYNC_EXECUTOR = Executors.newFixedThreadPool(8, factory);
    EVENT_LOOP = (new LocalEventLoopGroup(1, factory)).next();
    EVENT_LOOP.submit(INIT_FUTURE::join);
  }
  
  public static String getVersion() {
    return "1.0";
  }
  
  public void onInitialize() throws IllegalAccessException, NoSuchFieldException, MalformedURLException {
    loadVia();
    Via.init(ViaManager.builder()
        .injector((ViaInjector)new VRInjector())
        .loader((ViaPlatformLoader)new VRLoader())
        .platform((ViaPlatform)new VRPlatform()).build());
    MappingDataLoader.enableMappingsCache();
    new ViaBackwardsPlatformImplementation();
    new ViaRewindPlatformImplementation();
    Via.getManager().init();
    INIT_FUTURE.complete(null);
  }
  
  public void loadVia() throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    Field addUrl = loader.getClass().getDeclaredField("ucp");
    addUrl.setAccessible(true);
    URLClassPath ucp = (URLClassPath)addUrl.get(loader);
    File[] files = (new File((Minecraft.getMinecraft()).mcDataDir, "mods")).listFiles();
    if (files != null)
      for (File f : files) {
        if (f.isFile() && f.getName().startsWith("Via") && f.getName().toLowerCase().endsWith(".jar"))
          ucp.addURL(f.toURI().toURL()); 
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\ViaFabric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */