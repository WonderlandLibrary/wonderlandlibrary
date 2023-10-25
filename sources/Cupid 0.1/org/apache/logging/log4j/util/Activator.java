package org.apache.logging.log4j.util;

import java.net.URL;
import java.security.Permission;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.AdaptPermission;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

public class Activator implements BundleActivator, SynchronousBundleListener {
  private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private boolean lockingProviderUtil;
  
  private static void checkPermission(Permission permission) {
    if (SECURITY_MANAGER != null)
      SECURITY_MANAGER.checkPermission(permission); 
  }
  
  private void loadProvider(Bundle bundle) {
    if (bundle.getState() == 1)
      return; 
    try {
      checkPermission((Permission)new AdminPermission(bundle, "resource"));
      checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
      BundleContext bundleContext = bundle.getBundleContext();
      if (bundleContext == null) {
        LOGGER.debug("Bundle {} has no context (state={}), skipping loading provider", bundle.getSymbolicName(), toStateString(bundle.getState()));
      } else {
        loadProvider(bundleContext, (BundleWiring)bundle.adapt(BundleWiring.class));
      } 
    } catch (SecurityException e) {
      LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
    } catch (Exception e) {
      LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e);
    } 
  }
  
  private String toStateString(int state) {
    switch (state) {
      case 1:
        return "UNINSTALLED";
      case 2:
        return "INSTALLED";
      case 4:
        return "RESOLVED";
      case 8:
        return "STARTING";
      case 16:
        return "STOPPING";
      case 32:
        return "ACTIVE";
    } 
    return Integer.toString(state);
  }
  
  private void loadProvider(BundleContext bundleContext, BundleWiring bundleWiring) {
    String filter = "(APIVersion>=2.6.0)";
    try {
      Collection<ServiceReference<Provider>> serviceReferences = bundleContext.getServiceReferences(Provider.class, "(APIVersion>=2.6.0)");
      Provider maxProvider = null;
      for (ServiceReference<Provider> serviceReference : serviceReferences) {
        Provider provider = (Provider)bundleContext.getService(serviceReference);
        if (maxProvider == null || provider.getPriority().intValue() > maxProvider.getPriority().intValue())
          maxProvider = provider; 
      } 
      if (maxProvider != null)
        ProviderUtil.addProvider(maxProvider); 
    } catch (InvalidSyntaxException ex) {
      LOGGER.error("Invalid service filter: (APIVersion>=2.6.0)", (Throwable)ex);
    } 
    List<URL> urls = bundleWiring.findEntries("META-INF", "log4j-provider.properties", 0);
    for (URL url : urls)
      ProviderUtil.loadProvider(url, bundleWiring.getClassLoader()); 
  }
  
  public void start(BundleContext bundleContext) throws Exception {
    ProviderUtil.STARTUP_LOCK.lock();
    this.lockingProviderUtil = true;
    BundleWiring self = (BundleWiring)bundleContext.getBundle().adapt(BundleWiring.class);
    List<BundleWire> required = self.getRequiredWires(LoggerContextFactory.class.getName());
    for (BundleWire wire : required)
      loadProvider(bundleContext, wire.getProviderWiring()); 
    bundleContext.addBundleListener((BundleListener)this);
    Bundle[] bundles = bundleContext.getBundles();
    for (Bundle bundle : bundles)
      loadProvider(bundle); 
    unlockIfReady();
  }
  
  private void unlockIfReady() {
    if (this.lockingProviderUtil && !ProviderUtil.PROVIDERS.isEmpty()) {
      ProviderUtil.STARTUP_LOCK.unlock();
      this.lockingProviderUtil = false;
    } 
  }
  
  public void stop(BundleContext bundleContext) throws Exception {
    bundleContext.removeBundleListener((BundleListener)this);
    unlockIfReady();
  }
  
  public void bundleChanged(BundleEvent event) {
    switch (event.getType()) {
      case 2:
        loadProvider(event.getBundle());
        unlockIfReady();
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Activator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */