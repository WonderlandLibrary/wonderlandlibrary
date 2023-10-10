package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

public class DefaultHttpHeaders extends HttpHeaders {
   private static final int BUCKET_SIZE = 17;
   private final DefaultHttpHeaders.HeaderEntry[] entries;
   private final DefaultHttpHeaders.HeaderEntry head;
   protected final boolean validate;

   private static int index(int var0) {
      return var0 % 17;
   }

   public DefaultHttpHeaders() {
      this(true);
   }

   public DefaultHttpHeaders(boolean var1) {
      this.entries = new DefaultHttpHeaders.HeaderEntry[17];
      this.head = new DefaultHttpHeaders.HeaderEntry(this);
      this.validate = var1;
      this.head.before = this.head.after = this.head;
   }

   void validateHeaderName0(CharSequence var1) {
      validateHeaderName(var1);
   }

   public HttpHeaders add(HttpHeaders var1) {
      if (!(var1 instanceof DefaultHttpHeaders)) {
         return super.add(var1);
      } else {
         DefaultHttpHeaders var2 = (DefaultHttpHeaders)var1;

         for(DefaultHttpHeaders.HeaderEntry var3 = var2.head.after; var3 != var2.head; var3 = var3.after) {
            this.add((CharSequence)var3.key, (Object)var3.value);
         }

         return this;
      }
   }

   public HttpHeaders set(HttpHeaders var1) {
      if (!(var1 instanceof DefaultHttpHeaders)) {
         return super.set(var1);
      } else {
         this.clear();
         DefaultHttpHeaders var2 = (DefaultHttpHeaders)var1;

         for(DefaultHttpHeaders.HeaderEntry var3 = var2.head.after; var3 != var2.head; var3 = var3.after) {
            this.add((CharSequence)var3.key, (Object)var3.value);
         }

         return this;
      }
   }

   public HttpHeaders add(String var1, Object var2) {
      return this.add((CharSequence)var1, (Object)var2);
   }

   public HttpHeaders add(CharSequence var1, Object var2) {
      CharSequence var3;
      if (this.validate) {
         this.validateHeaderName0(var1);
         var3 = toCharSequence(var2);
         validateHeaderValue(var3);
      } else {
         var3 = toCharSequence(var2);
      }

      int var4 = hash(var1);
      int var5 = index(var4);
      this.add0(var4, var5, var1, var3);
      return this;
   }

   public HttpHeaders add(String var1, Iterable var2) {
      return this.add((CharSequence)var1, (Iterable)var2);
   }

   public HttpHeaders add(CharSequence var1, Iterable var2) {
      if (this.validate) {
         this.validateHeaderName0(var1);
      }

      int var3 = hash(var1);
      int var4 = index(var3);

      CharSequence var7;
      for(Iterator var5 = var2.iterator(); var5.hasNext(); this.add0(var3, var4, var1, var7)) {
         Object var6 = var5.next();
         var7 = toCharSequence(var6);
         if (this.validate) {
            validateHeaderValue(var7);
         }
      }

      return this;
   }

   private void add0(int var1, int var2, CharSequence var3, CharSequence var4) {
      DefaultHttpHeaders.HeaderEntry var5 = this.entries[var2];
      DefaultHttpHeaders.HeaderEntry var6;
      this.entries[var2] = var6 = new DefaultHttpHeaders.HeaderEntry(this, var1, var3, var4);
      var6.next = var5;
      var6.addBefore(this.head);
   }

   public HttpHeaders remove(String var1) {
      return this.remove((CharSequence)var1);
   }

   public HttpHeaders remove(CharSequence var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         int var2 = hash(var1);
         int var3 = index(var2);
         this.remove0(var2, var3, var1);
         return this;
      }
   }

   private void remove0(int var1, int var2, CharSequence var3) {
      DefaultHttpHeaders.HeaderEntry var4 = this.entries[var2];
      if (var4 != null) {
         DefaultHttpHeaders.HeaderEntry var5;
         while(var4.hash == var1 && equalsIgnoreCase(var3, var4.key)) {
            var4.remove();
            var5 = var4.next;
            if (var5 == null) {
               this.entries[var2] = null;
               return;
            }

            this.entries[var2] = var5;
            var4 = var5;
         }

         while(true) {
            while(true) {
               var5 = var4.next;
               if (var5 == null) {
                  return;
               }

               if (var5.hash == var1 && equalsIgnoreCase(var3, var5.key)) {
                  var4.next = var5.next;
                  var5.remove();
               } else {
                  var4 = var5;
               }
            }
         }
      }
   }

   public HttpHeaders set(String var1, Object var2) {
      return this.set((CharSequence)var1, (Object)var2);
   }

   public HttpHeaders set(CharSequence var1, Object var2) {
      CharSequence var3;
      if (this.validate) {
         this.validateHeaderName0(var1);
         var3 = toCharSequence(var2);
         validateHeaderValue(var3);
      } else {
         var3 = toCharSequence(var2);
      }

      int var4 = hash(var1);
      int var5 = index(var4);
      this.remove0(var4, var5, var1);
      this.add0(var4, var5, var1, var3);
      return this;
   }

   public HttpHeaders set(String var1, Iterable var2) {
      return this.set((CharSequence)var1, (Iterable)var2);
   }

   public HttpHeaders set(CharSequence var1, Iterable var2) {
      if (var2 == null) {
         throw new NullPointerException("values");
      } else {
         if (this.validate) {
            this.validateHeaderName0(var1);
         }

         int var3 = hash(var1);
         int var4 = index(var3);
         this.remove0(var3, var4, var1);

         CharSequence var7;
         for(Iterator var5 = var2.iterator(); var5.hasNext(); this.add0(var3, var4, var1, var7)) {
            Object var6 = var5.next();
            if (var6 == null) {
               break;
            }

            var7 = toCharSequence(var6);
            if (this.validate) {
               validateHeaderValue(var7);
            }
         }

         return this;
      }
   }

   public HttpHeaders clear() {
      Arrays.fill(this.entries, (Object)null);
      this.head.before = this.head.after = this.head;
      return this;
   }

   public String get(String var1) {
      return this.get((CharSequence)var1);
   }

   public String get(CharSequence var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         int var2 = hash(var1);
         int var3 = index(var2);
         DefaultHttpHeaders.HeaderEntry var4 = this.entries[var3];

         CharSequence var5;
         for(var5 = null; var4 != null; var4 = var4.next) {
            if (var4.hash == var2 && equalsIgnoreCase(var1, var4.key)) {
               var5 = var4.value;
            }
         }

         if (var5 == null) {
            return null;
         } else {
            return var5.toString();
         }
      }
   }

   public List getAll(String var1) {
      return this.getAll((CharSequence)var1);
   }

   public List getAll(CharSequence var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         LinkedList var2 = new LinkedList();
         int var3 = hash(var1);
         int var4 = index(var3);

         for(DefaultHttpHeaders.HeaderEntry var5 = this.entries[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && equalsIgnoreCase(var1, var5.key)) {
               var2.addFirst(var5.getValue());
            }
         }

         return var2;
      }
   }

   public List entries() {
      LinkedList var1 = new LinkedList();

      for(DefaultHttpHeaders.HeaderEntry var2 = this.head.after; var2 != this.head; var2 = var2.after) {
         var1.add(var2);
      }

      return var1;
   }

   public Iterator iterator() {
      return new DefaultHttpHeaders.HeaderIterator(this);
   }

   public boolean contains(String var1) {
      return this.get(var1) != null;
   }

   public boolean contains(CharSequence var1) {
      return this.get(var1) != null;
   }

   public boolean isEmpty() {
      return this.head == this.head.after;
   }

   public boolean contains(String var1, String var2, boolean var3) {
      return this.contains((CharSequence)var1, (CharSequence)var2, var3);
   }

   public boolean contains(CharSequence var1, CharSequence var2, boolean var3) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         int var4 = hash(var1);
         int var5 = index(var4);

         for(DefaultHttpHeaders.HeaderEntry var6 = this.entries[var5]; var6 != null; var6 = var6.next) {
            if (var6.hash == var4 && equalsIgnoreCase(var1, var6.key)) {
               if (var3) {
                  if (equalsIgnoreCase(var6.value, var2)) {
                     return true;
                  }
               } else if (var6.value.equals(var2)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public Set names() {
      LinkedHashSet var1 = new LinkedHashSet();

      for(DefaultHttpHeaders.HeaderEntry var2 = this.head.after; var2 != this.head; var2 = var2.after) {
         var1.add(var2.getKey());
      }

      return var1;
   }

   private static CharSequence toCharSequence(Object var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof CharSequence) {
         return (CharSequence)var0;
      } else if (var0 instanceof Number) {
         return var0.toString();
      } else if (var0 instanceof Date) {
         return HttpHeaderDateFormat.get().format((Date)var0);
      } else {
         return var0 instanceof Calendar ? HttpHeaderDateFormat.get().format(((Calendar)var0).getTime()) : var0.toString();
      }
   }

   void encode(ByteBuf var1) {
      for(DefaultHttpHeaders.HeaderEntry var2 = this.head.after; var2 != this.head; var2 = var2.after) {
         var2.encode(var1);
      }

   }

   static DefaultHttpHeaders.HeaderEntry access$100(DefaultHttpHeaders var0) {
      return var0.head;
   }

   private final class HeaderEntry implements Entry {
      final int hash;
      final CharSequence key;
      CharSequence value;
      DefaultHttpHeaders.HeaderEntry next;
      DefaultHttpHeaders.HeaderEntry before;
      DefaultHttpHeaders.HeaderEntry after;
      final DefaultHttpHeaders this$0;

      HeaderEntry(DefaultHttpHeaders var1, int var2, CharSequence var3, CharSequence var4) {
         this.this$0 = var1;
         this.hash = var2;
         this.key = var3;
         this.value = var4;
      }

      HeaderEntry(DefaultHttpHeaders var1) {
         this.this$0 = var1;
         this.hash = -1;
         this.key = null;
         this.value = null;
      }

      void remove() {
         this.before.after = this.after;
         this.after.before = this.before;
      }

      void addBefore(DefaultHttpHeaders.HeaderEntry var1) {
         this.after = var1;
         this.before = var1.before;
         this.before.after = this;
         this.after.before = this;
      }

      public String getKey() {
         return this.key.toString();
      }

      public String getValue() {
         return this.value.toString();
      }

      public String setValue(String var1) {
         if (var1 == null) {
            throw new NullPointerException("value");
         } else {
            HttpHeaders.validateHeaderValue(var1);
            CharSequence var2 = this.value;
            this.value = var1;
            return var2.toString();
         }
      }

      public String toString() {
         return this.key.toString() + '=' + this.value.toString();
      }

      void encode(ByteBuf var1) {
         HttpHeaders.encode(this.key, this.value, var1);
      }

      public Object setValue(Object var1) {
         return this.setValue((String)var1);
      }

      public Object getValue() {
         return this.getValue();
      }

      public Object getKey() {
         return this.getKey();
      }
   }

   private final class HeaderIterator implements Iterator {
      private DefaultHttpHeaders.HeaderEntry current;
      final DefaultHttpHeaders this$0;

      private HeaderIterator(DefaultHttpHeaders var1) {
         this.this$0 = var1;
         this.current = DefaultHttpHeaders.access$100(this.this$0);
      }

      public boolean hasNext() {
         return this.current.after != DefaultHttpHeaders.access$100(this.this$0);
      }

      public Entry next() {
         this.current = this.current.after;
         if (this.current == DefaultHttpHeaders.access$100(this.this$0)) {
            throw new NoSuchElementException();
         } else {
            return this.current;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public Object next() {
         return this.next();
      }

      HeaderIterator(DefaultHttpHeaders var1, Object var2) {
         this(var1);
      }
   }
}
