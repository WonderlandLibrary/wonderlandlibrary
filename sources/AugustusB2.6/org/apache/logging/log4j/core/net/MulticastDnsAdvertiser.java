// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.util.Integers;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
public class MulticastDnsAdvertiser implements Advertiser
{
    protected static final Logger LOGGER;
    private static final int MAX_LENGTH = 255;
    private static final int DEFAULT_PORT = 4555;
    private static Object jmDNS;
    private static Class<?> jmDNSClass;
    private static Class<?> serviceInfoClass;
    
    @Override
    public Object advertise(final Map<String, String> properties) {
        final Map<String, String> truncatedProperties = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getKey().length() <= 255 && entry.getValue().length() <= 255) {
                truncatedProperties.put(entry.getKey(), entry.getValue());
            }
        }
        final String protocol = truncatedProperties.get("protocol");
        final String zone = "._log4j._" + ((protocol != null) ? protocol : "tcp") + ".local.";
        final String portString = truncatedProperties.get("port");
        final int port = Integers.parseInt(portString, 4555);
        final String name = truncatedProperties.get("name");
        if (MulticastDnsAdvertiser.jmDNS != null) {
            boolean isVersion3 = false;
            try {
                MulticastDnsAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex2) {}
            Object serviceInfo;
            if (isVersion3) {
                serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
            }
            else {
                serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
            }
            try {
                final Method method = MulticastDnsAdvertiser.jmDNSClass.getMethod("registerService", MulticastDnsAdvertiser.serviceInfoClass);
                method.invoke(MulticastDnsAdvertiser.jmDNS, serviceInfo);
            }
            catch (IllegalAccessException | InvocationTargetException ex3) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke registerService method", e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDnsAdvertiser.LOGGER.warn("No registerService method", e2);
            }
            return serviceInfo;
        }
        MulticastDnsAdvertiser.LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
        return null;
    }
    
    @Override
    public void unadvertise(final Object serviceInfo) {
        if (MulticastDnsAdvertiser.jmDNS != null) {
            try {
                final Method method = MulticastDnsAdvertiser.jmDNSClass.getMethod("unregisterService", MulticastDnsAdvertiser.serviceInfoClass);
                method.invoke(MulticastDnsAdvertiser.jmDNS, serviceInfo);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDnsAdvertiser.LOGGER.warn("No unregisterService method", e2);
            }
        }
    }
    
    private static Object createJmDnsVersion1() {
        try {
            return MulticastDnsAdvertiser.jmDNSClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", e);
            return null;
        }
    }
    
    private static Object createJmDnsVersion3() {
        try {
            final Method jmDNSCreateMethod = MulticastDnsAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])new Class[0]);
            return jmDNSCreateMethod.invoke(null, (Object[])null);
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke create method", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to get create method", e2);
        }
        return null;
    }
    
    private static Object buildServiceInfoVersion1(final String zone, final int port, final String name, final Map<String, String> properties) {
        final Hashtable<String, String> hashtableProperties = new Hashtable<String, String>(properties);
        try {
            return MulticastDnsAdvertiser.serviceInfoClass.getConstructor(String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class).newInstance(zone, name, port, 0, 0, hashtableProperties);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to get ServiceInfo constructor", e2);
        }
        return null;
    }
    
    private static Object buildServiceInfoVersion3(final String zone, final int port, final String name, final Map<String, String> properties) {
        try {
            return MulticastDnsAdvertiser.serviceInfoClass.getMethod("create", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class).invoke(null, zone, name, port, 0, 0, properties);
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke create method", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to find create method", e2);
        }
        return null;
    }
    
    private static Object initializeJmDns() {
        try {
            MulticastDnsAdvertiser.jmDNSClass = LoaderUtil.loadClass("javax.jmdns.JmDNS");
            MulticastDnsAdvertiser.serviceInfoClass = LoaderUtil.loadClass("javax.jmdns.ServiceInfo");
            boolean isVersion3 = false;
            try {
                MulticastDnsAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex) {}
            if (isVersion3) {
                return createJmDnsVersion3();
            }
            return createJmDnsVersion1();
        }
        catch (ClassNotFoundException | ExceptionInInitializerError ex2) {
            final Throwable t;
            final Throwable e = t;
            MulticastDnsAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        MulticastDnsAdvertiser.jmDNS = initializeJmDns();
    }
}
