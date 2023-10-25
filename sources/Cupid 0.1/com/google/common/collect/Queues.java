package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public final class Queues {
  public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
    return new ArrayBlockingQueue<E>(capacity);
  }
  
  public static <E> ArrayDeque<E> newArrayDeque() {
    return new ArrayDeque<E>();
  }
  
  public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new ArrayDeque<E>(Collections2.cast(elements)); 
    ArrayDeque<E> deque = new ArrayDeque<E>();
    Iterables.addAll(deque, elements);
    return deque;
  }
  
  public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
    return new ConcurrentLinkedQueue<E>();
  }
  
  public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new ConcurrentLinkedQueue<E>(Collections2.cast(elements)); 
    ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
    Iterables.addAll(queue, elements);
    return queue;
  }
  
  public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
    return new LinkedBlockingDeque<E>();
  }
  
  public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
    return new LinkedBlockingDeque<E>(capacity);
  }
  
  public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new LinkedBlockingDeque<E>(Collections2.cast(elements)); 
    LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<E>();
    Iterables.addAll(deque, elements);
    return deque;
  }
  
  public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
    return new LinkedBlockingQueue<E>();
  }
  
  public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
    return new LinkedBlockingQueue<E>(capacity);
  }
  
  public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new LinkedBlockingQueue<E>(Collections2.cast(elements)); 
    LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();
    Iterables.addAll(queue, elements);
    return queue;
  }
  
  public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
    return new PriorityBlockingQueue<E>();
  }
  
  public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new PriorityBlockingQueue<E>(Collections2.cast(elements)); 
    PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<E>();
    Iterables.addAll(queue, elements);
    return queue;
  }
  
  public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
    return new PriorityQueue<E>();
  }
  
  public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
    if (elements instanceof Collection)
      return new PriorityQueue<E>(Collections2.cast(elements)); 
    PriorityQueue<E> queue = new PriorityQueue<E>();
    Iterables.addAll(queue, elements);
    return queue;
  }
  
  public static <E> SynchronousQueue<E> newSynchronousQueue() {
    return new SynchronousQueue<E>();
  }
  
  @Beta
  public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
    Preconditions.checkNotNull(buffer);
    long deadline = System.nanoTime() + unit.toNanos(timeout);
    int added = 0;
    while (added < numElements) {
      added += q.drainTo(buffer, numElements - added);
      if (added < numElements) {
        E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
        if (e == null)
          break; 
        buffer.add(e);
        added++;
      } 
    } 
    return added;
  }
  
  @Beta
  public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
    Preconditions.checkNotNull(buffer);
    long deadline = System.nanoTime() + unit.toNanos(timeout);
    int added = 0;
    boolean interrupted = false;
    try {
      while (added < numElements) {
        added += q.drainTo(buffer, numElements - added);
        if (added < numElements) {
          E e;
          while (true) {
            try {
              e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
              break;
            } catch (InterruptedException ex) {
              interrupted = true;
            } 
          } 
          if (e == null)
            break; 
          buffer.add(e);
          added++;
        } 
      } 
    } finally {
      if (interrupted)
        Thread.currentThread().interrupt(); 
    } 
    return added;
  }
  
  @Beta
  public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
    return Synchronized.queue(queue, null);
  }
  
  @Beta
  public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
    return Synchronized.deque(deque, null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Queues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */