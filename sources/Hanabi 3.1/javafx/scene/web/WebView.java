package javafx.scene.web;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.web.NGWebView;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.InputMethodClientImpl;
import com.sun.javafx.webkit.KeyCodeMap;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCFocusEvent;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;
import javafx.stage.Window;

public final class WebView extends Parent {
   private static final Map idMap = new HashMap();
   private static final boolean DEFAULT_CONTEXT_MENU_ENABLED = true;
   private static final FontSmoothingType DEFAULT_FONT_SMOOTHING_TYPE;
   private static final double DEFAULT_ZOOM = 1.0;
   private static final double DEFAULT_FONT_SCALE = 1.0;
   private static final double DEFAULT_MIN_WIDTH = 0.0;
   private static final double DEFAULT_MIN_HEIGHT = 0.0;
   private static final double DEFAULT_PREF_WIDTH = 800.0;
   private static final double DEFAULT_PREF_HEIGHT = 600.0;
   private static final double DEFAULT_MAX_WIDTH = Double.MAX_VALUE;
   private static final double DEFAULT_MAX_HEIGHT = Double.MAX_VALUE;
   private final WebPage page;
   private final WebEngine engine;
   private volatile InputMethodClientImpl imClient;
   private final TKPulseListener stagePulseListener;
   private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, "width");
   private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper(this, "height");
   private DoubleProperty zoom;
   private DoubleProperty fontScale;
   private DoubleProperty minWidth;
   private DoubleProperty minHeight;
   private DoubleProperty prefWidth;
   private DoubleProperty prefHeight;
   private DoubleProperty maxWidth;
   private DoubleProperty maxHeight;
   private ObjectProperty fontSmoothingType;
   private BooleanProperty contextMenuEnabled;
   private static final int WK_DND_ACTION_NONE = 0;
   private static final int WK_DND_ACTION_COPY = 1;
   private static final int WK_DND_ACTION_MOVE = 2;
   private static final int WK_DND_ACTION_LINK = 1073741824;

   public final WebEngine getEngine() {
      return this.engine;
   }

   public final double getWidth() {
      return this.width.get();
   }

   public ReadOnlyDoubleProperty widthProperty() {
      return this.width.getReadOnlyProperty();
   }

   public final double getHeight() {
      return this.height.get();
   }

   public ReadOnlyDoubleProperty heightProperty() {
      return this.height.getReadOnlyProperty();
   }

   public final void setZoom(double var1) {
      WebEngine.checkThread();
      this.zoomProperty().set(var1);
   }

   public final double getZoom() {
      return this.zoom != null ? this.zoom.get() : 1.0;
   }

   public final DoubleProperty zoomProperty() {
      if (this.zoom == null) {
         this.zoom = new StyleableDoubleProperty(1.0) {
            public void invalidated() {
               Toolkit.getToolkit().checkFxUserThread();
               WebView.this.page.setZoomFactor((float)this.get(), false);
            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.ZOOM;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "zoom";
            }
         };
      }

      return this.zoom;
   }

   public final void setFontScale(double var1) {
      WebEngine.checkThread();
      this.fontScaleProperty().set(var1);
   }

   public final double getFontScale() {
      return this.fontScale != null ? this.fontScale.get() : 1.0;
   }

   public DoubleProperty fontScaleProperty() {
      if (this.fontScale == null) {
         this.fontScale = new StyleableDoubleProperty(1.0) {
            public void invalidated() {
               Toolkit.getToolkit().checkFxUserThread();
               WebView.this.page.setZoomFactor((float)this.get(), true);
            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.FONT_SCALE;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "fontScale";
            }
         };
      }

      return this.fontScale;
   }

   public WebView() {
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      this.getStyleClass().add("web-view");
      this.engine = new WebEngine();
      this.engine.setView(this);
      this.page = this.engine.getPage();
      this.page.setFontSmoothingType(DEFAULT_FONT_SMOOTHING_TYPE.ordinal());
      this.registerEventHandlers();
      this.stagePulseListener = () -> {
         this.handleStagePulse();
      };
      this.focusedProperty().addListener((var1, var2, var3) -> {
         if (this.page != null) {
            WCFocusEvent var4 = new WCFocusEvent(this.isFocused() ? 2 : 3, -1);
            this.page.dispatchFocusEvent(var4);
         }

      });
      this.setFocusTraversable(true);
      Toolkit.getToolkit().addStageTkPulseListener(this.stagePulseListener);
   }

   public boolean isResizable() {
      return true;
   }

   public void resize(double var1, double var3) {
      if (var1 != this.width.get() || var3 != this.height.get()) {
         this.width.set(var1);
         this.height.set(var3);
         this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         this.impl_geomChanged();
      }

   }

   public final double minWidth(double var1) {
      double var3 = this.getMinWidth();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public final double minHeight(double var1) {
      double var3 = this.getMinHeight();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public final double prefWidth(double var1) {
      double var3 = this.getPrefWidth();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public final double prefHeight(double var1) {
      double var3 = this.getPrefHeight();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public final double maxWidth(double var1) {
      double var3 = this.getMaxWidth();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public final double maxHeight(double var1) {
      double var3 = this.getMaxHeight();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new StyleableDoubleProperty(0.0) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.MIN_WIDTH;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "minWidth";
            }
         };
      }

      return this.minWidth;
   }

   public final void setMinWidth(double var1) {
      this.minWidthProperty().set(var1);
   }

   public final double getMinWidth() {
      return this.minWidth != null ? this.minWidth.get() : 0.0;
   }

   public DoubleProperty minHeightProperty() {
      if (this.minHeight == null) {
         this.minHeight = new StyleableDoubleProperty(0.0) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.MIN_HEIGHT;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "minHeight";
            }
         };
      }

      return this.minHeight;
   }

   public final void setMinHeight(double var1) {
      this.minHeightProperty().set(var1);
   }

   public final double getMinHeight() {
      return this.minHeight != null ? this.minHeight.get() : 0.0;
   }

   public void setMinSize(double var1, double var3) {
      this.setMinWidth(var1);
      this.setMinHeight(var3);
   }

   public DoubleProperty prefWidthProperty() {
      if (this.prefWidth == null) {
         this.prefWidth = new StyleableDoubleProperty(800.0) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.PREF_WIDTH;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "prefWidth";
            }
         };
      }

      return this.prefWidth;
   }

   public final void setPrefWidth(double var1) {
      this.prefWidthProperty().set(var1);
   }

   public final double getPrefWidth() {
      return this.prefWidth != null ? this.prefWidth.get() : 800.0;
   }

   public DoubleProperty prefHeightProperty() {
      if (this.prefHeight == null) {
         this.prefHeight = new StyleableDoubleProperty(600.0) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.PREF_HEIGHT;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "prefHeight";
            }
         };
      }

      return this.prefHeight;
   }

   public final void setPrefHeight(double var1) {
      this.prefHeightProperty().set(var1);
   }

   public final double getPrefHeight() {
      return this.prefHeight != null ? this.prefHeight.get() : 600.0;
   }

   public void setPrefSize(double var1, double var3) {
      this.setPrefWidth(var1);
      this.setPrefHeight(var3);
   }

   public DoubleProperty maxWidthProperty() {
      if (this.maxWidth == null) {
         this.maxWidth = new StyleableDoubleProperty(Double.MAX_VALUE) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.MAX_WIDTH;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "maxWidth";
            }
         };
      }

      return this.maxWidth;
   }

   public final void setMaxWidth(double var1) {
      this.maxWidthProperty().set(var1);
   }

   public final double getMaxWidth() {
      return this.maxWidth != null ? this.maxWidth.get() : Double.MAX_VALUE;
   }

   public DoubleProperty maxHeightProperty() {
      if (this.maxHeight == null) {
         this.maxHeight = new StyleableDoubleProperty(Double.MAX_VALUE) {
            public void invalidated() {
               if (WebView.this.getParent() != null) {
                  WebView.this.getParent().requestLayout();
               }

            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.MAX_HEIGHT;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "maxHeight";
            }
         };
      }

      return this.maxHeight;
   }

   public final void setMaxHeight(double var1) {
      this.maxHeightProperty().set(var1);
   }

   public final double getMaxHeight() {
      return this.maxHeight != null ? this.maxHeight.get() : Double.MAX_VALUE;
   }

   public void setMaxSize(double var1, double var3) {
      this.setMaxWidth(var1);
      this.setMaxHeight(var3);
   }

   public final void setFontSmoothingType(FontSmoothingType var1) {
      this.fontSmoothingTypeProperty().set(var1);
   }

   public final FontSmoothingType getFontSmoothingType() {
      return this.fontSmoothingType != null ? (FontSmoothingType)this.fontSmoothingType.get() : DEFAULT_FONT_SMOOTHING_TYPE;
   }

   public final ObjectProperty fontSmoothingTypeProperty() {
      if (this.fontSmoothingType == null) {
         this.fontSmoothingType = new StyleableObjectProperty(DEFAULT_FONT_SMOOTHING_TYPE) {
            public void invalidated() {
               Toolkit.getToolkit().checkFxUserThread();
               WebView.this.page.setFontSmoothingType(((FontSmoothingType)this.get()).ordinal());
            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.FONT_SMOOTHING_TYPE;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "fontSmoothingType";
            }
         };
      }

      return this.fontSmoothingType;
   }

   public final void setContextMenuEnabled(boolean var1) {
      this.contextMenuEnabledProperty().set(var1);
   }

   public final boolean isContextMenuEnabled() {
      return this.contextMenuEnabled == null ? true : this.contextMenuEnabled.get();
   }

   public final BooleanProperty contextMenuEnabledProperty() {
      if (this.contextMenuEnabled == null) {
         this.contextMenuEnabled = new StyleableBooleanProperty(true) {
            public void invalidated() {
               Toolkit.getToolkit().checkFxUserThread();
               WebView.this.page.setContextMenuEnabled(this.get());
            }

            public CssMetaData getCssMetaData() {
               return WebView.StyleableProperties.CONTEXT_MENU_ENABLED;
            }

            public Object getBean() {
               return WebView.this;
            }

            public String getName() {
               return "contextMenuEnabled";
            }
         };
      }

      return this.contextMenuEnabled;
   }

   public static List getClassCssMetaData() {
      return WebView.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private boolean isTreeReallyVisible() {
      if (this.getScene() == null) {
         return false;
      } else {
         Window var1 = this.getScene().getWindow();
         if (var1 == null) {
            return false;
         } else {
            boolean var2 = var1 instanceof Stage ? ((Stage)var1).isIconified() : false;
            return this.impl_isTreeVisible() && var1.isShowing() && var1.getWidth() > 0.0 && var1.getHeight() > 0.0 && !var2;
         }
      }
   }

   private void handleStagePulse() {
      if (this.page != null) {
         boolean var1 = this.isTreeReallyVisible();
         if (var1) {
            if (this.page.isDirty()) {
               Scene.impl_setAllowPGAccess(true);
               NGWebView var2 = (NGWebView)this.impl_getPeer();
               var2.update();
               if (this.page.isRepaintPending()) {
                  this.impl_markDirty(DirtyBits.WEBVIEW_VIEW);
               }

               Scene.impl_setAllowPGAccess(false);
            }
         } else {
            this.page.dropRenderFrames();
         }

      }
   }

   private void processMouseEvent(MouseEvent var1) {
      if (this.page != null) {
         EventType var2 = var1.getEventType();
         double var3 = var1.getX();
         double var5 = var1.getY();
         double var7 = var1.getScreenX();
         double var9 = var1.getScreenY();
         if (var2 == MouseEvent.MOUSE_EXITED) {
            var2 = MouseEvent.MOUSE_MOVED;
            var3 = -32768.0;
            var5 = -32768.0;
            Point2D var11 = this.localToScreen(var3, var5);
            if (var11 == null) {
               return;
            }

            var7 = var11.getX();
            var9 = var11.getY();
         }

         Integer var13 = (Integer)idMap.get(var2);
         if (var13 != null) {
            WCMouseEvent var12 = new WCMouseEvent(var13, (Integer)idMap.get(var1.getButton()), var1.getClickCount(), (int)var3, (int)var5, (int)var7, (int)var9, System.currentTimeMillis(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), var1.isPopupTrigger());
            this.page.dispatchMouseEvent(var12);
            var1.consume();
         }
      }
   }

   private void processScrollEvent(ScrollEvent var1) {
      if (this.page != null) {
         double var2 = -var1.getDeltaX() * this.getFontScale() * this.getScaleX();
         double var4 = -var1.getDeltaY() * this.getFontScale() * this.getScaleY();
         WCMouseWheelEvent var6 = new WCMouseWheelEvent((int)var1.getX(), (int)var1.getY(), (int)var1.getScreenX(), (int)var1.getScreenY(), System.currentTimeMillis(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), (float)var2, (float)var4);
         this.page.dispatchMouseWheelEvent(var6);
         var1.consume();
      }
   }

   private void processKeyEvent(KeyEvent var1) {
      if (this.page != null) {
         String var2 = null;
         String var3 = null;
         int var4 = 0;
         if (var1.getEventType() == KeyEvent.KEY_TYPED) {
            var2 = var1.getCharacter();
         } else {
            KeyCodeMap.Entry var5 = KeyCodeMap.lookup(var1.getCode());
            var3 = var5.getKeyIdentifier();
            var4 = var5.getWindowsVirtualKeyCode();
         }

         WCKeyEvent var6 = new WCKeyEvent((Integer)idMap.get(var1.getEventType()), var2, var3, var4, var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), System.currentTimeMillis());
         if (this.page.dispatchKeyEvent(var6)) {
            var1.consume();
         }

      }
   }

   private InputMethodClientImpl getInputMethodClient() {
      if (this.imClient == null) {
         synchronized(this) {
            if (this.imClient == null) {
               this.imClient = new InputMethodClientImpl(this, this.page);
            }
         }
      }

      return this.imClient;
   }

   private void processInputMethodEvent(InputMethodEvent var1) {
      if (this.page != null) {
         if (!this.getInputMethodClient().getInputMethodState()) {
            var1.consume();
         } else {
            WCInputMethodEvent var2 = InputMethodClientImpl.convertToWCInputMethodEvent(var1);
            if (this.page.dispatchInputMethodEvent(var2)) {
               var1.consume();
            }
         }
      }
   }

   private static int getWKDndEventType(EventType var0) {
      byte var1 = 0;
      if (var0 == DragEvent.DRAG_ENTERED) {
         var1 = 0;
      } else if (var0 == DragEvent.DRAG_EXITED) {
         var1 = 3;
      } else if (var0 == DragEvent.DRAG_OVER) {
         var1 = 1;
      } else if (var0 == DragEvent.DRAG_DROPPED) {
         var1 = 4;
      }

      return var1;
   }

   private static int getWKDndAction(TransferMode... var0) {
      int var1 = 0;
      TransferMode[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TransferMode var5 = var2[var4];
         if (var5 == TransferMode.COPY) {
            var1 |= 1;
         } else if (var5 == TransferMode.MOVE) {
            var1 |= 2;
         } else if (var5 == TransferMode.LINK) {
            var1 |= 1073741824;
         }
      }

      return var1;
   }

   private static TransferMode[] getFXDndAction(int var0) {
      LinkedList var1 = new LinkedList();
      if ((var0 & 1) != 0) {
         var1.add(TransferMode.COPY);
      }

      if ((var0 & 2) != 0) {
         var1.add(TransferMode.MOVE);
      }

      if ((var0 & 1073741824) != 0) {
         var1.add(TransferMode.LINK);
      }

      return (TransferMode[])var1.toArray(new TransferMode[0]);
   }

   private void registerEventHandlers() {
      this.addEventHandler(KeyEvent.ANY, (var1x) -> {
         this.processKeyEvent(var1x);
      });
      this.addEventHandler(MouseEvent.ANY, (var1x) -> {
         this.processMouseEvent(var1x);
         if (var1x.isDragDetect() && !this.page.isDragConfirmed()) {
            var1x.setDragDetect(false);
         }

      });
      this.addEventHandler(ScrollEvent.SCROLL, (var1x) -> {
         this.processScrollEvent(var1x);
      });
      this.setOnInputMethodTextChanged((var1x) -> {
         this.processInputMethodEvent(var1x);
      });
      EventHandler var1 = (var1x) -> {
         try {
            Dragboard var2 = var1x.getDragboard();
            LinkedList var3 = new LinkedList();
            LinkedList var4 = new LinkedList();
            Iterator var5 = var2.getContentTypes().iterator();

            while(true) {
               DataFormat var6;
               Object var7;
               do {
                  if (!var5.hasNext()) {
                     if (!var3.isEmpty()) {
                        int var11 = getWKDndEventType(var1x.getEventType());
                        int var12 = this.page.dispatchDragOperation(var11, (String[])var3.toArray(new String[0]), (String[])var4.toArray(new String[0]), (int)var1x.getX(), (int)var1x.getY(), (int)var1x.getScreenX(), (int)var1x.getScreenY(), getWKDndAction((TransferMode[])var2.getTransferModes().toArray(new TransferMode[0])));
                        if (var11 != 4 || var12 != 0) {
                           var1x.acceptTransferModes(getFXDndAction(var12));
                        }

                        var1x.consume();
                     }

                     return;
                  }

                  var6 = (DataFormat)var5.next();
                  var7 = var2.getContent(var6);
               } while(var7 == null);

               Iterator var8 = var6.getIdentifiers().iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  var3.add(var9);
                  var4.add(var7.toString());
               }
            }
         } catch (SecurityException var10) {
         }
      };
      this.setOnDragEntered(var1);
      this.setOnDragExited(var1);
      this.setOnDragOver(var1);
      this.setOnDragDropped(var1);
      this.setOnDragDetected((var1x) -> {
         if (this.page.isDragConfirmed()) {
            this.page.confirmStartDrag();
            var1x.consume();
         }

      });
      this.setOnDragDone((var1x) -> {
         this.page.dispatchDragOperation(104, (String[])null, (String[])null, (int)var1x.getX(), (int)var1x.getY(), (int)var1x.getScreenX(), (int)var1x.getScreenY(), getWKDndAction(var1x.getAcceptedTransferMode()));
         var1x.consume();
      });
      this.setInputMethodRequests(this.getInputMethodClient());
   }

   /** @deprecated */
   @Deprecated
   protected void impl_pickNodeLocal(PickRay var1, PickResultChooser var2) {
      this.impl_intersects(var1, var2);
   }

   protected ObservableList getChildren() {
      return super.getChildren();
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGWebView();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      var1.deriveWithNewBounds(0.0F, 0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight(), 0.0F);
      var2.transform(var1, var1);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGWebView var1 = (NGWebView)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1.setPage(this.page);
      }

      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         var1.resize((float)this.getWidth(), (float)this.getHeight());
      }

      if (this.impl_isDirty(DirtyBits.WEBVIEW_VIEW)) {
         var1.requestRender();
      }

   }

   static {
      DEFAULT_FONT_SMOOTHING_TYPE = FontSmoothingType.LCD;
      idMap.put(MouseButton.NONE, 0);
      idMap.put(MouseButton.PRIMARY, 1);
      idMap.put(MouseButton.MIDDLE, 2);
      idMap.put(MouseButton.SECONDARY, 4);
      idMap.put(MouseEvent.MOUSE_PRESSED, 0);
      idMap.put(MouseEvent.MOUSE_RELEASED, 1);
      idMap.put(MouseEvent.MOUSE_MOVED, 2);
      idMap.put(MouseEvent.MOUSE_DRAGGED, 3);
      idMap.put(KeyEvent.KEY_PRESSED, 1);
      idMap.put(KeyEvent.KEY_RELEASED, 2);
      idMap.put(KeyEvent.KEY_TYPED, 0);
   }

   private static final class StyleableProperties {
      private static final CssMetaData CONTEXT_MENU_ENABLED = new CssMetaData("-fx-context-menu-enabled", BooleanConverter.getInstance(), true) {
         public boolean isSettable(WebView var1) {
            return var1.contextMenuEnabled == null || !var1.contextMenuEnabled.isBound();
         }

         public StyleableProperty getStyleableProperty(WebView var1) {
            return (StyleableProperty)var1.contextMenuEnabledProperty();
         }
      };
      private static final CssMetaData FONT_SMOOTHING_TYPE;
      private static final CssMetaData ZOOM;
      private static final CssMetaData FONT_SCALE;
      private static final CssMetaData MIN_WIDTH;
      private static final CssMetaData MIN_HEIGHT;
      private static final CssMetaData MAX_WIDTH;
      private static final CssMetaData MAX_HEIGHT;
      private static final CssMetaData PREF_WIDTH;
      private static final CssMetaData PREF_HEIGHT;
      private static final List STYLEABLES;

      static {
         FONT_SMOOTHING_TYPE = new CssMetaData("-fx-font-smoothing-type", new EnumConverter(FontSmoothingType.class), WebView.DEFAULT_FONT_SMOOTHING_TYPE) {
            public boolean isSettable(WebView var1) {
               return var1.fontSmoothingType == null || !var1.fontSmoothingType.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.fontSmoothingTypeProperty();
            }
         };
         ZOOM = new CssMetaData("-fx-zoom", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(WebView var1) {
               return var1.zoom == null || !var1.zoom.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.zoomProperty();
            }
         };
         FONT_SCALE = new CssMetaData("-fx-font-scale", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(WebView var1) {
               return var1.fontScale == null || !var1.fontScale.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.fontScaleProperty();
            }
         };
         MIN_WIDTH = new CssMetaData("-fx-min-width", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(WebView var1) {
               return var1.minWidth == null || !var1.minWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.minWidthProperty();
            }
         };
         MIN_HEIGHT = new CssMetaData("-fx-min-height", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(WebView var1) {
               return var1.minHeight == null || !var1.minHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.minHeightProperty();
            }
         };
         MAX_WIDTH = new CssMetaData("-fx-max-width", SizeConverter.getInstance(), Double.MAX_VALUE) {
            public boolean isSettable(WebView var1) {
               return var1.maxWidth == null || !var1.maxWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.maxWidthProperty();
            }
         };
         MAX_HEIGHT = new CssMetaData("-fx-max-height", SizeConverter.getInstance(), Double.MAX_VALUE) {
            public boolean isSettable(WebView var1) {
               return var1.maxHeight == null || !var1.maxHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.maxHeightProperty();
            }
         };
         PREF_WIDTH = new CssMetaData("-fx-pref-width", SizeConverter.getInstance(), 800.0) {
            public boolean isSettable(WebView var1) {
               return var1.prefWidth == null || !var1.prefWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.prefWidthProperty();
            }
         };
         PREF_HEIGHT = new CssMetaData("-fx-pref-height", SizeConverter.getInstance(), 600.0) {
            public boolean isSettable(WebView var1) {
               return var1.prefHeight == null || !var1.prefHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(WebView var1) {
               return (StyleableProperty)var1.prefHeightProperty();
            }
         };
         ArrayList var0 = new ArrayList(Parent.getClassCssMetaData());
         var0.add(CONTEXT_MENU_ENABLED);
         var0.add(FONT_SMOOTHING_TYPE);
         var0.add(ZOOM);
         var0.add(FONT_SCALE);
         var0.add(MIN_WIDTH);
         var0.add(PREF_WIDTH);
         var0.add(MAX_WIDTH);
         var0.add(MIN_HEIGHT);
         var0.add(PREF_HEIGHT);
         var0.add(MAX_HEIGHT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
