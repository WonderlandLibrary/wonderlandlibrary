package io.netty.util.internal;

public abstract class OneTimeTask extends MpscLinkedQueueNode implements Runnable {
   public Runnable value() {
      return this;
   }

   public Object value() {
      return this.value();
   }
}
