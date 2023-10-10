// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import javax.annotation.CheckForNull;
import java.util.Arrays;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class CompactHashing
{
    static final byte UNSET = 0;
    private static final int HASH_TABLE_BITS_MAX_BITS = 5;
    static final int MODIFICATION_COUNT_INCREMENT = 32;
    static final int HASH_TABLE_BITS_MASK = 31;
    static final int MAX_SIZE = 1073741823;
    static final int DEFAULT_SIZE = 3;
    private static final int MIN_HASH_TABLE_SIZE = 4;
    private static final int BYTE_MAX_SIZE = 256;
    private static final int BYTE_MASK = 255;
    private static final int SHORT_MAX_SIZE = 65536;
    private static final int SHORT_MASK = 65535;
    
    private CompactHashing() {
    }
    
    static int tableSize(final int expectedSize) {
        return Math.max(4, Hashing.closedTableSize(expectedSize + 1, 1.0));
    }
    
    static Object createTable(final int buckets) {
        if (buckets < 2 || buckets > 1073741824 || Integer.highestOneBit(buckets) != buckets) {
            throw new IllegalArgumentException(new StringBuilder(52).append("must be power of 2 between 2^1 and 2^30: ").append(buckets).toString());
        }
        if (buckets <= 256) {
            return new byte[buckets];
        }
        if (buckets <= 65536) {
            return new short[buckets];
        }
        return new int[buckets];
    }
    
    static void tableClear(final Object table) {
        if (table instanceof byte[]) {
            Arrays.fill((byte[])table, (byte)0);
        }
        else if (table instanceof short[]) {
            Arrays.fill((short[])table, (short)0);
        }
        else {
            Arrays.fill((int[])table, 0);
        }
    }
    
    static int tableGet(final Object table, final int index) {
        if (table instanceof byte[]) {
            return ((byte[])table)[index] & 0xFF;
        }
        if (table instanceof short[]) {
            return ((short[])table)[index] & 0xFFFF;
        }
        return ((int[])table)[index];
    }
    
    static void tableSet(final Object table, final int index, final int entry) {
        if (table instanceof byte[]) {
            ((byte[])table)[index] = (byte)entry;
        }
        else if (table instanceof short[]) {
            ((short[])table)[index] = (short)entry;
        }
        else {
            ((int[])table)[index] = entry;
        }
    }
    
    static int newCapacity(final int mask) {
        return ((mask < 32) ? 4 : 2) * (mask + 1);
    }
    
    static int getHashPrefix(final int value, final int mask) {
        return value & ~mask;
    }
    
    static int getNext(final int entry, final int mask) {
        return entry & mask;
    }
    
    static int maskCombine(final int prefix, final int suffix, final int mask) {
        return (prefix & ~mask) | (suffix & mask);
    }
    
    static int remove(@CheckForNull final Object key, @CheckForNull final Object value, final int mask, final Object table, final int[] entries, final Object[] keys, @CheckForNull final Object[] values) {
        final int hash = Hashing.smearedHash(key);
        final int tableIndex = hash & mask;
        int next = tableGet(table, tableIndex);
        if (next == 0) {
            return -1;
        }
        final int hashPrefix = getHashPrefix(hash, mask);
        int lastEntryIndex = -1;
        do {
            final int entryIndex = next - 1;
            final int entry = entries[entryIndex];
            if (getHashPrefix(entry, mask) == hashPrefix && Objects.equal(key, keys[entryIndex]) && (values == null || Objects.equal(value, values[entryIndex]))) {
                final int newNext = getNext(entry, mask);
                if (lastEntryIndex == -1) {
                    tableSet(table, tableIndex, newNext);
                }
                else {
                    entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
                }
                return entryIndex;
            }
            lastEntryIndex = entryIndex;
            next = getNext(entry, mask);
        } while (next != 0);
        return -1;
    }
}
