package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class AbstractIterator extends UnmodifiableIterator {
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

   public final Object peek() {
      // $FF: Couldn't be decompiled
   }

   private static enum State {
      READY,
      NOT_READY,
      DONE,
      FAILED;

      private static final AbstractIterator.State[] $VALUES = new AbstractIterator.State[]{READY, NOT_READY, DONE, FAILED};
   }
}
