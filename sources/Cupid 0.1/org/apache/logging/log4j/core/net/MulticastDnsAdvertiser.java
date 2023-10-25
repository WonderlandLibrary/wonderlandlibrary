package org.apache.logging.log4j.core.net;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

@Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
public class MulticastDnsAdvertiser implements Advertiser {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final int MAX_LENGTH = 255;
  
  private static final int DEFAULT_PORT = 4555;
  
  private static Object jmDNS = initializeJmDns();
  
  private static Class<?> jmDNSClass;
  
  private static Class<?> serviceInfoClass;
  
  public Object advertise(Map<String, String> properties) {
    Map<String, String> truncatedProperties = new HashMap<>();
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      if (((String)entry.getKey()).length() <= 255 && ((String)entry.getValue()).length() <= 255)
        truncatedProperties.put(entry.getKey(), entry.getValue()); 
    } 
    String protocol = truncatedProperties.get("protocol");
    String zone = "._log4j._" + ((protocol != null) ? protocol : "tcp") + ".local.";
    String portString = truncatedProperties.get("port");
    int port = Integers.parseInt(portString, 4555);
    String name = truncatedProperties.get("name");
    if (jmDNS != null) {
      Object serviceInfo;
      boolean isVersion3 = false;
      try {
        jmDNSClass.getMethod("create", new Class[0]);
        isVersion3 = true;
      } catch (NoSuchMethodException null) {}
      if (isVersion3) {
        serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
      } else {
        serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
      } 
      try {
        Method method = jmDNSClass.getMethod("registerService", new Class[] { serviceInfoClass });
        method.invoke(jmDNS, new Object[] { serviceInfo });
      } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
        LOGGER.warn("Unable to invoke registerService method", e);
      } catch (NoSuchMethodException e) {
        LOGGER.warn("No registerService method", e);
      } 
      return serviceInfo;
    } 
    LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
    return null;
  }
  
  public void unadvertise(Object serviceInfo) {
    if (jmDNS != null)
      try {
        Method method = jmDNSClass.getMethod("unregisterService", new Class[] { serviceInfoClass });
        method.invoke(jmDNS, new Object[] { serviceInfo });
      } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
        LOGGER.warn("Unable to invoke unregisterService method", e);
      } catch (NoSuchMethodException e) {
        LOGGER.warn("No unregisterService method", e);
      }  
  }
  
  private static Object createJmDnsVersion1() {
    try {
      return jmDNSClass.getConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InstantiationException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
      LOGGER.warn("Unable to instantiate JMDNS", e);
      return null;
    } 
  }
  
  private static Object createJmDnsVersion3() {
    try {
      Method jmDNSCreateMethod = jmDNSClass.getMethod("create", new Class[0]);
      return jmDNSCreateMethod.invoke(null, (Object[])null);
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      LOGGER.warn("Unable to invoke create method", e);
    } catch (NoSuchMethodException e) {
      LOGGER.warn("Unable to get create method", e);
    } 
    return null;
  }
  
  private static Object buildServiceInfoVersion1(String zone, int port, String name, Map<String, String> properties) {
    Hashtable<String, String> hashtableProperties = new Hashtable<>(properties);
    try {
      return serviceInfoClass.getConstructor(new Class[] { String.class, String.class, int.class, int.class, int.class, Hashtable.class }).newInstance(new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), hashtableProperties });
    } catch (IllegalAccessException|InstantiationException|java.lang.reflect.InvocationTargetException e) {
      LOGGER.warn("Unable to construct ServiceInfo instance", e);
    } catch (NoSuchMethodException e) {
      LOGGER.warn("Unable to get ServiceInfo constructor", e);
    } 
    return null;
  }
  
  private static Object buildServiceInfoVersion3(String zone, int port, String name, Map<String, String> properties) {
    try {
      return serviceInfoClass
        
        .getMethod("create", new Class[] { String.class, String.class, int.class, int.class, int.class, Map.class }).invoke(null, new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), properties });
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      LOGGER.warn("Unable to invoke create method", e);
    } catch (NoSuchMethodException e) {
      LOGGER.warn("Unable to find create method", e);
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
      } catch (NoSuchMethodException noSuchMethodException) {}
      if (isVersion3)
        return createJmDnsVersion3(); 
      return createJmDnsVersion1();
    } catch (ClassNotFoundException|ExceptionInInitializerError e) {
      LOGGER.warn("JmDNS or serviceInfo class not found", e);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\MulticastDnsAdvertiser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */