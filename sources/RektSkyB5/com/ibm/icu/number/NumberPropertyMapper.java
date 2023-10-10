/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.CurrencyPluralInfoAffixProvider;
import com.ibm.icu.impl.number.CustomSymbolCurrency;
import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.impl.number.Grouper;
import com.ibm.icu.impl.number.MacroProps;
import com.ibm.icu.impl.number.Padder;
import com.ibm.icu.impl.number.PatternStringParser;
import com.ibm.icu.impl.number.PropertiesAffixPatternProvider;
import com.ibm.icu.impl.number.RoundingUtils;
import com.ibm.icu.number.CompactNotation;
import com.ibm.icu.number.CurrencyPrecision;
import com.ibm.icu.number.FractionPrecision;
import com.ibm.icu.number.IntegerWidth;
import com.ibm.icu.number.Notation;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.number.Precision;
import com.ibm.icu.number.ScientificNotation;
import com.ibm.icu.number.UnlocalizedNumberFormatter;
import com.ibm.icu.text.CompactDecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;
import java.math.BigDecimal;
import java.math.MathContext;

final class NumberPropertyMapper {
    NumberPropertyMapper() {
    }

    public static UnlocalizedNumberFormatter create(DecimalFormatProperties properties, DecimalFormatSymbols symbols) {
        MacroProps macros = NumberPropertyMapper.oldToNew(properties, symbols, null);
        return (UnlocalizedNumberFormatter)NumberFormatter.with().macros(macros);
    }

    public static UnlocalizedNumberFormatter create(DecimalFormatProperties properties, DecimalFormatSymbols symbols, DecimalFormatProperties exportedProperties) {
        MacroProps macros = NumberPropertyMapper.oldToNew(properties, symbols, exportedProperties);
        return (UnlocalizedNumberFormatter)NumberFormatter.with().macros(macros);
    }

    public static UnlocalizedNumberFormatter create(String pattern, DecimalFormatSymbols symbols) {
        DecimalFormatProperties properties = PatternStringParser.parseToProperties(pattern);
        return NumberPropertyMapper.create(properties, symbols);
    }

    public static MacroProps oldToNew(DecimalFormatProperties properties, DecimalFormatSymbols symbols, DecimalFormatProperties exportedProperties) {
        boolean explicitMinMaxSig;
        boolean explicitCurrencyUsage;
        MacroProps macros = new MacroProps();
        ULocale locale = symbols.getULocale();
        macros.symbols = symbols;
        PluralRules rules = properties.getPluralRules();
        if (rules == null && properties.getCurrencyPluralInfo() != null) {
            rules = properties.getCurrencyPluralInfo().getPluralRules();
        }
        macros.rules = rules;
        AffixPatternProvider affixProvider = properties.getCurrencyPluralInfo() == null ? new PropertiesAffixPatternProvider(properties) : new CurrencyPluralInfoAffixProvider(properties.getCurrencyPluralInfo(), properties);
        macros.affixProvider = affixProvider;
        boolean useCurrency = properties.getCurrency() != null || properties.getCurrencyPluralInfo() != null || properties.getCurrencyUsage() != null || affixProvider.hasCurrencySign();
        Currency currency = CustomSymbolCurrency.resolve(properties.getCurrency(), locale, symbols);
        Currency.CurrencyUsage currencyUsage = properties.getCurrencyUsage();
        boolean bl = explicitCurrencyUsage = currencyUsage != null;
        if (!explicitCurrencyUsage) {
            currencyUsage = Currency.CurrencyUsage.STANDARD;
        }
        if (useCurrency) {
            macros.unit = currency;
        }
        int maxInt = properties.getMaximumIntegerDigits();
        int minInt = properties.getMinimumIntegerDigits();
        int maxFrac = properties.getMaximumFractionDigits();
        int minFrac = properties.getMinimumFractionDigits();
        int minSig = properties.getMinimumSignificantDigits();
        int maxSig = properties.getMaximumSignificantDigits();
        BigDecimal roundingIncrement = properties.getRoundingIncrement();
        MathContext mathContext = RoundingUtils.getMathContextOrUnlimited(properties);
        boolean explicitMinMaxFrac = minFrac != -1 || maxFrac != -1;
        boolean bl2 = explicitMinMaxSig = minSig != -1 || maxSig != -1;
        if (useCurrency) {
            if (minFrac == -1 && maxFrac == -1) {
                minFrac = currency.getDefaultFractionDigits(currencyUsage);
                maxFrac = currency.getDefaultFractionDigits(currencyUsage);
            } else if (minFrac == -1) {
                minFrac = Math.min(maxFrac, currency.getDefaultFractionDigits(currencyUsage));
            } else if (maxFrac == -1) {
                maxFrac = Math.max(minFrac, currency.getDefaultFractionDigits(currencyUsage));
            }
        }
        if (minInt == 0 && maxFrac != 0) {
            int n2 = minFrac = minFrac <= 0 ? 1 : minFrac;
            maxFrac = maxFrac < 0 ? -1 : (maxFrac < minFrac ? minFrac : maxFrac);
            minInt = 0;
            maxInt = maxInt < 0 ? -1 : (maxInt > 999 ? -1 : maxInt);
        } else {
            int n3 = minFrac = minFrac < 0 ? 0 : minFrac;
            int n4 = maxFrac < 0 ? -1 : (maxFrac = maxFrac < minFrac ? minFrac : maxFrac);
            int n5 = minInt <= 0 ? 1 : (minInt = minInt > 999 ? 1 : minInt);
            maxInt = maxInt < 0 ? -1 : (maxInt < minInt ? minInt : (maxInt > 999 ? -1 : maxInt));
        }
        Precision rounding = null;
        if (explicitCurrencyUsage) {
            rounding = Precision.constructCurrency(currencyUsage).withCurrency(currency);
        } else if (roundingIncrement != null) {
            rounding = Precision.constructIncrement(roundingIncrement);
        } else if (explicitMinMaxSig) {
            int n6 = minSig < 1 ? 1 : (minSig = minSig > 999 ? 999 : minSig);
            maxSig = maxSig < 0 ? 999 : (maxSig < minSig ? minSig : (maxSig > 999 ? 999 : maxSig));
            rounding = Precision.constructSignificant(minSig, maxSig);
        } else if (explicitMinMaxFrac) {
            rounding = Precision.constructFraction(minFrac, maxFrac);
        } else if (useCurrency) {
            rounding = Precision.constructCurrency(currencyUsage);
        }
        if (rounding != null) {
            macros.precision = rounding = rounding.withMode(mathContext);
        }
        macros.integerWidth = IntegerWidth.zeroFillTo(minInt).truncateAt(maxInt);
        macros.grouping = Grouper.forProperties(properties);
        if (properties.getFormatWidth() != -1) {
            macros.padder = Padder.forProperties(properties);
        }
        macros.decimal = properties.getDecimalSeparatorAlwaysShown() ? NumberFormatter.DecimalSeparatorDisplay.ALWAYS : NumberFormatter.DecimalSeparatorDisplay.AUTO;
        NumberFormatter.SignDisplay signDisplay = macros.sign = properties.getSignAlwaysShown() ? NumberFormatter.SignDisplay.ALWAYS : NumberFormatter.SignDisplay.AUTO;
        if (properties.getMinimumExponentDigits() != -1) {
            if (maxInt > 8) {
                maxInt = minInt;
                macros.integerWidth = IntegerWidth.zeroFillTo(minInt).truncateAt(maxInt);
            } else if (maxInt > minInt && minInt > 1) {
                minInt = 1;
                macros.integerWidth = IntegerWidth.zeroFillTo(minInt).truncateAt(maxInt);
            }
            int engineering = maxInt < 0 ? -1 : maxInt;
            macros.notation = new ScientificNotation(engineering, engineering == minInt, properties.getMinimumExponentDigits(), properties.getExponentSignAlwaysShown() ? NumberFormatter.SignDisplay.ALWAYS : NumberFormatter.SignDisplay.AUTO);
            if (macros.precision instanceof FractionPrecision) {
                int minInt_ = properties.getMinimumIntegerDigits();
                int minFrac_ = properties.getMinimumFractionDigits();
                int maxFrac_ = properties.getMaximumFractionDigits();
                macros.precision = minInt_ == 0 && maxFrac_ == 0 ? Precision.constructInfinite().withMode(mathContext) : (minInt_ == 0 && minFrac_ == 0 ? Precision.constructSignificant(1, maxFrac_ + 1).withMode(mathContext) : Precision.constructSignificant(minInt_ + minFrac_, minInt_ + maxFrac_).withMode(mathContext));
            }
        }
        if (properties.getCompactStyle() != null) {
            macros.notation = properties.getCompactCustomData() != null ? new CompactNotation(properties.getCompactCustomData()) : (properties.getCompactStyle() == CompactDecimalFormat.CompactStyle.LONG ? Notation.compactLong() : Notation.compactShort());
            macros.affixProvider = null;
        }
        macros.scale = RoundingUtils.scaleFromProperties(properties);
        if (exportedProperties != null) {
            exportedProperties.setCurrency(currency);
            exportedProperties.setMathContext(mathContext);
            exportedProperties.setRoundingMode(mathContext.getRoundingMode());
            exportedProperties.setMinimumIntegerDigits(minInt);
            exportedProperties.setMaximumIntegerDigits(maxInt == -1 ? Integer.MAX_VALUE : maxInt);
            Precision rounding_ = rounding instanceof CurrencyPrecision ? ((CurrencyPrecision)rounding).withCurrency(currency) : rounding;
            int minFrac_ = minFrac;
            int maxFrac_ = maxFrac;
            int minSig_ = minSig;
            int maxSig_ = maxSig;
            BigDecimal increment_ = null;
            if (rounding_ instanceof Precision.FractionRounderImpl) {
                minFrac_ = ((Precision.FractionRounderImpl)rounding_).minFrac;
                maxFrac_ = ((Precision.FractionRounderImpl)rounding_).maxFrac;
            } else if (rounding_ instanceof Precision.IncrementRounderImpl) {
                increment_ = ((Precision.IncrementRounderImpl)rounding_).increment;
                minFrac_ = increment_.scale();
                maxFrac_ = increment_.scale();
            } else if (rounding_ instanceof Precision.SignificantRounderImpl) {
                minSig_ = ((Precision.SignificantRounderImpl)rounding_).minSig;
                maxSig_ = ((Precision.SignificantRounderImpl)rounding_).maxSig;
            }
            exportedProperties.setMinimumFractionDigits(minFrac_);
            exportedProperties.setMaximumFractionDigits(maxFrac_);
            exportedProperties.setMinimumSignificantDigits(minSig_);
            exportedProperties.setMaximumSignificantDigits(maxSig_);
            exportedProperties.setRoundingIncrement(increment_);
        }
        return macros;
    }
}

