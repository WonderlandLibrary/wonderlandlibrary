package org.lwjgl.input;

public interface Controller {
  String getName();
  
  int getIndex();
  
  int getButtonCount();
  
  String getButtonName(int paramInt);
  
  boolean isButtonPressed(int paramInt);
  
  void poll();
  
  float getPovX();
  
  float getPovY();
  
  float getDeadZone(int paramInt);
  
  void setDeadZone(int paramInt, float paramFloat);
  
  int getAxisCount();
  
  String getAxisName(int paramInt);
  
  float getAxisValue(int paramInt);
  
  float getXAxisValue();
  
  float getXAxisDeadZone();
  
  void setXAxisDeadZone(float paramFloat);
  
  float getYAxisValue();
  
  float getYAxisDeadZone();
  
  void setYAxisDeadZone(float paramFloat);
  
  float getZAxisValue();
  
  float getZAxisDeadZone();
  
  void setZAxisDeadZone(float paramFloat);
  
  float getRXAxisValue();
  
  float getRXAxisDeadZone();
  
  void setRXAxisDeadZone(float paramFloat);
  
  float getRYAxisValue();
  
  float getRYAxisDeadZone();
  
  void setRYAxisDeadZone(float paramFloat);
  
  float getRZAxisValue();
  
  float getRZAxisDeadZone();
  
  void setRZAxisDeadZone(float paramFloat);
  
  int getRumblerCount();
  
  String getRumblerName(int paramInt);
  
  void setRumblerStrength(int paramInt, float paramFloat);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\input\Controller.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */