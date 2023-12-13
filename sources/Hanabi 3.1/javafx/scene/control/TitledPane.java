package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

@DefaultProperty("content")
public class TitledPane extends Labeled {
   private ObjectProperty content;
   private BooleanProperty expanded;
   private BooleanProperty animated;
   private BooleanProperty collapsible;
   private static final String DEFAULT_STYLE_CLASS = "titled-pane";
   private static final PseudoClass PSEUDO_CLASS_EXPANDED = PseudoClass.getPseudoClass("expanded");
   private static final PseudoClass PSEUDO_CLASS_COLLAPSED = PseudoClass.getPseudoClass("collapsed");

   public TitledPane() {
      this.expanded = new BooleanPropertyBase(true) {
         protected void invalidated() {
            boolean var1 = this.get();
            TitledPane.this.pseudoClassStateChanged(TitledPane.PSEUDO_CLASS_EXPANDED, var1);
            TitledPane.this.pseudoClassStateChanged(TitledPane.PSEUDO_CLASS_COLLAPSED, !var1);
            TitledPane.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
         }

         public Object getBean() {
            return TitledPane.this;
         }

         public String getName() {
            return "expanded";
         }
      };
      this.animated = new StyleableBooleanProperty(true) {
         public Object getBean() {
            return TitledPane.this;
         }

         public String getName() {
            return "animated";
         }

         public CssMetaData getCssMetaData() {
            return TitledPane.StyleableProperties.ANIMATED;
         }
      };
      this.collapsible = new StyleableBooleanProperty(true) {
         public Object getBean() {
            return TitledPane.this;
         }

         public String getName() {
            return "collapsible";
         }

         public CssMetaData getCssMetaData() {
            return TitledPane.StyleableProperties.COLLAPSIBLE;
         }
      };
      this.getStyleClass().setAll((Object[])("titled-pane"));
      this.setAccessibleRole(AccessibleRole.TITLED_PANE);
      this.pseudoClassStateChanged(PSEUDO_CLASS_EXPANDED, true);
   }

   public TitledPane(String var1, Node var2) {
      this();
      this.setText(var1);
      this.setContent(var2);
   }

   public final void setContent(Node var1) {
      this.contentProperty().set(var1);
   }

   public final Node getContent() {
      return this.content == null ? null : (Node)this.content.get();
   }

   public final ObjectProperty contentProperty() {
      if (this.content == null) {
         this.content = new SimpleObjectProperty(this, "content");
      }

      return this.content;
   }

   public final void setExpanded(boolean var1) {
      this.expandedProperty().set(var1);
   }

   public final boolean isExpanded() {
      return this.expanded.get();
   }

   public final BooleanProperty expandedProperty() {
      return this.expanded;
   }

   public final void setAnimated(boolean var1) {
      this.animatedProperty().set(var1);
   }

   public final boolean isAnimated() {
      return this.animated.get();
   }

   public final BooleanProperty animatedProperty() {
      return this.animated;
   }

   public final void setCollapsible(boolean var1) {
      this.collapsibleProperty().set(var1);
   }

   public final boolean isCollapsible() {
      return this.collapsible.get();
   }

   public final BooleanProperty collapsibleProperty() {
      return this.collapsible;
   }

   protected Skin createDefaultSkin() {
      return new TitledPaneSkin(this);
   }

   public static List getClassCssMetaData() {
      return TitledPane.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Orientation getContentBias() {
      Node var1 = this.getContent();
      return var1 == null ? super.getContentBias() : var1.getContentBias();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            String var3 = this.getAccessibleText();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            }

            return this.getText();
         case EXPANDED:
            return this.isExpanded();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case EXPAND:
            this.setExpanded(true);
            break;
         case COLLAPSE:
            this.setExpanded(false);
            break;
         default:
            super.executeAccessibleAction(var1, new Object[0]);
      }

   }

   private static class StyleableProperties {
      private static final CssMetaData COLLAPSIBLE;
      private static final CssMetaData ANIMATED;
      private static final List STYLEABLES;

      static {
         COLLAPSIBLE = new CssMetaData("-fx-collapsible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(TitledPane var1) {
               return var1.collapsible == null || !var1.collapsible.isBound();
            }

            public StyleableProperty getStyleableProperty(TitledPane var1) {
               return (StyleableProperty)var1.collapsibleProperty();
            }
         };
         ANIMATED = new CssMetaData("-fx-animated", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(TitledPane var1) {
               return var1.animated == null || !var1.animated.isBound();
            }

            public StyleableProperty getStyleableProperty(TitledPane var1) {
               return (StyleableProperty)var1.animatedProperty();
            }
         };
         ArrayList var0 = new ArrayList(Labeled.getClassCssMetaData());
         var0.add(COLLAPSIBLE);
         var0.add(ANIMATED);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
