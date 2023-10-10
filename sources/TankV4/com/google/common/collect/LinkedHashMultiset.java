package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class LinkedHashMultiset extends AbstractMapBasedMultiset {
   @GwtIncompatible("not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static LinkedHashMultiset create() {
      return new LinkedHashMultiset();
   }

   public static LinkedHashMultiset create(int var0) {
      return new LinkedHashMultiset(var0);
   }

   public static LinkedHashMultiset create(Iterable var0) {
      LinkedHashMultiset var1 = create(Multisets.inferDistinctElements(var0));
      Iterables.addAll(var1, var0);
      return var1;
   }

   private LinkedHashMultiset() {
      super(new LinkedHashMap());
   }

   private LinkedHashMultiset(int var1) {
      super(new LinkedHashMap(Maps.capacity(var1)));
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Serialization.writeMultiset(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = Serialization.readCount(var1);
      this.setBackingMap(new LinkedHashMap(Maps.capacity(var2)));
      Serialization.populateMultiset(this, var1, var2);
   }

   public int setCount(Object var1, int var2) {
      return super.setCount(var1, var2);
   }

   public int remove(Object var1, int var2) {
      return super.remove(var1, var2);
   }

   public int add(Object var1, int var2) {
      return super.add(var1, var2);
   }

   public int count(Object var1) {
      return super.count(var1);
   }

   public Iterator iterator() {
      return super.iterator();
   }

   public int size() {
      return super.size();
   }

   public void clear() {
      super.clear();
   }

   public Set entrySet() {
      return super.entrySet();
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public Set elementSet() {
      return super.elementSet();
   }

   public boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return super.removeAll(var1);
   }

   public boolean addAll(Collection var1) {
      return super.addAll(var1);
   }

   public boolean setCount(Object var1, int var2, int var3) {
      return super.setCount(var1, var2, var3);
   }

   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   public boolean add(Object var1) {
      return super.add(var1);
   }

   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }
}
