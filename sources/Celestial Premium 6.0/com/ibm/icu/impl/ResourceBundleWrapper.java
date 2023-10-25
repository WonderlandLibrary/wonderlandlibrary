/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.Utility;
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

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ResourceBundleWrapper
extends UResourceBundle {
    private ResourceBundle bundle = null;
    private String localeID = null;
    private String baseName = null;
    private List<String> keys = null;
    private static final boolean DEBUG = ICUDebug.enabled("resourceBundleWrapper");

    private ResourceBundleWrapper(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected void setLoadingStatus(int newStatus) {
    }

    @Override
    protected Object handleGetObject(String aKey) {
        Object obj = null;
        for (ResourceBundleWrapper current = this; current != null; current = (ResourceBundleWrapper)current.getParent()) {
            try {
                obj = current.bundle.getObject(aKey);
                break;
            }
            catch (MissingResourceException ex) {
                continue;
            }
        }
        if (obj == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.baseName + ", key " + aKey, this.getClass().getName(), aKey);
        }
        return obj;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(this.keys);
    }

    private void initKeysVector() {
        this.keys = new ArrayList<String>();
        for (ResourceBundleWrapper current = this; current != null; current = (ResourceBundleWrapper)current.getParent()) {
            Enumeration<String> e = current.bundle.getKeys();
            while (e.hasMoreElements()) {
                String elem = e.nextElement();
                if (this.keys.contains(elem)) continue;
                this.keys.add(elem);
            }
        }
    }

    @Override
    protected String getLocaleID() {
        return this.localeID;
    }

    @Override
    protected String getBaseName() {
        return this.bundle.getClass().getName().replace('.', '/');
    }

    @Override
    public ULocale getULocale() {
        return new ULocale(this.localeID);
    }

    @Override
    public UResourceBundle getParent() {
        return (UResourceBundle)this.parent;
    }

    public static UResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
        UResourceBundle b = ResourceBundleWrapper.instantiateBundle(baseName, localeID, root, disableFallback);
        if (b == null) {
            String separator = "_";
            if (baseName.indexOf(47) >= 0) {
                separator = "/";
            }
            throw new MissingResourceException("Could not find the bundle " + baseName + separator + localeID, "", "");
        }
        return b;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static synchronized UResourceBundle instantiateBundle(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
        ResourceBundleWrapper b;
        if (root == null) {
            root = Utility.getFallbackClassLoader();
        }
        final ClassLoader cl = root;
        String name = baseName;
        ULocale defaultLocale = ULocale.getDefault();
        if (localeID.length() != 0) {
            name = name + "_" + localeID;
        }
        if ((b = (ResourceBundleWrapper)ResourceBundleWrapper.loadFromCache(cl, name, defaultLocale)) == null) {
            block35: {
                boolean loadFromProperties;
                ResourceBundleWrapper parent;
                block33: {
                    parent = null;
                    int i = localeID.lastIndexOf(95);
                    loadFromProperties = false;
                    if (i != -1) {
                        String locName = localeID.substring(0, i);
                        parent = (ResourceBundleWrapper)ResourceBundleWrapper.loadFromCache(cl, baseName + "_" + locName, defaultLocale);
                        if (parent == null) {
                            parent = (ResourceBundleWrapper)ResourceBundleWrapper.instantiateBundle(baseName, locName, cl, disableFallback);
                        }
                    } else if (localeID.length() > 0 && (parent = (ResourceBundleWrapper)ResourceBundleWrapper.loadFromCache(cl, baseName, defaultLocale)) == null) {
                        parent = (ResourceBundleWrapper)ResourceBundleWrapper.instantiateBundle(baseName, "", cl, disableFallback);
                    }
                    try {
                        Class<ResourceBundle> cls = cl.loadClass(name).asSubclass(ResourceBundle.class);
                        ResourceBundle bx = cls.newInstance();
                        b = new ResourceBundleWrapper(bx);
                        if (parent != null) {
                            b.setParent(parent);
                        }
                        b.baseName = baseName;
                        b.localeID = localeID;
                    }
                    catch (ClassNotFoundException e) {
                        loadFromProperties = true;
                    }
                    catch (NoClassDefFoundError e) {
                        loadFromProperties = true;
                    }
                    catch (Exception e) {
                        if (DEBUG) {
                            System.out.println("failure");
                        }
                        if (!DEBUG) break block33;
                        System.out.println(e);
                    }
                }
                if (loadFromProperties) {
                    try {
                        block34: {
                            final String resName = name.replace('.', '/') + ".properties";
                            InputStream stream = AccessController.doPrivileged(new PrivilegedAction<InputStream>(){

                                @Override
                                public InputStream run() {
                                    if (cl != null) {
                                        return cl.getResourceAsStream(resName);
                                    }
                                    return ClassLoader.getSystemResourceAsStream(resName);
                                }
                            });
                            if (stream != null) {
                                stream = new BufferedInputStream(stream);
                                b = new ResourceBundleWrapper(new PropertyResourceBundle(stream));
                                if (parent != null) {
                                    b.setParent(parent);
                                }
                                b.baseName = baseName;
                                b.localeID = localeID;
                                try {
                                    stream.close();
                                }
                                catch (Exception ex) {}
                                break block34;
                                catch (Exception ex) {
                                    try {
                                        stream.close();
                                    }
                                    catch (Exception ex2) {}
                                    catch (Throwable throwable) {
                                        try {
                                            stream.close();
                                            throw throwable;
                                        }
                                        catch (Exception ex3) {
                                            // empty catch block
                                        }
                                        throw throwable;
                                    }
                                }
                            }
                        }
                        if (b == null) {
                            String defaultName = defaultLocale.toString();
                            if (localeID.length() > 0 && localeID.indexOf(95) < 0 && defaultName.indexOf(localeID) == -1 && (b = (ResourceBundleWrapper)ResourceBundleWrapper.loadFromCache(cl, baseName + "_" + defaultName, defaultLocale)) == null) {
                                b = (ResourceBundleWrapper)ResourceBundleWrapper.instantiateBundle(baseName, defaultName, cl, disableFallback);
                            }
                        }
                        if (b == null) {
                            b = parent;
                        }
                    }
                    catch (Exception e) {
                        if (DEBUG) {
                            System.out.println("failure");
                        }
                        if (!DEBUG) break block35;
                        System.out.println(e);
                    }
                }
            }
            b = (ResourceBundleWrapper)ResourceBundleWrapper.addToCache(cl, name, defaultLocale, b);
        }
        if (b != null) {
            b.initKeysVector();
            return b;
        }
        if (!DEBUG) return b;
        System.out.println("Returning null for " + baseName + "_" + localeID);
        return b;
    }
}

