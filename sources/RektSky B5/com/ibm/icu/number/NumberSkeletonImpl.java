/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.MacroProps;
import com.ibm.icu.impl.number.RoundingUtils;
import com.ibm.icu.number.CompactNotation;
import com.ibm.icu.number.FractionPrecision;
import com.ibm.icu.number.IntegerWidth;
import com.ibm.icu.number.Notation;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.number.Precision;
import com.ibm.icu.number.Scale;
import com.ibm.icu.number.ScientificNotation;
import com.ibm.icu.number.SimpleNotation;
import com.ibm.icu.number.SkeletonSyntaxException;
import com.ibm.icu.number.UnlocalizedNumberFormatter;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.util.BytesTrie;
import com.ibm.icu.util.CharsTrie;
import com.ibm.icu.util.CharsTrieBuilder;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.MeasureUnit;
import com.ibm.icu.util.NoUnit;
import com.ibm.icu.util.StringTrieBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

class NumberSkeletonImpl {
    static final StemEnum[] STEM_ENUM_VALUES = StemEnum.values();
    static final String SERIALIZED_STEM_TRIE = NumberSkeletonImpl.buildStemTrie();
    private static final CacheBase<String, UnlocalizedNumberFormatter, Void> cache = new SoftCache<String, UnlocalizedNumberFormatter, Void>(){

        @Override
        protected UnlocalizedNumberFormatter createInstance(String skeletonString, Void unused) {
            return NumberSkeletonImpl.create(skeletonString);
        }
    };

    NumberSkeletonImpl() {
    }

    static String buildStemTrie() {
        CharsTrieBuilder b2 = new CharsTrieBuilder();
        b2.add("compact-short", StemEnum.STEM_COMPACT_SHORT.ordinal());
        b2.add("compact-long", StemEnum.STEM_COMPACT_LONG.ordinal());
        b2.add("scientific", StemEnum.STEM_SCIENTIFIC.ordinal());
        b2.add("engineering", StemEnum.STEM_ENGINEERING.ordinal());
        b2.add("notation-simple", StemEnum.STEM_NOTATION_SIMPLE.ordinal());
        b2.add("base-unit", StemEnum.STEM_BASE_UNIT.ordinal());
        b2.add("percent", StemEnum.STEM_PERCENT.ordinal());
        b2.add("permille", StemEnum.STEM_PERMILLE.ordinal());
        b2.add("precision-integer", StemEnum.STEM_PRECISION_INTEGER.ordinal());
        b2.add("precision-unlimited", StemEnum.STEM_PRECISION_UNLIMITED.ordinal());
        b2.add("precision-currency-standard", StemEnum.STEM_PRECISION_CURRENCY_STANDARD.ordinal());
        b2.add("precision-currency-cash", StemEnum.STEM_PRECISION_CURRENCY_CASH.ordinal());
        b2.add("rounding-mode-ceiling", StemEnum.STEM_ROUNDING_MODE_CEILING.ordinal());
        b2.add("rounding-mode-floor", StemEnum.STEM_ROUNDING_MODE_FLOOR.ordinal());
        b2.add("rounding-mode-down", StemEnum.STEM_ROUNDING_MODE_DOWN.ordinal());
        b2.add("rounding-mode-up", StemEnum.STEM_ROUNDING_MODE_UP.ordinal());
        b2.add("rounding-mode-half-even", StemEnum.STEM_ROUNDING_MODE_HALF_EVEN.ordinal());
        b2.add("rounding-mode-half-down", StemEnum.STEM_ROUNDING_MODE_HALF_DOWN.ordinal());
        b2.add("rounding-mode-half-up", StemEnum.STEM_ROUNDING_MODE_HALF_UP.ordinal());
        b2.add("rounding-mode-unnecessary", StemEnum.STEM_ROUNDING_MODE_UNNECESSARY.ordinal());
        b2.add("group-off", StemEnum.STEM_GROUP_OFF.ordinal());
        b2.add("group-min2", StemEnum.STEM_GROUP_MIN2.ordinal());
        b2.add("group-auto", StemEnum.STEM_GROUP_AUTO.ordinal());
        b2.add("group-on-aligned", StemEnum.STEM_GROUP_ON_ALIGNED.ordinal());
        b2.add("group-thousands", StemEnum.STEM_GROUP_THOUSANDS.ordinal());
        b2.add("latin", StemEnum.STEM_LATIN.ordinal());
        b2.add("unit-width-narrow", StemEnum.STEM_UNIT_WIDTH_NARROW.ordinal());
        b2.add("unit-width-short", StemEnum.STEM_UNIT_WIDTH_SHORT.ordinal());
        b2.add("unit-width-full-name", StemEnum.STEM_UNIT_WIDTH_FULL_NAME.ordinal());
        b2.add("unit-width-iso-code", StemEnum.STEM_UNIT_WIDTH_ISO_CODE.ordinal());
        b2.add("unit-width-hidden", StemEnum.STEM_UNIT_WIDTH_HIDDEN.ordinal());
        b2.add("sign-auto", StemEnum.STEM_SIGN_AUTO.ordinal());
        b2.add("sign-always", StemEnum.STEM_SIGN_ALWAYS.ordinal());
        b2.add("sign-never", StemEnum.STEM_SIGN_NEVER.ordinal());
        b2.add("sign-accounting", StemEnum.STEM_SIGN_ACCOUNTING.ordinal());
        b2.add("sign-accounting-always", StemEnum.STEM_SIGN_ACCOUNTING_ALWAYS.ordinal());
        b2.add("sign-except-zero", StemEnum.STEM_SIGN_EXCEPT_ZERO.ordinal());
        b2.add("sign-accounting-except-zero", StemEnum.STEM_SIGN_ACCOUNTING_EXCEPT_ZERO.ordinal());
        b2.add("decimal-auto", StemEnum.STEM_DECIMAL_AUTO.ordinal());
        b2.add("decimal-always", StemEnum.STEM_DECIMAL_ALWAYS.ordinal());
        b2.add("precision-increment", StemEnum.STEM_PRECISION_INCREMENT.ordinal());
        b2.add("measure-unit", StemEnum.STEM_MEASURE_UNIT.ordinal());
        b2.add("per-measure-unit", StemEnum.STEM_PER_MEASURE_UNIT.ordinal());
        b2.add("currency", StemEnum.STEM_CURRENCY.ordinal());
        b2.add("integer-width", StemEnum.STEM_INTEGER_WIDTH.ordinal());
        b2.add("numbering-system", StemEnum.STEM_NUMBERING_SYSTEM.ordinal());
        b2.add("scale", StemEnum.STEM_SCALE.ordinal());
        return b2.buildCharSequence(StringTrieBuilder.Option.FAST).toString();
    }

    public static UnlocalizedNumberFormatter getOrCreate(String skeletonString) {
        return cache.getInstance(skeletonString, null);
    }

    public static UnlocalizedNumberFormatter create(String skeletonString) {
        MacroProps macros = NumberSkeletonImpl.parseSkeleton(skeletonString);
        return (UnlocalizedNumberFormatter)NumberFormatter.with().macros(macros);
    }

    public static String generate(MacroProps macros) {
        StringBuilder sb = new StringBuilder();
        NumberSkeletonImpl.generateSkeleton(macros, sb);
        return sb.toString();
    }

    private static MacroProps parseSkeleton(String skeletonString) {
        skeletonString = skeletonString + " ";
        MacroProps macros = new MacroProps();
        StringSegment segment = new StringSegment(skeletonString, false);
        CharsTrie stemTrie = new CharsTrie(SERIALIZED_STEM_TRIE, 0);
        ParseState stem = ParseState.STATE_NULL;
        int offset = 0;
        while (offset < segment.length()) {
            boolean isOptionSeparator;
            int cp = segment.codePointAt(offset);
            boolean isTokenSeparator = PatternProps.isWhiteSpace(cp);
            boolean bl = isOptionSeparator = cp == 47;
            if (!isTokenSeparator && !isOptionSeparator) {
                offset += Character.charCount(cp);
                if (stem != ParseState.STATE_NULL) continue;
                stemTrie.nextForCodePoint(cp);
                continue;
            }
            if (offset != 0) {
                segment.setLength(offset);
                if (stem == ParseState.STATE_NULL) {
                    stem = NumberSkeletonImpl.parseStem(segment, stemTrie, macros);
                    stemTrie.reset();
                } else {
                    stem = NumberSkeletonImpl.parseOption(stem, segment, macros);
                }
                segment.resetLength();
                segment.adjustOffset(offset);
                offset = 0;
            } else if (stem != ParseState.STATE_NULL) {
                segment.setLength(Character.charCount(cp));
                throw new SkeletonSyntaxException("Unexpected separator character", segment);
            }
            if (isOptionSeparator && stem == ParseState.STATE_NULL) {
                segment.setLength(Character.charCount(cp));
                throw new SkeletonSyntaxException("Unexpected option separator", segment);
            }
            if (isTokenSeparator && stem != ParseState.STATE_NULL) {
                switch (stem) {
                    case STATE_INCREMENT_PRECISION: 
                    case STATE_MEASURE_UNIT: 
                    case STATE_PER_MEASURE_UNIT: 
                    case STATE_CURRENCY_UNIT: 
                    case STATE_INTEGER_WIDTH: 
                    case STATE_NUMBERING_SYSTEM: 
                    case STATE_SCALE: {
                        segment.setLength(Character.charCount(cp));
                        throw new SkeletonSyntaxException("Stem requires an option", segment);
                    }
                }
                stem = ParseState.STATE_NULL;
            }
            segment.adjustOffset(Character.charCount(cp));
        }
        assert (stem == ParseState.STATE_NULL);
        return macros;
    }

    private static ParseState parseStem(StringSegment segment, CharsTrie stemTrie, MacroProps macros) {
        switch (segment.charAt(0)) {
            case '.': {
                NumberSkeletonImpl.checkNull(macros.precision, segment);
                BlueprintHelpers.parseFractionStem(segment, macros);
                return ParseState.STATE_FRACTION_PRECISION;
            }
            case '@': {
                NumberSkeletonImpl.checkNull(macros.precision, segment);
                BlueprintHelpers.parseDigitsStem(segment, macros);
                return ParseState.STATE_NULL;
            }
        }
        BytesTrie.Result stemResult = stemTrie.current();
        if (stemResult != BytesTrie.Result.INTERMEDIATE_VALUE && stemResult != BytesTrie.Result.FINAL_VALUE) {
            throw new SkeletonSyntaxException("Unknown stem", segment);
        }
        StemEnum stem = STEM_ENUM_VALUES[stemTrie.getValue()];
        switch (stem) {
            case STEM_COMPACT_SHORT: 
            case STEM_COMPACT_LONG: 
            case STEM_SCIENTIFIC: 
            case STEM_ENGINEERING: 
            case STEM_NOTATION_SIMPLE: {
                NumberSkeletonImpl.checkNull(macros.notation, segment);
                macros.notation = StemToObject.notation(stem);
                switch (stem) {
                    case STEM_SCIENTIFIC: 
                    case STEM_ENGINEERING: {
                        return ParseState.STATE_SCIENTIFIC;
                    }
                }
                return ParseState.STATE_NULL;
            }
            case STEM_BASE_UNIT: 
            case STEM_PERCENT: 
            case STEM_PERMILLE: {
                NumberSkeletonImpl.checkNull(macros.unit, segment);
                macros.unit = StemToObject.unit(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_PRECISION_INTEGER: 
            case STEM_PRECISION_UNLIMITED: 
            case STEM_PRECISION_CURRENCY_STANDARD: 
            case STEM_PRECISION_CURRENCY_CASH: {
                NumberSkeletonImpl.checkNull(macros.precision, segment);
                macros.precision = StemToObject.precision(stem);
                switch (stem) {
                    case STEM_PRECISION_INTEGER: {
                        return ParseState.STATE_FRACTION_PRECISION;
                    }
                }
                return ParseState.STATE_NULL;
            }
            case STEM_ROUNDING_MODE_CEILING: 
            case STEM_ROUNDING_MODE_FLOOR: 
            case STEM_ROUNDING_MODE_DOWN: 
            case STEM_ROUNDING_MODE_UP: 
            case STEM_ROUNDING_MODE_HALF_EVEN: 
            case STEM_ROUNDING_MODE_HALF_DOWN: 
            case STEM_ROUNDING_MODE_HALF_UP: 
            case STEM_ROUNDING_MODE_UNNECESSARY: {
                NumberSkeletonImpl.checkNull((Object)macros.roundingMode, segment);
                macros.roundingMode = StemToObject.roundingMode(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_GROUP_OFF: 
            case STEM_GROUP_MIN2: 
            case STEM_GROUP_AUTO: 
            case STEM_GROUP_ON_ALIGNED: 
            case STEM_GROUP_THOUSANDS: {
                NumberSkeletonImpl.checkNull(macros.grouping, segment);
                macros.grouping = StemToObject.groupingStrategy(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_LATIN: {
                NumberSkeletonImpl.checkNull(macros.symbols, segment);
                macros.symbols = NumberingSystem.LATIN;
                return ParseState.STATE_NULL;
            }
            case STEM_UNIT_WIDTH_NARROW: 
            case STEM_UNIT_WIDTH_SHORT: 
            case STEM_UNIT_WIDTH_FULL_NAME: 
            case STEM_UNIT_WIDTH_ISO_CODE: 
            case STEM_UNIT_WIDTH_HIDDEN: {
                NumberSkeletonImpl.checkNull((Object)macros.unitWidth, segment);
                macros.unitWidth = StemToObject.unitWidth(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_SIGN_AUTO: 
            case STEM_SIGN_ALWAYS: 
            case STEM_SIGN_NEVER: 
            case STEM_SIGN_ACCOUNTING: 
            case STEM_SIGN_ACCOUNTING_ALWAYS: 
            case STEM_SIGN_EXCEPT_ZERO: 
            case STEM_SIGN_ACCOUNTING_EXCEPT_ZERO: {
                NumberSkeletonImpl.checkNull((Object)macros.sign, segment);
                macros.sign = StemToObject.signDisplay(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_DECIMAL_AUTO: 
            case STEM_DECIMAL_ALWAYS: {
                NumberSkeletonImpl.checkNull((Object)macros.decimal, segment);
                macros.decimal = StemToObject.decimalSeparatorDisplay(stem);
                return ParseState.STATE_NULL;
            }
            case STEM_PRECISION_INCREMENT: {
                NumberSkeletonImpl.checkNull(macros.precision, segment);
                return ParseState.STATE_INCREMENT_PRECISION;
            }
            case STEM_MEASURE_UNIT: {
                NumberSkeletonImpl.checkNull(macros.unit, segment);
                return ParseState.STATE_MEASURE_UNIT;
            }
            case STEM_PER_MEASURE_UNIT: {
                NumberSkeletonImpl.checkNull(macros.perUnit, segment);
                return ParseState.STATE_PER_MEASURE_UNIT;
            }
            case STEM_CURRENCY: {
                NumberSkeletonImpl.checkNull(macros.unit, segment);
                return ParseState.STATE_CURRENCY_UNIT;
            }
            case STEM_INTEGER_WIDTH: {
                NumberSkeletonImpl.checkNull(macros.integerWidth, segment);
                return ParseState.STATE_INTEGER_WIDTH;
            }
            case STEM_NUMBERING_SYSTEM: {
                NumberSkeletonImpl.checkNull(macros.symbols, segment);
                return ParseState.STATE_NUMBERING_SYSTEM;
            }
            case STEM_SCALE: {
                NumberSkeletonImpl.checkNull(macros.scale, segment);
                return ParseState.STATE_SCALE;
            }
        }
        throw new AssertionError();
    }

    private static ParseState parseOption(ParseState stem, StringSegment segment, MacroProps macros) {
        switch (stem) {
            case STATE_CURRENCY_UNIT: {
                BlueprintHelpers.parseCurrencyOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_MEASURE_UNIT: {
                BlueprintHelpers.parseMeasureUnitOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_PER_MEASURE_UNIT: {
                BlueprintHelpers.parseMeasurePerUnitOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_INCREMENT_PRECISION: {
                BlueprintHelpers.parseIncrementOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_INTEGER_WIDTH: {
                BlueprintHelpers.parseIntegerWidthOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_NUMBERING_SYSTEM: {
                BlueprintHelpers.parseNumberingSystemOption(segment, macros);
                return ParseState.STATE_NULL;
            }
            case STATE_SCALE: {
                BlueprintHelpers.parseScaleOption(segment, macros);
                return ParseState.STATE_NULL;
            }
        }
        switch (stem) {
            case STATE_SCIENTIFIC: {
                if (BlueprintHelpers.parseExponentWidthOption(segment, macros)) {
                    return ParseState.STATE_SCIENTIFIC;
                }
                if (!BlueprintHelpers.parseExponentSignOption(segment, macros)) break;
                return ParseState.STATE_SCIENTIFIC;
            }
        }
        switch (stem) {
            case STATE_FRACTION_PRECISION: {
                if (!BlueprintHelpers.parseFracSigOption(segment, macros)) break;
                return ParseState.STATE_NULL;
            }
        }
        throw new SkeletonSyntaxException("Invalid option", segment);
    }

    private static void generateSkeleton(MacroProps macros, StringBuilder sb) {
        if (macros.notation != null && GeneratorHelpers.notation(macros, sb)) {
            sb.append(' ');
        }
        if (macros.unit != null && GeneratorHelpers.unit(macros, sb)) {
            sb.append(' ');
        }
        if (macros.perUnit != null && GeneratorHelpers.perUnit(macros, sb)) {
            sb.append(' ');
        }
        if (macros.precision != null && GeneratorHelpers.precision(macros, sb)) {
            sb.append(' ');
        }
        if (macros.roundingMode != null && GeneratorHelpers.roundingMode(macros, sb)) {
            sb.append(' ');
        }
        if (macros.grouping != null && GeneratorHelpers.grouping(macros, sb)) {
            sb.append(' ');
        }
        if (macros.integerWidth != null && GeneratorHelpers.integerWidth(macros, sb)) {
            sb.append(' ');
        }
        if (macros.symbols != null && GeneratorHelpers.symbols(macros, sb)) {
            sb.append(' ');
        }
        if (macros.unitWidth != null && GeneratorHelpers.unitWidth(macros, sb)) {
            sb.append(' ');
        }
        if (macros.sign != null && GeneratorHelpers.sign(macros, sb)) {
            sb.append(' ');
        }
        if (macros.decimal != null && GeneratorHelpers.decimal(macros, sb)) {
            sb.append(' ');
        }
        if (macros.scale != null && GeneratorHelpers.scale(macros, sb)) {
            sb.append(' ');
        }
        if (macros.padder != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom padder");
        }
        if (macros.affixProvider != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom affix provider");
        }
        if (macros.rules != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom plural rules");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
    }

    private static void checkNull(Object value, CharSequence content) {
        if (value != null) {
            throw new SkeletonSyntaxException("Duplicated setting", content);
        }
    }

    private static void appendMultiple(StringBuilder sb, int cp, int count) {
        for (int i2 = 0; i2 < count; ++i2) {
            sb.appendCodePoint(cp);
        }
    }

    static final class GeneratorHelpers {
        GeneratorHelpers() {
        }

        private static boolean notation(MacroProps macros, StringBuilder sb) {
            if (macros.notation instanceof CompactNotation) {
                if (macros.notation == Notation.compactLong()) {
                    sb.append("compact-long");
                    return true;
                }
                if (macros.notation == Notation.compactShort()) {
                    sb.append("compact-short");
                    return true;
                }
                throw new UnsupportedOperationException("Cannot generate number skeleton with custom compact data");
            }
            if (macros.notation instanceof ScientificNotation) {
                ScientificNotation impl = (ScientificNotation)macros.notation;
                if (impl.engineeringInterval == 3) {
                    sb.append("engineering");
                } else {
                    sb.append("scientific");
                }
                if (impl.minExponentDigits > 1) {
                    sb.append('/');
                    BlueprintHelpers.generateExponentWidthOption(impl.minExponentDigits, sb);
                }
                if (impl.exponentSignDisplay != NumberFormatter.SignDisplay.AUTO) {
                    sb.append('/');
                    EnumToStemString.signDisplay(impl.exponentSignDisplay, sb);
                }
                return true;
            }
            assert (macros.notation instanceof SimpleNotation);
            return false;
        }

        private static boolean unit(MacroProps macros, StringBuilder sb) {
            if (macros.unit instanceof Currency) {
                sb.append("currency/");
                BlueprintHelpers.generateCurrencyOption((Currency)macros.unit, sb);
                return true;
            }
            if (macros.unit instanceof NoUnit) {
                if (macros.unit == NoUnit.PERCENT) {
                    sb.append("percent");
                    return true;
                }
                if (macros.unit == NoUnit.PERMILLE) {
                    sb.append("permille");
                    return true;
                }
                assert (macros.unit == NoUnit.BASE);
                return false;
            }
            sb.append("measure-unit/");
            BlueprintHelpers.generateMeasureUnitOption(macros.unit, sb);
            return true;
        }

        private static boolean perUnit(MacroProps macros, StringBuilder sb) {
            if (macros.perUnit instanceof Currency || macros.perUnit instanceof NoUnit) {
                throw new UnsupportedOperationException("Cannot generate number skeleton with per-unit that is not a standard measure unit");
            }
            sb.append("per-measure-unit/");
            BlueprintHelpers.generateMeasureUnitOption(macros.perUnit, sb);
            return true;
        }

        private static boolean precision(MacroProps macros, StringBuilder sb) {
            if (macros.precision instanceof Precision.InfiniteRounderImpl) {
                sb.append("precision-unlimited");
            } else if (macros.precision instanceof Precision.FractionRounderImpl) {
                Precision.FractionRounderImpl impl = (Precision.FractionRounderImpl)macros.precision;
                BlueprintHelpers.generateFractionStem(impl.minFrac, impl.maxFrac, sb);
            } else if (macros.precision instanceof Precision.SignificantRounderImpl) {
                Precision.SignificantRounderImpl impl = (Precision.SignificantRounderImpl)macros.precision;
                BlueprintHelpers.generateDigitsStem(impl.minSig, impl.maxSig, sb);
            } else if (macros.precision instanceof Precision.FracSigRounderImpl) {
                Precision.FracSigRounderImpl impl = (Precision.FracSigRounderImpl)macros.precision;
                BlueprintHelpers.generateFractionStem(impl.minFrac, impl.maxFrac, sb);
                sb.append('/');
                if (impl.minSig == -1) {
                    BlueprintHelpers.generateDigitsStem(1, impl.maxSig, sb);
                } else {
                    BlueprintHelpers.generateDigitsStem(impl.minSig, -1, sb);
                }
            } else if (macros.precision instanceof Precision.IncrementRounderImpl) {
                Precision.IncrementRounderImpl impl = (Precision.IncrementRounderImpl)macros.precision;
                sb.append("precision-increment/");
                BlueprintHelpers.generateIncrementOption(impl.increment, sb);
            } else {
                assert (macros.precision instanceof Precision.CurrencyRounderImpl);
                Precision.CurrencyRounderImpl impl = (Precision.CurrencyRounderImpl)macros.precision;
                if (impl.usage == Currency.CurrencyUsage.STANDARD) {
                    sb.append("precision-currency-standard");
                } else {
                    sb.append("precision-currency-cash");
                }
            }
            return true;
        }

        private static boolean roundingMode(MacroProps macros, StringBuilder sb) {
            if (macros.roundingMode == RoundingUtils.DEFAULT_ROUNDING_MODE) {
                return false;
            }
            EnumToStemString.roundingMode(macros.roundingMode, sb);
            return true;
        }

        private static boolean grouping(MacroProps macros, StringBuilder sb) {
            if (macros.grouping instanceof NumberFormatter.GroupingStrategy) {
                if (macros.grouping == NumberFormatter.GroupingStrategy.AUTO) {
                    return false;
                }
                EnumToStemString.groupingStrategy((NumberFormatter.GroupingStrategy)((Object)macros.grouping), sb);
                return true;
            }
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom Grouper");
        }

        private static boolean integerWidth(MacroProps macros, StringBuilder sb) {
            if (macros.integerWidth.equals(IntegerWidth.DEFAULT)) {
                return false;
            }
            sb.append("integer-width/");
            BlueprintHelpers.generateIntegerWidthOption(macros.integerWidth.minInt, macros.integerWidth.maxInt, sb);
            return true;
        }

        private static boolean symbols(MacroProps macros, StringBuilder sb) {
            if (macros.symbols instanceof NumberingSystem) {
                NumberingSystem ns = (NumberingSystem)macros.symbols;
                if (ns.getName().equals("latn")) {
                    sb.append("latin");
                } else {
                    sb.append("numbering-system/");
                    BlueprintHelpers.generateNumberingSystemOption(ns, sb);
                }
                return true;
            }
            assert (macros.symbols instanceof DecimalFormatSymbols);
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom DecimalFormatSymbols");
        }

        private static boolean unitWidth(MacroProps macros, StringBuilder sb) {
            if (macros.unitWidth == NumberFormatter.UnitWidth.SHORT) {
                return false;
            }
            EnumToStemString.unitWidth(macros.unitWidth, sb);
            return true;
        }

        private static boolean sign(MacroProps macros, StringBuilder sb) {
            if (macros.sign == NumberFormatter.SignDisplay.AUTO) {
                return false;
            }
            EnumToStemString.signDisplay(macros.sign, sb);
            return true;
        }

        private static boolean decimal(MacroProps macros, StringBuilder sb) {
            if (macros.decimal == NumberFormatter.DecimalSeparatorDisplay.AUTO) {
                return false;
            }
            EnumToStemString.decimalSeparatorDisplay(macros.decimal, sb);
            return true;
        }

        private static boolean scale(MacroProps macros, StringBuilder sb) {
            if (!macros.scale.isValid()) {
                return false;
            }
            sb.append("scale/");
            BlueprintHelpers.generateScaleOption(macros.scale, sb);
            return true;
        }
    }

    static final class BlueprintHelpers {
        BlueprintHelpers() {
        }

        private static boolean parseExponentWidthOption(StringSegment segment, MacroProps macros) {
            int offset;
            if (segment.charAt(0) != '+') {
                return false;
            }
            int minExp = 0;
            for (offset = 1; offset < segment.length() && segment.charAt(offset) == 'e'; ++offset) {
                ++minExp;
            }
            if (offset < segment.length()) {
                return false;
            }
            macros.notation = ((ScientificNotation)macros.notation).withMinExponentDigits(minExp);
            return true;
        }

        private static void generateExponentWidthOption(int minExponentDigits, StringBuilder sb) {
            sb.append('+');
            NumberSkeletonImpl.appendMultiple(sb, 101, minExponentDigits);
        }

        private static boolean parseExponentSignOption(StringSegment segment, MacroProps macros) {
            CharsTrie tempStemTrie = new CharsTrie(SERIALIZED_STEM_TRIE, 0);
            BytesTrie.Result result = tempStemTrie.next(segment, 0, segment.length());
            if (result != BytesTrie.Result.INTERMEDIATE_VALUE && result != BytesTrie.Result.FINAL_VALUE) {
                return false;
            }
            NumberFormatter.SignDisplay sign = StemToObject.signDisplay(STEM_ENUM_VALUES[tempStemTrie.getValue()]);
            if (sign == null) {
                return false;
            }
            macros.notation = ((ScientificNotation)macros.notation).withExponentSignDisplay(sign);
            return true;
        }

        private static void parseCurrencyOption(StringSegment segment, MacroProps macros) {
            Currency currency;
            String currencyCode = segment.subSequence(0, segment.length()).toString();
            try {
                currency = Currency.getInstance(currencyCode);
            }
            catch (IllegalArgumentException e2) {
                throw new SkeletonSyntaxException("Invalid currency", segment, e2);
            }
            macros.unit = currency;
        }

        private static void generateCurrencyOption(Currency currency, StringBuilder sb) {
            sb.append(currency.getCurrencyCode());
        }

        private static void parseMeasureUnitOption(StringSegment segment, MacroProps macros) {
            int firstHyphen;
            for (firstHyphen = 0; firstHyphen < segment.length() && segment.charAt(firstHyphen) != '-'; ++firstHyphen) {
            }
            if (firstHyphen == segment.length()) {
                throw new SkeletonSyntaxException("Invalid measure unit option", segment);
            }
            String type = segment.subSequence(0, firstHyphen).toString();
            String subType = segment.subSequence(firstHyphen + 1, segment.length()).toString();
            Set<MeasureUnit> units = MeasureUnit.getAvailable(type);
            for (MeasureUnit unit : units) {
                if (!subType.equals(unit.getSubtype())) continue;
                macros.unit = unit;
                return;
            }
            throw new SkeletonSyntaxException("Unknown measure unit", segment);
        }

        private static void generateMeasureUnitOption(MeasureUnit unit, StringBuilder sb) {
            sb.append(unit.getType());
            sb.append("-");
            sb.append(unit.getSubtype());
        }

        private static void parseMeasurePerUnitOption(StringSegment segment, MacroProps macros) {
            MeasureUnit numerator = macros.unit;
            BlueprintHelpers.parseMeasureUnitOption(segment, macros);
            macros.perUnit = macros.unit;
            macros.unit = numerator;
        }

        private static void parseFractionStem(StringSegment segment, MacroProps macros) {
            int maxFrac;
            int offset;
            assert (segment.charAt(0) == '.');
            int minFrac = 0;
            for (offset = 1; offset < segment.length() && segment.charAt(offset) == '0'; ++offset) {
                ++minFrac;
            }
            if (offset < segment.length()) {
                if (segment.charAt(offset) == '+') {
                    maxFrac = -1;
                    ++offset;
                } else {
                    maxFrac = minFrac;
                    while (offset < segment.length() && segment.charAt(offset) == '#') {
                        ++maxFrac;
                        ++offset;
                    }
                }
            } else {
                maxFrac = minFrac;
            }
            if (offset < segment.length()) {
                throw new SkeletonSyntaxException("Invalid fraction stem", segment);
            }
            macros.precision = maxFrac == -1 ? Precision.minFraction(minFrac) : Precision.minMaxFraction(minFrac, maxFrac);
        }

        private static void generateFractionStem(int minFrac, int maxFrac, StringBuilder sb) {
            if (minFrac == 0 && maxFrac == 0) {
                sb.append("precision-integer");
                return;
            }
            sb.append('.');
            NumberSkeletonImpl.appendMultiple(sb, 48, minFrac);
            if (maxFrac == -1) {
                sb.append('+');
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, maxFrac - minFrac);
            }
        }

        private static void parseDigitsStem(StringSegment segment, MacroProps macros) {
            int maxSig;
            int offset;
            assert (segment.charAt(0) == '@');
            int minSig = 0;
            for (offset = 0; offset < segment.length() && segment.charAt(offset) == '@'; ++offset) {
                ++minSig;
            }
            if (offset < segment.length()) {
                if (segment.charAt(offset) == '+') {
                    maxSig = -1;
                    ++offset;
                } else {
                    maxSig = minSig;
                    while (offset < segment.length() && segment.charAt(offset) == '#') {
                        ++maxSig;
                        ++offset;
                    }
                }
            } else {
                maxSig = minSig;
            }
            if (offset < segment.length()) {
                throw new SkeletonSyntaxException("Invalid significant digits stem", segment);
            }
            macros.precision = maxSig == -1 ? Precision.minSignificantDigits(minSig) : Precision.minMaxSignificantDigits(minSig, maxSig);
        }

        private static void generateDigitsStem(int minSig, int maxSig, StringBuilder sb) {
            NumberSkeletonImpl.appendMultiple(sb, 64, minSig);
            if (maxSig == -1) {
                sb.append('+');
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, maxSig - minSig);
            }
        }

        private static boolean parseFracSigOption(StringSegment segment, MacroProps macros) {
            int maxSig;
            int offset;
            if (segment.charAt(0) != '@') {
                return false;
            }
            int minSig = 0;
            for (offset = 0; offset < segment.length() && segment.charAt(offset) == '@'; ++offset) {
                ++minSig;
            }
            if (offset < segment.length()) {
                if (segment.charAt(offset) == '+') {
                    maxSig = -1;
                    ++offset;
                } else {
                    if (minSig > 1) {
                        throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", segment);
                    }
                    maxSig = minSig;
                    while (offset < segment.length() && segment.charAt(offset) == '#') {
                        ++maxSig;
                        ++offset;
                    }
                }
            } else {
                throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", segment);
            }
            if (offset < segment.length()) {
                throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", segment);
            }
            FractionPrecision oldRounder = (FractionPrecision)macros.precision;
            macros.precision = maxSig == -1 ? oldRounder.withMinDigits(minSig) : oldRounder.withMaxDigits(maxSig);
            return true;
        }

        private static void parseIncrementOption(StringSegment segment, MacroProps macros) {
            BigDecimal increment;
            String str = segment.subSequence(0, segment.length()).toString();
            try {
                increment = new BigDecimal(str);
            }
            catch (NumberFormatException e2) {
                throw new SkeletonSyntaxException("Invalid rounding increment", segment, e2);
            }
            macros.precision = Precision.increment(increment);
        }

        private static void generateIncrementOption(BigDecimal increment, StringBuilder sb) {
            sb.append(increment.toPlainString());
        }

        private static void parseIntegerWidthOption(StringSegment segment, MacroProps macros) {
            int maxInt;
            int offset = 0;
            int minInt = 0;
            if (segment.charAt(0) == '+') {
                maxInt = -1;
                ++offset;
            } else {
                maxInt = 0;
            }
            while (offset < segment.length() && segment.charAt(offset) == '#') {
                ++maxInt;
                ++offset;
            }
            if (offset < segment.length()) {
                while (offset < segment.length() && segment.charAt(offset) == '0') {
                    ++minInt;
                    ++offset;
                }
            }
            if (maxInt != -1) {
                maxInt += minInt;
            }
            if (offset < segment.length()) {
                throw new SkeletonSyntaxException("Invalid integer width stem", segment);
            }
            macros.integerWidth = maxInt == -1 ? IntegerWidth.zeroFillTo(minInt) : IntegerWidth.zeroFillTo(minInt).truncateAt(maxInt);
        }

        private static void generateIntegerWidthOption(int minInt, int maxInt, StringBuilder sb) {
            if (maxInt == -1) {
                sb.append('+');
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, maxInt - minInt);
            }
            NumberSkeletonImpl.appendMultiple(sb, 48, minInt);
        }

        private static void parseNumberingSystemOption(StringSegment segment, MacroProps macros) {
            String nsName = segment.subSequence(0, segment.length()).toString();
            NumberingSystem ns = NumberingSystem.getInstanceByName(nsName);
            if (ns == null) {
                throw new SkeletonSyntaxException("Unknown numbering system", segment);
            }
            macros.symbols = ns;
        }

        private static void generateNumberingSystemOption(NumberingSystem ns, StringBuilder sb) {
            sb.append(ns.getName());
        }

        private static void parseScaleOption(StringSegment segment, MacroProps macros) {
            BigDecimal bd;
            String str = segment.subSequence(0, segment.length()).toString();
            try {
                bd = new BigDecimal(str);
            }
            catch (NumberFormatException e2) {
                throw new SkeletonSyntaxException("Invalid scale", segment, e2);
            }
            macros.scale = Scale.byBigDecimal(bd);
        }

        private static void generateScaleOption(Scale scale, StringBuilder sb) {
            BigDecimal bd = scale.arbitrary;
            if (bd == null) {
                bd = BigDecimal.ONE;
            }
            bd = bd.scaleByPowerOfTen(scale.magnitude);
            sb.append(bd.toPlainString());
        }
    }

    static final class EnumToStemString {
        EnumToStemString() {
        }

        private static void roundingMode(RoundingMode value, StringBuilder sb) {
            switch (value) {
                case CEILING: {
                    sb.append("rounding-mode-ceiling");
                    break;
                }
                case FLOOR: {
                    sb.append("rounding-mode-floor");
                    break;
                }
                case DOWN: {
                    sb.append("rounding-mode-down");
                    break;
                }
                case UP: {
                    sb.append("rounding-mode-up");
                    break;
                }
                case HALF_EVEN: {
                    sb.append("rounding-mode-half-even");
                    break;
                }
                case HALF_DOWN: {
                    sb.append("rounding-mode-half-down");
                    break;
                }
                case HALF_UP: {
                    sb.append("rounding-mode-half-up");
                    break;
                }
                case UNNECESSARY: {
                    sb.append("rounding-mode-unnecessary");
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }

        private static void groupingStrategy(NumberFormatter.GroupingStrategy value, StringBuilder sb) {
            switch (value) {
                case OFF: {
                    sb.append("group-off");
                    break;
                }
                case MIN2: {
                    sb.append("group-min2");
                    break;
                }
                case AUTO: {
                    sb.append("group-auto");
                    break;
                }
                case ON_ALIGNED: {
                    sb.append("group-on-aligned");
                    break;
                }
                case THOUSANDS: {
                    sb.append("group-thousands");
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }

        private static void unitWidth(NumberFormatter.UnitWidth value, StringBuilder sb) {
            switch (value) {
                case NARROW: {
                    sb.append("unit-width-narrow");
                    break;
                }
                case SHORT: {
                    sb.append("unit-width-short");
                    break;
                }
                case FULL_NAME: {
                    sb.append("unit-width-full-name");
                    break;
                }
                case ISO_CODE: {
                    sb.append("unit-width-iso-code");
                    break;
                }
                case HIDDEN: {
                    sb.append("unit-width-hidden");
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }

        private static void signDisplay(NumberFormatter.SignDisplay value, StringBuilder sb) {
            switch (value) {
                case AUTO: {
                    sb.append("sign-auto");
                    break;
                }
                case ALWAYS: {
                    sb.append("sign-always");
                    break;
                }
                case NEVER: {
                    sb.append("sign-never");
                    break;
                }
                case ACCOUNTING: {
                    sb.append("sign-accounting");
                    break;
                }
                case ACCOUNTING_ALWAYS: {
                    sb.append("sign-accounting-always");
                    break;
                }
                case EXCEPT_ZERO: {
                    sb.append("sign-except-zero");
                    break;
                }
                case ACCOUNTING_EXCEPT_ZERO: {
                    sb.append("sign-accounting-except-zero");
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }

        private static void decimalSeparatorDisplay(NumberFormatter.DecimalSeparatorDisplay value, StringBuilder sb) {
            switch (value) {
                case AUTO: {
                    sb.append("decimal-auto");
                    break;
                }
                case ALWAYS: {
                    sb.append("decimal-always");
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
    }

    static final class StemToObject {
        StemToObject() {
        }

        private static Notation notation(StemEnum stem) {
            switch (stem) {
                case STEM_COMPACT_SHORT: {
                    return Notation.compactShort();
                }
                case STEM_COMPACT_LONG: {
                    return Notation.compactLong();
                }
                case STEM_SCIENTIFIC: {
                    return Notation.scientific();
                }
                case STEM_ENGINEERING: {
                    return Notation.engineering();
                }
                case STEM_NOTATION_SIMPLE: {
                    return Notation.simple();
                }
            }
            throw new AssertionError();
        }

        private static MeasureUnit unit(StemEnum stem) {
            switch (stem) {
                case STEM_BASE_UNIT: {
                    return NoUnit.BASE;
                }
                case STEM_PERCENT: {
                    return NoUnit.PERCENT;
                }
                case STEM_PERMILLE: {
                    return NoUnit.PERMILLE;
                }
            }
            throw new AssertionError();
        }

        private static Precision precision(StemEnum stem) {
            switch (stem) {
                case STEM_PRECISION_INTEGER: {
                    return Precision.integer();
                }
                case STEM_PRECISION_UNLIMITED: {
                    return Precision.unlimited();
                }
                case STEM_PRECISION_CURRENCY_STANDARD: {
                    return Precision.currency(Currency.CurrencyUsage.STANDARD);
                }
                case STEM_PRECISION_CURRENCY_CASH: {
                    return Precision.currency(Currency.CurrencyUsage.CASH);
                }
            }
            throw new AssertionError();
        }

        private static RoundingMode roundingMode(StemEnum stem) {
            switch (stem) {
                case STEM_ROUNDING_MODE_CEILING: {
                    return RoundingMode.CEILING;
                }
                case STEM_ROUNDING_MODE_FLOOR: {
                    return RoundingMode.FLOOR;
                }
                case STEM_ROUNDING_MODE_DOWN: {
                    return RoundingMode.DOWN;
                }
                case STEM_ROUNDING_MODE_UP: {
                    return RoundingMode.UP;
                }
                case STEM_ROUNDING_MODE_HALF_EVEN: {
                    return RoundingMode.HALF_EVEN;
                }
                case STEM_ROUNDING_MODE_HALF_DOWN: {
                    return RoundingMode.HALF_DOWN;
                }
                case STEM_ROUNDING_MODE_HALF_UP: {
                    return RoundingMode.HALF_UP;
                }
                case STEM_ROUNDING_MODE_UNNECESSARY: {
                    return RoundingMode.UNNECESSARY;
                }
            }
            throw new AssertionError();
        }

        private static NumberFormatter.GroupingStrategy groupingStrategy(StemEnum stem) {
            switch (stem) {
                case STEM_GROUP_OFF: {
                    return NumberFormatter.GroupingStrategy.OFF;
                }
                case STEM_GROUP_MIN2: {
                    return NumberFormatter.GroupingStrategy.MIN2;
                }
                case STEM_GROUP_AUTO: {
                    return NumberFormatter.GroupingStrategy.AUTO;
                }
                case STEM_GROUP_ON_ALIGNED: {
                    return NumberFormatter.GroupingStrategy.ON_ALIGNED;
                }
                case STEM_GROUP_THOUSANDS: {
                    return NumberFormatter.GroupingStrategy.THOUSANDS;
                }
            }
            return null;
        }

        private static NumberFormatter.UnitWidth unitWidth(StemEnum stem) {
            switch (stem) {
                case STEM_UNIT_WIDTH_NARROW: {
                    return NumberFormatter.UnitWidth.NARROW;
                }
                case STEM_UNIT_WIDTH_SHORT: {
                    return NumberFormatter.UnitWidth.SHORT;
                }
                case STEM_UNIT_WIDTH_FULL_NAME: {
                    return NumberFormatter.UnitWidth.FULL_NAME;
                }
                case STEM_UNIT_WIDTH_ISO_CODE: {
                    return NumberFormatter.UnitWidth.ISO_CODE;
                }
                case STEM_UNIT_WIDTH_HIDDEN: {
                    return NumberFormatter.UnitWidth.HIDDEN;
                }
            }
            return null;
        }

        private static NumberFormatter.SignDisplay signDisplay(StemEnum stem) {
            switch (stem) {
                case STEM_SIGN_AUTO: {
                    return NumberFormatter.SignDisplay.AUTO;
                }
                case STEM_SIGN_ALWAYS: {
                    return NumberFormatter.SignDisplay.ALWAYS;
                }
                case STEM_SIGN_NEVER: {
                    return NumberFormatter.SignDisplay.NEVER;
                }
                case STEM_SIGN_ACCOUNTING: {
                    return NumberFormatter.SignDisplay.ACCOUNTING;
                }
                case STEM_SIGN_ACCOUNTING_ALWAYS: {
                    return NumberFormatter.SignDisplay.ACCOUNTING_ALWAYS;
                }
                case STEM_SIGN_EXCEPT_ZERO: {
                    return NumberFormatter.SignDisplay.EXCEPT_ZERO;
                }
                case STEM_SIGN_ACCOUNTING_EXCEPT_ZERO: {
                    return NumberFormatter.SignDisplay.ACCOUNTING_EXCEPT_ZERO;
                }
            }
            return null;
        }

        private static NumberFormatter.DecimalSeparatorDisplay decimalSeparatorDisplay(StemEnum stem) {
            switch (stem) {
                case STEM_DECIMAL_AUTO: {
                    return NumberFormatter.DecimalSeparatorDisplay.AUTO;
                }
                case STEM_DECIMAL_ALWAYS: {
                    return NumberFormatter.DecimalSeparatorDisplay.ALWAYS;
                }
            }
            return null;
        }
    }

    static enum StemEnum {
        STEM_COMPACT_SHORT,
        STEM_COMPACT_LONG,
        STEM_SCIENTIFIC,
        STEM_ENGINEERING,
        STEM_NOTATION_SIMPLE,
        STEM_BASE_UNIT,
        STEM_PERCENT,
        STEM_PERMILLE,
        STEM_PRECISION_INTEGER,
        STEM_PRECISION_UNLIMITED,
        STEM_PRECISION_CURRENCY_STANDARD,
        STEM_PRECISION_CURRENCY_CASH,
        STEM_ROUNDING_MODE_CEILING,
        STEM_ROUNDING_MODE_FLOOR,
        STEM_ROUNDING_MODE_DOWN,
        STEM_ROUNDING_MODE_UP,
        STEM_ROUNDING_MODE_HALF_EVEN,
        STEM_ROUNDING_MODE_HALF_DOWN,
        STEM_ROUNDING_MODE_HALF_UP,
        STEM_ROUNDING_MODE_UNNECESSARY,
        STEM_GROUP_OFF,
        STEM_GROUP_MIN2,
        STEM_GROUP_AUTO,
        STEM_GROUP_ON_ALIGNED,
        STEM_GROUP_THOUSANDS,
        STEM_LATIN,
        STEM_UNIT_WIDTH_NARROW,
        STEM_UNIT_WIDTH_SHORT,
        STEM_UNIT_WIDTH_FULL_NAME,
        STEM_UNIT_WIDTH_ISO_CODE,
        STEM_UNIT_WIDTH_HIDDEN,
        STEM_SIGN_AUTO,
        STEM_SIGN_ALWAYS,
        STEM_SIGN_NEVER,
        STEM_SIGN_ACCOUNTING,
        STEM_SIGN_ACCOUNTING_ALWAYS,
        STEM_SIGN_EXCEPT_ZERO,
        STEM_SIGN_ACCOUNTING_EXCEPT_ZERO,
        STEM_DECIMAL_AUTO,
        STEM_DECIMAL_ALWAYS,
        STEM_PRECISION_INCREMENT,
        STEM_MEASURE_UNIT,
        STEM_PER_MEASURE_UNIT,
        STEM_CURRENCY,
        STEM_INTEGER_WIDTH,
        STEM_NUMBERING_SYSTEM,
        STEM_SCALE;

    }

    static enum ParseState {
        STATE_NULL,
        STATE_SCIENTIFIC,
        STATE_FRACTION_PRECISION,
        STATE_INCREMENT_PRECISION,
        STATE_MEASURE_UNIT,
        STATE_PER_MEASURE_UNIT,
        STATE_CURRENCY_UNIT,
        STATE_INTEGER_WIDTH,
        STATE_NUMBERING_SYSTEM,
        STATE_SCALE;

    }
}

