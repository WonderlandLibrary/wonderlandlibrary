package javafx.scene.media;

import java.util.Map;

public final class VideoTrack extends Track {
   private int width;
   private int height;

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   VideoTrack(long var1, Map var3) {
      super(var1, var3);
      Object var4 = var3.get("video width");
      if (null != var4 && var4 instanceof Number) {
         this.width = ((Number)var4).intValue();
      }

      var4 = var3.get("video height");
      if (null != var4 && var4 instanceof Number) {
         this.height = ((Number)var4).intValue();
      }

   }
}
