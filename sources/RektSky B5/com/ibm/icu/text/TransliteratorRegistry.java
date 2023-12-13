/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.LocaleUtility;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.AnyTransliterator;
import com.ibm.icu.text.CompoundTransliterator;
import com.ibm.icu.text.RuleBasedTransliterator;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.TransliteratorIDParser;
import com.ibm.icu.text.TransliteratorParser;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.CaseInsensitiveString;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class TransliteratorRegistry {
    private static final char LOCALE_SEP = '_';
    private static final String NO_VARIANT = "";
    private static final String ANY = "Any";
    private Map<CaseInsensitiveString, Object[]> registry = Collections.synchronizedMap(new HashMap());
    private Map<CaseInsensitiveString, Map<CaseInsensitiveString, List<CaseInsensitiveString>>> specDAG = Collections.synchronizedMap(new HashMap());
    private List<CaseInsensitiveString> availableIDs = new ArrayList<CaseInsensitiveString>();
    private static final boolean DEBUG = false;

    public Transliterator get(String ID2, StringBuffer aliasReturn) {
        Object[] entry = this.find(ID2);
        return entry == null ? null : this.instantiateEntry(ID2, entry, aliasReturn);
    }

    public void put(String ID2, Class<? extends Transliterator> transliteratorSubclass, boolean visible) {
        this.registerEntry(ID2, transliteratorSubclass, visible);
    }

    public void put(String ID2, Transliterator.Factory factory, boolean visible) {
        this.registerEntry(ID2, factory, visible);
    }

    public void put(String ID2, String resourceName, int dir, boolean visible) {
        this.registerEntry(ID2, new ResourceEntry(resourceName, dir), visible);
    }

    public void put(String ID2, String alias, boolean visible) {
        this.registerEntry(ID2, new AliasEntry(alias), visible);
    }

    public void put(String ID2, Transliterator trans, boolean visible) {
        this.registerEntry(ID2, trans, visible);
    }

    public void remove(String ID2) {
        String[] stv = TransliteratorIDParser.IDtoSTV(ID2);
        String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
        this.registry.remove(new CaseInsensitiveString(id));
        this.removeSTV(stv[0], stv[1], stv[2]);
        this.availableIDs.remove(new CaseInsensitiveString(id));
    }

    public Enumeration<String> getAvailableIDs() {
        return new IDEnumeration(Collections.enumeration(this.availableIDs));
    }

    public Enumeration<String> getAvailableSources() {
        return new IDEnumeration(Collections.enumeration(this.specDAG.keySet()));
    }

    public Enumeration<String> getAvailableTargets(String source) {
        CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
        Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
        if (targets == null) {
            return new IDEnumeration(null);
        }
        return new IDEnumeration(Collections.enumeration(targets.keySet()));
    }

    public Enumeration<String> getAvailableVariants(String source, String target) {
        CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
        CaseInsensitiveString citrg = new CaseInsensitiveString(target);
        Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
        if (targets == null) {
            return new IDEnumeration(null);
        }
        List<CaseInsensitiveString> variants = targets.get(citrg);
        if (variants == null) {
            return new IDEnumeration(null);
        }
        return new IDEnumeration(Collections.enumeration(variants));
    }

    private void registerEntry(String source, String target, String variant, Object entry, boolean visible) {
        String s2 = source;
        if (s2.length() == 0) {
            s2 = ANY;
        }
        String ID2 = TransliteratorIDParser.STVtoID(source, target, variant);
        this.registerEntry(ID2, s2, target, variant, entry, visible);
    }

    private void registerEntry(String ID2, Object entry, boolean visible) {
        String[] stv = TransliteratorIDParser.IDtoSTV(ID2);
        String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
        this.registerEntry(id, stv[0], stv[1], stv[2], entry, visible);
    }

    private void registerEntry(String ID2, String source, String target, String variant, Object entry, boolean visible) {
        CaseInsensitiveString ciID = new CaseInsensitiveString(ID2);
        Object[] arrayOfObj = entry instanceof Object[] ? (Object[])entry : new Object[]{entry};
        this.registry.put(ciID, arrayOfObj);
        if (visible) {
            this.registerSTV(source, target, variant);
            if (!this.availableIDs.contains(ciID)) {
                this.availableIDs.add(ciID);
            }
        } else {
            this.removeSTV(source, target, variant);
            this.availableIDs.remove(ciID);
        }
    }

    private void registerSTV(String source, String target, String variant) {
        List<CaseInsensitiveString> variants;
        CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
        CaseInsensitiveString citrg = new CaseInsensitiveString(target);
        CaseInsensitiveString civar = new CaseInsensitiveString(variant);
        Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
        if (targets == null) {
            targets = Collections.synchronizedMap(new HashMap());
            this.specDAG.put(cisrc, targets);
        }
        if ((variants = targets.get(citrg)) == null) {
            variants = new ArrayList<CaseInsensitiveString>();
            targets.put(citrg, variants);
        }
        if (!variants.contains(civar)) {
            if (variant.length() > 0) {
                variants.add(civar);
            } else {
                variants.add(0, civar);
            }
        }
    }

    private void removeSTV(String source, String target, String variant) {
        CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
        CaseInsensitiveString citrg = new CaseInsensitiveString(target);
        CaseInsensitiveString civar = new CaseInsensitiveString(variant);
        Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
        if (targets == null) {
            return;
        }
        List<CaseInsensitiveString> variants = targets.get(citrg);
        if (variants == null) {
            return;
        }
        variants.remove(civar);
        if (variants.size() == 0) {
            targets.remove(citrg);
            if (targets.size() == 0) {
                this.specDAG.remove(cisrc);
            }
        }
    }

    private Object[] findInDynamicStore(Spec src, Spec trg, String variant) {
        String ID2 = TransliteratorIDParser.STVtoID(src.get(), trg.get(), variant);
        return this.registry.get(new CaseInsensitiveString(ID2));
    }

    private Object[] findInStaticStore(Spec src, Spec trg, String variant) {
        Object[] entry = null;
        if (src.isLocale()) {
            entry = this.findInBundle(src, trg, variant, 0);
        } else if (trg.isLocale()) {
            entry = this.findInBundle(trg, src, variant, 1);
        }
        if (entry != null) {
            this.registerEntry(src.getTop(), trg.getTop(), variant, entry, false);
        }
        return entry;
    }

    private Object[] findInBundle(Spec specToOpen, Spec specToFind, String variant, int direction) {
        ResourceBundle res = specToOpen.getBundle();
        if (res == null) {
            return null;
        }
        for (int pass = 0; pass < 2; ++pass) {
            StringBuilder tag = new StringBuilder();
            if (pass == 0) {
                tag.append(direction == 0 ? "TransliterateTo" : "TransliterateFrom");
            } else {
                tag.append("Transliterate");
            }
            tag.append(specToFind.get().toUpperCase(Locale.ENGLISH));
            try {
                String[] subres = res.getStringArray(tag.toString());
                int i2 = 0;
                if (variant.length() != 0) {
                    for (i2 = 0; i2 < subres.length && !subres[i2].equalsIgnoreCase(variant); i2 += 2) {
                    }
                }
                if (i2 >= subres.length) continue;
                int dir = pass == 0 ? 0 : direction;
                return new Object[]{new LocaleEntry(subres[i2 + 1], dir)};
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
        }
        return null;
    }

    private Object[] find(String ID2) {
        String[] stv = TransliteratorIDParser.IDtoSTV(ID2);
        return this.find(stv[0], stv[1], stv[2]);
    }

    private Object[] find(String source, String target, String variant) {
        Spec src = new Spec(source);
        Spec trg = new Spec(target);
        Object[] entry = null;
        if (variant.length() != 0) {
            entry = this.findInDynamicStore(src, trg, variant);
            if (entry != null) {
                return entry;
            }
            entry = this.findInStaticStore(src, trg, variant);
            if (entry != null) {
                return entry;
            }
        }
        while (true) {
            src.reset();
            while (true) {
                if ((entry = this.findInDynamicStore(src, trg, NO_VARIANT)) != null) {
                    return entry;
                }
                entry = this.findInStaticStore(src, trg, NO_VARIANT);
                if (entry != null) {
                    return entry;
                }
                if (!src.hasFallback()) break;
                src.next();
            }
            if (!trg.hasFallback()) break;
            trg.next();
        }
        return null;
    }

    private Transliterator instantiateEntry(String ID2, Object[] entryWrapper, StringBuffer aliasReturn) {
        while (true) {
            Transliterator temp;
            Object entry;
            if ((entry = entryWrapper[0]) instanceof RuleBasedTransliterator.Data) {
                RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)entry;
                return new RuleBasedTransliterator(ID2, data, null);
            }
            if (entry instanceof Class) {
                try {
                    return (Transliterator)((Class)entry).newInstance();
                }
                catch (InstantiationException data) {
                }
                catch (IllegalAccessException data) {
                    // empty catch block
                }
                return null;
            }
            if (entry instanceof AliasEntry) {
                aliasReturn.append(((AliasEntry)entry).alias);
                return null;
            }
            if (entry instanceof Transliterator.Factory) {
                return ((Transliterator.Factory)entry).getInstance(ID2);
            }
            if (entry instanceof CompoundRBTEntry) {
                return ((CompoundRBTEntry)entry).getInstance();
            }
            if (entry instanceof AnyTransliterator) {
                temp = (AnyTransliterator)entry;
                return ((AnyTransliterator)temp).safeClone();
            }
            if (entry instanceof RuleBasedTransliterator) {
                temp = (RuleBasedTransliterator)entry;
                return ((RuleBasedTransliterator)temp).safeClone();
            }
            if (entry instanceof CompoundTransliterator) {
                temp = (CompoundTransliterator)entry;
                return ((CompoundTransliterator)temp).safeClone();
            }
            if (entry instanceof Transliterator) {
                return (Transliterator)entry;
            }
            TransliteratorParser parser = new TransliteratorParser();
            try {
                ResourceEntry re = (ResourceEntry)entry;
                parser.parse(re.resource, re.direction);
            }
            catch (ClassCastException e2) {
                LocaleEntry le = (LocaleEntry)entry;
                parser.parse(le.rule, le.direction);
            }
            if (parser.idBlockVector.size() == 0 && parser.dataVector.size() == 0) {
                entryWrapper[0] = new AliasEntry("Any-Null");
                continue;
            }
            if (parser.idBlockVector.size() == 0 && parser.dataVector.size() == 1) {
                entryWrapper[0] = parser.dataVector.get(0);
                continue;
            }
            if (parser.idBlockVector.size() == 1 && parser.dataVector.size() == 0) {
                if (parser.compoundFilter != null) {
                    entryWrapper[0] = new AliasEntry(parser.compoundFilter.toPattern(false) + ";" + parser.idBlockVector.get(0));
                    continue;
                }
                entryWrapper[0] = new AliasEntry(parser.idBlockVector.get(0));
                continue;
            }
            entryWrapper[0] = new CompoundRBTEntry(ID2, parser.idBlockVector, parser.dataVector, parser.compoundFilter);
        }
    }

    private static class IDEnumeration
    implements Enumeration<String> {
        Enumeration<CaseInsensitiveString> en;

        public IDEnumeration(Enumeration<CaseInsensitiveString> e2) {
            this.en = e2;
        }

        @Override
        public boolean hasMoreElements() {
            return this.en != null && this.en.hasMoreElements();
        }

        @Override
        public String nextElement() {
            return this.en.nextElement().getString();
        }
    }

    static class CompoundRBTEntry {
        private String ID;
        private List<String> idBlockVector;
        private List<RuleBasedTransliterator.Data> dataVector;
        private UnicodeSet compoundFilter;

        public CompoundRBTEntry(String theID, List<String> theIDBlockVector, List<RuleBasedTransliterator.Data> theDataVector, UnicodeSet theCompoundFilter) {
            this.ID = theID;
            this.idBlockVector = theIDBlockVector;
            this.dataVector = theDataVector;
            this.compoundFilter = theCompoundFilter;
        }

        public Transliterator getInstance() {
            ArrayList<Transliterator> transliterators = new ArrayList<Transliterator>();
            int passNumber = 1;
            int limit = Math.max(this.idBlockVector.size(), this.dataVector.size());
            for (int i2 = 0; i2 < limit; ++i2) {
                String idBlock;
                if (i2 < this.idBlockVector.size() && (idBlock = this.idBlockVector.get(i2)).length() > 0) {
                    transliterators.add(Transliterator.getInstance(idBlock));
                }
                if (i2 >= this.dataVector.size()) continue;
                RuleBasedTransliterator.Data data = this.dataVector.get(i2);
                transliterators.add(new RuleBasedTransliterator("%Pass" + passNumber++, data, null));
            }
            CompoundTransliterator t2 = new CompoundTransliterator(transliterators, passNumber - 1);
            t2.setID(this.ID);
            if (this.compoundFilter != null) {
                t2.setFilter(this.compoundFilter);
            }
            return t2;
        }
    }

    static class AliasEntry {
        public String alias;

        public AliasEntry(String a2) {
            this.alias = a2;
        }
    }

    static class LocaleEntry {
        public String rule;
        public int direction;

        public LocaleEntry(String r2, int d2) {
            this.rule = r2;
            this.direction = d2;
        }
    }

    static class ResourceEntry {
        public String resource;
        public int direction;

        public ResourceEntry(String n2, int d2) {
            this.resource = n2;
            this.direction = d2;
        }
    }

    static class Spec {
        private String top;
        private String spec;
        private String nextSpec;
        private String scriptName;
        private boolean isSpecLocale;
        private boolean isNextLocale;
        private ICUResourceBundle res;

        public Spec(String theSpec) {
            this.top = theSpec;
            this.spec = null;
            this.scriptName = null;
            try {
                int script = UScript.getCodeFromName(this.top);
                int[] s2 = UScript.getCode(this.top);
                if (s2 != null) {
                    this.scriptName = UScript.getName(s2[0]);
                    if (this.scriptName.equalsIgnoreCase(this.top)) {
                        this.scriptName = null;
                    }
                }
                this.isSpecLocale = false;
                this.res = null;
                if (script == -1) {
                    Locale toploc = LocaleUtility.getLocaleFromName(this.top);
                    this.res = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/translit", toploc);
                    if (this.res != null && LocaleUtility.isFallbackOf(this.res.getULocale().toString(), this.top)) {
                        this.isSpecLocale = true;
                    }
                }
            }
            catch (MissingResourceException e2) {
                this.scriptName = null;
            }
            this.reset();
        }

        public boolean hasFallback() {
            return this.nextSpec != null;
        }

        public void reset() {
            if (!Utility.sameObjects(this.spec, this.top)) {
                this.spec = this.top;
                this.isSpecLocale = this.res != null;
                this.setupNext();
            }
        }

        private void setupNext() {
            this.isNextLocale = false;
            if (this.isSpecLocale) {
                this.nextSpec = this.spec;
                int i2 = this.nextSpec.lastIndexOf(95);
                if (i2 > 0) {
                    this.nextSpec = this.spec.substring(0, i2);
                    this.isNextLocale = true;
                } else {
                    this.nextSpec = this.scriptName;
                }
            } else {
                this.nextSpec = !Utility.sameObjects(this.nextSpec, this.scriptName) ? this.scriptName : null;
            }
        }

        public String next() {
            this.spec = this.nextSpec;
            this.isSpecLocale = this.isNextLocale;
            this.setupNext();
            return this.spec;
        }

        public String get() {
            return this.spec;
        }

        public boolean isLocale() {
            return this.isSpecLocale;
        }

        public ResourceBundle getBundle() {
            if (this.res != null && this.res.getULocale().toString().equals(this.spec)) {
                return this.res;
            }
            return null;
        }

        public String getTop() {
            return this.top;
        }
    }
}

