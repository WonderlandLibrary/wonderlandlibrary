/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.osgi.framework.AdaptPermission
 *  org.osgi.framework.AdminPermission
 *  org.osgi.framework.Bundle
 *  org.osgi.framework.BundleActivator
 *  org.osgi.framework.BundleContext
 *  org.osgi.framework.BundleEvent
 *  org.osgi.framework.BundleListener
 *  org.osgi.framework.InvalidSyntaxException
 *  org.osgi.framework.ServiceReference
 *  org.osgi.framework.SynchronousBundleListener
 *  org.osgi.framework.wiring.BundleWire
 *  org.osgi.framework.wiring.BundleWiring
 */
package org.apache.logging.log4j.util;

import java.net.URL;
import java.security.Permission;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.ProviderUtil;
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

public class Activator
implements BundleActivator,
SynchronousBundleListener {
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
    private static final Logger LOGGER = StatusLogger.getLogger();
    private boolean lockingProviderUtil;

    private static void checkPermission(Permission permission) {
        if (SECURITY_MANAGER != null) {
            SECURITY_MANAGER.checkPermission(permission);
        }
    }

    private void loadProvider(Bundle bundle) {
        if (bundle.getState() == 1) {
            return;
        }
        try {
            Activator.checkPermission((Permission)new AdminPermission(bundle, "resource"));
            Activator.checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
            BundleContext bundleContext = bundle.getBundleContext();
            if (bundleContext == null) {
                LOGGER.debug("Bundle {} has no context (state={}), skipping loading provider", bundle.getSymbolicName(), this.toStateString(bundle.getState()));
            } else {
                this.loadProvider(bundleContext, (BundleWiring)bundle.adapt(BundleWiring.class));
            }
        }
        catch (SecurityException e) {
            LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
        }
        catch (Exception e) {
            LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e);
        }
    }

    private String toStateString(int state) {
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
        }
        return Integer.toString(state);
    }

    private void loadProvider(BundleContext bundleContext, BundleWiring bundleWiring) {
        String filter = "(APIVersion>=2.6.0)";
        try {
            Collection serviceReferences = bundleContext.getServiceReferences(Provider.class, "(APIVersion>=2.6.0)");
            Provider maxProvider = null;
            for (ServiceReference serviceReference : serviceReferences) {
                Provider provider = (Provider)bundleContext.getService(serviceReference);
                if (maxProvider != null && provider.getPriority() <= maxProvider.getPriority()) continue;
                maxProvider = provider;
            }
            if (maxProvider != null) {
                ProviderUtil.addProvider(maxProvider);
            }
        }
        catch (InvalidSyntaxException ex) {
            LOGGER.error("Invalid service filter: (APIVersion>=2.6.0)", (Throwable)ex);
        }
        List urls = bundleWiring.findEntries("META-INF", "log4j-provider.properties", 0);
        for (URL url : urls) {
            ProviderUtil.loadProvider((URL)url, (ClassLoader)bundleWiring.getClassLoader());
        }
    }

    public void start(BundleContext bundleContext) throws Exception {
        Bundle[] bundles;
        ProviderUtil.STARTUP_LOCK.lock();
        this.lockingProviderUtil = true;
        BundleWiring self = (BundleWiring)bundleContext.getBundle().adapt(BundleWiring.class);
        List required = self.getRequiredWires(LoggerContextFactory.class.getName());
        for (BundleWire wire : required) {
            this.loadProvider(bundleContext, wire.getProviderWiring());
        }
        bundleContext.addBundleListener((BundleListener)this);
        for (Bundle bundle : bundles = bundleContext.getBundles()) {
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

    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeBundleListener((BundleListener)this);
        this.unlockIfReady();
    }

    public void bundleChanged(BundleEvent event) {
        switch (event.getType()) {
            case 2: {
                this.loadProvider(event.getBundle());
                this.unlockIfReady();
                break;
            }
        }
    }
}

