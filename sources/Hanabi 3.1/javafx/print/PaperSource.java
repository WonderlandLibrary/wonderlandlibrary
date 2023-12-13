package javafx.print;

public final class PaperSource {
   public static final PaperSource AUTOMATIC = new PaperSource("Automatic");
   public static final PaperSource MAIN = new PaperSource("Main");
   public static final PaperSource MANUAL = new PaperSource("Manual");
   public static final PaperSource BOTTOM = new PaperSource("Bottom");
   public static final PaperSource MIDDLE = new PaperSource("Middle");
   public static final PaperSource TOP = new PaperSource("Top");
   public static final PaperSource SIDE = new PaperSource("Side");
   public static final PaperSource ENVELOPE = new PaperSource("Envelope");
   public static final PaperSource LARGE_CAPACITY = new PaperSource("Large Capacity");
   private String name;

   PaperSource(String var1) {
      if (var1 != null) {
         this.name = var1;
      } else {
         this.name = "Unknown";
      }

   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return "Paper source : " + this.getName();
   }
}
