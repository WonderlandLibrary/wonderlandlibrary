package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class CrashReportCategory {
   private final List children = Lists.newArrayList();
   private final CrashReport crashReport;
   private StackTraceElement[] stackTrace = new StackTraceElement[0];
   private final String name;

   public static void addBlockInfo(CrashReportCategory var0, BlockPos var1, Block var2, int var3) {
      int var4 = Block.getIdFromBlock(var2);
      var0.addCrashSectionCallable("Block type", new Callable(var4, var2) {
         private final Block val$blockIn;
         private final int val$i;

         public String call() throws Exception {
            try {
               return String.format("ID #%d (%s // %s)", this.val$i, this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName());
            } catch (Throwable var2) {
               return "ID #" + this.val$i;
            }
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.val$i = var1;
            this.val$blockIn = var2;
         }
      });
      var0.addCrashSectionCallable("Block data value", new Callable(var3) {
         private final int val$blockData;

         {
            this.val$blockData = var1;
         }

         public String call() throws Exception {
            if (this.val$blockData < 0) {
               return "Unknown? (Got " + this.val$blockData + ")";
            } else {
               String var1 = String.format("%4s", Integer.toBinaryString(this.val$blockData)).replace(" ", "0");
               return String.format("%1$d / 0x%1$X / 0b%2$s", this.val$blockData, var1);
            }
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var0.addCrashSectionCallable("Block location", new Callable(var1) {
         private final BlockPos val$pos;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.val$pos = var1;
         }

         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(this.val$pos);
         }
      });
   }

   public void addCrashSectionThrowable(String var1, Throwable var2) {
      this.addCrashSection(var1, var2);
   }

   public void appendToStringBuilder(StringBuilder var1) {
      var1.append("-- ").append(this.name).append(" --\n");
      var1.append("Details:");
      Iterator var3 = this.children.iterator();

      while(var3.hasNext()) {
         CrashReportCategory.Entry var2 = (CrashReportCategory.Entry)var3.next();
         var1.append("\n\t");
         var1.append(var2.getKey());
         var1.append(": ");
         var1.append(var2.getValue());
      }

      if (this.stackTrace != null && this.stackTrace.length > 0) {
         var1.append("\nStacktrace:");
         StackTraceElement[] var5;
         int var4 = (var5 = this.stackTrace).length;

         for(int var7 = 0; var7 < var4; ++var7) {
            StackTraceElement var6 = var5[var7];
            var1.append("\n\tat ");
            var1.append(var6.toString());
         }
      }

   }

   public static String getCoordinateInfo(BlockPos var0) {
      int var1 = var0.getX();
      int var2 = var0.getY();
      int var3 = var0.getZ();
      StringBuilder var4 = new StringBuilder();

      try {
         var4.append(String.format("World: (%d,%d,%d)", var1, var2, var3));
      } catch (Throwable var17) {
         var4.append("(Error finding world loc)");
      }

      var4.append(", ");

      int var5;
      int var6;
      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      int var13;
      try {
         var5 = var1 >> 4;
         var6 = var3 >> 4;
         var7 = var1 & 15;
         var8 = var2 >> 4;
         var9 = var3 & 15;
         var10 = var5 << 4;
         var11 = var6 << 4;
         var12 = (var5 + 1 << 4) - 1;
         var13 = (var6 + 1 << 4) - 1;
         var4.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", var7, var8, var9, var5, var6, var10, var11, var12, var13));
      } catch (Throwable var16) {
         var4.append("(Error finding chunk loc)");
      }

      var4.append(", ");

      try {
         var5 = var1 >> 9;
         var6 = var3 >> 9;
         var7 = var5 << 5;
         var8 = var6 << 5;
         var9 = (var5 + 1 << 5) - 1;
         var10 = (var6 + 1 << 5) - 1;
         var11 = var5 << 9;
         var12 = var6 << 9;
         var13 = (var5 + 1 << 9) - 1;
         int var14 = (var6 + 1 << 9) - 1;
         var4.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", var5, var6, var7, var8, var9, var10, var11, var12, var13, var14));
      } catch (Throwable var15) {
         var4.append("(Error finding world loc)");
      }

      return String.valueOf(var4);
   }

   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement var1, StackTraceElement var2) {
      if (this.stackTrace.length != 0 && var1 != null) {
         StackTraceElement var3 = this.stackTrace[0];
         if (var3.isNativeMethod() == var1.isNativeMethod() && var3.getClassName().equals(var1.getClassName()) && var3.getFileName().equals(var1.getFileName()) && var3.getMethodName().equals(var1.getMethodName())) {
            if (var2 != null != this.stackTrace.length > 1) {
               return false;
            } else if (var2 != null && !this.stackTrace[1].equals(var2)) {
               return false;
            } else {
               this.stackTrace[0] = var1;
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public StackTraceElement[] getStackTrace() {
      return this.stackTrace;
   }

   public void addCrashSectionCallable(String var1, Callable var2) {
      try {
         this.addCrashSection(var1, var2.call());
      } catch (Throwable var4) {
         this.addCrashSectionThrowable(var1, var4);
      }

   }

   public int getPrunedStackTrace(int var1) {
      StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
      if (var2.length <= 0) {
         return 0;
      } else {
         this.stackTrace = new StackTraceElement[var2.length - 3 - var1];
         System.arraycopy(var2, 3 + var1, this.stackTrace, 0, this.stackTrace.length);
         return this.stackTrace.length;
      }
   }

   public static void addBlockInfo(CrashReportCategory var0, BlockPos var1, IBlockState var2) {
      var0.addCrashSectionCallable("Block", new Callable(var2) {
         private final IBlockState val$state;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            return this.val$state.toString();
         }

         {
            this.val$state = var1;
         }
      });
      var0.addCrashSectionCallable("Block location", new Callable(var1) {
         private final BlockPos val$pos;

         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(this.val$pos);
         }

         {
            this.val$pos = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
   }

   public static String getCoordinateInfo(double var0, double var2, double var4) {
      return String.format("%.2f,%.2f,%.2f - %s", var0, var2, var4, getCoordinateInfo(new BlockPos(var0, var2, var4)));
   }

   public void addCrashSection(String var1, Object var2) {
      this.children.add(new CrashReportCategory.Entry(var1, var2));
   }

   public CrashReportCategory(CrashReport var1, String var2) {
      this.crashReport = var1;
      this.name = var2;
   }

   public void trimStackTraceEntriesFromBottom(int var1) {
      StackTraceElement[] var2 = new StackTraceElement[this.stackTrace.length - var1];
      System.arraycopy(this.stackTrace, 0, var2, 0, var2.length);
      this.stackTrace = var2;
   }

   static class Entry {
      private final String value;
      private final String key;

      public String getKey() {
         return this.key;
      }

      public String getValue() {
         return this.value;
      }

      public Entry(String var1, Object var2) {
         this.key = var1;
         if (var2 == null) {
            this.value = "~~NULL~~";
         } else if (var2 instanceof Throwable) {
            Throwable var3 = (Throwable)var2;
            this.value = "~~ERROR~~ " + var3.getClass().getSimpleName() + ": " + var3.getMessage();
         } else {
            this.value = var2.toString();
         }

      }
   }
}
