package com.sun.javafx.scene.control;

import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public class ControlAcceleratorSupport {
   public static void addAcceleratorsIntoScene(ObservableList var0, Tab var1) {
      addAcceleratorsIntoScene(var0, (Object)var1);
   }

   public static void addAcceleratorsIntoScene(ObservableList var0, TableColumnBase var1) {
      addAcceleratorsIntoScene(var0, (Object)var1);
   }

   public static void addAcceleratorsIntoScene(final ObservableList var0, final Node var1) {
      if (var0 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("Anchor cannot be null");
         } else {
            Scene var2 = var1.getScene();
            if (var2 == null) {
               var1.sceneProperty().addListener(new InvalidationListener() {
                  public void invalidated(Observable var1x) {
                     Scene var2 = var1.getScene();
                     if (var2 != null) {
                        var1.sceneProperty().removeListener(this);
                        ControlAcceleratorSupport.doAcceleratorInstall(var0, var2);
                     }

                  }
               });
            } else {
               doAcceleratorInstall(var0, var2);
            }

         }
      }
   }

   private static void addAcceleratorsIntoScene(final ObservableList var0, Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Anchor cannot be null");
      } else {
         final ReadOnlyObjectProperty var2 = getControlProperty(var1);
         Control var3 = (Control)var2.get();
         if (var3 == null) {
            var2.addListener(new InvalidationListener() {
               public void invalidated(Observable var1) {
                  Control var2x = (Control)var2.get();
                  if (var2x != null) {
                     var2.removeListener(this);
                     ControlAcceleratorSupport.addAcceleratorsIntoScene(var0, (Node)var2x);
                  }

               }
            });
         } else {
            addAcceleratorsIntoScene(var0, (Node)var3);
         }

      }
   }

   private static void doAcceleratorInstall(ObservableList var0, Scene var1) {
      var0.addListener((var1x) -> {
         while(var1x.next()) {
            if (var1x.wasRemoved()) {
               removeAcceleratorsFromScene(var1x.getRemoved(), var1);
            }

            if (var1x.wasAdded()) {
               doAcceleratorInstall(var1x.getAddedSubList(), var1);
            }
         }

      });
      doAcceleratorInstall((List)var0, var1);
   }

   private static void doAcceleratorInstall(List var0, Scene var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         MenuItem var3 = (MenuItem)var2.next();
         if (var3 instanceof Menu) {
            doAcceleratorInstall(((Menu)var3).getItems(), var1);
         } else {
            if (var3.getAccelerator() != null) {
               ObservableMap var4 = var1.getAccelerators();
               Runnable var5 = () -> {
                  if (var3.getOnMenuValidation() != null) {
                     Event.fireEvent(var3, new Event(MenuItem.MENU_VALIDATION_EVENT));
                  }

                  Menu var1 = var3.getParentMenu();
                  if (var1 != null && var1.getOnMenuValidation() != null) {
                     Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
                  }

                  if (!var3.isDisable()) {
                     if (var3 instanceof RadioMenuItem) {
                        ((RadioMenuItem)var3).setSelected(!((RadioMenuItem)var3).isSelected());
                     } else if (var3 instanceof CheckMenuItem) {
                        ((CheckMenuItem)var3).setSelected(!((CheckMenuItem)var3).isSelected());
                     }

                     var3.fire();
                  }

               };
               var4.put(var3.getAccelerator(), var5);
            }

            var3.acceleratorProperty().addListener((var1x, var2x, var3x) -> {
               ObservableMap var4 = var1.getAccelerators();
               Runnable var5 = (Runnable)var4.remove(var2x);
               if (var3x != null) {
                  var4.put(var3x, var5);
               }

            });
         }
      }

   }

   public static void removeAcceleratorsFromScene(List var0, Tab var1) {
      TabPane var2 = var1.getTabPane();
      if (var2 != null) {
         Scene var3 = var2.getScene();
         removeAcceleratorsFromScene(var0, var3);
      }
   }

   public static void removeAcceleratorsFromScene(List var0, TableColumnBase var1) {
      ReadOnlyObjectProperty var2 = getControlProperty(var1);
      if (var2 != null) {
         Control var3 = (Control)var2.get();
         if (var3 != null) {
            Scene var4 = var3.getScene();
            removeAcceleratorsFromScene(var0, var4);
         }
      }
   }

   public static void removeAcceleratorsFromScene(List var0, Node var1) {
      Scene var2 = var1.getScene();
      removeAcceleratorsFromScene(var0, var2);
   }

   public static void removeAcceleratorsFromScene(List var0, Scene var1) {
      if (var1 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            MenuItem var3 = (MenuItem)var2.next();
            if (var3 instanceof Menu) {
               removeAcceleratorsFromScene(((Menu)var3).getItems(), (Scene)var1);
            } else {
               ObservableMap var4 = var1.getAccelerators();
               var4.remove(var3.getAccelerator());
            }
         }

      }
   }

   private static ReadOnlyObjectProperty getControlProperty(Object var0) {
      if (var0 instanceof TableColumn) {
         return ((TableColumn)var0).tableViewProperty();
      } else if (var0 instanceof TreeTableColumn) {
         return ((TreeTableColumn)var0).treeTableViewProperty();
      } else {
         return var0 instanceof Tab ? ((Tab)var0).tabPaneProperty() : null;
      }
   }
}
