package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color4b extends Tuple4b implements Serializable {
  static final long serialVersionUID = -105080578052502155L;
  
  public Color4b(byte b1, byte b2, byte b3, byte b4) {
    super(b1, b2, b3, b4);
  }
  
  public Color4b(byte[] c) {
    super(c);
  }
  
  public Color4b(Color4b c1) {
    super(c1);
  }
  
  public Color4b(Tuple4b t1) {
    super(t1);
  }
  
  public Color4b(Color color) {
    super((byte)color.getRed(), 
        (byte)color.getGreen(), 
        (byte)color.getBlue(), 
        (byte)color.getAlpha());
  }
  
  public Color4b() {}
  
  public final void set(Color color) {
    this.x = (byte)color.getRed();
    this.y = (byte)color.getGreen();
    this.z = (byte)color.getBlue();
    this.w = (byte)color.getAlpha();
  }
  
  public final Color get() {
    int r = this.x & 0xFF;
    int g = this.y & 0xFF;
    int b = this.z & 0xFF;
    int a = this.w & 0xFF;
    return new Color(r, g, b, a);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Color4b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */