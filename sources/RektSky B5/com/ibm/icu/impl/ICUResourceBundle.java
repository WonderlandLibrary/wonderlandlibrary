/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.ClassLoaderUtil;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundleImpl;
import com.ibm.icu.impl.ICUResourceBundleReader;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.URLHandler;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import com.ibm.icu.util.UResourceTypeMismatchException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

public class ICUResourceBundle
extends UResourceBundle {
    public static final String NO_INHERITANCE_MARKER = "\u2205\u2205\u2205";
    public static final ClassLoader ICU_DATA_CLASS_LOADER = ClassLoaderUtil.getClassLoader(ICUData.class);
    protected static final String INSTALLED_LOCALES = "InstalledLocales";
    WholeBundle wholeBundle;
    private ICUResourceBundle container;
    private static CacheBase<String, ICUResourceBundle, Loader> BUNDLE_CACHE = new SoftCache<String, ICUResourceBundle, Loader>(){

        @Override
        protected ICUResourceBundle createInstance(String unusedKey, Loader loader) {
            return loader.load();
        }
    };
    private static final String ICU_RESOURCE_INDEX = "res_index";
    private static final String DEFAULT_TAG = "default";
    private static final String FULL_LOCALE_NAMES_LIST = "fullLocaleNames.lst";
    private static final boolean DEBUG = ICUDebug.enabled("localedata");
    private static CacheBase<String, AvailEntry, ClassLoader> GET_AVAILABLE_CACHE = new SoftCache<String, AvailEntry, ClassLoader>(){

        @Override
        protected AvailEntry createInstance(String key, ClassLoader loader) {
            return new AvailEntry(key, loader);
        }
    };
    protected String key;
    public static final int RES_BOGUS = -1;
    public static final int ALIAS = 3;
    public static final int TABLE32 = 4;
    public static final int TABLE16 = 5;
    public static final int STRING_V2 = 6;
    public static final int ARRAY16 = 9;
    private static final char RES_PATH_SEP_CHAR = '/';
    private static final String RES_PATH_SEP_STR = "/";
    private static final String ICUDATA = "ICUDATA";
    private static final char HYPHEN = '-';
    private static final String LOCALE = "LOCALE";

    public static final ULocale getFunctionalEquivalent(String baseName, ClassLoader loader, String resName, String keyword, ULocale locID, boolean[] isAvailable, boolean omitDefault) {
        ICUResourceBundle irb2;
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
        ICUResourceBundle r2 = null;
        r2 = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        if (isAvailable != null) {
            isAvailable[0] = false;
            ULocale[] availableULocales = ICUResourceBundle.getAvailEntry(baseName, loader).getULocaleList();
            for (int i2 = 0; i2 < availableULocales.length; ++i2) {
                if (!parent.equals(availableULocales[i2])) continue;
                isAvailable[0] = true;
                break;
            }
        }
        do {
            try {
                irb2 = (ICUResourceBundle)r2.get(resName);
                defStr = irb2.getString(DEFAULT_TAG);
                if (lookForDefault) {
                    kwVal = defStr;
                    lookForDefault = false;
                }
                defLoc = r2.getULocale();
            }
            catch (MissingResourceException irb2) {
                // empty catch block
            }
            if (defLoc != null) continue;
            r2 = r2.getParent();
            ++defDepth;
        } while (r2 != null && defLoc == null);
        parent = new ULocale(baseLoc);
        r2 = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        do {
            try {
                irb2 = (ICUResourceBundle)r2.get(resName);
                irb2.get(kwVal);
                fullBase = irb2.getULocale();
                if (fullBase != null && resDepth > defDepth) {
                    defStr = irb2.getString(DEFAULT_TAG);
                    defLoc = r2.getULocale();
                    defDepth = resDepth;
                }
            }
            catch (MissingResourceException irb3) {
                // empty catch block
            }
            if (fullBase != null) continue;
            r2 = r2.getParent();
            ++resDepth;
        } while (r2 != null && fullBase == null);
        if (fullBase == null && defStr != null && !defStr.equals(kwVal)) {
            kwVal = defStr;
            parent = new ULocale(baseLoc);
            r2 = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
            resDepth = 0;
            do {
                try {
                    irb2 = (ICUResourceBundle)r2.get(resName);
                    ICUResourceBundle urb = (ICUResourceBundle)irb2.get(kwVal);
                    fullBase = r2.getULocale();
                    if (!fullBase.getBaseName().equals(urb.getULocale().getBaseName())) {
                        fullBase = null;
                    }
                    if (fullBase != null && resDepth > defDepth) {
                        defStr = irb2.getString(DEFAULT_TAG);
                        defLoc = r2.getULocale();
                        defDepth = resDepth;
                    }
                }
                catch (MissingResourceException missingResourceException) {
                    // empty catch block
                }
                if (fullBase != null) continue;
                r2 = r2.getParent();
                ++resDepth;
            } while (r2 != null && fullBase == null);
        }
        if (fullBase == null) {
            throw new MissingResourceException("Could not find locale containing requested or default keyword.", baseName, keyword + "=" + kwVal);
        }
        if (omitDefault && defStr.equals(kwVal) && resDepth <= defDepth) {
            return fullBase;
        }
        return new ULocale(fullBase.getBaseName() + "@" + keyword + "=" + kwVal);
    }

    public static final String[] getKeywordValues(String baseName, String keyword) {
        HashSet<String> keywords = new HashSet<String>();
        ULocale[] locales = ICUResourceBundle.getAvailEntry(baseName, ICU_DATA_CLASS_LOADER).getULocaleList();
        for (int i2 = 0; i2 < locales.length; ++i2) {
            try {
                UResourceBundle b2 = UResourceBundle.getBundleInstance(baseName, locales[i2]);
                ICUResourceBundle irb = (ICUResourceBundle)b2.getObject(keyword);
                Enumeration<String> e2 = irb.getKeys();
                while (e2.hasMoreElements()) {
                    String s2 = e2.nextElement();
                    if (DEFAULT_TAG.equals(s2) || s2.startsWith("private-")) continue;
                    keywords.add(s2);
                }
                continue;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return keywords.toArray(new String[0]);
    }

    public ICUResourceBundle getWithFallback(String path) throws MissingResourceException {
        ICUResourceBundle actualBundle = this;
        ICUResourceBundle result = ICUResourceBundle.findResourceWithFallback(path, actualBundle, null);
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

    public String findStringWithFallback(String path) {
        return ICUResourceBundle.findStringWithFallback(path, this, null);
    }

    public String getStringWithFallback(String path) throws MissingResourceException {
        ICUResourceBundle actualBundle = this;
        String result = ICUResourceBundle.findStringWithFallback(path, actualBundle, null);
        if (result == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getType(), path, this.getKey());
        }
        if (result.equals(NO_INHERITANCE_MARKER)) {
            throw new MissingResourceException("Encountered NO_INHERITANCE_MARKER", path, this.getKey());
        }
        return result;
    }

    public void getAllItemsWithFallbackNoFail(String path, UResource.Sink sink) {
        try {
            this.getAllItemsWithFallback(path, sink);
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
    }

    public void getAllItemsWithFallback(String path, UResource.Sink sink) throws MissingResourceException {
        ICUResourceBundle rb;
        int numPathKeys = ICUResourceBundle.countPathKeys(path);
        if (numPathKeys == 0) {
            rb = this;
        } else {
            int depth = this.getResDepth();
            String[] pathKeys = new String[depth + numPathKeys];
            ICUResourceBundle.getResPathKeys(path, numPathKeys, pathKeys, depth);
            rb = ICUResourceBundle.findResourceWithFallback(pathKeys, depth, this, null);
            if (rb == null) {
                throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getType(), path, this.getKey());
            }
        }
        UResource.Key key = new UResource.Key();
        ICUResourceBundleReader.ReaderValue readerValue = new ICUResourceBundleReader.ReaderValue();
        rb.getAllItemsWithFallback(key, readerValue, sink);
    }

    private void getAllItemsWithFallback(UResource.Key key, ICUResourceBundleReader.ReaderValue readerValue, UResource.Sink sink) {
        ICUResourceBundleImpl impl = (ICUResourceBundleImpl)this;
        readerValue.reader = impl.wholeBundle.reader;
        readerValue.res = impl.getResource();
        key.setString(this.key != null ? this.key : "");
        sink.put(key, readerValue, this.parent == null);
        if (this.parent != null) {
            ICUResourceBundle rb;
            ICUResourceBundle parentBundle = (ICUResourceBundle)this.parent;
            int depth = this.getResDepth();
            if (depth == 0) {
                rb = parentBundle;
            } else {
                String[] pathKeys = new String[depth];
                this.getResPathKeys(pathKeys, depth);
                rb = ICUResourceBundle.findResourceWithFallback(pathKeys, 0, parentBundle, null);
            }
            if (rb != null) {
                rb.getAllItemsWithFallback(key, readerValue, sink);
            }
        }
    }

    public static Set<String> getAvailableLocaleNameSet(String bundlePrefix, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(bundlePrefix, loader).getLocaleNameSet();
    }

    public static Set<String> getFullLocaleNameSet() {
        return ICUResourceBundle.getFullLocaleNameSet("com/ibm/icu/impl/data/icudt62b", ICU_DATA_CLASS_LOADER);
    }

    public static Set<String> getFullLocaleNameSet(String bundlePrefix, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(bundlePrefix, loader).getFullLocaleNameSet();
    }

    public static Set<String> getAvailableLocaleNameSet() {
        return ICUResourceBundle.getAvailableLocaleNameSet("com/ibm/icu/impl/data/icudt62b", ICU_DATA_CLASS_LOADER);
    }

    public static final ULocale[] getAvailableULocales(String baseName, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(baseName, loader).getULocaleList();
    }

    public static final ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales("com/ibm/icu/impl/data/icudt62b", ICU_DATA_CLASS_LOADER);
    }

    public static final Locale[] getAvailableLocales(String baseName, ClassLoader loader) {
        return ICUResourceBundle.getAvailEntry(baseName, loader).getLocaleList();
    }

    public static final Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailEntry("com/ibm/icu/impl/data/icudt62b", ICU_DATA_CLASS_LOADER).getLocaleList();
    }

    public static final Locale[] getLocaleList(ULocale[] ulocales) {
        ArrayList<Locale> list = new ArrayList<Locale>(ulocales.length);
        HashSet<Locale> uniqueSet = new HashSet<Locale>();
        for (int i2 = 0; i2 < ulocales.length; ++i2) {
            Locale loc = ulocales[i2].toLocale();
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
        int i2 = 0;
        ULocale[] locales = new ULocale[length];
        UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            String locstr = iter.next().getKey();
            if (locstr.equals("root")) {
                locales[i2++] = ULocale.ROOT;
                continue;
            }
            locales[i2++] = new ULocale(locstr);
        }
        bundle = null;
        return locales;
    }

    private static final void addLocaleIDsFromIndexBundle(String baseName, ClassLoader root, Set<String> locales) {
        ICUResourceBundle bundle;
        try {
            bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, ICU_RESOURCE_INDEX, root, true);
            bundle = (ICUResourceBundle)bundle.get(INSTALLED_LOCALES);
        }
        catch (MissingResourceException e2) {
            if (DEBUG) {
                System.out.println("couldn't find " + baseName + '/' + ICU_RESOURCE_INDEX + ".res");
                Thread.dumpStack();
            }
            return;
        }
        UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            String locstr = iter.next().getKey();
            locales.add(locstr);
        }
    }

    private static final void addBundleBaseNamesFromClassLoader(final String bn, final ClassLoader root, final Set<String> names) {
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                block5: {
                    try {
                        Enumeration<URL> urls = root.getResources(bn);
                        if (urls == null) {
                            return null;
                        }
                        URLHandler.URLVisitor v2 = new URLHandler.URLVisitor(){

                            @Override
                            public void visit(String s2) {
                                if (s2.endsWith(".res")) {
                                    String locstr = s2.substring(0, s2.length() - 4);
                                    names.add(locstr);
                                }
                            }
                        };
                        while (urls.hasMoreElements()) {
                            URL url = urls.nextElement();
                            URLHandler handler = URLHandler.get(url);
                            if (handler != null) {
                                handler.guide(v2, false);
                                continue;
                            }
                            if (!DEBUG) continue;
                            System.out.println("handler for " + url + " is null");
                        }
                    }
                    catch (IOException e2) {
                        if (!DEBUG) break block5;
                        System.out.println("ouch: " + e2.getMessage());
                    }
                }
                return null;
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void addLocaleIDsFromListFile(String bn, ClassLoader root, Set<String> locales) {
        block6: {
            try {
                InputStream s2 = root.getResourceAsStream(bn + FULL_LOCALE_NAMES_LIST);
                if (s2 == null) break block6;
                BufferedReader br = new BufferedReader(new InputStreamReader(s2, "ASCII"));
                try {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.length() == 0 || line.startsWith("#")) continue;
                        locales.add(line);
                    }
                }
                finally {
                    br.close();
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static Set<String> createFullLocaleNameSet(String baseName, ClassLoader loader) {
        String bn = baseName.endsWith(RES_PATH_SEP_STR) ? baseName : baseName + RES_PATH_SEP_STR;
        HashSet<String> set = new HashSet<String>();
        String skipScan = ICUConfig.get("com.ibm.icu.impl.ICUResourceBundle.skipRuntimeLocaleResourceScan", "false");
        if (!skipScan.equalsIgnoreCase("true")) {
            String folder;
            ICUResourceBundle.addBundleBaseNamesFromClassLoader(bn, loader, set);
            if (baseName.startsWith("com/ibm/icu/impl/data/icudt62b") && (folder = baseName.length() == "com/ibm/icu/impl/data/icudt62b".length() ? "" : (baseName.charAt("com/ibm/icu/impl/data/icudt62b".length()) == '/' ? baseName.substring("com/ibm/icu/impl/data/icudt62b".length() + 1) : null)) != null) {
                ICUBinary.addBaseNamesInFileFolder(folder, ".res", set);
            }
            set.remove(ICU_RESOURCE_INDEX);
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                String name = (String)iter.next();
                if (name.length() != 1 && name.length() <= 3 || name.indexOf(95) >= 0) continue;
                iter.remove();
            }
        }
        if (set.isEmpty()) {
            if (DEBUG) {
                System.out.println("unable to enumerate data files in " + baseName);
            }
            ICUResourceBundle.addLocaleIDsFromListFile(bn, loader, set);
        }
        if (set.isEmpty()) {
            ICUResourceBundle.addLocaleIDsFromIndexBundle(baseName, loader, set);
        }
        set.remove("root");
        set.add(ULocale.ROOT.toString());
        return Collections.unmodifiableSet(set);
    }

    private static Set<String> createLocaleNameSet(String baseName, ClassLoader loader) {
        HashSet<String> set = new HashSet<String>();
        ICUResourceBundle.addLocaleIDsFromIndexBundle(baseName, loader, set);
        return Collections.unmodifiableSet(set);
    }

    private static AvailEntry getAvailEntry(String key, ClassLoader loader) {
        return GET_AVAILABLE_CACHE.getInstance(key, loader);
    }

    private static final ICUResourceBundle findResourceWithFallback(String path, UResourceBundle actualBundle, UResourceBundle requested) {
        if (path.length() == 0) {
            return null;
        }
        ICUResourceBundle base = (ICUResourceBundle)actualBundle;
        int depth = base.getResDepth();
        int numPathKeys = ICUResourceBundle.countPathKeys(path);
        assert (numPathKeys > 0);
        String[] keys = new String[depth + numPathKeys];
        ICUResourceBundle.getResPathKeys(path, numPathKeys, keys, depth);
        return ICUResourceBundle.findResourceWithFallback(keys, depth, base, requested);
    }

    private static final ICUResourceBundle findResourceWithFallback(String[] keys, int depth, ICUResourceBundle base, UResourceBundle requested) {
        if (requested == null) {
            requested = base;
        }
        while (true) {
            String subKey;
            ICUResourceBundle sub;
            if ((sub = (ICUResourceBundle)base.handleGet(subKey = keys[depth++], null, requested)) == null) {
                --depth;
            } else {
                if (depth == keys.length) {
                    return sub;
                }
                base = sub;
                continue;
            }
            ICUResourceBundle nextBase = base.getParent();
            if (nextBase == null) {
                return null;
            }
            int baseDepth = base.getResDepth();
            if (depth != baseDepth) {
                String[] newKeys = new String[baseDepth + (keys.length - depth)];
                System.arraycopy(keys, depth, newKeys, baseDepth, keys.length - depth);
                keys = newKeys;
            }
            base.getResPathKeys(keys, baseDepth);
            base = nextBase;
            depth = 0;
        }
    }

    /*
     * Unable to fully structure code
     */
    private static final String findStringWithFallback(String path, UResourceBundle actualBundle, UResourceBundle requested) {
        if (path.length() == 0) {
            return null;
        }
        if (!(actualBundle instanceof ICUResourceBundleImpl.ResourceContainer)) {
            return null;
        }
        if (requested == null) {
            requested = actualBundle;
        }
        base = (ICUResourceBundle)actualBundle;
        reader = base.wholeBundle.reader;
        res = -1;
        depth = baseDepth = base.getResDepth();
        numPathKeys = ICUResourceBundle.countPathKeys(path);
        if (!ICUResourceBundle.$assertionsDisabled && numPathKeys <= 0) {
            throw new AssertionError();
        }
        keys = new String[depth + numPathKeys];
        ICUResourceBundle.getResPathKeys(path, numPathKeys, keys, depth);
        while (true) {
            block16: {
                block17: {
                    block15: {
                        if (res != -1) break block15;
                        type = base.getType();
                        if (type != 2 && type != 8) break block16;
                        readerContainer = ((ICUResourceBundleImpl.ResourceContainer)base).value;
                        ** GOTO lbl32
                    }
                    type = ICUResourceBundleReader.RES_GET_TYPE(res);
                    if (!ICUResourceBundleReader.URES_IS_TABLE(type)) break block17;
                    readerContainer = reader.getTable(res);
                    ** GOTO lbl32
                }
                if (!ICUResourceBundleReader.URES_IS_ARRAY(type)) {
                    res = -1;
                } else {
                    readerContainer = reader.getArray(res);
lbl32:
                    // 3 sources

                    subKey = keys[depth++];
                    res = readerContainer.getResource(reader, subKey);
                    if (res == -1) {
                        --depth;
                    } else {
                        if (ICUResourceBundleReader.RES_GET_TYPE(res) == 3) {
                            base.getResPathKeys(keys, baseDepth);
                            sub = ICUResourceBundle.getAliasedResource((ICUResourceBundle)base, keys, depth, subKey, res, null, requested);
                        } else {
                            sub = null;
                        }
                        if (depth == keys.length) {
                            if (sub != null) {
                                return sub.getString();
                            }
                            s = reader.getString(res);
                            if (s == null) {
                                throw new UResourceTypeMismatchException("");
                            }
                            return s;
                        }
                        if (sub == null) continue;
                        base = sub;
                        reader = base.wholeBundle.reader;
                        res = -1;
                        baseDepth = base.getResDepth();
                        if (depth == baseDepth) continue;
                        newKeys = new String[baseDepth + (keys.length - depth)];
                        System.arraycopy(keys, depth, newKeys, baseDepth, keys.length - depth);
                        keys = newKeys;
                        depth = baseDepth;
                        continue;
                    }
                }
            }
            if ((nextBase = base.getParent()) == null) {
                return null;
            }
            base.getResPathKeys(keys, baseDepth);
            base = nextBase;
            reader = base.wholeBundle.reader;
            baseDepth = 0;
            depth = 0;
        }
    }

    private int getResDepth() {
        return this.container == null ? 0 : this.container.getResDepth() + 1;
    }

    private void getResPathKeys(String[] keys, int depth) {
        ICUResourceBundle b2 = this;
        while (depth > 0) {
            keys[--depth] = b2.key;
            b2 = b2.container;
            assert (depth == 0 == (b2.container == null));
        }
    }

    private static int countPathKeys(String path) {
        if (path.isEmpty()) {
            return 0;
        }
        int num = 1;
        for (int i2 = 0; i2 < path.length(); ++i2) {
            if (path.charAt(i2) != '/') continue;
            ++num;
        }
        return num;
    }

    private static void getResPathKeys(String path, int num, String[] keys, int start) {
        int j2;
        if (num == 0) {
            return;
        }
        if (num == 1) {
            keys[start] = path;
            return;
        }
        int i2 = 0;
        while (true) {
            j2 = path.indexOf(47, i2);
            assert (j2 >= i2);
            keys[start++] = path.substring(i2, j2);
            if (num == 2) {
                assert (path.indexOf(47, j2 + 1) < 0);
                break;
            }
            i2 = j2 + 1;
            --num;
        }
        keys[start] = path.substring(j2 + 1);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof ICUResourceBundle) {
            ICUResourceBundle o2 = (ICUResourceBundle)other;
            if (this.getBaseName().equals(o2.getBaseName()) && this.getLocaleID().equals(o2.getLocaleID())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    public static ICUResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback) {
        return ICUResourceBundle.getBundleInstance(baseName, localeID, root, disableFallback ? OpenType.DIRECT : OpenType.LOCALE_DEFAULT_ROOT);
    }

    public static ICUResourceBundle getBundleInstance(String baseName, ULocale locale, OpenType openType) {
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return ICUResourceBundle.getBundleInstance(baseName, locale.getBaseName(), ICU_DATA_CLASS_LOADER, openType);
    }

    public static ICUResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, OpenType openType) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt62b";
        }
        localeID = ULocale.getBaseName(localeID);
        ICUResourceBundle b2 = openType == OpenType.LOCALE_DEFAULT_ROOT ? ICUResourceBundle.instantiateBundle(baseName, localeID, ULocale.getDefault().getBaseName(), root, openType) : ICUResourceBundle.instantiateBundle(baseName, localeID, null, root, openType);
        if (b2 == null) {
            throw new MissingResourceException("Could not find the bundle " + baseName + RES_PATH_SEP_STR + localeID + ".res", "", "");
        }
        return b2;
    }

    private static boolean localeIDStartsWithLangSubtag(String localeID, String lang) {
        return localeID.startsWith(lang) && (localeID.length() == lang.length() || localeID.charAt(lang.length()) == '_');
    }

    private static ICUResourceBundle instantiateBundle(final String baseName, final String localeID, final String defaultID, final ClassLoader root, final OpenType openType) {
        assert (localeID.indexOf(64) < 0);
        assert (defaultID == null || defaultID.indexOf(64) < 0);
        final String fullName = ICUResourceBundleReader.getFullName(baseName, localeID);
        char openTypeChar = (char)(48 + openType.ordinal());
        String cacheKey = openType != OpenType.LOCALE_DEFAULT_ROOT ? fullName + '#' + openTypeChar : fullName + '#' + openTypeChar + '#' + defaultID;
        return BUNDLE_CACHE.getInstance(cacheKey, new Loader(){

            @Override
            public ICUResourceBundle load() {
                if (DEBUG) {
                    System.out.println("Creating " + fullName);
                }
                String rootLocale = baseName.indexOf(46) == -1 ? "root" : "";
                String localeName = localeID.isEmpty() ? rootLocale : localeID;
                ICUResourceBundle b2 = ICUResourceBundle.createBundle(baseName, localeName, root);
                if (DEBUG) {
                    System.out.println("The bundle created is: " + b2 + " and openType=" + (Object)((Object)openType) + " and bundle.getNoFallback=" + (b2 != null && b2.getNoFallback()));
                }
                if (openType == OpenType.DIRECT || b2 != null && b2.getNoFallback()) {
                    return b2;
                }
                if (b2 == null) {
                    int i2 = localeName.lastIndexOf(95);
                    if (i2 != -1) {
                        String temp = localeName.substring(0, i2);
                        b2 = ICUResourceBundle.instantiateBundle(baseName, temp, defaultID, root, openType);
                    } else if (openType == OpenType.LOCALE_DEFAULT_ROOT && !ICUResourceBundle.localeIDStartsWithLangSubtag(defaultID, localeName)) {
                        b2 = ICUResourceBundle.instantiateBundle(baseName, defaultID, defaultID, root, openType);
                    } else if (openType != OpenType.LOCALE_ONLY && !rootLocale.isEmpty()) {
                        b2 = ICUResourceBundle.createBundle(baseName, rootLocale, root);
                    }
                } else {
                    ICUResourceBundle parent = null;
                    localeName = b2.getLocaleID();
                    int i3 = localeName.lastIndexOf(95);
                    String parentLocaleName = ((ICUResourceBundleImpl.ResourceTable)b2).findString("%%Parent");
                    if (parentLocaleName != null) {
                        parent = ICUResourceBundle.instantiateBundle(baseName, parentLocaleName, defaultID, root, openType);
                    } else if (i3 != -1) {
                        parent = ICUResourceBundle.instantiateBundle(baseName, localeName.substring(0, i3), defaultID, root, openType);
                    } else if (!localeName.equals(rootLocale)) {
                        parent = ICUResourceBundle.instantiateBundle(baseName, rootLocale, defaultID, root, openType);
                    }
                    if (!b2.equals(parent)) {
                        b2.setParent(parent);
                    }
                }
                return b2;
            }
        });
    }

    ICUResourceBundle get(String aKey, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
        ICUResourceBundle obj = (ICUResourceBundle)this.handleGet(aKey, aliasesVisited, requested);
        if (obj == null) {
            obj = this.getParent();
            if (obj != null) {
                obj = obj.get(aKey, aliasesVisited, requested);
            }
            if (obj == null) {
                String fullName = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
                throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, this.getClass().getName(), aKey);
            }
        }
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
        return this.wholeBundle.localeID;
    }

    @Override
    protected String getBaseName() {
        return this.wholeBundle.baseName;
    }

    @Override
    public ULocale getULocale() {
        return this.wholeBundle.ulocale;
    }

    public boolean isRoot() {
        return this.wholeBundle.localeID.isEmpty() || this.wholeBundle.localeID.equals("root");
    }

    @Override
    public ICUResourceBundle getParent() {
        return (ICUResourceBundle)this.parent;
    }

    @Override
    protected void setParent(ResourceBundle parent) {
        this.parent = parent;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    private boolean getNoFallback() {
        return this.wholeBundle.reader.getNoFallback();
    }

    private static ICUResourceBundle getBundle(ICUResourceBundleReader reader, String baseName, String localeID, ClassLoader loader) {
        int rootRes = reader.getRootResource();
        if (!ICUResourceBundleReader.URES_IS_TABLE(ICUResourceBundleReader.RES_GET_TYPE(rootRes))) {
            throw new IllegalStateException("Invalid format error");
        }
        WholeBundle wb = new WholeBundle(baseName, localeID, loader, reader);
        ICUResourceBundleImpl.ResourceTable rootTable = new ICUResourceBundleImpl.ResourceTable(wb, rootRes);
        String aliasString = rootTable.findString("%%ALIAS");
        if (aliasString != null) {
            return (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, aliasString);
        }
        return rootTable;
    }

    protected ICUResourceBundle(WholeBundle wholeBundle) {
        this.wholeBundle = wholeBundle;
    }

    protected ICUResourceBundle(ICUResourceBundle container, String key) {
        this.key = key;
        this.wholeBundle = container.wholeBundle;
        this.container = container;
        this.parent = container.parent;
    }

    protected static ICUResourceBundle getAliasedResource(ICUResourceBundle base, String[] keys, int depth, String key, int _resource, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
        String locale;
        String bundleName;
        int i2;
        WholeBundle wholeBundle = base.wholeBundle;
        ClassLoader loaderToUse = wholeBundle.loader;
        String keyPath = null;
        String rpath = wholeBundle.reader.getAlias(_resource);
        if (aliasesVisited == null) {
            aliasesVisited = new HashMap();
        }
        if (aliasesVisited.get(rpath) != null) {
            throw new IllegalArgumentException("Circular references in the resource bundles");
        }
        aliasesVisited.put(rpath, "");
        if (rpath.indexOf(47) == 0) {
            int idx;
            i2 = rpath.indexOf(47, 1);
            int j2 = rpath.indexOf(47, i2 + 1);
            bundleName = rpath.substring(1, i2);
            if (j2 < 0) {
                locale = rpath.substring(i2 + 1);
            } else {
                locale = rpath.substring(i2 + 1, j2);
                keyPath = rpath.substring(j2 + 1, rpath.length());
            }
            if (bundleName.equals(ICUDATA)) {
                bundleName = "com/ibm/icu/impl/data/icudt62b";
                loaderToUse = ICU_DATA_CLASS_LOADER;
            } else if (bundleName.indexOf(ICUDATA) > -1 && (idx = bundleName.indexOf(45)) > -1) {
                bundleName = "com/ibm/icu/impl/data/icudt62b/" + bundleName.substring(idx + 1, bundleName.length());
                loaderToUse = ICU_DATA_CLASS_LOADER;
            }
        } else {
            i2 = rpath.indexOf(47);
            if (i2 != -1) {
                locale = rpath.substring(0, i2);
                keyPath = rpath.substring(i2 + 1);
            } else {
                locale = rpath;
            }
            bundleName = wholeBundle.baseName;
        }
        ICUResourceBundle bundle = null;
        ICUResourceBundle sub = null;
        if (bundleName.equals(LOCALE)) {
            bundleName = wholeBundle.baseName;
            keyPath = rpath.substring(LOCALE.length() + 2, rpath.length());
            bundle = (ICUResourceBundle)requested;
            while (bundle.container != null) {
                bundle = bundle.container;
            }
            sub = ICUResourceBundle.findResourceWithFallback(keyPath, bundle, null);
        } else {
            int numKeys;
            bundle = ICUResourceBundle.getBundleInstance(bundleName, locale, loaderToUse, false);
            if (keyPath != null) {
                numKeys = ICUResourceBundle.countPathKeys(keyPath);
                if (numKeys > 0) {
                    keys = new String[numKeys];
                    ICUResourceBundle.getResPathKeys(keyPath, numKeys, keys, 0);
                }
            } else if (keys != null) {
                numKeys = depth;
            } else {
                depth = base.getResDepth();
                numKeys = depth + 1;
                keys = new String[numKeys];
                base.getResPathKeys(keys, depth);
                keys[depth] = key;
            }
            if (numKeys > 0) {
                sub = bundle;
                for (int i3 = 0; sub != null && i3 < numKeys; sub = sub.get(keys[i3], aliasesVisited, requested), ++i3) {
                }
            }
        }
        if (sub == null) {
            throw new MissingResourceException(wholeBundle.localeID, wholeBundle.baseName, key);
        }
        return sub;
    }

    @Deprecated
    public final Set<String> getTopLevelKeySet() {
        return this.wholeBundle.topLevelKeys;
    }

    @Deprecated
    public final void setTopLevelKeySet(Set<String> keySet) {
        this.wholeBundle.topLevelKeys = keySet;
    }

    @Override
    protected Enumeration<String> handleGetKeys() {
        return Collections.enumeration(this.handleKeySet());
    }

    @Override
    protected boolean isTopLevelResource() {
        return this.container == null;
    }

    public static enum OpenType {
        LOCALE_DEFAULT_ROOT,
        LOCALE_ROOT,
        LOCALE_ONLY,
        DIRECT;

    }

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
                this.getULocaleList();
                AvailEntry availEntry = this;
                synchronized (availEntry) {
                    if (this.locales == null) {
                        this.locales = ICUResourceBundle.getLocaleList(this.ulocales);
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

    private static abstract class Loader {
        private Loader() {
        }

        abstract ICUResourceBundle load();
    }

    protected static final class WholeBundle {
        String baseName;
        String localeID;
        ULocale ulocale;
        ClassLoader loader;
        ICUResourceBundleReader reader;
        Set<String> topLevelKeys;

        WholeBundle(String baseName, String localeID, ClassLoader loader, ICUResourceBundleReader reader) {
            this.baseName = baseName;
            this.localeID = localeID;
            this.ulocale = new ULocale(localeID);
            this.loader = loader;
            this.reader = reader;
        }
    }
}

