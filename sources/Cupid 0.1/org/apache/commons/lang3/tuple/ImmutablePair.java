package org.apache.commons.lang3.tuple;

public final class ImmutablePair<L, R> extends Pair<L, R> {
  private static final long serialVersionUID = 4954918890077093841L;
  
  public final L left;
  
  public final R right;
  
  public static <L, R> ImmutablePair<L, R> of(L left, R right) {
    return new ImmutablePair<L, R>(left, right);
  }
  
  public ImmutablePair(L left, R right) {
    this.left = left;
    this.right = right;
  }
  
  public L getLeft() {
    return this.left;
  }
  
  public R getRight() {
    return this.right;
  }
  
  public R setValue(R value) {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\tuple\ImmutablePair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */