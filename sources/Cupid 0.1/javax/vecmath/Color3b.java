package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3b extends Tuple3b implements Serializable {
  static final long serialVersionUID = 6632576088353444794L;
  
  public Color3b(byte c1, byte c2, byte c3) {
    super(c1, c2, c3);
  }
  
  public Color3b(byte[] c) {
    super(c);
  }
  
  public Color3b(Color3b c1) {
    super(c1);
  }
  
  public Color3b(Tuple3b t1) {
    super(t1);
  }
  
  public Color3b(Color color) {
    super((byte)color.getRed(), 
        (byte)color.getGreen(), 
        (byte)color.getBlue());
  }
  
  public Color3b() {}
  
  public final void set(Color color) {
    this.x = (byte)color.getRed();
    this.y = (byte)color.getGreen();
    this.z = (byte)color.getBlue();
  }
  
  public final Color get() {
    int r = this.x & 0xFF;
    int g = this.y & 0xFF;
    int b = this.z & 0xFF;
    return new Color(r, g, b);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Color3b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */