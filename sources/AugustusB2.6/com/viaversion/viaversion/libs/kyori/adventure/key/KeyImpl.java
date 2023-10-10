// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.VisibleForTesting;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.function.IntPredicate;

final class KeyImpl implements Key
{
    static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
    static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";
    private static final IntPredicate NAMESPACE_PREDICATE;
    private static final IntPredicate VALUE_PREDICATE;
    private final String namespace;
    private final String value;
    
    KeyImpl(@NotNull final String namespace, @NotNull final String value) {
        if (!namespaceValid(namespace)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9_.-] character in namespace of Key[%s]", asString(namespace, value)));
        }
        if (!valueValid(value)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9/._-] character in value of Key[%s]", asString(namespace, value)));
        }
        this.namespace = Objects.requireNonNull(namespace, "namespace");
        this.value = Objects.requireNonNull(value, "value");
    }
    
    @VisibleForTesting
    static boolean namespaceValid(@NotNull final String namespace) {
        for (int i = 0, length = namespace.length(); i < length; ++i) {
            if (!KeyImpl.NAMESPACE_PREDICATE.test(namespace.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    @VisibleForTesting
    static boolean valueValid(@NotNull final String value) {
        for (int i = 0, length = value.length(); i < length; ++i) {
            if (!KeyImpl.VALUE_PREDICATE.test(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    @NotNull
    @Override
    public String namespace() {
        return this.namespace;
    }
    
    @NotNull
    @Override
    public String value() {
        return this.value;
    }
    
    @NotNull
    @Override
    public String asString() {
        return asString(this.namespace, this.value);
    }
    
    @NotNull
    private static String asString(@NotNull final String namespace, @NotNull final String value) {
        return namespace + ':' + value;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.asString();
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Key)) {
            return false;
        }
        final Key that = (Key)other;
        return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
    }
    
    @Override
    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @Override
    public int compareTo(@NotNull final Key that) {
        return super.compareTo(that);
    }
    
    static int clampCompare(final int value) {
        if (value < 0) {
            return -1;
        }
        if (value > 0) {
            return 1;
        }
        return value;
    }
    
    static {
        NAMESPACE_PREDICATE = (value -> value == 95 || value == 45 || (value >= 97 && value <= 122) || (value >= 48 && value <= 57) || value == 46);
        VALUE_PREDICATE = (value -> value == 95 || value == 45 || (value >= 97 && value <= 122) || (value >= 48 && value <= 57) || value == 47 || value == 46);
    }
}
