package joptsimple.internal;

public final class Objects {
  private Objects() {
    throw new UnsupportedOperationException();
  }
  
  public static void ensureNotNull(Object target) {
    if (target == null)
      throw new NullPointerException(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Objects.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */