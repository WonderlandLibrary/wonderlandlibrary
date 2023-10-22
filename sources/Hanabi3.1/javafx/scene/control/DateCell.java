package javafx.scene.control;

import com.sun.javafx.scene.control.skin.DateCellSkin;
import java.time.LocalDate;

public class DateCell extends Cell {
   private static final String DEFAULT_STYLE_CLASS = "date-cell";

   public DateCell() {
      this.getStyleClass().add("date-cell");
   }

   public void updateItem(LocalDate var1, boolean var2) {
      super.updateItem(var1, var2);
   }

   protected Skin createDefaultSkin() {
      return new DateCellSkin(this);
   }
}
