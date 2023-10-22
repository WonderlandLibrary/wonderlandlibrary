package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProgressBarTableCell extends TableCell {
   private final ProgressBar progressBar;
   private ObservableValue observable;

   public static Callback forTableColumn() {
      return (var0) -> {
         return new ProgressBarTableCell();
      };
   }

   public ProgressBarTableCell() {
      this.getStyleClass().add("progress-bar-table-cell");
      this.progressBar = new ProgressBar();
      this.progressBar.setMaxWidth(Double.MAX_VALUE);
   }

   public void updateItem(Double var1, boolean var2) {
      super.updateItem(var1, var2);
      if (var2) {
         this.setGraphic((Node)null);
      } else {
         this.progressBar.progressProperty().unbind();
         TableColumn var3 = this.getTableColumn();
         this.observable = var3 == null ? null : var3.getCellObservableValue(this.getIndex());
         if (this.observable != null) {
            this.progressBar.progressProperty().bind(this.observable);
         } else if (var1 != null) {
            this.progressBar.setProgress(var1);
         }

         this.setGraphic(this.progressBar);
      }

   }
}
