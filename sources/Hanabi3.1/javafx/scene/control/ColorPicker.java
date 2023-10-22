package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ColorPicker extends ComboBoxBase {
   public static final String STYLE_CLASS_BUTTON = "button";
   public static final String STYLE_CLASS_SPLIT_BUTTON = "split-button";
   private ObservableList customColors;
   private static final String DEFAULT_STYLE_CLASS = "color-picker";

   public final ObservableList getCustomColors() {
      return this.customColors;
   }

   public ColorPicker() {
      this(Color.WHITE);
   }

   public ColorPicker(Color var1) {
      this.customColors = FXCollections.observableArrayList();
      this.setValue(var1);
      this.getStyleClass().add("color-picker");
   }

   protected Skin createDefaultSkin() {
      return new ColorPickerSkin(this);
   }
}
