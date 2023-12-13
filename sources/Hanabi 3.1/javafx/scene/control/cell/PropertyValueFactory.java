package javafx.scene.control.cell;

import com.sun.javafx.property.PropertyReference;
import com.sun.javafx.scene.control.Logging;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class PropertyValueFactory implements Callback {
   private final String property;
   private Class columnClass;
   private String previousProperty;
   private PropertyReference propertyRef;

   public PropertyValueFactory(@NamedArg("property") String var1) {
      this.property = var1;
   }

   public ObservableValue call(TableColumn.CellDataFeatures var1) {
      return this.getCellDataReflectively(var1.getValue());
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

            if (this.propertyRef.hasProperty()) {
               return this.propertyRef.getProperty(var1);
            } else {
               Object var2 = this.propertyRef.get(var1);
               return new ReadOnlyObjectWrapper(var2);
            }
         } catch (IllegalStateException var4) {
            PlatformLogger var3 = Logging.getControlsLogger();
            if (var3.isLoggable(Level.WARNING)) {
               var3.finest("Can not retrieve property '" + this.getProperty() + "' in PropertyValueFactory: " + this + " with provided class type: " + var1.getClass(), var4);
            }

            return null;
         }
      } else {
         return null;
      }
   }
}
