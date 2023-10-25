package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Bytes {
  public static int hashCode(byte value) {
    return value;
  }
  
  public static boolean contains(byte[] array, byte target) {
    for (byte value : array) {
      if (value == target)
        return true; 
    } 
    return false;
  }
  
  public static int indexOf(byte[] array, byte target) {
    return indexOf(array, target, 0, array.length);
  }
  
  private static int indexOf(byte[] array, byte target, int start, int end) {
    for (int i = start; i < end; i++) {
      if (array[i] == target)
        return i; 
    } 
    return -1;
  }
  
  public static int indexOf(byte[] array, byte[] target) {
    Preconditions.checkNotNull(array, "array");
    Preconditions.checkNotNull(target, "target");
    if (target.length == 0)
      return 0; 
    for (int i = 0; i < array.length - target.length + 1; i++) {
      int j = 0;
      while (true) {
        if (j < target.length) {
          if (array[i + j] != target[j])
            break; 
          j++;
          continue;
        } 
        return i;
      } 
    } 
    return -1;
  }
  
  public static int lastIndexOf(byte[] array, byte target) {
    return lastIndexOf(array, target, 0, array.length);
  }
  
  private static int lastIndexOf(byte[] array, byte target, int start, int end) {
    for (int i = end - 1; i >= start; i--) {
      if (array[i] == target)
        return i; 
    } 
    return -1;
  }
  
  public static byte[] concat(byte[]... arrays) {
    int length = 0;
    for (byte[] array : arrays)
      length += array.length; 
    byte[] result = new byte[length];
    int pos = 0;
    for (byte[] array : arrays) {
      System.arraycopy(array, 0, result, pos, array.length);
      pos += array.length;
    } 
    return result;
  }
  
  public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
    Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[] { Integer.valueOf(minLength) });
    Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[] { Integer.valueOf(padding) });
    return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
  }
  
  private static byte[] copyOf(byte[] original, int length) {
    byte[] copy = new byte[length];
    System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
    return copy;
  }
  
  public static byte[] toArray(Collection<? extends Number> collection) {
    if (collection instanceof ByteArrayAsList)
      return ((ByteArrayAsList)collection).toByteArray(); 
    Object[] boxedArray = collection.toArray();
    int len = boxedArray.length;
    byte[] array = new byte[len];
    for (int i = 0; i < len; i++)
      array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue(); 
    return array;
  }
  
  public static List<Byte> asList(byte... backingArray) {
    if (backingArray.length == 0)
      return Collections.emptyList(); 
    return new ByteArrayAsList(backingArray);
  }
  
  @GwtCompatible
  private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
    final byte[] array;
    
    final int start;
    
    final int end;
    
    private static final long serialVersionUID = 0L;
    
    ByteArrayAsList(byte[] array) {
      this(array, 0, array.length);
    }
    
    ByteArrayAsList(byte[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }
    
    public int size() {
      return this.end - this.start;
    }
    
    public boolean isEmpty() {
      return false;
    }
    
    public Byte get(int index) {
      Preconditions.checkElementIndex(index, size());
      return Byte.valueOf(this.array[this.start + index]);
    }
    
    public boolean contains(Object target) {
      return (target instanceof Byte && Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end) != -1);
    }
    
    public int indexOf(Object target) {
      if (target instanceof Byte) {
        int i = Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
        if (i >= 0)
          return i - this.start; 
      } 
      return -1;
    }
    
    public int lastIndexOf(Object target) {
      if (target instanceof Byte) {
        int i = Bytes.lastIndexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
        if (i >= 0)
          return i - this.start; 
      } 
      return -1;
    }
    
    public Byte set(int index, Byte element) {
      Preconditions.checkElementIndex(index, size());
      byte oldValue = this.array[this.start + index];
      this.array[this.start + index] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
      return Byte.valueOf(oldValue);
    }
    
    public List<Byte> subList(int fromIndex, int toIndex) {
      int size = size();
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
      if (fromIndex == toIndex)
        return Collections.emptyList(); 
      return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
    }
    
    public boolean equals(Object object) {
      if (object == this)
        return true; 
      if (object instanceof ByteArrayAsList) {
        ByteArrayAsList that = (ByteArrayAsList)object;
        int size = size();
        if (that.size() != size)
          return false; 
        for (int i = 0; i < size; i++) {
          if (this.array[this.start + i] != that.array[that.start + i])
            return false; 
        } 
        return true;
      } 
      return super.equals(object);
    }
    
    public int hashCode() {
      int result = 1;
      for (int i = this.start; i < this.end; i++)
        result = 31 * result + Bytes.hashCode(this.array[i]); 
      return result;
    }
    
    public String toString() {
      StringBuilder builder = new StringBuilder(size() * 5);
      builder.append('[').append(this.array[this.start]);
      for (int i = this.start + 1; i < this.end; i++)
        builder.append(", ").append(this.array[i]); 
      return builder.append(']').toString();
    }
    
    byte[] toByteArray() {
      int size = size();
      byte[] result = new byte[size];
      System.arraycopy(this.array, this.start, result, 0, size);
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\primitives\Bytes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */