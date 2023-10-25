package me.tojatta.api.utilities.angle;

import me.tojatta.api.utilities.vector.impl.Vector2;

public class Angle extends Vector2<Float> {
  public Angle(Float x, Float y) {
    super(x, y);
  }
  
  public Angle setYaw(Float yaw) {
    setX(yaw);
    return this;
  }
  
  public Angle setPitch(Float pitch) {
    setY(pitch);
    return this;
  }
  
  public Float getYaw() {
    return Float.valueOf(getX().floatValue());
  }
  
  public Float getPitch() {
    return Float.valueOf(getY().floatValue());
  }
  
  public Angle constrantAngle() {
    setYaw(Float.valueOf(getYaw().floatValue() % 360.0F));
    setPitch(Float.valueOf(getPitch().floatValue() % 360.0F));
    while (getYaw().floatValue() <= -180.0F)
      setYaw(Float.valueOf(getYaw().floatValue() + 360.0F)); 
    while (getPitch().floatValue() <= -180.0F)
      setPitch(Float.valueOf(getPitch().floatValue() + 360.0F)); 
    while (getYaw().floatValue() > 180.0F)
      setYaw(Float.valueOf(getYaw().floatValue() - 360.0F)); 
    while (getPitch().floatValue() > 180.0F)
      setPitch(Float.valueOf(getPitch().floatValue() - 360.0F)); 
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\tojatta\ap\\utilities\angle\Angle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */