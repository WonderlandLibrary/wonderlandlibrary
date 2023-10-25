/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundleImpl;
import com.ibm.icu.impl.ICUResourceBundleReader;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.URLHandler;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ICUResourceBundle
extends UResourceBundle {
    protected static final String ICU_DATA_PATH = "com/ibm/icu/impl/";
    public static final String ICU_BUNDLE = "data/icudt51b";
    public static final String ICU_BASE_NAME = "com/ibm/icu/impl/data/icudt51b";
    public static final String ICU_COLLATION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/coll";
    public static final String ICU_BRKITR_NAME = "/brkitr";
    public static final String ICU_BRKITR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/brkitr";
    public static final String ICU_RBNF_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/rbnf";
    public static final String ICU_TRANSLIT_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/translit";
    public static final String ICU_LANG_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/lang";
    public static final String ICU_CURR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/curr";
    public static final String ICU_REGION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/region";
    public static final String ICU_ZONE_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/zone";
    private static final String NO_INHERITANCE_MARKER = "\u2205\u2205\u2205";
    protected String resPath;
    public static final ClassLoader ICU_DATA_CLASS_LOADER;
    protected static final String INSTALLED_LOCALES = "InstalledLocales";
    public static final int FROM_FALLBACK = 1;
    public static final int FROM_ROOT = 2;
    public static final int FROM_DEFAULT = 3;
    public static final int FROM_LOCALE = 4;
    private int loadingStatus = -1;
    private static final String ICU_RESOURCE_INDEX = "res_index";
    private static final String DEFAULT_TAG = "default";
    private static final String FULL_LOCALE_NAMES_LIST = "fullLocaleNames.lst";
    private static final boolean DEBUG;
    private static CacheBase<String, AvailEntry, ClassLoader> GET_AVAILABLE_CACHE;
    protected String localeID;
    protected String baseName;
    protected ULocale ulocale;
    protected ClassLoader loader;
    protected ICUResourceBundleReader reader;
    protected String key;
    protected int resource;
    public static final int RES_BOGUS = -1;
    public static final int ALIAS = 3;
    public static final int TABLE32 = 4;
    public static final int TABLE16 = 5;
    public static final int STRING_V2 = 6;
    public static final int ARRAY16 = 9;
    private static final int[] gPublicTypes;
    private static final char RES_PATH_SEP_CHAR = '/';
    private static final String RES_PATH_SEP_STR = "/";
    private static final String ICUDATA = "ICUDATA";
    private static final char HYPHEN = '-';
    private static final String LOCALE = "LOCALE";
    protected ICUCache<Object, UResourceBundle> lookup;
    private static final int MAX_INITIAL_LOOKUP_SIZE = 64;

    @Override
    public void setLoadingStatus(int newStatus) {
        this.loadingStatus = newStatus;
    }

    public int getLoadingStatus() {
        return this.loadingStatus;
    }

    public void setLoadingStatus(String requestedLocale) {
        String locale = this.getLocaleID();
        if (locale.equals("root")) {
            this.setLoadingStatus(2);
        } else if (locale.equals(requestedLocale)) {
            this.setLoadingStatus(4);
        } else {
            this.setLoadingStatus(1);
        }
    }

    public String getResPath() {
        return this.resPath;
    }

    public static final ULocale getFunctionalEquivalent(String baseName, ClassLoader loader, String resName, String keyword, ULocale locID, boolean[] isAvailable, boolean omitDefault) {
        ICUResourceBundle irb;
        String kwVal = locID.getKeywordValue(keyword);
        String baseLoc = locID.getBaseName();
        String defStr = null;
        ULocale parent = new ULocale(baseLoc);
        ULocale defLoc = null;
        boolean lookForDefault = false;
        ULocale fullBase = null;
        int defDepth = 0;
        int resDepth = 0;
        if (kwVal == null || kwVal.length() == 0 || kwVal.equals(DEFAULT_TAG)) {
            kwVal = "";
            lookForDefault = true;
        }
        ICUResourceBundle r = null;
        r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        if (isAvailable != null) {
            isAvailable[0] = false;
            ULocale[] availableULocales = ICUResourceBundle.getAvailEntry(baseName, loader).getULocaleList();
            for (int i = 0; i < availableULocales.length; ++i) {
                if (!parent.equals(availableULocales[i])) continue;
                isAvailable[0] = true;
                break;
            }
        }
        do {
            try {
                irb = (ICUResourceBundle)r.get(resName);
                defStr = irb.getString(DEFAULT_TAG);
                if (lookForDefault) {
                    kwVal = defStr;
                    lookForDefault = false;
                }
                defLoc = r.getULocale();
            }
            catch (MissingResourceException t) {
                // empty catch block
            }
            if (defLoc != null) continue;
            r = (ICUResourceBundle)r.getParent();
            ++defDepth;
        } while (r != null && defLoc == null);
        parent = new ULocale(baseLoc);
        r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        do {
            try {
                irb = (ICUResourceBundle)r.get(resName);
                irb.get(kwVal);
                fullBase = irb.getULocale();
                if (fullBase != null && resDepth > defDepth) {
                    defStr = irb.getString(DEFAULT_TAG);
                    defLoc = r.getULocale();
                    defDepth = resDepth;
                }
            }
            catch (MissingResourceException t) {
                // empty catch block
            }
            if (fullBase != null) continue;
            r = (ICUResourceBundle)r.getParent();
            ++resDepth;
        } while (r != null && fullBase == null);
        if (fullBase == null && defStr != null && !defStr.equals(kwVal)) {
            kwVal = defStr;
            parent = new ULocale(baseLoc);
            r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
            resDepth = 0;
            do {
                try {
                    irb = (ICUResourceBundle)r.get(resName);
                    UResourceBundle urb = irb.get(kwVal);
                    fullBase = r.getULocale();
                    if (!fullBase.toString().equals(urb.getLocale().toString())) {
                        fullBase = null;
                    }
                    if (fullBase != null && resDepth > defDepth) {
                        defStr = irb.getString(DEFAULT_TAG);
                        defLoc = r.getULocale();
                        defDepth = resDepth;
                    }
                }
                catch (MissingResourceException t) {
                    // empty catch block
                }
                if (fullBase != null) continue;
                r = (ICUResourceBundle)r.getParent();
                ++resDepth;
            } while (r != null && fullBase == null);
        }
        if (fullBase == null) {
            throw new MissingResourceException("Could not find locale containing requested or default keyword.", baseName, keyword + "=" + kwVal);
        }
        if (omitDefault && defStr.equals(kwVal) && resDepth <= defDepth) {
            return fullBase;
        }
        return new ULocale(fullBase.toString() + "@" + keyword + "=" + kwVal);
    }

    public static final String[] getKeywordValues(String baseName, String keyword) {
        HashSet<String> keywords = new HashSet<String>();
        ULocale[] locales = ICUResourceBundle.createULocaleList(baseName, ICU_DATA_CLASS_LOADER);
        for (int i = 0; i < locales.length; ++i) {
            try {
                UResourceBundle b = UResourceBundle.getBundleInstance(baseName, locales[i]);
                ICUResourceBundle irb = (ICUResourceBundle)b.getObject(keyword);
                Enumeration<String> e = irb.getKeys();
                while (e.hasMoreElements()) {
                    String s = e.nextElement();
                    if (DEFAULT_TAG.equals(s)) continue;
                    keywords.add(s);
                }
                continue;
            }
            catch (Throwable t) {
                // empty catch block
            }
        }
        return keywords.toArray(new String[0]);
    }

    public ICUResourceBundle getWithFallback(String path) throws MissingResourceException {
        ICUResourceBundle result = null;
        ICUResourceBundle actualBundle = this;
        result = ICUResourceBundle.findResourceWithFallback(path, actualBundle, null);
        if (result == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getType(), path, this.getKey());
        }
        if (result.getType() == 0 && result.getString().equals(NO_INHERITANCE_MARKER)) {
            throw new MissingResourceException("Encountered NO_INHERITANCE_MARKER", path, this.getKey());
        }
        return result;
    }

    public ICUResourceBundle at(int index) {
        return (ICUResourceBundle)this.handleGet(index, null, (UResourceBundle)this);
    }

    public ICUResourceBundle at(String key) {
        if (this instanceof ICUResourceBundleImpl.ResourceTable) {
            return (ICUResourceBundle)this.handleGet(key, null, (UResourceBundle)this);
        }
        return null;
    }

    @Override
    public ICUResourceBundle findTopLevel(int index) {
        return (ICUResourceBundle)super.findTopLevel(index);
    }

    @Override
    public ICUResourceBundle findTopLevel(String aKey) {
        return (ICUResourceBundle)super.findTopLevel(aKey);
    }

    public ICUResourceBundle findWithFallback(String path) {
        return ICUResourceBundle.findResourceWithFallback(path, this, null);
    }

    public String getStringWithFallback(String path) throws MissingResourceException {
        return this.getWithFallback(path).getString();
    }

    public static Set<String> getAvailableLocaleNameSet(String bundlePrefix, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(bundlePrefix, loader).getLocaleNameSet();
    }

    public static Set<String> getFullLocaleNameSet() {
        return ICUResourceBundle.getFullLocaleNameSet(ICU_BASE_NAME, ICU_DATA_CLASS_LOADER);
    }

    public static Set<String> getFullLocaleNameSet(String bundlePrefix, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(bundlePrefix, loader).getFullLocaleNameSet();
    }

    public static Set<String> getAvailableLocaleNameSet() {
        return ICUResourceBundle.getAvailableLocaleNameSet(ICU_BASE_NAME, ICU_DATA_CLASS_LOADER);
    }

    public static final ULocale[] getAvailableULocales(String baseName, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(baseName, loader).getULocaleList();
    }

    public static final ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales(ICU_BASE_NAME, ICU_DATA_CLASS_LOADER);
    }

    public static final Locale[] getAvailableLocales(String baseName, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(baseName, loader).getLocaleList();
    }

    public static final Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailEntry(ICU_BASE_NAME, ICU_DATA_CLASS_LOADER).getLocaleList();
    }

    public static final Locale[] getLocaleList(ULocale[] ulocales) {
        ArrayList<Locale> list = new ArrayList<Locale>(ulocales.length);
        HashSet<Locale> uniqueSet = new HashSet<Locale>();
        for (int i = 0; i < ulocales.length; ++i) {
            Locale loc = ulocales[i].toLocale();
            if (uniqueSet.contains(loc)) continue;
            list.add(loc);
            uniqueSet.add(loc);
        }
        return list.toArray(new Locale[list.size()]);
    }

    @Override
    public Locale getLocale() {
        return this.getULocale().toLocale();
    }

    private static final ULocale[] createULocaleList(String baseName, ClassLoader root) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, ICU_RESOURCE_INDEX, root, true);
        bundle = (ICUResourceBundle)bundle.get(INSTALLED_LOCALES);
        int length = bundle.getSize();
        int i = 0;
        ULocale[] locales = new ULocale[length];
        UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            String locstr = iter.next().getKey();
            if (locstr.equals("root")) {
                locales[i++] = ULocale.ROOT;
                continue;
            }
            locales[i++] = new ULocale(locstr);
        }
        bundle = null;
        return locales;
    }

    private static final Locale[] createLocaleList(String baseName, ClassLoader loader) {
        ULocale[] ulocales = ICUResourceBundle.getAvailEntry(baseName, loader).getULocaleList();
        return ICUResourceBundle.getLocaleList(ulocales);
    }

    private static final String[] createLocaleNameArray(String baseName, ClassLoader root) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, ICU_RESOURCE_INDEX, root, true);
        bundle = (ICUResourceBundle)bundle.get(INSTALLED_LOCALES);
        int length = bundle.getSize();
        int i = 0;
        String[] locales = new String[length];
        UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            String locstr = iter.next().getKey();
            if (locstr.equals("root")) {
                locales[i++] = ULocale.ROOT.toString();
                continue;
            }
            locales[i++] = locstr;
        }
        bundle = null;
        return locales;
    }

    private static final List<String> createFullLocaleNameArray(final String baseName, final ClassLoader root) {
        List<String> list = AccessController.doPrivileged(new PrivilegedAction<List<String>>(){

            @Override
            public List<String> run() {
                String bn = baseName.endsWith(ICUResourceBundle.RES_PATH_SEP_STR) ? baseName : baseName + ICUResourceBundle.RES_PATH_SEP_STR;
                ArrayList resList = null;
                String skipScan = ICUConfig.get("com.ibm.icu.impl.ICUResourceBundle.skipRuntimeLocaleResourceScan", "false");
                if (!skipScan.equalsIgnoreCase("true")) {
                    try {
                        Enumeration<URL> urls = root.getResources(bn);
                        while (urls.hasMoreElements()) {
                            URL url = urls.nextElement();
                            URLHandler handler = URLHandler.get(url);
                            if (handler != null) {
                                final ArrayList lst = new ArrayList();
                                URLHandler.URLVisitor v = new URLHandler.URLVisitor(){

                                    public void visit(String s) {
                                        if (s.endsWith(".res")) {
                                            String locstr = s.substring(0, s.length() - 4);
                                            if (locstr.contains("_") && !locstr.equals(ICUResourceBundle.ICU_RESOURCE_INDEX)) {
                                                lst.add(locstr);
                                            } else if (locstr.length() == 2 || locstr.length() == 3) {
                                                lst.add(locstr);
                                            } else if (locstr.equalsIgnoreCase("root")) {
                                                lst.add(ULocale.ROOT.toString());
                                            }
                                        }
                                    }
                                };
                                handler.guide(v, false);
                                if (resList == null) {
                                    resList = new ArrayList(lst);
                                    continue;
                                }
                                resList.addAll(lst);
                                continue;
                            }
                            if (!DEBUG) continue;
                            System.out.println("handler for " + url + " is null");
                        }
                    }
                    catch (IOException e) {
                        if (DEBUG) {
                            System.out.println("ouch: " + e.getMessage());
                        }
                        resList = null;
                    }
                }
                if (resList == null) {
                    try {
                        InputStream s = root.getResourceAsStream(bn + ICUResourceBundle.FULL_LOCALE_NAMES_LIST);
                        if (s != null) {
                            String line;
                            resList = new ArrayList();
                            BufferedReader br = new BufferedReader(new InputStreamReader(s, "ASCII"));
                            while ((line = br.readLine()) != null) {
                                if (line.length() == 0 || line.startsWith("#")) continue;
                                if (line.equalsIgnoreCase("root")) {
                                    resList.add(ULocale.ROOT.toString());
                                    continue;
                                }
                                resList.add(line);
                            }
                            br.close();
                        }
                    }
                    catch (IOException e) {
                        // empty catch block
                    }
                }
                return resList;
            }
        });
        return list;
    }

    private static Set<String> createFullLocaleNameSet(String baseName, ClassLoader loader) {
        List<String> list = ICUResourceBundle.createFullLocaleNameArray(baseName, loader);
        if (list == null) {
            String rootLocaleID;
            Set<String> locNameSet;
            if (DEBUG) {
                System.out.println("createFullLocaleNameArray returned null");
            }
            if (!(locNameSet = ICUResourceBundle.createLocaleNameSet(baseName, loader)).contains(rootLocaleID = ULocale.ROOT.toString())) {
                HashSet<String> tmp = new HashSet<String>(locNameSet);
                tmp.add(rootLocaleID);
                locNameSet = Collections.unmodifiableSet(tmp);
            }
            return locNameSet;
        }
        HashSet<String> fullLocNameSet = new HashSet<String>();
        fullLocNameSet.addAll(list);
        return Collections.unmodifiableSet(fullLocNameSet);
    }

    private static Set<String> createLocaleNameSet(String baseName, ClassLoader loader) {
        try {
            String[] locales = ICUResourceBundle.createLocaleNameArray(baseName, loader);
            HashSet<String> set = new HashSet<String>();
            set.addAll(Arrays.asList(locales));
            return Collections.unmodifiableSet(set);
        }
        catch (MissingResourceException e) {
            if (DEBUG) {
                System.out.println("couldn't find index for bundleName: " + baseName);
                Thread.dumpStack();
            }
            return Collections.emptySet();
        }
    }

    private static AvailEntry getAvailEntry(String key, ClassLoader loader) {
        return GET_AVAILABLE_CACHE.getInstance(key, loader);
    }

    protected static final ICUResourceBundle findResourceWithFallback(String path, UResourceBundle actualBundle, UResourceBundle requested) {
        String basePath;
        ICUResourceBundle sub = null;
        if (requested == null) {
            requested = actualBundle;
        }
        String string = basePath = ((ICUResourceBundle)actualBundle).resPath.length() > 0 ? ((ICUResourceBundle)actualBundle).resPath : "";
        for (ICUResourceBundle base = (ICUResourceBundle)actualBundle; base != null; base = (ICUResourceBundle)base.getParent()) {
            if (path.indexOf(47) == -1) {
                sub = (ICUResourceBundle)base.handleGet(path, null, requested);
                if (sub != null) {
                    break;
                }
            } else {
                String subKey;
                ICUResourceBundle currentBase = base;
                StringTokenizer st = new StringTokenizer(path, RES_PATH_SEP_STR);
                while (st.hasMoreTokens() && (sub = ICUResourceBundle.findResourceWithFallback(subKey = st.nextToken(), currentBase, requested)) != null) {
                    currentBase = sub;
                }
                if (sub != null) break;
            }
            path = basePath.length() > 0 ? basePath + RES_PATH_SEP_STR + path : path;
            basePath = "";
        }
        if (sub != null) {
            sub.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
        }
        return sub;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof ICUResourceBundle) {
            ICUResourceBundle o = (ICUResourceBundle)other;
            if (this.getBaseName().equals(o.getBaseName()) && this.getLocaleID().equals(o.getLocaleID())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    public static UResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
        UResourceBundle b = ICUResourceBundle.instantiateBundle(baseName, localeID, root, disableFallback);
        if (b == null) {
            throw new MissingResourceException("Could not find the bundle " + baseName + RES_PATH_SEP_STR + localeID + ".res", "", "");
        }
        return b;
    }

    protected static synchronized UResourceBundle instantiateBundle(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
        ULocale defaultLocale = ULocale.getDefault();
        String localeName = localeID;
        if (localeName.indexOf(64) >= 0) {
            localeName = ULocale.getBaseName(localeID);
        }
        String fullName = ICUResourceBundleReader.getFullName(baseName, localeName);
        ICUResourceBundle b = (ICUResourceBundle)ICUResourceBundle.loadFromCache(root, fullName, defaultLocale);
        String rootLocale = baseName.indexOf(46) == -1 ? "root" : "";
        String defaultID = defaultLocale.getBaseName();
        if (localeName.equals("")) {
            localeName = rootLocale;
        }
        if (DEBUG) {
            System.out.println("Creating " + fullName + " currently b is " + b);
        }
        if (b == null) {
            b = ICUResourceBundle.createBundle(baseName, localeName, root);
            if (DEBUG) {
                System.out.println("The bundle created is: " + b + " and disableFallback=" + disableFallback + " and bundle.getNoFallback=" + (b != null && b.getNoFallback()));
            }
            if (disableFallback || b != null && b.getNoFallback()) {
                return ICUResourceBundle.addToCache(root, fullName, defaultLocale, b);
            }
            if (b == null) {
                int i = localeName.lastIndexOf(95);
                if (i != -1) {
                    String temp = localeName.substring(0, i);
                    b = (ICUResourceBundle)ICUResourceBundle.instantiateBundle(baseName, temp, root, disableFallback);
                    if (b != null && b.getULocale().getName().equals(temp)) {
                        b.setLoadingStatus(1);
                    }
                } else if (defaultID.indexOf(localeName) == -1) {
                    b = (ICUResourceBundle)ICUResourceBundle.instantiateBundle(baseName, defaultID, root, disableFallback);
                    if (b != null) {
                        b.setLoadingStatus(3);
                    }
                } else if (rootLocale.length() != 0 && (b = ICUResourceBundle.createBundle(baseName, rootLocale, root)) != null) {
                    b.setLoadingStatus(2);
                }
            } else {
                UResourceBundle parent = null;
                localeName = b.getLocaleID();
                int i = localeName.lastIndexOf(95);
                if ((b = (ICUResourceBundle)ICUResourceBundle.addToCache(root, fullName, defaultLocale, b)).getTableResource("%%Parent") != -1) {
                    String parentLocaleName = b.getString("%%Parent");
                    parent = ICUResourceBundle.instantiateBundle(baseName, parentLocaleName, root, disableFallback);
                } else if (i != -1) {
                    parent = ICUResourceBundle.instantiateBundle(baseName, localeName.substring(0, i), root, disableFallback);
                } else if (!localeName.equals(rootLocale)) {
                    parent = ICUResourceBundle.instantiateBundle(baseName, rootLocale, root, true);
                }
                if (!b.equals(parent)) {
                    b.setParent(parent);
                }
            }
        }
        return b;
    }

    UResourceBundle get(String aKey, HashMap<String, String> table, UResourceBundle requested) {
        ICUResourceBundle obj = (ICUResourceBundle)this.handleGet(aKey, table, requested);
        if (obj == null) {
            obj = (ICUResourceBundle)this.getParent();
            if (obj != null) {
                obj = (ICUResourceBundle)obj.get(aKey, table, requested);
            }
            if (obj == null) {
                String fullName = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
                throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, this.getClass().getName(), aKey);
            }
        }
        obj.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
        return obj;
    }

    public static ICUResourceBundle createBundle(String baseName, String localeID, ClassLoader root) {
        ICUResourceBundleReader reader = ICUResourceBundleReader.getReader(baseName, localeID, root);
        if (reader == null) {
            return null;
        }
        return ICUResourceBundle.getBundle(reader, baseName, localeID, root);
    }

    @Override
    protected String getLocaleID() {
        return this.localeID;
    }

    @Override
    protected String getBaseName() {
        return this.baseName;
    }

    @Override
    public ULocale getULocale() {
        return this.ulocale;
    }

    @Override
    public UResourceBundle getParent() {
        return (UResourceBundle)this.parent;
    }

    @Override
    protected void setParent(ResourceBundle parent) {
        this.parent = parent;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public int getType() {
        return gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(this.resource)];
    }

    private boolean getNoFallback() {
        return this.reader.getNoFallback();
    }

    private static ICUResourceBundle getBundle(ICUResourceBundleReader reader, String baseName, String localeID, ClassLoader loader) {
        int rootRes = reader.getRootResource();
        if (gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(rootRes)] != 2) {
            throw new IllegalStateException("Invalid format error");
        }
        ICUResourceBundleImpl.ResourceTable bundle = new ICUResourceBundleImpl.ResourceTable(reader, null, "", rootRes, null);
        bundle.baseName = baseName;
        bundle.localeID = localeID;
        bundle.ulocale = new ULocale(localeID);
        bundle.loader = loader;
        UResourceBundle alias = ((ICUResourceBundle)bundle).handleGetImpl("%%ALIAS", null, bundle, null, null);
        if (alias != null) {
            return (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, alias.getString());
        }
        return bundle;
    }

    protected ICUResourceBundle(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundle container) {
        this.reader = reader;
        this.key = key;
        this.resPath = resPath;
        this.resource = resource;
        if (container != null) {
            this.baseName = container.baseName;
            this.localeID = container.localeID;
            this.ulocale = container.ulocale;
            this.loader = container.loader;
            this.parent = container.parent;
        }
    }

    private String getAliasValue(int res) {
        String result = this.reader.getAlias(res);
        return result != null ? result : "";
    }

    protected ICUResourceBundle findResource(String key, String resPath, int _resource, HashMap<String, String> table, UResourceBundle requested) {
        String bundleName;
        int i;
        ClassLoader loaderToUse = this.loader;
        String locale = null;
        String keyPath = null;
        String rpath = this.getAliasValue(_resource);
        if (table == null) {
            table = new HashMap();
        }
        if (table.get(rpath) != null) {
            throw new IllegalArgumentException("Circular references in the resource bundles");
        }
        table.put(rpath, "");
        if (rpath.indexOf(47) == 0) {
            int idx;
            i = rpath.indexOf(47, 1);
            int j = rpath.indexOf(47, i + 1);
            bundleName = rpath.substring(1, i);
            if (j < 0) {
                locale = rpath.substring(i + 1);
                keyPath = resPath;
            } else {
                locale = rpath.substring(i + 1, j);
                keyPath = rpath.substring(j + 1, rpath.length());
            }
            if (bundleName.equals(ICUDATA)) {
                bundleName = ICU_BASE_NAME;
                loaderToUse = ICU_DATA_CLASS_LOADER;
            } else if (bundleName.indexOf(ICUDATA) > -1 && (idx = bundleName.indexOf(45)) > -1) {
                bundleName = "com/ibm/icu/impl/data/icudt51b/" + bundleName.substring(idx + 1, bundleName.length());
                loaderToUse = ICU_DATA_CLASS_LOADER;
            }
        } else {
            i = rpath.indexOf(47);
            if (i != -1) {
                locale = rpath.substring(0, i);
                keyPath = rpath.substring(i + 1);
            } else {
                locale = rpath;
                keyPath = resPath;
            }
            bundleName = this.baseName;
        }
        ICUResourceBundle bundle = null;
        ICUResourceBundle sub = null;
        if (bundleName.equals(LOCALE)) {
            bundleName = this.baseName;
            keyPath = rpath.substring(LOCALE.length() + 2, rpath.length());
            locale = ((ICUResourceBundle)requested).getLocaleID();
            bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance(bundleName, locale, loaderToUse, false);
            if (bundle != null) {
                sub = ICUResourceBundle.findResourceWithFallback(keyPath, bundle, null);
            }
        } else {
            String subKey;
            bundle = locale == null ? (ICUResourceBundle)ICUResourceBundle.getBundleInstance(bundleName, "", loaderToUse, false) : (ICUResourceBundle)ICUResourceBundle.getBundleInstance(bundleName, locale, loaderToUse, false);
            StringTokenizer st = new StringTokenizer(keyPath, RES_PATH_SEP_STR);
            ICUResourceBundle current = bundle;
            while (st.hasMoreTokens() && (sub = (ICUResourceBundle)current.get(subKey = st.nextToken(), table, requested)) != null) {
                current = sub;
            }
        }
        if (sub == null) {
            throw new MissingResourceException(this.localeID, this.baseName, key);
        }
        return sub;
    }

    protected void createLookupCache() {
        this.lookup = new SimpleCache<Object, UResourceBundle>(1, Math.max(this.getSize() * 2, 64));
    }

    @Override
    protected UResourceBundle handleGet(String resKey, HashMap<String, String> table, UResourceBundle requested) {
        boolean[] alias;
        int[] index;
        UResourceBundle res = null;
        if (this.lookup != null) {
            res = this.lookup.get(resKey);
        }
        if (res == null && (res = this.handleGetImpl(resKey, table, requested, index = new int[1], alias = new boolean[1])) != null && this.lookup != null && !alias[0]) {
            this.lookup.put(resKey, res);
            this.lookup.put(index[0], res);
        }
        return res;
    }

    @Override
    protected UResourceBundle handleGet(int index, HashMap<String, String> table, UResourceBundle requested) {
        boolean[] alias;
        UResourceBundle res = null;
        Integer indexKey = null;
        if (this.lookup != null) {
            indexKey = index;
            res = this.lookup.get(indexKey);
        }
        if (res == null && (res = this.handleGetImpl(index, table, requested, alias = new boolean[1])) != null && this.lookup != null && !alias[0]) {
            this.lookup.put(res.getKey(), res);
            this.lookup.put(indexKey, res);
        }
        return res;
    }

    protected UResourceBundle handleGetImpl(String resKey, HashMap<String, String> table, UResourceBundle requested, int[] index, boolean[] isAlias) {
        return null;
    }

    protected UResourceBundle handleGetImpl(int index, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias) {
        return null;
    }

    protected int getTableResource(String resKey) {
        return -1;
    }

    protected int getTableResource(int index) {
        return -1;
    }

    public boolean isAlias(int index) {
        return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(index)) == 3;
    }

    public boolean isAlias() {
        return ICUResourceBundleReader.RES_GET_TYPE(this.resource) == 3;
    }

    public boolean isAlias(String k) {
        return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(k)) == 3;
    }

    public String getAliasPath(int index) {
        return this.getAliasValue(this.getTableResource(index));
    }

    public String getAliasPath() {
        return this.getAliasValue(this.resource);
    }

    public String getAliasPath(String k) {
        return this.getAliasValue(this.getTableResource(k));
    }

    protected String getKey(int index) {
        return null;
    }

    public Enumeration<String> getKeysSafe() {
        if (!ICUResourceBundleReader.URES_IS_TABLE(this.resource)) {
            return this.getKeys();
        }
        ArrayList<String> v = new ArrayList<String>();
        int size = this.getSize();
        for (int index = 0; index < size; ++index) {
            String curKey = this.getKey(index);
            v.add(curKey);
        }
        return Collections.enumeration(v);
    }

    @Override
    protected Enumeration<String> handleGetKeys() {
        return Collections.enumeration(this.handleKeySet());
    }

    @Override
    protected boolean isTopLevelResource() {
        return this.resPath.length() == 0;
    }

    static {
        ClassLoader loader = ICUData.class.getClassLoader();
        if (loader == null) {
            loader = Utility.getFallbackClassLoader();
        }
        ICU_DATA_CLASS_LOADER = loader;
        DEBUG = ICUDebug.enabled("localedata");
        GET_AVAILABLE_CACHE = new SoftCache<String, AvailEntry, ClassLoader>(){

            @Override
            protected AvailEntry createInstance(String key, ClassLoader loader) {
                return new AvailEntry(key, loader);
            }
        };
        gPublicTypes = new int[]{0, 1, 2, 3, 2, 2, 0, 7, 8, 8, -1, -1, -1, -1, 14, -1};
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class AvailEntry {
        private String prefix;
        private ClassLoader loader;
        private volatile ULocale[] ulocales;
        private volatile Locale[] locales;
        private volatile Set<String> nameSet;
        private volatile Set<String> fullNameSet;

        AvailEntry(String prefix, ClassLoader loader) {
            this.prefix = prefix;
            this.loader = loader;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        ULocale[] getULocaleList() {
            if (this.ulocales == null) {
                AvailEntry availEntry = this;
                synchronized (availEntry) {
                    if (this.ulocales == null) {
                        this.ulocales = ICUResourceBundle.createULocaleList(this.prefix, this.loader);
                    }
                }
            }
            return this.ulocales;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        Locale[] getLocaleList() {
            if (this.locales == null) {
                AvailEntry availEntry = this;
                synchronized (availEntry) {
                    if (this.locales == null) {
                        this.locales = ICUResourceBundle.createLocaleList(this.prefix, this.loader);
                    }
                }
            }
            return this.locales;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        Set<String> getLocaleNameSet() {
            if (this.nameSet == null) {
                AvailEntry availEntry = this;
                synchronized (availEntry) {
                    if (this.nameSet == null) {
                        this.nameSet = ICUResourceBundle.createLocaleNameSet(this.prefix, this.loader);
                    }
                }
            }
            return this.nameSet;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        Set<String> getFullLocaleNameSet() {
            if (this.fullNameSet == null) {
                AvailEntry availEntry = this;
                synchronized (availEntry) {
                    if (this.fullNameSet == null) {
                        this.fullNameSet = ICUResourceBundle.createFullLocaleNameSet(this.prefix, this.loader);
                    }
                }
            }
            return this.fullNameSet;
        }
    }
}

