package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TextInputDialog extends Dialog {
   private final GridPane grid;
   private final Label label;
   private final TextField textField;
   private final String defaultValue;

   public TextInputDialog() {
      this("");
   }

   public TextInputDialog(@NamedArg("defaultValue") String var1) {
      DialogPane var2 = this.getDialogPane();
      this.textField = new TextField(var1);
      this.textField.setMaxWidth(Double.MAX_VALUE);
      GridPane.setHgrow(this.textField, Priority.ALWAYS);
      GridPane.setFillWidth(this.textField, true);
      this.label = DialogPane.createContentLabel(var2.getContentText());
      this.label.setPrefWidth(-1.0);
      this.label.textProperty().bind(var2.contentTextProperty());
      this.defaultValue = var1;
      this.grid = new GridPane();
      this.grid.setHgap(10.0);
      this.grid.setMaxWidth(Double.MAX_VALUE);
      this.grid.setAlignment(Pos.CENTER_LEFT);
      var2.contentTextProperty().addListener((var1x) -> {
         this.updateGrid();
      });
      this.setTitle(ControlResources.getString("Dialog.confirm.title"));
      var2.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
      var2.getStyleClass().add("text-input-dialog");
      var2.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
      this.updateGrid();
      this.setResultConverter((var1x) -> {
         ButtonBar.ButtonData var2 = var1x == null ? null : var1x.getButtonData();
         return var2 == ButtonBar.ButtonData.OK_DONE ? this.textField.getText() : null;
      });
   }

   public final TextField getEditor() {
      return this.textField;
   }

   public final String getDefaultValue() {
      return this.defaultValue;
   }

   private void updateGrid() {
      this.grid.getChildren().clear();
      this.grid.add(this.label, 0, 0);
      this.grid.add(this.textField, 1, 0);
      this.getDialogPane().setContent(this.grid);
      Platform.runLater(() -> {
         this.textField.requestFocus();
      });
   }
}
