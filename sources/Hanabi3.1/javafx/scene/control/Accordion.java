package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.AccordionSkin;
import java.util.Iterator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public class Accordion extends Control {
   private final ObservableList panes;
   private ObjectProperty expandedPane;
   private static final String DEFAULT_STYLE_CLASS = "accordion";

   public Accordion() {
      this((TitledPane[])null);
   }

   public Accordion(TitledPane... var1) {
      this.panes = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            label27:
            while(var1.next()) {
               if (var1.wasRemoved() && !Accordion.this.expandedPane.isBound()) {
                  Iterator var2 = var1.getRemoved().iterator();

                  TitledPane var3;
                  do {
                     if (!var2.hasNext()) {
                        continue label27;
                     }

                     var3 = (TitledPane)var2.next();
                  } while(var1.getAddedSubList().contains(var3) || Accordion.this.getExpandedPane() != var3);

                  Accordion.this.setExpandedPane((TitledPane)null);
               }
            }

         }
      };
      this.expandedPane = new ObjectPropertyBase() {
         private TitledPane oldValue;

         protected void invalidated() {
            TitledPane var1 = (TitledPane)this.get();
            if (var1 != null) {
               var1.setExpanded(true);
            } else if (this.oldValue != null) {
               this.oldValue.setExpanded(false);
            }

            this.oldValue = var1;
         }

         public String getName() {
            return "expandedPane";
         }

         public Object getBean() {
            return Accordion.this;
         }
      };
      this.getStyleClass().setAll((Object[])("accordion"));
      if (var1 != null) {
         this.getPanes().addAll(var1);
      }

      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
   }

   public final void setExpandedPane(TitledPane var1) {
      this.expandedPaneProperty().set(var1);
   }

   public final TitledPane getExpandedPane() {
      return (TitledPane)this.expandedPane.get();
   }

   public final ObjectProperty expandedPaneProperty() {
      return this.expandedPane;
   }

   public final ObservableList getPanes() {
      return this.panes;
   }

   protected Skin createDefaultSkin() {
      return new AccordionSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }
}
