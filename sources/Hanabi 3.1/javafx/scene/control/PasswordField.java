package javafx.scene.control;

import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class PasswordField extends TextField {
   public PasswordField() {
      this.getStyleClass().add("password-field");
      this.setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
   }

   public void cut() {
   }

   public void copy() {
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            return null;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
