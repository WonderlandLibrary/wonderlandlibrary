package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.util.AbstractList;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
  private final transient ImmutableList<List<E>> axes;
  
  private final transient int[] axesSizeProduct;
  
  static <E> List<List<E>> create(List<? extends List<? extends E>> lists) {
    ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder<List<E>>(lists.size());
    for (List<? extends E> list : lists) {
      List<E> copy = ImmutableList.copyOf(list);
      if (copy.isEmpty())
        return ImmutableList.of(); 
      axesBuilder.add(copy);
    } 
    return new CartesianList<E>(axesBuilder.build());
  }
  
  CartesianList(ImmutableList<List<E>> axes) {
    this.axes = axes;
    int[] axesSizeProduct = new int[axes.size() + 1];
    axesSizeProduct[axes.size()] = 1;
    try {
      for (int i = axes.size() - 1; i >= 0; i--)
        axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], ((List)axes.get(i)).size()); 
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
    } 
    this.axesSizeProduct = axesSizeProduct;
  }
  
  private int getAxisIndexForProductIndex(int index, int axis) {
    return index / this.axesSizeProduct[axis + 1] % ((List)this.axes.get(axis)).size();
  }
  
  public ImmutableList<E> get(final int index) {
    Preconditions.checkElementIndex(index, size());
    return new ImmutableList<E>() {
        public int size() {
          return CartesianList.this.axes.size();
        }
        
        public E get(int axis) {
          Preconditions.checkElementIndex(axis, size());
          int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
          return ((List<E>)CartesianList.this.axes.get(axis)).get(axisIndex);
        }
        
        boolean isPartialView() {
          return true;
        }
      };
  }
  
  public int size() {
    return this.axesSizeProduct[0];
  }
  
  public boolean contains(@Nullable Object o) {
    if (!(o instanceof List))
      return false; 
    List<?> list = (List)o;
    if (list.size() != this.axes.size())
      return false; 
    ListIterator<?> itr = list.listIterator();
    while (itr.hasNext()) {
      int index = itr.nextIndex();
      if (!((List)this.axes.get(index)).contains(itr.next()))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\CartesianList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */