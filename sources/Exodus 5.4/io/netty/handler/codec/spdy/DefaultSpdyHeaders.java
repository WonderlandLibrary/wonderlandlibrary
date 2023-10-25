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
    private final HeaderEntry[] entries = new HeaderEntry[17];
    private final HeaderEntry head;
    private static final int BUCKET_SIZE = 17;

    @Override
    public SpdyHeaders set(String string, Iterable<?> iterable) {
        if (iterable == null) {
            throw new NullPointerException("values");
        }
        String string2 = string.toLowerCase();
        SpdyCodecUtil.validateHeaderName(string2);
        int n = DefaultSpdyHeaders.hash(string2);
        int n2 = DefaultSpdyHeaders.index(n);
        this.remove0(n, n2, string2);
        for (Object obj : iterable) {
            if (obj == null) break;
            String string3 = DefaultSpdyHeaders.toString(obj);
            SpdyCodecUtil.validateHeaderValue(string3);
            this.add0(n, n2, string2, string3);
        }
        return this;
    }

    private static int hash(String string) {
        int n = 0;
        for (int i = string.length() - 1; i >= 0; --i) {
            char c = string.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32);
            }
            n = 31 * n + c;
        }
        if (n > 0) {
            return n;
        }
        if (n == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return -n;
    }

    @Override
    public SpdyHeaders clear() {
        for (int i = 0; i < this.entries.length; ++i) {
            this.entries[i] = null;
        }
        this.head.before = this.head.after = this.head;
        return this;
    }

    @Override
    public List<String> getAll(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        LinkedList<String> linkedList = new LinkedList<String>();
        int n = DefaultSpdyHeaders.hash(string);
        int n2 = DefaultSpdyHeaders.index(n);
        HeaderEntry headerEntry = this.entries[n2];
        while (headerEntry != null) {
            if (headerEntry.hash == n && DefaultSpdyHeaders.eq(string, headerEntry.key)) {
                linkedList.addFirst(headerEntry.value);
            }
            headerEntry = headerEntry.next;
        }
        return linkedList;
    }

    private static boolean eq(String string, String string2) {
        int n = string.length();
        if (n != string2.length()) {
            return false;
        }
        for (int i = n - 1; i >= 0; --i) {
            char c;
            char c2 = string.charAt(i);
            if (c2 == (c = string2.charAt(i))) continue;
            if (c2 >= 'A' && c2 <= 'Z') {
                c2 = (char)(c2 + 32);
            }
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32);
            }
            if (c2 == c) continue;
            return false;
        }
        return true;
    }

    DefaultSpdyHeaders() {
        this.head.before = this.head.after = (this.head = new HeaderEntry(-1, null, null));
    }

    @Override
    public SpdyHeaders set(String string, Object object) {
        String string2 = string.toLowerCase();
        SpdyCodecUtil.validateHeaderName(string2);
        String string3 = DefaultSpdyHeaders.toString(object);
        SpdyCodecUtil.validateHeaderValue(string3);
        int n = DefaultSpdyHeaders.hash(string2);
        int n2 = DefaultSpdyHeaders.index(n);
        this.remove0(n, n2, string2);
        this.add0(n, n2, string2, string3);
        return this;
    }

    @Override
    public List<Map.Entry<String, String>> entries() {
        LinkedList<Map.Entry<String, String>> linkedList = new LinkedList<Map.Entry<String, String>>();
        HeaderEntry headerEntry = this.head.after;
        while (headerEntry != this.head) {
            linkedList.add(headerEntry);
            headerEntry = headerEntry.after;
        }
        return linkedList;
    }

    private void add0(int n, int n2, String string, String string2) {
        HeaderEntry headerEntry;
        HeaderEntry headerEntry2 = this.entries[n2];
        this.entries[n2] = headerEntry = new HeaderEntry(n, string, string2);
        headerEntry.next = headerEntry2;
        headerEntry.addBefore(this.head);
    }

    private static String toString(Object object) {
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public SpdyHeaders add(String string, Object object) {
        String string2 = string.toLowerCase();
        SpdyCodecUtil.validateHeaderName(string2);
        String string3 = DefaultSpdyHeaders.toString(object);
        SpdyCodecUtil.validateHeaderValue(string3);
        int n = DefaultSpdyHeaders.hash(string2);
        int n2 = DefaultSpdyHeaders.index(n);
        this.add0(n, n2, string2, string3);
        return this;
    }

    @Override
    public boolean contains(String string) {
        return this.get(string) != null;
    }

    @Override
    public String get(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        int n = DefaultSpdyHeaders.hash(string);
        int n2 = DefaultSpdyHeaders.index(n);
        HeaderEntry headerEntry = this.entries[n2];
        while (headerEntry != null) {
            if (headerEntry.hash == n && DefaultSpdyHeaders.eq(string, headerEntry.key)) {
                return headerEntry.value;
            }
            headerEntry = headerEntry.next;
        }
        return null;
    }

    @Override
    public SpdyHeaders add(String string, Iterable<?> iterable) {
        SpdyCodecUtil.validateHeaderValue(string);
        int n = DefaultSpdyHeaders.hash(string);
        int n2 = DefaultSpdyHeaders.index(n);
        for (Object obj : iterable) {
            String string2 = DefaultSpdyHeaders.toString(obj);
            SpdyCodecUtil.validateHeaderValue(string2);
            this.add0(n, n2, string, string2);
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }

    private static int index(int n) {
        return n % 17;
    }

    @Override
    public SpdyHeaders remove(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        String string2 = string.toLowerCase();
        int n = DefaultSpdyHeaders.hash(string2);
        int n2 = DefaultSpdyHeaders.index(n);
        this.remove0(n, n2, string2);
        return this;
    }

    @Override
    public Set<String> names() {
        TreeSet<String> treeSet = new TreeSet<String>();
        HeaderEntry headerEntry = this.head.after;
        while (headerEntry != this.head) {
            treeSet.add(headerEntry.key);
            headerEntry = headerEntry.after;
        }
        return treeSet;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new HeaderIterator();
    }

    private void remove0(int n, int n2, String string) {
        HeaderEntry headerEntry;
        HeaderEntry headerEntry2 = this.entries[n2];
        if (headerEntry2 == null) {
            return;
        }
        while (headerEntry2.hash == n && DefaultSpdyHeaders.eq(string, headerEntry2.key)) {
            headerEntry2.remove();
            headerEntry = headerEntry2.next;
            if (headerEntry != null) {
                this.entries[n2] = headerEntry;
                headerEntry2 = headerEntry;
                continue;
            }
            this.entries[n2] = null;
            return;
        }
        while ((headerEntry = headerEntry2.next) != null) {
            if (headerEntry.hash == n && DefaultSpdyHeaders.eq(string, headerEntry.key)) {
                headerEntry2.next = headerEntry.next;
                headerEntry.remove();
                continue;
            }
            headerEntry2 = headerEntry;
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
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<String, String> next() {
            this.current = this.current.after;
            if (this.current == DefaultSpdyHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }
    }

    private static final class HeaderEntry
    implements Map.Entry<String, String> {
        HeaderEntry after;
        String value;
        final int hash;
        final String key;
        HeaderEntry before;
        HeaderEntry next;

        @Override
        public String getValue() {
            return this.value;
        }

        HeaderEntry(int n, String string, String string2) {
            this.hash = n;
            this.key = string;
            this.value = string2;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        void addBefore(HeaderEntry headerEntry) {
            this.after = headerEntry;
            this.before = headerEntry.before;
            this.before.after = this;
            this.after.before = this;
        }

        void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }

        @Override
        public String setValue(String string) {
            if (string == null) {
                throw new NullPointerException("value");
            }
            SpdyCodecUtil.validateHeaderValue(string);
            String string2 = this.value;
            this.value = string;
            return string2;
        }

        public String toString() {
            return this.key + '=' + this.value;
        }
    }
}

