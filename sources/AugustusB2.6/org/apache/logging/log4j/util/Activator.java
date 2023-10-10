// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.wiring.BundleWire;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.net.URL;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.apache.logging.log4j.spi.Provider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.AdaptPermission;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import java.security.Permission;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.BundleActivator;

public class Activator implements BundleActivator, SynchronousBundleListener
{
    private static final SecurityManager SECURITY_MANAGER;
    private static final Logger LOGGER;
    private boolean lockingProviderUtil;
    
    private static void checkPermission(final Permission permission) {
        if (Activator.SECURITY_MANAGER != null) {
            Activator.SECURITY_MANAGER.checkPermission(permission);
        }
    }
    
    private void loadProvider(final Bundle bundle) {
        if (bundle.getState() == 1) {
            return;
        }
        try {
            checkPermission((Permission)new AdminPermission(bundle, "resource"));
            checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
            final BundleContext bundleContext = bundle.getBundleContext();
            if (bundleContext == null) {
                Activator.LOGGER.debug("Bundle {} has no context (state={}), skipping loading provider", bundle.getSymbolicName(), this.toStateString(bundle.getState()));
            }
            else {
                this.loadProvider(bundleContext, (BundleWiring)bundle.adapt((Class)BundleWiring.class));
            }
        }
        catch (SecurityException e) {
            Activator.LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
        }
        catch (Exception e2) {
            Activator.LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e2);
        }
    }
    
    private String toStateString(final int state) {
        switch (state) {
            case 1: {
                return "UNINSTALLED";
            }
            case 2: {
                return "INSTALLED";
            }
            case 4: {
                return "RESOLVED";
            }
            case 8: {
                return "STARTING";
            }
            case 16: {
                return "STOPPING";
            }
            case 32: {
                return "ACTIVE";
            }
            default: {
                return Integer.toString(state);
            }
        }
    }
    
    private void loadProvider(final BundleContext bundleContext, final BundleWiring bundleWiring) {
        final String filter = "(APIVersion>=2.6.0)";
        try {
            final Collection<ServiceReference<Provider>> serviceReferences = (Collection<ServiceReference<Provider>>)bundleContext.getServiceReferences((Class)Provider.class, "(APIVersion>=2.6.0)");
            Provider maxProvider = null;
            for (final ServiceReference<Provider> serviceReference : serviceReferences) {
                final Provider provider = (Provider)bundleContext.getService((ServiceReference)serviceReference);
                if (maxProvider == null || provider.getPriority() > maxProvider.getPriority()) {
                    maxProvider = provider;
                }
            }
            if (maxProvider != null) {
                ProviderUtil.addProvider(maxProvider);
            }
        }
        catch (InvalidSyntaxException ex) {
            Activator.LOGGER.error("Invalid service filter: (APIVersion>=2.6.0)", (Throwable)ex);
        }
        final List<URL> urls = (List<URL>)bundleWiring.findEntries("META-INF", "log4j-provider.properties", 0);
        for (final URL url : urls) {
            ProviderUtil.loadProvider(url, bundleWiring.getClassLoader());
        }
    }
    
    public void start(final BundleContext bundleContext) throws Exception {
        ProviderUtil.STARTUP_LOCK.lock();
        this.lockingProviderUtil = true;
        final BundleWiring self = (BundleWiring)bundleContext.getBundle().adapt((Class)BundleWiring.class);
        final List<BundleWire> required = (List<BundleWire>)self.getRequiredWires(LoggerContextFactory.class.getName());
        for (final BundleWire wire : required) {
            this.loadProvider(bundleContext, wire.getProviderWiring());
        }
        bundleContext.addBundleListener((BundleListener)this);
        final Bundle[] bundles2;
        final Bundle[] bundles = bundles2 = bundleContext.getBundles();
        for (final Bundle bundle : bundles2) {
            this.loadProvider(bundle);
        }
        this.unlockIfReady();
    }
    
    private void unlockIfReady() {
        if (this.lockingProviderUtil && !ProviderUtil.PROVIDERS.isEmpty()) {
            ProviderUtil.STARTUP_LOCK.unlock();
            this.lockingProviderUtil = false;
        }
    }
    
    public void stop(final BundleContext bundleContext) throws Exception {
        bundleContext.removeBundleListener((BundleListener)this);
        this.unlockIfReady();
    }
    
    public void bundleChanged(final BundleEvent event) {
        switch (event.getType()) {
            case 2: {
                this.loadProvider(event.getBundle());
                this.unlockIfReady();
                break;
            }
        }
    }
    
    static {
        SECURITY_MANAGER = System.getSecurityManager();
        LOGGER = StatusLogger.getLogger();
    }
}
