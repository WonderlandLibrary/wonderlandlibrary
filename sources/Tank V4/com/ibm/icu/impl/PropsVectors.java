package com.ibm.icu.impl;

import java.util.Arrays;
import java.util.Comparator;

public class PropsVectors {
   private int[] v;
   private int columns;
   private int maxRows;
   private int rows;
   private int prevRow;
   private boolean isCompacted;
   public static final int FIRST_SPECIAL_CP = 1114112;
   public static final int INITIAL_VALUE_CP = 1114112;
   public static final int ERROR_VALUE_CP = 1114113;
   public static final int MAX_CP = 1114113;
   public static final int INITIAL_ROWS = 4096;
   public static final int MEDIUM_ROWS = 65536;
   public static final int MAX_ROWS = 1114114;

   private int findRow(int var1) {
      boolean var2 = false;
      int var6 = this.prevRow * this.columns;
      if (var1 >= this.v[var6]) {
         if (var1 < this.v[var6 + 1]) {
            return var6;
         }

         var6 += this.columns;
         if (var1 < this.v[var6 + 1]) {
            ++this.prevRow;
            return var6;
         }

         var6 += this.columns;
         if (var1 < this.v[var6 + 1]) {
            this.prevRow += 2;
            return var6;
         }

         if (var1 - this.v[var6 + 1] < 10) {
            this.prevRow += 2;

            do {
               ++this.prevRow;
               var6 += this.columns;
            } while(var1 >= this.v[var6 + 1]);

            return var6;
         }
      } else if (var1 < this.v[1]) {
         this.prevRow = 0;
         return 0;
      }

      int var3 = 0;
      boolean var4 = false;
      int var5 = this.rows;

      while(var3 < var5 - 1) {
         int var7 = (var3 + var5) / 2;
         var6 = this.columns * var7;
         if (var1 < this.v[var6]) {
            var5 = var7;
         } else {
            if (var1 < this.v[var6 + 1]) {
               this.prevRow = var7;
               return var6;
            }

            var3 = var7;
         }
      }

      this.prevRow = var3;
      var6 = var3 * this.columns;
      return var6;
   }

   public PropsVectors(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("numOfColumns need to be no less than 1; but it is " + var1);
      } else {
         this.columns = var1 + 2;
         this.v = new int[4096 * this.columns];
         this.maxRows = 4096;
         this.rows = 3;
         this.prevRow = 0;
         this.isCompacted = false;
         this.v[0] = 0;
         this.v[1] = 1114112;
         int var2 = this.columns;

         for(int var3 = 1114112; var3 <= 1114113; ++var3) {
            this.v[var2] = var3;
            this.v[var2 + 1] = var3 + 1;
            var2 += this.columns;
         }

      }
   }

   public void setValue(int var1, int var2, int var3, int var4, int var5) {
      if (var1 >= 0 && var1 <= var2 && var2 <= 1114113 && var3 >= 0 && var3 < this.columns - 2) {
         if (this.isCompacted) {
            throw new IllegalStateException("Shouldn't be called aftercompact()!");
         } else {
            int var8 = var2 + 1;
            var3 += 2;
            var4 &= var5;
            int var6 = this.findRow(var1);
            int var7 = this.findRow(var2);
            boolean var9 = var1 != this.v[var6] && var4 != (this.v[var6 + var3] & var5);
            boolean var10 = var8 != this.v[var7 + 1] && var4 != (this.v[var7 + var3] & var5);
            if (var9 || var10) {
               int var11 = 0;
               if (var9) {
                  ++var11;
               }

               if (var10) {
                  ++var11;
               }

               boolean var12 = false;
               if (this.rows + var11 > this.maxRows) {
                  int var14;
                  if (this.maxRows < 65536) {
                     var14 = 65536;
                  } else {
                     if (this.maxRows >= 1114114) {
                        throw new IndexOutOfBoundsException("MAX_ROWS exceeded! Increase it to a higher valuein the implementation");
                     }

                     var14 = 1114114;
                  }

                  int[] var13 = new int[var14 * this.columns];
                  System.arraycopy(this.v, 0, var13, 0, this.rows * this.columns);
                  this.v = var13;
                  this.maxRows = var14;
               }

               int var15 = this.rows * this.columns - (var7 + this.columns);
               if (var15 > 0) {
                  System.arraycopy(this.v, var7 + this.columns, this.v, var7 + (1 + var11) * this.columns, var15);
               }

               this.rows += var11;
               if (var9) {
                  var15 = var7 - var6 + this.columns;
                  System.arraycopy(this.v, var6, this.v, var6 + this.columns, var15);
                  var7 += this.columns;
                  this.v[var6 + 1] = this.v[var6 + this.columns] = var1;
                  var6 += this.columns;
               }

               if (var10) {
                  System.arraycopy(this.v, var7, this.v, var7 + this.columns, this.columns);
                  this.v[var7 + 1] = this.v[var7 + this.columns] = var8;
               }
            }

            this.prevRow = var7 / this.columns;
            var6 += var3;
            var7 += var3;
            var5 = ~var5;

            while(true) {
               this.v[var6] = this.v[var6] & var5 | var4;
               if (var6 == var7) {
                  return;
               }

               var6 += this.columns;
            }
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int getValue(int var1, int var2) {
      if (!this.isCompacted && var1 >= 0 && var1 <= 1114113 && var2 >= 0 && var2 < this.columns - 2) {
         int var3 = this.findRow(var1);
         return this.v[var3 + 2 + var2];
      } else {
         return 0;
      }
   }

   public int[] getRow(int var1) {
      if (this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method after compact()");
      } else if (var1 >= 0 && var1 <= this.rows) {
         int[] var2 = new int[this.columns - 2];
         System.arraycopy(this.v, var1 * this.columns + 2, var2, 0, this.columns - 2);
         return var2;
      } else {
         throw new IllegalArgumentException("rowIndex out of bound!");
      }
   }

   public int getRowStart(int var1) {
      if (this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method after compact()");
      } else if (var1 >= 0 && var1 <= this.rows) {
         return this.v[var1 * this.columns];
      } else {
         throw new IllegalArgumentException("rowIndex out of bound!");
      }
   }

   public int getRowEnd(int var1) {
      if (this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method after compact()");
      } else if (var1 >= 0 && var1 <= this.rows) {
         return this.v[var1 * this.columns + 1] - 1;
      } else {
         throw new IllegalArgumentException("rowIndex out of bound!");
      }
   }

   public void compact(PropsVectors.CompactHandler var1) {
      if (!this.isCompacted) {
         this.isCompacted = true;
         int var2 = this.columns - 2;
         Integer[] var3 = new Integer[this.rows];

         int var4;
         for(var4 = 0; var4 < this.rows; ++var4) {
            var3[var4] = this.columns * var4;
         }

         Arrays.sort(var3, new Comparator(this) {
            final PropsVectors this$0;

            {
               this.this$0 = var1;
            }

            public int compare(Integer var1, Integer var2) {
               int var3 = var1;
               int var4 = var2;
               int var5 = PropsVectors.access$000(this.this$0);
               int var6 = 2;

               do {
                  if (PropsVectors.access$100(this.this$0)[var3 + var6] != PropsVectors.access$100(this.this$0)[var4 + var6]) {
                     return PropsVectors.access$100(this.this$0)[var3 + var6] < PropsVectors.access$100(this.this$0)[var4 + var6] ? -1 : 1;
                  }

                  ++var6;
                  if (var6 == PropsVectors.access$000(this.this$0)) {
                     var6 = 0;
                  }

                  --var5;
               } while(var5 > 0);

               return 0;
            }

            public int compare(Object var1, Object var2) {
               return this.compare((Integer)var1, (Integer)var2);
            }
         });
         var4 = -var2;

         int var10001;
         int var6;
         for(int var5 = 0; var5 < this.rows; ++var5) {
            label48: {
               var6 = this.v[var3[var5]];
               if (var4 >= 0) {
                  var10001 = var3[var5] + 2;
                  int[] var10002 = this.v;
                  if (var3[var5 - 1] + 2 >= var2) {
                     break label48;
                  }
               }

               var4 += var2;
            }

            if (var6 == 1114112) {
               var1.setRowIndexForInitialValue(var4);
            } else if (var6 == 1114113) {
               var1.setRowIndexForErrorValue(var4);
            }
         }

         var4 += var2;
         var1.startRealValues(var4);
         int[] var9 = new int[var4];
         var4 = -var2;

         for(var6 = 0; var6 < this.rows; ++var6) {
            int var7;
            int var8;
            label39: {
               var7 = this.v[var3[var6]];
               var8 = this.v[var3[var6] + 1];
               if (var4 >= 0) {
                  var10001 = var3[var6] + 2;
                  if (var4 >= var2) {
                     break label39;
                  }
               }

               var4 += var2;
               System.arraycopy(this.v, var3[var6] + 2, var9, var4, var2);
            }

            if (var7 < 1114112) {
               var1.setRowIndexForRange(var7, var8 - 1, var4);
            }
         }

         this.v = var9;
         this.rows = var4 / var2 + 1;
      }
   }

   public int[] getCompactedArray() {
      if (!this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method before compact()");
      } else {
         return this.v;
      }
   }

   public int getCompactedRows() {
      if (!this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method before compact()");
      } else {
         return this.rows;
      }
   }

   public int getCompactedColumns() {
      if (!this.isCompacted) {
         throw new IllegalStateException("Illegal Invocation of the method before compact()");
      } else {
         return this.columns - 2;
      }
   }

   public IntTrie compactToTrieWithRowIndexes() {
      PVecToTrieCompactHandler var1 = new PVecToTrieCompactHandler();
      this.compact(var1);
      return var1.builder.serialize(new PropsVectors.DefaultGetFoldedValue(var1.builder), new PropsVectors.DefaultGetFoldingOffset());
   }

   static int access$000(PropsVectors var0) {
      return var0.columns;
   }

   static int[] access$100(PropsVectors var0) {
      return var0.v;
   }

   public interface CompactHandler {
      void setRowIndexForRange(int var1, int var2, int var3);

      void setRowIndexForInitialValue(int var1);

      void setRowIndexForErrorValue(int var1);

      void startRealValues(int var1);
   }

   private static class DefaultGetFoldedValue implements TrieBuilder.DataManipulate {
      private IntTrieBuilder builder;

      public DefaultGetFoldedValue(IntTrieBuilder var1) {
         this.builder = var1;
      }

      public int getFoldedValue(int var1, int var2) {
         int var3 = this.builder.m_initialValue_;
         int var4 = var1 + 1024;

         while(var1 < var4) {
            boolean[] var5 = new boolean[1];
            int var6 = this.builder.getValue(var1, var5);
            if (var5[0]) {
               var1 += 32;
            } else {
               if (var6 != var3) {
                  return var2;
               }

               ++var1;
            }
         }

         return 0;
      }
   }

   private static class DefaultGetFoldingOffset implements Trie.DataManipulate {
      private DefaultGetFoldingOffset() {
      }

      public int getFoldingOffset(int var1) {
         return var1;
      }

      DefaultGetFoldingOffset(Object var1) {
         this();
      }
   }
}
