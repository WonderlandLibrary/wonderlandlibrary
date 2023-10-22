package javafx.scene;

public final class SceneAntialiasing {
   public static final SceneAntialiasing DISABLED = new SceneAntialiasing("DISABLED");
   public static final SceneAntialiasing BALANCED = new SceneAntialiasing("BALANCED");
   private final String val;

   private SceneAntialiasing(String var1) {
      this.val = var1;
   }

   public String toString() {
      return this.val;
   }
}
