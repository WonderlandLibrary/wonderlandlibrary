package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.Invoker;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicInteger;
import netscape.javascript.JSException;

class JSObject extends netscape.javascript.JSObject {
   private static final String UNDEFINED = new String("undefined");
   static final int JS_CONTEXT_OBJECT = 0;
   static final int JS_DOM_NODE_OBJECT = 1;
   static final int JS_DOM_WINDOW_OBJECT = 2;
   private final long peer;
   private final int peer_type;
   private static AtomicInteger peerCount = new AtomicInteger();

   JSObject(long var1, int var3) {
      this.peer = var1;
      this.peer_type = var3;
      if (var3 == 0) {
         Disposer.addRecord(this, new SelfDisposer(var1, var3));
         peerCount.incrementAndGet();
      }

   }

   long getPeer() {
      return this.peer;
   }

   static int test_getPeerCount() {
      return peerCount.get();
   }

   private static native void unprotectImpl(long var0, int var2);

   public Object eval(String var1) throws JSException {
      Invoker.getInvoker().checkEventThread();
      return evalImpl(this.peer, this.peer_type, var1);
   }

   private static native Object evalImpl(long var0, int var2, String var3);

   public Object getMember(String var1) {
      Invoker.getInvoker().checkEventThread();
      return getMemberImpl(this.peer, this.peer_type, var1);
   }

   private static native Object getMemberImpl(long var0, int var2, String var3);

   public void setMember(String var1, Object var2) throws JSException {
      Invoker.getInvoker().checkEventThread();
      setMemberImpl(this.peer, this.peer_type, var1, var2, AccessController.getContext());
   }

   private static native void setMemberImpl(long var0, int var2, String var3, Object var4, AccessControlContext var5);

   public void removeMember(String var1) throws JSException {
      Invoker.getInvoker().checkEventThread();
      removeMemberImpl(this.peer, this.peer_type, var1);
   }

   private static native void removeMemberImpl(long var0, int var2, String var3);

   public Object getSlot(int var1) throws JSException {
      Invoker.getInvoker().checkEventThread();
      return getSlotImpl(this.peer, this.peer_type, var1);
   }

   private static native Object getSlotImpl(long var0, int var2, int var3);

   public void setSlot(int var1, Object var2) throws JSException {
      Invoker.getInvoker().checkEventThread();
      setSlotImpl(this.peer, this.peer_type, var1, var2, AccessController.getContext());
   }

   private static native void setSlotImpl(long var0, int var2, int var3, Object var4, AccessControlContext var5);

   public Object call(String var1, Object... var2) throws JSException {
      Invoker.getInvoker().checkEventThread();
      return callImpl(this.peer, this.peer_type, var1, var2, AccessController.getContext());
   }

   private static native Object callImpl(long var0, int var2, String var3, Object[] var4, AccessControlContext var5);

   public String toString() {
      Invoker.getInvoker().checkEventThread();
      return toStringImpl(this.peer, this.peer_type);
   }

   private static native String toStringImpl(long var0, int var2);

   public boolean equals(Object var1) {
      return var1 == this || var1 != null && var1.getClass() == JSObject.class && this.peer == ((JSObject)var1).peer;
   }

   public int hashCode() {
      return (int)(this.peer ^ this.peer >> 17);
   }

   private static JSException fwkMakeException(Object var0) {
      if (var0 == null) {
         Object var10000 = null;
      } else {
         var0.toString();
      }

      JSException var2 = new JSException(var0 == null ? null : var0.toString());
      if (var0 instanceof Throwable) {
         var2.initCause((Throwable)var0);
      }

      return var2;
   }

   private static final class SelfDisposer implements DisposerRecord {
      long peer;
      final int peer_type;

      private SelfDisposer(long var1, int var3) {
         this.peer = var1;
         this.peer_type = var3;
      }

      public void dispose() {
         if (this.peer != 0L) {
            JSObject.unprotectImpl(this.peer, this.peer_type);
            this.peer = 0L;
            JSObject.peerCount.decrementAndGet();
         }

      }

      // $FF: synthetic method
      SelfDisposer(long var1, int var3, Object var4) {
         this(var1, var3);
      }
   }
}
