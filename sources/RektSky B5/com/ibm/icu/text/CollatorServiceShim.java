/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import com.ibm.icu.impl.coll.CollationLoader;
import com.ibm.icu.impl.coll.CollationTailoring;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

final class CollatorServiceShim
extends Collator.ServiceShim {
    private static ICULocaleService service = new CService();

    CollatorServiceShim() {
    }

    @Override
    Collator getInstance(ULocale locale) {
        try {
            ULocale[] actualLoc = new ULocale[1];
            Collator coll = (Collator)service.get(locale, actualLoc);
            if (coll == null) {
                throw new MissingResourceException("Could not locate Collator data", "", "");
            }
            return (Collator)coll.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    @Override
    Object registerInstance(Collator collator, ULocale locale) {
        collator.setLocale(locale, locale);
        return service.registerObject((Object)collator, locale);
    }

    @Override
    Object registerFactory(Collator.CollatorFactory f2) {
        class CFactory
        extends ICULocaleService.LocaleKeyFactory {
            Collator.CollatorFactory delegate;

            CFactory(Collator.CollatorFactory fctry) {
                super(fctry.visible());
                this.delegate = fctry;
            }

            @Override
            public Object handleCreate(ULocale loc, int kind, ICUService srvc) {
                Collator coll = this.delegate.createCollator(loc);
                return coll;
            }

            @Override
            public String getDisplayName(String id, ULocale displayLocale) {
                ULocale objectLocale = new ULocale(id);
                return this.delegate.getDisplayName(objectLocale, displayLocale);
            }

            @Override
            public Set<String> getSupportedIDs() {
                return this.delegate.getSupportedLocaleIDs();
            }
        }
        return service.registerFactory(new CFactory(f2));
    }

    @Override
    boolean unregister(Object registryKey) {
        return service.unregisterFactory((ICUService.Factory)registryKey);
    }

    @Override
    Locale[] getAvailableLocales() {
        Locale[] result = service.isDefault() ? ICUResourceBundle.getAvailableLocales("com/ibm/icu/impl/data/icudt62b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER) : service.getAvailableLocales();
        return result;
    }

    @Override
    ULocale[] getAvailableULocales() {
        ULocale[] result = service.isDefault() ? ICUResourceBundle.getAvailableULocales("com/ibm/icu/impl/data/icudt62b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER) : service.getAvailableULocales();
        return result;
    }

    @Override
    String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
        String id = objectLocale.getName();
        return service.getDisplayName(id, displayLocale);
    }

    private static final Collator makeInstance(ULocale desiredLocale) {
        Output<ULocale> validLocale = new Output<ULocale>(ULocale.ROOT);
        CollationTailoring t2 = CollationLoader.loadTailoring(desiredLocale, validLocale);
        return new RuleBasedCollator(t2, (ULocale)validLocale.value);
    }

    private static class CService
    extends ICULocaleService {
        CService() {
            super("Collator");
            class CollatorFactory
            extends ICULocaleService.ICUResourceBundleFactory {
                CollatorFactory() {
                    super("com/ibm/icu/impl/data/icudt62b/coll");
                }

                @Override
                protected Object handleCreate(ULocale uloc, int kind, ICUService srvc) {
                    return CollatorServiceShim.makeInstance(uloc);
                }
            }
            this.registerFactory(new CollatorFactory());
            this.markDefault();
        }

        @Override
        public String validateFallbackLocale() {
            return "";
        }

        @Override
        protected Object handleDefault(ICUService.Key key, String[] actualIDReturn) {
            if (actualIDReturn != null) {
                actualIDReturn[0] = "root";
            }
            try {
                return CollatorServiceShim.makeInstance(ULocale.ROOT);
            }
            catch (MissingResourceException e2) {
                return null;
            }
        }
    }
}

