/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationValue;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.TimeZone;

@Target(value={ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonFormat {
    public static final String DEFAULT_LOCALE = "##default";
    public static final String DEFAULT_TIMEZONE = "##default";

    public String pattern() default "";

    public Shape shape() default Shape.ANY;

    public String locale() default "##default";

    public String timezone() default "##default";

    public OptBoolean lenient() default OptBoolean.DEFAULT;

    public Feature[] with() default {};

    public Feature[] without() default {};

    public static class Value
    implements JacksonAnnotationValue<JsonFormat>,
    Serializable {
        private static final long serialVersionUID = 1L;
        private static final Value EMPTY = new Value();
        private final String _pattern;
        private final Shape _shape;
        private final Locale _locale;
        private final String _timezoneStr;
        private final Boolean _lenient;
        private final Features _features;
        private transient TimeZone _timezone;

        public Value() {
            this("", Shape.ANY, "", "", Features.empty(), null);
        }

        public Value(JsonFormat ann) {
            this(ann.pattern(), ann.shape(), ann.locale(), ann.timezone(), Features.construct(ann), ann.lenient().asBoolean());
        }

        public Value(String p2, Shape sh, String localeStr, String tzStr, Features f2, Boolean lenient) {
            this(p2, sh, localeStr == null || localeStr.length() == 0 || "##default".equals(localeStr) ? null : new Locale(localeStr), tzStr == null || tzStr.length() == 0 || "##default".equals(tzStr) ? null : tzStr, null, f2, lenient);
        }

        public Value(String p2, Shape sh, Locale l2, TimeZone tz, Features f2, Boolean lenient) {
            this._pattern = p2 == null ? "" : p2;
            this._shape = sh == null ? Shape.ANY : sh;
            this._locale = l2;
            this._timezone = tz;
            this._timezoneStr = null;
            this._features = f2 == null ? Features.empty() : f2;
            this._lenient = lenient;
        }

        public Value(String p2, Shape sh, Locale l2, String tzStr, TimeZone tz, Features f2, Boolean lenient) {
            this._pattern = p2 == null ? "" : p2;
            this._shape = sh == null ? Shape.ANY : sh;
            this._locale = l2;
            this._timezone = tz;
            this._timezoneStr = tzStr;
            this._features = f2 == null ? Features.empty() : f2;
            this._lenient = lenient;
        }

        @Deprecated
        public Value(String p2, Shape sh, Locale l2, String tzStr, TimeZone tz, Features f2) {
            this(p2, sh, l2, tzStr, tz, f2, null);
        }

        @Deprecated
        public Value(String p2, Shape sh, String localeStr, String tzStr, Features f2) {
            this(p2, sh, localeStr, tzStr, f2, null);
        }

        @Deprecated
        public Value(String p2, Shape sh, Locale l2, TimeZone tz, Features f2) {
            this(p2, sh, l2, tz, f2, null);
        }

        public static final Value empty() {
            return EMPTY;
        }

        public static Value merge(Value base, Value overrides) {
            return base == null ? overrides : base.withOverrides(overrides);
        }

        public static Value mergeAll(Value ... values) {
            Value result = null;
            for (Value curr : values) {
                if (curr == null) continue;
                result = result == null ? curr : result.withOverrides(curr);
            }
            return result;
        }

        public static final Value from(JsonFormat ann) {
            return ann == null ? EMPTY : new Value(ann);
        }

        public final Value withOverrides(Value overrides) {
            TimeZone tz;
            String tzStr;
            Features f2;
            Locale l2;
            Shape sh;
            if (overrides == null || overrides == EMPTY || overrides == this) {
                return this;
            }
            if (this == EMPTY) {
                return overrides;
            }
            String p2 = overrides._pattern;
            if (p2 == null || p2.isEmpty()) {
                p2 = this._pattern;
            }
            if ((sh = overrides._shape) == Shape.ANY) {
                sh = this._shape;
            }
            if ((l2 = overrides._locale) == null) {
                l2 = this._locale;
            }
            f2 = (f2 = this._features) == null ? overrides._features : f2.withOverrides(overrides._features);
            Boolean lenient = overrides._lenient;
            if (lenient == null) {
                lenient = this._lenient;
            }
            if ((tzStr = overrides._timezoneStr) == null || tzStr.isEmpty()) {
                tzStr = this._timezoneStr;
                tz = this._timezone;
            } else {
                tz = overrides._timezone;
            }
            return new Value(p2, sh, l2, tzStr, tz, f2, lenient);
        }

        public static Value forPattern(String p2) {
            return new Value(p2, null, null, null, null, Features.empty(), null);
        }

        public static Value forShape(Shape sh) {
            return new Value("", sh, null, null, null, Features.empty(), null);
        }

        public static Value forLeniency(boolean lenient) {
            return new Value("", null, null, null, null, Features.empty(), lenient);
        }

        public Value withPattern(String p2) {
            return new Value(p2, this._shape, this._locale, this._timezoneStr, this._timezone, this._features, this._lenient);
        }

        public Value withShape(Shape s2) {
            if (s2 == this._shape) {
                return this;
            }
            return new Value(this._pattern, s2, this._locale, this._timezoneStr, this._timezone, this._features, this._lenient);
        }

        public Value withLocale(Locale l2) {
            return new Value(this._pattern, this._shape, l2, this._timezoneStr, this._timezone, this._features, this._lenient);
        }

        public Value withTimeZone(TimeZone tz) {
            return new Value(this._pattern, this._shape, this._locale, null, tz, this._features, this._lenient);
        }

        public Value withLenient(Boolean lenient) {
            if (lenient == this._lenient) {
                return this;
            }
            return new Value(this._pattern, this._shape, this._locale, this._timezoneStr, this._timezone, this._features, lenient);
        }

        public Value withFeature(Feature f2) {
            Features newFeats = this._features.with(f2);
            return newFeats == this._features ? this : new Value(this._pattern, this._shape, this._locale, this._timezoneStr, this._timezone, newFeats, this._lenient);
        }

        public Value withoutFeature(Feature f2) {
            Features newFeats = this._features.without(f2);
            return newFeats == this._features ? this : new Value(this._pattern, this._shape, this._locale, this._timezoneStr, this._timezone, newFeats, this._lenient);
        }

        @Override
        public Class<JsonFormat> valueFor() {
            return JsonFormat.class;
        }

        public String getPattern() {
            return this._pattern;
        }

        public Shape getShape() {
            return this._shape;
        }

        public Locale getLocale() {
            return this._locale;
        }

        public Boolean getLenient() {
            return this._lenient;
        }

        public boolean isLenient() {
            return Boolean.TRUE.equals(this._lenient);
        }

        public String timeZoneAsString() {
            if (this._timezone != null) {
                return this._timezone.getID();
            }
            return this._timezoneStr;
        }

        public TimeZone getTimeZone() {
            TimeZone tz = this._timezone;
            if (tz == null) {
                if (this._timezoneStr == null) {
                    return null;
                }
                this._timezone = tz = TimeZone.getTimeZone(this._timezoneStr);
            }
            return tz;
        }

        public boolean hasShape() {
            return this._shape != Shape.ANY;
        }

        public boolean hasPattern() {
            return this._pattern != null && this._pattern.length() > 0;
        }

        public boolean hasLocale() {
            return this._locale != null;
        }

        public boolean hasTimeZone() {
            return this._timezone != null || this._timezoneStr != null && !this._timezoneStr.isEmpty();
        }

        public boolean hasLenient() {
            return this._lenient != null;
        }

        public Boolean getFeature(Feature f2) {
            return this._features.get(f2);
        }

        public Features getFeatures() {
            return this._features;
        }

        public String toString() {
            return String.format("JsonFormat.Value(pattern=%s,shape=%s,lenient=%s,locale=%s,timezone=%s,features=%s)", new Object[]{this._pattern, this._shape, this._lenient, this._locale, this._timezoneStr, this._features});
        }

        public int hashCode() {
            int hash;
            int n2 = hash = this._timezoneStr == null ? 1 : this._timezoneStr.hashCode();
            if (this._pattern != null) {
                hash ^= this._pattern.hashCode();
            }
            hash += this._shape.hashCode();
            if (this._lenient != null) {
                hash ^= this._lenient.hashCode();
            }
            if (this._locale != null) {
                hash += this._locale.hashCode();
            }
            return hash ^= this._features.hashCode();
        }

        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            if (o2 == null) {
                return false;
            }
            if (o2.getClass() != this.getClass()) {
                return false;
            }
            Value other = (Value)o2;
            if (this._shape != other._shape || !this._features.equals(other._features)) {
                return false;
            }
            return Value._equal(this._lenient, other._lenient) && Value._equal(this._timezoneStr, other._timezoneStr) && Value._equal(this._pattern, other._pattern) && Value._equal(this._timezone, other._timezone) && Value._equal(this._locale, other._locale);
        }

        private static <T> boolean _equal(T value1, T value2) {
            if (value1 == null) {
                return value2 == null;
            }
            if (value2 == null) {
                return false;
            }
            return value1.equals(value2);
        }
    }

    public static class Features {
        private final int _enabled;
        private final int _disabled;
        private static final Features EMPTY = new Features(0, 0);

        private Features(int e2, int d2) {
            this._enabled = e2;
            this._disabled = d2;
        }

        public static Features empty() {
            return EMPTY;
        }

        public static Features construct(JsonFormat f2) {
            return Features.construct(f2.with(), f2.without());
        }

        public static Features construct(Feature[] enabled, Feature[] disabled) {
            int e2 = 0;
            for (Feature f2 : enabled) {
                e2 |= 1 << f2.ordinal();
            }
            int d2 = 0;
            for (Feature f3 : disabled) {
                d2 |= 1 << f3.ordinal();
            }
            return new Features(e2, d2);
        }

        public Features withOverrides(Features overrides) {
            if (overrides == null) {
                return this;
            }
            int overrideD = overrides._disabled;
            int overrideE = overrides._enabled;
            if (overrideD == 0 && overrideE == 0) {
                return this;
            }
            if (this._enabled == 0 && this._disabled == 0) {
                return overrides;
            }
            int newE = this._enabled & ~overrideD | overrideE;
            int newD = this._disabled & ~overrideE | overrideD;
            if (newE == this._enabled && newD == this._disabled) {
                return this;
            }
            return new Features(newE, newD);
        }

        public Features with(Feature ... features) {
            int e2 = this._enabled;
            for (Feature f2 : features) {
                e2 |= 1 << f2.ordinal();
            }
            return e2 == this._enabled ? this : new Features(e2, this._disabled);
        }

        public Features without(Feature ... features) {
            int d2 = this._disabled;
            for (Feature f2 : features) {
                d2 |= 1 << f2.ordinal();
            }
            return d2 == this._disabled ? this : new Features(this._enabled, d2);
        }

        public Boolean get(Feature f2) {
            int mask = 1 << f2.ordinal();
            if ((this._disabled & mask) != 0) {
                return Boolean.FALSE;
            }
            if ((this._enabled & mask) != 0) {
                return Boolean.TRUE;
            }
            return null;
        }

        public String toString() {
            if (this == EMPTY) {
                return "EMPTY";
            }
            return String.format("(enabled=0x%x,disabled=0x%x)", this._enabled, this._disabled);
        }

        public int hashCode() {
            return this._disabled + this._enabled;
        }

        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            if (o2 == null) {
                return false;
            }
            if (o2.getClass() != this.getClass()) {
                return false;
            }
            Features other = (Features)o2;
            return other._enabled == this._enabled && other._disabled == this._disabled;
        }
    }

    public static enum Feature {
        ACCEPT_SINGLE_VALUE_AS_ARRAY,
        ACCEPT_CASE_INSENSITIVE_PROPERTIES,
        ACCEPT_CASE_INSENSITIVE_VALUES,
        WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
        WRITE_DATES_WITH_ZONE_ID,
        WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
        WRITE_SORTED_MAP_ENTRIES,
        ADJUST_DATES_TO_CONTEXT_TIME_ZONE;

    }

    public static enum Shape {
        ANY,
        NATURAL,
        SCALAR,
        ARRAY,
        OBJECT,
        NUMBER,
        NUMBER_FLOAT,
        NUMBER_INT,
        STRING,
        BOOLEAN,
        BINARY;


        public boolean isNumeric() {
            return this == NUMBER || this == NUMBER_INT || this == NUMBER_FLOAT;
        }

        public boolean isStructured() {
            return this == OBJECT || this == ARRAY;
        }
    }
}

