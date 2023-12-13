package gnu.trove.list.linked;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TLongList;
import gnu.trove.procedure.TLongProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class TLongLinkedList implements TLongList, Externalizable {
   long no_entry_value;
   int size;
   TLongLinkedList.TLongLink head = null;
   TLongLinkedList.TLongLink tail;

   public TLongLinkedList() {
      this.tail = this.head;
   }

   public TLongLinkedList(long var1) {
      this.tail = this.head;
      this.no_entry_value = var1;
   }

   public TLongLinkedList(TLongList var1) {
      this.tail = this.head;
      this.no_entry_value = var1.getNoEntryValue();
      TLongIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         long var3 = var2.next();
         this.add(var3);
      }

   }

   public long getNoEntryValue() {
      return this.no_entry_value;
   }

   public int size() {
      return this.size;
   }

   public void add(long[] var1) {
      long[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var2[var4];
         this.add(var5);
      }

   }

   public void add(long[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var1[var2 + var4];
         this.add(var5);
      }

   }

   public void insert(int var1, long var2) {
      TLongLinkedList var4 = new TLongLinkedList();
      var4.add(var2);
      this.insert(var1, var4);
   }

   public void insert(int var1, long[] var2) {
      this.insert(var1, link(var2, 0, var2.length));
   }

   public void insert(int var1, long[] var2, int var3, int var4) {
      this.insert(var1, link(var2, var3, var4));
   }

   void insert(int var1, TLongLinkedList var2) {
      TLongLinkedList.TLongLink var3 = this.getLinkAt(var1);
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
            TLongLinkedList.TLongLink var4 = var3.getPrevious();
            var3.getPrevious().setNext(var2.head);
            var2.tail.setNext(var3);
            var3.setPrevious(var2.tail);
            var2.head.setPrevious(var4);
         }

      }
   }

   static TLongLinkedList link(long[] var0, int var1, int var2) {
      TLongLinkedList var3 = new TLongLinkedList();

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(var0[var1 + var4]);
      }

      return var3;
   }

   public long get(int var1) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TLongLinkedList.TLongLink var2 = this.getLinkAt(var1);
         return var2 == null ? this.no_entry_value : var2.getValue();
      }
   }

   public TLongLinkedList.TLongLink getLinkAt(int var1) {
      if (var1 >= this.size()) {
         return null;
      } else {
         return var1 <= this.size() >>> 1 ? getLink(this.head, 0, var1, true) : getLink(this.tail, this.size() - 1, var1, false);
      }
   }

   private static TLongLinkedList.TLongLink getLink(TLongLinkedList.TLongLink var0, int var1, int var2) {
      return getLink(var0, var1, var2, true);
   }

   private static TLongLinkedList.TLongLink getLink(TLongLinkedList.TLongLink var0, int var1, int var2, boolean var3) {
      for(int var4 = var1; var0 != null; var0 = var3 ? var0.getNext() : var0.getPrevious()) {
         if (var4 == var2) {
            return var0;
         }

         var4 += var3 ? 1 : -1;
      }

      return null;
   }

   public long set(int var1, long var2) {
      if (var1 > this.size) {
         throw new IndexOutOfBoundsException("index " + var1 + " exceeds size " + this.size);
      } else {
         TLongLinkedList.TLongLink var4 = this.getLinkAt(var1);
         if (var4 == null) {
            throw new IndexOutOfBoundsException("at offset " + var1);
         } else {
            long var5 = var4.getValue();
            var4.setValue(var2);
            return var5;
         }
      }
   }

   public void set(int var1, long[] var2) {
      this.set(var1, var2, 0, var2.length);
   }

   public void set(int var1, long[] var2, int var3, int var4) {
      for(int var5 = 0; var5 < var4; ++var5) {
         long var6 = var2[var3 + var5];
         this.set(var1 + var5, var6);
      }

   }

   public long replace(int var1, long var2) {
      return this.set(var1, var2);
   }

   public void clear() {
      this.size = 0;
      this.head = null;
      this.tail = null;
   }

   public boolean remove(long var1) {
      boolean var3 = false;

      for(TLongLinkedList.TLongLink var4 = this.head; var4 != null; var4 = var4.getNext()) {
         if (var4.getValue() == var1) {
            var3 = true;
            this.removeLink(var4);
         }
      }

      return var3;
   }

   private void removeLink(TLongLinkedList.TLongLink var1) {
      if (var1 != null) {
         --this.size;
         TLongLinkedList.TLongLink var2 = var1.getPrevious();
         TLongLinkedList.TLongLink var3 = var1.getNext();
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

         var1.setNext((TLongLinkedList.TLongLink)null);
         var1.setPrevious((TLongLinkedList.TLongLink)null);
      }
   }

   public boolean containsAll(Collection var1) {
      if (this == false) {
         return false;
      } else {
         Iterator var2 = var1.iterator();

         Long var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Long)) {
               return false;
            }

            var4 = (Long)var3;
         } while(var4 == false);

         return false;
      }
   }

   public boolean containsAll(TLongCollection var1) {
      if (this == false) {
         return false;
      } else {
         TLongIterator var2 = var1.iterator();

         long var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = var2.next();
         } while(var3 == false);

         return false;
      }
   }

   public boolean containsAll(long[] var1) {
      if (this == false) {
         return false;
      } else {
         long[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            long var5 = var2[var4];
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
         Long var4 = (Long)var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(TLongCollection var1) {
      boolean var2 = false;
      TLongIterator var3 = var1.iterator();

      while(var3.hasNext()) {
         long var4 = var3.next();
         if (var4 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(long[] var1) {
      boolean var2 = false;
      long[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long var6 = var3[var5];
         if (var6 == null) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      boolean var2 = false;
      TLongIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(TLongCollection var1) {
      boolean var2 = false;
      TLongIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(long[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TLongIterator var3 = this.iterator();

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
      TLongIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(TLongCollection var1) {
      boolean var2 = false;
      TLongIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(long[] var1) {
      Arrays.sort(var1);
      boolean var2 = false;
      TLongIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (Arrays.binarySearch(var1, var3.next()) >= 0) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public long removeAt(int var1) {
      TLongLinkedList.TLongLink var2 = this.getLinkAt(var1);
      if (var2 == null) {
         throw new ArrayIndexOutOfBoundsException("no elemenet at " + var1);
      } else {
         long var3 = var2.getValue();
         this.removeLink(var2);
         return var3;
      }
   }

   public void remove(int var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         this.removeAt(var1);
      }

   }

   public void transformValues(TLongFunction var1) {
      for(TLongLinkedList.TLongLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         var2.setValue(var1.execute(var2.getValue()));
      }

   }

   public void reverse() {
      TLongLinkedList.TLongLink var1 = this.head;
      TLongLinkedList.TLongLink var2 = this.tail;
      TLongLinkedList.TLongLink var6 = this.head;

      while(var6 != null) {
         TLongLinkedList.TLongLink var4 = var6.getNext();
         TLongLinkedList.TLongLink var3 = var6.getPrevious();
         TLongLinkedList.TLongLink var5 = var6;
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
         TLongLinkedList.TLongLink var3 = this.getLinkAt(var1);
         TLongLinkedList.TLongLink var4 = this.getLinkAt(var2);
         TLongLinkedList.TLongLink var7 = null;
         TLongLinkedList.TLongLink var8 = var3.getPrevious();
         TLongLinkedList.TLongLink var9 = var3;

         while(var9 != var4) {
            TLongLinkedList.TLongLink var6 = var9.getNext();
            TLongLinkedList.TLongLink var5 = var9.getPrevious();
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
         TLongLinkedList.TLongLink var3 = this.getLinkAt(var1.nextInt(this.size()));
         this.removeLink(var3);
         this.add(var3.getValue());
      }

   }

   public TLongList subList(int var1, int var2) {
      if (var2 < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than end index " + var2);
      } else if (this.size < var1) {
         throw new IllegalArgumentException("begin index " + var1 + " greater than last index " + this.size);
      } else if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var2 > this.size) {
         throw new IndexOutOfBoundsException("end index < " + this.size);
      } else {
         TLongLinkedList var3 = new TLongLinkedList();
         TLongLinkedList.TLongLink var4 = this.getLinkAt(var1);

         for(int var5 = var1; var5 < var2; ++var5) {
            var3.add(var4.getValue());
            var4 = var4.getNext();
         }

         return var3;
      }
   }

   public long[] toArray() {
      return this.toArray(new long[this.size], 0, this.size);
   }

   public long[] toArray(int var1, int var2) {
      return this.toArray(new long[var2], var1, 0, var2);
   }

   public long[] toArray(long[] var1) {
      return this.toArray(var1, 0, this.size);
   }

   public long[] toArray(long[] var1, int var2, int var3) {
      return this.toArray(var1, var2, 0, var3);
   }

   public long[] toArray(long[] var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         return var1;
      } else if (var2 >= 0 && var2 < this.size()) {
         TLongLinkedList.TLongLink var5 = this.getLinkAt(var2);

         for(int var6 = 0; var6 < var4; ++var6) {
            var1[var3 + var6] = var5.getValue();
            var5 = var5.getNext();
         }

         return var1;
      } else {
         throw new ArrayIndexOutOfBoundsException(var2);
      }
   }

   public boolean forEach(TLongProcedure var1) {
      for(TLongLinkedList.TLongLink var2 = this.head; var2 != null; var2 = var2.getNext()) {
         if (!var1.execute(var2.getValue())) {
            return false;
         }
      }

      return true;
   }

   public boolean forEachDescending(TLongProcedure var1) {
      for(TLongLinkedList.TLongLink var2 = this.tail; var2 != null; var2 = var2.getPrevious()) {
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
      TLongList var3 = this.subList(var1, var2);
      long[] var4 = var3.toArray();
      Arrays.sort(var4);
      this.set(var1, var4);
   }

   public void fill(long var1) {
      this.fill(0, this.size, var1);
   }

   public void fill(int var1, int var2, long var3) {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else {
         TLongLinkedList.TLongLink var5 = this.getLinkAt(var1);
         int var6;
         if (var2 > this.size) {
            for(var6 = var1; var6 < this.size; ++var6) {
               var5.setValue(var3);
               var5 = var5.getNext();
            }

            for(var6 = this.size; var6 < var2; ++var6) {
               this.add(var3);
            }
         } else {
            for(var6 = var1; var6 < var2; ++var6) {
               var5.setValue(var3);
               var5 = var5.getNext();
            }
         }

      }
   }

   public int binarySearch(long var1) {
      return this.binarySearch(var1, 0, this.size());
   }

   public int binarySearch(long var1, int var3, int var4) {
      if (var3 < 0) {
         throw new IndexOutOfBoundsException("begin index can not be < 0");
      } else if (var4 > this.size) {
         throw new IndexOutOfBoundsException("end index > size: " + var4 + " > " + this.size);
      } else if (var4 < var3) {
         return -(var3 + 1);
      } else {
         int var7 = var3;
         TLongLinkedList.TLongLink var8 = this.getLinkAt(var3);
         int var9 = var4;

         while(var7 < var9) {
            int var6 = var7 + var9 >>> 1;
            TLongLinkedList.TLongLink var5 = getLink(var8, var7, var6);
            if (var5.getValue() == var1) {
               return var6;
            }

            if (var5.getValue() < var1) {
               var7 = var6 + 1;
               var8 = var5.next;
            } else {
               var9 = var6 - 1;
            }
         }

         return -(var7 + 1);
      }
   }

   public int indexOf(long var1) {
      return this.indexOf(0, var1);
   }

   public int indexOf(int var1, long var2) {
      int var4 = var1;

      for(TLongLinkedList.TLongLink var5 = this.getLinkAt(var1); var5.getNext() != null; var5 = var5.getNext()) {
         if (var5.getValue() == var2) {
            return var4;
         }

         ++var4;
      }

      return -1;
   }

   public int lastIndexOf(long var1) {
      return this.lastIndexOf(0, var1);
   }

   public int lastIndexOf(int var1, long var2) {
      if (this == false) {
         return -1;
      } else {
         int var4 = -1;
         int var5 = var1;

         for(TLongLinkedList.TLongLink var6 = this.getLinkAt(var1); var6.getNext() != null; var6 = var6.getNext()) {
            if (var6.getValue() == var2) {
               var4 = var5;
            }

            ++var5;
         }

         return var4;
      }
   }

   public TLongIterator iterator() {
      return new TLongIterator(this) {
         TLongLinkedList.TLongLink l;
         TLongLinkedList.TLongLink current;
         final TLongLinkedList this$0;

         {
            this.this$0 = var1;
            this.l = this.this$0.head;
         }

         public long next() {
            if (TLongLinkedList.no(this.l)) {
               throw new NoSuchElementException();
            } else {
               long var1 = this.l.getValue();
               this.current = this.l;
               this.l = this.l.getNext();
               return var1;
            }
         }

         public boolean hasNext() {
            return TLongLinkedList.got(this.l);
         }

         public void remove() {
            if (this.current == null) {
               throw new IllegalStateException();
            } else {
               TLongLinkedList.access$000(this.this$0, this.current);
               this.current = null;
            }
         }
      };
   }

   public TLongList grep(TLongProcedure var1) {
      TLongLinkedList var2 = new TLongLinkedList();

      for(TLongLinkedList.TLongLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public TLongList inverseGrep(TLongProcedure var1) {
      TLongLinkedList var2 = new TLongLinkedList();

      for(TLongLinkedList.TLongLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         if (!var1.execute(var3.getValue())) {
            var2.add(var3.getValue());
         }
      }

      return var2;
   }

   public long max() {
      long var1 = Long.MIN_VALUE;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TLongLinkedList.TLongLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
            if (var1 < var3.getValue()) {
               var1 = var3.getValue();
            }
         }

         return var1;
      }
   }

   public long min() {
      long var1 = Long.MAX_VALUE;
      if (this == false) {
         throw new IllegalStateException();
      } else {
         for(TLongLinkedList.TLongLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
            if (var1 > var3.getValue()) {
               var1 = var3.getValue();
            }
         }

         return var1;
      }
   }

   public long sum() {
      long var1 = 0L;

      for(TLongLinkedList.TLongLink var3 = this.head; var3 != null; var3 = var3.getNext()) {
         var1 += var3.getValue();
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeLong(this.no_entry_value);
      var1.writeInt(this.size);
      TLongIterator var2 = this.iterator();

      while(var2.hasNext()) {
         long var3 = var2.next();
         var1.writeLong(var3);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this.no_entry_value = var1.readLong();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.add(var1.readLong());
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         TLongLinkedList var2 = (TLongLinkedList)var1;
         if (this.no_entry_value != var2.no_entry_value) {
            return false;
         } else if (this.size != var2.size) {
            return false;
         } else {
            TLongIterator var3 = this.iterator();
            TLongIterator var4 = var2.iterator();

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

      for(TLongIterator var2 = this.iterator(); var2.hasNext(); var1 = 31 * var1 + HashFunctions.hash(var2.next())) {
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      TLongIterator var2 = this.iterator();

      while(var2.hasNext()) {
         long var3 = var2.next();
         var1.append(var3);
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append("}");
      return var1.toString();
   }

   static void access$000(TLongLinkedList var0, TLongLinkedList.TLongLink var1) {
      var0.removeLink(var1);
   }

   class RemoveProcedure implements TLongProcedure {
      boolean changed;
      final TLongLinkedList this$0;

      RemoveProcedure(TLongLinkedList var1) {
         this.this$0 = var1;
         this.changed = false;
      }

      public boolean execute(long var1) {
         if (this.this$0.remove(var1)) {
            this.changed = true;
         }

         return true;
      }

      public boolean isChanged() {
         return this.changed;
      }
   }

   static class TLongLink {
      long value;
      TLongLinkedList.TLongLink previous;
      TLongLinkedList.TLongLink next;

      TLongLink(long var1) {
         this.value = var1;
      }

      public long getValue() {
         return this.value;
      }

      public void setValue(long var1) {
         this.value = var1;
      }

      public TLongLinkedList.TLongLink getPrevious() {
         return this.previous;
      }

      public void setPrevious(TLongLinkedList.TLongLink var1) {
         this.previous = var1;
      }

      public TLongLinkedList.TLongLink getNext() {
         return this.next;
      }

      public void setNext(TLongLinkedList.TLongLink var1) {
         this.next = var1;
      }
   }
}
