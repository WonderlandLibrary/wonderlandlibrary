package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.Logging;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Region;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public abstract class Control extends Region implements Skinnable {
   private List styleableProperties;
   private SkinBase skinBase;
   private static final EventHandler contextMenuHandler;
   private ObjectProperty skin = new StyleableObjectProperty() {
      private Skin oldValue;

      public void set(Skin var1) {
         if (var1 == null) {
            if (this.oldValue == null) {
               return;
            }
         } else if (this.oldValue != null && var1.getClass().equals(this.oldValue.getClass())) {
            return;
         }

         super.set(var1);
      }

      protected void invalidated() {
         Skin var1 = (Skin)this.get();
         Control.this.currentSkinClassName = var1 == null ? null : var1.getClass().getName();
         Control.this.skinClassNameProperty().set(Control.this.currentSkinClassName);
         if (this.oldValue != null) {
            this.oldValue.dispose();
         }

         this.oldValue = var1;
         Control.this.skinBase = null;
         if (var1 instanceof SkinBase) {
            Control.this.skinBase = (SkinBase)var1;
         } else {
            Node var2 = Control.this.getSkinNode();
            if (var2 != null) {
               Control.this.getChildren().setAll((Object[])(var2));
            } else {
               Control.this.getChildren().clear();
            }
         }

         Control.this.styleableProperties = null;
         Control.this.impl_reapplyCSS();
         PlatformLogger var3 = Logging.getControlsLogger();
         if (var3.isLoggable(Level.FINEST)) {
            var3.finest("Stored skin[" + this.getValue() + "] on " + this);
         }

      }

      public CssMetaData getCssMetaData() {
         return Control.StyleableProperties.SKIN;
      }

      public Object getBean() {
         return Control.this;
      }

      public String getName() {
         return "skin";
      }
   };
   private ObjectProperty tooltip;
   private ObjectProperty contextMenu = new SimpleObjectProperty(this, "contextMenu") {
      private WeakReference contextMenuRef;

      protected void invalidated() {
         ContextMenu var1 = this.contextMenuRef == null ? null : (ContextMenu)this.contextMenuRef.get();
         if (var1 != null) {
            ControlAcceleratorSupport.removeAcceleratorsFromScene(var1.getItems(), (Node)Control.this);
         }

         ContextMenu var2 = (ContextMenu)this.get();
         this.contextMenuRef = new WeakReference(var2);
         if (var2 != null) {
            var2.setImpl_showRelativeToWindow(true);
            ControlAcceleratorSupport.addAcceleratorsIntoScene(var2.getItems(), (Node)Control.this);
         }

      }
   };
   private String currentSkinClassName = null;
   private StringProperty skinClassName;

   private static Class loadClass(String var0, Object var1) throws ClassNotFoundException {
      try {
         return Class.forName(var0, false, Control.class.getClassLoader());
      } catch (ClassNotFoundException var7) {
         if (Thread.currentThread().getContextClassLoader() != null) {
            try {
               ClassLoader var8 = Thread.currentThread().getContextClassLoader();
               return Class.forName(var0, false, var8);
            } catch (ClassNotFoundException var6) {
            }
         }

         if (var1 != null) {
            Class var3 = var1.getClass();

            while(var3 != null) {
               try {
                  ClassLoader var4 = var3.getClassLoader();
                  return Class.forName(var0, false, var4);
               } catch (ClassNotFoundException var5) {
                  var3 = var3.getSuperclass();
               }
            }
         }

         throw var7;
      }
   }

   public final ObjectProperty skinProperty() {
      return this.skin;
   }

   public final void setSkin(Skin var1) {
      this.skinProperty().set(var1);
   }

   public final Skin getSkin() {
      return (Skin)this.skinProperty().getValue();
   }

   public final ObjectProperty tooltipProperty() {
      if (this.tooltip == null) {
         this.tooltip = new ObjectPropertyBase() {
            private Tooltip old = null;

            protected void invalidated() {
               Tooltip var1 = (Tooltip)this.get();
               if (var1 != this.old) {
                  if (this.old != null) {
                     Tooltip.uninstall(Control.this, this.old);
                  }

                  if (var1 != null) {
                     Tooltip.install(Control.this, var1);
                  }

                  this.old = var1;
               }

            }

            public Object getBean() {
               return Control.this;
            }

            public String getName() {
               return "tooltip";
            }
         };
      }

      return this.tooltip;
   }

   public final void setTooltip(Tooltip var1) {
      this.tooltipProperty().setValue(var1);
   }

   public final Tooltip getTooltip() {
      return this.tooltip == null ? null : (Tooltip)this.tooltip.getValue();
   }

   public final ObjectProperty contextMenuProperty() {
      return this.contextMenu;
   }

   public final void setContextMenu(ContextMenu var1) {
      this.contextMenu.setValue(var1);
   }

   public final ContextMenu getContextMenu() {
      return this.contextMenu == null ? null : (ContextMenu)this.contextMenu.getValue();
   }

   protected Control() {
      StyleableProperty var1 = (StyleableProperty)this.focusTraversableProperty();
      var1.applyStyle((StyleOrigin)null, Boolean.TRUE);
      this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, contextMenuHandler);
   }

   public boolean isResizable() {
      return true;
   }

   protected double computeMinWidth(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computeMinWidth(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.minWidth(var1);
      }
   }

   protected double computeMinHeight(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computeMinHeight(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.minHeight(var1);
      }
   }

   protected double computeMaxWidth(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computeMaxWidth(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.maxWidth(var1);
      }
   }

   protected double computeMaxHeight(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computeMaxHeight(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.maxHeight(var1);
      }
   }

   protected double computePrefWidth(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computePrefWidth(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.prefWidth(var1);
      }
   }

   protected double computePrefHeight(double var1) {
      if (this.skinBase != null) {
         return this.skinBase.computePrefHeight(var1, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var3 = this.getSkinNode();
         return var3 == null ? 0.0 : var3.prefHeight(var1);
      }
   }

   public double getBaselineOffset() {
      if (this.skinBase != null) {
         return this.skinBase.computeBaselineOffset(this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
      } else {
         Node var1 = this.getSkinNode();
         return var1 == null ? 0.0 : var1.getBaselineOffset();
      }
   }

   protected void layoutChildren() {
      if (this.skinBase != null) {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedTopInset();
         double var5 = this.snapSize(this.getWidth()) - var1 - this.snappedRightInset();
         double var7 = this.snapSize(this.getHeight()) - var3 - this.snappedBottomInset();
         this.skinBase.layoutChildren(var1, var3, var5, var7);
      } else {
         Node var9 = this.getSkinNode();
         if (var9 != null) {
            var9.resizeRelocate(0.0, 0.0, this.getWidth(), this.getHeight());
         }
      }

   }

   protected Skin createDefaultSkin() {
      return null;
   }

   ObservableList getControlChildren() {
      return this.getChildren();
   }

   private Node getSkinNode() {
      assert this.skinBase == null;

      Skin var1 = this.getSkin();
      return var1 == null ? null : var1.getNode();
   }

   /** @deprecated */
   @Deprecated
   protected StringProperty skinClassNameProperty() {
      if (this.skinClassName == null) {
         this.skinClassName = new StyleableStringProperty() {
            public void set(String var1) {
               if (var1 != null && !var1.isEmpty() && !var1.equals(this.get())) {
                  super.set(var1);
               }
            }

            public void invalidated() {
               if (this.get() != null && !this.get().equals(Control.this.currentSkinClassName)) {
                  Control.loadSkinClass(Control.this, (String)Control.this.skinClassName.get());
               }

            }

            public Object getBean() {
               return Control.this;
            }

            public String getName() {
               return "skinClassName";
            }

            public CssMetaData getCssMetaData() {
               return Control.StyleableProperties.SKIN;
            }
         };
      }

      return this.skinClassName;
   }

   static void loadSkinClass(Skinnable var0, String var1) {
      if (var1 != null && !var1.isEmpty()) {
         CssError var5;
         String var13;
         ObservableList var15;
         try {
            Class var12 = loadClass(var1, var0);
            if (!Skin.class.isAssignableFrom(var12)) {
               var13 = "'" + var1 + "' is not a valid Skin class for control " + var0;
               var15 = StyleManager.getErrors();
               if (var15 != null) {
                  var5 = new CssError(var13);
                  var15.add(var5);
               }

               Logging.getControlsLogger().severe(var13);
               return;
            }

            Constructor[] var14 = var12.getConstructors();
            Constructor var16 = null;
            Constructor[] var17 = var14;
            int var6 = var14.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Constructor var8 = var17[var7];
               Class[] var9 = var8.getParameterTypes();
               if (var9.length == 1 && Skinnable.class.isAssignableFrom(var9[0])) {
                  var16 = var8;
                  break;
               }
            }

            if (var16 == null) {
               String var18 = "No valid constructor defined in '" + var1 + "' for control " + var0 + ".\r\nYou must provide a constructor that accepts a single Skinnable (e.g. Control or PopupControl) parameter in " + var1 + ".";
               ObservableList var20 = StyleManager.getErrors();
               if (var20 != null) {
                  CssError var21 = new CssError(var18);
                  var20.add(var21);
               }

               Logging.getControlsLogger().severe(var18);
            } else {
               Skin var19 = (Skin)var16.newInstance(var0);
               var0.skinProperty().set(var19);
            }
         } catch (InvocationTargetException var10) {
            var13 = "Failed to load skin '" + var1 + "' for control " + var0;
            var15 = StyleManager.getErrors();
            if (var15 != null) {
               var5 = new CssError(var13 + " :" + var10.getLocalizedMessage());
               var15.add(var5);
            }

            Logging.getControlsLogger().severe(var13, var10.getCause());
         } catch (Exception var11) {
            var13 = "Failed to load skin '" + var1 + "' for control " + var0;
            var15 = StyleManager.getErrors();
            if (var15 != null) {
               var5 = new CssError(var13 + " :" + var11.getLocalizedMessage());
               var15.add(var5);
            }

            Logging.getControlsLogger().severe(var13, var11);
         }

      } else {
         String var2 = "Empty -fx-skin property specified for control " + var0;
         ObservableList var3 = StyleManager.getErrors();
         if (var3 != null) {
            CssError var4 = new CssError(var2);
            var3.add(var4);
         }

         Logging.getControlsLogger().severe(var2);
      }
   }

   public static List getClassCssMetaData() {
      return Control.StyleableProperties.STYLEABLES;
   }

   public final List getCssMetaData() {
      if (this.styleableProperties == null) {
         HashMap var1 = new HashMap();
         List var2 = this.getControlCssMetaData();
         int var3 = 0;

         int var4;
         CssMetaData var5;
         for(var4 = var2 != null ? var2.size() : 0; var3 < var4; ++var3) {
            var5 = (CssMetaData)var2.get(var3);
            if (var5 != null) {
               var1.put(var5.getProperty(), var5);
            }
         }

         var2 = this.skinBase != null ? this.skinBase.getCssMetaData() : null;
         var3 = 0;

         for(var4 = var2 != null ? var2.size() : 0; var3 < var4; ++var3) {
            var5 = (CssMetaData)var2.get(var3);
            if (var5 != null) {
               var1.put(var5.getProperty(), var5);
            }
         }

         this.styleableProperties = new ArrayList();
         this.styleableProperties.addAll(var1.values());
      }

      return this.styleableProperties;
   }

   protected List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected void impl_processCSS(WritableValue var1) {
      super.impl_processCSS(var1);
      if (this.getSkin() == null) {
         Skin var2 = this.createDefaultSkin();
         if (var2 != null) {
            this.skinProperty().set(var2);
            super.impl_processCSS(var1);
         } else {
            String var3 = "The -fx-skin property has not been defined in CSS for " + this + " and createDefaultSkin() returned null.";
            ObservableList var4 = StyleManager.getErrors();
            if (var4 != null) {
               CssError var5 = new CssError(var3);
               var4.add(var5);
            }

            Logging.getControlsLogger().severe(var3);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.TRUE;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case HELP:
            String var3 = this.getAccessibleHelp();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            }

            Tooltip var4 = this.getTooltip();
            return var4 == null ? "" : var4.getText();
         default:
            if (this.skinBase != null) {
               Object var5 = this.skinBase.queryAccessibleAttribute(var1, var2);
               if (var5 != null) {
                  return var5;
               }
            }

            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      if (this.skinBase != null) {
         this.skinBase.executeAccessibleAction(var1, var2);
      }

      super.executeAccessibleAction(var1, var2);
   }

   static {
      if (Application.getUserAgentStylesheet() == null) {
         PlatformImpl.setDefaultPlatformUserAgentStylesheet();
      }

      contextMenuHandler = (var0) -> {
         if (!var0.isConsumed()) {
            Object var1 = var0.getSource();
            if (var1 instanceof Control) {
               Control var2 = (Control)var1;
               if (var2.getContextMenu() != null) {
                  var2.getContextMenu().show(var2, var0.getScreenX(), var0.getScreenY());
                  var0.consume();
               }
            }

         }
      };
   }

   private static class StyleableProperties {
      private static final CssMetaData SKIN = new CssMetaData("-fx-skin", StringConverter.getInstance()) {
         public boolean isSettable(Control var1) {
            return var1.skin == null || !var1.skin.isBound();
         }

         public StyleableProperty getStyleableProperty(Control var1) {
            return (StyleableProperty)var1.skinClassNameProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(SKIN);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
