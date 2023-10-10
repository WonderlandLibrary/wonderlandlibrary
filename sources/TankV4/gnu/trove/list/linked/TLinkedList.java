package gnu.trove.list.linked;

import gnu.trove.list.TLinkable;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TLinkedList extends AbstractSequentialList implements Externalizable {
   static final long serialVersionUID = 1L;
   protected TLinkable _head;
   protected TLinkable _tail;
   protected int _size = 0;

   public ListIterator listIterator(int var1) {
      return new TLinkedList.IteratorImpl(this, var1);
   }

   public int size() {
      return this._size;
   }

   public void add(int var1, TLinkable var2) {
      if (var1 >= 0 && var1 <= this.size()) {
         this.insert(var1, var2);
      } else {
         throw new IndexOutOfBoundsException("index:" + var1);
      }
   }

   public boolean add(TLinkable var1) {
      this.insert(this._size, var1);
      return true;
   }

   public void addFirst(TLinkable var1) {
      this.insert(0, var1);
   }

   public void addLast(TLinkable var1) {
      this.insert(this.size(), var1);
   }

   public void clear() {
      if (null != this._head) {
         for(TLinkable var1 = this._head.getNext(); var1 != null; var1 = var1.getNext()) {
            TLinkable var2 = var1.getPrevious();
            var2.setNext((TLinkable)null);
            var1.setPrevious((TLinkable)null);
         }

         this._head = this._tail = null;
      }

      this._size = 0;
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this._size];
      int var2 = 0;

      for(TLinkable var3 = this._head; var3 != null; var3 = var3.getNext()) {
         var1[var2++] = var3;
      }

      return var1;
   }

   public Object[] toUnlinkedArray() {
      Object[] var1 = new Object[this._size];
      int var2 = 0;

      for(TLinkable var3 = this._head; var3 != null; ++var2) {
         var1[var2] = var3;
         TLinkable var4 = var3;
         var3 = var3.getNext();
         var4.setNext((TLinkable)null);
         var4.setPrevious((TLinkable)null);
      }

      this._size = 0;
      this._head = this._tail = null;
      return var1;
   }

   public TLinkable[] toUnlinkedArray(TLinkable[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = (TLinkable[])((TLinkable[])Array.newInstance(var1.getClass().getComponentType(), var2));
      }

      int var3 = 0;

      for(TLinkable var4 = this._head; var4 != null; ++var3) {
         var1[var3] = var4;
         TLinkable var5 = var4;
         var4 = var4.getNext();
         var5.setNext((TLinkable)null);
         var5.setPrevious((TLinkable)null);
      }

      this._size = 0;
      this._head = this._tail = null;
      return var1;
   }

   public boolean contains(Object var1) {
      for(TLinkable var2 = this._head; var2 != null; var2 = var2.getNext()) {
         if (var1.equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public TLinkable get(int var1) {
      if (var1 >= 0 && var1 < this._size) {
         int var2;
         TLinkable var3;
         if (var1 > this._size >> 1) {
            var2 = this._size - 1;

            for(var3 = this._tail; var2 > var1; --var2) {
               var3 = var3.getPrevious();
            }

            return var3;
         } else {
            var2 = 0;

            for(var3 = this._head; var2 < var1; ++var2) {
               var3 = var3.getNext();
            }

            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this._size);
      }
   }

   public TLinkable getFirst() {
      return this._head;
   }

   public TLinkable getLast() {
      return this._tail;
   }

   public TLinkable getNext(TLinkable var1) {
      return var1.getNext();
   }

   public TLinkable getPrevious(TLinkable var1) {
      return var1.getPrevious();
   }

   public TLinkable removeFirst() {
      TLinkable var1 = this._head;
      if (var1 == null) {
         return null;
      } else {
         TLinkable var2 = var1.getNext();
         var1.setNext((TLinkable)null);
         if (null != var2) {
            var2.setPrevious((TLinkable)null);
         }

         this._head = var2;
         if (--this._size == 0) {
            this._tail = null;
         }

         return var1;
      }
   }

   public TLinkable removeLast() {
      TLinkable var1 = this._tail;
      if (var1 == null) {
         return null;
      } else {
         TLinkable var2 = var1.getPrevious();
         var1.setPrevious((TLinkable)null);
         if (null != var2) {
            var2.setNext((TLinkable)null);
         }

         this._tail = var2;
         if (--this._size == 0) {
            this._head = null;
         }

         return var1;
      }
   }

   protected void insert(int var1, TLinkable var2) {
      if (this._size == 0) {
         this._head = this._tail = var2;
      } else if (var1 == 0) {
         var2.setNext(this._head);
         this._head.setPrevious(var2);
         this._head = var2;
      } else if (var1 == this._size) {
         this._tail.setNext(var2);
         var2.setPrevious(this._tail);
         this._tail = var2;
      } else {
         TLinkable var3 = this.get(var1);
         TLinkable var4 = var3.getPrevious();
         if (var4 != null) {
            var4.setNext(var2);
         }

         var2.setPrevious(var4);
         var2.setNext(var3);
         var3.setPrevious(var2);
      }

      ++this._size;
   }

   public boolean remove(Object var1) {
      if (!(var1 instanceof TLinkable)) {
         return false;
      } else {
         TLinkable var4 = (TLinkable)var1;
         TLinkable var2 = var4.getPrevious();
         TLinkable var3 = var4.getNext();
         if (var3 == null && var2 == null) {
            if (var1 != this._head) {
               return false;
            }

            this._head = this._tail = null;
         } else if (var3 == null) {
            var4.setPrevious((TLinkable)null);
            var2.setNext((TLinkable)null);
            this._tail = var2;
         } else if (var2 == null) {
            var4.setNext((TLinkable)null);
            var3.setPrevious((TLinkable)null);
            this._head = var3;
         } else {
            var2.setNext(var3);
            var3.setPrevious(var2);
            var4.setNext((TLinkable)null);
            var4.setPrevious((TLinkable)null);
         }

         --this._size;
         return true;
      }
   }

   public void addBefore(TLinkable var1, TLinkable var2) {
      if (var1 == this._head) {
         this.addFirst(var2);
      } else if (var1 == null) {
         this.addLast(var2);
      } else {
         TLinkable var3 = var1.getPrevious();
         var2.setNext(var1);
         var3.setNext(var2);
         var2.setPrevious(var3);
         var1.setPrevious(var2);
         ++this._size;
      }

   }

   public void addAfter(TLinkable var1, TLinkable var2) {
      if (var1 == this._tail) {
         this.addLast(var2);
      } else if (var1 == null) {
         this.addFirst(var2);
      } else {
         TLinkable var3 = var1.getNext();
         var2.setPrevious(var1);
         var2.setNext(var3);
         var1.setNext(var2);
         var3.setPrevious(var2);
         ++this._size;
      }

   }

   public boolean forEachValue(TObjectProcedure var1) {
      for(TLinkable var2 = this._head; var2 != null; var2 = var2.getNext()) {
         boolean var3 = var1.execute(var2);
         if (!var3) {
            return false;
         }
      }

      return true;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeInt(this._size);
      var1.writeObject(this._head);
      var1.writeObject(this._tail);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._size = var1.readInt();
      this._head = (TLinkable)var1.readObject();
      this._tail = (TLinkable)var1.readObject();
   }

   public void add(int var1, Object var2) {
      this.add(var1, (TLinkable)var2);
   }

   public Object get(int var1) {
      return this.get(var1);
   }

   public boolean add(Object var1) {
      return this.add((TLinkable)var1);
   }

   protected final class IteratorImpl implements ListIterator {
      private int _nextIndex;
      private TLinkable _next;
      private TLinkable _lastReturned;
      final TLinkedList this$0;

      IteratorImpl(TLinkedList var1, int var2) {
         this.this$0 = var1;
         this._nextIndex = 0;
         if (var2 >= 0 && var2 <= var1._size) {
            this._nextIndex = var2;
            if (var2 == 0) {
               this._next = var1._head;
            } else if (var2 == var1._size) {
               this._next = null;
            } else {
               int var3;
               if (var2 < var1._size >> 1) {
                  var3 = 0;

                  for(this._next = var1._head; var3 < var2; ++var3) {
                     this._next = this._next.getNext();
                  }
               } else {
                  var3 = var1._size - 1;

                  for(this._next = var1._tail; var3 > var2; --var3) {
                     this._next = this._next.getPrevious();
                  }
               }
            }

         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final void add(TLinkable var1) {
         this._lastReturned = null;
         ++this._nextIndex;
         if (this.this$0._size == 0) {
            this.this$0.add(var1);
         } else {
            this.this$0.addBefore(this._next, var1);
         }

      }

      public final boolean hasNext() {
         return this._nextIndex != this.this$0._size;
      }

      public final boolean hasPrevious() {
         return this._nextIndex != 0;
      }

      public final TLinkable next() {
         if (this._nextIndex == this.this$0._size) {
            throw new NoSuchElementException();
         } else {
            this._lastReturned = this._next;
            this._next = this._next.getNext();
            ++this._nextIndex;
            return this._lastReturned;
         }
      }

      public final int nextIndex() {
         return this._nextIndex;
      }

      public final TLinkable previous() {
         if (this._nextIndex == 0) {
            throw new NoSuchElementException();
         } else {
            if (this._nextIndex == this.this$0._size) {
               this._lastReturned = this._next = this.this$0._tail;
            } else {
               this._lastReturned = this._next = this._next.getPrevious();
            }

            --this._nextIndex;
            return this._lastReturned;
         }
      }

      public final int previousIndex() {
         return this._nextIndex - 1;
      }

      public final void remove() {
         if (this._lastReturned == null) {
            throw new IllegalStateException("must invoke next or previous before invoking remove");
         } else {
            if (this._lastReturned != this._next) {
               --this._nextIndex;
            }

            this._next = this._lastReturned.getNext();
            this.this$0.remove(this._lastReturned);
            this._lastReturned = null;
         }
      }

      public final void set(TLinkable var1) {
         if (this._lastReturned == null) {
            throw new IllegalStateException();
         } else {
            this.swap(this._lastReturned, var1);
            this._lastReturned = var1;
         }
      }

      private void swap(TLinkable var1, TLinkable var2) {
         TLinkable var3 = var1.getPrevious();
         TLinkable var4 = var1.getNext();
         TLinkable var5 = var2.getPrevious();
         TLinkable var6 = var2.getNext();
         if (var4 == var2) {
            if (var3 != null) {
               var3.setNext(var2);
            }

            var2.setPrevious(var3);
            var2.setNext(var1);
            var1.setPrevious(var2);
            var1.setNext(var6);
            if (var6 != null) {
               var6.setPrevious(var1);
            }
         } else if (var6 == var1) {
            if (var5 != null) {
               var5.setNext(var2);
            }

            var2.setPrevious(var1);
            var2.setNext(var4);
            var1.setPrevious(var5);
            var1.setNext(var2);
            if (var4 != null) {
               var4.setPrevious(var2);
            }
         } else {
            var1.setNext(var6);
            var1.setPrevious(var5);
            if (var5 != null) {
               var5.setNext(var1);
            }

            if (var6 != null) {
               var6.setPrevious(var1);
            }

            var2.setNext(var4);
            var2.setPrevious(var3);
            if (var3 != null) {
               var3.setNext(var2);
            }

            if (var4 != null) {
               var4.setPrevious(var2);
            }
         }

         if (this.this$0._head == var1) {
            this.this$0._head = var2;
         } else if (this.this$0._head == var2) {
            this.this$0._head = var1;
         }

         if (this.this$0._tail == var1) {
            this.this$0._tail = var2;
         } else if (this.this$0._tail == var2) {
            this.this$0._tail = var1;
         }

         if (this._lastReturned == var1) {
            this._lastReturned = var2;
         } else if (this._lastReturned == var2) {
            this._lastReturned = var1;
         }

         if (this._next == var1) {
            this._next = var2;
         } else if (this._next == var2) {
            this._next = var1;
         }

      }

      public void add(Object var1) {
         this.add((TLinkable)var1);
      }

      public void set(Object var1) {
         this.set((TLinkable)var1);
      }

      public Object previous() {
         return this.previous();
      }

      public Object next() {
         return this.next();
      }
   }
}
