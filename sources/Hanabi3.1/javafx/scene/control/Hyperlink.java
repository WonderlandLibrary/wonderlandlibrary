package javafx.scene.control;

import com.sun.javafx.scene.control.skin.HyperlinkSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.Node;

public class Hyperlink extends ButtonBase {
   private BooleanProperty visited;
   private static final String DEFAULT_STYLE_CLASS = "hyperlink";
   private static final PseudoClass PSEUDO_CLASS_VISITED = PseudoClass.getPseudoClass("visited");

   public Hyperlink() {
      this.initialize();
   }

   public Hyperlink(String var1) {
      super(var1);
      this.initialize();
   }

   public Hyperlink(String var1, Node var2) {
      super(var1, var2);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("hyperlink"));
      this.setAccessibleRole(AccessibleRole.HYPERLINK);
      ((StyleableProperty)this.cursorProperty()).applyStyle((StyleOrigin)null, Cursor.HAND);
   }

   public final BooleanProperty visitedProperty() {
      if (this.visited == null) {
         this.visited = new BooleanPropertyBase() {
            protected void invalidated() {
               Hyperlink.this.pseudoClassStateChanged(Hyperlink.PSEUDO_CLASS_VISITED, this.get());
            }

            public Object getBean() {
               return Hyperlink.this;
            }

            public String getName() {
               return "visited";
            }
         };
      }

      return this.visited;
   }

   public final void setVisited(boolean var1) {
      this.visitedProperty().set(var1);
   }

   public final boolean isVisited() {
      return this.visited == null ? false : this.visited.get();
   }

   public void fire() {
      if (!this.isDisabled()) {
         if (this.visited == null || !this.visited.isBound()) {
            this.setVisited(true);
         }

         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new HyperlinkSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Cursor impl_cssGetCursorInitialValue() {
      return Cursor.HAND;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case VISITED:
            return this.isVisited();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
