/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.NFRule;
import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.RBNFPostProcessor;
import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.text.RbnfLenientScannerProvider;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class RuleBasedNumberFormat
extends NumberFormat {
    static final long serialVersionUID = -7664252765575395068L;
    public static final int SPELLOUT = 1;
    public static final int ORDINAL = 2;
    public static final int DURATION = 3;
    public static final int NUMBERING_SYSTEM = 4;
    private transient NFRuleSet[] ruleSets = null;
    private transient Map<String, NFRuleSet> ruleSetsMap = null;
    private transient NFRuleSet defaultRuleSet = null;
    private ULocale locale = null;
    private int roundingMode = 7;
    private transient RbnfLenientScannerProvider scannerProvider = null;
    private transient boolean lookedForScanner;
    private transient DecimalFormatSymbols decimalFormatSymbols = null;
    private transient DecimalFormat decimalFormat = null;
    private transient NFRule defaultInfinityRule = null;
    private transient NFRule defaultNaNRule = null;
    private boolean lenientParse = false;
    private transient String lenientParseRules;
    private transient String postProcessRules;
    private transient RBNFPostProcessor postProcessor;
    private Map<String, String[]> ruleSetDisplayNames;
    private String[] publicRuleSetNames;
    private boolean capitalizationInfoIsSet = false;
    private boolean capitalizationForListOrMenu = false;
    private boolean capitalizationForStandAlone = false;
    private transient BreakIterator capitalizationBrkIter = null;
    private static final boolean DEBUG = ICUDebug.enabled("rbnf");
    private static final String[] rulenames = new String[]{"SpelloutRules", "OrdinalRules", "DurationRules", "NumberingSystemRules"};
    private static final String[] locnames = new String[]{"SpelloutLocalizations", "OrdinalLocalizations", "DurationLocalizations", "NumberingSystemLocalizations"};
    private static final com.ibm.icu.math.BigDecimal MAX_VALUE = com.ibm.icu.math.BigDecimal.valueOf(Long.MAX_VALUE);
    private static final com.ibm.icu.math.BigDecimal MIN_VALUE = com.ibm.icu.math.BigDecimal.valueOf(Long.MIN_VALUE);

    public RuleBasedNumberFormat(String description) {
        this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        this.init(description, null);
    }

    public RuleBasedNumberFormat(String description, String[][] localizations) {
        this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        this.init(description, localizations);
    }

    public RuleBasedNumberFormat(String description, Locale locale) {
        this(description, ULocale.forLocale(locale));
    }

    public RuleBasedNumberFormat(String description, ULocale locale) {
        this.locale = locale;
        this.init(description, null);
    }

    public RuleBasedNumberFormat(String description, String[][] localizations, ULocale locale) {
        this.locale = locale;
        this.init(description, localizations);
    }

    public RuleBasedNumberFormat(Locale locale, int format) {
        this(ULocale.forLocale(locale), format);
    }

    public RuleBasedNumberFormat(ULocale locale, int format) {
        ICUResourceBundle locNamesBundle;
        this.locale = locale;
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/rbnf", locale);
        ULocale uloc = bundle.getULocale();
        this.setLocale(uloc, uloc);
        StringBuilder description = new StringBuilder();
        String[][] localizations = null;
        try {
            ICUResourceBundle rules = bundle.getWithFallback("RBNFRules/" + rulenames[format - 1]);
            UResourceBundleIterator it = rules.getIterator();
            while (it.hasNext()) {
                description.append(it.nextString());
            }
        }
        catch (MissingResourceException rules) {
            // empty catch block
        }
        if ((locNamesBundle = bundle.findTopLevel(locnames[format - 1])) != null) {
            localizations = new String[locNamesBundle.getSize()][];
            for (int i2 = 0; i2 < localizations.length; ++i2) {
                localizations[i2] = locNamesBundle.get(i2).getStringArray();
            }
        }
        this.init(description.toString(), localizations);
    }

    public RuleBasedNumberFormat(int format) {
        this(ULocale.getDefault(ULocale.Category.FORMAT), format);
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof RuleBasedNumberFormat)) {
            return false;
        }
        RuleBasedNumberFormat that2 = (RuleBasedNumberFormat)that;
        if (!this.locale.equals(that2.locale) || this.lenientParse != that2.lenientParse) {
            return false;
        }
        if (this.ruleSets.length != that2.ruleSets.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.ruleSets.length; ++i2) {
            if (this.ruleSets[i2].equals(that2.ruleSets[i2])) continue;
            return false;
        }
        return true;
    }

    @Override
    @Deprecated
    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (NFRuleSet ruleSet : this.ruleSets) {
            result.append(ruleSet.toString());
        }
        return result.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(this.toString());
        out.writeObject(this.locale);
        out.writeInt(this.roundingMode);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        ULocale loc;
        String description = in.readUTF();
        try {
            loc = (ULocale)in.readObject();
        }
        catch (Exception e2) {
            loc = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        try {
            this.roundingMode = in.readInt();
        }
        catch (Exception e2) {
            // empty catch block
        }
        RuleBasedNumberFormat temp = new RuleBasedNumberFormat(description, loc);
        this.ruleSets = temp.ruleSets;
        this.ruleSetsMap = temp.ruleSetsMap;
        this.defaultRuleSet = temp.defaultRuleSet;
        this.publicRuleSetNames = temp.publicRuleSetNames;
        this.decimalFormatSymbols = temp.decimalFormatSymbols;
        this.decimalFormat = temp.decimalFormat;
        this.locale = temp.locale;
        this.defaultInfinityRule = temp.defaultInfinityRule;
        this.defaultNaNRule = temp.defaultNaNRule;
    }

    public String[] getRuleSetNames() {
        return (String[])this.publicRuleSetNames.clone();
    }

    public ULocale[] getRuleSetDisplayNameLocales() {
        if (this.ruleSetDisplayNames != null) {
            Set<String> s2 = this.ruleSetDisplayNames.keySet();
            String[] locales = s2.toArray(new String[s2.size()]);
            Arrays.sort(locales, String.CASE_INSENSITIVE_ORDER);
            ULocale[] result = new ULocale[locales.length];
            for (int i2 = 0; i2 < locales.length; ++i2) {
                result[i2] = new ULocale(locales[i2]);
            }
            return result;
        }
        return null;
    }

    private String[] getNameListForLocale(ULocale loc) {
        if (loc != null && this.ruleSetDisplayNames != null) {
            String[] localeNames;
            for (String lname : localeNames = new String[]{loc.getBaseName(), ULocale.getDefault(ULocale.Category.DISPLAY).getBaseName()}) {
                while (lname.length() > 0) {
                    String[] names = this.ruleSetDisplayNames.get(lname);
                    if (names != null) {
                        return names;
                    }
                    lname = ULocale.getFallback(lname);
                }
            }
        }
        return null;
    }

    public String[] getRuleSetDisplayNames(ULocale loc) {
        String[] names = this.getNameListForLocale(loc);
        if (names != null) {
            return (String[])names.clone();
        }
        names = this.getRuleSetNames();
        for (int i2 = 0; i2 < names.length; ++i2) {
            names[i2] = names[i2].substring(1);
        }
        return names;
    }

    public String[] getRuleSetDisplayNames() {
        return this.getRuleSetDisplayNames(ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public String getRuleSetDisplayName(String ruleSetName, ULocale loc) {
        String[] rsnames = this.publicRuleSetNames;
        for (int ix = 0; ix < rsnames.length; ++ix) {
            if (!rsnames[ix].equals(ruleSetName)) continue;
            String[] names = this.getNameListForLocale(loc);
            if (names != null) {
                return names[ix];
            }
            return rsnames[ix].substring(1);
        }
        throw new IllegalArgumentException("unrecognized rule set name: " + ruleSetName);
    }

    public String getRuleSetDisplayName(String ruleSetName) {
        return this.getRuleSetDisplayName(ruleSetName, ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public String format(double number, String ruleSet) throws IllegalArgumentException {
        if (ruleSet.startsWith("%%")) {
            throw new IllegalArgumentException("Can't use internal rule set");
        }
        return this.adjustForContext(this.format(number, this.findRuleSet(ruleSet)));
    }

    public String format(long number, String ruleSet) throws IllegalArgumentException {
        if (ruleSet.startsWith("%%")) {
            throw new IllegalArgumentException("Can't use internal rule set");
        }
        return this.adjustForContext(this.format(number, this.findRuleSet(ruleSet)));
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition ignore) {
        if (toAppendTo.length() == 0) {
            toAppendTo.append(this.adjustForContext(this.format(number, this.defaultRuleSet)));
        } else {
            toAppendTo.append(this.format(number, this.defaultRuleSet));
        }
        return toAppendTo;
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition ignore) {
        if (toAppendTo.length() == 0) {
            toAppendTo.append(this.adjustForContext(this.format(number, this.defaultRuleSet)));
        } else {
            toAppendTo.append(this.format(number, this.defaultRuleSet));
        }
        return toAppendTo;
    }

    @Override
    public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format(new com.ibm.icu.math.BigDecimal(number), toAppendTo, pos);
    }

    @Override
    public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format(new com.ibm.icu.math.BigDecimal(number), toAppendTo, pos);
    }

    @Override
    public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        if (MIN_VALUE.compareTo(number) > 0 || MAX_VALUE.compareTo(number) < 0) {
            return this.getDecimalFormat().format(number, toAppendTo, pos);
        }
        if (number.scale() == 0) {
            return this.format(number.longValue(), toAppendTo, pos);
        }
        return this.format(number.doubleValue(), toAppendTo, pos);
    }

    @Override
    public Number parse(String text, ParsePosition parsePosition) {
        String workingText = text.substring(parsePosition.getIndex());
        ParsePosition workingPos = new ParsePosition(0);
        Number tempResult = null;
        Number result = NFRule.ZERO;
        ParsePosition highWaterMark = new ParsePosition(workingPos.getIndex());
        for (int i2 = this.ruleSets.length - 1; i2 >= 0; --i2) {
            if (!this.ruleSets[i2].isPublic() || !this.ruleSets[i2].isParseable()) continue;
            tempResult = this.ruleSets[i2].parse(workingText, workingPos, Double.MAX_VALUE, 0);
            if (workingPos.getIndex() > highWaterMark.getIndex()) {
                result = tempResult;
                highWaterMark.setIndex(workingPos.getIndex());
            }
            if (highWaterMark.getIndex() == workingText.length()) break;
            workingPos.setIndex(0);
        }
        parsePosition.setIndex(parsePosition.getIndex() + highWaterMark.getIndex());
        return result;
    }

    public void setLenientParseMode(boolean enabled) {
        this.lenientParse = enabled;
    }

    public boolean lenientParseEnabled() {
        return this.lenientParse;
    }

    public void setLenientScannerProvider(RbnfLenientScannerProvider scannerProvider) {
        this.scannerProvider = scannerProvider;
    }

    public RbnfLenientScannerProvider getLenientScannerProvider() {
        if (this.scannerProvider == null && this.lenientParse && !this.lookedForScanner) {
            try {
                this.lookedForScanner = true;
                Class<?> cls = Class.forName("com.ibm.icu.impl.text.RbnfScannerProviderImpl");
                RbnfLenientScannerProvider provider = (RbnfLenientScannerProvider)cls.newInstance();
                this.setLenientScannerProvider(provider);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return this.scannerProvider;
    }

    public void setDefaultRuleSet(String ruleSetName) {
        if (ruleSetName == null) {
            if (this.publicRuleSetNames.length > 0) {
                this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
            } else {
                this.defaultRuleSet = null;
                int n2 = this.ruleSets.length;
                while (--n2 >= 0) {
                    String currentName = this.ruleSets[n2].getName();
                    if (!currentName.equals("%spellout-numbering") && !currentName.equals("%digits-ordinal") && !currentName.equals("%duration")) continue;
                    this.defaultRuleSet = this.ruleSets[n2];
                    return;
                }
                n2 = this.ruleSets.length;
                while (--n2 >= 0) {
                    if (!this.ruleSets[n2].isPublic()) continue;
                    this.defaultRuleSet = this.ruleSets[n2];
                    break;
                }
            }
        } else {
            if (ruleSetName.startsWith("%%")) {
                throw new IllegalArgumentException("cannot use private rule set: " + ruleSetName);
            }
            this.defaultRuleSet = this.findRuleSet(ruleSetName);
        }
    }

    public String getDefaultRuleSetName() {
        if (this.defaultRuleSet != null && this.defaultRuleSet.isPublic()) {
            return this.defaultRuleSet.getName();
        }
        return "";
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        if (newSymbols != null) {
            this.decimalFormatSymbols = (DecimalFormatSymbols)newSymbols.clone();
            if (this.decimalFormat != null) {
                this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
            if (this.defaultInfinityRule != null) {
                this.defaultInfinityRule = null;
                this.getDefaultInfinityRule();
            }
            if (this.defaultNaNRule != null) {
                this.defaultNaNRule = null;
                this.getDefaultNaNRule();
            }
            for (NFRuleSet ruleSet : this.ruleSets) {
                ruleSet.setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
        }
    }

    @Override
    public void setContext(DisplayContext context) {
        super.setContext(context);
        if (!(this.capitalizationInfoIsSet || context != DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU && context != DisplayContext.CAPITALIZATION_FOR_STANDALONE)) {
            this.initCapitalizationContextInfo(this.locale);
            this.capitalizationInfoIsSet = true;
        }
        if (this.capitalizationBrkIter == null && (context == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE || context == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU && this.capitalizationForListOrMenu || context == DisplayContext.CAPITALIZATION_FOR_STANDALONE && this.capitalizationForStandAlone)) {
            this.capitalizationBrkIter = BreakIterator.getSentenceInstance(this.locale);
        }
    }

    @Override
    public int getRoundingMode() {
        return this.roundingMode;
    }

    @Override
    public void setRoundingMode(int roundingMode) {
        if (roundingMode < 0 || roundingMode > 7) {
            throw new IllegalArgumentException("Invalid rounding mode: " + roundingMode);
        }
        this.roundingMode = roundingMode;
    }

    NFRuleSet getDefaultRuleSet() {
        return this.defaultRuleSet;
    }

    RbnfLenientScanner getLenientScanner() {
        RbnfLenientScannerProvider provider;
        if (this.lenientParse && (provider = this.getLenientScannerProvider()) != null) {
            return provider.get(this.locale, this.lenientParseRules);
        }
        return null;
    }

    DecimalFormatSymbols getDecimalFormatSymbols() {
        if (this.decimalFormatSymbols == null) {
            this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale);
        }
        return this.decimalFormatSymbols;
    }

    DecimalFormat getDecimalFormat() {
        if (this.decimalFormat == null) {
            String pattern = RuleBasedNumberFormat.getPattern(this.locale, 0);
            this.decimalFormat = new DecimalFormat(pattern, this.getDecimalFormatSymbols());
        }
        return this.decimalFormat;
    }

    PluralFormat createPluralFormat(PluralRules.PluralType pluralType, String pattern) {
        return new PluralFormat(this.locale, pluralType, pattern, this.getDecimalFormat());
    }

    NFRule getDefaultInfinityRule() {
        if (this.defaultInfinityRule == null) {
            this.defaultInfinityRule = new NFRule(this, "Inf: " + this.getDecimalFormatSymbols().getInfinity());
        }
        return this.defaultInfinityRule;
    }

    NFRule getDefaultNaNRule() {
        if (this.defaultNaNRule == null) {
            this.defaultNaNRule = new NFRule(this, "NaN: " + this.getDecimalFormatSymbols().getNaN());
        }
        return this.defaultNaNRule;
    }

    private String extractSpecial(StringBuilder description, String specialName) {
        String result = null;
        int lp = description.indexOf(specialName);
        if (lp != -1 && (lp == 0 || description.charAt(lp - 1) == ';')) {
            int lpStart;
            int lpEnd = description.indexOf(";%", lp);
            if (lpEnd == -1) {
                lpEnd = description.length() - 1;
            }
            for (lpStart = lp + specialName.length(); lpStart < lpEnd && PatternProps.isWhiteSpace(description.charAt(lpStart)); ++lpStart) {
            }
            result = description.substring(lpStart, lpEnd);
            description.delete(lp, lpEnd + 1);
        }
        return result;
    }

    private void init(String description, String[][] localizations) {
        int i2;
        this.initLocalizations(localizations);
        StringBuilder descBuf = this.stripWhitespace(description);
        this.lenientParseRules = this.extractSpecial(descBuf, "%%lenient-parse:");
        this.postProcessRules = this.extractSpecial(descBuf, "%%post-process:");
        int numRuleSets = 1;
        int p2 = 0;
        while ((p2 = descBuf.indexOf(";%", p2)) != -1) {
            ++numRuleSets;
            p2 += 2;
        }
        this.ruleSets = new NFRuleSet[numRuleSets];
        this.ruleSetsMap = new HashMap<String, NFRuleSet>(numRuleSets * 2 + 1);
        this.defaultRuleSet = null;
        int publicRuleSetCount = 0;
        String[] ruleSetDescriptions = new String[numRuleSets];
        int start = 0;
        for (int curRuleSet = 0; curRuleSet < this.ruleSets.length; ++curRuleSet) {
            NFRuleSet ruleSet;
            p2 = descBuf.indexOf(";%", start);
            if (p2 < 0) {
                p2 = descBuf.length() - 1;
            }
            ruleSetDescriptions[curRuleSet] = descBuf.substring(start, p2 + 1);
            this.ruleSets[curRuleSet] = ruleSet = new NFRuleSet(this, ruleSetDescriptions, curRuleSet);
            String currentName = ruleSet.getName();
            this.ruleSetsMap.put(currentName, ruleSet);
            if (!currentName.startsWith("%%")) {
                ++publicRuleSetCount;
                if (this.defaultRuleSet == null && currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal") || currentName.equals("%duration")) {
                    this.defaultRuleSet = ruleSet;
                }
            }
            start = p2 + 1;
        }
        if (this.defaultRuleSet == null) {
            for (int i3 = this.ruleSets.length - 1; i3 >= 0; --i3) {
                if (this.ruleSets[i3].getName().startsWith("%%")) continue;
                this.defaultRuleSet = this.ruleSets[i3];
                break;
            }
        }
        if (this.defaultRuleSet == null) {
            this.defaultRuleSet = this.ruleSets[this.ruleSets.length - 1];
        }
        for (int i4 = 0; i4 < this.ruleSets.length; ++i4) {
            this.ruleSets[i4].parseRules(ruleSetDescriptions[i4]);
        }
        String[] publicRuleSetTemp = new String[publicRuleSetCount];
        publicRuleSetCount = 0;
        for (i2 = this.ruleSets.length - 1; i2 >= 0; --i2) {
            if (this.ruleSets[i2].getName().startsWith("%%")) continue;
            publicRuleSetTemp[publicRuleSetCount++] = this.ruleSets[i2].getName();
        }
        if (this.publicRuleSetNames != null) {
            block5: for (i2 = 0; i2 < this.publicRuleSetNames.length; ++i2) {
                String name = this.publicRuleSetNames[i2];
                for (int j2 = 0; j2 < publicRuleSetTemp.length; ++j2) {
                    if (name.equals(publicRuleSetTemp[j2])) continue block5;
                }
                throw new IllegalArgumentException("did not find public rule set: " + name);
            }
            this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
        } else {
            this.publicRuleSetNames = publicRuleSetTemp;
        }
    }

    private void initLocalizations(String[][] localizations) {
        if (localizations != null) {
            this.publicRuleSetNames = (String[])localizations[0].clone();
            HashMap<String, String[]> m2 = new HashMap<String, String[]>();
            for (int i2 = 1; i2 < localizations.length; ++i2) {
                String[] data = localizations[i2];
                String loc = data[0];
                String[] names = new String[data.length - 1];
                if (names.length != this.publicRuleSetNames.length) {
                    throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + i2 + "] length: " + names.length);
                }
                System.arraycopy(data, 1, names, 0, names.length);
                m2.put(loc, names);
            }
            if (!m2.isEmpty()) {
                this.ruleSetDisplayNames = m2;
            }
        }
    }

    private void initCapitalizationContextInfo(ULocale theLocale) {
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", theLocale);
        try {
            ICUResourceBundle rdb = rb.getWithFallback("contextTransforms/number-spellout");
            int[] intVector = rdb.getIntVector();
            if (intVector.length >= 2) {
                this.capitalizationForListOrMenu = intVector[0] != 0;
                this.capitalizationForStandAlone = intVector[1] != 0;
            }
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
    }

    private StringBuilder stripWhitespace(String description) {
        StringBuilder result = new StringBuilder();
        int descriptionLength = description.length();
        int start = 0;
        while (start < descriptionLength) {
            while (start < descriptionLength && PatternProps.isWhiteSpace(description.charAt(start))) {
                ++start;
            }
            if (start < descriptionLength && description.charAt(start) == ';') {
                ++start;
                continue;
            }
            int p2 = description.indexOf(59, start);
            if (p2 == -1) {
                result.append(description.substring(start));
                break;
            }
            if (p2 >= descriptionLength) break;
            result.append(description.substring(start, p2 + 1));
            start = p2 + 1;
        }
        return result;
    }

    private String format(double number, NFRuleSet ruleSet) {
        StringBuilder result = new StringBuilder();
        if (this.getRoundingMode() != 7 && !Double.isNaN(number) && !Double.isInfinite(number)) {
            number = new com.ibm.icu.math.BigDecimal(Double.toString(number)).setScale(this.getMaximumFractionDigits(), this.roundingMode).doubleValue();
        }
        ruleSet.format(number, result, 0, 0);
        this.postProcess(result, ruleSet);
        return result.toString();
    }

    private String format(long number, NFRuleSet ruleSet) {
        StringBuilder result = new StringBuilder();
        if (number == Long.MIN_VALUE) {
            result.append(this.getDecimalFormat().format(Long.MIN_VALUE));
        } else {
            ruleSet.format(number, result, 0, 0);
        }
        this.postProcess(result, ruleSet);
        return result.toString();
    }

    private void postProcess(StringBuilder result, NFRuleSet ruleSet) {
        if (this.postProcessRules != null) {
            if (this.postProcessor == null) {
                int ix = this.postProcessRules.indexOf(";");
                if (ix == -1) {
                    ix = this.postProcessRules.length();
                }
                String ppClassName = this.postProcessRules.substring(0, ix).trim();
                try {
                    Class<?> cls = Class.forName(ppClassName);
                    this.postProcessor = (RBNFPostProcessor)cls.newInstance();
                    this.postProcessor.init(this, this.postProcessRules);
                }
                catch (Exception e2) {
                    if (DEBUG) {
                        System.out.println("could not locate " + ppClassName + ", error " + e2.getClass().getName() + ", " + e2.getMessage());
                    }
                    this.postProcessor = null;
                    this.postProcessRules = null;
                    return;
                }
            }
            this.postProcessor.process(result, ruleSet);
        }
    }

    private String adjustForContext(String result) {
        DisplayContext capitalization = this.getContext(DisplayContext.Type.CAPITALIZATION);
        if (capitalization != DisplayContext.CAPITALIZATION_NONE && result != null && result.length() > 0 && UCharacter.isLowerCase(result.codePointAt(0)) && (capitalization == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE || capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU && this.capitalizationForListOrMenu || capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE && this.capitalizationForStandAlone)) {
            if (this.capitalizationBrkIter == null) {
                this.capitalizationBrkIter = BreakIterator.getSentenceInstance(this.locale);
            }
            return UCharacter.toTitleCase(this.locale, result, this.capitalizationBrkIter, 768);
        }
        return result;
    }

    NFRuleSet findRuleSet(String name) throws IllegalArgumentException {
        NFRuleSet result = this.ruleSetsMap.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No rule set named " + name);
        }
        return result;
    }
}

