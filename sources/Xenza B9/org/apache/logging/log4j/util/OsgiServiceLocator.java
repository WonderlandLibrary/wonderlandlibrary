// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.function.Function;
import org.osgi.framework.FrameworkUtil;
import java.util.stream.Stream;
import java.lang.invoke.MethodHandles;

public class OsgiServiceLocator
{
    private static final boolean OSGI_AVAILABLE;
    
    private static boolean checkOsgiAvailable() {
        try {
            Class.forName("org.osgi.framework.Bundle");
            return true;
        }
        catch (final ClassNotFoundException | LinkageError e) {
            return false;
        }
        catch (final Throwable e) {
            LowLevelLogUtil.logException("Unknown error checking for existence of class: org.osgi.framework.Bundle", e);
            return false;
        }
    }
    
    public static boolean isAvailable() {
        return OsgiServiceLocator.OSGI_AVAILABLE;
    }
    
    public static <T> Stream<T> loadServices(final Class<T> serviceType, final MethodHandles.Lookup lookup) {
        return loadServices(serviceType, lookup, true);
    }
    
    public static <T> Stream<T> loadServices(final Class<T> serviceType, final MethodHandles.Lookup lookup, final boolean verbose) {
        final Bundle bundle = FrameworkUtil.getBundle((Class)lookup.lookupClass());
        if (bundle != null) {
            final BundleContext ctx = bundle.getBundleContext();
            try {
                return ctx.getServiceReferences((Class)serviceType, (String)null).stream().map((Function<? super Object, ? extends T>)ctx::getService);
            }
            catch (final Throwable e) {
                if (verbose) {
                    StatusLogger.getLogger().error("Unable to load OSGI services for service {}", serviceType, e);
                }
            }
        }
        return Stream.empty();
    }
    
    static {
        OSGI_AVAILABLE = checkOsgiAvailable();
    }
}
