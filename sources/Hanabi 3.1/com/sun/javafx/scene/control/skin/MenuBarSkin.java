package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.control.GlobalMenuAdapter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import com.sun.javafx.stage.StageHelper;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MenuBarSkin extends BehaviorSkinBase implements TraverseListener {
   private final HBox container = new HBox();
   private Menu openMenu;
   private MenuBarButton openMenuButton;
   private int focusedMenuIndex = -1;
   private static WeakHashMap systemMenuMap;
   private static List wrappedDefaultMenus = new ArrayList();
   private static Stage currentMenuBarStage;
   private List wrappedMenus;
   private WeakEventHandler weakSceneKeyEventHandler;
   private WeakEventHandler weakSceneMouseEventHandler;
   private WeakChangeListener weakWindowFocusListener;
   private WeakChangeListener weakWindowSceneListener;
   private EventHandler keyEventHandler;
   private EventHandler mouseEventHandler;
   private ChangeListener menuBarFocusedPropertyListener;
   private ChangeListener sceneChangeListener;
   Runnable firstMenuRunnable = new Runnable() {
      public void run() {
         if (MenuBarSkin.this.container.getChildren().size() > 0 && MenuBarSkin.this.container.getChildren().get(0) instanceof MenuButton) {
            if (MenuBarSkin.this.focusedMenuIndex != 0) {
               MenuBarSkin.this.unSelectMenus();
               MenuBarSkin.this.menuModeStart(0);
               MenuBarSkin.this.openMenuButton = (MenuBarButton)MenuBarSkin.this.container.getChildren().get(0);
               MenuBarSkin.this.openMenu = (Menu)((MenuBar)MenuBarSkin.this.getSkinnable()).getMenus().get(0);
               MenuBarSkin.this.openMenuButton.setHover();
            } else {
               MenuBarSkin.this.unSelectMenus();
            }
         }

      }
   };
   private boolean pendingDismiss = false;
   private EventHandler menuActionEventHandler = (var1x) -> {
      if (var1x.getSource() instanceof CustomMenuItem) {
         CustomMenuItem var2 = (CustomMenuItem)var1x.getSource();
         if (!var2.isHideOnClick()) {
            return;
         }
      }

      this.unSelectMenus();
   };
   private ListChangeListener menuItemListener = (var1x) -> {
      while(var1x.next()) {
         Iterator var2 = var1x.getAddedSubList().iterator();

         MenuItem var3;
         while(var2.hasNext()) {
            var3 = (MenuItem)var2.next();
            var3.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
         }

         var2 = var1x.getRemoved().iterator();

         while(var2.hasNext()) {
            var3 = (MenuItem)var2.next();
            var3.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
         }
      }

   };
   private DoubleProperty spacing;
   private ObjectProperty containerAlignment;
   private static final CssMetaData SPACING = new CssMetaData("-fx-spacing", SizeConverter.getInstance(), 0.0) {
      public boolean isSettable(MenuBar var1) {
         MenuBarSkin var2 = (MenuBarSkin)var1.getSkin();
         return var2.spacing == null || !var2.spacing.isBound();
      }

      public StyleableProperty getStyleableProperty(MenuBar var1) {
         MenuBarSkin var2 = (MenuBarSkin)var1.getSkin();
         return (StyleableProperty)var2.spacingProperty();
      }
   };
   private static final CssMetaData ALIGNMENT;
   private static final List STYLEABLES;

   public static void setDefaultSystemMenuBar(MenuBar var0) {
      if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
         wrappedDefaultMenus.clear();
         Iterator var1 = var0.getMenus().iterator();

         while(var1.hasNext()) {
            Menu var2 = (Menu)var1.next();
            wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(var2));
         }

         var0.getMenus().addListener((var1x) -> {
            wrappedDefaultMenus.clear();
            Iterator var2 = var0.getMenus().iterator();

            while(var2.hasNext()) {
               Menu var3 = (Menu)var2.next();
               wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(var3));
            }

         });
      }

   }

   private static MenuBarSkin getMenuBarSkin(Stage var0) {
      if (systemMenuMap == null) {
         return null;
      } else {
         Reference var1 = (Reference)systemMenuMap.get(var0);
         return var1 == null ? null : (MenuBarSkin)var1.get();
      }
   }

   private static void setSystemMenu(Stage var0) {
      if (var0 != null && var0.isFocused()) {
         while(var0 != null && var0.getOwner() instanceof Stage) {
            MenuBarSkin var1 = getMenuBarSkin(var0);
            if (var1 != null && var1.wrappedMenus != null) {
               break;
            }

            var0 = (Stage)var0.getOwner();
         }
      } else {
         var0 = null;
      }

      if (var0 != currentMenuBarStage) {
         List var3 = null;
         if (var0 != null) {
            MenuBarSkin var2 = getMenuBarSkin(var0);
            if (var2 != null) {
               var3 = var2.wrappedMenus;
            }
         }

         if (var3 == null) {
            var3 = wrappedDefaultMenus;
         }

         Toolkit.getToolkit().getSystemMenu().setMenus(var3);
         currentMenuBarStage = var0;
      }

   }

   private static void initSystemMenuBar() {
      systemMenuMap = new WeakHashMap();
      InvalidationListener var0 = (var0x) -> {
         setSystemMenu((Stage)((ReadOnlyProperty)var0x).getBean());
      };
      ObservableList var1 = StageHelper.getStages();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Stage var3 = (Stage)var2.next();
         var3.focusedProperty().addListener(var0);
      }

      var1.addListener((var1x) -> {
         while(var1x.next()) {
            Iterator var2 = var1x.getRemoved().iterator();

            Stage var3;
            while(var2.hasNext()) {
               var3 = (Stage)var2.next();
               var3.focusedProperty().removeListener(var0);
            }

            var2 = var1x.getAddedSubList().iterator();

            while(var2.hasNext()) {
               var3 = (Stage)var2.next();
               var3.focusedProperty().addListener(var0);
               setSystemMenu(var3);
            }
         }

      });
   }

   EventHandler getKeyEventHandler() {
      return this.keyEventHandler;
   }

   public MenuBarSkin(MenuBar var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.container.getStyleClass().add("container");
      this.getChildren().add(this.container);
      this.keyEventHandler = (var2x) -> {
         if (this.openMenu != null) {
            boolean var3;
            switch (var2x.getCode()) {
               case LEFT:
                  var3 = var1.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                  if (var1.getScene().getWindow().isFocused()) {
                     if (this.openMenu == null) {
                        return;
                     }

                     if (!this.openMenu.isShowing()) {
                        if (var3) {
                           this.selectNextMenu();
                        } else {
                           this.selectPrevMenu();
                        }

                        var2x.consume();
                        return;
                     }

                     if (var3) {
                        this.showNextMenu();
                     } else {
                        this.showPrevMenu();
                     }
                  }

                  var2x.consume();
                  break;
               case RIGHT:
                  var3 = var1.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                  if (var1.getScene().getWindow().isFocused()) {
                     if (this.openMenu == null) {
                        return;
                     }

                     if (!this.openMenu.isShowing()) {
                        if (var3) {
                           this.selectPrevMenu();
                        } else {
                           this.selectNextMenu();
                        }

                        var2x.consume();
                        return;
                     }

                     if (var3) {
                        this.showPrevMenu();
                     } else {
                        this.showNextMenu();
                     }
                  }

                  var2x.consume();
                  break;
               case DOWN:
                  if (var1.getScene().getWindow().isFocused() && this.focusedMenuIndex != -1 && this.openMenu != null) {
                     this.openMenu = (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
                     if (!this.isMenuEmpty((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex))) {
                        this.openMenu.show();
                     }

                     var2x.consume();
                  }
                  break;
               case ESCAPE:
                  this.unSelectMenus();
                  var2x.consume();
            }
         }

      };
      this.menuBarFocusedPropertyListener = (var1x, var2x, var3x) -> {
         if (var3x) {
            this.unSelectMenus();
            this.menuModeStart(0);
            this.openMenuButton = (MenuBarButton)this.container.getChildren().get(0);
            this.openMenu = (Menu)((MenuBar)this.getSkinnable()).getMenus().get(0);
            this.openMenuButton.setHover();
         } else {
            this.unSelectMenus();
         }

      };
      this.weakSceneKeyEventHandler = new WeakEventHandler(this.keyEventHandler);
      Utils.executeOnceWhenPropertyIsNonNull(var1.sceneProperty(), (var1x) -> {
         var1x.addEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler);
      });
      this.mouseEventHandler = (var1x) -> {
         if (!this.container.localToScreen(this.container.getLayoutBounds()).contains(var1x.getScreenX(), var1x.getScreenY())) {
            this.unSelectMenus();
         }

      };
      this.weakSceneMouseEventHandler = new WeakEventHandler(this.mouseEventHandler);
      Utils.executeOnceWhenPropertyIsNonNull(var1.sceneProperty(), (var1x) -> {
         var1x.addEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler);
      });
      this.weakWindowFocusListener = new WeakChangeListener((var1x, var2x, var3x) -> {
         if (!var3x) {
            this.unSelectMenus();
         }

      });
      Utils.executeOnceWhenPropertyIsNonNull(var1.sceneProperty(), (var1x) -> {
         if (var1x.getWindow() != null) {
            var1x.getWindow().focusedProperty().addListener(this.weakWindowFocusListener);
         } else {
            ChangeListener var2 = (var1, var2x, var3) -> {
               if (var2x != null) {
                  var2x.focusedProperty().removeListener(this.weakWindowFocusListener);
               }

               if (var3 != null) {
                  var3.focusedProperty().addListener(this.weakWindowFocusListener);
               }

            };
            this.weakWindowSceneListener = new WeakChangeListener(var2);
            var1x.windowProperty().addListener(this.weakWindowSceneListener);
         }

      });
      this.rebuildUI();
      var1.getMenus().addListener((var1x) -> {
         this.rebuildUI();
      });
      Iterator var2 = ((MenuBar)this.getSkinnable()).getMenus().iterator();

      while(var2.hasNext()) {
         Menu var3 = (Menu)var2.next();
         var3.visibleProperty().addListener((var1x, var2x, var3x) -> {
            this.rebuildUI();
         });
      }

      if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
         var1.useSystemMenuBarProperty().addListener((var1x) -> {
            this.rebuildUI();
         });
      }

      KeyCombination var4;
      if (com.sun.javafx.util.Utils.isMac()) {
         var4 = KeyCombination.keyCombination("ctrl+F10");
      } else {
         var4 = KeyCombination.keyCombination("F10");
      }

      Utils.executeOnceWhenPropertyIsNonNull(var1.sceneProperty(), (var2x) -> {
         var2x.getAccelerators().put(var4, this.firstMenuRunnable);
         var2x.addEventHandler(KeyEvent.KEY_PRESSED, (var1) -> {
            if (var1.isAltDown() && !var1.isConsumed()) {
               this.firstMenuRunnable.run();
            }

         });
      });
      ParentTraversalEngine var5 = new ParentTraversalEngine(this.getSkinnable());
      var5.addTraverseListener(this);
      ((MenuBar)this.getSkinnable()).setImpl_traversalEngine(var5);
      var1.sceneProperty().addListener((var2x, var3x, var4x) -> {
         if (this.weakSceneKeyEventHandler != null && var3x != null) {
            var3x.removeEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler);
         }

         if (this.weakSceneMouseEventHandler != null && var3x != null) {
            var3x.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler);
         }

         if (var3x != null) {
            var3x.getAccelerators().remove(var4);
         }

         if (var4x != null) {
            var4x.getAccelerators().put(var4, this.firstMenuRunnable);
         }

      });
   }

   MenuButton getNodeForMenu(int var1) {
      return var1 < this.container.getChildren().size() ? (MenuBarButton)this.container.getChildren().get(var1) : null;
   }

   int getFocusedMenuIndex() {
      return this.focusedMenuIndex;
   }

   private boolean menusContainCustomMenuItem() {
      Iterator var1 = ((MenuBar)this.getSkinnable()).getMenus().iterator();

      Menu var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (Menu)var1.next();
      } while(!this.menuContainsCustomMenuItem(var2));

      System.err.println("Warning: MenuBar ignored property useSystemMenuBar because menus contain CustomMenuItem");
      return true;
   }

   private boolean menuContainsCustomMenuItem(Menu var1) {
      Iterator var2 = var1.getItems().iterator();

      MenuItem var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (MenuItem)var2.next();
         if (var3 instanceof CustomMenuItem && !(var3 instanceof SeparatorMenuItem)) {
            return true;
         }
      } while(!(var3 instanceof Menu) || !this.menuContainsCustomMenuItem((Menu)var3));

      return true;
   }

   private int getMenuBarButtonIndex(MenuBarButton var1) {
      for(int var2 = 0; var2 < this.container.getChildren().size(); ++var2) {
         MenuBarButton var3 = (MenuBarButton)this.container.getChildren().get(var2);
         if (var1 == var3) {
            return var2;
         }
      }

      return -1;
   }

   private void updateActionListeners(Menu var1, boolean var2) {
      if (var2) {
         var1.getItems().addListener(this.menuItemListener);
      } else {
         var1.getItems().removeListener(this.menuItemListener);
      }

      Iterator var3 = var1.getItems().iterator();

      while(var3.hasNext()) {
         MenuItem var4 = (MenuItem)var3.next();
         if (var4 instanceof Menu) {
            this.updateActionListeners((Menu)var4, var2);
         } else if (var2) {
            var4.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
         } else {
            var4.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
         }
      }

   }

   private void rebuildUI() {
      ((MenuBar)this.getSkinnable()).focusedProperty().removeListener(this.menuBarFocusedPropertyListener);
      Iterator var1 = ((MenuBar)this.getSkinnable()).getMenus().iterator();

      Menu var2;
      while(var1.hasNext()) {
         var2 = (Menu)var1.next();
         this.updateActionListeners(var2, false);
      }

      MenuBarButton var3;
      for(var1 = this.container.getChildren().iterator(); var1.hasNext(); var3 = null) {
         Node var5 = (Node)var1.next();
         var3 = (MenuBarButton)var5;
         var3.hide();
         var3.menu.showingProperty().removeListener(var3.menuListener);
         var3.disableProperty().unbind();
         var3.textProperty().unbind();
         var3.graphicProperty().unbind();
         var3.styleProperty().unbind();
         var3.dispose();
         var3.setSkin((Skin)null);
      }

      this.container.getChildren().clear();
      if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
         Scene var4 = ((MenuBar)this.getSkinnable()).getScene();
         if (var4 != null) {
            label62: {
               if (this.sceneChangeListener == null) {
                  this.sceneChangeListener = (var1x, var2x, var3x) -> {
                     Stage var4;
                     if (var2x != null && var2x.getWindow() instanceof Stage) {
                        var4 = (Stage)var2x.getWindow();
                        MenuBarSkin var5 = getMenuBarSkin(var4);
                        if (var5 == this) {
                           var5.wrappedMenus = null;
                           systemMenuMap.remove(var4);
                           if (currentMenuBarStage == var4) {
                              currentMenuBarStage = null;
                              setSystemMenu(var4);
                           }
                        }
                     }

                     if (var3x != null && ((MenuBar)this.getSkinnable()).isUseSystemMenuBar() && !this.menusContainCustomMenuItem() && var3x.getWindow() instanceof Stage) {
                        var4 = (Stage)var3x.getWindow();
                        if (systemMenuMap == null) {
                           initSystemMenuBar();
                        }

                        if (this.wrappedMenus == null) {
                           this.wrappedMenus = new ArrayList();
                           systemMenuMap.put(var4, new WeakReference(this));
                        } else {
                           this.wrappedMenus.clear();
                        }

                        Iterator var7 = ((MenuBar)this.getSkinnable()).getMenus().iterator();

                        while(var7.hasNext()) {
                           Menu var6 = (Menu)var7.next();
                           this.wrappedMenus.add(GlobalMenuAdapter.adapt(var6));
                        }

                        currentMenuBarStage = null;
                        setSystemMenu(var4);
                        ((MenuBar)this.getSkinnable()).requestLayout();
                        Platform.runLater(() -> {
                           ((MenuBar)this.getSkinnable()).requestLayout();
                        });
                     }

                  };
                  ((MenuBar)this.getSkinnable()).sceneProperty().addListener(this.sceneChangeListener);
               }

               this.sceneChangeListener.changed(((MenuBar)this.getSkinnable()).sceneProperty(), var4, var4);
               if (currentMenuBarStage != null) {
                  if (getMenuBarSkin(currentMenuBarStage) != this) {
                     break label62;
                  }
               } else if (!((MenuBar)this.getSkinnable()).isUseSystemMenuBar()) {
                  break label62;
               }

               return;
            }
         } else if (currentMenuBarStage != null) {
            MenuBarSkin var6 = getMenuBarSkin(currentMenuBarStage);
            if (var6 == this) {
               setSystemMenu((Stage)null);
            }
         }
      }

      ((MenuBar)this.getSkinnable()).focusedProperty().addListener(this.menuBarFocusedPropertyListener);
      var1 = ((MenuBar)this.getSkinnable()).getMenus().iterator();

      while(var1.hasNext()) {
         var2 = (Menu)var1.next();
         if (var2.isVisible()) {
            var3 = new MenuBarButton(this, var2);
            var3.setFocusTraversable(false);
            var3.getStyleClass().add("menu");
            var3.setStyle(var2.getStyle());
            var3.getItems().setAll((Collection)var2.getItems());
            this.container.getChildren().add(var3);
            var3.menuListener = (var3x, var4x, var5x) -> {
               if (var2.isShowing()) {
                  var3.show();
                  this.menuModeStart(this.container.getChildren().indexOf(var3));
               } else {
                  var3.hide();
               }

            };
            var3.menu = var2;
            var2.showingProperty().addListener(var3.menuListener);
            var3.disableProperty().bindBidirectional(var2.disableProperty());
            var3.textProperty().bind(var2.textProperty());
            var3.graphicProperty().bind(var2.graphicProperty());
            var3.styleProperty().bind(var2.styleProperty());
            var3.getProperties().addListener((var2x) -> {
               if (var2x.wasAdded() && "autoHide".equals(var2x.getKey())) {
                  var3.getProperties().remove("autoHide");
                  var2.hide();
               }

            });
            var3.showingProperty().addListener((var3x, var4x, var5x) -> {
               if (var5x) {
                  if (this.openMenuButton != null && this.openMenuButton != var3) {
                     this.openMenuButton.hide();
                  }

                  this.openMenuButton = var3;
                  this.openMenu = var2;
                  if (!var2.isShowing()) {
                     var2.show();
                  }
               }

            });
            var3.setOnMousePressed((var3x) -> {
               this.pendingDismiss = var3.isShowing();
               if (var3.getScene().getWindow().isFocused()) {
                  this.openMenu = var2;
                  if (!this.isMenuEmpty(var2)) {
                     this.openMenu.show();
                  }

                  this.menuModeStart(this.getMenuBarButtonIndex(var3));
               }

            });
            var3.setOnMouseReleased((var2x) -> {
               if (var3.getScene().getWindow().isFocused() && this.pendingDismiss) {
                  this.resetOpenMenu();
               }

               this.pendingDismiss = false;
            });
            var3.setOnMouseEntered((var3x) -> {
               if (var3.getScene() != null && var3.getScene().getWindow() != null && var3.getScene().getWindow().isFocused()) {
                  if (this.openMenuButton != null && this.openMenuButton != var3) {
                     this.openMenuButton.clearHover();
                     this.openMenuButton = null;
                     this.openMenuButton = var3;
                  }

                  this.updateFocusedIndex();
                  if (this.openMenu != null && this.openMenu != var2) {
                     this.openMenu.hide();
                     this.openMenu = var2;
                     this.updateFocusedIndex();
                     if (!this.isMenuEmpty(var2)) {
                        this.openMenu.show();
                     }
                  }
               }

            });
            this.updateActionListeners(var2, true);
         }
      }

      ((MenuBar)this.getSkinnable()).requestLayout();
   }

   public final void setSpacing(double var1) {
      this.spacingProperty().set(this.snapSpace(var1));
   }

   public final double getSpacing() {
      return this.spacing == null ? 0.0 : this.snapSpace(this.spacing.get());
   }

   public final DoubleProperty spacingProperty() {
      if (this.spacing == null) {
         this.spacing = new StyleableDoubleProperty() {
            protected void invalidated() {
               double var1 = this.get();
               MenuBarSkin.this.container.setSpacing(var1);
            }

            public Object getBean() {
               return MenuBarSkin.this;
            }

            public String getName() {
               return "spacing";
            }

            public CssMetaData getCssMetaData() {
               return MenuBarSkin.SPACING;
            }
         };
      }

      return this.spacing;
   }

   public final void setContainerAlignment(Pos var1) {
      this.containerAlignmentProperty().set(var1);
   }

   public final Pos getContainerAlignment() {
      return this.containerAlignment == null ? Pos.TOP_LEFT : (Pos)this.containerAlignment.get();
   }

   public final ObjectProperty containerAlignmentProperty() {
      if (this.containerAlignment == null) {
         this.containerAlignment = new StyleableObjectProperty(Pos.TOP_LEFT) {
            public void invalidated() {
               Pos var1 = (Pos)this.get();
               MenuBarSkin.this.container.setAlignment(var1);
            }

            public Object getBean() {
               return MenuBarSkin.this;
            }

            public String getName() {
               return "containerAlignment";
            }

            public CssMetaData getCssMetaData() {
               return MenuBarSkin.ALIGNMENT;
            }
         };
      }

      return this.containerAlignment;
   }

   public void dispose() {
      this.cleanUpSystemMenu();
      super.dispose();
   }

   private void cleanUpSystemMenu() {
      if (this.sceneChangeListener != null && this.getSkinnable() != null) {
         ((MenuBar)this.getSkinnable()).sceneProperty().removeListener(this.sceneChangeListener);
         this.sceneChangeListener = null;
      }

      if (currentMenuBarStage != null && getMenuBarSkin(currentMenuBarStage) == this) {
         setSystemMenu((Stage)null);
      }

      if (systemMenuMap != null) {
         Iterator var1 = systemMenuMap.entrySet().iterator();

         while(true) {
            MenuBarSkin var4;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               Map.Entry var2 = (Map.Entry)var1.next();
               Reference var3 = (Reference)var2.getValue();
               var4 = var3 != null ? (MenuBarSkin)var3.get() : null;
            } while(var4 != null && var4 != this);

            var1.remove();
         }
      }
   }

   private boolean isMenuEmpty(Menu var1) {
      boolean var2 = true;
      if (var1 != null) {
         Iterator var3 = var1.getItems().iterator();

         while(var3.hasNext()) {
            MenuItem var4 = (MenuItem)var3.next();
            if (var4 != null && var4.isVisible()) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   private void resetOpenMenu() {
      if (this.openMenu != null) {
         this.openMenu.hide();
         this.openMenu = null;
         this.openMenuButton = (MenuBarButton)this.container.getChildren().get(this.focusedMenuIndex);
         this.openMenuButton.clearHover();
         this.openMenuButton = null;
         this.menuModeEnd();
      }

   }

   private void unSelectMenus() {
      this.clearMenuButtonHover();
      if (this.focusedMenuIndex != -1) {
         if (this.openMenu != null) {
            this.openMenu.hide();
            this.openMenu = null;
         }

         if (this.openMenuButton != null) {
            this.openMenuButton.clearHover();
            this.openMenuButton = null;
         }

         this.menuModeEnd();
      }
   }

   private void menuModeStart(int var1) {
      if (this.focusedMenuIndex == -1) {
         SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar)this.getSkinnable()).getScene(), this.getSkinnable());
      }

      this.focusedMenuIndex = var1;
   }

   private void menuModeEnd() {
      if (this.focusedMenuIndex != -1) {
         SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar)this.getSkinnable()).getScene(), (Node)null);
         ((MenuBar)this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
      }

      this.focusedMenuIndex = -1;
   }

   private void selectNextMenu() {
      Menu var1 = this.findNextSibling();
      if (var1 != null && this.focusedMenuIndex != -1) {
         this.openMenuButton = (MenuBarButton)this.container.getChildren().get(this.focusedMenuIndex);
         this.openMenuButton.setHover();
         this.openMenu = var1;
      }

   }

   private void selectPrevMenu() {
      Menu var1 = this.findPreviousSibling();
      if (var1 != null && this.focusedMenuIndex != -1) {
         this.openMenuButton = (MenuBarButton)this.container.getChildren().get(this.focusedMenuIndex);
         this.openMenuButton.setHover();
         this.openMenu = var1;
      }

   }

   private void showNextMenu() {
      Menu var1 = this.findNextSibling();
      if (this.openMenu != null) {
         this.openMenu.hide();
      }

      this.openMenu = var1;
      if (!this.isMenuEmpty(var1)) {
         this.openMenu.show();
      }

   }

   private void showPrevMenu() {
      Menu var1 = this.findPreviousSibling();
      if (this.openMenu != null) {
         this.openMenu.hide();
      }

      this.openMenu = var1;
      if (!this.isMenuEmpty(var1)) {
         this.openMenu.show();
      }

   }

   private Menu findPreviousSibling() {
      if (this.focusedMenuIndex == -1) {
         return null;
      } else {
         if (this.focusedMenuIndex == 0) {
            this.focusedMenuIndex = this.container.getChildren().size() - 1;
         } else {
            --this.focusedMenuIndex;
         }

         if (((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex)).isDisable()) {
            return this.findPreviousSibling();
         } else {
            this.clearMenuButtonHover();
            return (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
         }
      }
   }

   private Menu findNextSibling() {
      if (this.focusedMenuIndex == -1) {
         return null;
      } else {
         if (this.focusedMenuIndex == this.container.getChildren().size() - 1) {
            this.focusedMenuIndex = 0;
         } else {
            ++this.focusedMenuIndex;
         }

         if (((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex)).isDisable()) {
            return this.findNextSibling();
         } else {
            this.clearMenuButtonHover();
            return (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
         }
      }
   }

   private void updateFocusedIndex() {
      int var1 = 0;

      for(Iterator var2 = this.container.getChildren().iterator(); var2.hasNext(); ++var1) {
         Node var3 = (Node)var2.next();
         if (var3.isHover()) {
            this.focusedMenuIndex = var1;
            return;
         }
      }

      this.menuModeEnd();
   }

   private void clearMenuButtonHover() {
      Iterator var1 = this.container.getChildren().iterator();

      Node var2;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (Node)var1.next();
      } while(!var2.isHover());

      ((MenuBarButton)var2).clearHover();
   }

   public void onTraverse(Node var1, Bounds var2) {
      if (this.openMenu != null) {
         this.openMenu.hide();
      }

      this.focusedMenuIndex = 0;
   }

   protected double snappedTopInset() {
      return this.container.getChildren().isEmpty() ? 0.0 : super.snappedTopInset();
   }

   protected double snappedBottomInset() {
      return this.container.getChildren().isEmpty() ? 0.0 : super.snappedBottomInset();
   }

   protected double snappedLeftInset() {
      return this.container.getChildren().isEmpty() ? 0.0 : super.snappedLeftInset();
   }

   protected double snappedRightInset() {
      return this.container.getChildren().isEmpty() ? 0.0 : super.snappedRightInset();
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.container.resizeRelocate(var1, var3, var5, var7);
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.container.minWidth(var1) + this.snappedLeftInset() + this.snappedRightInset();
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.container.prefWidth(var1) + this.snappedLeftInset() + this.snappedRightInset();
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.container.minHeight(var1) + this.snappedTopInset() + this.snappedBottomInset();
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.container.prefHeight(var1) + this.snappedTopInset() + this.snappedBottomInset();
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((MenuBar)this.getSkinnable()).prefHeight(-1.0);
   }

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case FOCUS_NODE:
            return this.openMenuButton;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static {
      ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) {
         public boolean isSettable(MenuBar var1) {
            MenuBarSkin var2 = (MenuBarSkin)var1.getSkin();
            return var2.containerAlignment == null || !var2.containerAlignment.isBound();
         }

         public StyleableProperty getStyleableProperty(MenuBar var1) {
            MenuBarSkin var2 = (MenuBarSkin)var1.getSkin();
            return (StyleableProperty)var2.containerAlignmentProperty();
         }
      };
      ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
      String var1 = ALIGNMENT.getProperty();
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         CssMetaData var4 = (CssMetaData)var0.get(var2);
         if (var1.equals(var4.getProperty())) {
            var0.remove(var4);
         }
      }

      var0.add(SPACING);
      var0.add(ALIGNMENT);
      STYLEABLES = Collections.unmodifiableList(var0);
   }

   static class MenuBarButton extends MenuButton {
      private ChangeListener menuListener;
      private MenuBarSkin menuBarSkin;
      private Menu menu;
      private final ListChangeListener itemsListener;
      private final ListChangeListener styleClassListener;

      public MenuBarButton(MenuBarSkin var1, Menu var2) {
         super(var2.getText(), var2.getGraphic());
         this.menuBarSkin = var1;
         this.setAccessibleRole(AccessibleRole.MENU);
         var2.getItems().addListener(this.itemsListener = (var1x) -> {
            while(var1x.next()) {
               this.getItems().removeAll(var1x.getRemoved());
               this.getItems().addAll(var1x.getFrom(), var1x.getAddedSubList());
            }

         });
         var2.getStyleClass().addListener(this.styleClassListener = (var2x) -> {
            while(var2x.next()) {
               for(int var3 = var2x.getFrom(); var3 < var2x.getTo(); ++var3) {
                  this.getStyleClass().add(var2.getStyleClass().get(var3));
               }

               Iterator var5 = var2x.getRemoved().iterator();

               while(var5.hasNext()) {
                  String var4 = (String)var5.next();
                  this.getStyleClass().remove(var4);
               }
            }

         });
         this.idProperty().bind(var2.idProperty());
      }

      public MenuBarSkin getMenuBarSkin() {
         return this.menuBarSkin;
      }

      private void clearHover() {
         this.setHover(false);
      }

      private void setHover() {
         this.setHover(true);
         ((MenuBar)this.menuBarSkin.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
      }

      void dispose() {
         this.menu.getItems().removeListener(this.itemsListener);
         this.menu.getStyleClass().removeListener(this.styleClassListener);
         this.idProperty().unbind();
      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         switch (var1) {
            case FOCUS_ITEM:
               return this;
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }
   }
}
