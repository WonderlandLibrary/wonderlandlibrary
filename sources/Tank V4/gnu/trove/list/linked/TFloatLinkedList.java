package gnu.trove.list.linked;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.list.TFloatList;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class TFloatLinkedList implements TFloatList, Externalizable {
   float no_entry_value;
   int size;
   TFloatLinkedList.TFloatLink head = null;
   TFloatLinkedList.TFloatLink tail;

   public TFloatLinkedList() {
      this.tail = this.head;
   }

   public TFloatLinkedList(float var1) {
      this.tail = this.head;
      this.no_entry_value = var1;
   }

   public TFloatLinkedList(TFloatList var1) {
      this.tail = this.head;
      this.no_entry_value = var1.getNoEntryValue();
      TFloatIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         float var3 = var2.next();
         this.add(var3);
      }

   }

   public float getNoEntryValue() {
      return this.no_entry_value;
   }

   public int size() {
      return this.size;
   }

   public void add(float[] var1) {
      float[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float var5 = var2[var4];
         this.add(var5);
      }

   }

   public void add(float[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         float var5 = var1[var2 + var4];
         this.add(var5);
      }

   }

   public void insert(int var1, float var2) {
      TFloatLinkedList var3 = new TFloatLinkedList();
      var3.add(var2);
      this.insert(var1, var3);
   }

   public void insert(int var1, float[] var2) {
      this.insert(var1, link(var2, 0, var2.length));
   }

   public void insert(int var1, float[] var2, int var3, int var4) {
      this.insert(var1, link(var2, var3, var4));
   }

   void insert(int var1, TFloatLinkedList var2) {
      TFloatLinkedList.TFloatLink var3 = this.getLinkAt(var1);
      this.size += var2.size;
      if (var3 == this.head) {
         var2.tail.setNext(this.head);
         this.head.setPrevious(var2.tail);
         this.head = var2.head;
      } else {
         if (var3 == null) {
            if (this.size == 0) {
               this.head = var2.head;
               this.tail = var2.tail;
            } else {
               this.tail.setNext(var2.head);
               var2.head.setPrevious(this.tail);
               this.tail = var2.tail;
            }
         } else {
            TFloatLinkedList.TFloatLink var4 = var3.getPrevious();
            var3.getPrevious().setNext(var2.head);
            var2.tail.setNext(var3);
            var3.setPrevious(var2.tail);
            var2.head.setPrevious(var4);
         }

      }
   }

   static TFloatLinkedList link(float[] var0, int var1, int var2) {
      TFloatLinkedList var3 = new TFloatLinkedList();

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(var0[var1 + var4]);
      }

      return var3;
   }

   public float get(int var1) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TFloatLinkedList.TFloatLink var2 = this.getLinkAt(var1);
         return var2 == null ? this.no_entry_value : var2.getValue();
      }
   }

   public TFloatLinkedList.TFloatLink getLinkAt(int var1) {
      if (var1 >= this.size()) {
         return null;
      } else {
         return var1 <= this.size() >>> 1 ? getLink(this.head, 0, var1, true) : getLink(this.tail, this.size() - 1, var1, false);
      }
   }

   private static TFloatLinkedList.TFloatLink getLink(TFloatLinkedList.TFloatLink var0, int var1, int var2) {
      return getLink(var0, var1, var2, true);
   }

   private static TFloatLinkedList.TFloatLink getLink(TFloatLinkedList.TFloatLink var0, int var1, int var2, boolean var3) {
      for(int var4 = var1; var0 != null; var0 = var3 ? var0.getNext() : var0.getPrevious()) {
         if (var4 == var2) {
            return var0;
         }

         var4 += var3 ? 1 : -1;
      }

      return null;
   }

   public float set(int var1, float var2) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TFloatLinkedList.TFloatLink var3 = this.getLinkAt(var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException("at offset " + var1);
         } else {
            float var4 = var3.getValue();
            var3.setValue(var2);
            return var4;
         }
      }
   }

   public void set(int var1, float[] var2) {
      this.set(var1, var2, 0, var2.length);
   }

   public void set(int var1, float[] var2, int var3, int var4) {
      for(int var5 = 0; var5 < var4; ++var5) {
         float var6 = var2[var3 + var5];
         this.set(var1 + var5, var6);
      }

   }

   public float replace(int var1, float var2) {
      return this.set(var1, var2);
   }

   public void clear() {
      this.size = 0;
      this.head = null;
      this.tail = null;
   }

   public boolean remove(float var1) {
      boolean var2 = false;

      for(TFloatLinkedList.TFloatLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (var3.getValue() == var1) {
            var2 = true;
            this.removeLink(var3);
         }
      }

      return var2;
   }

   private void removeLink(TFloatLinkedList.TFloatLink var1) {
      if (var1 != null) {
         --this.size;
         TFloatLinkedList.TFloatLink var2 = var1.getPrevious();
         TFloatLinkedList.TFloatLink var3 = var1.getNext();
         if (var2 != null) {
            var2.setNext(var3);
         } else {
            this.head = var3;
         }

         if (var3 != null) {
            var3.setPrevious(var2);
         } else {
            this.tail = var2;
         }

         var1.setNext((TFloatLinkedList.TFloatLink)null);
         var1.setPrevious((TFloatLinkedList.TFloatLink)null);
      }
   }

   public boolean containsAll(Collection var1) {
      if (this == false) {
         return false;
      } else {
         Iterator var2 = var1.iterator();

         Float var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Float)) {
               return false;
            }

            var4 = (Float)var3;
         } while(var4 == false);

         return false;
      }
   }

   public boolean containsAll(TFloatCollection var1) {
      if (this == false) {
         return false;
      } else {
         TFloatIterator var2 = var1.iterator();

         float var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = var2.next();
         } while(var3 == false);

         return false;
      }
   }

   public boolean containsAll(float[] var1) {
      if (this == false) {
         return false;
      } else {
         float[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            float var5 = var2[var4];
            if (var5 != false) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean addAll(Collection var1) {
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Float var4 = (Float)var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(TFloatCollection var1) {
      boolean var2 = false;
      TFloatIterator var3 = var1.iterator();

      while(var3.hasNext()) {
         float var4 = var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(float[] var1) {
      boolean var2 = false;
      float[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         float var6 = var3[var5];
         if (var6 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(TFloatCollection var1) {
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(float[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (Arrays.binarySearch(var1, var3.next()) < 0) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(Collection var1) {
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(TFloatCollection var1) {
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(float[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (Arrays.binarySearch(var1, var3.next()) >= 0) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public float removeAt(int var1) {
      TFloatLinkedList.TFloatLink var2 = this.getLinkAt(var1);
      if (var2 == null) {
         throw new ArrayIndexOutOfBoundsException("no elemenet at " + var1);
      } else {
         float var3 = var2.getValue();
         this.removeLink(var2);
         return var3;
      }
   }

   public void remove(int var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         this.removeAt(var1);
      }

   }

   public void transformValues(TFloatFunction var1) {
      for(TFloatLinkedList.TFloatLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         var2.setValue(var1.execute(var2.getValue()));
      }

   }

   public void reverse() {
      TFloatLinkedList.TFloatLink var1 = this.head;
      TFloatLinkedList.TFloatLink var2 = this.tail;
      TFloatLinkedList.TFloatLink var6 = this.head;

      while(var6 != null) {
         TFloatLinkedList.TFloatLink var4 = var6.getNext();
         TFloatLinkedList.TFloatLink var3 = var6.getPrevious();
         TFloatLinkedList.TFloatLink var5 = var6;
         var6 = var6.getNext();
         var5.setNext(var3);
         var5.setPrevious(var4);
      }

      this.head = var2;
      this.tail = var1;
   }

   public void reverse(int var1, int var2) {
      if (var1 > var2) {
         throw new IllegalArgumentException("from > to : " + var1 + ">" + var2);
      } else {
         TFloatLinkedList.TFloatLink var3 = this.getLinkAt(var1);
         TFloatLinkedList.TFloatLink var4 = this.getLinkAt(var2);
         TFloatLinkedList.TFloatLink var7 = null;
         TFloatLinkedList.TFloatLink var8 = var3.getPrevious();
         TFloatLinkedList.TFloatLink var9 = var3;

         while(var9 != var4) {
            TFloatLinkedList.TFloatLink var6 = var9.getNext();
            TFloatLinkedList.TFloatLink var5 = var9.getPrevious();
            var7 = var9;
            var9 = var9.getNext();
            var7.setNext(var5);
            var7.setPrevious(var6);
         }

         if (var7 != null) {
            var8.setNext(var7);
            var4.setPrevious(var8);
         }

         var3.setNext(var4);
         var4.setPrevious(var3);
      }
   }

   public void shuffle(Random var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         TFloatLinkedList.TFloatLink var3 = this.getLinkAt(var1.nextInt(this.size()));
         this.removeLink(var3);
         this.add(var3.getValue());
      }

   }

   public TFloatList subList(int var1, int var2) {
      if (var2 < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than end index " + var2);
      } else if (this.size < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than last index " + this.size);
      } else if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var2 > this.size) {
         throw new IndexOutOfBoundsException("end index < " + this.size);
      } else {
         TFloatLinkedList var3 = new TFloatLinkedList();
         TFloatLinkedList.TFloatLink var4 = this.getLinkAt(var1);

         for(int var5 = var1; var5 < var2; ++var5) {
            var3.add(var4.getValue());
            var4 = var4.getNext();
         }

         return var3;
      }
   }

   public float[] toArray() {
      return this.toArray(new float[this.size], 0, this.size);
   }

   public float[] toArray(int var1, int var2) {
      return this.toArray(new float[var2], var1, 0, var2);
   }

   public float[] toArray(float[] var1) {
      return this.toArray(var1, 0, this.size);
   }

   public float[] toArray(float[] var1, int var2, int var3) {
      return this.toArray(var1, var2, 0, var3);
   }

   public float[] toArray(float[] var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         return var1;
      } else if (var2 >= 0 && var2 < this.size()) {
         TFloatLinkedList.TFloatLink var5 = this.getLinkAt(var2);

         for(int var6 = 0; var6 < var4; ++var6) {
            var1[var3 + var6] = var5.getValue();
            var5 = var5.getNext();
         }

         return var1;
      } else {
         throw new ArrayIndexOutOfBoundsException(var2);
      }
   }

   public boolean forEach(TFloatProcedure var1) {
      for(TFloatLinkedList.TFloatLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         if (!var1.execute(var2.getValue())) {
            return false;
         }
      }

      return true;
   }

   public boolean forEachDescending(TFloatProcedure var1) {
      for(TFloatLinkedList.TFloatLink var2 = this.tail; var2 != null; var2 = var2.getPrevious()) {
         if (!var1.execute(var2.getValue())) {
            return false;
         }
      }

      return true;
   }

   public void sort() {
      this.sort(0, this.size);
   }

   public void sort(int var1, int var2) {
      TFloatList var3 = this.subList(var1, var2);
      float[] var4 = var3.toArray();
      Arrays.sort(var4);
      this.set(var1, var4);
   }

   public void fill(float var1) {
      this.fill(0, this.size, var1);
   }

   public void fill(int var1, int var2, float var3) {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else {
         TFloatLinkedList.TFloatLink var4 = this.getLinkAt(var1);
         int var5;
         if (var2 > this.size) {
            for(var5 = var1; var5 < this.size; ++var5) {
               var4.setValue(var3);
               var4 = var4.getNext();
            }

            for(var5 = this.size; var5 < var2; ++var5) {
               this.add(var3);
            }
         } else {
            for(var5 = var1; var5 < var2; ++var5) {
               var4.setValue(var3);
               var4 = var4.getNext();
            }
         }

      }
   }

   public int binarySearch(float var1) {
      return this.binarySearch(var1, 0, this.size());
   }

   public int binarySearch(float var1, int var2, int var3) {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var3 > this.size) {
         throw new IndexOutOfBoundsException("end index > size: " + var3 + " > " + this.size);
      } else if (var3 < var2) {
         return -(var2 + 1);
      } else {
         int var6 = var2;
         TFloatLinkedList.TFloatLink var7 = this.getLinkAt(var2);
         int var8 = var3;

         while(var6 < var8) {
            int var5 = var6 + var8 >>> 1;
            TFloatLinkedList.TFloatLink var4 = getLink(var7, var6, var5);
            if (var4.getValue() == var1) {
               return var5;
            }

            if (var4.getValue() < var1) {
               var6 = var5 + 1;
               var7 = var4.next;
            } else {
               var8 = var5 - 1;
            }
         }

         return -(var6 + 1);
      }
   }

   public int indexOf(float var1) {
      return this.indexOf(0, var1);
   }

   public int indexOf(int var1, float var2) {
      int var3 = var1;

      for(TFloatLinkedList.TFloatLink var4 = this.getLinkAt(var1); var4.getNext() != null; var4 = var4.getNext()) {
         if (var4.getValue() == var2) {
            return var3;
         }

         ++var3;
      }

      return -1;
   }

   public int lastIndexOf(float var1) {
      return this.lastIndexOf(0, var1);
   }

   public int lastIndexOf(int var1, float var2) {
      if (this == false) {
         return -1;
      } else {
         int var3 = -1;
         int var4 = var1;

         for(TFloatLinkedList.TFloatLink var5 = this.getLinkAt(var1); var5.getNext() != null; var5 = var5.getNext()) {
            if (var5.getValue() == var2) {
               var3 = var4;
            }

            ++var4;
         }

         return var3;
      }
   }

   public TFloatIterator iterator() {
      return new TFloatIterator(this) {
         TFloatLinkedList.TFloatLink l;
         TFloatLinkedList.TFloatLink current;
         final TFloatLinkedList this$0;

         {
            this.this$0 = var1;
            this.l = this.this$0.head;
         }

         public float next() {
            if (TFloatLinkedList.no(this.l)) {
               throw new NoSuchElementException();
            } else {
               float var1 = this.l.getValue();
               this.current = this.l;
               this.l = this.l.getNext();
               return var1;
            }
         }

         public boolean hasNext() {
            return TFloatLinkedList.got(this.l);
         }

         public void remove() {
            if (this.current == null) {
               throw new IllegalStateException();
            } else {
               TFloatLinkedList.access$000(this.this$0, this.current);
               this.current = null;
            }
         }
      };
   }

   public TFloatList grep(TFloatProcedure var1) {
      TFloatLinkedList var2 = new TFloatLinkedList();

      for(TFloatLinkedList.TFloatLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public TFloatList inverseGrep(TFloatProcedure var1) {
      TFloatLinkedList var2 = new TFloatLinkedList();

      for(TFloatLinkedList.TFloatLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (!var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public float max() {
      float var1 = Float.NEGATIVE_INFINITY;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TFloatLinkedList.TFloatLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
            if (var1 < var2.getValue()) {
               var1 = var2.getValue();
            }
         }

         return var1;
      }
   }

   public float min() {
      float var1 = Float.POSITIVE_INFINITY;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TFloatLinkedList.TFloatLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
            if (var1 > var2.getValue()) {
               var1 = var2.getValue();
            }
         }

         return var1;
      }
   }

   public float sum() {
      float var1 = 0.0F;

      for(TFloatLinkedList.TFloatLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         var1 += var2.getValue();
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeFloat(this.no_entry_value);
      var1.writeInt(this.size);
      TFloatIterator var2 = this.iterator();

      while(var2.hasNext()) {
         float var3 = var2.next();
         var1.writeFloat(var3);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this.no_entry_value = var1.readFloat();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.add(var1.readFloat());
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         TFloatLinkedList var2 = (TFloatLinkedList)var1;
         if (this.no_entry_value != var2.no_entry_value) {
            return false;
         } else if (this.size != var2.size) {
            return false;
         } else {
            TFloatIterator var3 = this.iterator();
            TFloatIterator var4 = var2.iterator();

            do {
               if (!var3.hasNext()) {
                  return true;
               }

               if (!var4.hasNext()) {
                  return false;
               }
            } while(var3.next() == var4.next());

            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = HashFunctions.hash(this.no_entry_value);
      var1 = 31 * var1 + this.size;

      for(TFloatIterator var2 = this.iterator(); var2.hasNext(); var1 = 31 * var1 + HashFunctions.hash(var2.next())) {
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      TFloatIterator var2 = this.iterator();

      while(var2.hasNext()) {
         float var3 = var2.next();
         var1.append(var3);
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append("}");
      return var1.toString();
   }

   static void access$000(TFloatLinkedList var0, TFloatLinkedList.TFloatLink var1) {
      var0.removeLink(var1);
   }

   class RemoveProcedure implements TFloatProcedure {
      boolean changed;
      final TFloatLinkedList this$0;

      RemoveProcedure(TFloatLinkedList var1) {
         this.this$0 = var1;
         this.changed = false;
      }

      public boolean execute(float var1) {
         if (this.this$0.remove(var1)) {
            this.changed = true;
         }

         return true;
      }

      public boolean isChanged() {
         return this.changed;
      }
   }

   static class TFloatLink {
      float value;
      TFloatLinkedList.TFloatLink previous;
      TFloatLinkedList.TFloatLink next;

      TFloatLink(float var1) {
         this.value = var1;
      }

      public float getValue() {
         return this.value;
      }

      public void setValue(float var1) {
         this.value = var1;
      }

      public TFloatLinkedList.TFloatLink getPrevious() {
         return this.previous;
      }

      public void setPrevious(TFloatLinkedList.TFloatLink var1) {
         this.previous = var1;
      }

      public TFloatLinkedList.TFloatLink getNext() {
         return this.next;
      }

      public void setNext(TFloatLinkedList.TFloatLink var1) {
         this.next = var1;
      }
   }
}
