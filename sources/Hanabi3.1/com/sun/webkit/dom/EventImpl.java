package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class EventImpl implements Event {
   private final long peer;
   private static final int TYPE_WheelEvent = 1;
   private static final int TYPE_MouseEvent = 2;
   private static final int TYPE_KeyboardEvent = 3;
   private static final int TYPE_UIEvent = 4;
   private static final int TYPE_MutationEvent = 5;
   public static final int NONE = 0;
   public static final int CAPTURING_PHASE = 1;
   public static final int AT_TARGET = 2;
   public static final int BUBBLING_PHASE = 3;
   public static final int MOUSEDOWN = 1;
   public static final int MOUSEUP = 2;
   public static final int MOUSEOVER = 4;
   public static final int MOUSEOUT = 8;
   public static final int MOUSEMOVE = 16;
   public static final int MOUSEDRAG = 32;
   public static final int CLICK = 64;
   public static final int DBLCLICK = 128;
   public static final int KEYDOWN = 256;
   public static final int KEYUP = 512;
   public static final int KEYPRESS = 1024;
   public static final int DRAGDROP = 2048;
   public static final int FOCUS = 4096;
   public static final int BLUR = 8192;
   public static final int SELECT = 16384;
   public static final int CHANGE = 32768;

   EventImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static Event create(long var0) {
      // $FF: Couldn't be decompiled
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof EventImpl && this.peer == ((EventImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(Event var0) {
      return var0 == null ? 0L : ((EventImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   private static native int getCPPTypeImpl(long var0);

   static Event getImpl(long var0) {
      return create(var0);
   }

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public EventTarget getTarget() {
      return (EventTarget)NodeImpl.getImpl(getTargetImpl(this.getPeer()));
   }

   static native long getTargetImpl(long var0);

   public EventTarget getCurrentTarget() {
      return (EventTarget)NodeImpl.getImpl(getCurrentTargetImpl(this.getPeer()));
   }

   static native long getCurrentTargetImpl(long var0);

   public short getEventPhase() {
      return getEventPhaseImpl(this.getPeer());
   }

   static native short getEventPhaseImpl(long var0);

   public boolean getBubbles() {
      return getBubblesImpl(this.getPeer());
   }

   static native boolean getBubblesImpl(long var0);

   public boolean getCancelable() {
      return getCancelableImpl(this.getPeer());
   }

   static native boolean getCancelableImpl(long var0);

   public long getTimeStamp() {
      return getTimeStampImpl(this.getPeer());
   }

   static native long getTimeStampImpl(long var0);

   public boolean getDefaultPrevented() {
      return getDefaultPreventedImpl(this.getPeer());
   }

   static native boolean getDefaultPreventedImpl(long var0);

   public boolean getIsTrusted() {
      return getIsTrustedImpl(this.getPeer());
   }

   static native boolean getIsTrustedImpl(long var0);

   public EventTarget getSrcElement() {
      return (EventTarget)NodeImpl.getImpl(getSrcElementImpl(this.getPeer()));
   }

   static native long getSrcElementImpl(long var0);

   public boolean getReturnValue() {
      return getReturnValueImpl(this.getPeer());
   }

   static native boolean getReturnValueImpl(long var0);

   public void setReturnValue(boolean var1) {
      setReturnValueImpl(this.getPeer(), var1);
   }

   static native void setReturnValueImpl(long var0, boolean var2);

   public boolean getCancelBubble() {
      return getCancelBubbleImpl(this.getPeer());
   }

   static native boolean getCancelBubbleImpl(long var0);

   public void setCancelBubble(boolean var1) {
      setCancelBubbleImpl(this.getPeer(), var1);
   }

   static native void setCancelBubbleImpl(long var0, boolean var2);

   public void stopPropagation() {
      stopPropagationImpl(this.getPeer());
   }

   static native void stopPropagationImpl(long var0);

   public void preventDefault() {
      preventDefaultImpl(this.getPeer());
   }

   static native void preventDefaultImpl(long var0);

   public void initEvent(String var1, boolean var2, boolean var3) {
      initEventImpl(this.getPeer(), var1, var2, var3);
   }

   static native void initEventImpl(long var0, String var2, boolean var3, boolean var4);

   public void stopImmediatePropagation() {
      stopImmediatePropagationImpl(this.getPeer());
   }

   static native void stopImmediatePropagationImpl(long var0);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         EventImpl.dispose(this.peer);
      }
   }
}
