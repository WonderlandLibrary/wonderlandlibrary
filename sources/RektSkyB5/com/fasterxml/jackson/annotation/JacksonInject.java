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

@Target(value={ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JacksonInject {
    public String value() default "";

    public OptBoolean useInput() default OptBoolean.DEFAULT;

    public static class Value
    implements JacksonAnnotationValue<JacksonInject>,
    Serializable {
        private static final long serialVersionUID = 1L;
        protected static final Value EMPTY = new Value(null, null);
        protected final Object _id;
        protected final Boolean _useInput;

        protected Value(Object id, Boolean useInput) {
            this._id = id;
            this._useInput = useInput;
        }

        @Override
        public Class<JacksonInject> valueFor() {
            return JacksonInject.class;
        }

        public static Value empty() {
            return EMPTY;
        }

        public static Value construct(Object id, Boolean useInput) {
            if ("".equals(id)) {
                id = null;
            }
            if (Value._empty(id, useInput)) {
                return EMPTY;
            }
            return new Value(id, useInput);
        }

        public static Value from(JacksonInject src) {
            if (src == null) {
                return EMPTY;
            }
            return Value.construct(src.value(), src.useInput().asBoolean());
        }

        public static Value forId(Object id) {
            return Value.construct(id, null);
        }

        public Value withId(Object id) {
            if (id == null ? this._id == null : id.equals(this._id)) {
                return this;
            }
            return new Value(id, this._useInput);
        }

        public Value withUseInput(Boolean useInput) {
            if (useInput == null ? this._useInput == null : useInput.equals(this._useInput)) {
                return this;
            }
            return new Value(this._id, useInput);
        }

        public Object getId() {
            return this._id;
        }

        public Boolean getUseInput() {
            return this._useInput;
        }

        public boolean hasId() {
            return this._id != null;
        }

        public boolean willUseInput(boolean defaultSetting) {
            return this._useInput == null ? defaultSetting : this._useInput;
        }

        public String toString() {
            return String.format("JacksonInject.Value(id=%s,useInput=%s)", this._id, this._useInput);
        }

        public int hashCode() {
            int h2 = 1;
            if (this._id != null) {
                h2 += this._id.hashCode();
            }
            if (this._useInput != null) {
                h2 += this._useInput.hashCode();
            }
            return h2;
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
                if (OptBoolean.equals(this._useInput, other._useInput)) {
                    if (this._id == null) {
                        return other._id == null;
                    }
                    return this._id.equals(other._id);
                }
            }
            return false;
        }

        private static boolean _empty(Object id, Boolean useInput) {
            return id == null && useInput == null;
        }
    }
}

