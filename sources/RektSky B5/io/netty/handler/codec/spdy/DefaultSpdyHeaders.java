/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaders;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public class DefaultSpdyHeaders
extends SpdyHeaders {
    private static final int BUCKET_SIZE = 17;
    private final HeaderEntry[] entries = new HeaderEntry[17];
    private final HeaderEntry head;

    private static int hash(String name) {
        int h2 = 0;
        for (int i2 = name.length() - 1; i2 >= 0; --i2) {
            char c2 = name.charAt(i2);
            if (c2 >= 'A' && c2 <= 'Z') {
                c2 = (char)(c2 + 32);
            }
            h2 = 31 * h2 + c2;
        }
        if (h2 > 0) {
            return h2;
        }
        if (h2 == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return -h2;
    }

    private static boolean eq(String name1, String name2) {
        int nameLen = name1.length();
        if (nameLen != name2.length()) {
            return false;
        }
        for (int i2 = nameLen - 1; i2 >= 0; --i2) {
            char c2;
            char c1 = name1.charAt(i2);
            if (c1 == (c2 = name2.charAt(i2))) continue;
            if (c1 >= 'A' && c1 <= 'Z') {
                c1 = (char)(c1 + 32);
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                c2 = (char)(c2 + 32);
            }
            if (c1 == c2) continue;
            return false;
        }
        return true;
    }

    private static int index(int hash) {
        return hash % 17;
    }

    DefaultSpdyHeaders() {
        this.head.before = this.head.after = (this.head = new HeaderEntry(-1, null, null));
    }

    @Override
    public SpdyHeaders add(String name, Object value) {
        String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        String strVal = DefaultSpdyHeaders.toString(value);
        SpdyCodecUtil.validateHeaderValue(strVal);
        int h2 = DefaultSpdyHeaders.hash(lowerCaseName);
        int i2 = DefaultSpdyHeaders.index(h2);
        this.add0(h2, i2, lowerCaseName, strVal);
        return this;
    }

    private void add0(int h2, int i2, String name, String value) {
        HeaderEntry newEntry;
        HeaderEntry e2 = this.entries[i2];
        this.entries[i2] = newEntry = new HeaderEntry(h2, name, value);
        newEntry.next = e2;
        newEntry.addBefore(this.head);
    }

    @Override
    public SpdyHeaders remove(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        String lowerCaseName = name.toLowerCase();
        int h2 = DefaultSpdyHeaders.hash(lowerCaseName);
        int i2 = DefaultSpdyHeaders.index(h2);
        this.remove0(h2, i2, lowerCaseName);
        return this;
    }

    private void remove0(int h2, int i2, String name) {
        HeaderEntry next;
        HeaderEntry e2 = this.entries[i2];
        if (e2 == null) {
            return;
        }
        while (e2.hash == h2 && DefaultSpdyHeaders.eq(name, e2.key)) {
            e2.remove();
            next = e2.next;
            if (next != null) {
                this.entries[i2] = next;
                e2 = next;
                continue;
            }
            this.entries[i2] = null;
            return;
        }
        while ((next = e2.next) != null) {
            if (next.hash == h2 && DefaultSpdyHeaders.eq(name, next.key)) {
                e2.next = next.next;
                next.remove();
                continue;
            }
            e2 = next;
        }
    }

    @Override
    public SpdyHeaders set(String name, Object value) {
        String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        String strVal = DefaultSpdyHeaders.toString(value);
        SpdyCodecUtil.validateHeaderValue(strVal);
        int h2 = DefaultSpdyHeaders.hash(lowerCaseName);
        int i2 = DefaultSpdyHeaders.index(h2);
        this.remove0(h2, i2, lowerCaseName);
        this.add0(h2, i2, lowerCaseName, strVal);
        return this;
    }

    @Override
    public SpdyHeaders set(String name, Iterable<?> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
        String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        int h2 = DefaultSpdyHeaders.hash(lowerCaseName);
        int i2 = DefaultSpdyHeaders.index(h2);
        this.remove0(h2, i2, lowerCaseName);
        for (Object v2 : values) {
            if (v2 == null) break;
            String strVal = DefaultSpdyHeaders.toString(v2);
            SpdyCodecUtil.validateHeaderValue(strVal);
            this.add0(h2, i2, lowerCaseName, strVal);
        }
        return this;
    }

    @Override
    public SpdyHeaders clear() {
        for (int i2 = 0; i2 < this.entries.length; ++i2) {
            this.entries[i2] = null;
        }
        this.head.before = this.head.after = this.head;
        return this;
    }

    @Override
    public String get(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        int h2 = DefaultSpdyHeaders.hash(name);
        int i2 = DefaultSpdyHeaders.index(h2);
        HeaderEntry e2 = this.entries[i2];
        while (e2 != null) {
            if (e2.hash == h2 && DefaultSpdyHeaders.eq(name, e2.key)) {
                return e2.value;
            }
            e2 = e2.next;
        }
        return null;
    }

    @Override
    public List<String> getAll(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        LinkedList<String> values = new LinkedList<String>();
        int h2 = DefaultSpdyHeaders.hash(name);
        int i2 = DefaultSpdyHeaders.index(h2);
        HeaderEntry e2 = this.entries[i2];
        while (e2 != null) {
            if (e2.hash == h2 && DefaultSpdyHeaders.eq(name, e2.key)) {
                values.addFirst(e2.value);
            }
            e2 = e2.next;
        }
        return values;
    }

    @Override
    public List<Map.Entry<String, String>> entries() {
        LinkedList<Map.Entry<String, String>> all = new LinkedList<Map.Entry<String, String>>();
        HeaderEntry e2 = this.head.after;
        while (e2 != this.head) {
            all.add(e2);
            e2 = e2.after;
        }
        return all;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new HeaderIterator();
    }

    @Override
    public boolean contains(String name) {
        return this.get(name) != null;
    }

    @Override
    public Set<String> names() {
        TreeSet<String> names = new TreeSet<String>();
        HeaderEntry e2 = this.head.after;
        while (e2 != this.head) {
            names.add(e2.key);
            e2 = e2.after;
        }
        return names;
    }

    @Override
    public SpdyHeaders add(String name, Iterable<?> values) {
        SpdyCodecUtil.validateHeaderValue(name);
        int h2 = DefaultSpdyHeaders.hash(name);
        int i2 = DefaultSpdyHeaders.index(h2);
        for (Object v2 : values) {
            String vstr = DefaultSpdyHeaders.toString(v2);
            SpdyCodecUtil.validateHeaderValue(vstr);
            this.add0(h2, i2, name, vstr);
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }

    private static String toString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private static final class HeaderEntry
    implements Map.Entry<String, String> {
        final int hash;
        final String key;
        String value;
        HeaderEntry next;
        HeaderEntry before;
        HeaderEntry after;

        HeaderEntry(int hash, String key, String value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }

        void addBefore(HeaderEntry e2) {
            this.after = e2;
            this.before = e2.before;
            this.before.after = this;
            this.after.before = this;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String setValue(String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            SpdyCodecUtil.validateHeaderValue(value);
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public String toString() {
            return this.key + '=' + this.value;
        }
    }

    private final class HeaderIterator
    implements Iterator<Map.Entry<String, String>> {
        private HeaderEntry current;

        private HeaderIterator() {
            this.current = DefaultSpdyHeaders.this.head;
        }

        @Override
        public boolean hasNext() {
            return this.current.after != DefaultSpdyHeaders.this.head;
        }

        @Override
        public Map.Entry<String, String> next() {
            this.current = this.current.after;
            if (this.current == DefaultSpdyHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

