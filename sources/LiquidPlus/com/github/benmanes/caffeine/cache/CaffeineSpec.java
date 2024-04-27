/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CaffeineSpec {
    static final String SPLIT_OPTIONS = ",";
    static final String SPLIT_KEY_VALUE = "=";
    final String specification;
    int initialCapacity = -1;
    long maximumWeight = -1L;
    long maximumSize = -1L;
    boolean recordStats;
    @Nullable Caffeine.Strength keyStrength;
    @Nullable Caffeine.Strength valueStrength;
    @Nullable Duration expireAfterWrite;
    @Nullable Duration expireAfterAccess;
    @Nullable Duration refreshAfterWrite;

    private CaffeineSpec(String specification) {
        this.specification = Objects.requireNonNull(specification);
    }

    Caffeine<Object, Object> toBuilder() {
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        if (this.initialCapacity != -1) {
            builder.initialCapacity(this.initialCapacity);
        }
        if (this.maximumSize != -1L) {
            builder.maximumSize(this.maximumSize);
        }
        if (this.maximumWeight != -1L) {
            builder.maximumWeight(this.maximumWeight);
        }
        if (this.keyStrength != null) {
            Caffeine.requireState(this.keyStrength == Caffeine.Strength.WEAK);
            builder.weakKeys();
        }
        if (this.valueStrength != null) {
            if (this.valueStrength == Caffeine.Strength.WEAK) {
                builder.weakValues();
            } else if (this.valueStrength == Caffeine.Strength.SOFT) {
                builder.softValues();
            } else {
                throw new IllegalStateException();
            }
        }
        if (this.expireAfterWrite != null) {
            builder.expireAfterWrite(this.expireAfterWrite);
        }
        if (this.expireAfterAccess != null) {
            builder.expireAfterAccess(this.expireAfterAccess);
        }
        if (this.refreshAfterWrite != null) {
            builder.refreshAfterWrite(this.refreshAfterWrite);
        }
        if (this.recordStats) {
            builder.recordStats();
        }
        return builder;
    }

    public static @NonNull CaffeineSpec parse(@NonNull String specification) {
        CaffeineSpec spec = new CaffeineSpec(specification);
        for (String option : specification.split(SPLIT_OPTIONS)) {
            spec.parseOption(option.trim());
        }
        return spec;
    }

    void parseOption(String option) {
        if (option.isEmpty()) {
            return;
        }
        String[] keyAndValue = option.split(SPLIT_KEY_VALUE);
        Caffeine.requireArgument(keyAndValue.length <= 2, "key-value pair %s with more than one equals sign", option);
        String key = keyAndValue[0].trim();
        String value = keyAndValue.length == 1 ? null : keyAndValue[1].trim();
        this.configure(key, value);
    }

    void configure(String key, @Nullable String value) {
        switch (key) {
            case "initialCapacity": {
                this.initialCapacity(key, value);
                return;
            }
            case "maximumSize": {
                this.maximumSize(key, value);
                return;
            }
            case "maximumWeight": {
                this.maximumWeight(key, value);
                return;
            }
            case "weakKeys": {
                this.weakKeys(value);
                return;
            }
            case "weakValues": {
                this.valueStrength(key, value, Caffeine.Strength.WEAK);
                return;
            }
            case "softValues": {
                this.valueStrength(key, value, Caffeine.Strength.SOFT);
                return;
            }
            case "expireAfterAccess": {
                this.expireAfterAccess(key, value);
                return;
            }
            case "expireAfterWrite": {
                this.expireAfterWrite(key, value);
                return;
            }
            case "refreshAfterWrite": {
                this.refreshAfterWrite(key, value);
                return;
            }
            case "recordStats": {
                this.recordStats(value);
                return;
            }
        }
        throw new IllegalArgumentException("Unknown key " + key);
    }

    void initialCapacity(String key, @Nullable String value) {
        Caffeine.requireArgument(this.initialCapacity == -1, "initial capacity was already set to %,d", this.initialCapacity);
        this.initialCapacity = CaffeineSpec.parseInt(key, value);
    }

    void maximumSize(String key, @Nullable String value) {
        Caffeine.requireArgument(this.maximumSize == -1L, "maximum size was already set to %,d", this.maximumSize);
        Caffeine.requireArgument(this.maximumWeight == -1L, "maximum weight was already set to %,d", this.maximumWeight);
        this.maximumSize = CaffeineSpec.parseLong(key, value);
    }

    void maximumWeight(String key, @Nullable String value) {
        Caffeine.requireArgument(this.maximumWeight == -1L, "maximum weight was already set to %,d", this.maximumWeight);
        Caffeine.requireArgument(this.maximumSize == -1L, "maximum size was already set to %,d", this.maximumSize);
        this.maximumWeight = CaffeineSpec.parseLong(key, value);
    }

    void weakKeys(@Nullable String value) {
        Caffeine.requireArgument(value == null, "weak keys does not take a value", new Object[0]);
        Caffeine.requireArgument(this.keyStrength == null, "weak keys was already set", new Object[0]);
        this.keyStrength = Caffeine.Strength.WEAK;
    }

    void valueStrength(String key, @Nullable String value, Caffeine.Strength strength) {
        Caffeine.requireArgument(value == null, "%s does not take a value", key);
        Caffeine.requireArgument(this.valueStrength == null, "%s was already set to %s", new Object[]{key, this.valueStrength});
        this.valueStrength = strength;
    }

    void expireAfterAccess(String key, @Nullable String value) {
        Caffeine.requireArgument(this.expireAfterAccess == null, "expireAfterAccess was already set", new Object[0]);
        this.expireAfterAccess = CaffeineSpec.parseDuration(key, value);
    }

    void expireAfterWrite(String key, @Nullable String value) {
        Caffeine.requireArgument(this.expireAfterWrite == null, "expireAfterWrite was already set", new Object[0]);
        this.expireAfterWrite = CaffeineSpec.parseDuration(key, value);
    }

    void refreshAfterWrite(String key, @Nullable String value) {
        Caffeine.requireArgument(this.refreshAfterWrite == null, "refreshAfterWrite was already set", new Object[0]);
        this.refreshAfterWrite = CaffeineSpec.parseDuration(key, value);
    }

    void recordStats(@Nullable String value) {
        Caffeine.requireArgument(value == null, "record stats does not take a value", new Object[0]);
        Caffeine.requireArgument(!this.recordStats, "record stats was already set", new Object[0]);
        this.recordStats = true;
    }

    static int parseInt(String key, @Nullable String value) {
        Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s was omitted", key);
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("key %s value was set to %s, must be an integer", key, value), e);
        }
    }

    static long parseLong(String key, @Nullable String value) {
        Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s was omitted", key);
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("key %s value was set to %s, must be a long", key, value), e);
        }
    }

    static Duration parseDuration(String key, @Nullable String value) {
        boolean isIsoFormat;
        Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
        boolean bl = isIsoFormat = value.contains("p") || value.contains("P");
        if (isIsoFormat) {
            Duration duration = Duration.parse(value);
            Caffeine.requireArgument(!duration.isNegative(), "key %s invalid format; was %s, but the duration cannot be negative", key, value);
            return duration;
        }
        long duration = CaffeineSpec.parseLong(key, value.substring(0, value.length() - 1));
        TimeUnit unit = CaffeineSpec.parseTimeUnit(key, value);
        return Duration.ofNanos(unit.toNanos(duration));
    }

    static TimeUnit parseTimeUnit(String key, @Nullable String value) {
        Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
        char lastChar = Character.toLowerCase(value.charAt(value.length() - 1));
        switch (lastChar) {
            case 'd': {
                return TimeUnit.DAYS;
            }
            case 'h': {
                return TimeUnit.HOURS;
            }
            case 'm': {
                return TimeUnit.MINUTES;
            }
            case 's': {
                return TimeUnit.SECONDS;
            }
        }
        throw new IllegalArgumentException(String.format("key %s invalid format; was %s, must end with one of [dDhHmMsS]", key, value));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaffeineSpec)) {
            return false;
        }
        CaffeineSpec spec = (CaffeineSpec)o;
        return Objects.equals(this.refreshAfterWrite, spec.refreshAfterWrite) && Objects.equals(this.expireAfterAccess, spec.expireAfterAccess) && Objects.equals(this.expireAfterWrite, spec.expireAfterWrite) && this.initialCapacity == spec.initialCapacity && this.maximumWeight == spec.maximumWeight && this.valueStrength == spec.valueStrength && this.keyStrength == spec.keyStrength && this.maximumSize == spec.maximumSize && this.recordStats == spec.recordStats;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.initialCapacity, this.maximumSize, this.maximumWeight, this.keyStrength, this.valueStrength, this.recordStats, this.expireAfterWrite, this.expireAfterAccess, this.refreshAfterWrite});
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return this.getClass().getSimpleName() + '{' + this.toParsableString() + '}';
    }
}

