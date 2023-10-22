package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.ToolBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

@DefaultProperty("items")
public class ToolBar extends Control {
   private final ObservableList items = FXCollections.observableArrayList();
   private ObjectProperty orientation;
   private static final String DEFAULT_STYLE_CLASS = "tool-bar";
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public ToolBar() {
      this.initialize();
   }

   public ToolBar(Node... var1) {
      this.initialize();
      this.items.addAll(var1);
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("tool-bar"));
      this.setAccessibleRole(AccessibleRole.TOOL_BAR);
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
   }

   public final ObservableList getItems() {
      return this.items;
   }

   public final void setOrientation(Orientation var1) {
      this.orientationProperty().set(var1);
   }

   public final Orientation getOrientation() {
      return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
   }

   public final ObjectProperty orientationProperty() {
      if (this.orientation == null) {
         this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) {
            public void invalidated() {
               boolean var1 = this.get() == Orientation.VERTICAL;
               ToolBar.this.pseudoClassStateChanged(ToolBar.VERTICAL_PSEUDOCLASS_STATE, var1);
               ToolBar.this.pseudoClassStateChanged(ToolBar.HORIZONTAL_PSEUDOCLASS_STATE, !var1);
            }

            public Object getBean() {
               return ToolBar.this;
            }

            public String getName() {
               return "orientation";
            }

            public CssMetaData getCssMetaData() {
               return ToolBar.StyleableProperties.ORIENTATION;
            }
         };
      }

      return this.orientation;
   }

   protected Skin createDefaultSkin() {
      return new ToolBarSkin(this);
   }

   public static List getClassCssMetaData() {
      return ToolBar.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   private static class StyleableProperties {
      private static final CssMetaData ORIENTATION;
      private static final List STYLEABLES;

      static {
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(ToolBar var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(ToolBar var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(ToolBar var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(ORIENTATION);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
