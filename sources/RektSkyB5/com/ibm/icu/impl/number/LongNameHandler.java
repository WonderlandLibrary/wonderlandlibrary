/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.MicroProps;
import com.ibm.icu.impl.number.MicroPropsGenerator;
import com.ibm.icu.impl.number.SimpleModifier;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.MeasureUnit;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.EnumMap;
import java.util.Map;
import java.util.MissingResourceException;

public class LongNameHandler
implements MicroPropsGenerator {
    private static final int DNAM_INDEX = StandardPlural.COUNT;
    private static final int PER_INDEX = StandardPlural.COUNT + 1;
    private static final int ARRAY_LENGTH = StandardPlural.COUNT + 2;
    private final Map<StandardPlural, SimpleModifier> modifiers;
    private final PluralRules rules;
    private final MicroPropsGenerator parent;

    private static int getIndex(String pluralKeyword) {
        if (pluralKeyword.equals("dnam")) {
            return DNAM_INDEX;
        }
        if (pluralKeyword.equals("per")) {
            return PER_INDEX;
        }
        return StandardPlural.fromString(pluralKeyword).ordinal();
    }

    private static String getWithPlural(String[] strings, StandardPlural plural) {
        String result = strings[plural.ordinal()];
        if (result == null) {
            result = strings[StandardPlural.OTHER.ordinal()];
        }
        if (result == null) {
            throw new ICUException("Could not find data in 'other' plural variant");
        }
        return result;
    }

    private static void getMeasureData(ULocale locale, MeasureUnit unit, NumberFormatter.UnitWidth width, String[] outArray) {
        PluralTableSink sink = new PluralTableSink(outArray);
        ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", locale);
        StringBuilder key = new StringBuilder();
        key.append("units");
        if (width == NumberFormatter.UnitWidth.NARROW) {
            key.append("Narrow");
        } else if (width == NumberFormatter.UnitWidth.SHORT) {
            key.append("Short");
        }
        key.append("/");
        key.append(unit.getType());
        key.append("/");
        key.append(unit.getSubtype());
        try {
            resource.getAllItemsWithFallback(key.toString(), sink);
        }
        catch (MissingResourceException e2) {
            throw new IllegalArgumentException("No data for unit " + unit + ", width " + (Object)((Object)width), e2);
        }
    }

    private static void getCurrencyLongNameData(ULocale locale, Currency currency, String[] outArray) {
        Map<String, String> data = CurrencyData.provider.getInstance(locale, true).getUnitPatterns();
        for (Map.Entry<String, String> e2 : data.entrySet()) {
            String pluralKeyword = e2.getKey();
            int index = LongNameHandler.getIndex(pluralKeyword);
            String longName = currency.getName(locale, 2, pluralKeyword, null);
            String simpleFormat = e2.getValue();
            outArray[index] = simpleFormat = simpleFormat.replace("{1}", longName);
        }
    }

    private static String getPerUnitFormat(ULocale locale, NumberFormatter.UnitWidth width) {
        ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", locale);
        StringBuilder key = new StringBuilder();
        key.append("units");
        if (width == NumberFormatter.UnitWidth.NARROW) {
            key.append("Narrow");
        } else if (width == NumberFormatter.UnitWidth.SHORT) {
            key.append("Short");
        }
        key.append("/compound/per");
        try {
            return resource.getStringWithFallback(key.toString());
        }
        catch (MissingResourceException e2) {
            throw new IllegalArgumentException("Could not find x-per-y format for " + locale + ", width " + (Object)((Object)width));
        }
    }

    private LongNameHandler(Map<StandardPlural, SimpleModifier> modifiers, PluralRules rules, MicroPropsGenerator parent) {
        this.modifiers = modifiers;
        this.rules = rules;
        this.parent = parent;
    }

    public static String getUnitDisplayName(ULocale locale, MeasureUnit unit, NumberFormatter.UnitWidth width) {
        String[] measureData = new String[ARRAY_LENGTH];
        LongNameHandler.getMeasureData(locale, unit, width, measureData);
        return measureData[DNAM_INDEX];
    }

    public static LongNameHandler forCurrencyLongNames(ULocale locale, Currency currency, PluralRules rules, MicroPropsGenerator parent) {
        String[] simpleFormats = new String[ARRAY_LENGTH];
        LongNameHandler.getCurrencyLongNameData(locale, currency, simpleFormats);
        EnumMap<StandardPlural, SimpleModifier> modifiers = new EnumMap<StandardPlural, SimpleModifier>(StandardPlural.class);
        LongNameHandler.simpleFormatsToModifiers(simpleFormats, null, modifiers);
        return new LongNameHandler(modifiers, rules, parent);
    }

    public static LongNameHandler forMeasureUnit(ULocale locale, MeasureUnit unit, MeasureUnit perUnit, NumberFormatter.UnitWidth width, PluralRules rules, MicroPropsGenerator parent) {
        if (perUnit != null) {
            MeasureUnit simplified = MeasureUnit.resolveUnitPerUnit(unit, perUnit);
            if (simplified != null) {
                unit = simplified;
            } else {
                return LongNameHandler.forCompoundUnit(locale, unit, perUnit, width, rules, parent);
            }
        }
        String[] simpleFormats = new String[ARRAY_LENGTH];
        LongNameHandler.getMeasureData(locale, unit, width, simpleFormats);
        EnumMap<StandardPlural, SimpleModifier> modifiers = new EnumMap<StandardPlural, SimpleModifier>(StandardPlural.class);
        LongNameHandler.simpleFormatsToModifiers(simpleFormats, null, modifiers);
        return new LongNameHandler(modifiers, rules, parent);
    }

    private static LongNameHandler forCompoundUnit(ULocale locale, MeasureUnit unit, MeasureUnit perUnit, NumberFormatter.UnitWidth width, PluralRules rules, MicroPropsGenerator parent) {
        String perUnitFormat;
        String[] primaryData = new String[ARRAY_LENGTH];
        LongNameHandler.getMeasureData(locale, unit, width, primaryData);
        String[] secondaryData = new String[ARRAY_LENGTH];
        LongNameHandler.getMeasureData(locale, perUnit, width, secondaryData);
        if (secondaryData[PER_INDEX] != null) {
            perUnitFormat = secondaryData[PER_INDEX];
        } else {
            String rawPerUnitFormat = LongNameHandler.getPerUnitFormat(locale, width);
            StringBuilder sb = new StringBuilder();
            String compiled = SimpleFormatterImpl.compileToStringMinMaxArguments(rawPerUnitFormat, sb, 2, 2);
            String secondaryFormat = LongNameHandler.getWithPlural(secondaryData, StandardPlural.ONE);
            String secondaryCompiled = SimpleFormatterImpl.compileToStringMinMaxArguments(secondaryFormat, sb, 1, 1);
            String secondaryString = SimpleFormatterImpl.getTextWithNoArguments(secondaryCompiled).trim();
            perUnitFormat = SimpleFormatterImpl.formatCompiledPattern(compiled, "{0}", secondaryString);
        }
        EnumMap<StandardPlural, SimpleModifier> modifiers = new EnumMap<StandardPlural, SimpleModifier>(StandardPlural.class);
        LongNameHandler.multiSimpleFormatsToModifiers(primaryData, perUnitFormat, null, modifiers);
        return new LongNameHandler(modifiers, rules, parent);
    }

    private static void simpleFormatsToModifiers(String[] simpleFormats, NumberFormat.Field field, Map<StandardPlural, SimpleModifier> output) {
        StringBuilder sb = new StringBuilder();
        for (StandardPlural plural : StandardPlural.VALUES) {
            String simpleFormat = LongNameHandler.getWithPlural(simpleFormats, plural);
            String compiled = SimpleFormatterImpl.compileToStringMinMaxArguments(simpleFormat, sb, 0, 1);
            output.put(plural, new SimpleModifier(compiled, field, false));
        }
    }

    private static void multiSimpleFormatsToModifiers(String[] leadFormats, String trailFormat, NumberFormat.Field field, Map<StandardPlural, SimpleModifier> output) {
        StringBuilder sb = new StringBuilder();
        String trailCompiled = SimpleFormatterImpl.compileToStringMinMaxArguments(trailFormat, sb, 1, 1);
        for (StandardPlural plural : StandardPlural.VALUES) {
            String leadFormat = LongNameHandler.getWithPlural(leadFormats, plural);
            String compoundFormat = SimpleFormatterImpl.formatCompiledPattern(trailCompiled, leadFormat);
            String compoundCompiled = SimpleFormatterImpl.compileToStringMinMaxArguments(compoundFormat, sb, 0, 1);
            output.put(plural, new SimpleModifier(compoundCompiled, field, false));
        }
    }

    @Override
    public MicroProps processQuantity(DecimalQuantity quantity) {
        MicroProps micros = this.parent.processQuantity(quantity);
        DecimalQuantity copy = quantity.createCopy();
        micros.rounder.apply(copy);
        micros.modOuter = this.modifiers.get((Object)copy.getStandardPlural(this.rules));
        return micros;
    }

    private static final class PluralTableSink
    extends UResource.Sink {
        String[] outArray;

        public PluralTableSink(String[] outArray) {
            this.outArray = outArray;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table pluralsTable = value.getTable();
            int i2 = 0;
            while (pluralsTable.getKeyAndValue(i2, key, value)) {
                int index = LongNameHandler.getIndex(key.toString());
                if (this.outArray[index] == null) {
                    String formatString;
                    this.outArray[index] = formatString = value.getString();
                }
                ++i2;
            }
        }
    }
}

