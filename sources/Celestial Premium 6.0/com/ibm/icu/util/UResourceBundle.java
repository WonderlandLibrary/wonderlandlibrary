/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceBundleReader;
import com.ibm.icu.impl.ResourceBundleWrapper;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundleIterator;
import com.ibm.icu.util.UResourceTypeMismatchException;
import com.ibm.icu.util.VersionInfo;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class UResourceBundle
extends ResourceBundle {
    private static ICUCache<ResourceCacheKey, UResourceBundle> BUNDLE_CACHE = new SimpleCache<ResourceCacheKey, UResourceBundle>();
    private static final ResourceCacheKey cacheKey = new ResourceCacheKey();
    private static final int ROOT_MISSING = 0;
    private static final int ROOT_ICU = 1;
    private static final int ROOT_JAVA = 2;
    private static SoftReference<ConcurrentHashMap<String, Integer>> ROOT_CACHE = new SoftReference(new ConcurrentHashMap());
    private Set<String> keys = null;
    public static final int NONE = -1;
    public static final int STRING = 0;
    public static final int BINARY = 1;
    public static final int TABLE = 2;
    public static final int INT = 7;
    public static final int ARRAY = 8;
    public static final int INT_VECTOR = 14;

    public static UResourceBundle getBundleInstance(String baseName, String localeName) {
        return UResourceBundle.getBundleInstance(baseName, localeName, ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }

    public static UResourceBundle getBundleInstance(String baseName, String localeName, ClassLoader root) {
        return UResourceBundle.getBundleInstance(baseName, localeName, root, false);
    }

    protected static UResourceBundle getBundleInstance(String baseName, String localeName, ClassLoader root, boolean disableFallback) {
        return UResourceBundle.instantiateBundle(baseName, localeName, root, disableFallback);
    }

    public static UResourceBundle getBundleInstance(ULocale locale) {
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }

    public static UResourceBundle getBundleInstance(String baseName) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        ULocale uloc = ULocale.getDefault();
        return UResourceBundle.getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }

    public static UResourceBundle getBundleInstance(String baseName, Locale locale) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        ULocale uloc = locale == null ? ULocale.getDefault() : ULocale.forLocale(locale);
        return UResourceBundle.getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }

    public static UResourceBundle getBundleInstance(String baseName, ULocale locale) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return UResourceBundle.getBundleInstance(baseName, locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }

    public static UResourceBundle getBundleInstance(String baseName, Locale locale, ClassLoader loader) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        ULocale uloc = locale == null ? ULocale.getDefault() : ULocale.forLocale(locale);
        return UResourceBundle.getBundleInstance(baseName, uloc.toString(), loader, false);
    }

    public static UResourceBundle getBundleInstance(String baseName, ULocale locale, ClassLoader loader) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return UResourceBundle.getBundleInstance(baseName, locale.toString(), loader, false);
    }

    public abstract ULocale getULocale();

    protected abstract String getLocaleID();

    protected abstract String getBaseName();

    protected abstract UResourceBundle getParent();

    @Override
    public Locale getLocale() {
        return this.getULocale().toLocale();
    }

    public static void resetBundleCache() {
        BUNDLE_CACHE = new SimpleCache<ResourceCacheKey, UResourceBundle>();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected static UResourceBundle addToCache(ClassLoader cl, String fullName, ULocale defaultLocale, UResourceBundle b) {
        ResourceCacheKey resourceCacheKey = cacheKey;
        synchronized (resourceCacheKey) {
            UResourceBundle.cacheKey.setKeyValues(cl, fullName, defaultLocale);
            UResourceBundle cachedBundle = BUNDLE_CACHE.get(cacheKey);
            if (cachedBundle != null) {
                return cachedBundle;
            }
            BUNDLE_CACHE.put((ResourceCacheKey)cacheKey.clone(), b);
            return b;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected static UResourceBundle loadFromCache(ClassLoader cl, String fullName, ULocale defaultLocale) {
        ResourceCacheKey resourceCacheKey = cacheKey;
        synchronized (resourceCacheKey) {
            UResourceBundle.cacheKey.setKeyValues(cl, fullName, defaultLocale);
            return BUNDLE_CACHE.get(cacheKey);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static int getRootType(String baseName, ClassLoader root) {
        Integer rootType;
        ConcurrentHashMap<String, Integer> m = null;
        m = ROOT_CACHE.get();
        if (m == null) {
            Class<UResourceBundle> class_ = UResourceBundle.class;
            // MONITORENTER : com.ibm.icu.util.UResourceBundle.class
            m = ROOT_CACHE.get();
            if (m == null) {
                m = new ConcurrentHashMap();
                ROOT_CACHE = new SoftReference<ConcurrentHashMap<String, Integer>>(m);
            }
            // MONITOREXIT : class_
        }
        if ((rootType = m.get(baseName)) != null) return rootType;
        String rootLocale = baseName.indexOf(46) == -1 ? "root" : "";
        int rt = 0;
        try {
            ICUResourceBundle.getBundleInstance(baseName, rootLocale, root, true);
            rt = 1;
        }
        catch (MissingResourceException ex) {
            try {
                ResourceBundleWrapper.getBundleInstance(baseName, rootLocale, root, true);
                rt = 2;
            }
            catch (MissingResourceException e) {
                // empty catch block
            }
        }
        rootType = rt;
        m.putIfAbsent(baseName, rootType);
        return rootType;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static void setRootType(String baseName, int rootType) {
        Integer rt = rootType;
        ConcurrentHashMap<String, Integer> m = null;
        m = ROOT_CACHE.get();
        if (m == null) {
            Class<UResourceBundle> class_ = UResourceBundle.class;
            // MONITORENTER : com.ibm.icu.util.UResourceBundle.class
            m = ROOT_CACHE.get();
            if (m == null) {
                m = new ConcurrentHashMap();
                ROOT_CACHE = new SoftReference<ConcurrentHashMap<String, Integer>>(m);
            }
            // MONITOREXIT : class_
        }
        m.put(baseName, rt);
    }

    protected static UResourceBundle instantiateBundle(String baseName, String localeName, ClassLoader root, boolean disableFallback) {
        UResourceBundle b = null;
        int rootType = UResourceBundle.getRootType(baseName, root);
        ULocale defaultLocale = ULocale.getDefault();
        switch (rootType) {
            case 1: {
                if (disableFallback) {
                    String fullName = ICUResourceBundleReader.getFullName(baseName, localeName);
                    b = UResourceBundle.loadFromCache(root, fullName, defaultLocale);
                    if (b == null) {
                        b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
                    }
                } else {
                    b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
                }
                return b;
            }
            case 2: {
                return ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
            }
        }
        try {
            b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
            UResourceBundle.setRootType(baseName, 1);
        }
        catch (MissingResourceException ex) {
            b = ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
            UResourceBundle.setRootType(baseName, 2);
        }
        return b;
    }

    public ByteBuffer getBinary() {
        throw new UResourceTypeMismatchException("");
    }

    public String getString() {
        throw new UResourceTypeMismatchException("");
    }

    public String[] getStringArray() {
        throw new UResourceTypeMismatchException("");
    }

    public byte[] getBinary(byte[] ba) {
        throw new UResourceTypeMismatchException("");
    }

    public int[] getIntVector() {
        throw new UResourceTypeMismatchException("");
    }

    public int getInt() {
        throw new UResourceTypeMismatchException("");
    }

    public int getUInt() {
        throw new UResourceTypeMismatchException("");
    }

    public UResourceBundle get(String aKey) {
        UResourceBundle obj = this.findTopLevel(aKey);
        if (obj == null) {
            String fullName = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
            throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, this.getClass().getName(), aKey);
        }
        return obj;
    }

    protected UResourceBundle findTopLevel(String aKey) {
        for (UResourceBundle res = this; res != null; res = res.getParent()) {
            UResourceBundle obj = res.handleGet(aKey, null, this);
            if (obj == null) continue;
            ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
            return obj;
        }
        return null;
    }

    public String getString(int index) {
        ICUResourceBundle temp = (ICUResourceBundle)this.get(index);
        if (temp.getType() == 0) {
            return temp.getString();
        }
        throw new UResourceTypeMismatchException("");
    }

    public UResourceBundle get(int index) {
        UResourceBundle obj = this.handleGet(index, null, this);
        if (obj == null) {
            obj = (ICUResourceBundle)this.getParent();
            if (obj != null) {
                obj = obj.get(index);
            }
            if (obj == null) {
                throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getKey(), this.getClass().getName(), this.getKey());
            }
        }
        ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
        return obj;
    }

    protected UResourceBundle findTopLevel(int index) {
        for (UResourceBundle res = this; res != null; res = res.getParent()) {
            UResourceBundle obj = res.handleGet(index, null, this);
            if (obj == null) continue;
            ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
            return obj;
        }
        return null;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(this.keySet());
    }

    @Override
    public Set<String> keySet() {
        if (this.keys == null) {
            if (this.isTopLevelResource()) {
                TreeSet<Object> newKeySet;
                if (this.parent == null) {
                    newKeySet = new TreeSet<String>();
                } else if (this.parent instanceof UResourceBundle) {
                    newKeySet = new TreeSet<String>(((UResourceBundle)this.parent).keySet());
                } else {
                    newKeySet = new TreeSet();
                    Enumeration<String> parentKeys = this.parent.getKeys();
                    while (parentKeys.hasMoreElements()) {
                        newKeySet.add(parentKeys.nextElement());
                    }
                }
                newKeySet.addAll(this.handleKeySet());
                this.keys = Collections.unmodifiableSet(newKeySet);
            } else {
                return this.handleKeySet();
            }
        }
        return this.keys;
    }

    @Override
    protected Set<String> handleKeySet() {
        return Collections.emptySet();
    }

    public int getSize() {
        return 1;
    }

    public int getType() {
        return -1;
    }

    public VersionInfo getVersion() {
        return null;
    }

    public UResourceBundleIterator getIterator() {
        return new UResourceBundleIterator(this);
    }

    public String getKey() {
        return null;
    }

    protected UResourceBundle handleGet(String aKey, HashMap<String, String> table, UResourceBundle requested) {
        return null;
    }

    protected UResourceBundle handleGet(int index, HashMap<String, String> table, UResourceBundle requested) {
        return null;
    }

    protected String[] handleGetStringArray() {
        return null;
    }

    protected Enumeration<String> handleGetKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String aKey) {
        return this.handleGetObjectImpl(aKey, this);
    }

    private Object handleGetObjectImpl(String aKey, UResourceBundle requested) {
        Object obj = this.resolveObject(aKey, requested);
        if (obj == null) {
            UResourceBundle parentBundle = this.getParent();
            if (parentBundle != null) {
                obj = parentBundle.handleGetObjectImpl(aKey, requested);
            }
            if (obj == null) {
                throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + aKey, this.getClass().getName(), aKey);
            }
        }
        return obj;
    }

    private Object resolveObject(String aKey, UResourceBundle requested) {
        if (this.getType() == 0) {
            return this.getString();
        }
        UResourceBundle obj = this.handleGet(aKey, null, requested);
        if (obj != null) {
            if (obj.getType() == 0) {
                return obj.getString();
            }
            try {
                if (obj.getType() == 8) {
                    return obj.handleGetStringArray();
                }
            }
            catch (UResourceTypeMismatchException ex) {
                return obj;
            }
        }
        return obj;
    }

    protected abstract void setLoadingStatus(int var1);

    protected boolean isTopLevelResource() {
        return true;
    }

    private static final class ResourceCacheKey
    implements Cloneable {
        private SoftReference<ClassLoader> loaderRef;
        private String searchName;
        private ULocale defaultLocale;
        private int hashCodeCache;

        private ResourceCacheKey() {
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (this == other) {
                return true;
            }
            try {
                ResourceCacheKey otherEntry = (ResourceCacheKey)other;
                if (this.hashCodeCache != otherEntry.hashCodeCache) {
                    return false;
                }
                if (!this.searchName.equals(otherEntry.searchName)) {
                    return false;
                }
                if (this.defaultLocale == null ? otherEntry.defaultLocale != null : !this.defaultLocale.equals(otherEntry.defaultLocale)) {
                    return false;
                }
                if (this.loaderRef == null) {
                    return otherEntry.loaderRef == null;
                }
                return otherEntry.loaderRef != null && this.loaderRef.get() == otherEntry.loaderRef.get();
            }
            catch (NullPointerException e) {
                return false;
            }
            catch (ClassCastException e) {
                return false;
            }
        }

        public int hashCode() {
            return this.hashCodeCache;
        }

        public Object clone() {
            try {
                return super.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalStateException();
            }
        }

        private synchronized void setKeyValues(ClassLoader root, String searchName, ULocale defaultLocale) {
            this.searchName = searchName;
            this.hashCodeCache = searchName.hashCode();
            this.defaultLocale = defaultLocale;
            if (defaultLocale != null) {
                this.hashCodeCache ^= defaultLocale.hashCode();
            }
            if (root == null) {
                this.loaderRef = null;
            } else {
                this.loaderRef = new SoftReference<ClassLoader>(root);
                this.hashCodeCache ^= root.hashCode();
            }
        }
    }
}

