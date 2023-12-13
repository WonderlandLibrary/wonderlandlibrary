/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.util.Map;
import java.util.TreeMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AbbreviationMap<V> {
    private String key;
    private V value;
    private final Map<Character, AbbreviationMap<V>> children = new TreeMap<Character, AbbreviationMap<V>>();
    private int keysBeyond;

    public boolean contains(String aKey) {
        return this.get(aKey) != null;
    }

    public V get(String aKey) {
        char[] chars = AbbreviationMap.charsOf(aKey);
        AbbreviationMap<V> child = this;
        for (char each : chars) {
            child = child.children.get(Character.valueOf(each));
            if (child != null) continue;
            return null;
        }
        return child.value;
    }

    public void put(String aKey, V newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        }
        if (aKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        char[] chars = AbbreviationMap.charsOf(aKey);
        this.add(chars, newValue, 0, chars.length);
    }

    public void putAll(Iterable<String> keys, V newValue) {
        for (String each : keys) {
            this.put(each, newValue);
        }
    }

    private boolean add(char[] chars, V newValue, int offset, int length) {
        boolean newKeyAdded;
        if (offset == length) {
            this.value = newValue;
            boolean wasAlreadyAKey = this.key != null;
            this.key = new String(chars);
            return !wasAlreadyAKey;
        }
        char nextChar = chars[offset];
        AbbreviationMap<V> child = this.children.get(Character.valueOf(nextChar));
        if (child == null) {
            child = new AbbreviationMap<V>();
            this.children.put(Character.valueOf(nextChar), child);
        }
        if (newKeyAdded = super.add(chars, newValue, offset + 1, length)) {
            ++this.keysBeyond;
        }
        if (this.key == null) {
            this.value = this.keysBeyond > 1 ? null : newValue;
        }
        return newKeyAdded;
    }

    public void remove(String aKey) {
        if (aKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        char[] keyChars = AbbreviationMap.charsOf(aKey);
        this.remove(keyChars, 0, keyChars.length);
    }

    private boolean remove(char[] aKey, int offset, int length) {
        if (offset == length) {
            return this.removeAtEndOfKey();
        }
        char nextChar = aKey[offset];
        AbbreviationMap<V> child = this.children.get(Character.valueOf(nextChar));
        if (child == null || !super.remove(aKey, offset + 1, length)) {
            return false;
        }
        --this.keysBeyond;
        if (child.keysBeyond == 0) {
            this.children.remove(Character.valueOf(nextChar));
        }
        if (this.keysBeyond == 1 && this.key == null) {
            this.setValueToThatOfOnlyChild();
        }
        return true;
    }

    private void setValueToThatOfOnlyChild() {
        Map.Entry<Character, AbbreviationMap<V>> entry = this.children.entrySet().iterator().next();
        AbbreviationMap<V> onlyChild = entry.getValue();
        this.value = onlyChild.value;
    }

    private boolean removeAtEndOfKey() {
        if (this.key == null) {
            return false;
        }
        this.key = null;
        if (this.keysBeyond == 1) {
            this.setValueToThatOfOnlyChild();
        } else {
            this.value = null;
        }
        return true;
    }

    public Map<String, V> toJavaUtilMap() {
        TreeMap mappings = new TreeMap();
        this.addToMappings(mappings);
        return mappings;
    }

    private void addToMappings(Map<String, V> mappings) {
        if (this.key != null) {
            mappings.put(this.key, this.value);
        }
        for (AbbreviationMap<V> each : this.children.values()) {
            super.addToMappings(mappings);
        }
    }

    private static char[] charsOf(String aKey) {
        char[] chars = new char[aKey.length()];
        aKey.getChars(0, aKey.length(), chars, 0);
        return chars;
    }
}

