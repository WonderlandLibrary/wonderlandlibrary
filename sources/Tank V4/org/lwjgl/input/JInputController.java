package org.lwjgl.input;

import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;

class JInputController implements Controller {
   private net.java.games.input.Controller target;
   private int index;
   private ArrayList buttons = new ArrayList();
   private ArrayList axes = new ArrayList();
   private ArrayList pov = new ArrayList();
   private Rumbler[] rumblers;
   private boolean[] buttonState;
   private float[] povValues;
   private float[] axesValue;
   private float[] axesMax;
   private float[] deadZones;
   private int xaxis = -1;
   private int yaxis = -1;
   private int zaxis = -1;
   private int rxaxis = -1;
   private int ryaxis = -1;
   private int rzaxis = -1;

   JInputController(int var1, net.java.games.input.Controller var2) {
      this.target = var2;
      this.index = var1;
      Component[] var3 = var2.getComponents();
      Component[] var4 = var3;
      int var5 = var3.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         Component var7 = var4[var6];
         if (var7.getIdentifier() instanceof Button) {
            this.buttons.add(var7);
         } else if (var7.getIdentifier().equals(Axis.POV)) {
            this.pov.add(var7);
         } else {
            this.axes.add(var7);
         }
      }

      this.buttonState = new boolean[this.buttons.size()];
      this.povValues = new float[this.pov.size()];
      this.axesValue = new float[this.axes.size()];
      int var10 = 0;
      var5 = 0;
      Component[] var11 = var3;
      int var12 = var3.length;

      for(int var8 = 0; var8 < var12; ++var8) {
         Component var9 = var11[var8];
         if (var9.getIdentifier() instanceof Button) {
            this.buttonState[var10] = var9.getPollData() != 0.0F;
            ++var10;
         } else if (!var9.getIdentifier().equals(Axis.POV)) {
            this.axesValue[var5] = var9.getPollData();
            if (var9.getIdentifier().equals(Axis.X)) {
               this.xaxis = var5;
            }

            if (var9.getIdentifier().equals(Axis.Y)) {
               this.yaxis = var5;
            }

            if (var9.getIdentifier().equals(Axis.Z)) {
               this.zaxis = var5;
            }

            if (var9.getIdentifier().equals(Axis.RX)) {
               this.rxaxis = var5;
            }

            if (var9.getIdentifier().equals(Axis.RY)) {
               this.ryaxis = var5;
            }

            if (var9.getIdentifier().equals(Axis.RZ)) {
               this.rzaxis = var5;
            }

            ++var5;
         }
      }

      this.axesMax = new float[this.axes.size()];
      this.deadZones = new float[this.axes.size()];

      for(var6 = 0; var6 < this.axesMax.length; ++var6) {
         this.axesMax[var6] = 1.0F;
         this.deadZones[var6] = 0.05F;
      }

      this.rumblers = var2.getRumblers();
   }

   public String getName() {
      String var1 = this.target.getName();
      return var1;
   }

   public int getIndex() {
      return this.index;
   }

   public int getButtonCount() {
      return this.buttons.size();
   }

   public String getButtonName(int var1) {
      return ((Component)this.buttons.get(var1)).getName();
   }

   public boolean isButtonPressed(int var1) {
      return this.buttonState[var1];
   }

   public void poll() {
      this.target.poll();
      Event var1 = new Event();
      EventQueue var2 = this.target.getEventQueue();

      while(var2.getNextEvent(var1)) {
         Component var3;
         int var4;
         if (this.buttons.contains(var1.getComponent())) {
            var3 = var1.getComponent();
            var4 = this.buttons.indexOf(var3);
            this.buttonState[var4] = var1.getValue() != 0.0F;
            Controllers.addEvent(new ControllerEvent(this, var1.getNanos(), 1, var4, this.buttonState[var4], false, false, 0.0F, 0.0F));
         }

         float var5;
         float var6;
         if (this.pov.contains(var1.getComponent())) {
            var3 = var1.getComponent();
            var4 = this.pov.indexOf(var3);
            var5 = this.getPovX();
            var6 = this.getPovY();
            this.povValues[var4] = var1.getValue();
            if (var5 != this.getPovX()) {
               Controllers.addEvent(new ControllerEvent(this, var1.getNanos(), 3, 0, false, false));
            }

            if (var6 != this.getPovY()) {
               Controllers.addEvent(new ControllerEvent(this, var1.getNanos(), 4, 0, false, false));
            }
         }

         if (this.axes.contains(var1.getComponent())) {
            var3 = var1.getComponent();
            var4 = this.axes.indexOf(var3);
            var5 = var3.getPollData();
            var6 = 0.0F;
            float var7 = 0.0F;
            if (Math.abs(var5) < this.deadZones[var4]) {
               var5 = 0.0F;
            }

            if (Math.abs(var5) < var3.getDeadZone()) {
               var5 = 0.0F;
            }

            if (Math.abs(var5) > this.axesMax[var4]) {
               this.axesMax[var4] = Math.abs(var5);
            }

            var5 /= this.axesMax[var4];
            if (var4 == this.xaxis) {
               var6 = var5;
            }

            if (var4 == this.yaxis) {
               var7 = var5;
            }

            Controllers.addEvent(new ControllerEvent(this, var1.getNanos(), 2, var4, false, var4 == this.xaxis, var4 == this.yaxis, var6, var7));
            this.axesValue[var4] = var5;
         }
      }

   }

   public int getAxisCount() {
      return this.axes.size();
   }

   public String getAxisName(int var1) {
      return ((Component)this.axes.get(var1)).getName();
   }

   public float getAxisValue(int var1) {
      return this.axesValue[var1];
   }

   public float getXAxisValue() {
      return this.xaxis == -1 ? 0.0F : this.getAxisValue(this.xaxis);
   }

   public float getYAxisValue() {
      return this.yaxis == -1 ? 0.0F : this.getAxisValue(this.yaxis);
   }

   public float getXAxisDeadZone() {
      return this.xaxis == -1 ? 0.0F : this.getDeadZone(this.xaxis);
   }

   public float getYAxisDeadZone() {
      return this.yaxis == -1 ? 0.0F : this.getDeadZone(this.yaxis);
   }

   public void setXAxisDeadZone(float var1) {
      this.setDeadZone(this.xaxis, var1);
   }

   public void setYAxisDeadZone(float var1) {
      this.setDeadZone(this.yaxis, var1);
   }

   public float getDeadZone(int var1) {
      return this.deadZones[var1];
   }

   public void setDeadZone(int var1, float var2) {
      this.deadZones[var1] = var2;
   }

   public float getZAxisValue() {
      return this.zaxis == -1 ? 0.0F : this.getAxisValue(this.zaxis);
   }

   public float getZAxisDeadZone() {
      return this.zaxis == -1 ? 0.0F : this.getDeadZone(this.zaxis);
   }

   public void setZAxisDeadZone(float var1) {
      this.setDeadZone(this.zaxis, var1);
   }

   public float getRXAxisValue() {
      return this.rxaxis == -1 ? 0.0F : this.getAxisValue(this.rxaxis);
   }

   public float getRXAxisDeadZone() {
      return this.rxaxis == -1 ? 0.0F : this.getDeadZone(this.rxaxis);
   }

   public void setRXAxisDeadZone(float var1) {
      this.setDeadZone(this.rxaxis, var1);
   }

   public float getRYAxisValue() {
      return this.ryaxis == -1 ? 0.0F : this.getAxisValue(this.ryaxis);
   }

   public float getRYAxisDeadZone() {
      return this.ryaxis == -1 ? 0.0F : this.getDeadZone(this.ryaxis);
   }

   public void setRYAxisDeadZone(float var1) {
      this.setDeadZone(this.ryaxis, var1);
   }

   public float getRZAxisValue() {
      return this.rzaxis == -1 ? 0.0F : this.getAxisValue(this.rzaxis);
   }

   public float getRZAxisDeadZone() {
      return this.rzaxis == -1 ? 0.0F : this.getDeadZone(this.rzaxis);
   }

   public void setRZAxisDeadZone(float var1) {
      this.setDeadZone(this.rzaxis, var1);
   }

   public float getPovX() {
      if (this.pov.size() == 0) {
         return 0.0F;
      } else {
         float var1 = this.povValues[0];
         if (var1 != 0.875F && var1 != 0.125F && var1 != 1.0F) {
            return var1 != 0.625F && var1 != 0.375F && var1 != 0.5F ? 0.0F : 1.0F;
         } else {
            return -1.0F;
         }
      }
   }

   public float getPovY() {
      if (this.pov.size() == 0) {
         return 0.0F;
      } else {
         float var1 = this.povValues[0];
         if (var1 != 0.875F && var1 != 0.625F && var1 != 0.75F) {
            return var1 != 0.125F && var1 != 0.375F && var1 != 0.25F ? 0.0F : -1.0F;
         } else {
            return 1.0F;
         }
      }
   }

   public int getRumblerCount() {
      return this.rumblers.length;
   }

   public String getRumblerName(int var1) {
      return this.rumblers[var1].getAxisName();
   }

   public void setRumblerStrength(int var1, float var2) {
      this.rumblers[var1].rumble(var2);
   }
}
