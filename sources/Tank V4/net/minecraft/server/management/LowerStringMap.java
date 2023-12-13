package net.minecraft.server.management;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LowerStringMap implements Map {
   private final Map internalMap = Maps.newLinkedHashMap();

   public Object remove(Object var1) {
      return this.internalMap.remove(var1.toString().toLowerCase());
   }

   public Set entrySet() {
      return this.internalMap.entrySet();
   }

   public boolean isEmpty() {
      return this.internalMap.isEmpty();
   }

   public Object put(String var1, Object var2) {
      return this.internalMap.put(var1.toLowerCase(), var2);
   }

   public Collection values() {
      return this.internalMap.values();
   }

   public boolean containsValue(Object var1) {
      return this.internalMap.containsKey(var1);
   }

   public Set keySet() {
      return this.internalMap.keySet();
   }

   public boolean containsKey(Object var1) {
      return this.internalMap.containsKey(var1.toString().toLowerCase());
   }

   public void clear() {
      this.internalMap.clear();
   }

   public Object get(Object var1) {
      return this.internalMap.get(var1.toString().toLowerCase());
   }

   public Object put(Object var1, Object var2) {
      return this.put((String)var1, (Object)var2);
   }

   public void putAll(Map var1) {
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.put((String)var2.getKey(), var2.getValue());
      }

   }

   public int size() {
      return this.internalMap.size();
   }
}
