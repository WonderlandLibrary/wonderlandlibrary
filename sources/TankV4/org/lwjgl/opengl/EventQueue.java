package org.lwjgl.opengl;

import java.nio.ByteBuffer;

class EventQueue {
   private static final int QUEUE_SIZE = 200;
   private final int event_size;
   private final ByteBuffer queue;

   protected EventQueue(int var1) {
      this.event_size = var1;
      this.queue = ByteBuffer.allocate(200 * var1);
   }

   protected synchronized void clearEvents() {
      this.queue.clear();
   }

   public synchronized void copyEvents(ByteBuffer var1) {
      this.queue.flip();
      int var2 = this.queue.limit();
      if (var1.remaining() < this.queue.remaining()) {
         this.queue.limit(var1.remaining() + this.queue.position());
      }

      var1.put(this.queue);
      this.queue.limit(var2);
      this.queue.compact();
   }

   public synchronized boolean putEvent(ByteBuffer var1) {
      if (var1.remaining() != this.event_size) {
         throw new IllegalArgumentException("Internal error: event size " + this.event_size + " does not equal the given event size " + var1.remaining());
      } else if (this.queue.remaining() >= var1.remaining()) {
         this.queue.put(var1);
         return true;
      } else {
         return false;
      }
   }
}
