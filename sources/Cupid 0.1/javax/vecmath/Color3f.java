package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3f extends Tuple3f implements Serializable {
  static final long serialVersionUID = -1861792981817493659L;
  
  public Color3f(float x, float y, float z) {
    super(x, y, z);
  }
  
  public Color3f(float[] v) {
    super(v);
  }
  
  public Color3f(Color3f v1) {
    super(v1);
  }
  
  public Color3f(Tuple3f t1) {
    super(t1);
  }
  
  public Color3f(Tuple3d t1) {
    super(t1);
  }
  
  public Color3f(Color color) {
    super(color.getRed() / 255.0F, color
        .getGreen() / 255.0F, color
        .getBlue() / 255.0F);
  }
  
  public Color3f() {}
  
  public final void set(Color color) {
    this.x = color.getRed() / 255.0F;
    this.y = color.getGreen() / 255.0F;
    this.z = color.getBlue() / 255.0F;
  }
  
  public final Color get() {
    int r = Math.round(this.x * 255.0F);
    int g = Math.round(this.y * 255.0F);
    int b = Math.round(this.z * 255.0F);
    return new Color(r, g, b);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Color3f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */