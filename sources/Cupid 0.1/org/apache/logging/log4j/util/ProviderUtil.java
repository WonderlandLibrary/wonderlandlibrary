package org.apache.logging.log4j.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;

public final class ProviderUtil {
  protected static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
  
  protected static final Collection<Provider> PROVIDERS = new HashSet<>();
  
  protected static final Lock STARTUP_LOCK = new ReentrantLock();
  
  private static final String API_VERSION = "Log4jAPIVersion";
  
  private static final String[] COMPATIBLE_API_VERSIONS = new String[] { "2.6.0" };
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static volatile ProviderUtil instance;
  
  private ProviderUtil() {
    for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
      try {
        loadProviders(classLoader);
      } catch (Throwable ex) {
        LOGGER.debug("Unable to retrieve provider from ClassLoader {}", classLoader, ex);
      } 
    } 
    for (LoaderUtil.UrlResource resource : LoaderUtil.findUrlResources("META-INF/log4j-provider.properties"))
      loadProvider(resource.getUrl(), resource.getClassLoader()); 
  }
  
  protected static void addProvider(Provider provider) {
    PROVIDERS.add(provider);
    LOGGER.debug("Loaded Provider {}", provider);
  }
  
  protected static void loadProvider(URL url, ClassLoader cl) {
    try {
      Properties props = PropertiesUtil.loadClose(url.openStream(), url);
      if (validVersion(props.getProperty("Log4jAPIVersion"))) {
        Provider provider = new Provider(props, url, cl);
        PROVIDERS.add(provider);
        LOGGER.debug("Loaded Provider {}", provider);
      } 
    } catch (IOException e) {
      LOGGER.error("Unable to open {}", url, e);
    } 
  }
  
  protected static void loadProviders(ClassLoader classLoader) {
    ServiceLoader<Provider> serviceLoader = ServiceLoader.load(Provider.class, classLoader);
    for (Provider provider : serviceLoader) {
      if (validVersion(provider.getVersions()) && !PROVIDERS.contains(provider))
        PROVIDERS.add(provider); 
    } 
  }
  
  @Deprecated
  protected static void loadProviders(Enumeration<URL> urls, ClassLoader cl) {
    if (urls != null)
      while (urls.hasMoreElements())
        loadProvider(urls.nextElement(), cl);  
  }
  
  public static Iterable<Provider> getProviders() {
    lazyInit();
    return PROVIDERS;
  }
  
  public static boolean hasProviders() {
    lazyInit();
    return !PROVIDERS.isEmpty();
  }
  
  protected static void lazyInit() {
    if (instance == null)
      try {
        STARTUP_LOCK.lockInterruptibly();
        try {
          if (instance == null)
            instance = new ProviderUtil(); 
        } finally {
          STARTUP_LOCK.unlock();
        } 
      } catch (InterruptedException e) {
        LOGGER.fatal("Interrupted before Log4j Providers could be loaded.", e);
        Thread.currentThread().interrupt();
      }  
  }
  
  public static ClassLoader findClassLoader() {
    return LoaderUtil.getThreadContextClassLoader();
  }
  
  private static boolean validVersion(String version) {
    for (String v : COMPATIBLE_API_VERSIONS) {
      if (version.startsWith(v))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\ProviderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */