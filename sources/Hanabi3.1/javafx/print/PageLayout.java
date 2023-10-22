package javafx.print;

public final class PageLayout {
   private PageOrientation orient;
   private Paper paper;
   private double lMargin;
   private double rMargin;
   private double tMargin;
   private double bMargin;

   PageLayout(Paper var1, PageOrientation var2) {
      this(var1, var2, 56.0, 56.0, 56.0, 56.0);
   }

   PageLayout(Paper var1, PageOrientation var2, double var3, double var5, double var7, double var9) {
      if (var1 != null && var2 != null && !(var3 < 0.0) && !(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0)) {
         if (var2 != PageOrientation.PORTRAIT && var2 != PageOrientation.REVERSE_PORTRAIT) {
            if (var3 + var5 > var1.getHeight() || var7 + var9 > var1.getWidth()) {
               throw new IllegalArgumentException("Bad margins");
            }
         } else if (var3 + var5 > var1.getWidth() || var7 + var9 > var1.getHeight()) {
            throw new IllegalArgumentException("Bad margins");
         }

         this.paper = var1;
         this.orient = var2;
         this.lMargin = var3;
         this.rMargin = var5;
         this.tMargin = var7;
         this.bMargin = var9;
      } else {
         throw new IllegalArgumentException("Illegal parameters");
      }
   }

   public PageOrientation getPageOrientation() {
      return this.orient;
   }

   public Paper getPaper() {
      return this.paper;
   }

   public double getPrintableWidth() {
      double var1 = 0.0;
      if (this.orient != PageOrientation.PORTRAIT && this.orient != PageOrientation.REVERSE_PORTRAIT) {
         var1 = this.paper.getHeight();
      } else {
         var1 = this.paper.getWidth();
      }

      var1 -= this.lMargin + this.rMargin;
      if (var1 < 0.0) {
         var1 = 0.0;
      }

      return var1;
   }

   public double getPrintableHeight() {
      double var1 = 0.0;
      if (this.orient != PageOrientation.PORTRAIT && this.orient != PageOrientation.REVERSE_PORTRAIT) {
         var1 = this.paper.getWidth();
      } else {
         var1 = this.paper.getHeight();
      }

      var1 -= this.tMargin + this.bMargin;
      if (var1 < 0.0) {
         var1 = 0.0;
      }

      return var1;
   }

   public double getLeftMargin() {
      return this.lMargin;
   }

   public double getRightMargin() {
      return this.rMargin;
   }

   public double getTopMargin() {
      return this.tMargin;
   }

   public double getBottomMargin() {
      return this.bMargin;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PageLayout)) {
         return false;
      } else {
         PageLayout var2 = (PageLayout)var1;
         return this.paper.equals(var2.paper) && this.orient.equals(var2.orient) && this.tMargin == var2.tMargin && this.bMargin == var2.bMargin && this.rMargin == var2.rMargin && this.lMargin == var2.lMargin;
      }
   }

   public int hashCode() {
      return this.paper.hashCode() + this.orient.hashCode() + (int)(this.tMargin + this.bMargin + this.lMargin + this.rMargin);
   }

   public String toString() {
      return "Paper=" + this.paper + " Orient=" + this.orient + " leftMargin=" + this.lMargin + " rightMargin=" + this.rMargin + " topMargin=" + this.tMargin + " bottomMargin=" + this.bMargin;
   }
}
