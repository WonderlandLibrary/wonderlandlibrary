package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.ThreadContext;

public class DefaultThreadContextStack implements ThreadContextStack {
   private static final long serialVersionUID = 5050501L;
   private static ThreadLocal stack = new ThreadLocal();
   private final boolean useStack;

   public DefaultThreadContextStack(boolean var1) {
      this.useStack = var1;
   }

   public String pop() {
      if (!this.useStack) {
         return "";
      } else {
         List var1 = (List)stack.get();
         if (var1 != null && var1.size() != 0) {
            ArrayList var2 = new ArrayList(var1);
            int var3 = var2.size() - 1;
            String var4 = (String)var2.remove(var3);
            stack.set(Collections.unmodifiableList(var2));
            return var4;
         } else {
            throw new NoSuchElementException("The ThreadContext stack is empty");
         }
      }
   }

   public String peek() {
      List var1 = (List)stack.get();
      if (var1 != null && var1.size() != 0) {
         int var2 = var1.size() - 1;
         return (String)var1.get(var2);
      } else {
         return null;
      }
   }

   public void push(String var1) {
      if (this.useStack) {
         this.add(var1);
      }
   }

   public int getDepth() {
      List var1 = (List)stack.get();
      return var1 == null ? 0 : var1.size();
   }

   public List asList() {
      List var1 = (List)stack.get();
      return var1 == null ? Collections.emptyList() : var1;
   }

   public void trim(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Maximum stack depth cannot be negative");
      } else {
         List var2 = (List)stack.get();
         if (var2 != null) {
            ArrayList var3 = new ArrayList();
            int var4 = Math.min(var1, var2.size());

            for(int var5 = 0; var5 < var4; ++var5) {
               var3.add(var2.get(var5));
            }

            stack.set(var3);
         }
      }
   }

   public ThreadContextStack copy() {
      List var1 = null;
      return this.useStack && (var1 = (List)stack.get()) != null ? new MutableThreadContextStack(var1) : new MutableThreadContextStack(new ArrayList());
   }

   public void clear() {
      stack.remove();
   }

   public int size() {
      List var1 = (List)stack.get();
      return var1 == null ? 0 : var1.size();
   }

   public boolean isEmpty() {
      List var1 = (List)stack.get();
      return var1 == null || var1.isEmpty();
   }

   public boolean contains(Object var1) {
      List var2 = (List)stack.get();
      return var2 != null && var2.contains(var1);
   }

   public Iterator iterator() {
      List var1 = (List)stack.get();
      if (var1 == null) {
         List var2 = Collections.emptyList();
         return var2.iterator();
      } else {
         return var1.iterator();
      }
   }

   public Object[] toArray() {
      List var1 = (List)stack.get();
      return (Object[])(var1 == null ? new String[0] : var1.toArray(new Object[var1.size()]));
   }

   public Object[] toArray(Object[] var1) {
      List var2 = (List)stack.get();
      if (var2 == null) {
         if (var1.length > 0) {
            var1[0] = null;
         }

         return var1;
      } else {
         return var2.toArray(var1);
      }
   }

   public boolean add(String var1) {
      if (!this.useStack) {
         return false;
      } else {
         List var2 = (List)stack.get();
         ArrayList var3 = var2 == null ? new ArrayList() : new ArrayList(var2);
         var3.add(var1);
         stack.set(Collections.unmodifiableList(var3));
         return true;
      }
   }

   public boolean remove(Object var1) {
      if (!this.useStack) {
         return false;
      } else {
         List var2 = (List)stack.get();
         if (var2 != null && var2.size() != 0) {
            ArrayList var3 = new ArrayList(var2);
            boolean var4 = var3.remove(var1);
            stack.set(Collections.unmodifiableList(var3));
            return var4;
         } else {
            return false;
         }
      }
   }

   public boolean containsAll(Collection var1) {
      if (var1.isEmpty()) {
         return true;
      } else {
         List var2 = (List)stack.get();
         return var2 != null && var2.containsAll(var1);
      }
   }

   public boolean addAll(Collection var1) {
      if (this.useStack && !var1.isEmpty()) {
         List var2 = (List)stack.get();
         ArrayList var3 = var2 == null ? new ArrayList() : new ArrayList(var2);
         var3.addAll(var1);
         stack.set(Collections.unmodifiableList(var3));
         return true;
      } else {
         return false;
      }
   }

   public boolean removeAll(Collection var1) {
      if (this.useStack && !var1.isEmpty()) {
         List var2 = (List)stack.get();
         if (var2 != null && !var2.isEmpty()) {
            ArrayList var3 = new ArrayList(var2);
            boolean var4 = var3.removeAll(var1);
            stack.set(Collections.unmodifiableList(var3));
            return var4;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean retainAll(Collection var1) {
      if (this.useStack && !var1.isEmpty()) {
         List var2 = (List)stack.get();
         if (var2 != null && !var2.isEmpty()) {
            ArrayList var3 = new ArrayList(var2);
            boolean var4 = var3.retainAll(var1);
            stack.set(Collections.unmodifiableList(var3));
            return var4;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public String toString() {
      List var1 = (List)stack.get();
      return var1 == null ? "[]" : var1.toString();
   }

   public ThreadContext.ContextStack copy() {
      return this.copy();
   }

   public boolean add(Object var1) {
      return this.add((String)var1);
   }
}
