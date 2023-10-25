package org.apache.commons.lang3.tuple;

public class MutableTriple<L, M, R> extends Triple<L, M, R> {
  private static final long serialVersionUID = 1L;
  
  public L left;
  
  public M middle;
  
  public R right;
  
  public static <L, M, R> MutableTriple<L, M, R> of(L left, M middle, R right) {
    return new MutableTriple<L, M, R>(left, middle, right);
  }
  
  public MutableTriple() {}
  
  public MutableTriple(L left, M middle, R right) {
    this.left = left;
    this.middle = middle;
    this.right = right;
  }
  
  public L getLeft() {
    return this.left;
  }
  
  public void setLeft(L left) {
    this.left = left;
  }
  
  public M getMiddle() {
    return this.middle;
  }
  
  public void setMiddle(M middle) {
    this.middle = middle;
  }
  
  public R getRight() {
    return this.right;
  }
  
  public void setRight(R right) {
    this.right = right;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\tuple\MutableTriple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */