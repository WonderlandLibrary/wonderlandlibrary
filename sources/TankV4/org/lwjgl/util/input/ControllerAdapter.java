package org.lwjgl.util.input;

import org.lwjgl.input.Controller;

public class ControllerAdapter implements Controller {
   public String getName() {
      return "Dummy Controller";
   }

   public int getIndex() {
      return 0;
   }

   public int getButtonCount() {
      return 0;
   }

   public String getButtonName(int var1) {
      return "button n/a";
   }

   public boolean isButtonPressed(int var1) {
      return false;
   }

   public void poll() {
   }

   public float getPovX() {
      return 0.0F;
   }

   public float getPovY() {
      return 0.0F;
   }

   public float getDeadZone(int var1) {
      return 0.0F;
   }

   public void setDeadZone(int var1, float var2) {
   }

   public int getAxisCount() {
      return 0;
   }

   public String getAxisName(int var1) {
      return "axis n/a";
   }

   public float getAxisValue(int var1) {
      return 0.0F;
   }

   public float getXAxisValue() {
      return 0.0F;
   }

   public float getXAxisDeadZone() {
      return 0.0F;
   }

   public void setXAxisDeadZone(float var1) {
   }

   public float getYAxisValue() {
      return 0.0F;
   }

   public float getYAxisDeadZone() {
      return 0.0F;
   }

   public void setYAxisDeadZone(float var1) {
   }

   public float getZAxisValue() {
      return 0.0F;
   }

   public float getZAxisDeadZone() {
      return 0.0F;
   }

   public void setZAxisDeadZone(float var1) {
   }

   public float getRXAxisValue() {
      return 0.0F;
   }

   public float getRXAxisDeadZone() {
      return 0.0F;
   }

   public void setRXAxisDeadZone(float var1) {
   }

   public float getRYAxisValue() {
      return 0.0F;
   }

   public float getRYAxisDeadZone() {
      return 0.0F;
   }

   public void setRYAxisDeadZone(float var1) {
   }

   public float getRZAxisValue() {
      return 0.0F;
   }

   public float getRZAxisDeadZone() {
      return 0.0F;
   }

   public void setRZAxisDeadZone(float var1) {
   }

   public int getRumblerCount() {
      return 0;
   }

   public String getRumblerName(int var1) {
      return "rumber n/a";
   }

   public void setRumblerStrength(int var1, float var2) {
   }
}
