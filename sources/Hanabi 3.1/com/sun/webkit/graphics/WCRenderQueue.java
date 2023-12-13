package com.sun.webkit.graphics;

import com.sun.webkit.Invoker;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WCRenderQueue extends Ref {
   private static final AtomicInteger idCountObj = new AtomicInteger(0);
   private static final Logger log = Logger.getLogger(WCRenderQueue.class.getName());
   public static final int MAX_QUEUE_SIZE = 524288;
   private final LinkedList buffers = new LinkedList();
   private BufferData currentBuffer = new BufferData();
   private final WCRectangle clip;
   private int size = 0;
   private final boolean opaque;
   protected final WCGraphicsContext gc;

   protected WCRenderQueue(WCGraphicsContext var1) {
      this.clip = null;
      this.opaque = false;
      this.gc = var1;
   }

   protected WCRenderQueue(WCRectangle var1, boolean var2) {
      this.clip = var1;
      this.opaque = var2;
      this.gc = null;
   }

   public synchronized int getSize() {
      return this.size;
   }

   public synchronized void addBuffer(ByteBuffer var1) {
      if (log.isLoggable(Level.FINE) && this.buffers.isEmpty()) {
         log.log(Level.FINE, "'{'WCRenderQueue{0}[{1}]", new Object[]{this.hashCode(), idCountObj.incrementAndGet()});
      }

      this.currentBuffer.setBuffer(var1);
      this.buffers.addLast(this.currentBuffer);
      this.currentBuffer = new BufferData();
      this.size += var1.capacity();
      if (this.size > 524288 && this.gc != null) {
         this.flush();
      }

   }

   public synchronized boolean isEmpty() {
      return this.buffers.isEmpty();
   }

   public synchronized void decode(WCGraphicsContext var1) {
      Iterator var2 = this.buffers.iterator();

      while(var2.hasNext()) {
         BufferData var3 = (BufferData)var2.next();

         try {
            GraphicsDecoder.decode(WCGraphicsManager.getGraphicsManager(), var1, var3);
         } catch (RuntimeException var5) {
            var5.printStackTrace(System.err);
         }
      }

      this.dispose();
   }

   public synchronized void decode() {
      assert this.gc != null;

      this.decode(this.gc);
      this.gc.flush();
   }

   public synchronized void decode(int var1) {
      assert this.gc != null;

      this.gc.setFontSmoothingType(var1);
      this.decode();
   }

   protected abstract void flush();

   private void fwkFlush() {
      this.flush();
   }

   private void fwkAddBuffer(ByteBuffer var1) {
      this.addBuffer(var1);
   }

   public WCRectangle getClip() {
      return this.clip;
   }

   public synchronized void dispose() {
      int var1 = this.buffers.size();
      if (var1 > 0) {
         int var2 = 0;
         Object[] var3 = new Object[var1];

         BufferData var5;
         for(Iterator var4 = this.buffers.iterator(); var4.hasNext(); var3[var2++] = var5.getBuffer()) {
            var5 = (BufferData)var4.next();
         }

         this.buffers.clear();
         Invoker.getInvoker().invokeOnEventThread(() -> {
            this.twkRelease(var3);
         });
         this.size = 0;
         if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "'}'WCRenderQueue{0}[{1}]", new Object[]{this.hashCode(), idCountObj.decrementAndGet()});
         }
      }

   }

   protected abstract void disposeGraphics();

   private void fwkDisposeGraphics() {
      this.disposeGraphics();
   }

   private native void twkRelease(Object[] var1);

   private int refString(String var1) {
      return this.currentBuffer.addString(var1);
   }

   private int refIntArr(int[] var1) {
      return this.currentBuffer.addIntArray(var1);
   }

   private int refFloatArr(float[] var1) {
      return this.currentBuffer.addFloatArray(var1);
   }

   public boolean isOpaque() {
      return this.opaque;
   }

   public synchronized String toString() {
      return "WCRenderQueue{clip=" + this.clip + ", size=" + this.size + ", opaque=" + this.opaque + "}";
   }
}
