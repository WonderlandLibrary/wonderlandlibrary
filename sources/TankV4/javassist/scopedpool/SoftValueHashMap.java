package javassist.scopedpool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoftValueHashMap extends AbstractMap implements Map {
   private Map hash;
   private ReferenceQueue queue;

   public Set entrySet() {
      this.processQueue();
      return this.hash.entrySet();
   }

   private void processQueue() {
      SoftValueHashMap.SoftValueRef var1;
      while((var1 = (SoftValueHashMap.SoftValueRef)this.queue.poll()) != null) {
         if (var1 == (SoftValueHashMap.SoftValueRef)this.hash.get(var1.key)) {
            this.hash.remove(var1.key);
         }
      }

   }

   public SoftValueHashMap(int var1, float var2) {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap(var1, var2);
   }

   public SoftValueHashMap(int var1) {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap(var1);
   }

   public SoftValueHashMap() {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap();
   }

   public SoftValueHashMap(Map var1) {
      this(Math.max(2 * var1.size(), 11), 0.75F);
      this.putAll(var1);
   }

   public int size() {
      this.processQueue();
      return this.hash.size();
   }

   public boolean isEmpty() {
      this.processQueue();
      return this.hash.isEmpty();
   }

   public boolean containsKey(Object var1) {
      this.processQueue();
      return this.hash.containsKey(var1);
   }

   public Object get(Object var1) {
      this.processQueue();
      SoftReference var2 = (SoftReference)this.hash.get(var1);
      return var2 != null ? var2.get() : null;
   }

   public Object put(Object var1, Object var2) {
      this.processQueue();
      Object var3 = this.hash.put(var1, SoftValueHashMap.SoftValueRef.access$000(var1, var2, this.queue));
      if (var3 != null) {
         var3 = ((SoftReference)var3).get();
      }

      return var3;
   }

   public Object remove(Object var1) {
      this.processQueue();
      return this.hash.remove(var1);
   }

   public void clear() {
      this.processQueue();
      this.hash.clear();
   }

   private static class SoftValueRef extends SoftReference {
      public Object key;

      private SoftValueRef(Object var1, Object var2, ReferenceQueue var3) {
         super(var2, var3);
         this.key = var1;
      }

      private static SoftValueHashMap.SoftValueRef create(Object var0, Object var1, ReferenceQueue var2) {
         return var1 == null ? null : new SoftValueHashMap.SoftValueRef(var0, var1, var2);
      }

      static SoftValueHashMap.SoftValueRef access$000(Object var0, Object var1, ReferenceQueue var2) {
         return create(var0, var1, var2);
      }
   }
}
