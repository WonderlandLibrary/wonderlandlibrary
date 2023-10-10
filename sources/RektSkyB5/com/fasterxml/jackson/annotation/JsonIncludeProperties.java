/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationValue;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Target(value={ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonIncludeProperties {
    public String[] value() default {};

    public static class Value
    implements JacksonAnnotationValue<JsonIncludeProperties>,
    Serializable {
        private static final long serialVersionUID = 1L;
        protected static final Value ALL = new Value(null);
        protected final Set<String> _included;

        protected Value(Set<String> included) {
            this._included = included;
        }

        public static Value from(JsonIncludeProperties src) {
            if (src == null) {
                return ALL;
            }
            return new Value(Value._asSet(src.value()));
        }

        public static Value all() {
            return ALL;
        }

        @Override
        public Class<JsonIncludeProperties> valueFor() {
            return JsonIncludeProperties.class;
        }

        public Set<String> getIncluded() {
            return this._included;
        }

        public Value withOverrides(Value overrides) {
            Set<String> otherIncluded;
            if (overrides == null || (otherIncluded = overrides.getIncluded()) == null) {
                return this;
            }
            if (this._included == null) {
                return overrides;
            }
            HashSet<String> toInclude = new HashSet<String>();
            for (String incl : otherIncluded) {
                if (!this._included.contains(incl)) continue;
                toInclude.add(incl);
            }
            return new Value(toInclude);
        }

        public String toString() {
            return String.format("JsonIncludeProperties.Value(included=%s)", this._included);
        }

        public int hashCode() {
            return this._included == null ? 0 : this._included.size();
        }

        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            if (o2 == null) {
                return false;
            }
            return o2.getClass() == this.getClass() && Value._equals(this._included, ((Value)o2)._included);
        }

        private static boolean _equals(Set<String> a2, Set<String> b2) {
            return a2 == null ? b2 == null : a2.equals(b2);
        }

        private static Set<String> _asSet(String[] v2) {
            if (v2 == null || v2.length == 0) {
                return Collections.emptySet();
            }
            HashSet<String> s2 = new HashSet<String>(v2.length);
            for (String str : v2) {
                s2.add(str);
            }
            return s2;
        }
    }
}

