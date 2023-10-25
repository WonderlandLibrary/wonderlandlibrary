package org.apache.commons.lang3.tuple;

public final class ImmutableTriple<L, M, R> extends Triple<L, M, R> {
  private static final long serialVersionUID = 1L;
  
  public final L left;
  
  public final M middle;
  
  public final R right;
  
  public static <L, M, R> ImmutableTriple<L, M, R> of(L left, M middle, R right) {
    return new ImmutableTriple<L, M, R>(left, middle, right);
  }
  
  public ImmutableTriple(L left, M middle, R right) {
    this.left = left;
    this.middle = middle;
    this.right = right;
  }
  
  public L getLeft() {
    return this.left;
  }
  
  public M getMiddle() {
    return this.middle;
  }
  
  public R getRight() {
    return this.right;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\tuple\ImmutableTriple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */