// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.Strings;
import java.util.Locale;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Splitter;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class CacheBuilderSpec
{
    private static final Splitter KEYS_SPLITTER;
    private static final Splitter KEY_VALUE_SPLITTER;
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS;
    @CheckForNull
    @VisibleForTesting
    Integer initialCapacity;
    @CheckForNull
    @VisibleForTesting
    Long maximumSize;
    @CheckForNull
    @VisibleForTesting
    Long maximumWeight;
    @CheckForNull
    @VisibleForTesting
    Integer concurrencyLevel;
    @CheckForNull
    @VisibleForTesting
    LocalCache.Strength keyStrength;
    @CheckForNull
    @VisibleForTesting
    LocalCache.Strength valueStrength;
    @CheckForNull
    @VisibleForTesting
    Boolean recordStats;
    @VisibleForTesting
    long writeExpirationDuration;
    @CheckForNull
    @VisibleForTesting
    TimeUnit writeExpirationTimeUnit;
    @VisibleForTesting
    long accessExpirationDuration;
    @CheckForNull
    @VisibleForTesting
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    long refreshDuration;
    @CheckForNull
    @VisibleForTesting
    TimeUnit refreshTimeUnit;
    private final String specification;
    
    private CacheBuilderSpec(final String specification) {
        this.specification = specification;
    }
    
    public static CacheBuilderSpec parse(final String cacheBuilderSpecification) {
        final CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        if (!cacheBuilderSpecification.isEmpty()) {
            for (final String keyValuePair : CacheBuilderSpec.KEYS_SPLITTER.split(cacheBuilderSpecification)) {
                final List<String> keyAndValue = (List<String>)ImmutableList.copyOf((Iterable<?>)CacheBuilderSpec.KEY_VALUE_SPLITTER.split(keyValuePair));
                Preconditions.checkArgument(!keyAndValue.isEmpty(), (Object)"blank key-value pair");
                Preconditions.checkArgument(keyAndValue.size() <= 2, "key-value pair %s with more than one equals sign", keyValuePair);
                final String key = keyAndValue.get(0);
                final ValueParser valueParser = CacheBuilderSpec.VALUE_PARSERS.get(key);
                Preconditions.checkArgument(valueParser != null, "unknown key %s", key);
                final String value = (keyAndValue.size() == 1) ? null : keyAndValue.get(1);
                valueParser.parse(spec, key, value);
            }
        }
        return spec;
    }
    
    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }
    
    CacheBuilder<Object, Object> toCacheBuilder() {
        final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            builder.initialCapacity(this.initialCapacity);
        }
        if (this.maximumSize != null) {
            builder.maximumSize(this.maximumSize);
        }
        if (this.maximumWeight != null) {
            builder.maximumWeight(this.maximumWeight);
        }
        if (this.concurrencyLevel != null) {
            builder.concurrencyLevel(this.concurrencyLevel);
        }
        if (this.keyStrength != null) {
            switch (this.keyStrength) {
                case WEAK: {
                    builder.weakKeys();
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case SOFT: {
                    builder.softValues();
                    break;
                }
                case WEAK: {
                    builder.weakValues();
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        if (this.recordStats != null && this.recordStats) {
            builder.recordStats();
        }
        if (this.writeExpirationTimeUnit != null) {
            builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return builder;
    }
    
    public String toParsableString() {
        return this.specification;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.toParsableString()).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        final CacheBuilderSpec that = (CacheBuilderSpec)obj;
        return Objects.equal(this.initialCapacity, that.initialCapacity) && Objects.equal(this.maximumSize, that.maximumSize) && Objects.equal(this.maximumWeight, that.maximumWeight) && Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && Objects.equal(this.keyStrength, that.keyStrength) && Objects.equal(this.valueStrength, that.valueStrength) && Objects.equal(this.recordStats, that.recordStats) && Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(that.refreshDuration, that.refreshTimeUnit));
    }
    
    @CheckForNull
    private static Long durationInNanos(final long duration, @CheckForNull final TimeUnit unit) {
        return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
    }
    
    private static String format(final String format, final Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
    
    static {
        KEYS_SPLITTER = Splitter.on(',').trimResults();
        KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
        VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", (InitialCapacityParser)new MaximumSizeParser()).put("maximumWeight", (InitialCapacityParser)new MaximumWeightParser()).put("concurrencyLevel", (InitialCapacityParser)new ConcurrencyLevelParser()).put("weakKeys", (InitialCapacityParser)new KeyStrengthParser(LocalCache.Strength.WEAK)).put("softValues", (InitialCapacityParser)new ValueStrengthParser(LocalCache.Strength.SOFT)).put("weakValues", (InitialCapacityParser)new ValueStrengthParser(LocalCache.Strength.WEAK)).put("recordStats", (InitialCapacityParser)new RecordStatsParser()).put("expireAfterAccess", (InitialCapacityParser)new AccessDurationParser()).put("expireAfterWrite", (InitialCapacityParser)new WriteDurationParser()).put("refreshAfterWrite", (InitialCapacityParser)new RefreshDurationParser()).put("refreshInterval", (InitialCapacityParser)new RefreshDurationParser()).build();
    }
    
    abstract static class IntegerParser implements ValueParser
    {
        protected abstract void parseInteger(final CacheBuilderSpec p0, final int p1);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, final String value) {
            if (Strings.isNullOrEmpty(value)) {
                throw new IllegalArgumentException(new StringBuilder(21 + String.valueOf(key).length()).append("value of key ").append(key).append(" omitted").toString());
            }
            try {
                this.parseInteger(spec, Integer.parseInt(value));
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
            }
        }
    }
    
    abstract static class LongParser implements ValueParser
    {
        protected abstract void parseLong(final CacheBuilderSpec p0, final long p1);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, final String value) {
            if (Strings.isNullOrEmpty(value)) {
                throw new IllegalArgumentException(new StringBuilder(21 + String.valueOf(key).length()).append("value of key ").append(key).append(" omitted").toString());
            }
            try {
                this.parseLong(spec, Long.parseLong(value));
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
            }
        }
    }
    
    static class InitialCapacityParser extends IntegerParser
    {
        @Override
        protected void parseInteger(final CacheBuilderSpec spec, final int value) {
            Preconditions.checkArgument(spec.initialCapacity == null, "initial capacity was already set to ", spec.initialCapacity);
            spec.initialCapacity = value;
        }
    }
    
    static class MaximumSizeParser extends LongParser
    {
        @Override
        protected void parseLong(final CacheBuilderSpec spec, final long value) {
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", spec.maximumSize);
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", spec.maximumWeight);
            spec.maximumSize = value;
        }
    }
    
    static class MaximumWeightParser extends LongParser
    {
        @Override
        protected void parseLong(final CacheBuilderSpec spec, final long value) {
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", spec.maximumWeight);
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", spec.maximumSize);
            spec.maximumWeight = value;
        }
    }
    
    static class ConcurrencyLevelParser extends IntegerParser
    {
        @Override
        protected void parseInteger(final CacheBuilderSpec spec, final int value) {
            Preconditions.checkArgument(spec.concurrencyLevel == null, "concurrency level was already set to ", spec.concurrencyLevel);
            spec.concurrencyLevel = value;
        }
    }
    
    static class KeyStrengthParser implements ValueParser
    {
        private final LocalCache.Strength strength;
        
        public KeyStrengthParser(final LocalCache.Strength strength) {
            this.strength = strength;
        }
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @CheckForNull final String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            Preconditions.checkArgument(spec.keyStrength == null, "%s was already set to %s", key, spec.keyStrength);
            spec.keyStrength = this.strength;
        }
    }
    
    static class ValueStrengthParser implements ValueParser
    {
        private final LocalCache.Strength strength;
        
        public ValueStrengthParser(final LocalCache.Strength strength) {
            this.strength = strength;
        }
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @CheckForNull final String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            Preconditions.checkArgument(spec.valueStrength == null, "%s was already set to %s", key, spec.valueStrength);
            spec.valueStrength = this.strength;
        }
    }
    
    static class RecordStatsParser implements ValueParser
    {
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @CheckForNull final String value) {
            Preconditions.checkArgument(value == null, (Object)"recordStats does not take values");
            Preconditions.checkArgument(spec.recordStats == null, (Object)"recordStats already set");
            spec.recordStats = true;
        }
    }
    
    abstract static class DurationParser implements ValueParser
    {
        protected abstract void parseDuration(final CacheBuilderSpec p0, final long p1, final TimeUnit p2);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @CheckForNull final String value) {
            if (Strings.isNullOrEmpty(value)) {
                throw new IllegalArgumentException(new StringBuilder(21 + String.valueOf(key).length()).append("value of key ").append(key).append(" omitted").toString());
            }
            try {
                final char lastChar = value.charAt(value.length() - 1);
                TimeUnit timeUnit = null;
                switch (lastChar) {
                    case 'd': {
                        timeUnit = TimeUnit.DAYS;
                        break;
                    }
                    case 'h': {
                        timeUnit = TimeUnit.HOURS;
                        break;
                    }
                    case 'm': {
                        timeUnit = TimeUnit.MINUTES;
                        break;
                    }
                    case 's': {
                        timeUnit = TimeUnit.SECONDS;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(format("key %s invalid unit: was %s, must end with one of [dhms]", new Object[] { key, value }));
                    }
                }
                final long duration = Long.parseLong(value.substring(0, value.length() - 1));
                this.parseDuration(spec, duration, timeUnit);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(format("key %s value set to %s, must be integer", new Object[] { key, value }));
            }
        }
    }
    
    static class AccessDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.accessExpirationTimeUnit == null, (Object)"expireAfterAccess already set");
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }
    
    static class WriteDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.writeExpirationTimeUnit == null, (Object)"expireAfterWrite already set");
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }
    
    static class RefreshDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.refreshTimeUnit == null, (Object)"refreshAfterWrite already set");
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }
    
    private interface ValueParser
    {
        void parse(final CacheBuilderSpec p0, final String p1, @CheckForNull final String p2);
    }
}
