package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ToggleGroup {
   private final ObservableList toggles = new VetoableListDecorator(new TrackableObservableList() {
      protected void onChanged(ListChangeListener.Change var1) {
         label43:
         while(var1.next()) {
            Iterator var2 = var1.getRemoved().iterator();

            Toggle var3;
            while(var2.hasNext()) {
               var3 = (Toggle)var2.next();
               if (var3.isSelected()) {
                  ToggleGroup.this.selectToggle((Toggle)null);
               }
            }

            var2 = var1.getAddedSubList().iterator();

            while(var2.hasNext()) {
               var3 = (Toggle)var2.next();
               if (!ToggleGroup.this.equals(var3.getToggleGroup())) {
                  if (var3.getToggleGroup() != null) {
                     var3.getToggleGroup().getToggles().remove(var3);
                  }

                  var3.setToggleGroup(ToggleGroup.this);
               }
            }

            var2 = var1.getAddedSubList().iterator();

            do {
               if (!var2.hasNext()) {
                  continue label43;
               }

               var3 = (Toggle)var2.next();
            } while(!var3.isSelected());

            ToggleGroup.this.selectToggle(var3);
         }

      }
   }) {
      protected void onProposedChange(List var1, int... var2) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Toggle var4 = (Toggle)var3.next();
            if (var2[0] == 0 && var2[1] == this.size()) {
               break;
            }

            if (ToggleGroup.this.toggles.contains(var4)) {
               throw new IllegalArgumentException("Duplicate toggles are not allow in a ToggleGroup.");
            }
         }

      }
   };
   private final ReadOnlyObjectWrapper selectedToggle = new ReadOnlyObjectWrapper() {
      public void set(Toggle var1) {
         if (this.isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
         } else {
            Toggle var2 = (Toggle)this.get();
            if (var2 != var1) {
               if (ToggleGroup.this.setSelected(var1, true) || var1 != null && var1.getToggleGroup() == ToggleGroup.this || var1 == null) {
                  if (var2 == null || var2.getToggleGroup() == ToggleGroup.this || !var2.isSelected()) {
                     ToggleGroup.this.setSelected(var2, false);
                  }

                  super.set(var1);
               }

            }
         }
      }
   };
   private static final Object USER_DATA_KEY = new Object();
   private ObservableMap properties;

   public final ObservableList getToggles() {
      return this.toggles;
   }

   public final void selectToggle(Toggle var1) {
      this.selectedToggle.set(var1);
   }

   public final Toggle getSelectedToggle() {
      return (Toggle)this.selectedToggle.get();
   }

   public final ReadOnlyObjectProperty selectedToggleProperty() {
      return this.selectedToggle.getReadOnlyProperty();
   }

   private boolean setSelected(Toggle var1, boolean var2) {
      if (var1 != null && var1.getToggleGroup() == this && !var1.selectedProperty().isBound()) {
         var1.setSelected(var2);
         return true;
      } else {
         return false;
      }
   }

   final void clearSelectedToggle() {
      if (!((Toggle)this.selectedToggle.getValue()).isSelected()) {
         Iterator var1 = this.getToggles().iterator();

         while(var1.hasNext()) {
            Toggle var2 = (Toggle)var1.next();
            if (var2.isSelected()) {
               return;
            }
         }
      }

      this.selectedToggle.set((Object)null);
   }

   public final ObservableMap getProperties() {
      if (this.properties == null) {
         this.properties = FXCollections.observableMap(new HashMap());
      }

      return this.properties;
   }

   public boolean hasProperties() {
      return this.properties != null && !this.properties.isEmpty();
   }

   public void setUserData(Object var1) {
      this.getProperties().put(USER_DATA_KEY, var1);
   }

   public Object getUserData() {
      return this.getProperties().get(USER_DATA_KEY);
   }
}
