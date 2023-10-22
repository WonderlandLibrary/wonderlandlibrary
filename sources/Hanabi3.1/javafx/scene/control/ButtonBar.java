package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ButtonBarSkin;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.Node;

public class ButtonBar extends Control {
   public static final String BUTTON_ORDER_WINDOWS = "L_E+U+FBXI_YNOCAH_R";
   public static final String BUTTON_ORDER_MAC_OS = "L_HE+U+FBIX_NCYOA_R";
   public static final String BUTTON_ORDER_LINUX = "L_HE+UNYACBXIO_R";
   public static final String BUTTON_ORDER_NONE = "";
   private ObservableList buttons;
   private final StringProperty buttonOrderProperty;
   private final DoubleProperty buttonMinWidthProperty;

   public static void setButtonData(Node var0, ButtonData var1) {
      ObservableMap var2 = var0.getProperties();
      ObjectProperty var3 = (ObjectProperty)var2.getOrDefault("javafx.scene.control.ButtonBar.ButtonData", new SimpleObjectProperty(var0, "buttonData", var1));
      var3.set(var1);
      var2.putIfAbsent("javafx.scene.control.ButtonBar.ButtonData", var3);
   }

   public static ButtonData getButtonData(Node var0) {
      ObservableMap var1 = var0.getProperties();
      if (var1.containsKey("javafx.scene.control.ButtonBar.ButtonData")) {
         ObjectProperty var2 = (ObjectProperty)var1.get("javafx.scene.control.ButtonBar.ButtonData");
         return var2 == null ? null : (ButtonData)var2.get();
      } else {
         return null;
      }
   }

   public static void setButtonUniformSize(Node var0, boolean var1) {
      if (var1) {
         var0.getProperties().remove("javafx.scene.control.ButtonBar.independentSize");
      } else {
         var0.getProperties().put("javafx.scene.control.ButtonBar.independentSize", var1);
      }

   }

   public static boolean isButtonUniformSize(Node var0) {
      return (Boolean)var0.getProperties().getOrDefault("javafx.scene.control.ButtonBar.independentSize", true);
   }

   public ButtonBar() {
      this((String)null);
   }

   public ButtonBar(String var1) {
      this.buttons = FXCollections.observableArrayList();
      this.buttonOrderProperty = new SimpleStringProperty(this, "buttonOrder");
      this.buttonMinWidthProperty = new SimpleDoubleProperty(this, "buttonMinWidthProperty");
      this.getStyleClass().add("button-bar");
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      boolean var2 = var1 == null || var1.isEmpty();
      if (Utils.isMac()) {
         this.setButtonOrder(var2 ? "L_HE+U+FBIX_NCYOA_R" : var1);
         this.setButtonMinWidth(70.0);
      } else if (Utils.isUnix()) {
         this.setButtonOrder(var2 ? "L_HE+UNYACBXIO_R" : var1);
         this.setButtonMinWidth(85.0);
      } else {
         this.setButtonOrder(var2 ? "L_E+U+FBXI_YNOCAH_R" : var1);
         this.setButtonMinWidth(75.0);
      }

   }

   protected Skin createDefaultSkin() {
      return new ButtonBarSkin(this);
   }

   public final ObservableList getButtons() {
      return this.buttons;
   }

   public final StringProperty buttonOrderProperty() {
      return this.buttonOrderProperty;
   }

   public final void setButtonOrder(String var1) {
      this.buttonOrderProperty.set(var1);
   }

   public final String getButtonOrder() {
      return (String)this.buttonOrderProperty.get();
   }

   public final DoubleProperty buttonMinWidthProperty() {
      return this.buttonMinWidthProperty;
   }

   public final void setButtonMinWidth(double var1) {
      this.buttonMinWidthProperty.set(var1);
   }

   public final double getButtonMinWidth() {
      return this.buttonMinWidthProperty.get();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   public static enum ButtonData {
      LEFT("L", false, false),
      RIGHT("R", false, false),
      HELP("H", false, false),
      HELP_2("E", false, false),
      YES("Y", false, true),
      NO("N", true, false),
      NEXT_FORWARD("X", false, true),
      BACK_PREVIOUS("B", false, false),
      FINISH("I", false, true),
      APPLY("A", false, false),
      CANCEL_CLOSE("C", true, false),
      OK_DONE("O", false, true),
      OTHER("U", false, false),
      BIG_GAP("+", false, false),
      SMALL_GAP("_", false, false);

      private final String typeCode;
      private final boolean cancelButton;
      private final boolean defaultButton;

      private ButtonData(String var3, boolean var4, boolean var5) {
         this.typeCode = var3;
         this.cancelButton = var4;
         this.defaultButton = var5;
      }

      public String getTypeCode() {
         return this.typeCode;
      }

      public final boolean isCancelButton() {
         return this.cancelButton;
      }

      public final boolean isDefaultButton() {
         return this.defaultButton;
      }
   }
}
