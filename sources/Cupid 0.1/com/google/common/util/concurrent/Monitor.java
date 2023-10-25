package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
  private final boolean fair;
  
  private final ReentrantLock lock;
  
  @Beta
  public static abstract class Guard {
    final Monitor monitor;
    
    final Condition condition;
    
    @GuardedBy("monitor.lock")
    int waiterCount = 0;
    
    @GuardedBy("monitor.lock")
    Guard next;
    
    protected Guard(Monitor monitor) {
      this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
      this.condition = monitor.lock.newCondition();
    }
    
    public abstract boolean isSatisfied();
  }
  
  @GuardedBy("lock")
  private Guard activeGuards = null;
  
  public Monitor() {
    this(false);
  }
  
  public Monitor(boolean fair) {
    this.fair = fair;
    this.lock = new ReentrantLock(fair);
  }
  
  public void enter() {
    this.lock.lock();
  }
  
  public void enterInterruptibly() throws InterruptedException {
    this.lock.lockInterruptibly();
  }
  
  public boolean enter(long time, TimeUnit unit) {
    long timeoutNanos = unit.toNanos(time);
    ReentrantLock lock = this.lock;
    if (!this.fair && lock.tryLock())
      return true; 
    long deadline = System.nanoTime() + timeoutNanos;
    boolean interrupted = Thread.interrupted();
    while (true) {
      try {
        return lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
      } catch (InterruptedException interrupt) {
        interrupted = true;
      } finally {
        if (interrupted)
          Thread.currentThread().interrupt(); 
      } 
    } 
  }
  
  public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
    return this.lock.tryLock(time, unit);
  }
  
  public boolean tryEnter() {
    return this.lock.tryLock();
  }
  
  public void enterWhen(Guard guard) throws InterruptedException {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
    lock.lockInterruptibly();
    boolean satisfied = false;
    try {
      if (!guard.isSatisfied())
        await(guard, signalBeforeWaiting); 
      satisfied = true;
    } finally {
      if (!satisfied)
        leave(); 
    } 
  }
  
  public void enterWhenUninterruptibly(Guard guard) {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
    lock.lock();
    boolean satisfied = false;
    try {
      if (!guard.isSatisfied())
        awaitUninterruptibly(guard, signalBeforeWaiting); 
      satisfied = true;
    } finally {
      if (!satisfied)
        leave(); 
    } 
  }
  
  public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
    long timeoutNanos = unit.toNanos(time);
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    boolean reentrant = lock.isHeldByCurrentThread();
    if (this.fair || !lock.tryLock()) {
      long deadline = System.nanoTime() + timeoutNanos;
      if (!lock.tryLock(time, unit))
        return false; 
      timeoutNanos = deadline - System.nanoTime();
    } 
    boolean satisfied = false;
    boolean threw = true;
    try {
      satisfied = (guard.isSatisfied() || awaitNanos(guard, timeoutNanos, reentrant));
      threw = false;
      return satisfied;
    } finally {
      if (!satisfied)
        try {
          if (threw && !reentrant)
            signalNextWaiter(); 
        } finally {
          lock.unlock();
        }  
    } 
  }
  
  public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
    long timeoutNanos = unit.toNanos(time);
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    long deadline = System.nanoTime() + timeoutNanos;
    boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
    boolean interrupted = Thread.interrupted();
  }
  
  public boolean enterIf(Guard guard) {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    lock.lock();
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied)
        lock.unlock(); 
    } 
  }
  
  public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied)
        lock.unlock(); 
    } 
  }
  
  public boolean enterIf(Guard guard, long time, TimeUnit unit) {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    if (!enter(time, unit))
      return false; 
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied)
        this.lock.unlock(); 
    } 
  }
  
  public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    if (!lock.tryLock(time, unit))
      return false; 
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied)
        lock.unlock(); 
    } 
  }
  
  public boolean tryEnterIf(Guard guard) {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    ReentrantLock lock = this.lock;
    if (!lock.tryLock())
      return false; 
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied)
        lock.unlock(); 
    } 
  }
  
  public void waitFor(Guard guard) throws InterruptedException {
    if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0)
      throw new IllegalMonitorStateException(); 
    if (!guard.isSatisfied())
      await(guard, true); 
  }
  
  public void waitForUninterruptibly(Guard guard) {
    if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0)
      throw new IllegalMonitorStateException(); 
    if (!guard.isSatisfied())
      awaitUninterruptibly(guard, true); 
  }
  
  public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
    long timeoutNanos = unit.toNanos(time);
    if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0)
      throw new IllegalMonitorStateException(); 
    return (guard.isSatisfied() || awaitNanos(guard, timeoutNanos, true));
  }
  
  public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
    long timeoutNanos = unit.toNanos(time);
    if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0)
      throw new IllegalMonitorStateException(); 
    if (guard.isSatisfied())
      return true; 
    boolean signalBeforeWaiting = true;
    long deadline = System.nanoTime() + timeoutNanos;
    boolean interrupted = Thread.interrupted();
    while (true) {
      try {
        return awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
      } catch (InterruptedException interrupt) {
        interrupted = true;
        if (guard.isSatisfied())
          return true; 
        signalBeforeWaiting = false;
      } finally {
        if (interrupted)
          Thread.currentThread().interrupt(); 
      } 
    } 
  }
  
  public void leave() {
    ReentrantLock lock = this.lock;
    try {
      if (lock.getHoldCount() == 1)
        signalNextWaiter(); 
    } finally {
      lock.unlock();
    } 
  }
  
  public boolean isFair() {
    return this.fair;
  }
  
  public boolean isOccupied() {
    return this.lock.isLocked();
  }
  
  public boolean isOccupiedByCurrentThread() {
    return this.lock.isHeldByCurrentThread();
  }
  
  public int getOccupiedDepth() {
    return this.lock.getHoldCount();
  }
  
  public int getQueueLength() {
    return this.lock.getQueueLength();
  }
  
  public boolean hasQueuedThreads() {
    return this.lock.hasQueuedThreads();
  }
  
  public boolean hasQueuedThread(Thread thread) {
    return this.lock.hasQueuedThread(thread);
  }
  
  public boolean hasWaiters(Guard guard) {
    return (getWaitQueueLength(guard) > 0);
  }
  
  public int getWaitQueueLength(Guard guard) {
    if (guard.monitor != this)
      throw new IllegalMonitorStateException(); 
    this.lock.lock();
    try {
      return guard.waiterCount;
    } finally {
      this.lock.unlock();
    } 
  }
  
  @GuardedBy("lock")
  private void signalNextWaiter() {
    for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
      if (isSatisfied(guard)) {
        guard.condition.signal();
        break;
      } 
    } 
  }
  
  @GuardedBy("lock")
  private boolean isSatisfied(Guard guard) {
    try {
      return guard.isSatisfied();
    } catch (Throwable throwable) {
      signalAllWaiters();
      throw Throwables.propagate(throwable);
    } 
  }
  
  @GuardedBy("lock")
  private void signalAllWaiters() {
    for (Guard guard = this.activeGuards; guard != null; guard = guard.next)
      guard.condition.signalAll(); 
  }
  
  @GuardedBy("lock")
  private void beginWaitingFor(Guard guard) {
    int waiters = guard.waiterCount++;
    if (waiters == 0) {
      guard.next = this.activeGuards;
      this.activeGuards = guard;
    } 
  }
  
  @GuardedBy("lock")
  private void endWaitingFor(Guard guard) {
    int waiters = --guard.waiterCount;
    if (waiters == 0)
      for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
        if (p == guard) {
          if (pred == null) {
            this.activeGuards = p.next;
          } else {
            pred.next = p.next;
          } 
          p.next = null;
          break;
        } 
      }  
  }
  
  @GuardedBy("lock")
  private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
    if (signalBeforeWaiting)
      signalNextWaiter(); 
    beginWaitingFor(guard);
    try {
      do {
        guard.condition.await();
      } while (!guard.isSatisfied());
    } finally {
      endWaitingFor(guard);
    } 
  }
  
  @GuardedBy("lock")
  private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
    if (signalBeforeWaiting)
      signalNextWaiter(); 
    beginWaitingFor(guard);
    try {
      do {
        guard.condition.awaitUninterruptibly();
      } while (!guard.isSatisfied());
    } finally {
      endWaitingFor(guard);
    } 
  }
  
  @GuardedBy("lock")
  private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
    if (signalBeforeWaiting)
      signalNextWaiter(); 
    beginWaitingFor(guard);
    try {
      while (true) {
        if (nanos < 0L)
          return false; 
        nanos = guard.condition.awaitNanos(nanos);
        if (guard.isSatisfied())
          return true; 
      } 
    } finally {
      endWaitingFor(guard);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\Monitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */