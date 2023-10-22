package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

public class MediaListImpl implements MediaList {
   private final long peer;

   MediaListImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static MediaList create(long var0) {
      return var0 == 0L ? null : new MediaListImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof MediaListImpl && this.peer == ((MediaListImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(MediaList var0) {
      return var0 == null ? 0L : ((MediaListImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static MediaList getImpl(long var0) {
      return create(var0);
   }

   public String getMediaText() {
      return getMediaTextImpl(this.getPeer());
   }

   static native String getMediaTextImpl(long var0);

   public void setMediaText(String var1) throws DOMException {
      setMediaTextImpl(this.getPeer(), var1);
   }

   static native void setMediaTextImpl(long var0, String var2);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public String item(int var1) {
      return itemImpl(this.getPeer(), var1);
   }

   static native String itemImpl(long var0, int var2);

   public void deleteMedium(String var1) throws DOMException {
      deleteMediumImpl(this.getPeer(), var1);
   }

   static native void deleteMediumImpl(long var0, String var2);

   public void appendMedium(String var1) throws DOMException {
      appendMediumImpl(this.getPeer(), var1);
   }

   static native void appendMediumImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         MediaListImpl.dispose(this.peer);
      }
   }
}
