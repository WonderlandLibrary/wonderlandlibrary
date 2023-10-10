package gnu.trove.list.linked;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class TByteLinkedList implements TByteList, Externalizable {
   byte no_entry_value;
   int size;
   TByteLinkedList.TByteLink head = null;
   TByteLinkedList.TByteLink tail;

   public TByteLinkedList() {
      this.tail = this.head;
   }

   public TByteLinkedList(byte var1) {
      this.tail = this.head;
      this.no_entry_value = var1;
   }

   public TByteLinkedList(TByteList var1) {
      this.tail = this.head;
      this.no_entry_value = var1.getNoEntryValue();
      TByteIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         byte var3 = var2.next();
         this.add(var3);
      }

   }

   public byte getNoEntryValue() {
      return this.no_entry_value;
   }

   public int size() {
      return this.size;
   }

   public void add(byte[] var1) {
      byte[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte var5 = var2[var4];
         this.add(var5);
      }

   }

   public void add(byte[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         byte var5 = var1[var2 + var4];
         this.add(var5);
      }

   }

   public void insert(int var1, byte var2) {
      TByteLinkedList var3 = new TByteLinkedList();
      var3.add(var2);
      this.insert(var1, var3);
   }

   public void insert(int var1, byte[] var2) {
      this.insert(var1, link(var2, 0, var2.length));
   }

   public void insert(int var1, byte[] var2, int var3, int var4) {
      this.insert(var1, link(var2, var3, var4));
   }

   void insert(int var1, TByteLinkedList var2) {
      TByteLinkedList.TByteLink var3 = this.getLinkAt(var1);
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
            TByteLinkedList.TByteLink var4 = var3.getPrevious();
            var3.getPrevious().setNext(var2.head);
            var2.tail.setNext(var3);
            var3.setPrevious(var2.tail);
            var2.head.setPrevious(var4);
         }

      }
   }

   static TByteLinkedList link(byte[] var0, int var1, int var2) {
      TByteLinkedList var3 = new TByteLinkedList();

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(var0[var1 + var4]);
      }

      return var3;
   }

   public byte get(int var1) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TByteLinkedList.TByteLink var2 = this.getLinkAt(var1);
         return var2 == null ? this.no_entry_value : var2.getValue();
      }
   }

   public TByteLinkedList.TByteLink getLinkAt(int var1) {
      if (var1 >= this.size()) {
         return null;
      } else {
         return var1 <= this.size() >>> 1 ? getLink(this.head, 0, var1, true) : getLink(this.tail, this.size() - 1, var1, false);
      }
   }

   private static TByteLinkedList.TByteLink getLink(TByteLinkedList.TByteLink var0, int var1, int var2) {
      return getLink(var0, var1, var2, true);
   }

   private static TByteLinkedList.TByteLink getLink(TByteLinkedList.TByteLink var0, int var1, int var2, boolean var3) {
      for(int var4 = var1; var0 != null; var0 = var3 ? var0.getNext() : var0.getPrevious()) {
         if (var4 == var2) {
            return var0;
         }

         var4 += var3 ? 1 : -1;
      }

      return null;
   }

   public byte set(int var1, byte var2) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TByteLinkedList.TByteLink var3 = this.getLinkAt(var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException("at offset " + var1);
         } else {
            byte var4 = var3.getValue();
            var3.setValue(var2);
            return var4;
         }
      }
   }

   public void set(int var1, byte[] var2) {
      this.set(var1, var2, 0, var2.length);
   }

   public void set(int var1, byte[] var2, int var3, int var4) {
      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var2[var3 + var5];
         this.set(var1 + var5, var6);
      }

   }

   public byte replace(int var1, byte var2) {
      return this.set(var1, var2);
   }

   public void clear() {
      this.size = 0;
      this.head = null;
      this.tail = null;
   }

   public boolean remove(byte var1) {
      boolean var2 = false;

      for(TByteLinkedList.TByteLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (var3.getValue() == var1) {
            var2 = true;
            this.removeLink(var3);
         }
      }

      return var2;
   }

   private void removeLink(TByteLinkedList.TByteLink var1) {
      if (var1 != null) {
         --this.size;
         TByteLinkedList.TByteLink var2 = var1.getPrevious();
         TByteLinkedList.TByteLink var3 = var1.getNext();
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

         var1.setNext((TByteLinkedList.TByteLink)null);
         var1.setPrevious((TByteLinkedList.TByteLink)null);
      }
   }

   public boolean containsAll(Collection var1) {
      if (this == false) {
         return false;
      } else {
         Iterator var2 = var1.iterator();

         Byte var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Byte)) {
               return false;
            }

            var4 = (Byte)var3;
         } while(var4 == 0);

         return false;
      }
   }

   public boolean containsAll(TByteCollection var1) {
      if (this == false) {
         return false;
      } else {
         TByteIterator var2 = var1.iterator();

         byte var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = var2.next();
         } while(var3 == 0);

         return false;
      }
   }

   public boolean containsAll(byte[] var1) {
      if (this == false) {
         return false;
      } else {
         byte[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            if (var5 != 0) {
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
         Byte var4 = (Byte)var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(TByteCollection var1) {
      boolean var2 = false;
      TByteIterator var3 = var1.iterator();

      while(var3.hasNext()) {
         byte var4 = var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(byte[] var1) {
      boolean var2 = false;
      byte[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var3[var5];
         if (var6 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      boolean var2 = false;
      TByteIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(TByteCollection var1) {
      boolean var2 = false;
      TByteIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(byte[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TByteIterator var3 = this.iterator();

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
      TByteIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(TByteCollection var1) {
      boolean var2 = false;
      TByteIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(byte[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TByteIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (Arrays.binarySearch(var1, var3.next()) >= 0) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public byte removeAt(int var1) {
      TByteLinkedList.TByteLink var2 = this.getLinkAt(var1);
      if (var2 == null) {
         throw new ArrayIndexOutOfBoundsException("no elemenet at " + var1);
      } else {
         byte var3 = var2.getValue();
         this.removeLink(var2);
         return var3;
      }
   }

   public void remove(int var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         this.removeAt(var1);
      }

   }

   public void transformValues(TByteFunction var1) {
      for(TByteLinkedList.TByteLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         var2.setValue(var1.execute(var2.getValue()));
      }

   }

   public void reverse() {
      TByteLinkedList.TByteLink var1 = this.head;
      TByteLinkedList.TByteLink var2 = this.tail;
      TByteLinkedList.TByteLink var6 = this.head;

      while(var6 != null) {
         TByteLinkedList.TByteLink var4 = var6.getNext();
         TByteLinkedList.TByteLink var3 = var6.getPrevious();
         TByteLinkedList.TByteLink var5 = var6;
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
         TByteLinkedList.TByteLink var3 = this.getLinkAt(var1);
         TByteLinkedList.TByteLink var4 = this.getLinkAt(var2);
         TByteLinkedList.TByteLink var7 = null;
         TByteLinkedList.TByteLink var8 = var3.getPrevious();
         TByteLinkedList.TByteLink var9 = var3;

         while(var9 != var4) {
            TByteLinkedList.TByteLink var6 = var9.getNext();
            TByteLinkedList.TByteLink var5 = var9.getPrevious();
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
         TByteLinkedList.TByteLink var3 = this.getLinkAt(var1.nextInt(this.size()));
         this.removeLink(var3);
         this.add(var3.getValue());
      }

   }

   public TByteList subList(int var1, int var2) {
      if (var2 < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than end index " + var2);
      } else if (this.size < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than last index " + this.size);
      } else if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var2 > this.size) {
         throw new IndexOutOfBoundsException("end index < " + this.size);
      } else {
         TByteLinkedList var3 = new TByteLinkedList();
         TByteLinkedList.TByteLink var4 = this.getLinkAt(var1);

         for(int var5 = var1; var5 < var2; ++var5) {
            var3.add(var4.getValue());
            var4 = var4.getNext();
         }

         return var3;
      }
   }

   public byte[] toArray() {
      return this.toArray(new byte[this.size], 0, this.size);
   }

   public byte[] toArray(int var1, int var2) {
      return this.toArray(new byte[var2], var1, 0, var2);
   }

   public byte[] toArray(byte[] var1) {
      return this.toArray(var1, 0, this.size);
   }

   public byte[] toArray(byte[] var1, int var2, int var3) {
      return this.toArray(var1, var2, 0, var3);
   }

   public byte[] toArray(byte[] var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         return var1;
      } else if (var2 >= 0 && var2 < this.size()) {
         TByteLinkedList.TByteLink var5 = this.getLinkAt(var2);

         for(int var6 = 0; var6 < var4; ++var6) {
            var1[var3 + var6] = var5.getValue();
            var5 = var5.getNext();
         }

         return var1;
      } else {
         throw new ArrayIndexOutOfBoundsException(var2);
      }
   }

   public boolean forEach(TByteProcedure var1) {
      for(TByteLinkedList.TByteLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         if (!var1.execute(var2.getValue())) {
            return false;
         }
      }

      return true;
   }

   public boolean forEachDescending(TByteProcedure var1) {
      for(TByteLinkedList.TByteLink var2 = this.tail; var2 != null; var2 = var2.getPrevious()) {
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
      TByteList var3 = this.subList(var1, var2);
      byte[] var4 = var3.toArray();
      Arrays.sort(var4);
      this.set(var1, var4);
   }

   public void fill(byte var1) {
      this.fill(0, this.size, var1);
   }

   public void fill(int var1, int var2, byte var3) {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else {
         TByteLinkedList.TByteLink var4 = this.getLinkAt(var1);
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

   public int binarySearch(byte var1) {
      return this.binarySearch(var1, 0, this.size());
   }

   public int binarySearch(byte var1, int var2, int var3) {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var3 > this.size) {
         throw new IndexOutOfBoundsException("end index > size: " + var3 + " > " + this.size);
      } else if (var3 < var2) {
         return -(var2 + 1);
      } else {
         int var6 = var2;
         TByteLinkedList.TByteLink var7 = this.getLinkAt(var2);
         int var8 = var3;

         while(var6 < var8) {
            int var5 = var6 + var8 >>> 1;
            TByteLinkedList.TByteLink var4 = getLink(var7, var6, var5);
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

   public int indexOf(byte var1) {
      return this.indexOf(0, var1);
   }

   public int indexOf(int var1, byte var2) {
      int var3 = var1;

      for(TByteLinkedList.TByteLink var4 = this.getLinkAt(var1); var4.getNext() != null; var4 = var4.getNext()) {
         if (var4.getValue() == var2) {
            return var3;
         }

         ++var3;
      }

      return -1;
   }

   public int lastIndexOf(byte var1) {
      return this.lastIndexOf(0, var1);
   }

   public int lastIndexOf(int var1, byte var2) {
      if (this == false) {
         return -1;
      } else {
         int var3 = -1;
         int var4 = var1;

         for(TByteLinkedList.TByteLink var5 = this.getLinkAt(var1); var5.getNext() != null; var5 = var5.getNext()) {
            if (var5.getValue() == var2) {
               var3 = var4;
            }

            ++var4;
         }

         return var3;
      }
   }

   public TByteIterator iterator() {
      return new TByteIterator(this) {
         TByteLinkedList.TByteLink l;
         TByteLinkedList.TByteLink current;
         final TByteLinkedList this$0;

         {
            this.this$0 = var1;
            this.l = this.this$0.head;
         }

         public byte next() {
            if (TByteLinkedList.no(this.l)) {
               throw new NoSuchElementException();
            } else {
               byte var1 = this.l.getValue();
               this.current = this.l;
               this.l = this.l.getNext();
               return var1;
            }
         }

         public boolean hasNext() {
            return TByteLinkedList.got(this.l);
         }

         public void remove() {
            if (this.current == null) {
               throw new IllegalStateException();
            } else {
               TByteLinkedList.access$000(this.this$0, this.current);
               this.current = null;
            }
         }
      };
   }

   public TByteList grep(TByteProcedure var1) {
      TByteLinkedList var2 = new TByteLinkedList();

      for(TByteLinkedList.TByteLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public TByteList inverseGrep(TByteProcedure var1) {
      TByteLinkedList var2 = new TByteLinkedList();

      for(TByteLinkedList.TByteLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (!var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public byte max() {
      byte var1 = -128;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TByteLinkedList.TByteLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
            if (var1 < var2.getValue()) {
               var1 = var2.getValue();
            }
         }

         return var1;
      }
   }

   public byte min() {
      byte var1 = 127;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TByteLinkedList.TByteLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
            if (var1 > var2.getValue()) {
               var1 = var2.getValue();
            }
         }

         return var1;
      }
   }

   public byte sum() {
      byte var1 = 0;

      for(TByteLinkedList.TByteLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         var1 += var2.getValue();
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeByte(this.no_entry_value);
      var1.writeInt(this.size);
      TByteIterator var2 = this.iterator();

      while(var2.hasNext()) {
         byte var3 = var2.next();
         var1.writeByte(var3);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this.no_entry_value = var1.readByte();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.add(var1.readByte());
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         TByteLinkedList var2 = (TByteLinkedList)var1;
         if (this.no_entry_value != var2.no_entry_value) {
            return false;
         } else if (this.size != var2.size) {
            return false;
         } else {
            TByteIterator var3 = this.iterator();
            TByteIterator var4 = var2.iterator();

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

      for(TByteIterator var2 = this.iterator(); var2.hasNext(); var1 = 31 * var1 + HashFunctions.hash(var2.next())) {
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      TByteIterator var2 = this.iterator();

      while(var2.hasNext()) {
         byte var3 = var2.next();
         var1.append(var3);
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append("}");
      return var1.toString();
   }

   static void access$000(TByteLinkedList var0, TByteLinkedList.TByteLink var1) {
      var0.removeLink(var1);
   }

   class RemoveProcedure implements TByteProcedure {
      boolean changed;
      final TByteLinkedList this$0;

      RemoveProcedure(TByteLinkedList var1) {
         this.this$0 = var1;
         this.changed = false;
      }

      public boolean execute(byte var1) {
         if (this.this$0.remove(var1)) {
            this.changed = true;
         }

         return true;
      }

      public boolean isChanged() {
         return this.changed;
      }
   }

   static class TByteLink {
      byte value;
      TByteLinkedList.TByteLink previous;
      TByteLinkedList.TByteLink next;

      TByteLink(byte var1) {
         this.value = var1;
      }

      public byte getValue() {
         return this.value;
      }

      public void setValue(byte var1) {
         this.value = var1;
      }

      public TByteLinkedList.TByteLink getPrevious() {
         return this.previous;
      }

      public void setPrevious(TByteLinkedList.TByteLink var1) {
         this.previous = var1;
      }

      public TByteLinkedList.TByteLink getNext() {
         return this.next;
      }

      public void setNext(TByteLinkedList.TByteLink var1) {
         this.next = var1;
      }
   }
}
