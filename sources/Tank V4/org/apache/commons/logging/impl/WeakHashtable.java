package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable extends Hashtable {
   private static final long serialVersionUID = -1546036869799732453L;
   private static final int MAX_CHANGES_BEFORE_PURGE = 100;
   private static final int PARTIAL_PURGE_COUNT = 10;
   private final ReferenceQueue queue = new ReferenceQueue();
   private int changeCount = 0;

   public boolean containsKey(Object var1) {
      WeakHashtable.Referenced var2 = new WeakHashtable.Referenced(var1);
      return super.containsKey(var2);
   }

   public Enumeration elements() {
      this.purge();
      return super.elements();
   }

   public Set entrySet() {
      this.purge();
      Set var1 = super.entrySet();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         java.util.Map.Entry var4 = (java.util.Map.Entry)var3.next();
         WeakHashtable.Referenced var5 = (WeakHashtable.Referenced)var4.getKey();
         Object var6 = WeakHashtable.Referenced.access$100(var5);
         Object var7 = var4.getValue();
         if (var6 != null) {
            WeakHashtable.Entry var8 = new WeakHashtable.Entry(var6, var7);
            var2.add(var8);
         }
      }

      return var2;
   }

   public Object get(Object var1) {
      WeakHashtable.Referenced var2 = new WeakHashtable.Referenced(var1);
      return super.get(var2);
   }

   public Enumeration keys() {
      this.purge();
      Enumeration var1 = super.keys();
      return new Enumeration(this, var1) {
         private final Enumeration val$enumer;
         private final WeakHashtable this$0;

         {
            this.this$0 = var1;
            this.val$enumer = var2;
         }

         public boolean hasMoreElements() {
            return this.val$enumer.hasMoreElements();
         }

         public Object nextElement() {
            WeakHashtable.Referenced var1 = (WeakHashtable.Referenced)this.val$enumer.nextElement();
            return WeakHashtable.Referenced.access$100(var1);
         }
      };
   }

   public Set keySet() {
      this.purge();
      Set var1 = super.keySet();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WeakHashtable.Referenced var4 = (WeakHashtable.Referenced)var3.next();
         Object var5 = WeakHashtable.Referenced.access$100(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public synchronized Object put(Object var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("Null keys are not allowed");
      } else if (var2 == null) {
         throw new NullPointerException("Null values are not allowed");
      } else {
         if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
         } else if (this.changeCount % 10 == 0) {
            this.purgeOne();
         }

         WeakHashtable.Referenced var3 = new WeakHashtable.Referenced(var1, this.queue);
         return super.put(var3, var2);
      }
   }

   public void putAll(Map var1) {
      if (var1 != null) {
         Set var2 = var1.entrySet();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            java.util.Map.Entry var4 = (java.util.Map.Entry)var3.next();
            this.put(var4.getKey(), var4.getValue());
         }
      }

   }

   public Collection values() {
      this.purge();
      return super.values();
   }

   public synchronized Object remove(Object var1) {
      if (this.changeCount++ > 100) {
         this.purge();
         this.changeCount = 0;
      } else if (this.changeCount % 10 == 0) {
         this.purgeOne();
      }

      return super.remove(new WeakHashtable.Referenced(var1));
   }

   public boolean isEmpty() {
      this.purge();
      return super.isEmpty();
   }

   public int size() {
      this.purge();
      return super.size();
   }

   public String toString() {
      this.purge();
      return super.toString();
   }

   protected void rehash() {
      this.purge();
      super.rehash();
   }

   private void purge() {
      ArrayList var1 = new ArrayList();
      synchronized(this.queue){}

      WeakHashtable.WeakKey var3;
      while((var3 = (WeakHashtable.WeakKey)this.queue.poll()) != null) {
         var1.add(WeakHashtable.WeakKey.access$400(var3));
      }

      int var2 = var1.size();

      for(int var5 = 0; var5 < var2; ++var5) {
         super.remove(var1.get(var5));
      }

   }

   private void purgeOne() {
      ReferenceQueue var1;
      synchronized(var1 = this.queue){}
      WeakHashtable.WeakKey var2 = (WeakHashtable.WeakKey)this.queue.poll();
      if (var2 != null) {
         super.remove(WeakHashtable.WeakKey.access$400(var2));
      }

   }

   private static final class WeakKey extends WeakReference {
      private final WeakHashtable.Referenced referenced;

      private WeakKey(Object var1, ReferenceQueue var2, WeakHashtable.Referenced var3) {
         super(var1, var2);
         this.referenced = var3;
      }

      private WeakHashtable.Referenced getReferenced() {
         return this.referenced;
      }

      static WeakHashtable.Referenced access$400(WeakHashtable.WeakKey var0) {
         return var0.getReferenced();
      }

      WeakKey(Object var1, ReferenceQueue var2, WeakHashtable.Referenced var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class Referenced {
      private final WeakReference reference;
      private final int hashCode;

      private Referenced(Object var1) {
         this.reference = new WeakReference(var1);
         this.hashCode = var1.hashCode();
      }

      private Referenced(Object var1, ReferenceQueue var2) {
         this.reference = new WeakHashtable.WeakKey(var1, var2, this);
         this.hashCode = var1.hashCode();
      }

      public int hashCode() {
         return this.hashCode;
      }

      private Object getValue() {
         return this.reference.get();
      }

      public boolean equals(Object var1) {
         boolean var2 = false;
         if (var1 instanceof WeakHashtable.Referenced) {
            WeakHashtable.Referenced var3 = (WeakHashtable.Referenced)var1;
            Object var4 = this.getValue();
            Object var5 = var3.getValue();
            if (var4 == null) {
               var2 = var5 == null;
               var2 = var2 && this.hashCode() == var3.hashCode();
            } else {
               var2 = var4.equals(var5);
            }
         }

         return var2;
      }

      Referenced(Object var1, Object var2) {
         this(var1);
      }

      static Object access$100(WeakHashtable.Referenced var0) {
         return var0.getValue();
      }

      Referenced(Object var1, ReferenceQueue var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class Entry implements java.util.Map.Entry {
      private final Object key;
      private final Object value;

      private Entry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = false;
         if (var1 != null && var1 instanceof java.util.Map.Entry) {
            boolean var10000;
            label29: {
               label28: {
                  label27: {
                     java.util.Map.Entry var3 = (java.util.Map.Entry)var1;
                     if (this.getKey() == null) {
                        if (var3.getKey() != null) {
                           break label27;
                        }
                     } else if (!this.getKey().equals(var3.getKey())) {
                        break label27;
                     }

                     if (this.getValue() == null) {
                        if (var3.getValue() == null) {
                           break label28;
                        }
                     } else if (this.getValue().equals(var3.getValue())) {
                        break label28;
                     }
                  }

                  var10000 = false;
                  break label29;
               }

               var10000 = true;
            }

            var2 = var10000;
         }

         return var2;
      }

      public int hashCode() {
         return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException("Entry.setValue is not supported.");
      }

      public Object getValue() {
         return this.value;
      }

      public Object getKey() {
         return this.key;
      }

      Entry(Object var1, Object var2, Object var3) {
         this(var1, var2);
      }
   }
}
