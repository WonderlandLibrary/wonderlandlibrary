package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.ThreadContext;

public class MutableThreadContextStack implements ThreadContextStack {
   private static final long serialVersionUID = 50505011L;
   private final List list;

   public MutableThreadContextStack(List var1) {
      this.list = new ArrayList(var1);
   }

   private MutableThreadContextStack(MutableThreadContextStack var1) {
      this.list = new ArrayList(var1.list);
   }

   public String pop() {
      if (this.list.isEmpty()) {
         return null;
      } else {
         int var1 = this.list.size() - 1;
         String var2 = (String)this.list.remove(var1);
         return var2;
      }
   }

   public String peek() {
      if (this.list.isEmpty()) {
         return null;
      } else {
         int var1 = this.list.size() - 1;
         return (String)this.list.get(var1);
      }
   }

   public void push(String var1) {
      this.list.add(var1);
   }

   public int getDepth() {
      return this.list.size();
   }

   public List asList() {
      return this.list;
   }

   public void trim(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Maximum stack depth cannot be negative");
      } else if (this.list != null) {
         ArrayList var2 = new ArrayList(this.list.size());
         int var3 = Math.min(var1, this.list.size());

         for(int var4 = 0; var4 < var3; ++var4) {
            var2.add(this.list.get(var4));
         }

         this.list.clear();
         this.list.addAll(var2);
      }
   }

   public ThreadContextStack copy() {
      return new MutableThreadContextStack(this);
   }

   public void clear() {
      this.list.clear();
   }

   public int size() {
      return this.list.size();
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.list.contains(var1);
   }

   public Iterator iterator() {
      return this.list.iterator();
   }

   public Object[] toArray() {
      return this.list.toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.list.toArray(var1);
   }

   public boolean add(String var1) {
      return this.list.add(var1);
   }

   public boolean remove(Object var1) {
      return this.list.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.list.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      return this.list.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return this.list.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      return this.list.retainAll(var1);
   }

   public String toString() {
      return String.valueOf(this.list);
   }

   public ThreadContext.ContextStack copy() {
      return this.copy();
   }

   public boolean add(Object var1) {
      return this.add((String)var1);
   }
}
