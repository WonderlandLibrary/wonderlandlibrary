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
    protected final boolean validate;
    private final HeaderEntry head = new HeaderEntry();

    @Override
    public HttpHeaders add(HttpHeaders httpHeaders) {
        if (httpHeaders instanceof DefaultHttpHeaders) {
            DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)httpHeaders;
            HeaderEntry headerEntry = defaultHttpHeaders.head.after;
            while (headerEntry != defaultHttpHeaders.head) {
                this.add(headerEntry.key, (Object)headerEntry.value);
                headerEntry = headerEntry.after;
            }
            return this;
        }
        return super.add(httpHeaders);
    }

    @Override
    public boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        if (charSequence == null) {
            throw new NullPointerException("name");
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        HeaderEntry headerEntry = this.entries[n2];
        while (headerEntry != null) {
            if (headerEntry.hash == n && DefaultHttpHeaders.equalsIgnoreCase(charSequence, headerEntry.key) && (bl ? DefaultHttpHeaders.equalsIgnoreCase(headerEntry.value, charSequence2) : headerEntry.value.equals(charSequence2))) {
                return true;
            }
            headerEntry = headerEntry.next;
        }
        return false;
    }

    @Override
    public HttpHeaders set(String string, Iterable<?> iterable) {
        return this.set((CharSequence)string, iterable);
    }

    void validateHeaderName0(CharSequence charSequence) {
        DefaultHttpHeaders.validateHeaderName(charSequence);
    }

    private void add0(int n, int n2, CharSequence charSequence, CharSequence charSequence2) {
        HeaderEntry headerEntry;
        HeaderEntry headerEntry2 = this.entries[n2];
        this.entries[n2] = headerEntry = new HeaderEntry(n, charSequence, charSequence2);
        headerEntry.next = headerEntry2;
        headerEntry.addBefore(this.head);
    }

    @Override
    public HttpHeaders set(CharSequence charSequence, Object object) {
        CharSequence charSequence2;
        if (this.validate) {
            this.validateHeaderName0(charSequence);
            charSequence2 = DefaultHttpHeaders.toCharSequence(object);
            DefaultHttpHeaders.validateHeaderValue(charSequence2);
        } else {
            charSequence2 = DefaultHttpHeaders.toCharSequence(object);
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        this.remove0(n, n2, charSequence);
        this.add0(n, n2, charSequence, charSequence2);
        return this;
    }

    @Override
    public HttpHeaders set(CharSequence charSequence, Iterable<?> iterable) {
        if (iterable == null) {
            throw new NullPointerException("values");
        }
        if (this.validate) {
            this.validateHeaderName0(charSequence);
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        this.remove0(n, n2, charSequence);
        for (Object obj : iterable) {
            if (obj == null) break;
            CharSequence charSequence2 = DefaultHttpHeaders.toCharSequence(obj);
            if (this.validate) {
                DefaultHttpHeaders.validateHeaderValue(charSequence2);
            }
            this.add0(n, n2, charSequence, charSequence2);
        }
        return this;
    }

    private void remove0(int n, int n2, CharSequence charSequence) {
        HeaderEntry headerEntry;
        HeaderEntry headerEntry2 = this.entries[n2];
        if (headerEntry2 == null) {
            return;
        }
        while (headerEntry2.hash == n && DefaultHttpHeaders.equalsIgnoreCase(charSequence, headerEntry2.key)) {
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
            if (headerEntry.hash == n && DefaultHttpHeaders.equalsIgnoreCase(charSequence, headerEntry.key)) {
                headerEntry2.next = headerEntry.next;
                headerEntry.remove();
                continue;
            }
            headerEntry2 = headerEntry;
        }
    }

    @Override
    public HttpHeaders set(String string, Object object) {
        return this.set((CharSequence)string, object);
    }

    @Override
    public HttpHeaders clear() {
        Arrays.fill(this.entries, null);
        this.head.before = this.head.after = this.head;
        return this;
    }

    @Override
    public List<String> getAll(CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("name");
        }
        LinkedList<String> linkedList = new LinkedList<String>();
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        HeaderEntry headerEntry = this.entries[n2];
        while (headerEntry != null) {
            if (headerEntry.hash == n && DefaultHttpHeaders.equalsIgnoreCase(charSequence, headerEntry.key)) {
                linkedList.addFirst(headerEntry.getValue());
            }
            headerEntry = headerEntry.next;
        }
        return linkedList;
    }

    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
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

    @Override
    public HttpHeaders add(CharSequence charSequence, Object object) {
        CharSequence charSequence2;
        if (this.validate) {
            this.validateHeaderName0(charSequence);
            charSequence2 = DefaultHttpHeaders.toCharSequence(object);
            DefaultHttpHeaders.validateHeaderValue(charSequence2);
        } else {
            charSequence2 = DefaultHttpHeaders.toCharSequence(object);
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        this.add0(n, n2, charSequence, charSequence2);
        return this;
    }

    @Override
    public HttpHeaders add(CharSequence charSequence, Iterable<?> iterable) {
        if (this.validate) {
            this.validateHeaderName0(charSequence);
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        for (Object obj : iterable) {
            CharSequence charSequence2 = DefaultHttpHeaders.toCharSequence(obj);
            if (this.validate) {
                DefaultHttpHeaders.validateHeaderValue(charSequence2);
            }
            this.add0(n, n2, charSequence, charSequence2);
        }
        return this;
    }

    @Override
    public Set<String> names() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        HeaderEntry headerEntry = this.head.after;
        while (headerEntry != this.head) {
            linkedHashSet.add((String)headerEntry.getKey());
            headerEntry = headerEntry.after;
        }
        return linkedHashSet;
    }

    void encode(ByteBuf byteBuf) {
        HeaderEntry headerEntry = this.head.after;
        while (headerEntry != this.head) {
            headerEntry.encode(byteBuf);
            headerEntry = headerEntry.after;
        }
    }

    @Override
    public String get(CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("name");
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        HeaderEntry headerEntry = this.entries[n2];
        CharSequence charSequence2 = null;
        while (headerEntry != null) {
            if (headerEntry.hash == n && DefaultHttpHeaders.equalsIgnoreCase(charSequence, headerEntry.key)) {
                charSequence2 = headerEntry.value;
            }
            headerEntry = headerEntry.next;
        }
        if (charSequence2 == null) {
            return null;
        }
        return charSequence2.toString();
    }

    @Override
    public HttpHeaders set(HttpHeaders httpHeaders) {
        if (httpHeaders instanceof DefaultHttpHeaders) {
            this.clear();
            DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)httpHeaders;
            HeaderEntry headerEntry = defaultHttpHeaders.head.after;
            while (headerEntry != defaultHttpHeaders.head) {
                this.add(headerEntry.key, (Object)headerEntry.value);
                headerEntry = headerEntry.after;
            }
            return this;
        }
        return super.set(httpHeaders);
    }

    @Override
    public HttpHeaders remove(String string) {
        return this.remove((CharSequence)string);
    }

    @Override
    public boolean contains(String string) {
        return this.get(string) != null;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new HeaderIterator();
    }

    @Override
    public String get(String string) {
        return this.get((CharSequence)string);
    }

    @Override
    public boolean contains(String string, String string2, boolean bl) {
        return this.contains((CharSequence)string, (CharSequence)string2, bl);
    }

    @Override
    public List<String> getAll(String string) {
        return this.getAll((CharSequence)string);
    }

    @Override
    public HttpHeaders add(String string, Object object) {
        return this.add((CharSequence)string, object);
    }

    public DefaultHttpHeaders(boolean bl) {
        this.validate = bl;
        this.head.before = this.head.after = this.head;
    }

    private static CharSequence toCharSequence(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof CharSequence) {
            return (CharSequence)object;
        }
        if (object instanceof Number) {
            return object.toString();
        }
        if (object instanceof Date) {
            return HttpHeaderDateFormat.get().format((Date)object);
        }
        if (object instanceof Calendar) {
            return HttpHeaderDateFormat.get().format(((Calendar)object).getTime());
        }
        return object.toString();
    }

    @Override
    public boolean contains(CharSequence charSequence) {
        return this.get(charSequence) != null;
    }

    @Override
    public HttpHeaders remove(CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("name");
        }
        int n = DefaultHttpHeaders.hash(charSequence);
        int n2 = DefaultHttpHeaders.index(n);
        this.remove0(n, n2, charSequence);
        return this;
    }

    @Override
    public HttpHeaders add(String string, Iterable<?> iterable) {
        return this.add((CharSequence)string, iterable);
    }

    private static int index(int n) {
        return n % 17;
    }

    public DefaultHttpHeaders() {
        this(true);
    }

    private final class HeaderEntry
    implements Map.Entry<String, String> {
        final int hash;
        HeaderEntry after;
        HeaderEntry before;
        HeaderEntry next;
        final CharSequence key;
        CharSequence value;

        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.value = null;
        }

        void addBefore(HeaderEntry headerEntry) {
            this.after = headerEntry;
            this.before = headerEntry.before;
            this.before.after = this;
            this.after.before = this;
        }

        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }

        @Override
        public String setValue(String string) {
            if (string == null) {
                throw new NullPointerException("value");
            }
            HttpHeaders.validateHeaderValue(string);
            CharSequence charSequence = this.value;
            this.value = string;
            return charSequence.toString();
        }

        @Override
        public String getValue() {
            return this.value.toString();
        }

        @Override
        public String getKey() {
            return this.key.toString();
        }

        void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }

        void encode(ByteBuf byteBuf) {
            HttpHeaders.encode(this.key, this.value, byteBuf);
        }

        HeaderEntry(int n, CharSequence charSequence, CharSequence charSequence2) {
            this.hash = n;
            this.key = charSequence;
            this.value = charSequence2;
        }
    }

    private final class HeaderIterator
    implements Iterator<Map.Entry<String, String>> {
        private HeaderEntry current;

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

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
    }
}

