/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.StyleCacheEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class StyleCache {
    private Map<StyleCacheEntry.Key, StyleCacheEntry> entries;

    public void clear() {
        if (this.entries == null) {
            return;
        }
        Thread.dumpStack();
        this.entries.clear();
    }

    public StyleCacheEntry getStyleCacheEntry(StyleCacheEntry.Key key) {
        StyleCacheEntry styleCacheEntry = null;
        if (this.entries != null) {
            styleCacheEntry = this.entries.get(key);
        }
        return styleCacheEntry;
    }

    public void addStyleCacheEntry(StyleCacheEntry.Key key, StyleCacheEntry styleCacheEntry) {
        if (this.entries == null) {
            this.entries = new HashMap<StyleCacheEntry.Key, StyleCacheEntry>(5);
        }
        this.entries.put(key, styleCacheEntry);
    }

    public static final class Key {
        final int[] styleMapIds;
        private int hash = Integer.MIN_VALUE;

        public Key(int[] arrn, int n) {
            this.styleMapIds = new int[n];
            System.arraycopy(arrn, 0, this.styleMapIds, 0, n);
        }

        public Key(Key key) {
            this(key.styleMapIds, key.styleMapIds.length);
        }

        public int[] getStyleMapIds() {
            return this.styleMapIds;
        }

        public String toString() {
            return Arrays.toString(this.styleMapIds);
        }

        public int hashCode() {
            if (this.hash == Integer.MIN_VALUE) {
                this.hash = 3;
                if (this.styleMapIds != null) {
                    for (int i = 0; i < this.styleMapIds.length; ++i) {
                        int n = this.styleMapIds[i];
                        this.hash = 17 * (this.hash + n);
                    }
                }
            }
            return this.hash;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || object.getClass() != this.getClass()) {
                return false;
            }
            Key key = (Key)object;
            if (this.hash != key.hash) {
                return false;
            }
            if (this.styleMapIds == null ^ key.styleMapIds == null) {
                return false;
            }
            if (this.styleMapIds == null) {
                return true;
            }
            if (this.styleMapIds.length != key.styleMapIds.length) {
                return false;
            }
            for (int i = 0; i < this.styleMapIds.length; ++i) {
                if (this.styleMapIds[i] == key.styleMapIds[i]) continue;
                return false;
            }
            return true;
        }
    }
}

