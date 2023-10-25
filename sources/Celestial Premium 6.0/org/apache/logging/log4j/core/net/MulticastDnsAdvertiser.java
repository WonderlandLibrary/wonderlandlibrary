/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

@Plugin(name="multicastdns", category="Core", elementType="advertiser", printObject=false)
public class MulticastDnsAdvertiser
implements Advertiser {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MAX_LENGTH = 255;
    private static final int DEFAULT_PORT = 4555;
    private static Object jmDNS = MulticastDnsAdvertiser.initializeJmDns();
    private static Class<?> jmDNSClass;
    private static Class<?> serviceInfoClass;

    @Override
    public Object advertise(Map<String, String> properties) {
        HashMap<String, String> truncatedProperties = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getKey().length() > 255 || entry.getValue().length() > 255) continue;
            truncatedProperties.put(entry.getKey(), entry.getValue());
        }
        String protocol = (String)truncatedProperties.get("protocol");
        String zone = "._log4j._" + (protocol != null ? protocol : "tcp") + ".local.";
        String portString = (String)truncatedProperties.get("port");
        int port = Integers.parseInt(portString, 4555);
        String name = (String)truncatedProperties.get("name");
        if (jmDNS != null) {
            boolean isVersion3 = false;
            try {
                jmDNSClass.getMethod("create", new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            Object serviceInfo = isVersion3 ? MulticastDnsAdvertiser.buildServiceInfoVersion3(zone, port, name, truncatedProperties) : MulticastDnsAdvertiser.buildServiceInfoVersion1(zone, port, name, truncatedProperties);
            try {
                Method method = jmDNSClass.getMethod("registerService", serviceInfoClass);
                method.invoke(jmDNS, serviceInfo);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.warn("Unable to invoke registerService method", (Throwable)e);
            }
            catch (NoSuchMethodException e) {
                LOGGER.warn("No registerService method", (Throwable)e);
            }
            return serviceInfo;
        }
        LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
        return null;
    }

    @Override
    public void unadvertise(Object serviceInfo) {
        if (jmDNS != null) {
            try {
                Method method = jmDNSClass.getMethod("unregisterService", serviceInfoClass);
                method.invoke(jmDNS, serviceInfo);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.warn("Unable to invoke unregisterService method", (Throwable)e);
            }
            catch (NoSuchMethodException e) {
                LOGGER.warn("No unregisterService method", (Throwable)e);
            }
        }
    }

    private static Object createJmDnsVersion1() {
        try {
            return jmDNSClass.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.warn("Unable to instantiate JMDNS", (Throwable)e);
            return null;
        }
    }

    private static Object createJmDnsVersion3() {
        try {
            Method jmDNSCreateMethod = jmDNSClass.getMethod("create", new Class[0]);
            return jmDNSCreateMethod.invoke(null, null);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn("Unable to invoke create method", (Throwable)e);
        }
        catch (NoSuchMethodException e) {
            LOGGER.warn("Unable to get create method", (Throwable)e);
        }
        return null;
    }

    private static Object buildServiceInfoVersion1(String zone, int port, String name, Map<String, String> properties) {
        Hashtable<String, String> hashtableProperties = new Hashtable<String, String>(properties);
        try {
            return serviceInfoClass.getConstructor(String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class).newInstance(zone, name, port, 0, 0, hashtableProperties);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.warn("Unable to construct ServiceInfo instance", (Throwable)e);
        }
        catch (NoSuchMethodException e) {
            LOGGER.warn("Unable to get ServiceInfo constructor", (Throwable)e);
        }
        return null;
    }

    private static Object buildServiceInfoVersion3(String zone, int port, String name, Map<String, String> properties) {
        try {
            return serviceInfoClass.getMethod("create", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class).invoke(null, zone, name, port, 0, 0, properties);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn("Unable to invoke create method", (Throwable)e);
        }
        catch (NoSuchMethodException e) {
            LOGGER.warn("Unable to find create method", (Throwable)e);
        }
        return null;
    }

    private static Object initializeJmDns() {
        try {
            jmDNSClass = LoaderUtil.loadClass("javax.jmdns.JmDNS");
            serviceInfoClass = LoaderUtil.loadClass("javax.jmdns.ServiceInfo");
            boolean isVersion3 = false;
            try {
                jmDNSClass.getMethod("create", new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            if (isVersion3) {
                return MulticastDnsAdvertiser.createJmDnsVersion3();
            }
            return MulticastDnsAdvertiser.createJmDnsVersion1();
        }
        catch (ClassNotFoundException | ExceptionInInitializerError e) {
            LOGGER.warn("JmDNS or serviceInfo class not found", e);
            return null;
        }
    }
}

