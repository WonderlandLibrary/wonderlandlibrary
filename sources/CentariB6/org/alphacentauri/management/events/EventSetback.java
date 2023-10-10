package org.alphacentauri.management.events;

import org.alphacentauri.management.events.Event;

public class EventSetback extends Event {
   private final double playerX;
   private final double playerY;
   private final double playerZ;
   private final double setbackX;
   private final double setbackY;
   private final double setbackZ;
   private final float playerYaw;
   private final float playerPitch;
   private final float setbackYaw;
   private final float setbackPitch;

   public EventSetback(double playerX, double playerY, double playerZ, double setbackX, double setbackY, double setbackZ, float playerYaw, float playerPitch, float setbackYaw, float setbackPitch) {
      this.playerX = playerX;
      this.playerY = playerY;
      this.playerZ = playerZ;
      this.setbackX = setbackX;
      this.setbackY = setbackY;
      this.setbackZ = setbackZ;
      this.playerYaw = playerYaw;
      this.playerPitch = playerPitch;
      this.setbackYaw = setbackYaw;
      this.setbackPitch = setbackPitch;
   }

   public double getDX() {
      return this.getPlayerX() - this.getSetbackX();
   }

   public double getDY() {
      return this.getPlayerY() - this.getSetbackY();
   }

   public float getSetbackPitch() {
      return this.setbackPitch;
   }

   public float getSetbackYaw() {
      return this.setbackYaw;
   }

   public double getDistVertSq() {
      return this.getDY() * this.getDY();
   }

   public double getSetbackZ() {
      return this.setbackZ;
   }

   public double getPlayerY() {
      return this.playerY;
   }

   public double getSetbackY() {
      return this.setbackY;
   }

   public double getDistSq() {
      return this.getDX() * this.getDX() + this.getDY() * this.getDY() + this.getDZ() * this.getDZ();
   }

   public double getPlayerZ() {
      return this.playerZ;
   }

   public double getDist() {
      return Math.sqrt(this.getDistSq());
   }

   public double getDistVert() {
      return Math.abs(this.getDY());
   }

   public double getDistHor() {
      return Math.sqrt(this.getDistHor());
   }

   public double getDZ() {
      return this.getPlayerZ() - this.getSetbackZ();
   }

   public double getDistHorSq() {
      return this.getDX() * this.getDX() + this.getDZ() * this.getDZ();
   }

   public float getPlayerYaw() {
      return this.playerYaw;
   }

   public float getPlayerPitch() {
      return this.playerPitch;
   }

   public double getPlayerX() {
      return this.playerX;
   }

   public double getSetbackX() {
      return this.setbackX;
   }
}
