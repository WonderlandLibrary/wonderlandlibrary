package javafx.scene.control;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

class HeavyweightDialog extends FXDialog {
   final Stage stage = new Stage() {
      public void centerOnScreen() {
         Window var1 = this.getOwner();
         if (var1 != null) {
            HeavyweightDialog.this.positionStage();
         } else if (this.getWidth() > 0.0 && this.getHeight() > 0.0) {
            super.centerOnScreen();
         }

      }
   };
   private Scene scene;
   private final Parent DUMMY_ROOT = new Region();
   private final Dialog dialog;
   private DialogPane dialogPane;
   private double prefX = Double.NaN;
   private double prefY = Double.NaN;

   HeavyweightDialog(Dialog var1) {
      this.dialog = var1;
      this.stage.setResizable(false);
      this.stage.setOnCloseRequest((var2) -> {
         if (this.requestPermissionToClose(var1)) {
            var1.close();
         } else {
            var2.consume();
         }

      });
      this.stage.addEventHandler(KeyEvent.KEY_PRESSED, (var2) -> {
         if (var2.getCode() == KeyCode.ESCAPE && !var2.isConsumed() && this.requestPermissionToClose(var1)) {
            var1.close();
            var2.consume();
         }

      });
   }

   void initStyle(StageStyle var1) {
      this.stage.initStyle(var1);
   }

   StageStyle getStyle() {
      return this.stage.getStyle();
   }

   public void initOwner(Window var1) {
      this.updateStageBindings(this.stage.getOwner(), var1);
      this.stage.initOwner(var1);
   }

   public Window getOwner() {
      return this.stage.getOwner();
   }

   public void initModality(Modality var1) {
      this.stage.initModality(var1 == null ? Modality.APPLICATION_MODAL : var1);
   }

   public Modality getModality() {
      return this.stage.getModality();
   }

   public void setDialogPane(DialogPane var1) {
      this.dialogPane = var1;
      if (this.scene == null) {
         this.scene = new Scene(var1);
         this.stage.setScene(this.scene);
      } else {
         this.scene.setRoot(var1);
      }

      var1.autosize();
      this.stage.sizeToScene();
   }

   public void show() {
      this.scene.setRoot(this.dialogPane);
      this.stage.centerOnScreen();
      this.stage.show();
   }

   public void showAndWait() {
      this.scene.setRoot(this.dialogPane);
      this.stage.centerOnScreen();
      this.stage.showAndWait();
   }

   public void close() {
      if (this.stage.isShowing()) {
         this.stage.hide();
      }

      if (this.scene != null) {
         this.scene.setRoot(this.DUMMY_ROOT);
      }

   }

   public ReadOnlyBooleanProperty showingProperty() {
      return this.stage.showingProperty();
   }

   public Window getWindow() {
      return this.stage;
   }

   public Node getRoot() {
      return this.stage.getScene().getRoot();
   }

   public double getX() {
      return this.stage.getX();
   }

   public void setX(double var1) {
      this.stage.setX(var1);
   }

   public ReadOnlyDoubleProperty xProperty() {
      return this.stage.xProperty();
   }

   public double getY() {
      return this.stage.getY();
   }

   public void setY(double var1) {
      this.stage.setY(var1);
   }

   public ReadOnlyDoubleProperty yProperty() {
      return this.stage.yProperty();
   }

   ReadOnlyDoubleProperty heightProperty() {
      return this.stage.heightProperty();
   }

   void setHeight(double var1) {
      this.stage.setHeight(var1);
   }

   double getSceneHeight() {
      return this.scene == null ? 0.0 : this.scene.getHeight();
   }

   ReadOnlyDoubleProperty widthProperty() {
      return this.stage.widthProperty();
   }

   void setWidth(double var1) {
      this.stage.setWidth(var1);
   }

   BooleanProperty resizableProperty() {
      return this.stage.resizableProperty();
   }

   StringProperty titleProperty() {
      return this.stage.titleProperty();
   }

   ReadOnlyBooleanProperty focusedProperty() {
      return this.stage.focusedProperty();
   }

   public void sizeToScene() {
      this.stage.sizeToScene();
   }

   private void positionStage() {
      double var1 = this.getX();
      double var3 = this.getY();
      if (!Double.isNaN(var1) && !Double.isNaN(var3) && Double.compare(var1, this.prefX) != 0 && Double.compare(var3, this.prefY) != 0) {
         this.setX(var1);
         this.setY(var3);
      } else {
         this.dialogPane.applyCss();
         this.dialogPane.layout();
         Window var5 = this.getOwner();
         Scene var6 = var5.getScene();
         double var7 = var6.getY();
         double var9 = this.dialogPane.prefWidth(-1.0);
         double var11 = this.dialogPane.prefHeight(var9);
         var1 = var5.getX() + var6.getWidth() / 2.0 - var9 / 2.0;
         var3 = var5.getY() + var7 / 2.0 + var6.getHeight() / 2.0 - var11 / 2.0;
         this.prefX = var1;
         this.prefY = var3;
         this.setX(var1);
         this.setY(var3);
      }
   }

   private void updateStageBindings(Window var1, Window var2) {
      Scene var3 = this.stage.getScene();
      Stage var4;
      Scene var5;
      if (var1 != null && var1 instanceof Stage) {
         var4 = (Stage)var1;
         Bindings.unbindContent(this.stage.getIcons(), var4.getIcons());
         var5 = var4.getScene();
         if (this.scene != null && var3 != null) {
            Bindings.unbindContent(var3.getStylesheets(), var5.getStylesheets());
         }
      }

      if (var2 instanceof Stage) {
         var4 = (Stage)var2;
         Bindings.bindContent((List)this.stage.getIcons(), (ObservableList)var4.getIcons());
         var5 = var4.getScene();
         if (this.scene != null && var3 != null) {
            Bindings.bindContent((List)var3.getStylesheets(), (ObservableList)var5.getStylesheets());
         }
      }

   }
}
