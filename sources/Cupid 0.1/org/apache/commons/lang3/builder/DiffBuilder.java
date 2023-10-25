package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class DiffBuilder implements Builder<DiffResult> {
  private final List<Diff<?>> diffs;
  
  private final boolean objectsTriviallyEqual;
  
  private final Object left;
  
  private final Object right;
  
  private final ToStringStyle style;
  
  public DiffBuilder(Object lhs, Object rhs, ToStringStyle style) {
    if (lhs == null)
      throw new IllegalArgumentException("lhs cannot be null"); 
    if (rhs == null)
      throw new IllegalArgumentException("rhs cannot be null"); 
    this.diffs = new ArrayList<Diff<?>>();
    this.left = lhs;
    this.right = rhs;
    this.style = style;
    this.objectsTriviallyEqual = (lhs == rhs || lhs.equals(rhs));
  }
  
  public DiffBuilder append(String fieldName, final boolean lhs, final boolean rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Boolean>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Boolean getLeft() {
              return Boolean.valueOf(lhs);
            }
            
            public Boolean getRight() {
              return Boolean.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final boolean[] lhs, final boolean[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Boolean[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Boolean[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Boolean[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final byte lhs, final byte rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Byte>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Byte getLeft() {
              return Byte.valueOf(lhs);
            }
            
            public Byte getRight() {
              return Byte.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final byte[] lhs, final byte[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Byte[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Byte[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Byte[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final char lhs, final char rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Character>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Character getLeft() {
              return Character.valueOf(lhs);
            }
            
            public Character getRight() {
              return Character.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final char[] lhs, final char[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Character[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Character[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Character[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final double lhs, final double rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs))
      this.diffs.add(new Diff<Double>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Double getLeft() {
              return Double.valueOf(lhs);
            }
            
            public Double getRight() {
              return Double.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final double[] lhs, final double[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Double[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Double[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Double[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final float lhs, final float rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs))
      this.diffs.add(new Diff<Float>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Float getLeft() {
              return Float.valueOf(lhs);
            }
            
            public Float getRight() {
              return Float.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final float[] lhs, final float[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Float[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Float[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Float[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final int lhs, final int rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Integer>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Integer getLeft() {
              return Integer.valueOf(lhs);
            }
            
            public Integer getRight() {
              return Integer.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final int[] lhs, final int[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Integer[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Integer[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Integer[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final long lhs, final long rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Long>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Long getLeft() {
              return Long.valueOf(lhs);
            }
            
            public Long getRight() {
              return Long.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final long[] lhs, final long[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Long[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Long[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Long[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final short lhs, final short rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs != rhs)
      this.diffs.add(new Diff<Short>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Short getLeft() {
              return Short.valueOf(lhs);
            }
            
            public Short getRight() {
              return Short.valueOf(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final short[] lhs, final short[] rhs) {
    if (fieldName == null)
      throw new IllegalArgumentException("Field name cannot be null"); 
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Short[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Short[] getLeft() {
              return ArrayUtils.toObject(lhs);
            }
            
            public Short[] getRight() {
              return ArrayUtils.toObject(rhs);
            }
          }); 
    return this;
  }
  
  public DiffBuilder append(String fieldName, final Object lhs, final Object rhs) {
    Object objectToTest;
    if (this.objectsTriviallyEqual)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs != null) {
      objectToTest = lhs;
    } else {
      objectToTest = rhs;
    } 
    if (objectToTest.getClass().isArray()) {
      if (objectToTest instanceof boolean[])
        return append(fieldName, (boolean[])lhs, (boolean[])rhs); 
      if (objectToTest instanceof byte[])
        return append(fieldName, (byte[])lhs, (byte[])rhs); 
      if (objectToTest instanceof char[])
        return append(fieldName, (char[])lhs, (char[])rhs); 
      if (objectToTest instanceof double[])
        return append(fieldName, (double[])lhs, (double[])rhs); 
      if (objectToTest instanceof float[])
        return append(fieldName, (float[])lhs, (float[])rhs); 
      if (objectToTest instanceof int[])
        return append(fieldName, (int[])lhs, (int[])rhs); 
      if (objectToTest instanceof long[])
        return append(fieldName, (long[])lhs, (long[])rhs); 
      if (objectToTest instanceof short[])
        return append(fieldName, (short[])lhs, (short[])rhs); 
      return append(fieldName, (Object[])lhs, (Object[])rhs);
    } 
    this.diffs.add(new Diff(fieldName) {
          private static final long serialVersionUID = 1L;
          
          public Object getLeft() {
            return lhs;
          }
          
          public Object getRight() {
            return rhs;
          }
        });
    return this;
  }
  
  public DiffBuilder append(String fieldName, final Object[] lhs, final Object[] rhs) {
    if (this.objectsTriviallyEqual)
      return this; 
    if (!Arrays.equals(lhs, rhs))
      this.diffs.add(new Diff<Object[]>(fieldName) {
            private static final long serialVersionUID = 1L;
            
            public Object[] getLeft() {
              return lhs;
            }
            
            public Object[] getRight() {
              return rhs;
            }
          }); 
    return this;
  }
  
  public DiffResult build() {
    return new DiffResult(this.left, this.right, this.diffs, this.style);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\DiffBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */