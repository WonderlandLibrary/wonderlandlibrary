package com.sun.webkit.network;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

final class ByteBufferPool {
   private final Queue byteBuffers = new ConcurrentLinkedQueue();
   private final int bufferSize;

   private ByteBufferPool(int var1) {
      this.bufferSize = var1;
   }

   static ByteBufferPool newInstance(int var0) {
      return new ByteBufferPool(var0);
   }

   ByteBufferAllocator newAllocator(int var1) {
      return new ByteBufferAllocatorImpl(var1);
   }

   private final class ByteBufferAllocatorImpl implements ByteBufferAllocator {
      private final Semaphore semaphore;

      private ByteBufferAllocatorImpl(int var2) {
         this.semaphore = new Semaphore(var2);
      }

      public ByteBuffer allocate() throws InterruptedException {
         this.semaphore.acquire();
         ByteBuffer var1 = (ByteBuffer)ByteBufferPool.this.byteBuffers.poll();
         if (var1 == null) {
            var1 = ByteBuffer.allocateDirect(ByteBufferPool.this.bufferSize);
         }

         return var1;
      }

      public void release(ByteBuffer var1) {
         var1.clear();
         ByteBufferPool.this.byteBuffers.add(var1);
         this.semaphore.release();
      }

      // $FF: synthetic method
      ByteBufferAllocatorImpl(int var2, Object var3) {
         this(var2);
      }
   }
}
