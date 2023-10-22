package javafx.print;

public final class PrintResolution {
   private int cfRes;
   private int fRes;

   PrintResolution(int var1, int var2) throws IllegalArgumentException {
      if (var1 > 0 && var2 > 0) {
         this.cfRes = var1;
         this.fRes = var2;
      } else {
         throw new IllegalArgumentException("Values must be positive");
      }
   }

   public int getCrossFeedResolution() {
      return this.cfRes;
   }

   public int getFeedResolution() {
      return this.fRes;
   }

   public boolean equals(Object var1) {
      try {
         PrintResolution var2 = (PrintResolution)var1;
         return this.cfRes == var2.cfRes && this.fRes == var2.fRes;
      } catch (Exception var3) {
         return false;
      }
   }

   public int hashCode() {
      return this.cfRes << 16 | this.fRes;
   }

   public String toString() {
      return "Feed res=" + this.fRes + "dpi. Cross Feed res=" + this.cfRes + "dpi.";
   }
}
