package org.apache.logging.log4j.core.osgi;

import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
import org.apache.logging.log4j.core.impl.Log4jProvider;
import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
import org.apache.logging.log4j.core.impl.ThreadContextDataProvider;
import org.apache.logging.log4j.core.util.ContextDataProvider;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.wiring.BundleWiring;

public final class Activator implements BundleActivator, SynchronousBundleListener {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final AtomicReference<BundleContext> contextRef = new AtomicReference<>();
  
  ServiceRegistration provideRegistration = null;
  
  ServiceRegistration contextDataRegistration = null;
  
  public void start(BundleContext context) throws Exception {
    Log4jProvider log4jProvider = new Log4jProvider();
    Hashtable<String, String> props = new Hashtable<>();
    props.put("APIVersion", "2.60");
    ThreadContextDataProvider threadContextDataProvider = new ThreadContextDataProvider();
    this.provideRegistration = context.registerService(Provider.class.getName(), log4jProvider, props);
    this.contextDataRegistration = context.registerService(ContextDataProvider.class.getName(), threadContextDataProvider, null);
    loadContextProviders(context);
    if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null)
      System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName()); 
    if (this.contextRef.compareAndSet(null, context)) {
      context.addBundleListener((BundleListener)this);
      scanInstalledBundlesForPlugins(context);
    } 
  }
  
  private static void scanInstalledBundlesForPlugins(BundleContext context) {
    Bundle[] bundles = context.getBundles();
    for (Bundle bundle : bundles)
      scanBundleForPlugins(bundle); 
  }
  
  private static void scanBundleForPlugins(Bundle bundle) {
    long bundleId = bundle.getBundleId();
    if (bundle.getState() == 32 && bundleId != 0L) {
      LOGGER.trace("Scanning bundle [{}, id=%d] for plugins.", bundle.getSymbolicName(), Long.valueOf(bundleId));
      PluginRegistry.getInstance().loadFromBundle(bundleId, ((BundleWiring)bundle
          .adapt(BundleWiring.class)).getClassLoader());
    } 
  }
  
  private static void loadContextProviders(BundleContext bundleContext) {
    try {
      Collection<ServiceReference<ContextDataProvider>> serviceReferences = bundleContext.getServiceReferences(ContextDataProvider.class, null);
      for (ServiceReference<ContextDataProvider> serviceReference : serviceReferences) {
        ContextDataProvider provider = (ContextDataProvider)bundleContext.getService(serviceReference);
        ThreadContextDataInjector.contextDataProviders.add(provider);
      } 
    } catch (InvalidSyntaxException ex) {
      LOGGER.error("Error accessing context data provider", (Throwable)ex);
    } 
  }
  
  private static void stopBundlePlugins(Bundle bundle) {
    LOGGER.trace("Stopping bundle [{}] plugins.", bundle.getSymbolicName());
    PluginRegistry.getInstance().clearBundlePlugins(bundle.getBundleId());
  }
  
  public void stop(BundleContext context) throws Exception {
    this.provideRegistration.unregister();
    this.contextDataRegistration.unregister();
    this.contextRef.compareAndSet(context, null);
    LogManager.shutdown();
  }
  
  public void bundleChanged(BundleEvent event) {
    switch (event.getType()) {
      case 2:
        scanBundleForPlugins(event.getBundle());
        break;
      case 256:
        stopBundlePlugins(event.getBundle());
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\osgi\Activator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */