package javax.vecmath;

import java.io.Serializable;

public class Point4i extends Tuple4i implements Serializable {
  static final long serialVersionUID = 620124780244617983L;
  
  public Point4i(int x, int y, int z, int w) {
    super(x, y, z, w);
  }
  
  public Point4i(int[] t) {
    super(t);
  }
  
  public Point4i(Tuple4i t1) {
    super(t1);
  }
  
  public Point4i() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Point4i.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */