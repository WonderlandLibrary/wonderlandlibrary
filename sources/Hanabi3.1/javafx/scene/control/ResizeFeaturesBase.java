package javafx.scene.control;

import javafx.beans.NamedArg;

public class ResizeFeaturesBase {
   private final TableColumnBase column;
   private final Double delta;

   public ResizeFeaturesBase(@NamedArg("column") TableColumnBase var1, @NamedArg("delta") Double var2) {
      this.column = var1;
      this.delta = var2;
   }

   public TableColumnBase getColumn() {
      return this.column;
   }

   public Double getDelta() {
      return this.delta;
   }
}
