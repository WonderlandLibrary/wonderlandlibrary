/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationValue;
import com.fasterxml.jackson.annotation.Nulls;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonSetter {
    public String value() default "";

    public Nulls nulls() default Nulls.DEFAULT;

    public Nulls contentNulls() default Nulls.DEFAULT;

    public static class Value
    implements JacksonAnnotationValue<JsonSetter>,
    Serializable {
        private static final long serialVersionUID = 1L;
        private final Nulls _nulls;
        private final Nulls _contentNulls;
        protected static final Value EMPTY = new Value(Nulls.DEFAULT, Nulls.DEFAULT);

        protected Value(Nulls nulls, Nulls contentNulls) {
            this._nulls = nulls;
            this._contentNulls = contentNulls;
        }

        @Override
        public Class<JsonSetter> valueFor() {
            return JsonSetter.class;
        }

        protected Object readResolve() {
            if (Value._empty(this._nulls, this._contentNulls)) {
                return EMPTY;
            }
            return this;
        }

        public static Value from(JsonSetter src) {
            if (src == null) {
                return EMPTY;
            }
            return Value.construct(src.nulls(), src.contentNulls());
        }

        public static Value construct(Nulls nulls, Nulls contentNulls) {
            if (nulls == null) {
                nulls = Nulls.DEFAULT;
            }
            if (contentNulls == null) {
                contentNulls = Nulls.DEFAULT;
            }
            if (Value._empty(nulls, contentNulls)) {
                return EMPTY;
            }
            return new Value(nulls, contentNulls);
        }

        public static Value empty() {
            return EMPTY;
        }

        public static Value merge(Value base, Value overrides) {
            return base == null ? overrides : base.withOverrides(overrides);
        }

        public static Value forValueNulls(Nulls nulls) {
            return Value.construct(nulls, Nulls.DEFAULT);
        }

        public static Value forValueNulls(Nulls nulls, Nulls contentNulls) {
            return Value.construct(nulls, contentNulls);
        }

        public static Value forContentNulls(Nulls nulls) {
            return Value.construct(Nulls.DEFAULT, nulls);
        }

        public Value withOverrides(Value overrides) {
            if (overrides == null || overrides == EMPTY) {
                return this;
            }
            Nulls nulls = overrides._nulls;
            Nulls contentNulls = overrides._contentNulls;
            if (nulls == Nulls.DEFAULT) {
                nulls = this._nulls;
            }
            if (contentNulls == Nulls.DEFAULT) {
                contentNulls = this._contentNulls;
            }
            if (nulls == this._nulls && contentNulls == this._contentNulls) {
                return this;
            }
            return Value.construct(nulls, contentNulls);
        }

        public Value withValueNulls(Nulls nulls) {
            if (nulls == null) {
                nulls = Nulls.DEFAULT;
            }
            if (nulls == this._nulls) {
                return this;
            }
            return Value.construct(nulls, this._contentNulls);
        }

        public Value withValueNulls(Nulls valueNulls, Nulls contentNulls) {
            if (valueNulls == null) {
                valueNulls = Nulls.DEFAULT;
            }
            if (contentNulls == null) {
                contentNulls = Nulls.DEFAULT;
            }
            if (valueNulls == this._nulls && contentNulls == this._contentNulls) {
                return this;
            }
            return Value.construct(valueNulls, contentNulls);
        }

        public Value withContentNulls(Nulls nulls) {
            if (nulls == null) {
                nulls = Nulls.DEFAULT;
            }
            if (nulls == this._contentNulls) {
                return this;
            }
            return Value.construct(this._nulls, nulls);
        }

        public Nulls getValueNulls() {
            return this._nulls;
        }

        public Nulls getContentNulls() {
            return this._contentNulls;
        }

        public Nulls nonDefaultValueNulls() {
            return this._nulls == Nulls.DEFAULT ? null : this._nulls;
        }

        public Nulls nonDefaultContentNulls() {
            return this._contentNulls == Nulls.DEFAULT ? null : this._contentNulls;
        }

        public String toString() {
            return String.format("JsonSetter.Value(valueNulls=%s,contentNulls=%s)", new Object[]{this._nulls, this._contentNulls});
        }

        public int hashCode() {
            return this._nulls.ordinal() + (this._contentNulls.ordinal() << 2);
        }

        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            if (o2 == null) {
                return false;
            }
            if (o2.getClass() == this.getClass()) {
                Value other = (Value)o2;
                return other._nulls == this._nulls && other._contentNulls == this._contentNulls;
            }
            return false;
        }

        private static boolean _empty(Nulls nulls, Nulls contentNulls) {
            return nulls == Nulls.DEFAULT && contentNulls == Nulls.DEFAULT;
        }
    }
}

