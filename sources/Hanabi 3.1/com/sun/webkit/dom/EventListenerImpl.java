package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

final class EventListenerImpl implements EventListener {
   private static final Map EL2peer = new WeakHashMap();
   private static final Map peer2EL = new HashMap();
   private final EventListener eventListener;
   private final long jsPeer;

   static long getPeer(EventListener var0) {
      if (var0 == null) {
         return 0L;
      } else {
         Long var1 = (Long)EL2peer.get(var0);
         if (var1 != null) {
            return var1;
         } else {
            EventListenerImpl var2 = new EventListenerImpl(var0, 0L);
            var1 = var2.twkCreatePeer();
            EL2peer.put(var0, var1);
            peer2EL.put(var1, new WeakReference(var0));
            return var1;
         }
      }
   }

   private native long twkCreatePeer();

   private static EventListener getELfromPeer(long var0) {
      WeakReference var2 = (WeakReference)peer2EL.get(var0);
      return var2 == null ? null : (EventListener)var2.get();
   }

   static EventListener getImpl(long var0) {
      if (var0 == 0L) {
         return null;
      } else {
         EventListener var2 = getELfromPeer(var0);
         if (var2 != null) {
            twkDisposeJSPeer(var0);
            return var2;
         } else {
            EventListenerImpl var3 = new EventListenerImpl((EventListener)null, var0);
            EL2peer.put(var3, var0);
            peer2EL.put(var0, new WeakReference(var3));
            Disposer.addRecord(var3, new SelfDisposer(var0));
            return var3;
         }
      }
   }

   public void handleEvent(Event var1) {
      if (this.jsPeer != 0L && var1 instanceof EventImpl) {
         twkDispatchEvent(this.jsPeer, ((EventImpl)var1).getPeer());
      }

   }

   private static native void twkDispatchEvent(long var0, long var2);

   private EventListenerImpl(EventListener var1, long var2) {
      this.eventListener = var1;
      this.jsPeer = var2;
   }

   private static void dispose(long var0) {
      EventListener var2 = getELfromPeer(var0);
      if (var2 != null) {
         EL2peer.remove(var2);
      }

      peer2EL.remove(var0);
   }

   private static native void twkDisposeJSPeer(long var0);

   private void fwkHandleEvent(long var1) {
      this.eventListener.handleEvent(EventImpl.getImpl(var1));
   }

   private static final class SelfDisposer implements DisposerRecord {
      private final long peer;

      private SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         EventListenerImpl.dispose(this.peer);
         EventListenerImpl.twkDisposeJSPeer(this.peer);
      }

      // $FF: synthetic method
      SelfDisposer(long var1, Object var3) {
         this(var1);
      }
   }
}
