package javafx.stage;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.stage.StageHelper;
import com.sun.javafx.stage.StagePeerListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;

public class Stage extends Window {
   private boolean inNestedEventLoop;
   private static ObservableList stages = FXCollections.observableArrayList();
   private static final StagePeerListener.StageAccessor STAGE_ACCESSOR;
   private boolean primary;
   private boolean securityDialog;
   private boolean important;
   private StageStyle style;
   private Modality modality;
   private Window owner;
   private ReadOnlyBooleanWrapper fullScreen;
   private ObservableList icons;
   private StringProperty title;
   private ReadOnlyBooleanWrapper iconified;
   private ReadOnlyBooleanWrapper maximized;
   private ReadOnlyBooleanWrapper alwaysOnTop;
   private BooleanProperty resizable;
   private DoubleProperty minWidth;
   private DoubleProperty minHeight;
   private DoubleProperty maxWidth;
   private DoubleProperty maxHeight;
   private final ObjectProperty fullScreenExitCombination;
   private final ObjectProperty fullScreenExitHint;

   public Stage() {
      this(StageStyle.DECORATED);
   }

   public Stage(StageStyle var1) {
      this.inNestedEventLoop = false;
      this.primary = false;
      this.securityDialog = false;
      this.important = true;
      this.modality = Modality.NONE;
      this.owner = null;
      this.icons = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            ArrayList var2 = new ArrayList();
            Iterator var3 = Stage.this.icons.iterator();

            while(var3.hasNext()) {
               Image var4 = (Image)var3.next();
               var2.add(var4.impl_getPlatformImage());
            }

            if (Stage.this.impl_peer != null) {
               Stage.this.impl_peer.setIcons(var2);
            }

         }
      };
      this.fullScreenExitCombination = new SimpleObjectProperty(this, "fullScreenExitCombination", (Object)null);
      this.fullScreenExitHint = new SimpleObjectProperty(this, "fullScreenExitHint", (Object)null);
      Toolkit.getToolkit().checkFxUserThread();
      this.initStyle(var1);
   }

   public final void setScene(Scene var1) {
      Toolkit.getToolkit().checkFxUserThread();
      super.setScene(var1);
   }

   public final void show() {
      super.show();
   }

   final void initSecurityDialog(boolean var1) {
      if (this.hasBeenVisible) {
         throw new IllegalStateException("Cannot set securityDialog once stage has been set visible");
      } else {
         this.securityDialog = var1;
      }
   }

   final boolean isSecurityDialog() {
      return this.securityDialog;
   }

   /** @deprecated */
   @Deprecated
   public void impl_setPrimary(boolean var1) {
      this.primary = var1;
   }

   boolean isPrimary() {
      return this.primary;
   }

   /** @deprecated */
   @Deprecated
   public String impl_getMXWindowType() {
      return this.primary ? "PrimaryStage" : this.getClass().getSimpleName();
   }

   /** @deprecated */
   @Deprecated
   public void impl_setImportant(boolean var1) {
      this.important = var1;
   }

   private boolean isImportant() {
      return this.important;
   }

   public void showAndWait() {
      Toolkit.getToolkit().checkFxUserThread();
      if (this.isPrimary()) {
         throw new IllegalStateException("Cannot call this method on primary stage");
      } else if (this.isShowing()) {
         throw new IllegalStateException("Stage already visible");
      } else if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
         throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
      } else {
         assert !this.inNestedEventLoop;

         this.show();
         this.inNestedEventLoop = true;
         Toolkit.getToolkit().enterNestedEventLoop(this);
      }
   }

   public final void initStyle(StageStyle var1) {
      if (this.hasBeenVisible) {
         throw new IllegalStateException("Cannot set style once stage has been set visible");
      } else {
         this.style = var1;
      }
   }

   public final StageStyle getStyle() {
      return this.style;
   }

   public final void initModality(Modality var1) {
      if (this.hasBeenVisible) {
         throw new IllegalStateException("Cannot set modality once stage has been set visible");
      } else if (this.isPrimary()) {
         throw new IllegalStateException("Cannot set modality for the primary stage");
      } else {
         this.modality = var1;
      }
   }

   public final Modality getModality() {
      return this.modality;
   }

   public final void initOwner(Window var1) {
      if (this.hasBeenVisible) {
         throw new IllegalStateException("Cannot set owner once stage has been set visible");
      } else if (this.isPrimary()) {
         throw new IllegalStateException("Cannot set owner for the primary stage");
      } else {
         this.owner = var1;
         Scene var2 = this.getScene();
         if (var2 != null) {
            SceneHelper.parentEffectiveOrientationInvalidated(var2);
         }

      }
   }

   public final Window getOwner() {
      return this.owner;
   }

   public final void setFullScreen(boolean var1) {
      Toolkit.getToolkit().checkFxUserThread();
      this.fullScreenPropertyImpl().set(var1);
      if (this.impl_peer != null) {
         this.impl_peer.setFullScreen(var1);
      }

   }

   public final boolean isFullScreen() {
      return this.fullScreen == null ? false : this.fullScreen.get();
   }

   public final ReadOnlyBooleanProperty fullScreenProperty() {
      return this.fullScreenPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper fullScreenPropertyImpl() {
      if (this.fullScreen == null) {
         this.fullScreen = new ReadOnlyBooleanWrapper(this, "fullScreen");
      }

      return this.fullScreen;
   }

   public final ObservableList getIcons() {
      return this.icons;
   }

   public final void setTitle(String var1) {
      this.titleProperty().set(var1);
   }

   public final String getTitle() {
      return this.title == null ? null : (String)this.title.get();
   }

   public final StringProperty titleProperty() {
      if (this.title == null) {
         this.title = new StringPropertyBase() {
            protected void invalidated() {
               if (Stage.this.impl_peer != null) {
                  Stage.this.impl_peer.setTitle(this.get());
               }

            }

            public Object getBean() {
               return Stage.this;
            }

            public String getName() {
               return "title";
            }
         };
      }

      return this.title;
   }

   public final void setIconified(boolean var1) {
      this.iconifiedPropertyImpl().set(var1);
      if (this.impl_peer != null) {
         this.impl_peer.setIconified(var1);
      }

   }

   public final boolean isIconified() {
      return this.iconified == null ? false : this.iconified.get();
   }

   public final ReadOnlyBooleanProperty iconifiedProperty() {
      return this.iconifiedPropertyImpl().getReadOnlyProperty();
   }

   private final ReadOnlyBooleanWrapper iconifiedPropertyImpl() {
      if (this.iconified == null) {
         this.iconified = new ReadOnlyBooleanWrapper(this, "iconified");
      }

      return this.iconified;
   }

   public final void setMaximized(boolean var1) {
      this.maximizedPropertyImpl().set(var1);
      if (this.impl_peer != null) {
         this.impl_peer.setMaximized(var1);
      }

   }

   public final boolean isMaximized() {
      return this.maximized == null ? false : this.maximized.get();
   }

   public final ReadOnlyBooleanProperty maximizedProperty() {
      return this.maximizedPropertyImpl().getReadOnlyProperty();
   }

   private final ReadOnlyBooleanWrapper maximizedPropertyImpl() {
      if (this.maximized == null) {
         this.maximized = new ReadOnlyBooleanWrapper(this, "maximized");
      }

      return this.maximized;
   }

   public final void setAlwaysOnTop(boolean var1) {
      this.alwaysOnTopPropertyImpl().set(var1);
      if (this.impl_peer != null) {
         this.impl_peer.setAlwaysOnTop(var1);
      }

   }

   public final boolean isAlwaysOnTop() {
      return this.alwaysOnTop == null ? false : this.alwaysOnTop.get();
   }

   public final ReadOnlyBooleanProperty alwaysOnTopProperty() {
      return this.alwaysOnTopPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper alwaysOnTopPropertyImpl() {
      if (this.alwaysOnTop == null) {
         this.alwaysOnTop = new ReadOnlyBooleanWrapper(this, "alwaysOnTop");
      }

      return this.alwaysOnTop;
   }

   public final void setResizable(boolean var1) {
      this.resizableProperty().set(var1);
   }

   public final boolean isResizable() {
      return this.resizable == null ? true : this.resizable.get();
   }

   public final BooleanProperty resizableProperty() {
      if (this.resizable == null) {
         this.resizable = new ResizableProperty();
      }

      return this.resizable;
   }

   public final void setMinWidth(double var1) {
      this.minWidthProperty().set(var1);
   }

   public final double getMinWidth() {
      return this.minWidth == null ? 0.0 : this.minWidth.get();
   }

   public final DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               if (Stage.this.impl_peer != null) {
                  Stage.this.impl_peer.setMinimumSize((int)Math.ceil(this.get()), (int)Math.ceil(Stage.this.getMinHeight()));
               }

               if (Stage.this.getWidth() < Stage.this.getMinWidth()) {
                  Stage.this.setWidth(Stage.this.getMinWidth());
               }

            }

            public Object getBean() {
               return Stage.this;
            }

            public String getName() {
               return "minWidth";
            }
         };
      }

      return this.minWidth;
   }

   public final void setMinHeight(double var1) {
      this.minHeightProperty().set(var1);
   }

   public final double getMinHeight() {
      return this.minHeight == null ? 0.0 : this.minHeight.get();
   }

   public final DoubleProperty minHeightProperty() {
      if (this.minHeight == null) {
         this.minHeight = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               if (Stage.this.impl_peer != null) {
                  Stage.this.impl_peer.setMinimumSize((int)Math.ceil(Stage.this.getMinWidth()), (int)Math.ceil(this.get()));
               }

               if (Stage.this.getHeight() < Stage.this.getMinHeight()) {
                  Stage.this.setHeight(Stage.this.getMinHeight());
               }

            }

            public Object getBean() {
               return Stage.this;
            }

            public String getName() {
               return "minHeight";
            }
         };
      }

      return this.minHeight;
   }

   public final void setMaxWidth(double var1) {
      this.maxWidthProperty().set(var1);
   }

   public final double getMaxWidth() {
      return this.maxWidth == null ? Double.MAX_VALUE : this.maxWidth.get();
   }

   public final DoubleProperty maxWidthProperty() {
      if (this.maxWidth == null) {
         this.maxWidth = new DoublePropertyBase(Double.MAX_VALUE) {
            protected void invalidated() {
               if (Stage.this.impl_peer != null) {
                  Stage.this.impl_peer.setMaximumSize((int)Math.floor(this.get()), (int)Math.floor(Stage.this.getMaxHeight()));
               }

               if (Stage.this.getWidth() > Stage.this.getMaxWidth()) {
                  Stage.this.setWidth(Stage.this.getMaxWidth());
               }

            }

            public Object getBean() {
               return Stage.this;
            }

            public String getName() {
               return "maxWidth";
            }
         };
      }

      return this.maxWidth;
   }

   public final void setMaxHeight(double var1) {
      this.maxHeightProperty().set(var1);
   }

   public final double getMaxHeight() {
      return this.maxHeight == null ? Double.MAX_VALUE : this.maxHeight.get();
   }

   public final DoubleProperty maxHeightProperty() {
      if (this.maxHeight == null) {
         this.maxHeight = new DoublePropertyBase(Double.MAX_VALUE) {
            protected void invalidated() {
               if (Stage.this.impl_peer != null) {
                  Stage.this.impl_peer.setMaximumSize((int)Math.floor(Stage.this.getMaxWidth()), (int)Math.floor(this.get()));
               }

               if (Stage.this.getHeight() > Stage.this.getMaxHeight()) {
                  Stage.this.setHeight(Stage.this.getMaxHeight());
               }

            }

            public Object getBean() {
               return Stage.this;
            }

            public String getName() {
               return "maxHeight";
            }
         };
      }

      return this.maxHeight;
   }

   /** @deprecated */
   @Deprecated
   protected void impl_visibleChanging(boolean var1) {
      super.impl_visibleChanging(var1);
      Toolkit var2 = Toolkit.getToolkit();
      if (var1 && this.impl_peer == null) {
         Window var3 = this.getOwner();
         TKStage var4 = var3 == null ? null : var3.impl_getPeer();
         Scene var5 = this.getScene();
         boolean var6 = var5 != null && var5.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
         StageStyle var7 = this.getStyle();
         if (var7 == StageStyle.TRANSPARENT) {
            SecurityManager var8 = System.getSecurityManager();
            if (var8 != null) {
               try {
                  var8.checkPermission(new AllPermission());
               } catch (SecurityException var10) {
                  var7 = StageStyle.UNDECORATED;
               }
            }
         }

         this.impl_peer = var2.createTKStage(this, this.isSecurityDialog(), var7, this.isPrimary(), this.getModality(), var4, var6, this.acc);
         this.impl_peer.setMinimumSize((int)Math.ceil(this.getMinWidth()), (int)Math.ceil(this.getMinHeight()));
         this.impl_peer.setMaximumSize((int)Math.floor(this.getMaxWidth()), (int)Math.floor(this.getMaxHeight()));
         this.peerListener = new StagePeerListener(this, STAGE_ACCESSOR);
         stages.add(this);
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_visibleChanged(boolean var1) {
      super.impl_visibleChanged(var1);
      if (var1) {
         this.impl_peer.setImportant(this.isImportant());
         this.impl_peer.setResizable(this.isResizable());
         this.impl_peer.setFullScreen(this.isFullScreen());
         this.impl_peer.setAlwaysOnTop(this.isAlwaysOnTop());
         this.impl_peer.setIconified(this.isIconified());
         this.impl_peer.setMaximized(this.isMaximized());
         this.impl_peer.setTitle(this.getTitle());
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.icons.iterator();

         while(var3.hasNext()) {
            Image var4 = (Image)var3.next();
            var2.add(var4.impl_getPlatformImage());
         }

         if (this.impl_peer != null) {
            this.impl_peer.setIcons(var2);
         }
      }

      if (!var1) {
         stages.remove(this);
      }

      if (!var1 && this.inNestedEventLoop) {
         this.inNestedEventLoop = false;
         Toolkit.getToolkit().exitNestedEventLoop(this, (Object)null);
      }

   }

   public void toFront() {
      if (this.impl_peer != null) {
         this.impl_peer.toFront();
      }

   }

   public void toBack() {
      if (this.impl_peer != null) {
         this.impl_peer.toBack();
      }

   }

   public void close() {
      this.hide();
   }

   Window getWindowOwner() {
      return this.getOwner();
   }

   public final void setFullScreenExitKeyCombination(KeyCombination var1) {
      this.fullScreenExitCombination.set(var1);
   }

   public final KeyCombination getFullScreenExitKeyCombination() {
      return (KeyCombination)this.fullScreenExitCombination.get();
   }

   public final ObjectProperty fullScreenExitKeyProperty() {
      return this.fullScreenExitCombination;
   }

   public final void setFullScreenExitHint(String var1) {
      this.fullScreenExitHint.set(var1);
   }

   public final String getFullScreenExitHint() {
      return (String)this.fullScreenExitHint.get();
   }

   public final ObjectProperty fullScreenExitHintProperty() {
      return this.fullScreenExitHint;
   }

   static {
      FXRobotHelper.setStageAccessor(new FXRobotHelper.FXRobotStageAccessor() {
         public ObservableList getStages() {
            return Stage.stages;
         }
      });
      StageHelper.setStageAccessor(new StageHelper.StageAccessor() {
         public ObservableList getStages() {
            return Stage.stages;
         }

         public void initSecurityDialog(Stage var1, boolean var2) {
            var1.initSecurityDialog(var2);
         }
      });
      STAGE_ACCESSOR = new StagePeerListener.StageAccessor() {
         public void setIconified(Stage var1, boolean var2) {
            var1.iconifiedPropertyImpl().set(var2);
         }

         public void setMaximized(Stage var1, boolean var2) {
            var1.maximizedPropertyImpl().set(var2);
         }

         public void setResizable(Stage var1, boolean var2) {
            ((ResizableProperty)var1.resizableProperty()).setNoInvalidate(var2);
         }

         public void setFullScreen(Stage var1, boolean var2) {
            var1.fullScreenPropertyImpl().set(var2);
         }

         public void setAlwaysOnTop(Stage var1, boolean var2) {
            var1.alwaysOnTopPropertyImpl().set(var2);
         }
      };
   }

   private class ResizableProperty extends SimpleBooleanProperty {
      private boolean noInvalidate;

      public ResizableProperty() {
         super(Stage.this, "resizable", true);
      }

      void setNoInvalidate(boolean var1) {
         this.noInvalidate = true;
         this.set(var1);
         this.noInvalidate = false;
      }

      protected void invalidated() {
         if (!this.noInvalidate) {
            if (Stage.this.impl_peer != null) {
               Stage.this.applyBounds();
               Stage.this.impl_peer.setResizable(this.get());
            }

         }
      }

      public void bind(ObservableValue var1) {
         throw new RuntimeException("Resizable property cannot be bound");
      }
   }
}
