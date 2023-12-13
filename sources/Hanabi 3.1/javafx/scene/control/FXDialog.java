package javafx.scene.control;

import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

abstract class FXDialog {
   protected Object owner;

   protected FXDialog() {
   }

   public boolean requestPermissionToClose(Dialog var1) {
      boolean var2 = true;
      DialogPane var3 = var1.getDialogPane();
      if (var3 != null) {
         ObservableList var4 = var3.getButtonTypes();
         if (var4.size() == 1) {
            var2 = false;
         } else {
            Iterator var5 = var4.iterator();

            ButtonBar.ButtonData var7;
            do {
               do {
                  ButtonType var6;
                  do {
                     if (!var5.hasNext()) {
                        return !var2;
                     }

                     var6 = (ButtonType)var5.next();
                  } while(var6 == null);

                  var7 = var6.getButtonData();
               } while(var7 == null);
            } while(var7 != ButtonBar.ButtonData.CANCEL_CLOSE && !var7.isCancelButton());

            var2 = false;
         }
      }

      return !var2;
   }

   public abstract void show();

   public abstract void showAndWait();

   public abstract void close();

   public abstract void initOwner(Window var1);

   public abstract Window getOwner();

   public abstract void initModality(Modality var1);

   public abstract Modality getModality();

   public abstract ReadOnlyBooleanProperty showingProperty();

   public abstract Window getWindow();

   public abstract void sizeToScene();

   public abstract double getX();

   public abstract void setX(double var1);

   public abstract ReadOnlyDoubleProperty xProperty();

   public abstract double getY();

   public abstract void setY(double var1);

   public abstract ReadOnlyDoubleProperty yProperty();

   abstract BooleanProperty resizableProperty();

   abstract ReadOnlyBooleanProperty focusedProperty();

   abstract StringProperty titleProperty();

   public abstract void setDialogPane(DialogPane var1);

   public abstract Node getRoot();

   abstract ReadOnlyDoubleProperty widthProperty();

   abstract void setWidth(double var1);

   abstract ReadOnlyDoubleProperty heightProperty();

   abstract void setHeight(double var1);

   abstract void initStyle(StageStyle var1);

   abstract StageStyle getStyle();

   abstract double getSceneHeight();
}
