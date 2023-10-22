package javafx.scene.media;

import javafx.event.ActionEvent;
import javafx.util.Pair;

public class MediaMarkerEvent extends ActionEvent {
   private static final long serialVersionUID = 20121107L;
   private Pair marker;

   MediaMarkerEvent(Pair var1) {
      this.marker = var1;
   }

   public Pair getMarker() {
      return this.marker;
   }
}
