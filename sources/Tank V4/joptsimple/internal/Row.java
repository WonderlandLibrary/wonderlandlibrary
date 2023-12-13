package joptsimple.internal;

class Row {
   final String option;
   final String description;

   Row(String var1, String var2) {
      this.option = var1;
      this.description = var2;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && this.getClass().equals(var1.getClass())) {
         Row var2 = (Row)var1;
         return this.option.equals(var2.option) && this.description.equals(var2.description);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.option.hashCode() ^ this.description.hashCode();
   }
}
