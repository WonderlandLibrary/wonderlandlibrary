package com.sun.media.jfxmedia.events;

public class MarkerEvent extends PlayerEvent {
   private String markerName;
   private double presentationTime;

   public MarkerEvent(String var1, double var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("name == null!");
      } else if (var2 < 0.0) {
         throw new IllegalArgumentException("time < 0.0!");
      } else {
         this.markerName = var1;
         this.presentationTime = var2;
      }
   }

   public String getMarkerName() {
      return this.markerName;
   }

   public double getPresentationTime() {
      return this.presentationTime;
   }
}
