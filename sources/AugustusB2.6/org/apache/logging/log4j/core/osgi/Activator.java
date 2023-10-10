// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.osgi;

import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.BundleEvent;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collection;
import org.osgi.framework.InvalidSyntaxException;
import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleListener;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.util.ContextDataProvider;
import java.util.Dictionary;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.core.impl.ThreadContextDataProvider;
import java.util.Hashtable;
import org.apache.logging.log4j.core.impl.Log4jProvider;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.BundleContext;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.BundleActivator;

public final class Activator implements BundleActivator, SynchronousBundleListener
{
    private static final Logger LOGGER;
    private final AtomicReference<BundleContext> contextRef;
    ServiceRegistration provideRegistration;
    ServiceRegistration contextDataRegistration;
    
    public Activator() {
        this.contextRef = new AtomicReference<BundleContext>();
        this.provideRegistration = null;
        this.contextDataRegistration = null;
    }
    
    public void start(final BundleContext context) throws Exception {
        final Provider provider = new Log4jProvider();
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("APIVersion", "2.60");
        final ContextDataProvider threadContextProvider = new ThreadContextDataProvider();
        this.provideRegistration = context.registerService(Provider.class.getName(), (Object)provider, (Dictionary)props);
        this.contextDataRegistration = context.registerService(ContextDataProvider.class.getName(), (Object)threadContextProvider, (Dictionary)null);
        loadContextProviders(context);
        if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null) {
            System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName());
        }
        if (this.contextRef.compareAndSet(null, context)) {
            context.addBundleListener((BundleListener)this);
            scanInstalledBundlesForPlugins(context);
        }
    }
    
    private static void scanInstalledBundlesForPlugins(final BundleContext context) {
        final Bundle[] bundles2;
        final Bundle[] bundles = bundles2 = context.getBundles();
        for (final Bundle bundle : bundles2) {
            scanBundleForPlugins(bundle);
        }
    }
    
    private static void scanBundleForPlugins(final Bundle bundle) {
        final long bundleId = bundle.getBundleId();
        if (bundle.getState() == 32 && bundleId != 0L) {
            Activator.LOGGER.trace("Scanning bundle [{}, id=%d] for plugins.", bundle.getSymbolicName(), bundleId);
            PluginRegistry.getInstance().loadFromBundle(bundleId, ((BundleWiring)bundle.adapt((Class)BundleWiring.class)).getClassLoader());
        }
    }
    
    private static void loadContextProviders(final BundleContext bundleContext) {
        try {
            final Collection<ServiceReference<ContextDataProvider>> serviceReferences = (Collection<ServiceReference<ContextDataProvider>>)bundleContext.getServiceReferences((Class)ContextDataProvider.class, (String)null);
            for (final ServiceReference<ContextDataProvider> serviceReference : serviceReferences) {
                final ContextDataProvider provider = (ContextDataProvider)bundleContext.getService((ServiceReference)serviceReference);
                ThreadContextDataInjector.contextDataProviders.add(provider);
            }
        }
        catch (InvalidSyntaxException ex) {
            Activator.LOGGER.error("Error accessing context data provider", (Throwable)ex);
        }
    }
    
    private static void stopBundlePlugins(final Bundle bundle) {
        Activator.LOGGER.trace("Stopping bundle [{}] plugins.", bundle.getSymbolicName());
        PluginRegistry.getInstance().clearBundlePlugins(bundle.getBundleId());
    }
    
    public void stop(final BundleContext context) throws Exception {
        this.provideRegistration.unregister();
        this.contextDataRegistration.unregister();
        this.contextRef.compareAndSet(context, null);
        LogManager.shutdown();
    }
    
    public void bundleChanged(final BundleEvent event) {
        switch (event.getType()) {
            case 2: {
                scanBundleForPlugins(event.getBundle());
                break;
            }
            case 256: {
                stopBundlePlugins(event.getBundle());
                break;
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
