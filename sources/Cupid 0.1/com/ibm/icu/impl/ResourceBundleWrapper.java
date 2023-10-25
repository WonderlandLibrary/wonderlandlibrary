package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleWrapper extends UResourceBundle {
  private ResourceBundle bundle = null;
  
  private String localeID = null;
  
  private String baseName = null;
  
  private List<String> keys = null;
  
  private ResourceBundleWrapper(ResourceBundle bundle) {
    this.bundle = bundle;
  }
  
  protected void setLoadingStatus(int newStatus) {}
  
  protected Object handleGetObject(String aKey) {
    ResourceBundleWrapper current = this;
    Object obj = null;
    while (current != null) {
      try {
        obj = current.bundle.getObject(aKey);
        break;
      } catch (MissingResourceException ex) {
        current = (ResourceBundleWrapper)current.getParent();
      } 
    } 
    if (obj == null)
      throw new MissingResourceException("Can't find resource for bundle " + this.baseName + ", key " + aKey, getClass().getName(), aKey); 
    return obj;
  }
  
  public Enumeration<String> getKeys() {
    return Collections.enumeration(this.keys);
  }
  
  private void initKeysVector() {
    ResourceBundleWrapper current = this;
    this.keys = new ArrayList<String>();
    while (current != null) {
      Enumeration<String> e = current.bundle.getKeys();
      while (e.hasMoreElements()) {
        String elem = e.nextElement();
        if (!this.keys.contains(elem))
          this.keys.add(elem); 
      } 
      current = (ResourceBundleWrapper)current.getParent();
    } 
  }
  
  protected String getLocaleID() {
    return this.localeID;
  }
  
  protected String getBaseName() {
    return this.bundle.getClass().getName().replace('.', '/');
  }
  
  public ULocale getULocale() {
    return new ULocale(this.localeID);
  }
  
  public UResourceBundle getParent() {
    return (UResourceBundle)this.parent;
  }
  
  private static final boolean DEBUG = ICUDebug.enabled("resourceBundleWrapper");
  
  public static UResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
    UResourceBundle b = instantiateBundle(baseName, localeID, root, disableFallback);
    if (b == null) {
      String separator = "_";
      if (baseName.indexOf('/') >= 0)
        separator = "/"; 
      throw new MissingResourceException("Could not find the bundle " + baseName + separator + localeID, "", "");
    } 
    return b;
  }
  
  protected static synchronized UResourceBundle instantiateBundle(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
    if (root == null)
      root = Utility.getFallbackClassLoader(); 
    final ClassLoader cl = root;
    String name = baseName;
    ULocale defaultLocale = ULocale.getDefault();
    if (localeID.length() != 0)
      name = name + "_" + localeID; 
    ResourceBundleWrapper b = (ResourceBundleWrapper)loadFromCache(cl, name, defaultLocale);
    if (b == null) {
      ResourceBundleWrapper parent = null;
      int i = localeID.lastIndexOf('_');
      boolean loadFromProperties = false;
      if (i != -1) {
        String locName = localeID.substring(0, i);
        parent = (ResourceBundleWrapper)loadFromCache(cl, baseName + "_" + locName, defaultLocale);
        if (parent == null)
          parent = (ResourceBundleWrapper)instantiateBundle(baseName, locName, cl, disableFallback); 
      } else if (localeID.length() > 0) {
        parent = (ResourceBundleWrapper)loadFromCache(cl, baseName, defaultLocale);
        if (parent == null)
          parent = (ResourceBundleWrapper)instantiateBundle(baseName, "", cl, disableFallback); 
      } 
      try {
        Class<? extends ResourceBundle> cls = cl.loadClass(name).asSubclass(ResourceBundle.class);
        ResourceBundle bx = cls.newInstance();
        b = new ResourceBundleWrapper(bx);
        if (parent != null)
          b.setParent((ResourceBundle)parent); 
        b.baseName = baseName;
        b.localeID = localeID;
      } catch (ClassNotFoundException e) {
        loadFromProperties = true;
      } catch (NoClassDefFoundError e) {
        loadFromProperties = true;
      } catch (Exception e) {
        if (DEBUG)
          System.out.println("failure"); 
        if (DEBUG)
          System.out.println(e); 
      } 
      if (loadFromProperties)
        try {
          final String resName = name.replace('.', '/') + ".properties";
          InputStream stream = AccessController.<InputStream>doPrivileged(new PrivilegedAction<InputStream>() {
                public InputStream run() {
                  if (cl != null)
                    return cl.getResourceAsStream(resName); 
                  return ClassLoader.getSystemResourceAsStream(resName);
                }
              });
          if (stream != null) {
            stream = new BufferedInputStream(stream);
            try {
              b = new ResourceBundleWrapper(new PropertyResourceBundle(stream));
              if (parent != null)
                b.setParent((ResourceBundle)parent); 
              b.baseName = baseName;
              b.localeID = localeID;
            } catch (Exception ex) {
              try {
                stream.close();
              } catch (Exception exception) {}
            } finally {
              try {
                stream.close();
              } catch (Exception ex) {}
            } 
          } 
          if (b == null) {
            String defaultName = defaultLocale.toString();
            if (localeID.length() > 0 && localeID.indexOf('_') < 0 && defaultName.indexOf(localeID) == -1) {
              b = (ResourceBundleWrapper)loadFromCache(cl, baseName + "_" + defaultName, defaultLocale);
              if (b == null)
                b = (ResourceBundleWrapper)instantiateBundle(baseName, defaultName, cl, disableFallback); 
            } 
          } 
          if (b == null)
            b = parent; 
        } catch (Exception e) {
          if (DEBUG)
            System.out.println("failure"); 
          if (DEBUG)
            System.out.println(e); 
        }  
      b = (ResourceBundleWrapper)addToCache(cl, name, defaultLocale, b);
    } 
    if (b != null) {
      b.initKeysVector();
    } else if (DEBUG) {
      System.out.println("Returning null for " + baseName + "_" + localeID);
    } 
    return b;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ResourceBundleWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */