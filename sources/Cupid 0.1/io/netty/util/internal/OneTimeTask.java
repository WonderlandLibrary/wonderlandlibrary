package io.netty.util.internal;

public abstract class OneTimeTask extends MpscLinkedQueueNode<Runnable> implements Runnable {
  public Runnable value() {
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\OneTimeTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */