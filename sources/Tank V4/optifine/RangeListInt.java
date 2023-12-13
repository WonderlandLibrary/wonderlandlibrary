package optifine;

public class RangeListInt {
   private RangeInt[] ranges = new RangeInt[0];

   public void addRange(RangeInt var1) {
      this.ranges = (RangeInt[])Config.addObjectToArray(this.ranges, var1);
   }

   public boolean isInRange(int var1) {
      for(int var2 = 0; var2 < this.ranges.length; ++var2) {
         RangeInt var3 = this.ranges[var2];
         if (var3.isInRange(var1)) {
            return true;
         }
      }

      return false;
   }

   public int getCountRanges() {
      return this.ranges.length;
   }

   public RangeInt getRange(int var1) {
      return this.ranges[var1];
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");

      for(int var2 = 0; var2 < this.ranges.length; ++var2) {
         RangeInt var3 = this.ranges[var2];
         if (var2 > 0) {
            var1.append(", ");
         }

         var1.append(var3.toString());
      }

      var1.append("]");
      return var1.toString();
   }
}
