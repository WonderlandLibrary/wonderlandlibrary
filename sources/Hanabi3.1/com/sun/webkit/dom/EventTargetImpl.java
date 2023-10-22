package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventTargetImpl implements EventTarget {
   private final long peer;

   EventTargetImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static EventTarget create(long var0) {
      return var0 == 0L ? null : new EventTargetImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof EventTargetImpl && this.peer == ((EventTargetImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(EventTarget var0) {
      return var0 == null ? 0L : ((EventTargetImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static EventTarget getImpl(long var0) {
      return create(var0);
   }

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      addEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      removeEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public boolean dispatchEvent(Event var1) throws DOMException {
      return dispatchEventImpl(this.getPeer(), EventImpl.getPeer(var1));
   }

   static native boolean dispatchEventImpl(long var0, long var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         EventTargetImpl.dispose(this.peer);
      }
   }
}
