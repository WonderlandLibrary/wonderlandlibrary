package org.lwjgl.opengl;

import java.nio.ByteBuffer;

class EventQueue {
  private static final int QUEUE_SIZE = 200;
  
  private final int event_size;
  
  private final ByteBuffer queue;
  
  protected EventQueue(int event_size) {
    this.event_size = event_size;
    this.queue = ByteBuffer.allocate(200 * event_size);
  }
  
  protected synchronized void clearEvents() {
    this.queue.clear();
  }
  
  public synchronized void copyEvents(ByteBuffer dest) {
    this.queue.flip();
    int old_limit = this.queue.limit();
    if (dest.remaining() < this.queue.remaining())
      this.queue.limit(dest.remaining() + this.queue.position()); 
    dest.put(this.queue);
    this.queue.limit(old_limit);
    this.queue.compact();
  }
  
  public synchronized boolean putEvent(ByteBuffer event) {
    if (event.remaining() != this.event_size)
      throw new IllegalArgumentException("Internal error: event size " + this.event_size + " does not equal the given event size " + event.remaining()); 
    if (this.queue.remaining() >= event.remaining()) {
      this.queue.put(event);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EventQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */