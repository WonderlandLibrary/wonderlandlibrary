/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class CurrencyPluralInfo
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 1L;
    private static final char[] tripleCurrencySign = new char[]{'\u00a4', '\u00a4', '\u00a4'};
    private static final String tripleCurrencyStr = new String(tripleCurrencySign);
    private static final char[] defaultCurrencyPluralPatternChar = new char[]{'\u0000', '.', '#', '#', ' ', '\u00a4', '\u00a4', '\u00a4'};
    private static final String defaultCurrencyPluralPattern = new String(defaultCurrencyPluralPatternChar);
    private Map<String, String> pluralCountToCurrencyUnitPattern = null;
    private PluralRules pluralRules = null;
    private ULocale ulocale = null;

    public CurrencyPluralInfo() {
        this.initialize(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public CurrencyPluralInfo(Locale locale) {
        this.initialize(ULocale.forLocale(locale));
    }

    public CurrencyPluralInfo(ULocale locale) {
        this.initialize(locale);
    }

    public static CurrencyPluralInfo getInstance() {
        return new CurrencyPluralInfo();
    }

    public static CurrencyPluralInfo getInstance(Locale locale) {
        return new CurrencyPluralInfo(locale);
    }

    public static CurrencyPluralInfo getInstance(ULocale locale) {
        return new CurrencyPluralInfo(locale);
    }

    public PluralRules getPluralRules() {
        return this.pluralRules;
    }

    public String getCurrencyPluralPattern(String pluralCount) {
        String currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
        if (currencyPluralPattern == null) {
            if (!pluralCount.equals("other")) {
                currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get("other");
            }
            if (currencyPluralPattern == null) {
                currencyPluralPattern = defaultCurrencyPluralPattern;
            }
        }
        return currencyPluralPattern;
    }

    public ULocale getLocale() {
        return this.ulocale;
    }

    public void setPluralRules(String ruleDescription) {
        this.pluralRules = PluralRules.createRules(ruleDescription);
    }

    public void setCurrencyPluralPattern(String pluralCount, String pattern) {
        this.pluralCountToCurrencyUnitPattern.put(pluralCount, pattern);
    }

    public void setLocale(ULocale loc) {
        this.ulocale = loc;
        this.initialize(loc);
    }

    public Object clone() {
        try {
            CurrencyPluralInfo other = (CurrencyPluralInfo)super.clone();
            other.ulocale = (ULocale)this.ulocale.clone();
            other.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
            for (String pluralCount : this.pluralCountToCurrencyUnitPattern.keySet()) {
                String currencyPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
                other.pluralCountToCurrencyUnitPattern.put(pluralCount, currencyPattern);
            }
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    public boolean equals(Object a2) {
        if (a2 instanceof CurrencyPluralInfo) {
            CurrencyPluralInfo other = (CurrencyPluralInfo)a2;
            return this.pluralRules.equals(other.pluralRules) && this.pluralCountToCurrencyUnitPattern.equals(other.pluralCountToCurrencyUnitPattern);
        }
        return false;
    }

    @Deprecated
    public int hashCode() {
        return this.pluralCountToCurrencyUnitPattern.hashCode() ^ this.pluralRules.hashCode() ^ this.ulocale.hashCode();
    }

    @Deprecated
    String select(double number) {
        return this.pluralRules.select(number);
    }

    @Deprecated
    public String select(PluralRules.FixedDecimal numberInfo) {
        return this.pluralRules.select(numberInfo);
    }

    @Deprecated
    public Iterator<String> pluralPatternIterator() {
        return this.pluralCountToCurrencyUnitPattern.keySet().iterator();
    }

    private void initialize(ULocale uloc) {
        this.ulocale = uloc;
        this.pluralRules = PluralRules.forLocale(uloc);
        this.setupCurrencyPluralPattern(uloc);
    }

    private void setupCurrencyPluralPattern(ULocale uloc) {
        this.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
        String numberStylePattern = NumberFormat.getPattern(uloc, 0);
        int separatorIndex = numberStylePattern.indexOf(";");
        String negNumberPattern = null;
        if (separatorIndex != -1) {
            negNumberPattern = numberStylePattern.substring(separatorIndex + 1);
            numberStylePattern = numberStylePattern.substring(0, separatorIndex);
        }
        Map<String, String> map = CurrencyData.provider.getInstance(uloc, true).getUnitPatterns();
        for (Map.Entry<String, String> e2 : map.entrySet()) {
            String pluralCount = e2.getKey();
            String pattern = e2.getValue();
            String patternWithNumber = pattern.replace("{0}", numberStylePattern);
            String patternWithCurrencySign = patternWithNumber.replace("{1}", tripleCurrencyStr);
            if (separatorIndex != -1) {
                String negPattern = pattern;
                String negWithNumber = negPattern.replace("{0}", negNumberPattern);
                String negWithCurrSign = negWithNumber.replace("{1}", tripleCurrencyStr);
                StringBuilder posNegPatterns = new StringBuilder(patternWithCurrencySign);
                posNegPatterns.append(";");
                posNegPatterns.append(negWithCurrSign);
                patternWithCurrencySign = posNegPatterns.toString();
            }
            this.pluralCountToCurrencyUnitPattern.put(pluralCount, patternWithCurrencySign);
        }
    }
}

