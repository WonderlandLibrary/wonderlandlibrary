/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.RuleBasedBreakIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.text.StringCharacterIterator;
import java.util.Locale;
import java.util.MissingResourceException;

final class BreakIteratorFactory
extends BreakIterator.BreakIteratorServiceShim {
    static final ICULocaleService service = new BFService();
    private static final String[] KIND_NAMES = new String[]{"grapheme", "word", "line", "sentence", "title"};

    BreakIteratorFactory() {
    }

    public Object registerInstance(BreakIterator iter, ULocale locale, int kind) {
        iter.setText(new StringCharacterIterator(""));
        return service.registerObject((Object)iter, locale, kind);
    }

    public boolean unregister(Object key) {
        if (service.isDefault()) {
            return false;
        }
        return service.unregisterFactory((ICUService.Factory)key);
    }

    public Locale[] getAvailableLocales() {
        if (service == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return service.getAvailableLocales();
    }

    public ULocale[] getAvailableULocales() {
        if (service == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return service.getAvailableULocales();
    }

    public BreakIterator createBreakIterator(ULocale locale, int kind) {
        if (service.isDefault()) {
            return BreakIteratorFactory.createBreakInstance(locale, kind);
        }
        ULocale[] actualLoc = new ULocale[1];
        BreakIterator iter = (BreakIterator)service.get(locale, kind, actualLoc);
        iter.setLocale(actualLoc[0], actualLoc[0]);
        return iter;
    }

    private static BreakIterator createBreakInstance(ULocale locale, int kind) {
        RuleBasedBreakIterator iter = null;
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/brkitr", locale);
        InputStream ruleStream = null;
        try {
            String typeKey = KIND_NAMES[kind];
            String brkfname = rb.getStringWithFallback("boundaries/" + typeKey);
            String rulesFileName = "data/icudt51b/brkitr/" + brkfname;
            ruleStream = ICUData.getStream(rulesFileName);
        }
        catch (Exception e) {
            throw new MissingResourceException(e.toString(), "", "");
        }
        try {
            iter = RuleBasedBreakIterator.getInstanceFromCompiledRules(ruleStream);
        }
        catch (IOException e) {
            Assert.fail(e);
        }
        ULocale uloc = ULocale.forLocale(rb.getLocale());
        iter.setLocale(uloc, uloc);
        iter.setBreakType(kind);
        return iter;
    }

    private static class BFService
    extends ICULocaleService {
        BFService() {
            super("BreakIterator");
            class RBBreakIteratorFactory
            extends ICULocaleService.ICUResourceBundleFactory {
                RBBreakIteratorFactory() {
                }

                protected Object handleCreate(ULocale loc, int kind, ICUService srvc) {
                    return BreakIteratorFactory.createBreakInstance(loc, kind);
                }
            }
            this.registerFactory(new RBBreakIteratorFactory());
            this.markDefault();
        }
    }
}

