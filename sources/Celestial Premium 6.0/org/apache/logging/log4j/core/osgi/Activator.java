/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.osgi.framework.Bundle
 *  org.osgi.framework.BundleActivator
 *  org.osgi.framework.BundleContext
 *  org.osgi.framework.BundleEvent
 *  org.osgi.framework.BundleListener
 *  org.osgi.framework.InvalidSyntaxException
 *  org.osgi.framework.ServiceReference
 *  org.osgi.framework.ServiceRegistration
 *  org.osgi.framework.SynchronousBundleListener
 *  org.osgi.framework.wiring.BundleWiring
 */
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
import org.apache.logging.log4j.core.osgi.BundleContextSelector;
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

public final class Activator
implements BundleActivator,
SynchronousBundleListener {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final AtomicReference<BundleContext> contextRef = new AtomicReference();
    ServiceRegistration provideRegistration = null;
    ServiceRegistration contextDataRegistration = null;

    public void start(BundleContext context) throws Exception {
        Log4jProvider provider = new Log4jProvider();
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("APIVersion", "2.60");
        ThreadContextDataProvider threadContextProvider = new ThreadContextDataProvider();
        this.provideRegistration = context.registerService(Provider.class.getName(), (Object)provider, props);
        this.contextDataRegistration = context.registerService(ContextDataProvider.class.getName(), (Object)threadContextProvider, null);
        Activator.loadContextProviders(context);
        if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null) {
            System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName());
        }
        if (this.contextRef.compareAndSet(null, context)) {
            context.addBundleListener((BundleListener)this);
            Activator.scanInstalledBundlesForPlugins(context);
        }
    }

    private static void scanInstalledBundlesForPlugins(BundleContext context) {
        Bundle[] bundles;
        for (Bundle bundle : bundles = context.getBundles()) {
            Activator.scanBundleForPlugins(bundle);
        }
    }

    private static void scanBundleForPlugins(Bundle bundle) {
        long bundleId = bundle.getBundleId();
        if (bundle.getState() == 32 && bundleId != 0L) {
            LOGGER.trace("Scanning bundle [{}, id=%d] for plugins.", bundle.getSymbolicName(), bundleId);
            PluginRegistry.getInstance().loadFromBundle(bundleId, ((BundleWiring)bundle.adapt(BundleWiring.class)).getClassLoader());
        }
    }

    private static void loadContextProviders(BundleContext bundleContext) {
        try {
            Collection serviceReferences = bundleContext.getServiceReferences(ContextDataProvider.class, null);
            for (ServiceReference serviceReference : serviceReferences) {
                ContextDataProvider provider = (ContextDataProvider)bundleContext.getService(serviceReference);
                ThreadContextDataInjector.contextDataProviders.add(provider);
            }
        }
        catch (InvalidSyntaxException ex) {
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
            case 2: {
                Activator.scanBundleForPlugins(event.getBundle());
                break;
            }
            case 256: {
                Activator.stopBundlePlugins(event.getBundle());
                break;
            }
        }
    }
}

