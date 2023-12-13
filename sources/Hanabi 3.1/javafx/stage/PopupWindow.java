package javafx.stage;

import com.sun.javafx.event.DirectEvent;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventRedirector;
import com.sun.javafx.event.EventUtil;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.PopupWindowPeerListener;
import com.sun.javafx.stage.WindowCloseRequestHandler;
import com.sun.javafx.stage.WindowEventDispatcher;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public abstract class PopupWindow extends Window {
   private final List children = new ArrayList();
   private final InvalidationListener popupWindowUpdater = new InvalidationListener() {
      public void invalidated(Observable var1) {
         PopupWindow.this.cachedExtendedBounds = null;
         PopupWindow.this.cachedAnchorBounds = null;
         PopupWindow.this.updateWindow(PopupWindow.this.getAnchorX(), PopupWindow.this.getAnchorY());
      }
   };
   private ChangeListener changeListener = (var1x, var2x, var3) -> {
      if (var2x && !var3) {
         this.hide();
      }

   };
   private WeakChangeListener weakOwnerNodeListener;
   private ReadOnlyObjectWrapper ownerWindow;
   private ReadOnlyObjectWrapper ownerNode;
   private BooleanProperty autoFix;
   private BooleanProperty autoHide;
   private ObjectProperty onAutoHide;
   private BooleanProperty hideOnEscape;
   private BooleanProperty consumeAutoHidingEvents;
   private Window rootWindow;
   private final ReadOnlyDoubleWrapper anchorX;
   private final ReadOnlyDoubleWrapper anchorY;
   private final ObjectProperty anchorLocation;
   private Bounds cachedExtendedBounds;
   private Bounds cachedAnchorBounds;
   private ChangeListener ownerFocusedListener;
   private boolean autofixActive;
   private boolean autohideActive;

   public PopupWindow() {
      this.weakOwnerNodeListener = new WeakChangeListener(this.changeListener);
      this.ownerWindow = new ReadOnlyObjectWrapper(this, "ownerWindow");
      this.ownerNode = new ReadOnlyObjectWrapper(this, "ownerNode");
      this.autoFix = new BooleanPropertyBase(true) {
         protected void invalidated() {
            PopupWindow.this.handleAutofixActivation(PopupWindow.this.isShowing(), this.get());
         }

         public Object getBean() {
            return PopupWindow.this;
         }

         public String getName() {
            return "autoFix";
         }
      };
      this.autoHide = new BooleanPropertyBase() {
         protected void invalidated() {
            PopupWindow.this.handleAutohideActivation(PopupWindow.this.isShowing(), this.get());
         }

         public Object getBean() {
            return PopupWindow.this;
         }

         public String getName() {
            return "autoHide";
         }
      };
      this.onAutoHide = new SimpleObjectProperty(this, "onAutoHide");
      this.hideOnEscape = new SimpleBooleanProperty(this, "hideOnEscape", true);
      this.consumeAutoHidingEvents = new SimpleBooleanProperty(this, "consumeAutoHidingEvents", true);
      this.anchorX = new ReadOnlyDoubleWrapper(this, "anchorX", Double.NaN);
      this.anchorY = new ReadOnlyDoubleWrapper(this, "anchorY", Double.NaN);
      this.anchorLocation = new ObjectPropertyBase(PopupWindow.AnchorLocation.WINDOW_TOP_LEFT) {
         protected void invalidated() {
            PopupWindow.this.cachedAnchorBounds = null;
            PopupWindow.this.updateWindow(PopupWindow.this.windowToAnchorX(PopupWindow.this.getX()), PopupWindow.this.windowToAnchorY(PopupWindow.this.getY()));
         }

         public Object getBean() {
            return PopupWindow.this;
         }

         public String getName() {
            return "anchorLocation";
         }
      };
      Pane var1 = new Pane();
      var1.setBackground(Background.EMPTY);
      var1.getStyleClass().add("popup");
      final Scene var2 = SceneHelper.createPopupScene(var1);
      var2.setFill((Paint)null);
      super.setScene(var2);
      var1.layoutBoundsProperty().addListener(this.popupWindowUpdater);
      var1.boundsInLocalProperty().addListener(this.popupWindowUpdater);
      var2.rootProperty().addListener(new InvalidationListener() {
         private Node oldRoot = var2.getRoot();

         public void invalidated(Observable var1) {
            Parent var2x = var2.getRoot();
            if (this.oldRoot != var2x) {
               if (this.oldRoot != null) {
                  this.oldRoot.layoutBoundsProperty().removeListener(PopupWindow.this.popupWindowUpdater);
                  this.oldRoot.boundsInLocalProperty().removeListener(PopupWindow.this.popupWindowUpdater);
                  this.oldRoot.getStyleClass().remove("popup");
               }

               if (var2x != null) {
                  var2x.layoutBoundsProperty().addListener(PopupWindow.this.popupWindowUpdater);
                  var2x.boundsInLocalProperty().addListener(PopupWindow.this.popupWindowUpdater);
                  var2x.getStyleClass().add("popup");
               }

               this.oldRoot = var2x;
               PopupWindow.this.cachedExtendedBounds = null;
               PopupWindow.this.cachedAnchorBounds = null;
               PopupWindow.this.updateWindow(PopupWindow.this.getAnchorX(), PopupWindow.this.getAnchorY());
            }

         }
      });
   }

   /** @deprecated */
   @Deprecated
   protected ObservableList getContent() {
      Parent var1 = this.getScene().getRoot();
      if (var1 instanceof Group) {
         return ((Group)var1).getChildren();
      } else if (var1 instanceof Pane) {
         return ((Pane)var1).getChildren();
      } else {
         throw new IllegalStateException("The content of the Popup can't be accessed");
      }
   }

   public final Window getOwnerWindow() {
      return (Window)this.ownerWindow.get();
   }

   public final ReadOnlyObjectProperty ownerWindowProperty() {
      return this.ownerWindow.getReadOnlyProperty();
   }

   public final Node getOwnerNode() {
      return (Node)this.ownerNode.get();
   }

   public final ReadOnlyObjectProperty ownerNodeProperty() {
      return this.ownerNode.getReadOnlyProperty();
   }

   protected final void setScene(Scene var1) {
      throw new UnsupportedOperationException();
   }

   public final void setAutoFix(boolean var1) {
      this.autoFix.set(var1);
   }

   public final boolean isAutoFix() {
      return this.autoFix.get();
   }

   public final BooleanProperty autoFixProperty() {
      return this.autoFix;
   }

   public final void setAutoHide(boolean var1) {
      this.autoHide.set(var1);
   }

   public final boolean isAutoHide() {
      return this.autoHide.get();
   }

   public final BooleanProperty autoHideProperty() {
      return this.autoHide;
   }

   public final void setOnAutoHide(EventHandler var1) {
      this.onAutoHide.set(var1);
   }

   public final EventHandler getOnAutoHide() {
      return (EventHandler)this.onAutoHide.get();
   }

   public final ObjectProperty onAutoHideProperty() {
      return this.onAutoHide;
   }

   public final void setHideOnEscape(boolean var1) {
      this.hideOnEscape.set(var1);
   }

   public final boolean isHideOnEscape() {
      return this.hideOnEscape.get();
   }

   public final BooleanProperty hideOnEscapeProperty() {
      return this.hideOnEscape;
   }

   public final void setConsumeAutoHidingEvents(boolean var1) {
      this.consumeAutoHidingEvents.set(var1);
   }

   public final boolean getConsumeAutoHidingEvents() {
      return this.consumeAutoHidingEvents.get();
   }

   public final BooleanProperty consumeAutoHidingEventsProperty() {
      return this.consumeAutoHidingEvents;
   }

   public void show(Window var1) {
      this.validateOwnerWindow(var1);
      this.showImpl(var1);
   }

   public void show(Node var1, double var2, double var4) {
      if (var1 == null) {
         throw new NullPointerException("The owner node must not be null");
      } else {
         Scene var6 = var1.getScene();
         if (var6 != null && var6.getWindow() != null) {
            Window var7 = var6.getWindow();
            this.validateOwnerWindow(var7);
            this.ownerNode.set(var1);
            if (var1 != null) {
               var1.visibleProperty().addListener(this.weakOwnerNodeListener);
            }

            this.updateWindow(var2, var4);
            this.showImpl(var7);
         } else {
            throw new IllegalArgumentException("The owner node needs to be associated with a window");
         }
      }
   }

   public void show(Window var1, double var2, double var4) {
      this.validateOwnerWindow(var1);
      this.updateWindow(var2, var4);
      this.showImpl(var1);
   }

   private void showImpl(Window var1) {
      this.ownerWindow.set(var1);
      if (var1 instanceof PopupWindow) {
         ((PopupWindow)var1).children.add(this);
      }

      if (var1 != null) {
         var1.showingProperty().addListener(this.weakOwnerNodeListener);
      }

      Scene var2 = this.getScene();
      SceneHelper.parentEffectiveOrientationInvalidated(var2);
      Scene var3 = getRootWindow(var1).getScene();
      if (var3 != null) {
         if (var3.getUserAgentStylesheet() != null) {
            var2.setUserAgentStylesheet(var3.getUserAgentStylesheet());
         }

         var2.getStylesheets().setAll((Collection)var3.getStylesheets());
         if (var2.getCursor() == null) {
            var2.setCursor(var3.getCursor());
         }
      }

      if (getRootWindow(var1).isShowing()) {
         this.show();
      }

   }

   public void hide() {
      Iterator var1 = this.children.iterator();

      while(var1.hasNext()) {
         PopupWindow var2 = (PopupWindow)var1.next();
         if (var2.isShowing()) {
            var2.hide();
         }
      }

      this.children.clear();
      super.hide();
      if (this.getOwnerWindow() != null) {
         this.getOwnerWindow().showingProperty().removeListener(this.weakOwnerNodeListener);
      }

      if (this.getOwnerNode() != null) {
         this.getOwnerNode().visibleProperty().removeListener(this.weakOwnerNodeListener);
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_visibleChanging(boolean var1) {
      super.impl_visibleChanging(var1);
      PerformanceTracker.logEvent("PopupWindow.storeVisible for [PopupWindow]");
      Toolkit var2 = Toolkit.getToolkit();
      if (var1 && this.impl_peer == null) {
         StageStyle var3;
         try {
            SecurityManager var4 = System.getSecurityManager();
            if (var4 != null) {
               var4.checkPermission(new AllPermission());
            }

            var3 = StageStyle.TRANSPARENT;
         } catch (SecurityException var5) {
            var3 = StageStyle.UNDECORATED;
         }

         this.impl_peer = var2.createTKPopupStage(this, var3, this.getOwnerWindow().impl_getPeer(), this.acc);
         this.peerListener = new PopupWindowPeerListener(this);
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_visibleChanged(boolean var1) {
      super.impl_visibleChanged(var1);
      Window var2 = this.getOwnerWindow();
      if (var1) {
         this.rootWindow = getRootWindow(var2);
         this.startMonitorOwnerEvents(var2);
         this.bindOwnerFocusedProperty(var2);
         this.setFocused(var2.isFocused());
         this.handleAutofixActivation(true, this.isAutoFix());
         this.handleAutohideActivation(true, this.isAutoHide());
      } else {
         this.stopMonitorOwnerEvents(var2);
         this.unbindOwnerFocusedProperty(var2);
         this.setFocused(false);
         this.handleAutofixActivation(false, this.isAutoFix());
         this.handleAutohideActivation(false, this.isAutoHide());
         this.rootWindow = null;
      }

      PerformanceTracker.logEvent("PopupWindow.storeVisible for [PopupWindow] finished");
   }

   public final void setAnchorX(double var1) {
      this.updateWindow(var1, this.getAnchorY());
   }

   public final double getAnchorX() {
      return this.anchorX.get();
   }

   public final ReadOnlyDoubleProperty anchorXProperty() {
      return this.anchorX.getReadOnlyProperty();
   }

   public final void setAnchorY(double var1) {
      this.updateWindow(this.getAnchorX(), var1);
   }

   public final double getAnchorY() {
      return this.anchorY.get();
   }

   public final ReadOnlyDoubleProperty anchorYProperty() {
      return this.anchorY.getReadOnlyProperty();
   }

   public final void setAnchorLocation(AnchorLocation var1) {
      this.anchorLocation.set(var1);
   }

   public final AnchorLocation getAnchorLocation() {
      return (AnchorLocation)this.anchorLocation.get();
   }

   public final ObjectProperty anchorLocationProperty() {
      return this.anchorLocation;
   }

   void setXInternal(double var1) {
      this.updateWindow(this.windowToAnchorX(var1), this.getAnchorY());
   }

   void setYInternal(double var1) {
      this.updateWindow(this.getAnchorX(), this.windowToAnchorY(var1));
   }

   void notifyLocationChanged(double var1, double var3) {
      super.notifyLocationChanged(var1, var3);
      this.anchorX.set(this.windowToAnchorX(var1));
      this.anchorY.set(this.windowToAnchorY(var3));
   }

   private Bounds getExtendedBounds() {
      if (this.cachedExtendedBounds == null) {
         Parent var1 = this.getScene().getRoot();
         this.cachedExtendedBounds = this.union(var1.getLayoutBounds(), var1.getBoundsInLocal());
      }

      return this.cachedExtendedBounds;
   }

   private Bounds getAnchorBounds() {
      if (this.cachedAnchorBounds == null) {
         this.cachedAnchorBounds = this.getAnchorLocation().isContentLocation() ? this.getScene().getRoot().getLayoutBounds() : this.getExtendedBounds();
      }

      return this.cachedAnchorBounds;
   }

   private void updateWindow(double var1, double var3) {
      AnchorLocation var5 = this.getAnchorLocation();
      Parent var6 = this.getScene().getRoot();
      Bounds var7 = this.getExtendedBounds();
      Bounds var8 = this.getAnchorBounds();
      double var9 = var5.getXCoef();
      double var11 = var5.getYCoef();
      double var13 = var9 * var8.getWidth();
      double var15 = var11 * var8.getHeight();
      double var17 = var1 - var13;
      double var19 = var3 - var15;
      if (this.autofixActive) {
         Screen var21 = Utils.getScreenForPoint(var1, var3);
         Rectangle2D var22 = Utils.hasFullScreenStage(var21) ? var21.getBounds() : var21.getVisualBounds();
         if (var9 <= 0.5) {
            var17 = Math.min(var17, var22.getMaxX() - var8.getWidth());
            var17 = Math.max(var17, var22.getMinX());
         } else {
            var17 = Math.max(var17, var22.getMinX());
            var17 = Math.min(var17, var22.getMaxX() - var8.getWidth());
         }

         if (var11 <= 0.5) {
            var19 = Math.min(var19, var22.getMaxY() - var8.getHeight());
            var19 = Math.max(var19, var22.getMinY());
         } else {
            var19 = Math.max(var19, var22.getMinY());
            var19 = Math.min(var19, var22.getMaxY() - var8.getHeight());
         }
      }

      double var25 = var17 - var8.getMinX() + var7.getMinX();
      double var23 = var19 - var8.getMinY() + var7.getMinY();
      this.setWidth(var7.getWidth());
      this.setHeight(var7.getHeight());
      var6.setTranslateX(-var7.getMinX());
      var6.setTranslateY(-var7.getMinY());
      if (!Double.isNaN(var25)) {
         super.setXInternal(var25);
      }

      if (!Double.isNaN(var23)) {
         super.setYInternal(var23);
      }

      this.anchorX.set(var17 + var13);
      this.anchorY.set(var19 + var15);
   }

   private Bounds union(Bounds var1, Bounds var2) {
      double var3 = Math.min(var1.getMinX(), var2.getMinX());
      double var5 = Math.min(var1.getMinY(), var2.getMinY());
      double var7 = Math.max(var1.getMaxX(), var2.getMaxX());
      double var9 = Math.max(var1.getMaxY(), var2.getMaxY());
      return new BoundingBox(var3, var5, var7 - var3, var9 - var5);
   }

   private double windowToAnchorX(double var1) {
      Bounds var3 = this.getAnchorBounds();
      return var1 - this.getExtendedBounds().getMinX() + var3.getMinX() + this.getAnchorLocation().getXCoef() * var3.getWidth();
   }

   private double windowToAnchorY(double var1) {
      Bounds var3 = this.getAnchorBounds();
      return var1 - this.getExtendedBounds().getMinY() + var3.getMinY() + this.getAnchorLocation().getYCoef() * var3.getHeight();
   }

   private static Window getRootWindow(Window var0) {
      while(var0 instanceof PopupWindow) {
         var0 = ((PopupWindow)var0).getOwnerWindow();
      }

      return var0;
   }

   void doAutoHide() {
      this.hide();
      if (this.getOnAutoHide() != null) {
         this.getOnAutoHide().handle(new Event(this, this, Event.ANY));
      }

   }

   WindowEventDispatcher createInternalEventDispatcher() {
      return new WindowEventDispatcher(new PopupEventRedirector(this), new WindowCloseRequestHandler(this), new EventHandlerManager(this));
   }

   Window getWindowOwner() {
      return this.getOwnerWindow();
   }

   private void startMonitorOwnerEvents(Window var1) {
      EventRedirector var2 = var1.getInternalEventDispatcher().getEventRedirector();
      var2.addEventDispatcher(this.getEventDispatcher());
   }

   private void stopMonitorOwnerEvents(Window var1) {
      EventRedirector var2 = var1.getInternalEventDispatcher().getEventRedirector();
      var2.removeEventDispatcher(this.getEventDispatcher());
   }

   private void bindOwnerFocusedProperty(Window var1) {
      this.ownerFocusedListener = (var1x, var2, var3) -> {
         this.setFocused(var3);
      };
      var1.focusedProperty().addListener(this.ownerFocusedListener);
   }

   private void unbindOwnerFocusedProperty(Window var1) {
      var1.focusedProperty().removeListener(this.ownerFocusedListener);
      this.ownerFocusedListener = null;
   }

   private void handleAutofixActivation(boolean var1, boolean var2) {
      boolean var3 = var1 && var2;
      if (this.autofixActive != var3) {
         this.autofixActive = var3;
         if (var3) {
            Screen.getScreens().addListener(this.popupWindowUpdater);
            this.updateWindow(this.getAnchorX(), this.getAnchorY());
         } else {
            Screen.getScreens().removeListener(this.popupWindowUpdater);
         }
      }

   }

   private void handleAutohideActivation(boolean var1, boolean var2) {
      boolean var3 = var1 && var2;
      if (this.autohideActive != var3) {
         this.autohideActive = var3;
         if (var3) {
            this.rootWindow.increaseFocusGrabCounter();
         } else {
            this.rootWindow.decreaseFocusGrabCounter();
         }
      }

   }

   private void validateOwnerWindow(Window var1) {
      if (var1 == null) {
         throw new NullPointerException("Owner window must not be null");
      } else if (wouldCreateCycle(var1, this)) {
         throw new IllegalArgumentException("Specified owner window would create cycle in the window hierarchy");
      } else if (this.isShowing() && this.getOwnerWindow() != var1) {
         throw new IllegalStateException("Popup is already shown with different owner window");
      }
   }

   private static boolean wouldCreateCycle(Window var0, Window var1) {
      while(var0 != null) {
         if (var0 == var1) {
            return true;
         }

         var0 = var0.getWindowOwner();
      }

      return false;
   }

   static class PopupEventRedirector extends EventRedirector {
      private static final KeyCombination ESCAPE_KEY_COMBINATION = KeyCombination.keyCombination("Esc");
      private final PopupWindow popupWindow;

      public PopupEventRedirector(PopupWindow var1) {
         super(var1);
         this.popupWindow = var1;
      }

      protected void handleRedirectedEvent(Object var1, Event var2) {
         if (var2 instanceof KeyEvent) {
            this.handleKeyEvent((KeyEvent)var2);
         } else {
            EventType var3 = var2.getEventType();
            if (var3 != MouseEvent.MOUSE_PRESSED && var3 != ScrollEvent.SCROLL) {
               if (var3 == FocusUngrabEvent.FOCUS_UNGRAB) {
                  this.handleFocusUngrabEvent();
               }
            } else {
               this.handleAutoHidingEvents(var1, var2);
            }
         }
      }

      private void handleKeyEvent(KeyEvent var1) {
         if (!var1.isConsumed()) {
            Scene var2 = this.popupWindow.getScene();
            if (var2 != null) {
               Node var3 = var2.getFocusOwner();
               Object var4 = var3 != null ? var3 : var2;
               if (EventUtil.fireEvent((EventTarget)var4, (Event)(new DirectEvent(var1.copyFor(this.popupWindow, (EventTarget)var4)))) == null) {
                  var1.consume();
                  return;
               }
            }

            if (var1.getEventType() == KeyEvent.KEY_PRESSED && ESCAPE_KEY_COMBINATION.match(var1)) {
               this.handleEscapeKeyPressedEvent(var1);
            }

         }
      }

      private void handleEscapeKeyPressedEvent(Event var1) {
         if (this.popupWindow.isHideOnEscape()) {
            this.popupWindow.doAutoHide();
            if (this.popupWindow.getConsumeAutoHidingEvents()) {
               var1.consume();
            }
         }

      }

      private void handleAutoHidingEvents(Object var1, Event var2) {
         if (this.popupWindow.getOwnerWindow() == var1) {
            if (this.popupWindow.isAutoHide() && !this.isOwnerNodeEvent(var2)) {
               Event.fireEvent(this.popupWindow, new FocusUngrabEvent());
               this.popupWindow.doAutoHide();
               if (this.popupWindow.getConsumeAutoHidingEvents()) {
                  var2.consume();
               }
            }

         }
      }

      private void handleFocusUngrabEvent() {
         if (this.popupWindow.isAutoHide()) {
            this.popupWindow.doAutoHide();
         }

      }

      private boolean isOwnerNodeEvent(Event var1) {
         Node var2 = this.popupWindow.getOwnerNode();
         if (var2 == null) {
            return false;
         } else {
            EventTarget var3 = var1.getTarget();
            if (!(var3 instanceof Node)) {
               return false;
            } else {
               Object var4 = (Node)var3;

               while(var4 != var2) {
                  var4 = ((Node)var4).getParent();
                  if (var4 == null) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public static enum AnchorLocation {
      WINDOW_TOP_LEFT(0.0, 0.0, false),
      WINDOW_TOP_RIGHT(1.0, 0.0, false),
      WINDOW_BOTTOM_LEFT(0.0, 1.0, false),
      WINDOW_BOTTOM_RIGHT(1.0, 1.0, false),
      CONTENT_TOP_LEFT(0.0, 0.0, true),
      CONTENT_TOP_RIGHT(1.0, 0.0, true),
      CONTENT_BOTTOM_LEFT(0.0, 1.0, true),
      CONTENT_BOTTOM_RIGHT(1.0, 1.0, true);

      private final double xCoef;
      private final double yCoef;
      private final boolean contentLocation;

      private AnchorLocation(double var3, double var5, boolean var7) {
         this.xCoef = var3;
         this.yCoef = var5;
         this.contentLocation = var7;
      }

      double getXCoef() {
         return this.xCoef;
      }

      double getYCoef() {
         return this.yCoef;
      }

      boolean isContentLocation() {
         return this.contentLocation;
      }
   }
}
