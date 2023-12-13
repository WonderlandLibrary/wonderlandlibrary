package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Set;

@GwtCompatible(
   emulated = true
)
public final class EnumMultiset extends AbstractMapBasedMultiset {
   private transient Class type;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static EnumMultiset create(Class var0) {
      return new EnumMultiset(var0);
   }

   public static EnumMultiset create(Iterable var0) {
      Iterator var1 = var0.iterator();
      Preconditions.checkArgument(var1.hasNext(), "EnumMultiset constructor passed empty Iterable");
      EnumMultiset var2 = new EnumMultiset(((Enum)var1.next()).getDeclaringClass());
      Iterables.addAll(var2, var0);
      return var2;
   }

   public static EnumMultiset create(Iterable var0, Class var1) {
      EnumMultiset var2 = create(var1);
      Iterables.addAll(var2, var0);
      return var2;
   }

   private EnumMultiset(Class var1) {
      super(WellBehavedMap.wrap(new EnumMap(var1)));
      this.type = var1;
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.type);
      Serialization.writeMultiset(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Class var2 = (Class)var1.readObject();
      this.type = var2;
      this.setBackingMap(WellBehavedMap.wrap(new EnumMap(this.type)));
      Serialization.populateMultiset(this, var1);
   }

   public int remove(Object var1, int var2) {
      return super.remove(var1, var2);
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

   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }
}
