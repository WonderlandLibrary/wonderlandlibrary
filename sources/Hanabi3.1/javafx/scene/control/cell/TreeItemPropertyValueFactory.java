package javafx.scene.control.cell;

import com.sun.javafx.property.PropertyReference;
import com.sun.javafx.scene.control.Logging;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class TreeItemPropertyValueFactory implements Callback {
   private final String property;
   private Class columnClass;
   private String previousProperty;
   private PropertyReference propertyRef;

   public TreeItemPropertyValueFactory(@NamedArg("property") String var1) {
      this.property = var1;
   }

   public ObservableValue call(TreeTableColumn.CellDataFeatures var1) {
      TreeItem var2 = var1.getValue();
      return this.getCellDataReflectively(var2.getValue());
   }

   public final String getProperty() {
      return this.property;
   }

   private ObservableValue getCellDataReflectively(Object var1) {
      if (this.getProperty() != null && !this.getProperty().isEmpty() && var1 != null) {
         try {
            if (this.columnClass == null || this.previousProperty == null || !this.columnClass.equals(var1.getClass()) || !this.previousProperty.equals(this.getProperty())) {
               this.columnClass = var1.getClass();
               this.previousProperty = this.getProperty();
               this.propertyRef = new PropertyReference(var1.getClass(), this.getProperty());
            }

            return this.propertyRef.getProperty(var1);
         } catch (IllegalStateException var5) {
            try {
               Object var6 = this.propertyRef.get(var1);
               return new ReadOnlyObjectWrapper(var6);
            } catch (IllegalStateException var4) {
               PlatformLogger var3 = Logging.getControlsLogger();
               if (var3.isLoggable(Level.WARNING)) {
                  var3.finest("Can not retrieve property '" + this.getProperty() + "' in TreeItemPropertyValueFactory: " + this + " with provided class type: " + var1.getClass(), var5);
               }

               return null;
            }
         }
      } else {
         return null;
      }
   }
}
