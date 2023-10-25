package javax.vecmath;

class VecMathUtil {
  static int floatToIntBits(float f) {
    if (f == 0.0F)
      return 0; 
    return Float.floatToIntBits(f);
  }
  
  static long doubleToLongBits(double d) {
    if (d == 0.0D)
      return 0L; 
    return Double.doubleToLongBits(d);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\VecMathUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */