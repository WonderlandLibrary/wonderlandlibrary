/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.CollectionSet;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Pair;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.NoUnit;
import com.ibm.icu.util.TimeUnit;
import com.ibm.icu.util.UResourceBundle;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MeasureUnit
implements Serializable {
    private static final long serialVersionUID = -1839973855554750484L;
    private static final Map<String, Map<String, MeasureUnit>> cache = new HashMap<String, Map<String, MeasureUnit>>();
    private static boolean cacheIsPopulated = false;
    @Deprecated
    protected final String type;
    @Deprecated
    protected final String subType;
    static final UnicodeSet ASCII = new UnicodeSet(97, 122).freeze();
    static final UnicodeSet ASCII_HYPHEN_DIGITS = new UnicodeSet(45, 45, 48, 57, 97, 122).freeze();
    private static Factory UNIT_FACTORY = new Factory(){

        @Override
        public MeasureUnit create(String type, String subType) {
            return new MeasureUnit(type, subType);
        }
    };
    static Factory CURRENCY_FACTORY = new Factory(){

        @Override
        public MeasureUnit create(String unusedType, String subType) {
            return new Currency(subType);
        }
    };
    static Factory TIMEUNIT_FACTORY = new Factory(){

        @Override
        public MeasureUnit create(String type, String subType) {
            return new TimeUnit(type, subType);
        }
    };
    static Factory NOUNIT_FACTORY = new Factory(){

        @Override
        public MeasureUnit create(String type, String subType) {
            return new NoUnit(subType);
        }
    };
    public static final MeasureUnit G_FORCE = MeasureUnit.internalGetInstance("acceleration", "g-force");
    public static final MeasureUnit METER_PER_SECOND_SQUARED = MeasureUnit.internalGetInstance("acceleration", "meter-per-second-squared");
    public static final MeasureUnit ARC_MINUTE = MeasureUnit.internalGetInstance("angle", "arc-minute");
    public static final MeasureUnit ARC_SECOND = MeasureUnit.internalGetInstance("angle", "arc-second");
    public static final MeasureUnit DEGREE = MeasureUnit.internalGetInstance("angle", "degree");
    public static final MeasureUnit RADIAN = MeasureUnit.internalGetInstance("angle", "radian");
    public static final MeasureUnit REVOLUTION_ANGLE = MeasureUnit.internalGetInstance("angle", "revolution");
    public static final MeasureUnit ACRE = MeasureUnit.internalGetInstance("area", "acre");
    public static final MeasureUnit HECTARE = MeasureUnit.internalGetInstance("area", "hectare");
    public static final MeasureUnit SQUARE_CENTIMETER = MeasureUnit.internalGetInstance("area", "square-centimeter");
    public static final MeasureUnit SQUARE_FOOT = MeasureUnit.internalGetInstance("area", "square-foot");
    public static final MeasureUnit SQUARE_INCH = MeasureUnit.internalGetInstance("area", "square-inch");
    public static final MeasureUnit SQUARE_KILOMETER = MeasureUnit.internalGetInstance("area", "square-kilometer");
    public static final MeasureUnit SQUARE_METER = MeasureUnit.internalGetInstance("area", "square-meter");
    public static final MeasureUnit SQUARE_MILE = MeasureUnit.internalGetInstance("area", "square-mile");
    public static final MeasureUnit SQUARE_YARD = MeasureUnit.internalGetInstance("area", "square-yard");
    public static final MeasureUnit KARAT = MeasureUnit.internalGetInstance("concentr", "karat");
    public static final MeasureUnit MILLIGRAM_PER_DECILITER = MeasureUnit.internalGetInstance("concentr", "milligram-per-deciliter");
    public static final MeasureUnit MILLIMOLE_PER_LITER = MeasureUnit.internalGetInstance("concentr", "millimole-per-liter");
    public static final MeasureUnit PART_PER_MILLION = MeasureUnit.internalGetInstance("concentr", "part-per-million");
    public static final MeasureUnit LITER_PER_100KILOMETERS = MeasureUnit.internalGetInstance("consumption", "liter-per-100kilometers");
    public static final MeasureUnit LITER_PER_KILOMETER = MeasureUnit.internalGetInstance("consumption", "liter-per-kilometer");
    public static final MeasureUnit MILE_PER_GALLON = MeasureUnit.internalGetInstance("consumption", "mile-per-gallon");
    public static final MeasureUnit MILE_PER_GALLON_IMPERIAL = MeasureUnit.internalGetInstance("consumption", "mile-per-gallon-imperial");
    public static final MeasureUnit BIT = MeasureUnit.internalGetInstance("digital", "bit");
    public static final MeasureUnit BYTE = MeasureUnit.internalGetInstance("digital", "byte");
    public static final MeasureUnit GIGABIT = MeasureUnit.internalGetInstance("digital", "gigabit");
    public static final MeasureUnit GIGABYTE = MeasureUnit.internalGetInstance("digital", "gigabyte");
    public static final MeasureUnit KILOBIT = MeasureUnit.internalGetInstance("digital", "kilobit");
    public static final MeasureUnit KILOBYTE = MeasureUnit.internalGetInstance("digital", "kilobyte");
    public static final MeasureUnit MEGABIT = MeasureUnit.internalGetInstance("digital", "megabit");
    public static final MeasureUnit MEGABYTE = MeasureUnit.internalGetInstance("digital", "megabyte");
    public static final MeasureUnit TERABIT = MeasureUnit.internalGetInstance("digital", "terabit");
    public static final MeasureUnit TERABYTE = MeasureUnit.internalGetInstance("digital", "terabyte");
    public static final MeasureUnit CENTURY = MeasureUnit.internalGetInstance("duration", "century");
    public static final TimeUnit DAY = (TimeUnit)MeasureUnit.internalGetInstance("duration", "day");
    public static final TimeUnit HOUR = (TimeUnit)MeasureUnit.internalGetInstance("duration", "hour");
    public static final MeasureUnit MICROSECOND = MeasureUnit.internalGetInstance("duration", "microsecond");
    public static final MeasureUnit MILLISECOND = MeasureUnit.internalGetInstance("duration", "millisecond");
    public static final TimeUnit MINUTE = (TimeUnit)MeasureUnit.internalGetInstance("duration", "minute");
    public static final TimeUnit MONTH = (TimeUnit)MeasureUnit.internalGetInstance("duration", "month");
    public static final MeasureUnit NANOSECOND = MeasureUnit.internalGetInstance("duration", "nanosecond");
    public static final TimeUnit SECOND = (TimeUnit)MeasureUnit.internalGetInstance("duration", "second");
    public static final TimeUnit WEEK = (TimeUnit)MeasureUnit.internalGetInstance("duration", "week");
    public static final TimeUnit YEAR = (TimeUnit)MeasureUnit.internalGetInstance("duration", "year");
    public static final MeasureUnit AMPERE = MeasureUnit.internalGetInstance("electric", "ampere");
    public static final MeasureUnit MILLIAMPERE = MeasureUnit.internalGetInstance("electric", "milliampere");
    public static final MeasureUnit OHM = MeasureUnit.internalGetInstance("electric", "ohm");
    public static final MeasureUnit VOLT = MeasureUnit.internalGetInstance("electric", "volt");
    public static final MeasureUnit CALORIE = MeasureUnit.internalGetInstance("energy", "calorie");
    public static final MeasureUnit FOODCALORIE = MeasureUnit.internalGetInstance("energy", "foodcalorie");
    public static final MeasureUnit JOULE = MeasureUnit.internalGetInstance("energy", "joule");
    public static final MeasureUnit KILOCALORIE = MeasureUnit.internalGetInstance("energy", "kilocalorie");
    public static final MeasureUnit KILOJOULE = MeasureUnit.internalGetInstance("energy", "kilojoule");
    public static final MeasureUnit KILOWATT_HOUR = MeasureUnit.internalGetInstance("energy", "kilowatt-hour");
    public static final MeasureUnit GIGAHERTZ = MeasureUnit.internalGetInstance("frequency", "gigahertz");
    public static final MeasureUnit HERTZ = MeasureUnit.internalGetInstance("frequency", "hertz");
    public static final MeasureUnit KILOHERTZ = MeasureUnit.internalGetInstance("frequency", "kilohertz");
    public static final MeasureUnit MEGAHERTZ = MeasureUnit.internalGetInstance("frequency", "megahertz");
    public static final MeasureUnit ASTRONOMICAL_UNIT = MeasureUnit.internalGetInstance("length", "astronomical-unit");
    public static final MeasureUnit CENTIMETER = MeasureUnit.internalGetInstance("length", "centimeter");
    public static final MeasureUnit DECIMETER = MeasureUnit.internalGetInstance("length", "decimeter");
    public static final MeasureUnit FATHOM = MeasureUnit.internalGetInstance("length", "fathom");
    public static final MeasureUnit FOOT = MeasureUnit.internalGetInstance("length", "foot");
    public static final MeasureUnit FURLONG = MeasureUnit.internalGetInstance("length", "furlong");
    public static final MeasureUnit INCH = MeasureUnit.internalGetInstance("length", "inch");
    public static final MeasureUnit KILOMETER = MeasureUnit.internalGetInstance("length", "kilometer");
    public static final MeasureUnit LIGHT_YEAR = MeasureUnit.internalGetInstance("length", "light-year");
    public static final MeasureUnit METER = MeasureUnit.internalGetInstance("length", "meter");
    public static final MeasureUnit MICROMETER = MeasureUnit.internalGetInstance("length", "micrometer");
    public static final MeasureUnit MILE = MeasureUnit.internalGetInstance("length", "mile");
    public static final MeasureUnit MILE_SCANDINAVIAN = MeasureUnit.internalGetInstance("length", "mile-scandinavian");
    public static final MeasureUnit MILLIMETER = MeasureUnit.internalGetInstance("length", "millimeter");
    public static final MeasureUnit NANOMETER = MeasureUnit.internalGetInstance("length", "nanometer");
    public static final MeasureUnit NAUTICAL_MILE = MeasureUnit.internalGetInstance("length", "nautical-mile");
    public static final MeasureUnit PARSEC = MeasureUnit.internalGetInstance("length", "parsec");
    public static final MeasureUnit PICOMETER = MeasureUnit.internalGetInstance("length", "picometer");
    public static final MeasureUnit POINT = MeasureUnit.internalGetInstance("length", "point");
    public static final MeasureUnit YARD = MeasureUnit.internalGetInstance("length", "yard");
    public static final MeasureUnit LUX = MeasureUnit.internalGetInstance("light", "lux");
    public static final MeasureUnit CARAT = MeasureUnit.internalGetInstance("mass", "carat");
    public static final MeasureUnit GRAM = MeasureUnit.internalGetInstance("mass", "gram");
    public static final MeasureUnit KILOGRAM = MeasureUnit.internalGetInstance("mass", "kilogram");
    public static final MeasureUnit METRIC_TON = MeasureUnit.internalGetInstance("mass", "metric-ton");
    public static final MeasureUnit MICROGRAM = MeasureUnit.internalGetInstance("mass", "microgram");
    public static final MeasureUnit MILLIGRAM = MeasureUnit.internalGetInstance("mass", "milligram");
    public static final MeasureUnit OUNCE = MeasureUnit.internalGetInstance("mass", "ounce");
    public static final MeasureUnit OUNCE_TROY = MeasureUnit.internalGetInstance("mass", "ounce-troy");
    public static final MeasureUnit POUND = MeasureUnit.internalGetInstance("mass", "pound");
    public static final MeasureUnit STONE = MeasureUnit.internalGetInstance("mass", "stone");
    public static final MeasureUnit TON = MeasureUnit.internalGetInstance("mass", "ton");
    public static final MeasureUnit GIGAWATT = MeasureUnit.internalGetInstance("power", "gigawatt");
    public static final MeasureUnit HORSEPOWER = MeasureUnit.internalGetInstance("power", "horsepower");
    public static final MeasureUnit KILOWATT = MeasureUnit.internalGetInstance("power", "kilowatt");
    public static final MeasureUnit MEGAWATT = MeasureUnit.internalGetInstance("power", "megawatt");
    public static final MeasureUnit MILLIWATT = MeasureUnit.internalGetInstance("power", "milliwatt");
    public static final MeasureUnit WATT = MeasureUnit.internalGetInstance("power", "watt");
    public static final MeasureUnit HECTOPASCAL = MeasureUnit.internalGetInstance("pressure", "hectopascal");
    public static final MeasureUnit INCH_HG = MeasureUnit.internalGetInstance("pressure", "inch-hg");
    public static final MeasureUnit MILLIBAR = MeasureUnit.internalGetInstance("pressure", "millibar");
    public static final MeasureUnit MILLIMETER_OF_MERCURY = MeasureUnit.internalGetInstance("pressure", "millimeter-of-mercury");
    public static final MeasureUnit POUND_PER_SQUARE_INCH = MeasureUnit.internalGetInstance("pressure", "pound-per-square-inch");
    public static final MeasureUnit KILOMETER_PER_HOUR = MeasureUnit.internalGetInstance("speed", "kilometer-per-hour");
    public static final MeasureUnit KNOT = MeasureUnit.internalGetInstance("speed", "knot");
    public static final MeasureUnit METER_PER_SECOND = MeasureUnit.internalGetInstance("speed", "meter-per-second");
    public static final MeasureUnit MILE_PER_HOUR = MeasureUnit.internalGetInstance("speed", "mile-per-hour");
    public static final MeasureUnit CELSIUS = MeasureUnit.internalGetInstance("temperature", "celsius");
    public static final MeasureUnit FAHRENHEIT = MeasureUnit.internalGetInstance("temperature", "fahrenheit");
    public static final MeasureUnit GENERIC_TEMPERATURE = MeasureUnit.internalGetInstance("temperature", "generic");
    public static final MeasureUnit KELVIN = MeasureUnit.internalGetInstance("temperature", "kelvin");
    public static final MeasureUnit ACRE_FOOT = MeasureUnit.internalGetInstance("volume", "acre-foot");
    public static final MeasureUnit BUSHEL = MeasureUnit.internalGetInstance("volume", "bushel");
    public static final MeasureUnit CENTILITER = MeasureUnit.internalGetInstance("volume", "centiliter");
    public static final MeasureUnit CUBIC_CENTIMETER = MeasureUnit.internalGetInstance("volume", "cubic-centimeter");
    public static final MeasureUnit CUBIC_FOOT = MeasureUnit.internalGetInstance("volume", "cubic-foot");
    public static final MeasureUnit CUBIC_INCH = MeasureUnit.internalGetInstance("volume", "cubic-inch");
    public static final MeasureUnit CUBIC_KILOMETER = MeasureUnit.internalGetInstance("volume", "cubic-kilometer");
    public static final MeasureUnit CUBIC_METER = MeasureUnit.internalGetInstance("volume", "cubic-meter");
    public static final MeasureUnit CUBIC_MILE = MeasureUnit.internalGetInstance("volume", "cubic-mile");
    public static final MeasureUnit CUBIC_YARD = MeasureUnit.internalGetInstance("volume", "cubic-yard");
    public static final MeasureUnit CUP = MeasureUnit.internalGetInstance("volume", "cup");
    public static final MeasureUnit CUP_METRIC = MeasureUnit.internalGetInstance("volume", "cup-metric");
    public static final MeasureUnit DECILITER = MeasureUnit.internalGetInstance("volume", "deciliter");
    public static final MeasureUnit FLUID_OUNCE = MeasureUnit.internalGetInstance("volume", "fluid-ounce");
    public static final MeasureUnit GALLON = MeasureUnit.internalGetInstance("volume", "gallon");
    public static final MeasureUnit GALLON_IMPERIAL = MeasureUnit.internalGetInstance("volume", "gallon-imperial");
    public static final MeasureUnit HECTOLITER = MeasureUnit.internalGetInstance("volume", "hectoliter");
    public static final MeasureUnit LITER = MeasureUnit.internalGetInstance("volume", "liter");
    public static final MeasureUnit MEGALITER = MeasureUnit.internalGetInstance("volume", "megaliter");
    public static final MeasureUnit MILLILITER = MeasureUnit.internalGetInstance("volume", "milliliter");
    public static final MeasureUnit PINT = MeasureUnit.internalGetInstance("volume", "pint");
    public static final MeasureUnit PINT_METRIC = MeasureUnit.internalGetInstance("volume", "pint-metric");
    public static final MeasureUnit QUART = MeasureUnit.internalGetInstance("volume", "quart");
    public static final MeasureUnit TABLESPOON = MeasureUnit.internalGetInstance("volume", "tablespoon");
    public static final MeasureUnit TEASPOON = MeasureUnit.internalGetInstance("volume", "teaspoon");
    private static HashMap<Pair<MeasureUnit, MeasureUnit>, MeasureUnit> unitPerUnitToSingleUnit = new HashMap();

    @Deprecated
    protected MeasureUnit(String type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    public String getType() {
        return this.type;
    }

    public String getSubtype() {
        return this.subType;
    }

    public int hashCode() {
        return 31 * this.type.hashCode() + this.subType.hashCode();
    }

    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        if (!(rhs instanceof MeasureUnit)) {
            return false;
        }
        MeasureUnit c2 = (MeasureUnit)rhs;
        return this.type.equals(c2.type) && this.subType.equals(c2.subType);
    }

    public String toString() {
        return this.type + "-" + this.subType;
    }

    public static synchronized Set<String> getAvailableTypes() {
        MeasureUnit.populateCache();
        return Collections.unmodifiableSet(cache.keySet());
    }

    public static synchronized Set<MeasureUnit> getAvailable(String type) {
        MeasureUnit.populateCache();
        Map<String, MeasureUnit> units = cache.get(type);
        return units == null ? Collections.emptySet() : Collections.unmodifiableSet(new CollectionSet<MeasureUnit>(units.values()));
    }

    public static synchronized Set<MeasureUnit> getAvailable() {
        HashSet<MeasureUnit> result = new HashSet<MeasureUnit>();
        for (String type : new HashSet<String>(MeasureUnit.getAvailableTypes())) {
            for (MeasureUnit unit : MeasureUnit.getAvailable(type)) {
                result.add(unit);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Deprecated
    public static MeasureUnit internalGetInstance(String type, String subType) {
        if (type == null || subType == null) {
            throw new NullPointerException("Type and subType must be non-null");
        }
        if (!("currency".equals(type) || ASCII.containsAll(type) && ASCII_HYPHEN_DIGITS.containsAll(subType))) {
            throw new IllegalArgumentException("The type or subType are invalid.");
        }
        Factory factory = "currency".equals(type) ? CURRENCY_FACTORY : ("duration".equals(type) ? TIMEUNIT_FACTORY : ("none".equals(type) ? NOUNIT_FACTORY : UNIT_FACTORY));
        return MeasureUnit.addUnit(type, subType, factory);
    }

    @Deprecated
    public static MeasureUnit resolveUnitPerUnit(MeasureUnit unit, MeasureUnit perUnit) {
        return unitPerUnitToSingleUnit.get(Pair.of(unit, perUnit));
    }

    private static void populateCache() {
        if (cacheIsPopulated) {
            return;
        }
        cacheIsPopulated = true;
        ICUResourceBundle rb1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", "en");
        rb1.getAllItemsWithFallback("units", new MeasureUnitSink());
        ICUResourceBundle rb2 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "currencyNumericCodes", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        rb2.getAllItemsWithFallback("codeMap", new CurrencyNumericCodeSink());
    }

    @Deprecated
    protected static synchronized MeasureUnit addUnit(String type, String unitName, Factory factory) {
        Map<String, MeasureUnit> tmp = cache.get(type);
        if (tmp == null) {
            tmp = new HashMap<String, MeasureUnit>();
            cache.put(type, tmp);
        } else {
            type = tmp.entrySet().iterator().next().getValue().type;
        }
        MeasureUnit unit = tmp.get(unitName);
        if (unit == null) {
            unit = factory.create(type, unitName);
            tmp.put(unitName, unit);
        }
        return unit;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MeasureUnitProxy(this.type, this.subType);
    }

    static {
        unitPerUnitToSingleUnit.put(Pair.of(LITER, KILOMETER), LITER_PER_KILOMETER);
        unitPerUnitToSingleUnit.put(Pair.of(POUND, SQUARE_INCH), POUND_PER_SQUARE_INCH);
        unitPerUnitToSingleUnit.put(Pair.of(MILE, HOUR), MILE_PER_HOUR);
        unitPerUnitToSingleUnit.put(Pair.of(MILLIGRAM, DECILITER), MILLIGRAM_PER_DECILITER);
        unitPerUnitToSingleUnit.put(Pair.of(MILE, GALLON_IMPERIAL), MILE_PER_GALLON_IMPERIAL);
        unitPerUnitToSingleUnit.put(Pair.of(KILOMETER, HOUR), KILOMETER_PER_HOUR);
        unitPerUnitToSingleUnit.put(Pair.of(MILE, GALLON), MILE_PER_GALLON);
        unitPerUnitToSingleUnit.put(Pair.of(METER, SECOND), METER_PER_SECOND);
    }

    static final class MeasureUnitProxy
    implements Externalizable {
        private static final long serialVersionUID = -3910681415330989598L;
        private String type;
        private String subType;

        public MeasureUnitProxy(String type, String subType) {
            this.type = type;
            this.subType = subType;
        }

        public MeasureUnitProxy() {
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(0);
            out.writeUTF(this.type);
            out.writeUTF(this.subType);
            out.writeShort(0);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            in.readByte();
            this.type = in.readUTF();
            this.subType = in.readUTF();
            short extra = in.readShort();
            if (extra > 0) {
                byte[] extraBytes = new byte[extra];
                in.read(extraBytes, 0, extra);
            }
        }

        private Object readResolve() throws ObjectStreamException {
            return MeasureUnit.internalGetInstance(this.type, this.subType);
        }
    }

    private static final class CurrencyNumericCodeSink
    extends UResource.Sink {
        private CurrencyNumericCodeSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table codesTable = value.getTable();
            int i1 = 0;
            while (codesTable.getKeyAndValue(i1, key, value)) {
                MeasureUnit.internalGetInstance("currency", key.toString());
                ++i1;
            }
        }
    }

    private static final class MeasureUnitSink
    extends UResource.Sink {
        private MeasureUnitSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table unitTypesTable = value.getTable();
            int i2 = 0;
            while (unitTypesTable.getKeyAndValue(i2, key, value)) {
                if (!key.contentEquals("compound") && !key.contentEquals("coordinate")) {
                    String unitType = key.toString();
                    UResource.Table unitNamesTable = value.getTable();
                    int i3 = 0;
                    while (unitNamesTable.getKeyAndValue(i3, key, value)) {
                        String unitName = key.toString();
                        MeasureUnit.internalGetInstance(unitType, unitName);
                        ++i3;
                    }
                }
                ++i2;
            }
        }
    }

    @Deprecated
    protected static interface Factory {
        @Deprecated
        public MeasureUnit create(String var1, String var2);
    }
}

