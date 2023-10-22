package javafx.scene;

import javafx.scene.image.WritableImage;

public class SnapshotResult {
   private WritableImage image;
   private Object source;
   private SnapshotParameters params;

   SnapshotResult(WritableImage var1, Object var2, SnapshotParameters var3) {
      this.image = var1;
      this.source = var2;
      this.params = var3;
   }

   public WritableImage getImage() {
      return this.image;
   }

   public Object getSource() {
      return this.source;
   }

   public SnapshotParameters getSnapshotParameters() {
      return this.params;
   }
}
