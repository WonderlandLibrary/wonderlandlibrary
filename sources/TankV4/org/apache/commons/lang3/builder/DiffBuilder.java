package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class DiffBuilder implements Builder {
   private final List diffs;
   private final boolean objectsTriviallyEqual;
   private final Object left;
   private final Object right;
   private final ToStringStyle style;

   public DiffBuilder(Object var1, Object var2, ToStringStyle var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("lhs cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("rhs cannot be null");
      } else {
         this.diffs = new ArrayList();
         this.left = var1;
         this.right = var2;
         this.style = var3;
         this.objectsTriviallyEqual = var1 == var2 || var1.equals(var2);
      }
   }

   public DiffBuilder append(String var1, boolean var2, boolean var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final boolean val$lhs;
               final boolean val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Boolean getLeft() {
                  return this.val$lhs;
               }

               public Boolean getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, boolean[] var2, boolean[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final boolean[] val$lhs;
               final boolean[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Boolean[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Boolean[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, byte var2, byte var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final byte val$lhs;
               final byte val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Byte getLeft() {
                  return this.val$lhs;
               }

               public Byte getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, byte[] var2, byte[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final byte[] val$lhs;
               final byte[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Byte[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Byte[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, char var2, char var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final char val$lhs;
               final char val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Character getLeft() {
                  return this.val$lhs;
               }

               public Character getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, char[] var2, char[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final char[] val$lhs;
               final char[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Character[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Character[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, double var2, double var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (Double.doubleToLongBits(var2) != Double.doubleToLongBits(var4)) {
            this.diffs.add(new Diff(this, var1, var2, var4) {
               private static final long serialVersionUID = 1L;
               final double val$lhs;
               final double val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var5;
               }

               public Double getLeft() {
                  return this.val$lhs;
               }

               public Double getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, double[] var2, double[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final double[] val$lhs;
               final double[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Double[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Double[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, float var2, float var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (Float.floatToIntBits(var2) != Float.floatToIntBits(var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final float val$lhs;
               final float val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Float getLeft() {
                  return this.val$lhs;
               }

               public Float getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, float[] var2, float[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final float[] val$lhs;
               final float[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Float[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Float[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, int var2, int var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final int val$lhs;
               final int val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Integer getLeft() {
                  return this.val$lhs;
               }

               public Integer getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, int[] var2, int[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final int[] val$lhs;
               final int[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Integer[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Integer[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, long var2, long var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var4) {
            this.diffs.add(new Diff(this, var1, var2, var4) {
               private static final long serialVersionUID = 1L;
               final long val$lhs;
               final long val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var5;
               }

               public Long getLeft() {
                  return this.val$lhs;
               }

               public Long getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, long[] var2, long[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final long[] val$lhs;
               final long[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Long[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Long[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, short var2, short var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final short val$lhs;
               final short val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Short getLeft() {
                  return this.val$lhs;
               }

               public Short getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, short[] var2, short[] var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final short[] val$lhs;
               final short[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Short[] getLeft() {
                  return ArrayUtils.toObject(this.val$lhs);
               }

               public Short[] getRight() {
                  return ArrayUtils.toObject(this.val$rhs);
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, Object var2, Object var3) {
      if (this.objectsTriviallyEqual) {
         return this;
      } else if (var2 == var3) {
         return this;
      } else {
         Object var4;
         if (var2 != null) {
            var4 = var2;
         } else {
            var4 = var3;
         }

         if (var4.getClass().isArray()) {
            if (var4 instanceof boolean[]) {
               return this.append(var1, (boolean[])((boolean[])var2), (boolean[])((boolean[])var3));
            } else if (var4 instanceof byte[]) {
               return this.append(var1, (byte[])((byte[])var2), (byte[])((byte[])var3));
            } else if (var4 instanceof char[]) {
               return this.append(var1, (char[])((char[])var2), (char[])((char[])var3));
            } else if (var4 instanceof double[]) {
               return this.append(var1, (double[])((double[])var2), (double[])((double[])var3));
            } else if (var4 instanceof float[]) {
               return this.append(var1, (float[])((float[])var2), (float[])((float[])var3));
            } else if (var4 instanceof int[]) {
               return this.append(var1, (int[])((int[])var2), (int[])((int[])var3));
            } else if (var4 instanceof long[]) {
               return this.append(var1, (long[])((long[])var2), (long[])((long[])var3));
            } else {
               return var4 instanceof short[] ? this.append(var1, (short[])((short[])var2), (short[])((short[])var3)) : this.append(var1, (Object[])((Object[])var2), (Object[])((Object[])var3));
            }
         } else {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final Object val$lhs;
               final Object val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Object getLeft() {
                  return this.val$lhs;
               }

               public Object getRight() {
                  return this.val$rhs;
               }
            });
            return this;
         }
      }
   }

   public DiffBuilder append(String var1, Object[] var2, Object[] var3) {
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(this, var1, var2, var3) {
               private static final long serialVersionUID = 1L;
               final Object[] val$lhs;
               final Object[] val$rhs;
               final DiffBuilder this$0;

               {
                  this.this$0 = var1;
                  this.val$lhs = var3;
                  this.val$rhs = var4;
               }

               public Object[] getLeft() {
                  return this.val$lhs;
               }

               public Object[] getRight() {
                  return this.val$rhs;
               }

               public Object getRight() {
                  return this.getRight();
               }

               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffResult build() {
      return new DiffResult(this.left, this.right, this.diffs, this.style);
   }

   public Object build() {
      return this.build();
   }
}
