package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
abstract class AbstractIterator implements Iterator {
   private AbstractIterator.State state;
   private Object next;

   protected AbstractIterator() {
      this.state = AbstractIterator.State.NOT_READY;
   }

   protected abstract Object computeNext();

   protected final Object endOfData() {
      this.state = AbstractIterator.State.DONE;
      return null;
   }

   private boolean tryToComputeNext() {
      this.state = AbstractIterator.State.FAILED;
      this.next = this.computeNext();
      if (this.state != AbstractIterator.State.DONE) {
         this.state = AbstractIterator.State.READY;
         return true;
      } else {
         return false;
      }
   }

   public final Object next() {
      // $FF: Couldn't be decompiled
   }

   public final void remove() {
      throw new UnsupportedOperationException();
   }

   private static enum State {
      READY,
      NOT_READY,
      DONE,
      FAILED;

      private static final AbstractIterator.State[] $VALUES = new AbstractIterator.State[]{READY, NOT_READY, DONE, FAILED};
   }
}
