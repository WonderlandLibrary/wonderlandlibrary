package com.google.common.collect;

import java.util.Collection;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

public abstract class ForwardingBlockingDeque extends ForwardingDeque implements BlockingDeque {
   protected ForwardingBlockingDeque() {
   }

   protected abstract BlockingDeque delegate();

   public int remainingCapacity() {
      return this.delegate().remainingCapacity();
   }

   public void putFirst(Object var1) throws InterruptedException {
      this.delegate().putFirst(var1);
   }

   public void putLast(Object var1) throws InterruptedException {
      this.delegate().putLast(var1);
   }

   public boolean offerFirst(Object var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.delegate().offerFirst(var1, var2, var4);
   }

   public boolean offerLast(Object var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.delegate().offerLast(var1, var2, var4);
   }

   public Object takeFirst() throws InterruptedException {
      return this.delegate().takeFirst();
   }

   public Object takeLast() throws InterruptedException {
      return this.delegate().takeLast();
   }

   public Object pollFirst(long var1, TimeUnit var3) throws InterruptedException {
      return this.delegate().pollFirst(var1, var3);
   }

   public Object pollLast(long var1, TimeUnit var3) throws InterruptedException {
      return this.delegate().pollLast(var1, var3);
   }

   public void put(Object var1) throws InterruptedException {
      this.delegate().put(var1);
   }

   public boolean offer(Object var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.delegate().offer(var1, var2, var4);
   }

   public Object take() throws InterruptedException {
      return this.delegate().take();
   }

   public Object poll(long var1, TimeUnit var3) throws InterruptedException {
      return this.delegate().poll(var1, var3);
   }

   public int drainTo(Collection var1) {
      return this.delegate().drainTo(var1);
   }

   public int drainTo(Collection var1, int var2) {
      return this.delegate().drainTo(var1, var2);
   }

   protected Deque delegate() {
      return this.delegate();
   }

   protected Queue delegate() {
      return this.delegate();
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
