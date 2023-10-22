package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class NativeMedia extends Media {
   protected final Lock markerLock = new ReentrantLock();
   protected final Lock listenerLock = new ReentrantLock();
   protected Map markersByName;
   protected NavigableMap markersByTime;
   protected WeakHashMap markerListeners;

   protected NativeMedia(Locator var1) {
      super(var1);
   }

   public abstract Platform getPlatform();

   public void addTrack(Track var1) {
      super.addTrack(var1);
   }

   public void addMarker(String var1, double var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("markerName == null!");
      } else if (var2 < 0.0) {
         throw new IllegalArgumentException("presentationTime < 0");
      } else {
         this.markerLock.lock();

         try {
            if (this.markersByName == null) {
               this.markersByName = new HashMap();
               this.markersByTime = new TreeMap();
            }

            this.markersByName.put(var1, var2);
            this.markersByTime.put(var2, var1);
         } finally {
            this.markerLock.unlock();
         }

         this.fireMarkerStateEvent(true);
      }
   }

   public Map getMarkers() {
      Map var1 = null;
      this.markerLock.lock();

      try {
         if (this.markersByName != null && !this.markersByName.isEmpty()) {
            var1 = Collections.unmodifiableMap(this.markersByName);
         }
      } finally {
         this.markerLock.unlock();
      }

      return var1;
   }

   public double removeMarker(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("markerName == null!");
      } else {
         double var2 = -1.0;
         boolean var4 = false;
         this.markerLock.lock();

         try {
            if (this.markersByName.containsKey(var1)) {
               var2 = (Double)this.markersByName.get(var1);
               this.markersByName.remove(var1);
               this.markersByTime.remove(var2);
               var4 = this.markersByName.size() > 0;
            }
         } finally {
            this.markerLock.unlock();
         }

         this.fireMarkerStateEvent(var4);
         return var2;
      }
   }

   public void removeAllMarkers() {
      this.markerLock.lock();

      try {
         this.markersByName.clear();
         this.markersByTime.clear();
      } finally {
         this.markerLock.unlock();
      }

      this.fireMarkerStateEvent(false);
   }

   public abstract void dispose();

   Map.Entry getNextMarker(double var1, boolean var3) {
      Map.Entry var4 = null;
      this.markerLock.lock();

      try {
         if (this.markersByTime != null) {
            if (var3) {
               var4 = this.markersByTime.ceilingEntry(var1);
            } else {
               var4 = this.markersByTime.higherEntry(var1);
            }
         }
      } finally {
         this.markerLock.unlock();
      }

      return var4;
   }

   void addMarkerStateListener(MarkerStateListener var1) {
      if (var1 != null) {
         this.listenerLock.lock();

         try {
            if (this.markerListeners == null) {
               this.markerListeners = new WeakHashMap();
            }

            this.markerListeners.put(var1, Boolean.TRUE);
         } finally {
            this.listenerLock.unlock();
         }
      }

   }

   void removeMarkerStateListener(MarkerStateListener var1) {
      if (var1 != null) {
         this.listenerLock.lock();

         try {
            if (this.markerListeners != null) {
               this.markerListeners.remove(var1);
            }
         } finally {
            this.listenerLock.unlock();
         }
      }

   }

   void fireMarkerStateEvent(boolean var1) {
      this.listenerLock.lock();

      try {
         if (this.markerListeners != null && !this.markerListeners.isEmpty()) {
            Iterator var2 = this.markerListeners.keySet().iterator();

            while(var2.hasNext()) {
               MarkerStateListener var3 = (MarkerStateListener)var2.next();
               if (var3 != null) {
                  var3.markerStateChanged(var1);
               }
            }
         }
      } finally {
         this.listenerLock.unlock();
      }

   }
}
