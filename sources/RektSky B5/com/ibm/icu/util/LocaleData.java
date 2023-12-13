/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.util.MissingResourceException;

public final class LocaleData {
    private static final String MEASUREMENT_SYSTEM = "MeasurementSystem";
    private static final String PAPER_SIZE = "PaperSize";
    private static final String LOCALE_DISPLAY_PATTERN = "localeDisplayPattern";
    private static final String PATTERN = "pattern";
    private static final String SEPARATOR = "separator";
    private boolean noSubstitute;
    private ICUResourceBundle bundle;
    private ICUResourceBundle langBundle;
    public static final int ES_STANDARD = 0;
    public static final int ES_AUXILIARY = 1;
    public static final int ES_INDEX = 2;
    @Deprecated
    public static final int ES_CURRENCY = 3;
    public static final int ES_PUNCTUATION = 4;
    @Deprecated
    public static final int ES_COUNT = 5;
    public static final int QUOTATION_START = 0;
    public static final int QUOTATION_END = 1;
    public static final int ALT_QUOTATION_START = 2;
    public static final int ALT_QUOTATION_END = 3;
    @Deprecated
    public static final int DELIMITER_COUNT = 4;
    private static final String[] DELIMITER_TYPES = new String[]{"quotationStart", "quotationEnd", "alternateQuotationStart", "alternateQuotationEnd"};
    private static VersionInfo gCLDRVersion = null;

    private LocaleData() {
    }

    public static UnicodeSet getExemplarSet(ULocale locale, int options) {
        return LocaleData.getInstance(locale).getExemplarSet(options, 0);
    }

    public static UnicodeSet getExemplarSet(ULocale locale, int options, int extype) {
        return LocaleData.getInstance(locale).getExemplarSet(options, extype);
    }

    public UnicodeSet getExemplarSet(int options, int extype) {
        String[] exemplarSetTypes = new String[]{"ExemplarCharacters", "AuxExemplarCharacters", "ExemplarCharactersIndex", "ExemplarCharactersCurrency", "ExemplarCharactersPunctuation"};
        if (extype == 3) {
            return this.noSubstitute ? null : UnicodeSet.EMPTY;
        }
        try {
            String aKey = exemplarSetTypes[extype];
            ICUResourceBundle stringBundle = (ICUResourceBundle)this.bundle.get(aKey);
            if (this.noSubstitute && !this.bundle.isRoot() && stringBundle.isRoot()) {
                return null;
            }
            String unicodeSetPattern = stringBundle.getString();
            return new UnicodeSet(unicodeSetPattern, 1 | options);
        }
        catch (ArrayIndexOutOfBoundsException aiooe) {
            throw new IllegalArgumentException(aiooe);
        }
        catch (Exception ex) {
            return this.noSubstitute ? null : UnicodeSet.EMPTY;
        }
    }

    public static final LocaleData getInstance(ULocale locale) {
        LocaleData ld = new LocaleData();
        ld.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
        ld.langBundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/lang", locale);
        ld.noSubstitute = false;
        return ld;
    }

    public static final LocaleData getInstance() {
        return LocaleData.getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public void setNoSubstitute(boolean setting) {
        this.noSubstitute = setting;
    }

    public boolean getNoSubstitute() {
        return this.noSubstitute;
    }

    public String getDelimiter(int type) {
        ICUResourceBundle delimitersBundle = (ICUResourceBundle)this.bundle.get("delimiters");
        ICUResourceBundle stringBundle = delimitersBundle.getWithFallback(DELIMITER_TYPES[type]);
        if (this.noSubstitute && !this.bundle.isRoot() && stringBundle.isRoot()) {
            return null;
        }
        return stringBundle.getString();
    }

    private static UResourceBundle measurementTypeBundleForLocale(ULocale locale, String measurementType) {
        UResourceBundle measTypeBundle = null;
        String region = ULocale.getRegionForSupplementalData(locale, true);
        try {
            UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle measurementData = rb.get("measurementData");
            UResourceBundle measDataBundle = null;
            try {
                measDataBundle = measurementData.get(region);
                measTypeBundle = measDataBundle.get(measurementType);
            }
            catch (MissingResourceException mre) {
                measDataBundle = measurementData.get("001");
                measTypeBundle = measDataBundle.get(measurementType);
            }
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        return measTypeBundle;
    }

    public static final MeasurementSystem getMeasurementSystem(ULocale locale) {
        UResourceBundle sysBundle = LocaleData.measurementTypeBundleForLocale(locale, MEASUREMENT_SYSTEM);
        switch (sysBundle.getInt()) {
            case 0: {
                return MeasurementSystem.SI;
            }
            case 1: {
                return MeasurementSystem.US;
            }
            case 2: {
                return MeasurementSystem.UK;
            }
        }
        return null;
    }

    public static final PaperSize getPaperSize(ULocale locale) {
        UResourceBundle obj = LocaleData.measurementTypeBundleForLocale(locale, PAPER_SIZE);
        int[] size = obj.getIntVector();
        return new PaperSize(size[0], size[1]);
    }

    public String getLocaleDisplayPattern() {
        ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get(LOCALE_DISPLAY_PATTERN);
        String localeDisplayPattern = locDispBundle.getStringWithFallback(PATTERN);
        return localeDisplayPattern;
    }

    public String getLocaleSeparator() {
        String sub0 = "{0}";
        String sub1 = "{1}";
        ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get(LOCALE_DISPLAY_PATTERN);
        String localeSeparator = locDispBundle.getStringWithFallback(SEPARATOR);
        int index0 = localeSeparator.indexOf(sub0);
        int index1 = localeSeparator.indexOf(sub1);
        if (index0 >= 0 && index1 >= 0 && index0 <= index1) {
            return localeSeparator.substring(index0 + sub0.length(), index1);
        }
        return localeSeparator;
    }

    public static VersionInfo getCLDRVersion() {
        if (gCLDRVersion == null) {
            UResourceBundle supplementalDataBundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle cldrVersionBundle = supplementalDataBundle.get("cldrVersion");
            gCLDRVersion = VersionInfo.getInstance(cldrVersionBundle.getString());
        }
        return gCLDRVersion;
    }

    public static final class PaperSize {
        private int height;
        private int width;

        private PaperSize(int h2, int w2) {
            this.height = h2;
            this.width = w2;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }
    }

    public static final class MeasurementSystem {
        public static final MeasurementSystem SI = new MeasurementSystem();
        public static final MeasurementSystem US = new MeasurementSystem();
        public static final MeasurementSystem UK = new MeasurementSystem();

        private MeasurementSystem() {
        }
    }
}

