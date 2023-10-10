// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.Optional;
import java.util.Map;
import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class MoreObjects
{
    public static <T> T firstNonNull(@CheckForNull final T first, final T second) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        throw new NullPointerException("Both parameters are null");
    }
    
    public static ToStringHelper toStringHelper(final Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final String className) {
        return new ToStringHelper(className);
    }
    
    private MoreObjects() {
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;
        private boolean omitEmptyValues;
        
        private ToStringHelper(final String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.omitEmptyValues = false;
            this.className = Preconditions.checkNotNull(className);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, @CheckForNull final Object value) {
            return this.addHolder(name, value);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final boolean value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final char value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final double value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final float value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final int value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String name, final long value) {
            return this.addUnconditionalHolder(name, String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(@CheckForNull final Object value) {
            return this.addHolder(value);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final boolean value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final char value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final double value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final float value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final int value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final long value) {
            return this.addUnconditionalHolder(String.valueOf(value));
        }
        
        private static boolean isEmpty(final Object value) {
            if (value instanceof CharSequence) {
                return ((CharSequence)value).length() == 0;
            }
            if (value instanceof Collection) {
                return ((Collection)value).isEmpty();
            }
            if (value instanceof Map) {
                return ((Map)value).isEmpty();
            }
            if (value instanceof Optional) {
                return !((Optional)value).isPresent();
            }
            if (value instanceof OptionalInt) {
                return !((OptionalInt)value).isPresent();
            }
            if (value instanceof OptionalLong) {
                return !((OptionalLong)value).isPresent();
            }
            if (value instanceof OptionalDouble) {
                return !((OptionalDouble)value).isPresent();
            }
            if (value instanceof com.google.common.base.Optional) {
                return !((com.google.common.base.Optional)value).isPresent();
            }
            return value.getClass().isArray() && Array.getLength(value) == 0;
        }
        
        @Override
        public String toString() {
            final boolean omitNullValuesSnapshot = this.omitNullValues;
            final boolean omitEmptyValuesSnapshot = this.omitEmptyValues;
            String nextSeparator = "";
            final StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                final Object value = valueHolder.value;
                if (!(valueHolder instanceof UnconditionalValueHolder)) {
                    if (value == null) {
                        if (omitNullValuesSnapshot) {
                            continue;
                        }
                    }
                    else if (omitEmptyValuesSnapshot && isEmpty(value)) {
                        continue;
                    }
                }
                builder.append(nextSeparator);
                nextSeparator = ", ";
                if (valueHolder.name != null) {
                    builder.append(valueHolder.name).append('=');
                }
                if (value != null && value.getClass().isArray()) {
                    final Object[] objectArray = { value };
                    final String arrayString = Arrays.deepToString(objectArray);
                    builder.append(arrayString, 1, arrayString.length() - 1);
                }
                else {
                    builder.append(value);
                }
            }
            return builder.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder valueHolder = new ValueHolder();
            final ValueHolder holderTail = this.holderTail;
            final ValueHolder valueHolder2 = valueHolder;
            holderTail.next = valueHolder2;
            this.holderTail = valueHolder2;
            return valueHolder;
        }
        
        private ToStringHelper addHolder(@CheckForNull final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            return this;
        }
        
        private ToStringHelper addHolder(final String name, @CheckForNull final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            valueHolder.name = Preconditions.checkNotNull(name);
            return this;
        }
        
        private UnconditionalValueHolder addUnconditionalHolder() {
            final UnconditionalValueHolder valueHolder = new UnconditionalValueHolder();
            final ValueHolder holderTail = this.holderTail;
            final UnconditionalValueHolder unconditionalValueHolder = valueHolder;
            holderTail.next = unconditionalValueHolder;
            this.holderTail = unconditionalValueHolder;
            return valueHolder;
        }
        
        private ToStringHelper addUnconditionalHolder(final Object value) {
            final UnconditionalValueHolder valueHolder = this.addUnconditionalHolder();
            valueHolder.value = value;
            return this;
        }
        
        private ToStringHelper addUnconditionalHolder(final String name, final Object value) {
            final UnconditionalValueHolder valueHolder = this.addUnconditionalHolder();
            valueHolder.value = value;
            valueHolder.name = Preconditions.checkNotNull(name);
            return this;
        }
        
        private static class ValueHolder
        {
            @CheckForNull
            String name;
            @CheckForNull
            Object value;
            @CheckForNull
            ValueHolder next;
        }
        
        private static final class UnconditionalValueHolder extends ValueHolder
        {
        }
    }
}
