package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.embed.swing.Disposer;
import com.sun.javafx.embed.swing.DisposerRecord;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGExternalNode;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.TKStage;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowFocusListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javax.swing.JComponent;
import sun.awt.UngrabEvent;
import sun.swing.JLightweightFrame;
import sun.swing.LightweightContent;

public class SwingNode extends Node {
   private static boolean isThreadMerged;
   private double fxWidth;
   private double fxHeight;
   private int swingPrefWidth;
   private int swingPrefHeight;
   private int swingMaxWidth;
   private int swingMaxHeight;
   private int swingMinWidth;
   private int swingMinHeight;
   private volatile JComponent content;
   private volatile JLightweightFrame lwFrame;
   private NGExternalNode peer;
   private final ReentrantLock paintLock = new ReentrantLock();
   private boolean skipBackwardUnrgabNotification;
   private boolean grabbed;
   private volatile int scale = 1;
   private EventHandler windowHiddenHandler = (var1) -> {
      if (this.lwFrame != null && var1.getTarget() instanceof Window) {
         Window var2 = (Window)var1.getTarget();
         TKStage var3 = var2.impl_getPeer();
         if (var3 != null) {
            if (isThreadMerged) {
               jlfOverrideNativeWindowHandle.invoke(this.lwFrame, 0L, null);
            } else {
               var3.postponeClose();
               SwingFXUtils.runOnEDT(() -> {
                  jlfOverrideNativeWindowHandle.invoke(this.lwFrame, 0L, () -> {
                     SwingFXUtils.runOnFxThread(() -> {
                        var3.closePostponed();
                     });
                  });
               });
            }
         }
      }

   };
   private Window hWindow = null;
   private static final OptionalMethod jlfNotifyDisplayChanged;
   private static OptionalMethod jlfOverrideNativeWindowHandle;
   private List peerRequests = new ArrayList();
   private final InvalidationListener locationListener = (var1) -> {
      this.locateLwFrame();
   };
   private final EventHandler ungrabHandler = (var1) -> {
      if (!this.skipBackwardUnrgabNotification && this.lwFrame != null) {
         AccessController.doPrivileged(new PostEventAction(new UngrabEvent(this.lwFrame)));
      }

   };
   private final ChangeListener windowVisibleListener = (var1, var2, var3) -> {
      if (!var3) {
         this.disposeLwFrame();
      } else {
         this.setContent(this.content);
      }

   };
   private final ChangeListener sceneWindowListener = (var1, var2, var3) -> {
      if (var2 != null) {
         this.removeWindowListeners(var2);
      }

      this.notifyNativeHandle(var3);
      if (var3 != null) {
         this.addWindowListeners(var3);
      }

   };
   private static final OptionalMethod jlfSetHostBounds;

   final JLightweightFrame getLightweightFrame() {
      return this.lwFrame;
   }

   public SwingNode() {
      this.setFocusTraversable(true);
      this.setEventHandler(MouseEvent.ANY, new SwingMouseEventHandler());
      this.setEventHandler(KeyEvent.ANY, new SwingKeyEventHandler());
      this.setEventHandler(ScrollEvent.SCROLL, new SwingScrollEventHandler());
      this.focusedProperty().addListener((var1, var2, var3) -> {
         this.activateLwFrame(var3);
      });
      Font.getFamilies();
   }

   private void notifyNativeHandle(Window var1) {
      if (this.hWindow != var1) {
         if (this.hWindow != null) {
            this.hWindow.removeEventHandler(WindowEvent.WINDOW_HIDDEN, this.windowHiddenHandler);
         }

         if (var1 != null) {
            var1.addEventHandler(WindowEvent.WINDOW_HIDDEN, this.windowHiddenHandler);
         }

         this.hWindow = var1;
      }

      if (this.lwFrame != null) {
         long var2 = 0L;
         if (var1 != null) {
            TKStage var4 = var1.impl_getPeer();
            if (var4 != null) {
               var2 = var4.getRawHandle();
            }
         }

         jlfOverrideNativeWindowHandle.invoke(this.lwFrame, var2, null);
      }

   }

   public void setContent(JComponent var1) {
      this.content = var1;
      SwingFXUtils.runOnEDT(() -> {
         this.setContentImpl(var1);
      });
   }

   public JComponent getContent() {
      return this.content;
   }

   private void setContentImpl(JComponent var1) {
      if (this.lwFrame != null) {
         this.lwFrame.dispose();
         this.lwFrame = null;
      }

      if (var1 != null) {
         this.lwFrame = new JLightweightFrame();
         SwingNodeWindowFocusListener var2 = new SwingNodeWindowFocusListener(this);
         this.lwFrame.addWindowFocusListener(var2);
         jlfNotifyDisplayChanged.invoke(this.lwFrame, this.scale);
         this.lwFrame.setContent(new SwingNodeContent(var1, this));
         this.lwFrame.setVisible(true);
         if (this.getScene() != null) {
            this.notifyNativeHandle(this.getScene().getWindow());
         }

         SwingNodeDisposer var3 = new SwingNodeDisposer(this.lwFrame);
         Disposer.addRecord(this, var3);
         SwingFXUtils.runOnFxThread(() -> {
            this.locateLwFrame();
            if (this.focusedProperty().get()) {
               this.activateLwFrame(true);
            }

         });
      }

   }

   private static float getRenderScale(Window var0) {
      return WindowHelper.getWindowAccessor().getRenderScale(var0);
   }

   private static float getPlatformScaleX(Window var0) {
      return WindowHelper.getWindowAccessor().getPlatformScaleX(var0);
   }

   private static float getPlatformScaleY(Window var0) {
      return WindowHelper.getWindowAccessor().getPlatformScaleY(var0);
   }

   private float getPlatformScaleX() {
      return getPlatformScaleX(this.getScene().getWindow());
   }

   private float getPlatformScaleY() {
      return getPlatformScaleY(this.getScene().getWindow());
   }

   void setImageBuffer(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      Runnable var8 = () -> {
         Window var8 = this.getScene().getWindow();
         float var9 = getPlatformScaleX(var8);
         float var10 = getPlatformScaleY(var8);
         this.peer.setImageBuffer(IntBuffer.wrap(var1), var2, var3, var4, var5, (float)var4 / var9, (float)var5 / var10, var6, var7);
      };
      SwingFXUtils.runOnFxThread(() -> {
         if (this.peer != null) {
            var8.run();
         } else {
            this.peerRequests.clear();
            this.peerRequests.add(var8);
         }

      });
   }

   void setImageBounds(int var1, int var2, int var3, int var4) {
      Runnable var5 = () -> {
         Window var5 = this.getScene().getWindow();
         float var6 = getPlatformScaleX(var5);
         float var7 = getPlatformScaleY(var5);
         this.peer.setImageBounds(var1, var2, var3, var4, (float)var3 / var6, (float)var4 / var7);
      };
      SwingFXUtils.runOnFxThread(() -> {
         if (this.peer != null) {
            var5.run();
         } else {
            this.peerRequests.add(var5);
         }

      });
   }

   void repaintDirtyRegion(int var1, int var2, int var3, int var4) {
      Runnable var5 = () -> {
         this.peer.repaintDirtyRegion(var1, var2, var3, var4);
         this.impl_markDirty(DirtyBits.NODE_CONTENTS);
      };
      SwingFXUtils.runOnFxThread(() -> {
         if (this.peer != null) {
            var5.run();
         } else {
            this.peerRequests.add(var5);
         }

      });
   }

   public boolean isResizable() {
      return true;
   }

   public void resize(double var1, double var3) {
      super.resize(var1, var3);
      if (var1 != this.fxWidth || var3 != this.fxHeight) {
         this.fxWidth = var1;
         this.fxHeight = var3;
         this.impl_geomChanged();
         this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
               this.locateLwFrame();
            }

         });
      }

   }

   public double prefWidth(double var1) {
      return (double)((float)this.swingPrefWidth / this.getPlatformScaleX());
   }

   public double prefHeight(double var1) {
      return (double)((float)this.swingPrefHeight / this.getPlatformScaleY());
   }

   public double maxWidth(double var1) {
      return (double)((float)this.swingMaxWidth / this.getPlatformScaleX());
   }

   public double maxHeight(double var1) {
      return (double)((float)this.swingMaxHeight / this.getPlatformScaleY());
   }

   public double minWidth(double var1) {
      return (double)((float)this.swingMinWidth / this.getPlatformScaleX());
   }

   public double minHeight(double var1) {
      return (double)((float)this.swingMinHeight / this.getPlatformScaleY());
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return true;
   }

   private void removeSceneListeners(Scene var1) {
      Window var2 = var1.getWindow();
      if (var2 != null) {
         this.removeWindowListeners(var2);
      }

      var1.windowProperty().removeListener(this.sceneWindowListener);
   }

   private void addSceneListeners(Scene var1) {
      Window var2 = var1.getWindow();
      if (var2 != null) {
         this.addWindowListeners(var2);
         this.notifyNativeHandle(var2);
      }

      var1.windowProperty().addListener(this.sceneWindowListener);
   }

   private void addWindowListeners(Window var1) {
      var1.xProperty().addListener(this.locationListener);
      var1.yProperty().addListener(this.locationListener);
      var1.widthProperty().addListener(this.locationListener);
      var1.heightProperty().addListener(this.locationListener);
      var1.addEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
      var1.showingProperty().addListener(this.windowVisibleListener);
      this.scale = Math.round(getRenderScale(var1));
      this.setLwFrameScale(this.scale);
   }

   private void removeWindowListeners(Window var1) {
      var1.xProperty().removeListener(this.locationListener);
      var1.yProperty().removeListener(this.locationListener);
      var1.widthProperty().removeListener(this.locationListener);
      var1.heightProperty().removeListener(this.locationListener);
      var1.removeEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
      var1.showingProperty().removeListener(this.windowVisibleListener);
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      this.peer = new NGExternalNode();
      this.peer.setLock(this.paintLock);
      Iterator var1 = this.peerRequests.iterator();

      while(var1.hasNext()) {
         Runnable var2 = (Runnable)var1.next();
         var2.run();
      }

      this.peerRequests = null;
      if (this.getScene() != null) {
         this.addSceneListeners(this.getScene());
      }

      this.sceneProperty().addListener((var1x, var2x, var3) -> {
         if (var2x != null) {
            this.removeSceneListeners(var2x);
            this.disposeLwFrame();
         }

         if (var3 != null) {
            if (this.content != null && this.lwFrame == null) {
               this.setContent(this.content);
            }

            this.addSceneListeners(var3);
         }

      });
      this.impl_treeVisibleProperty().addListener((var1x, var2x, var3) -> {
         this.setLwFrameVisible(var3);
      });
      return this.peer;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_VISIBLE) || this.impl_isDirty(DirtyBits.NODE_BOUNDS)) {
         this.locateLwFrame();
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         this.peer.markContentDirty();
      }

   }

   private void locateLwFrame() {
      if (this.getScene() != null && this.lwFrame != null && this.getScene().getWindow() != null && this.getScene().getWindow().isShowing()) {
         Window var1 = this.getScene().getWindow();
         float var2 = getRenderScale(var1);
         float var3 = getPlatformScaleX(var1);
         float var4 = getPlatformScaleY(var1);
         int var5 = Math.round(var2);
         boolean var6 = this.scale != var5;
         this.scale = var5;
         Point2D var7 = this.localToScene(0.0, 0.0);
         int var8 = (int)(var1.getX() * (double)var3);
         int var9 = (int)(var1.getY() * (double)var4);
         int var10 = (int)(var1.getWidth() * (double)var3);
         int var11 = (int)(var1.getHeight() * (double)var4);
         int var12 = (int)Math.round((var1.getX() + this.getScene().getX() + var7.getX()) * (double)var3);
         int var13 = (int)Math.round((var1.getY() + this.getScene().getY() + var7.getY()) * (double)var4);
         int var14 = (int)(this.fxWidth * (double)var3);
         int var15 = (int)(this.fxHeight * (double)var4);
         SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
               if (var6) {
                  jlfNotifyDisplayChanged.invoke(this.lwFrame, this.scale);
               }

               this.lwFrame.setSize(var14, var15);
               this.lwFrame.setLocation(var12, var13);
               jlfSetHostBounds.invoke(this.lwFrame, var8, var9, var10, var11);
            }

         });
      }
   }

   private void activateLwFrame(boolean var1) {
      if (this.lwFrame != null) {
         SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
               this.lwFrame.emulateActivation(var1);
            }

         });
      }
   }

   private void disposeLwFrame() {
      if (this.lwFrame != null) {
         SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
               this.lwFrame.dispose();
               this.lwFrame = null;
            }

         });
      }
   }

   private void setLwFrameVisible(boolean var1) {
      if (this.lwFrame != null) {
         SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
               this.lwFrame.setVisible(var1);
            }

         });
      }
   }

   private void setLwFrameScale(final int var1) {
      if (this.lwFrame != null) {
         SwingFXUtils.runOnEDT(new Runnable() {
            public void run() {
               if (SwingNode.this.lwFrame != null) {
                  SwingNode.jlfNotifyDisplayChanged.invoke(SwingNode.this.lwFrame, var1);
               }

            }
         });
      }
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      var1.deriveWithNewBounds(0.0F, 0.0F, 0.0F, (float)this.fxWidth, (float)this.fxHeight, 0.0F);
      var2.transform(var1, var1);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processLeafNode(this, var2);
   }

   private void ungrabFocus(boolean var1) {
      if (!PlatformUtil.isLinux()) {
         if (this.grabbed && this.getScene() != null && this.getScene().getWindow() != null && this.getScene().getWindow().impl_getPeer() != null) {
            this.skipBackwardUnrgabNotification = !var1;
            this.getScene().getWindow().impl_getPeer().ungrabFocus();
            this.skipBackwardUnrgabNotification = false;
            this.grabbed = false;
         }

      }
   }

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            SwingNode.isThreadMerged = Boolean.valueOf(System.getProperty("javafx.embed.singleThread"));
            return null;
         }
      });
      jlfNotifyDisplayChanged = new OptionalMethod(JLightweightFrame.class, "notifyDisplayChanged", new Class[]{Integer.TYPE});
      jlfOverrideNativeWindowHandle = new OptionalMethod(JLightweightFrame.class, "overrideNativeWindowHandle", new Class[]{Long.TYPE, Runnable.class});
      jlfSetHostBounds = new OptionalMethod(JLightweightFrame.class, "setHostBounds", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
   }

   private class SwingKeyEventHandler implements EventHandler {
      private SwingKeyEventHandler() {
      }

      public void handle(KeyEvent var1) {
         JLightweightFrame var2 = SwingNode.this.lwFrame;
         if (var2 != null) {
            if (!var1.getCharacter().isEmpty()) {
               if (var1.getCode() == KeyCode.LEFT || var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.UP || var1.getCode() == KeyCode.DOWN || var1.getCode() == KeyCode.TAB) {
                  var1.consume();
               }

               int var3 = SwingEvents.fxKeyEventTypeToKeyID(var1);
               if (var3 >= 0) {
                  int var4 = SwingEvents.fxKeyModsToKeyMods(var1);
                  int var5 = var1.getCode().impl_getCode();
                  char var6 = var1.getCharacter().charAt(0);
                  if (var1.getEventType() == KeyEvent.KEY_PRESSED) {
                     String var7 = var1.getText();
                     if (var7.length() == 1) {
                        var6 = var7.charAt(0);
                     }
                  }

                  long var10 = System.currentTimeMillis();
                  java.awt.event.KeyEvent var9 = new java.awt.event.KeyEvent(var2, var3, var10, var4, var5, var6);
                  AccessController.doPrivileged(SwingNode.this.new PostEventAction(var9));
               }
            }
         }
      }

      // $FF: synthetic method
      SwingKeyEventHandler(Object var2) {
         this();
      }
   }

   private class SwingScrollEventHandler implements EventHandler {
      private SwingScrollEventHandler() {
      }

      public void handle(ScrollEvent var1) {
         JLightweightFrame var2 = SwingNode.this.lwFrame;
         if (var2 != null) {
            int var3 = SwingEvents.fxScrollModsToMouseWheelMods(var1);
            boolean var4 = (var3 & 64) != 0;
            if (!var4 && var1.getDeltaY() != 0.0) {
               this.sendMouseWheelEvent(var2, var1.getX(), var1.getY(), var3, var1.getDeltaY() / var1.getMultiplierY());
            }

            double var5 = var4 && var1.getDeltaY() != 0.0 ? var1.getDeltaY() / var1.getMultiplierY() : var1.getDeltaX() / var1.getMultiplierX();
            if (var5 != 0.0) {
               var3 |= 64;
               this.sendMouseWheelEvent(var2, var1.getX(), var1.getY(), var3, var5);
            }

         }
      }

      private void sendMouseWheelEvent(Component var1, double var2, double var4, int var6, double var7) {
         int var9 = (int)var7;
         int var10 = (int)Math.signum(var7);
         if ((double)var10 * var7 < 1.0) {
            var9 = var10;
         }

         Window var11 = SwingNode.this.getScene().getWindow();
         float var12 = SwingNode.getPlatformScaleX(var11);
         float var13 = SwingNode.getPlatformScaleY(var11);
         int var14 = (int)Math.round(var2 * (double)var12);
         int var15 = (int)Math.round(var4 * (double)var13);
         MouseWheelEvent var16 = new MouseWheelEvent(var1, 507, System.currentTimeMillis(), var6, var14, var15, 0, 0, 0, false, 0, 1, -var9);
         AccessController.doPrivileged(SwingNode.this.new PostEventAction(var16));
      }

      // $FF: synthetic method
      SwingScrollEventHandler(Object var2) {
         this();
      }
   }

   private class SwingMouseEventHandler implements EventHandler {
      private final Set mouseClickedAllowed;

      private SwingMouseEventHandler() {
         this.mouseClickedAllowed = new HashSet();
      }

      public void handle(MouseEvent var1) {
         JLightweightFrame var2 = SwingNode.this.lwFrame;
         if (var2 != null) {
            int var3 = SwingEvents.fxMouseEventTypeToMouseID(var1);
            if (var3 >= 0) {
               var1.consume();
               EventType var4 = var1.getEventType();
               if (var4 == MouseEvent.MOUSE_PRESSED) {
                  this.mouseClickedAllowed.add(var1.getButton());
               } else if (var4 != MouseEvent.MOUSE_RELEASED) {
                  if (var4 == MouseEvent.MOUSE_DRAGGED) {
                     this.mouseClickedAllowed.clear();
                  } else if (var4 == MouseEvent.MOUSE_CLICKED) {
                     if (var1.getClickCount() == 1 && !this.mouseClickedAllowed.contains(var1.getButton())) {
                        return;
                     }

                     this.mouseClickedAllowed.remove(var1.getButton());
                  }
               }

               int var5 = SwingEvents.fxMouseModsToMouseMods(var1);
               boolean var6 = var1.isPopupTrigger();
               int var7 = SwingEvents.fxMouseButtonToMouseButton(var1);
               long var8 = System.currentTimeMillis();
               Window var10 = SwingNode.this.getScene().getWindow();
               float var11 = SwingNode.getPlatformScaleX(var10);
               float var12 = SwingNode.getPlatformScaleY(var10);
               int var13 = (int)Math.round(var1.getX() * (double)var11);
               int var14 = (int)Math.round(var1.getY() * (double)var12);
               int var15 = (int)Math.round(var1.getScreenX() * (double)var11);
               int var16 = (int)Math.round(var1.getScreenY() * (double)var12);
               java.awt.event.MouseEvent var17 = new java.awt.event.MouseEvent(var2, var3, var8, var5, var13, var14, var15, var16, var1.getClickCount(), var6, var7);
               AccessController.doPrivileged(SwingNode.this.new PostEventAction(var17));
            }
         }
      }

      // $FF: synthetic method
      SwingMouseEventHandler(Object var2) {
         this();
      }
   }

   private class PostEventAction implements PrivilegedAction {
      private AWTEvent event;

      public PostEventAction(AWTEvent var2) {
         this.event = var2;
      }

      public Void run() {
         EventQueue var1 = Toolkit.getDefaultToolkit().getSystemEventQueue();
         var1.postEvent(this.event);
         return null;
      }
   }

   private static class SwingNodeContent implements LightweightContent {
      private JComponent comp;
      private volatile FXDnD dnd;
      private WeakReference swingNodeRef;

      public SwingNodeContent(JComponent var1, SwingNode var2) {
         this.comp = var1;
         this.swingNodeRef = new WeakReference(var2);
      }

      public JComponent getComponent() {
         return this.comp;
      }

      public void paintLock() {
         SwingNode var1 = (SwingNode)this.swingNodeRef.get();
         if (var1 != null) {
            var1.paintLock.lock();
         }

      }

      public void paintUnlock() {
         SwingNode var1 = (SwingNode)this.swingNodeRef.get();
         if (var1 != null) {
            var1.paintLock.unlock();
         }

      }

      public void imageBufferReset(int[] var1, int var2, int var3, int var4, int var5, int var6) {
         this.imageBufferReset(var1, var2, var3, var4, var5, var6, 1);
      }

      public void imageBufferReset(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
         SwingNode var8 = (SwingNode)this.swingNodeRef.get();
         if (var8 != null) {
            var8.setImageBuffer(var1, var2, var3, var4, var5, var6, var7);
         }

      }

      public void imageReshaped(int var1, int var2, int var3, int var4) {
         SwingNode var5 = (SwingNode)this.swingNodeRef.get();
         if (var5 != null) {
            var5.setImageBounds(var1, var2, var3, var4);
         }

      }

      public void imageUpdated(int var1, int var2, int var3, int var4) {
         SwingNode var5 = (SwingNode)this.swingNodeRef.get();
         if (var5 != null) {
            var5.repaintDirtyRegion(var1, var2, var3, var4);
         }

      }

      public void focusGrabbed() {
         SwingFXUtils.runOnFxThread(() -> {
            if (!PlatformUtil.isLinux()) {
               SwingNode var1 = (SwingNode)this.swingNodeRef.get();
               if (var1 != null) {
                  Scene var2 = var1.getScene();
                  if (var2 != null && var2.getWindow() != null && var2.getWindow().impl_getPeer() != null) {
                     var2.getWindow().impl_getPeer().grabFocus();
                     var1.grabbed = true;
                  }
               }

            }
         });
      }

      public void focusUngrabbed() {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var1 = (SwingNode)this.swingNodeRef.get();
            if (var1 != null) {
               var1.ungrabFocus(false);
            }

         });
      }

      public void preferredSizeChanged(int var1, int var2) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var3 = (SwingNode)this.swingNodeRef.get();
            if (var3 != null) {
               var3.swingPrefWidth = var1;
               var3.swingPrefHeight = var2;
               var3.impl_notifyLayoutBoundsChanged();
            }

         });
      }

      public void maximumSizeChanged(int var1, int var2) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var3 = (SwingNode)this.swingNodeRef.get();
            if (var3 != null) {
               var3.swingMaxWidth = var1;
               var3.swingMaxHeight = var2;
               var3.impl_notifyLayoutBoundsChanged();
            }

         });
      }

      public void minimumSizeChanged(int var1, int var2) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var3 = (SwingNode)this.swingNodeRef.get();
            if (var3 != null) {
               var3.swingMinWidth = var1;
               var3.swingMinHeight = var2;
               var3.impl_notifyLayoutBoundsChanged();
            }

         });
      }

      public void setCursor(Cursor var1) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var2 = (SwingNode)this.swingNodeRef.get();
            if (var2 != null) {
               var2.setCursor(SwingCursors.embedCursorToCursor(var1));
            }

         });
      }

      private void initDnD() {
         synchronized(this) {
            if (this.dnd == null) {
               SwingNode var2 = (SwingNode)this.swingNodeRef.get();
               if (var2 != null) {
                  this.dnd = new FXDnD(var2);
               }
            }

         }
      }

      public synchronized DragGestureRecognizer createDragGestureRecognizer(Class var1, DragSource var2, Component var3, int var4, DragGestureListener var5) {
         this.initDnD();
         return this.dnd.createDragGestureRecognizer(var1, var2, var3, var4, var5);
      }

      public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent var1) throws InvalidDnDOperationException {
         this.initDnD();
         return this.dnd.createDragSourceContextPeer(var1);
      }

      public void addDropTarget(DropTarget var1) {
         this.initDnD();
         this.dnd.addDropTarget(var1);
      }

      public void removeDropTarget(DropTarget var1) {
         this.initDnD();
         this.dnd.removeDropTarget(var1);
      }
   }

   private static class SwingNodeWindowFocusListener implements WindowFocusListener {
      private WeakReference swingNodeRef;

      SwingNodeWindowFocusListener(SwingNode var1) {
         this.swingNodeRef = new WeakReference(var1);
      }

      public void windowGainedFocus(java.awt.event.WindowEvent var1) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var1 = (SwingNode)this.swingNodeRef.get();
            if (var1 != null) {
               var1.requestFocus();
            }

         });
      }

      public void windowLostFocus(java.awt.event.WindowEvent var1) {
         SwingFXUtils.runOnFxThread(() -> {
            SwingNode var1 = (SwingNode)this.swingNodeRef.get();
            if (var1 != null) {
               var1.ungrabFocus(true);
            }

         });
      }
   }

   private static class SwingNodeDisposer implements DisposerRecord {
      JLightweightFrame lwFrame;

      SwingNodeDisposer(JLightweightFrame var1) {
         this.lwFrame = var1;
      }

      public void dispose() {
         if (this.lwFrame != null) {
            this.lwFrame.dispose();
            this.lwFrame = null;
         }

      }
   }

   private static final class OptionalMethod {
      private final Method method;

      public OptionalMethod(Class var1, String var2, Class... var3) {
         Method var4;
         try {
            var4 = var1.getMethod(var2, var3);
         } catch (NoSuchMethodException var6) {
            var4 = null;
         } catch (Throwable var7) {
            throw new RuntimeException("Error when calling " + var1.getName() + ".getMethod('" + var2 + "').", var7);
         }

         this.method = var4;
      }

      public boolean isSupported() {
         return this.method != null;
      }

      public Object invoke(Object var1, Object... var2) {
         if (this.method != null) {
            try {
               return this.method.invoke(var1, var2);
            } catch (Throwable var4) {
               throw new RuntimeException("Error when calling " + var1.getClass().getName() + "." + this.method.getName() + "().", var4);
            }
         } else {
            return null;
         }
      }
   }
}
