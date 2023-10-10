/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationRoot;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.RawCollationKey;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class Collator
implements Comparator<Object>,
Freezable<Collator>,
Cloneable {
    public static final int PRIMARY = 0;
    public static final int SECONDARY = 1;
    public static final int TERTIARY = 2;
    public static final int QUATERNARY = 3;
    public static final int IDENTICAL = 15;
    public static final int FULL_DECOMPOSITION = 15;
    public static final int NO_DECOMPOSITION = 16;
    public static final int CANONICAL_DECOMPOSITION = 17;
    private static ServiceShim shim;
    private static final String[] KEYWORDS;
    private static final String RESOURCE = "collations";
    private static final String BASE = "com/ibm/icu/impl/data/icudt62b/coll";
    private static final boolean DEBUG;

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && this.getClass() == obj.getClass();
    }

    public int hashCode() {
        return 0;
    }

    private void checkNotFrozen() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen Collator");
        }
    }

    public void setStrength(int newStrength) {
        this.checkNotFrozen();
    }

    @Deprecated
    public Collator setStrength2(int newStrength) {
        this.setStrength(newStrength);
        return this;
    }

    public void setDecomposition(int decomposition) {
        this.checkNotFrozen();
    }

    public void setReorderCodes(int ... order) {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    public static final Collator getInstance() {
        return Collator.getInstance(ULocale.getDefault());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static ServiceShim getShim() {
        if (shim == null) {
            try {
                Class<?> cls = Class.forName("com.ibm.icu.text.CollatorServiceShim");
                shim = (ServiceShim)cls.newInstance();
            }
            catch (MissingResourceException e2) {
                throw e2;
            }
            catch (Exception e3) {
                if (DEBUG) {
                    e3.printStackTrace();
                }
                throw new ICUException(e3);
            }
        }
        return shim;
    }

    private static final boolean getYesOrNo(String keyword, String s2) {
        if (ASCII.equalIgnoreCase(s2, "yes")) {
            return true;
        }
        if (ASCII.equalIgnoreCase(s2, "no")) {
            return false;
        }
        throw new IllegalArgumentException("illegal locale keyword=value: " + keyword + "=" + s2);
    }

    private static final int getIntValue(String keyword, String s2, String ... values) {
        for (int i2 = 0; i2 < values.length; ++i2) {
            if (!ASCII.equalIgnoreCase(s2, values[i2])) continue;
            return i2;
        }
        throw new IllegalArgumentException("illegal locale keyword=value: " + keyword + "=" + s2);
    }

    private static final int getReorderCode(String keyword, String s2) {
        return 4096 + Collator.getIntValue(keyword, s2, "space", "punct", "symbol", "currency", "digit");
    }

    private static void setAttributesFromKeywords(ULocale loc, Collator coll, RuleBasedCollator rbc) {
        String value = loc.getKeywordValue("colHiraganaQuaternary");
        if (value != null) {
            throw new UnsupportedOperationException("locale keyword kh/colHiraganaQuaternary");
        }
        value = loc.getKeywordValue("variableTop");
        if (value != null) {
            throw new UnsupportedOperationException("locale keyword vt/variableTop");
        }
        value = loc.getKeywordValue("colStrength");
        if (value != null) {
            int strength = Collator.getIntValue("colStrength", value, "primary", "secondary", "tertiary", "quaternary", "identical");
            coll.setStrength(strength <= 3 ? strength : 15);
        }
        if ((value = loc.getKeywordValue("colBackwards")) != null) {
            if (rbc != null) {
                rbc.setFrenchCollation(Collator.getYesOrNo("colBackwards", value));
            } else {
                throw new UnsupportedOperationException("locale keyword kb/colBackwards only settable for RuleBasedCollator");
            }
        }
        if ((value = loc.getKeywordValue("colCaseLevel")) != null) {
            if (rbc != null) {
                rbc.setCaseLevel(Collator.getYesOrNo("colCaseLevel", value));
            } else {
                throw new UnsupportedOperationException("locale keyword kb/colBackwards only settable for RuleBasedCollator");
            }
        }
        if ((value = loc.getKeywordValue("colCaseFirst")) != null) {
            if (rbc != null) {
                int cf = Collator.getIntValue("colCaseFirst", value, "no", "lower", "upper");
                if (cf == 0) {
                    rbc.setLowerCaseFirst(false);
                    rbc.setUpperCaseFirst(false);
                } else if (cf == 1) {
                    rbc.setLowerCaseFirst(true);
                } else {
                    rbc.setUpperCaseFirst(true);
                }
            } else {
                throw new UnsupportedOperationException("locale keyword kf/colCaseFirst only settable for RuleBasedCollator");
            }
        }
        if ((value = loc.getKeywordValue("colAlternate")) != null) {
            if (rbc != null) {
                rbc.setAlternateHandlingShifted(Collator.getIntValue("colAlternate", value, "non-ignorable", "shifted") != 0);
            } else {
                throw new UnsupportedOperationException("locale keyword ka/colAlternate only settable for RuleBasedCollator");
            }
        }
        if ((value = loc.getKeywordValue("colNormalization")) != null) {
            coll.setDecomposition(Collator.getYesOrNo("colNormalization", value) ? 17 : 16);
        }
        if ((value = loc.getKeywordValue("colNumeric")) != null) {
            if (rbc != null) {
                rbc.setNumericCollation(Collator.getYesOrNo("colNumeric", value));
            } else {
                throw new UnsupportedOperationException("locale keyword kn/colNumeric only settable for RuleBasedCollator");
            }
        }
        if ((value = loc.getKeywordValue("colReorder")) != null) {
            int[] codes = new int[190];
            int codesLength = 0;
            int scriptNameStart = 0;
            while (true) {
                int limit;
                if (codesLength == codes.length) {
                    throw new IllegalArgumentException("too many script codes for colReorder locale keyword: " + value);
                }
                for (limit = scriptNameStart; limit < value.length() && value.charAt(limit) != '-'; ++limit) {
                }
                String scriptName = value.substring(scriptNameStart, limit);
                int code = scriptName.length() == 4 ? UCharacter.getPropertyValueEnum(4106, scriptName) : Collator.getReorderCode("colReorder", scriptName);
                codes[codesLength++] = code;
                if (limit == value.length()) break;
                scriptNameStart = limit + 1;
            }
            if (codesLength == 0) {
                throw new IllegalArgumentException("no script codes for colReorder locale keyword");
            }
            int[] args = new int[codesLength];
            System.arraycopy(codes, 0, args, 0, codesLength);
            coll.setReorderCodes(args);
        }
        if ((value = loc.getKeywordValue("kv")) != null) {
            coll.setMaxVariable(Collator.getReorderCode("kv", value));
        }
    }

    public static final Collator getInstance(ULocale locale) {
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        Collator coll = Collator.getShim().getInstance(locale);
        if (!locale.getName().equals(locale.getBaseName())) {
            Collator.setAttributesFromKeywords(locale, coll, coll instanceof RuleBasedCollator ? (RuleBasedCollator)coll : null);
        }
        return coll;
    }

    public static final Collator getInstance(Locale locale) {
        return Collator.getInstance(ULocale.forLocale(locale));
    }

    public static final Object registerInstance(Collator collator, ULocale locale) {
        return Collator.getShim().registerInstance(collator, locale);
    }

    public static final Object registerFactory(CollatorFactory factory) {
        return Collator.getShim().registerFactory(factory);
    }

    public static final boolean unregister(Object registryKey) {
        if (shim == null) {
            return false;
        }
        return shim.unregister(registryKey);
    }

    public static Locale[] getAvailableLocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableLocales(BASE, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        }
        return shim.getAvailableLocales();
    }

    public static final ULocale[] getAvailableULocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableULocales(BASE, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        }
        return shim.getAvailableULocales();
    }

    public static final String[] getKeywords() {
        return KEYWORDS;
    }

    public static final String[] getKeywordValues(String keyword) {
        if (!keyword.equals(KEYWORDS[0])) {
            throw new IllegalArgumentException("Invalid keyword: " + keyword);
        }
        return ICUResourceBundle.getKeywordValues(BASE, RESOURCE);
    }

    public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(BASE, locale);
        KeywordsSink sink = new KeywordsSink();
        bundle.getAllItemsWithFallback(RESOURCE, sink);
        return sink.values.toArray(new String[sink.values.size()]);
    }

    public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID, boolean[] isAvailable) {
        return ICUResourceBundle.getFunctionalEquivalent(BASE, ICUResourceBundle.ICU_DATA_CLASS_LOADER, RESOURCE, keyword, locID, isAvailable, true);
    }

    public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID) {
        return Collator.getFunctionalEquivalent(keyword, locID, null);
    }

    public static String getDisplayName(Locale objectLocale, Locale displayLocale) {
        return Collator.getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
    }

    public static String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
        return Collator.getShim().getDisplayName(objectLocale, displayLocale);
    }

    public static String getDisplayName(Locale objectLocale) {
        return Collator.getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public static String getDisplayName(ULocale objectLocale) {
        return Collator.getShim().getDisplayName(objectLocale, ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public int getStrength() {
        return 2;
    }

    public int getDecomposition() {
        return 16;
    }

    public boolean equals(String source, String target) {
        return this.compare(source, target) == 0;
    }

    public UnicodeSet getTailoredSet() {
        return new UnicodeSet(0, 0x10FFFF);
    }

    @Override
    public abstract int compare(String var1, String var2);

    @Override
    public int compare(Object source, Object target) {
        return this.doCompare((CharSequence)source, (CharSequence)target);
    }

    @Deprecated
    protected int doCompare(CharSequence left, CharSequence right) {
        return this.compare(left.toString(), right.toString());
    }

    public abstract CollationKey getCollationKey(String var1);

    public abstract RawCollationKey getRawCollationKey(String var1, RawCollationKey var2);

    public Collator setMaxVariable(int group) {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    public int getMaxVariable() {
        return 4097;
    }

    @Deprecated
    public abstract int setVariableTop(String var1);

    public abstract int getVariableTop();

    @Deprecated
    public abstract void setVariableTop(int var1);

    public abstract VersionInfo getVersion();

    public abstract VersionInfo getUCAVersion();

    public int[] getReorderCodes() {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    public static int[] getEquivalentReorderCodes(int reorderCode) {
        CollationData baseData = CollationRoot.getData();
        return baseData.getEquivalentScripts(reorderCode);
    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public Collator freeze() {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    @Override
    public Collator cloneAsThawed() {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    protected Collator() {
    }

    public ULocale getLocale(ULocale.Type type) {
        return ULocale.ROOT;
    }

    void setLocale(ULocale valid, ULocale actual) {
    }

    static {
        KEYWORDS = new String[]{"collation"};
        DEBUG = ICUDebug.enabled("collator");
    }

    private static final class KeywordsSink
    extends UResource.Sink {
        LinkedList<String> values = new LinkedList();
        boolean hasDefault = false;

        private KeywordsSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table collations = value.getTable();
            int i2 = 0;
            while (collations.getKeyAndValue(i2, key, value)) {
                String collkey;
                int type = value.getType();
                if (type == 0) {
                    String defcoll;
                    if (!this.hasDefault && key.contentEquals("default") && !(defcoll = value.getString()).isEmpty()) {
                        this.values.remove(defcoll);
                        this.values.addFirst(defcoll);
                        this.hasDefault = true;
                    }
                } else if (type == 2 && !key.startsWith("private-") && !this.values.contains(collkey = key.toString())) {
                    this.values.add(collkey);
                }
                ++i2;
            }
        }
    }

    private static final class ASCII {
        private ASCII() {
        }

        static boolean equalIgnoreCase(CharSequence left, CharSequence right) {
            int length = left.length();
            if (length != right.length()) {
                return false;
            }
            for (int i2 = 0; i2 < length; ++i2) {
                char rc;
                char lc = left.charAt(i2);
                if (lc == (rc = right.charAt(i2)) || ('A' <= lc && lc <= 'Z' ? lc + 32 == rc : 'A' <= rc && rc <= 'Z' && rc + 32 == lc)) continue;
                return false;
            }
            return true;
        }
    }

    static abstract class ServiceShim {
        ServiceShim() {
        }

        abstract Collator getInstance(ULocale var1);

        abstract Object registerInstance(Collator var1, ULocale var2);

        abstract Object registerFactory(CollatorFactory var1);

        abstract boolean unregister(Object var1);

        abstract Locale[] getAvailableLocales();

        abstract ULocale[] getAvailableULocales();

        abstract String getDisplayName(ULocale var1, ULocale var2);
    }

    public static abstract class CollatorFactory {
        public boolean visible() {
            return true;
        }

        public Collator createCollator(ULocale loc) {
            return this.createCollator(loc.toLocale());
        }

        public Collator createCollator(Locale loc) {
            return this.createCollator(ULocale.forLocale(loc));
        }

        public String getDisplayName(Locale objectLocale, Locale displayLocale) {
            return this.getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
        }

        public String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
            String name;
            Set<String> supported;
            if (this.visible() && (supported = this.getSupportedLocaleIDs()).contains(name = objectLocale.getBaseName())) {
                return objectLocale.getDisplayName(displayLocale);
            }
            return null;
        }

        public abstract Set<String> getSupportedLocaleIDs();

        protected CollatorFactory() {
        }
    }

    public static interface ReorderCodes {
        public static final int DEFAULT = -1;
        public static final int NONE = 103;
        public static final int OTHERS = 103;
        public static final int SPACE = 4096;
        public static final int FIRST = 4096;
        public static final int PUNCTUATION = 4097;
        public static final int SYMBOL = 4098;
        public static final int CURRENCY = 4099;
        public static final int DIGIT = 4100;
        @Deprecated
        public static final int LIMIT = 4101;
    }
}

