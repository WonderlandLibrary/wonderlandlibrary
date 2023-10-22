package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class ProgressIndicator extends Control {
   public static final double INDETERMINATE_PROGRESS = -1.0;
   private ReadOnlyBooleanWrapper indeterminate;
   private DoubleProperty progress;
   private static final String DEFAULT_STYLE_CLASS = "progress-indicator";
   private static final PseudoClass PSEUDO_CLASS_DETERMINATE = PseudoClass.getPseudoClass("determinate");
   private static final PseudoClass PSEUDO_CLASS_INDETERMINATE = PseudoClass.getPseudoClass("indeterminate");

   public ProgressIndicator() {
      this(-1.0);
   }

   public ProgressIndicator(double var1) {
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.setProgress(var1);
      this.getStyleClass().setAll((Object[])("progress-indicator"));
      this.setAccessibleRole(AccessibleRole.PROGRESS_INDICATOR);
      int var3 = Double.compare(-1.0, var1);
      this.pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, var3 == 0);
      this.pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, var3 != 0);
   }

   private void setIndeterminate(boolean var1) {
      this.indeterminatePropertyImpl().set(var1);
   }

   public final boolean isIndeterminate() {
      return this.indeterminate == null ? true : this.indeterminate.get();
   }

   public final ReadOnlyBooleanProperty indeterminateProperty() {
      return this.indeterminatePropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper indeterminatePropertyImpl() {
      if (this.indeterminate == null) {
         this.indeterminate = new ReadOnlyBooleanWrapper(true) {
            protected void invalidated() {
               boolean var1 = this.get();
               ProgressIndicator.this.pseudoClassStateChanged(ProgressIndicator.PSEUDO_CLASS_INDETERMINATE, var1);
               ProgressIndicator.this.pseudoClassStateChanged(ProgressIndicator.PSEUDO_CLASS_DETERMINATE, !var1);
            }

            public Object getBean() {
               return ProgressIndicator.this;
            }

            public String getName() {
               return "indeterminate";
            }
         };
      }

      return this.indeterminate;
   }

   public final void setProgress(double var1) {
      this.progressProperty().set(var1);
   }

   public final double getProgress() {
      return this.progress == null ? -1.0 : this.progress.get();
   }

   public final DoubleProperty progressProperty() {
      if (this.progress == null) {
         this.progress = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               ProgressIndicator.this.setIndeterminate(ProgressIndicator.this.getProgress() < 0.0);
            }

            public Object getBean() {
               return ProgressIndicator.this;
            }

            public String getName() {
               return "progress";
            }
         };
      }

      return this.progress;
   }

   protected Skin createDefaultSkin() {
      return new ProgressIndicatorSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case VALUE:
            return this.getProgress();
         case MAX_VALUE:
            return 1.0;
         case MIN_VALUE:
            return 0.0;
         case INDETERMINATE:
            return this.isIndeterminate();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
