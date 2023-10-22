package javafx.geometry;

public enum Pos {
   TOP_LEFT(VPos.TOP, HPos.LEFT),
   TOP_CENTER(VPos.TOP, HPos.CENTER),
   TOP_RIGHT(VPos.TOP, HPos.RIGHT),
   CENTER_LEFT(VPos.CENTER, HPos.LEFT),
   CENTER(VPos.CENTER, HPos.CENTER),
   CENTER_RIGHT(VPos.CENTER, HPos.RIGHT),
   BOTTOM_LEFT(VPos.BOTTOM, HPos.LEFT),
   BOTTOM_CENTER(VPos.BOTTOM, HPos.CENTER),
   BOTTOM_RIGHT(VPos.BOTTOM, HPos.RIGHT),
   BASELINE_LEFT(VPos.BASELINE, HPos.LEFT),
   BASELINE_CENTER(VPos.BASELINE, HPos.CENTER),
   BASELINE_RIGHT(VPos.BASELINE, HPos.RIGHT);

   private final VPos vpos;
   private final HPos hpos;

   private Pos(VPos var3, HPos var4) {
      this.vpos = var3;
      this.hpos = var4;
   }

   public VPos getVpos() {
      return this.vpos;
   }

   public HPos getHpos() {
      return this.hpos;
   }
}
