/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaderDateFormat;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class DefaultHttpHeaders
extends HttpHeaders {
    private static final int BUCKET_SIZE = 17;
    private final HeaderEntry[] entries = new HeaderEntry[17];
    private final HeaderEntry head = new HeaderEntry();
    protected final boolean validate;

    private static int index(int hash) {
        return hash % 17;
    }

    public DefaultHttpHeaders() {
        this(true);
    }

    public DefaultHttpHeaders(boolean validate) {
        this.validate = validate;
        this.head.before = this.head.after = this.head;
    }

    void validateHeaderName0(CharSequence headerName) {
        DefaultHttpHeaders.validateHeaderName(headerName);
    }

    @Override
    public HttpHeaders add(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
            HeaderEntry e2 = defaultHttpHeaders.head.after;
            while (e2 != defaultHttpHeaders.head) {
                this.add(e2.key, (Object)e2.value);
                e2 = e2.after;
            }
            return this;
        }
        return super.add(headers);
    }

    @Override
    public HttpHeaders set(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.clear();
            DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
            HeaderEntry e2 = defaultHttpHeaders.head.after;
            while (e2 != defaultHttpHeaders.head) {
                this.add(e2.key, (Object)e2.value);
                e2 = e2.after;
            }
            return this;
        }
        return super.set(headers);
    }

    @Override
    public HttpHeaders add(String name, Object value) {
        return this.add((CharSequence)name, value);
    }

    @Override
    public HttpHeaders add(CharSequence name, Object value) {
        CharSequence strVal;
        if (this.validate) {
            this.validateHeaderName0(name);
            strVal = DefaultHttpHeaders.toCharSequence(value);
            DefaultHttpHeaders.validateHeaderValue(strVal);
        } else {
            strVal = DefaultHttpHeaders.toCharSequence(value);
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        this.add0(h2, i2, name, strVal);
        return this;
    }

    @Override
    public HttpHeaders add(String name, Iterable<?> values) {
        return this.add((CharSequence)name, values);
    }

    @Override
    public HttpHeaders add(CharSequence name, Iterable<?> values) {
        if (this.validate) {
            this.validateHeaderName0(name);
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        for (Object v2 : values) {
            CharSequence vstr = DefaultHttpHeaders.toCharSequence(v2);
            if (this.validate) {
                DefaultHttpHeaders.validateHeaderValue(vstr);
            }
            this.add0(h2, i2, name, vstr);
        }
        return this;
    }

    private void add0(int h2, int i2, CharSequence name, CharSequence value) {
        HeaderEntry newEntry;
        HeaderEntry e2 = this.entries[i2];
        this.entries[i2] = newEntry = new HeaderEntry(h2, name, value);
        newEntry.next = e2;
        newEntry.addBefore(this.head);
    }

    @Override
    public HttpHeaders remove(String name) {
        return this.remove((CharSequence)name);
    }

    @Override
    public HttpHeaders remove(CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        this.remove0(h2, i2, name);
        return this;
    }

    private void remove0(int h2, int i2, CharSequence name) {
        HeaderEntry next;
        HeaderEntry e2 = this.entries[i2];
        if (e2 == null) {
            return;
        }
        while (e2.hash == h2 && DefaultHttpHeaders.equalsIgnoreCase(name, e2.key)) {
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
            if (next.hash == h2 && DefaultHttpHeaders.equalsIgnoreCase(name, next.key)) {
                e2.next = next.next;
                next.remove();
                continue;
            }
            e2 = next;
        }
    }

    @Override
    public HttpHeaders set(String name, Object value) {
        return this.set((CharSequence)name, value);
    }

    @Override
    public HttpHeaders set(CharSequence name, Object value) {
        CharSequence strVal;
        if (this.validate) {
            this.validateHeaderName0(name);
            strVal = DefaultHttpHeaders.toCharSequence(value);
            DefaultHttpHeaders.validateHeaderValue(strVal);
        } else {
            strVal = DefaultHttpHeaders.toCharSequence(value);
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        this.remove0(h2, i2, name);
        this.add0(h2, i2, name, strVal);
        return this;
    }

    @Override
    public HttpHeaders set(String name, Iterable<?> values) {
        return this.set((CharSequence)name, values);
    }

    @Override
    public HttpHeaders set(CharSequence name, Iterable<?> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
        if (this.validate) {
            this.validateHeaderName0(name);
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        this.remove0(h2, i2, name);
        for (Object v2 : values) {
            if (v2 == null) break;
            CharSequence strVal = DefaultHttpHeaders.toCharSequence(v2);
            if (this.validate) {
                DefaultHttpHeaders.validateHeaderValue(strVal);
            }
            this.add0(h2, i2, name, strVal);
        }
        return this;
    }

    @Override
    public HttpHeaders clear() {
        Arrays.fill(this.entries, null);
        this.head.before = this.head.after = this.head;
        return this;
    }

    @Override
    public String get(String name) {
        return this.get((CharSequence)name);
    }

    @Override
    public String get(CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        HeaderEntry e2 = this.entries[i2];
        CharSequence value = null;
        while (e2 != null) {
            if (e2.hash == h2 && DefaultHttpHeaders.equalsIgnoreCase(name, e2.key)) {
                value = e2.value;
            }
            e2 = e2.next;
        }
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public List<String> getAll(String name) {
        return this.getAll((CharSequence)name);
    }

    @Override
    public List<String> getAll(CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        LinkedList<String> values = new LinkedList<String>();
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        HeaderEntry e2 = this.entries[i2];
        while (e2 != null) {
            if (e2.hash == h2 && DefaultHttpHeaders.equalsIgnoreCase(name, e2.key)) {
                values.addFirst(e2.getValue());
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
    public boolean contains(CharSequence name) {
        return this.get(name) != null;
    }

    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }

    @Override
    public boolean contains(String name, String value, boolean ignoreCaseValue) {
        return this.contains((CharSequence)name, (CharSequence)value, ignoreCaseValue);
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCaseValue) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        int h2 = DefaultHttpHeaders.hash(name);
        int i2 = DefaultHttpHeaders.index(h2);
        HeaderEntry e2 = this.entries[i2];
        while (e2 != null) {
            if (e2.hash == h2 && DefaultHttpHeaders.equalsIgnoreCase(name, e2.key) && (ignoreCaseValue ? DefaultHttpHeaders.equalsIgnoreCase(e2.value, value) : e2.value.equals(value))) {
                return true;
            }
            e2 = e2.next;
        }
        return false;
    }

    @Override
    public Set<String> names() {
        LinkedHashSet<String> names = new LinkedHashSet<String>();
        HeaderEntry e2 = this.head.after;
        while (e2 != this.head) {
            names.add(e2.getKey());
            e2 = e2.after;
        }
        return names;
    }

    private static CharSequence toCharSequence(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return (CharSequence)value;
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Date) {
            return HttpHeaderDateFormat.get().format((Date)value);
        }
        if (value instanceof Calendar) {
            return HttpHeaderDateFormat.get().format(((Calendar)value).getTime());
        }
        return value.toString();
    }

    void encode(ByteBuf buf) {
        HeaderEntry e2 = this.head.after;
        while (e2 != this.head) {
            e2.encode(buf);
            e2 = e2.after;
        }
    }

    private final class HeaderEntry
    implements Map.Entry<String, String> {
        final int hash;
        final CharSequence key;
        CharSequence value;
        HeaderEntry next;
        HeaderEntry before;
        HeaderEntry after;

        HeaderEntry(int hash, CharSequence key, CharSequence value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.value = null;
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
            return this.key.toString();
        }

        @Override
        public String getValue() {
            return this.value.toString();
        }

        @Override
        public String setValue(String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            HttpHeaders.validateHeaderValue(value);
            CharSequence oldValue = this.value;
            this.value = value;
            return oldValue.toString();
        }

        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }

        void encode(ByteBuf buf) {
            HttpHeaders.encode(this.key, this.value, buf);
        }
    }

    private final class HeaderIterator
    implements Iterator<Map.Entry<String, String>> {
        private HeaderEntry current;

        private HeaderIterator() {
            this.current = DefaultHttpHeaders.this.head;
        }

        @Override
        public boolean hasNext() {
            return this.current.after != DefaultHttpHeaders.this.head;
        }

        @Override
        public Map.Entry<String, String> next() {
            this.current = this.current.after;
            if (this.current == DefaultHttpHeaders.this.head) {
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

