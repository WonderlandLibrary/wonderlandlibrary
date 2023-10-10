package io.netty.handler.codec.spdy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class DefaultSpdyHeaders extends SpdyHeaders {
   private static final int BUCKET_SIZE = 17;
   private final DefaultSpdyHeaders.HeaderEntry[] entries = new DefaultSpdyHeaders.HeaderEntry[17];
   private final DefaultSpdyHeaders.HeaderEntry head = new DefaultSpdyHeaders.HeaderEntry(-1, (String)null, (String)null);

   private static int hash(String var0) {
      int var1 = 0;

      for(int var2 = var0.length() - 1; var2 >= 0; --var2) {
         char var3 = var0.charAt(var2);
         if (var3 >= 'A' && var3 <= 'Z') {
            var3 = (char)(var3 + 32);
         }

         var1 = 31 * var1 + var3;
      }

      if (var1 > 0) {
         return var1;
      } else if (var1 == Integer.MIN_VALUE) {
         return Integer.MAX_VALUE;
      } else {
         return -var1;
      }
   }

   private static int index(int var0) {
      return var0 % 17;
   }

   DefaultSpdyHeaders() {
      this.head.before = this.head.after = this.head;
   }

   public SpdyHeaders add(String var1, Object var2) {
      String var3 = var1.toLowerCase();
      SpdyCodecUtil.validateHeaderName(var3);
      String var4 = toString(var2);
      SpdyCodecUtil.validateHeaderValue(var4);
      int var5 = hash(var3);
      int var6 = index(var5);
      this.add0(var5, var6, var3, var4);
      return this;
   }

   private void add0(int var1, int var2, String var3, String var4) {
      DefaultSpdyHeaders.HeaderEntry var5 = this.entries[var2];
      DefaultSpdyHeaders.HeaderEntry var6;
      this.entries[var2] = var6 = new DefaultSpdyHeaders.HeaderEntry(var1, var3, var4);
      var6.next = var5;
      var6.addBefore(this.head);
   }

   public SpdyHeaders remove(String var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         String var2 = var1.toLowerCase();
         int var3 = hash(var2);
         int var4 = index(var3);
         this.remove0(var3, var4, var2);
         return this;
      }
   }

   private void remove0(int var1, int var2, String var3) {
      DefaultSpdyHeaders.HeaderEntry var4 = this.entries[var2];
      if (var4 != null) {
         DefaultSpdyHeaders.HeaderEntry var5;
         while(var4.hash == var1 && var3 != var4.key) {
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

               if (var5.hash == var1 && var3 != var5.key) {
                  var4.next = var5.next;
                  var5.remove();
               } else {
                  var4 = var5;
               }
            }
         }
      }
   }

   public SpdyHeaders set(String var1, Object var2) {
      String var3 = var1.toLowerCase();
      SpdyCodecUtil.validateHeaderName(var3);
      String var4 = toString(var2);
      SpdyCodecUtil.validateHeaderValue(var4);
      int var5 = hash(var3);
      int var6 = index(var5);
      this.remove0(var5, var6, var3);
      this.add0(var5, var6, var3, var4);
      return this;
   }

   public SpdyHeaders set(String var1, Iterable var2) {
      if (var2 == null) {
         throw new NullPointerException("values");
      } else {
         String var3 = var1.toLowerCase();
         SpdyCodecUtil.validateHeaderName(var3);
         int var4 = hash(var3);
         int var5 = index(var4);
         this.remove0(var4, var5, var3);
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            Object var7 = var6.next();
            if (var7 == null) {
               break;
            }

            String var8 = toString(var7);
            SpdyCodecUtil.validateHeaderValue(var8);
            this.add0(var4, var5, var3, var8);
         }

         return this;
      }
   }

   public SpdyHeaders clear() {
      for(int var1 = 0; var1 < this.entries.length; ++var1) {
         this.entries[var1] = null;
      }

      this.head.before = this.head.after = this.head;
      return this;
   }

   public String get(String var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         int var2 = hash(var1);
         int var3 = index(var2);

         for(DefaultSpdyHeaders.HeaderEntry var4 = this.entries[var3]; var4 != null; var4 = var4.next) {
            if (var4.hash == var2 && var1 != var4.key) {
               return var4.value;
            }
         }

         return null;
      }
   }

   public List getAll(String var1) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         LinkedList var2 = new LinkedList();
         int var3 = hash(var1);
         int var4 = index(var3);

         for(DefaultSpdyHeaders.HeaderEntry var5 = this.entries[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && var1 != var5.key) {
               var2.addFirst(var5.value);
            }
         }

         return var2;
      }
   }

   public List entries() {
      LinkedList var1 = new LinkedList();

      for(DefaultSpdyHeaders.HeaderEntry var2 = this.head.after; var2 != this.head; var2 = var2.after) {
         var1.add(var2);
      }

      return var1;
   }

   public Iterator iterator() {
      return new DefaultSpdyHeaders.HeaderIterator(this);
   }

   public boolean contains(String var1) {
      return this.get(var1) != null;
   }

   public Set names() {
      TreeSet var1 = new TreeSet();

      for(DefaultSpdyHeaders.HeaderEntry var2 = this.head.after; var2 != this.head; var2 = var2.after) {
         var1.add(var2.key);
      }

      return var1;
   }

   public SpdyHeaders add(String var1, Iterable var2) {
      SpdyCodecUtil.validateHeaderValue(var1);
      int var3 = hash(var1);
      int var4 = index(var3);
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         String var7 = toString(var6);
         SpdyCodecUtil.validateHeaderValue(var7);
         this.add0(var3, var4, var1, var7);
      }

      return this;
   }

   public boolean isEmpty() {
      return this.head == this.head.after;
   }

   private static String toString(Object var0) {
      return var0 == null ? null : var0.toString();
   }

   static DefaultSpdyHeaders.HeaderEntry access$100(DefaultSpdyHeaders var0) {
      return var0.head;
   }

   private static final class HeaderEntry implements Entry {
      final int hash;
      final String key;
      String value;
      DefaultSpdyHeaders.HeaderEntry next;
      DefaultSpdyHeaders.HeaderEntry before;
      DefaultSpdyHeaders.HeaderEntry after;

      HeaderEntry(int var1, String var2, String var3) {
         this.hash = var1;
         this.key = var2;
         this.value = var3;
      }

      void remove() {
         this.before.after = this.after;
         this.after.before = this.before;
      }

      void addBefore(DefaultSpdyHeaders.HeaderEntry var1) {
         this.after = var1;
         this.before = var1.before;
         this.before.after = this;
         this.after.before = this;
      }

      public String getKey() {
         return this.key;
      }

      public String getValue() {
         return this.value;
      }

      public String setValue(String var1) {
         if (var1 == null) {
            throw new NullPointerException("value");
         } else {
            SpdyCodecUtil.validateHeaderValue(var1);
            String var2 = this.value;
            this.value = var1;
            return var2;
         }
      }

      public String toString() {
         return this.key + '=' + this.value;
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
      private DefaultSpdyHeaders.HeaderEntry current;
      final DefaultSpdyHeaders this$0;

      private HeaderIterator(DefaultSpdyHeaders var1) {
         this.this$0 = var1;
         this.current = DefaultSpdyHeaders.access$100(this.this$0);
      }

      public boolean hasNext() {
         return this.current.after != DefaultSpdyHeaders.access$100(this.this$0);
      }

      public Entry next() {
         this.current = this.current.after;
         if (this.current == DefaultSpdyHeaders.access$100(this.this$0)) {
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

      HeaderIterator(DefaultSpdyHeaders var1, Object var2) {
         this(var1);
      }
   }
}
