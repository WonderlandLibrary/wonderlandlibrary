package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;

public class ProgressBar extends ProgressIndicator {
   private static final String DEFAULT_STYLE_CLASS = "progress-bar";

   public ProgressBar() {
      this(-1.0);
   }

   public ProgressBar(double var1) {
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.setProgress(var1);
      this.getStyleClass().setAll((Object[])("progress-bar"));
   }

   protected Skin createDefaultSkin() {
      return new ProgressBarSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case ORIENTATION:
            return Orientation.HORIZONTAL;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
