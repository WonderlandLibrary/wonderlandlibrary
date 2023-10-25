package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color4f extends Tuple4f implements Serializable {
  static final long serialVersionUID = 8577680141580006740L;
  
  public Color4f(float x, float y, float z, float w) {
    super(x, y, z, w);
  }
  
  public Color4f(float[] c) {
    super(c);
  }
  
  public Color4f(Color4f c1) {
    super(c1);
  }
  
  public Color4f(Tuple4f t1) {
    super(t1);
  }
  
  public Color4f(Tuple4d t1) {
    super(t1);
  }
  
  public Color4f(Color color) {
    super(color.getRed() / 255.0F, color
        .getGreen() / 255.0F, color
        .getBlue() / 255.0F, color
        .getAlpha() / 255.0F);
  }
  
  public Color4f() {}
  
  public final void set(Color color) {
    this.x = color.getRed() / 255.0F;
    this.y = color.getGreen() / 255.0F;
    this.z = color.getBlue() / 255.0F;
    this.w = color.getAlpha() / 255.0F;
  }
  
  public final Color get() {
    int r = Math.round(this.x * 255.0F);
    int g = Math.round(this.y * 255.0F);
    int b = Math.round(this.z * 255.0F);
    int a = Math.round(this.w * 255.0F);
    return new Color(r, g, b, a);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Color4f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */