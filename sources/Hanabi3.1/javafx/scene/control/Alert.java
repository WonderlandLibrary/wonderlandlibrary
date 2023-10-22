package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Alert extends Dialog {
   private WeakReference dialogPaneRef;
   private boolean installingDefaults;
   private boolean hasCustomButtons;
   private boolean hasCustomTitle;
   private boolean hasCustomHeaderText;
   private final InvalidationListener headerTextListener;
   private final InvalidationListener titleListener;
   private final ListChangeListener buttonsListener;
   private final ObjectProperty alertType;

   public Alert(@NamedArg("alertType") AlertType var1) {
      this(var1, "");
   }

   public Alert(@NamedArg("alertType") AlertType var1, @NamedArg("contentText") String var2, ButtonType... var3) {
      this.installingDefaults = false;
      this.hasCustomButtons = false;
      this.hasCustomTitle = false;
      this.hasCustomHeaderText = false;
      this.headerTextListener = (var1x) -> {
         if (!this.installingDefaults) {
            this.hasCustomHeaderText = true;
         }

      };
      this.titleListener = (var1x) -> {
         if (!this.installingDefaults) {
            this.hasCustomTitle = true;
         }

      };
      this.buttonsListener = (var1x) -> {
         if (!this.installingDefaults) {
            this.hasCustomButtons = true;
         }

      };
      this.alertType = new SimpleObjectProperty((AlertType)null) {
         final String[] styleClasses = new String[]{"information", "warning", "error", "confirmation"};

         protected void invalidated() {
            String var1 = "";
            String var2 = "";
            String var3 = "";
            ButtonType[] var4 = new ButtonType[]{ButtonType.OK};
            switch (Alert.this.getAlertType()) {
               case NONE:
                  var4 = new ButtonType[0];
                  break;
               case INFORMATION:
                  var1 = ControlResources.getString("Dialog.info.title");
                  var2 = ControlResources.getString("Dialog.info.header");
                  var3 = "information";
                  break;
               case WARNING:
                  var1 = ControlResources.getString("Dialog.warning.title");
                  var2 = ControlResources.getString("Dialog.warning.header");
                  var3 = "warning";
                  break;
               case ERROR:
                  var1 = ControlResources.getString("Dialog.error.title");
                  var2 = ControlResources.getString("Dialog.error.header");
                  var3 = "error";
                  break;
               case CONFIRMATION:
                  var1 = ControlResources.getString("Dialog.confirm.title");
                  var2 = ControlResources.getString("Dialog.confirm.header");
                  var3 = "confirmation";
                  var4 = new ButtonType[]{ButtonType.OK, ButtonType.CANCEL};
            }

            Alert.this.installingDefaults = true;
            if (!Alert.this.hasCustomTitle) {
               Alert.this.setTitle(var1);
            }

            if (!Alert.this.hasCustomHeaderText) {
               Alert.this.setHeaderText(var2);
            }

            if (!Alert.this.hasCustomButtons) {
               Alert.this.getButtonTypes().setAll((Object[])var4);
            }

            DialogPane var5 = Alert.this.getDialogPane();
            if (var5 != null) {
               ArrayList var6 = new ArrayList(Arrays.asList(this.styleClasses));
               var6.remove(var3);
               var5.getStyleClass().removeAll(var6);
               if (!var5.getStyleClass().contains(var3)) {
                  var5.getStyleClass().add(var3);
               }
            }

            Alert.this.installingDefaults = false;
         }
      };
      DialogPane var4 = this.getDialogPane();
      var4.setContentText(var2);
      this.getDialogPane().getStyleClass().add("alert");
      this.dialogPaneRef = new WeakReference(var4);
      this.hasCustomButtons = var3 != null && var3.length > 0;
      if (this.hasCustomButtons) {
         ButtonType[] var5 = var3;
         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ButtonType var8 = var5[var7];
            var4.getButtonTypes().addAll(var8);
         }
      }

      this.setAlertType(var1);
      this.dialogPaneProperty().addListener((var1x) -> {
         this.updateListeners();
      });
      this.titleProperty().addListener(this.titleListener);
      this.updateListeners();
   }

   public final AlertType getAlertType() {
      return (AlertType)this.alertType.get();
   }

   public final void setAlertType(AlertType var1) {
      this.alertType.setValue(var1);
   }

   public final ObjectProperty alertTypeProperty() {
      return this.alertType;
   }

   public final ObservableList getButtonTypes() {
      return this.getDialogPane().getButtonTypes();
   }

   private void updateListeners() {
      DialogPane var1 = (DialogPane)this.dialogPaneRef.get();
      if (var1 != null) {
         var1.headerTextProperty().removeListener(this.headerTextListener);
         var1.getButtonTypes().removeListener(this.buttonsListener);
      }

      DialogPane var2 = this.getDialogPane();
      if (var2 != null) {
         var2.headerTextProperty().addListener(this.headerTextListener);
         var2.getButtonTypes().addListener(this.buttonsListener);
      }

      this.dialogPaneRef = new WeakReference(var2);
   }

   public static enum AlertType {
      NONE,
      INFORMATION,
      WARNING,
      CONFIRMATION,
      ERROR;
   }
}
