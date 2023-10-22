package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class CustomMenuItem extends MenuItem {
   private ObjectProperty content;
   private BooleanProperty hideOnClick;
   private static final String DEFAULT_STYLE_CLASS = "custom-menu-item";

   public CustomMenuItem() {
      this((Node)null, true);
   }

   public CustomMenuItem(Node var1) {
      this(var1, true);
   }

   public CustomMenuItem(Node var1, boolean var2) {
      this.getStyleClass().add("custom-menu-item");
      this.setContent(var1);
      this.setHideOnClick(var2);
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

   public final void setHideOnClick(boolean var1) {
      this.hideOnClickProperty().set(var1);
   }

   public final boolean isHideOnClick() {
      return this.hideOnClick == null ? true : this.hideOnClick.get();
   }

   public final BooleanProperty hideOnClickProperty() {
      if (this.hideOnClick == null) {
         this.hideOnClick = new SimpleBooleanProperty(this, "hideOnClick", true);
      }

      return this.hideOnClick;
   }
}
