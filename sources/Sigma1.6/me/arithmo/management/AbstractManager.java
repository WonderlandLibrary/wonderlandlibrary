package me.arithmo.management;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractManager<E>
{
  private final Class typeClass;
  protected E[] array;
  
  public AbstractManager(Class<E> typeClass, int size)
  {
    this.typeClass = typeClass;
    reset(size);
  }
  
  public abstract void setup();
  
  public void set(E item, int index)
  {
    this.array[index] = item;
  }
  
  public E get(int index)
  {
    return (E)this.array[index];
  }
  
  public E get(Class clazz)
  {
    for (E e : this.array) {
      if (e.getClass().equals(clazz)) {
        return e;
      }
    }
    return null;
  }
  
  public void add(E e)
  {
    this.array = Arrays.copyOf(this.array, this.array.length + 1);
    this.array[(this.array.length - 1)] = e;
  }
  
  public void remove(E e)
  {
    this.array = ArrayUtils.removeElement(this.array, e);
  }
  
  public void reset(int size)
  {
    this.array = (E[]) ((Object[])Array.newInstance(this.typeClass, size));
  }
  
  public E[] getArray()
  {
    return this.array;
  }
}
