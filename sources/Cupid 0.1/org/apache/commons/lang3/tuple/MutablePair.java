package org.apache.commons.lang3.tuple;

public class MutablePair<L, R> extends Pair<L, R> {
  private static final long serialVersionUID = 4954918890077093841L;
  
  public L left;
  
  public R right;
  
  public static <L, R> MutablePair<L, R> of(L left, R right) {
    return new MutablePair<L, R>(left, right);
  }
  
  public MutablePair() {}
  
  public MutablePair(L left, R right) {
    this.left = left;
    this.right = right;
  }
  
  public L getLeft() {
    return this.left;
  }
  
  public void setLeft(L left) {
    this.left = left;
  }
  
  public R getRight() {
    return this.right;
  }
  
  public void setRight(R right) {
    this.right = right;
  }
  
  public R setValue(R value) {
    R result = getRight();
    setRight(value);
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\tuple\MutablePair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */