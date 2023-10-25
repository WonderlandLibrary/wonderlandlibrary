package javax.vecmath;

import java.io.Serializable;

public class TexCoord4f extends Tuple4f implements Serializable {
  static final long serialVersionUID = -3517736544731446513L;
  
  public TexCoord4f(float x, float y, float z, float w) {
    super(x, y, z, w);
  }
  
  public TexCoord4f(float[] v) {
    super(v);
  }
  
  public TexCoord4f(TexCoord4f v1) {
    super(v1);
  }
  
  public TexCoord4f(Tuple4f t1) {
    super(t1);
  }
  
  public TexCoord4f(Tuple4d t1) {
    super(t1);
  }
  
  public TexCoord4f() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\TexCoord4f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */