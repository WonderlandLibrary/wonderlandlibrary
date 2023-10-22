package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SeparatorSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;

public class Separator extends Control {
   private ObjectProperty orientation;
   private ObjectProperty halignment;
   private ObjectProperty valignment;
   private static final String DEFAULT_STYLE_CLASS = "separator";
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public Separator() {
      this(Orientation.HORIZONTAL);
   }

   public Separator(Orientation var1) {
      this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) {
         protected void invalidated() {
            boolean var1 = this.get() == Orientation.VERTICAL;
            Separator.this.pseudoClassStateChanged(Separator.VERTICAL_PSEUDOCLASS_STATE, var1);
            Separator.this.pseudoClassStateChanged(Separator.HORIZONTAL_PSEUDOCLASS_STATE, !var1);
         }

         public CssMetaData getCssMetaData() {
            return Separator.StyleableProperties.ORIENTATION;
         }

         public Object getBean() {
            return Separator.this;
         }

         public String getName() {
            return "orientation";
         }
      };
      this.getStyleClass().setAll((Object[])("separator"));
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, var1 != Orientation.VERTICAL);
      this.pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, var1 == Orientation.VERTICAL);
      ((StyleableProperty)this.orientationProperty()).applyStyle((StyleOrigin)null, var1 != null ? var1 : Orientation.HORIZONTAL);
   }

   public final void setOrientation(Orientation var1) {
      this.orientation.set(var1);
   }

   public final Orientation getOrientation() {
      return (Orientation)this.orientation.get();
   }

   public final ObjectProperty orientationProperty() {
      return this.orientation;
   }

   public final void setHalignment(HPos var1) {
      this.halignmentProperty().set(var1);
   }

   public final HPos getHalignment() {
      return this.halignment == null ? HPos.CENTER : (HPos)this.halignment.get();
   }

   public final ObjectProperty halignmentProperty() {
      if (this.halignment == null) {
         this.halignment = new StyleableObjectProperty(HPos.CENTER) {
            public Object getBean() {
               return Separator.this;
            }

            public String getName() {
               return "halignment";
            }

            public CssMetaData getCssMetaData() {
               return Separator.StyleableProperties.HALIGNMENT;
            }
         };
      }

      return this.halignment;
   }

   public final void setValignment(VPos var1) {
      this.valignmentProperty().set(var1);
   }

   public final VPos getValignment() {
      return this.valignment == null ? VPos.CENTER : (VPos)this.valignment.get();
   }

   public final ObjectProperty valignmentProperty() {
      if (this.valignment == null) {
         this.valignment = new StyleableObjectProperty(VPos.CENTER) {
            public Object getBean() {
               return Separator.this;
            }

            public String getName() {
               return "valignment";
            }

            public CssMetaData getCssMetaData() {
               return Separator.StyleableProperties.VALIGNMENT;
            }
         };
      }

      return this.valignment;
   }

   protected Skin createDefaultSkin() {
      return new SeparatorSkin(this);
   }

   public static List getClassCssMetaData() {
      return Separator.StyleableProperties.STYLEABLES;
   }

   /** @deprecated */
   @Deprecated
   protected List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   private static class StyleableProperties {
      private static final CssMetaData ORIENTATION;
      private static final CssMetaData HALIGNMENT;
      private static final CssMetaData VALIGNMENT;
      private static final List STYLEABLES;

      static {
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(Separator var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(Separator var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(Separator var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         HALIGNMENT = new CssMetaData("-fx-halignment", new EnumConverter(HPos.class), HPos.CENTER) {
            public boolean isSettable(Separator var1) {
               return var1.halignment == null || !var1.halignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Separator var1) {
               return (StyleableProperty)var1.halignmentProperty();
            }
         };
         VALIGNMENT = new CssMetaData("-fx-valignment", new EnumConverter(VPos.class), VPos.CENTER) {
            public boolean isSettable(Separator var1) {
               return var1.valignment == null || !var1.valignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Separator var1) {
               return (StyleableProperty)var1.valignmentProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(ORIENTATION);
         var0.add(HALIGNMENT);
         var0.add(VALIGNMENT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
