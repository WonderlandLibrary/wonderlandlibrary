package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
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

public class DefaultHttpHeaders extends HttpHeaders {
  private static final int BUCKET_SIZE = 17;
  
  private static int index(int hash) {
    return hash % 17;
  }
  
  private final HeaderEntry[] entries = new HeaderEntry[17];
  
  private final HeaderEntry head = new HeaderEntry();
  
  protected final boolean validate;
  
  public DefaultHttpHeaders() {
    this(true);
  }
  
  public DefaultHttpHeaders(boolean validate) {
    this.validate = validate;
    this.head.before = this.head.after = this.head;
  }
  
  void validateHeaderName0(CharSequence headerName) {
    validateHeaderName(headerName);
  }
  
  public HttpHeaders add(HttpHeaders headers) {
    if (headers instanceof DefaultHttpHeaders) {
      DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
      HeaderEntry e = defaultHttpHeaders.head.after;
      while (e != defaultHttpHeaders.head) {
        add(e.key, e.value);
        e = e.after;
      } 
      return this;
    } 
    return super.add(headers);
  }
  
  public HttpHeaders set(HttpHeaders headers) {
    if (headers instanceof DefaultHttpHeaders) {
      clear();
      DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
      HeaderEntry e = defaultHttpHeaders.head.after;
      while (e != defaultHttpHeaders.head) {
        add(e.key, e.value);
        e = e.after;
      } 
      return this;
    } 
    return super.set(headers);
  }
  
  public HttpHeaders add(String name, Object value) {
    return add(name, value);
  }
  
  public HttpHeaders add(CharSequence name, Object value) {
    CharSequence strVal;
    if (this.validate) {
      validateHeaderName0(name);
      strVal = toCharSequence(value);
      validateHeaderValue(strVal);
    } else {
      strVal = toCharSequence(value);
    } 
    int h = hash(name);
    int i = index(h);
    add0(h, i, name, strVal);
    return this;
  }
  
  public HttpHeaders add(String name, Iterable<?> values) {
    return add(name, values);
  }
  
  public HttpHeaders add(CharSequence name, Iterable<?> values) {
    if (this.validate)
      validateHeaderName0(name); 
    int h = hash(name);
    int i = index(h);
    for (Object v : values) {
      CharSequence vstr = toCharSequence(v);
      if (this.validate)
        validateHeaderValue(vstr); 
      add0(h, i, name, vstr);
    } 
    return this;
  }
  
  private void add0(int h, int i, CharSequence name, CharSequence value) {
    HeaderEntry e = this.entries[i];
    HeaderEntry newEntry = new HeaderEntry(h, name, value);
    newEntry.next = e;
    newEntry.addBefore(this.head);
  }
  
  public HttpHeaders remove(String name) {
    return remove(name);
  }
  
  public HttpHeaders remove(CharSequence name) {
    if (name == null)
      throw new NullPointerException("name"); 
    int h = hash(name);
    int i = index(h);
    remove0(h, i, name);
    return this;
  }
  
  private void remove0(int h, int i, CharSequence name) {
    HeaderEntry e = this.entries[i];
    if (e == null)
      return; 
    while (e.hash == h && equalsIgnoreCase(name, e.key)) {
      e.remove();
      HeaderEntry next = e.next;
      if (next != null) {
        this.entries[i] = next;
        e = next;
        continue;
      } 
      this.entries[i] = null;
      return;
    } 
    while (true) {
      HeaderEntry next = e.next;
      if (next == null)
        break; 
      if (next.hash == h && equalsIgnoreCase(name, next.key)) {
        e.next = next.next;
        next.remove();
        continue;
      } 
      e = next;
    } 
  }
  
  public HttpHeaders set(String name, Object value) {
    return set(name, value);
  }
  
  public HttpHeaders set(CharSequence name, Object value) {
    CharSequence strVal;
    if (this.validate) {
      validateHeaderName0(name);
      strVal = toCharSequence(value);
      validateHeaderValue(strVal);
    } else {
      strVal = toCharSequence(value);
    } 
    int h = hash(name);
    int i = index(h);
    remove0(h, i, name);
    add0(h, i, name, strVal);
    return this;
  }
  
  public HttpHeaders set(String name, Iterable<?> values) {
    return set(name, values);
  }
  
  public HttpHeaders set(CharSequence name, Iterable<?> values) {
    if (values == null)
      throw new NullPointerException("values"); 
    if (this.validate)
      validateHeaderName0(name); 
    int h = hash(name);
    int i = index(h);
    remove0(h, i, name);
    for (Object v : values) {
      if (v == null)
        break; 
      CharSequence strVal = toCharSequence(v);
      if (this.validate)
        validateHeaderValue(strVal); 
      add0(h, i, name, strVal);
    } 
    return this;
  }
  
  public HttpHeaders clear() {
    Arrays.fill((Object[])this.entries, (Object)null);
    this.head.before = this.head.after = this.head;
    return this;
  }
  
  public String get(String name) {
    return get(name);
  }
  
  public String get(CharSequence name) {
    if (name == null)
      throw new NullPointerException("name"); 
    int h = hash(name);
    int i = index(h);
    HeaderEntry e = this.entries[i];
    CharSequence value = null;
    while (e != null) {
      if (e.hash == h && equalsIgnoreCase(name, e.key))
        value = e.value; 
      e = e.next;
    } 
    if (value == null)
      return null; 
    return value.toString();
  }
  
  public List<String> getAll(String name) {
    return getAll(name);
  }
  
  public List<String> getAll(CharSequence name) {
    if (name == null)
      throw new NullPointerException("name"); 
    LinkedList<String> values = new LinkedList<String>();
    int h = hash(name);
    int i = index(h);
    HeaderEntry e = this.entries[i];
    while (e != null) {
      if (e.hash == h && equalsIgnoreCase(name, e.key))
        values.addFirst(e.getValue()); 
      e = e.next;
    } 
    return values;
  }
  
  public List<Map.Entry<String, String>> entries() {
    List<Map.Entry<String, String>> all = new LinkedList<Map.Entry<String, String>>();
    HeaderEntry e = this.head.after;
    while (e != this.head) {
      all.add(e);
      e = e.after;
    } 
    return all;
  }
  
  public Iterator<Map.Entry<String, String>> iterator() {
    return new HeaderIterator();
  }
  
  public boolean contains(String name) {
    return (get(name) != null);
  }
  
  public boolean contains(CharSequence name) {
    return (get(name) != null);
  }
  
  public boolean isEmpty() {
    return (this.head == this.head.after);
  }
  
  public boolean contains(String name, String value, boolean ignoreCaseValue) {
    return contains(name, value, ignoreCaseValue);
  }
  
  public boolean contains(CharSequence name, CharSequence value, boolean ignoreCaseValue) {
    if (name == null)
      throw new NullPointerException("name"); 
    int h = hash(name);
    int i = index(h);
    HeaderEntry e = this.entries[i];
    while (e != null) {
      if (e.hash == h && equalsIgnoreCase(name, e.key))
        if (ignoreCaseValue) {
          if (equalsIgnoreCase(e.value, value))
            return true; 
        } else if (e.value.equals(value)) {
          return true;
        }  
      e = e.next;
    } 
    return false;
  }
  
  public Set<String> names() {
    Set<String> names = new LinkedHashSet<String>();
    HeaderEntry e = this.head.after;
    while (e != this.head) {
      names.add(e.getKey());
      e = e.after;
    } 
    return names;
  }
  
  private static CharSequence toCharSequence(Object value) {
    if (value == null)
      return null; 
    if (value instanceof CharSequence)
      return (CharSequence)value; 
    if (value instanceof Number)
      return value.toString(); 
    if (value instanceof Date)
      return HttpHeaderDateFormat.get().format((Date)value); 
    if (value instanceof Calendar)
      return HttpHeaderDateFormat.get().format(((Calendar)value).getTime()); 
    return value.toString();
  }
  
  void encode(ByteBuf buf) {
    HeaderEntry e = this.head.after;
    while (e != this.head) {
      e.encode(buf);
      e = e.after;
    } 
  }
  
  private final class HeaderIterator implements Iterator<Map.Entry<String, String>> {
    private DefaultHttpHeaders.HeaderEntry current = DefaultHttpHeaders.this.head;
    
    public boolean hasNext() {
      return (this.current.after != DefaultHttpHeaders.this.head);
    }
    
    public Map.Entry<String, String> next() {
      this.current = this.current.after;
      if (this.current == DefaultHttpHeaders.this.head)
        throw new NoSuchElementException(); 
      return this.current;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
    private HeaderIterator() {}
  }
  
  private final class HeaderEntry implements Map.Entry<String, String> {
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
    
    void addBefore(HeaderEntry e) {
      this.after = e;
      this.before = e.before;
      this.before.after = this;
      this.after.before = this;
    }
    
    public String getKey() {
      return this.key.toString();
    }
    
    public String getValue() {
      return this.value.toString();
    }
    
    public String setValue(String value) {
      if (value == null)
        throw new NullPointerException("value"); 
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultHttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */