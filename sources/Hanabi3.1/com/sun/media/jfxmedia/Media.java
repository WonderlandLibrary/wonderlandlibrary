package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Media {
   private Locator locator;
   private final List tracks = new ArrayList();

   protected Media(Locator var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("locator == null!");
      } else {
         this.locator = var1;
      }
   }

   public abstract void addMarker(String var1, double var2);

   public abstract double removeMarker(String var1);

   public abstract void removeAllMarkers();

   public List getTracks() {
      synchronized(this.tracks) {
         List var1;
         if (this.tracks.isEmpty()) {
            var1 = null;
         } else {
            var1 = Collections.unmodifiableList(new ArrayList(this.tracks));
         }

         return var1;
      }
   }

   public abstract Map getMarkers();

   public Locator getLocator() {
      return this.locator;
   }

   protected void addTrack(Track var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("track == null!");
      } else {
         synchronized(this.tracks) {
            this.tracks.add(var1);
         }
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.tracks != null && !this.tracks.isEmpty()) {
         Iterator var2 = this.tracks.iterator();

         while(var2.hasNext()) {
            Track var3 = (Track)var2.next();
            var1.append(var3);
            var1.append("\n");
         }
      }

      return var1.toString();
   }
}
