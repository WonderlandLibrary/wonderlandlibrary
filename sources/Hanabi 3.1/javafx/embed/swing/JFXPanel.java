package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindow;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;
import sun.awt.UngrabEvent;
import sun.awt.CausedFocusEvent.Cause;
import sun.java2d.SunGraphics2D;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class JFXPanel extends JComponent {
   private static final PlatformLogger log = PlatformLogger.getLogger(JFXPanel.class.getName());
   private static AtomicInteger instanceCount = new AtomicInteger(0);
   private static PlatformImpl.FinishListener finishListener;
   private HostContainer hostContainer;
   private volatile EmbeddedWindow stage;
   private volatile Scene scene;
   private SwingDnD dnd;
   private EmbeddedStageInterface stagePeer;
   private EmbeddedSceneInterface scenePeer;
   private int pWidth;
   private int pHeight;
   private int scaleFactor = 1;
   private volatile int pPreferredWidth = -1;
   private volatile int pPreferredHeight = -1;
   private volatile int screenX = 0;
   private volatile int screenY = 0;
   private BufferedImage pixelsIm;
   private volatile float opacity = 1.0F;
   private AtomicInteger disableCount = new AtomicInteger(0);
   private boolean isCapturingMouse = false;
   private static boolean fxInitialized;
   private final AWTEventListener ungrabListener = (var1) -> {
      if (var1 instanceof UngrabEvent) {
         SwingFXUtils.runOnFxThread(() -> {
            if (this.stagePeer != null) {
               this.stagePeer.focusUngrab();
            }

         });
      }

      if (var1 instanceof MouseEvent && var1.getID() == 501 && var1.getSource() instanceof Component) {
         Window var2 = SwingUtilities.getWindowAncestor(this);
         Component var3 = (Component)var1.getSource();
         Window var4 = var3 instanceof Window ? (Window)var3 : SwingUtilities.getWindowAncestor(var3);
         if (var2 == var4) {
            SwingFXUtils.runOnFxThread(() -> {
               if (this.stagePeer != null) {
                  this.stagePeer.focusUngrab();
               }

            });
         }
      }

   };

   private synchronized void registerFinishListener() {
      if (instanceCount.getAndIncrement() <= 0) {
         finishListener = new PlatformImpl.FinishListener() {
            public void idle(boolean var1) {
            }

            public void exitCalled() {
            }
         };
         PlatformImpl.addListener(finishListener);
      }
   }

   private synchronized void deregisterFinishListener() {
      if (instanceCount.decrementAndGet() <= 0) {
         PlatformImpl.removeListener(finishListener);
         finishListener = null;
      }
   }

   private static synchronized void initFx() {
      AccessController.doPrivileged(() -> {
         System.setProperty("glass.win.uiScale", "100%");
         System.setProperty("glass.win.minHiDPI", "9999");
         return null;
      });
      if (!fxInitialized) {
         Toolkit var10000 = Toolkit.getDefaultToolkit();
         var10000.getClass();
         EventQueue var0 = (EventQueue)AccessController.doPrivileged(var10000::getSystemEventQueue);
         if (EventQueue.isDispatchThread()) {
            SecondaryLoop var1 = var0.createSecondaryLoop();
            Throwable[] var2 = new Throwable[]{null};
            (new Thread(() -> {
               try {
                  PlatformImpl.startup(() -> {
                  });
               } catch (Throwable var6) {
                  var2[0] = var6;
               } finally {
                  var1.exit();
               }

            })).start();
            var1.enter();
            if (var2[0] != null) {
               if (var2[0] instanceof RuntimeException) {
                  throw (RuntimeException)var2[0];
               }

               if (var2[0] instanceof Error) {
                  throw (Error)var2[0];
               }

               throw new RuntimeException("FX initialization failed", var2[0]);
            }
         } else {
            PlatformImpl.startup(() -> {
            });
         }

         fxInitialized = true;
      }
   }

   public JFXPanel() {
      initFx();
      this.hostContainer = new HostContainer();
      this.enableEvents(231485L);
      this.setFocusable(true);
      this.setFocusTraversalKeysEnabled(false);
   }

   public Scene getScene() {
      return this.scene;
   }

   public void setScene(Scene var1) {
      if (com.sun.javafx.tk.Toolkit.getToolkit().isFxUserThread()) {
         this.setSceneImpl(var1);
      } else {
         CountDownLatch var2 = new CountDownLatch(1);
         Platform.runLater(() -> {
            this.setSceneImpl(var1);
            var2.countDown();
         });

         try {
            var2.await();
         } catch (InterruptedException var4) {
            var4.printStackTrace(System.err);
         }
      }

   }

   private void setSceneImpl(Scene var1) {
      if (this.stage != null && var1 == null) {
         this.stage.hide();
         this.stage = null;
      }

      this.scene = var1;
      if (this.stage == null && var1 != null) {
         this.stage = new EmbeddedWindow(this.hostContainer);
      }

      if (this.stage != null) {
         this.stage.setScene(var1);
         if (!this.stage.isShowing()) {
            this.stage.show();
         }
      }

   }

   public final void setOpaque(boolean var1) {
      if (!var1) {
         super.setOpaque(var1);
      }

   }

   public final boolean isOpaque() {
      return false;
   }

   private void sendMouseEventToFX(MouseEvent var1) {
      if (this.scenePeer != null && this.isFxEnabled()) {
         switch (var1.getID()) {
            case 501:
            case 502:
            case 506:
               if (var1.getButton() > 3) {
                  return;
               }
            default:
               int var2 = var1.getModifiersEx();
               boolean var3 = (var2 & 1024) != 0;
               boolean var4 = (var2 & 2048) != 0;
               boolean var5 = (var2 & 4096) != 0;
               if (var1.getID() == 506) {
                  if (!this.isCapturingMouse) {
                     return;
                  }
               } else if (var1.getID() == 501) {
                  this.isCapturingMouse = true;
               } else if (var1.getID() == 502) {
                  if (!this.isCapturingMouse) {
                     return;
                  }

                  this.isCapturingMouse = var3 || var4 || var5;
               } else if (var1.getID() == 500) {
                  return;
               }

               boolean var6 = false;
               if (var1.getID() == 501 || var1.getID() == 502) {
                  var6 = var1.isPopupTrigger();
               }

               this.scenePeer.mouseEvent(SwingEvents.mouseIDToEmbedMouseType(var1.getID()), SwingEvents.mouseButtonToEmbedMouseButton(var1.getButton(), var2), var3, var4, var5, var1.getX(), var1.getY(), var1.getXOnScreen(), var1.getYOnScreen(), (var2 & 64) != 0, (var2 & 128) != 0, (var2 & 512) != 0, (var2 & 256) != 0, SwingEvents.getWheelRotation(var1), var6);
               if (var1.isPopupTrigger()) {
                  this.scenePeer.menuEvent(var1.getX(), var1.getY(), var1.getXOnScreen(), var1.getYOnScreen(), false);
               }

         }
      }
   }

   protected void processMouseEvent(MouseEvent var1) {
      if (var1.getID() == 501 && var1.getButton() == 1 && !this.hasFocus()) {
         this.requestFocus();
      }

      this.sendMouseEventToFX(var1);
      super.processMouseEvent(var1);
   }

   protected void processMouseMotionEvent(MouseEvent var1) {
      this.sendMouseEventToFX(var1);
      super.processMouseMotionEvent(var1);
   }

   protected void processMouseWheelEvent(MouseWheelEvent var1) {
      this.sendMouseEventToFX(var1);
      super.processMouseWheelEvent(var1);
   }

   private void sendKeyEventToFX(KeyEvent var1) {
      if (this.scenePeer != null && this.isFxEnabled()) {
         char[] var2 = var1.getKeyChar() == '\uffff' ? new char[0] : new char[]{SwingEvents.keyCharToEmbedKeyChar(var1.getKeyChar())};
         this.scenePeer.keyEvent(SwingEvents.keyIDToEmbedKeyType(var1.getID()), var1.getKeyCode(), var2, SwingEvents.keyModifiersToEmbedKeyModifiers(var1.getModifiersEx()));
      }
   }

   protected void processKeyEvent(KeyEvent var1) {
      this.sendKeyEventToFX(var1);
      super.processKeyEvent(var1);
   }

   private void sendResizeEventToFX() {
      if (this.stagePeer != null) {
         this.stagePeer.setSize(this.pWidth, this.pHeight);
      }

      if (this.scenePeer != null) {
         this.scenePeer.setSize(this.pWidth, this.pHeight);
      }

   }

   protected void processComponentEvent(ComponentEvent var1) {
      switch (var1.getID()) {
         case 100:
            if (this.updateScreenLocation()) {
               this.sendMoveEventToFX();
            }
            break;
         case 101:
            this.updateComponentSize();
      }

      super.processComponentEvent(var1);
   }

   private void updateComponentSize() {
      int var1 = this.pWidth;
      int var2 = this.pHeight;
      this.pWidth = Math.max(0, this.getWidth());
      this.pHeight = Math.max(0, this.getHeight());
      if (this.getBorder() != null) {
         Insets var3 = this.getBorder().getBorderInsets(this);
         this.pWidth -= var3.left + var3.right;
         this.pHeight -= var3.top + var3.bottom;
      }

      if (var1 != this.pWidth || var2 != this.pHeight) {
         this.resizePixelBuffer(this.scaleFactor);
         this.sendResizeEventToFX();
      }

   }

   private boolean updateScreenLocation() {
      synchronized(this.getTreeLock()) {
         if (this.isShowing()) {
            Point var2 = this.getLocationOnScreen();
            this.screenX = var2.x;
            this.screenY = var2.y;
            return true;
         } else {
            return false;
         }
      }
   }

   private void sendMoveEventToFX() {
      if (this.stagePeer != null) {
         this.stagePeer.setLocation(this.screenX, this.screenY);
      }
   }

   protected void processHierarchyBoundsEvent(HierarchyEvent var1) {
      if (var1.getID() == 1401 && this.updateScreenLocation()) {
         this.sendMoveEventToFX();
      }

      super.processHierarchyBoundsEvent(var1);
   }

   protected void processHierarchyEvent(HierarchyEvent var1) {
      if ((var1.getChangeFlags() & 4L) != 0L && this.updateScreenLocation()) {
         this.sendMoveEventToFX();
      }

      super.processHierarchyEvent(var1);
   }

   private void sendFocusEventToFX(FocusEvent var1) {
      if (this.stage != null && this.stagePeer != null && this.isFxEnabled()) {
         boolean var2 = var1.getID() == 1004;
         int var3 = var2 ? 0 : 3;
         if (var2 && var1 instanceof CausedFocusEvent) {
            CausedFocusEvent var4 = (CausedFocusEvent)var1;
            if (var4.getCause() == Cause.TRAVERSAL_FORWARD) {
               var3 = 1;
            } else if (var4.getCause() == Cause.TRAVERSAL_BACKWARD) {
               var3 = 2;
            }
         }

         this.stagePeer.setFocused(var2, var3);
      }
   }

   protected void processFocusEvent(FocusEvent var1) {
      this.sendFocusEventToFX(var1);
      super.processFocusEvent(var1);
   }

   private void resizePixelBuffer(int var1) {
      if (this.pWidth > 0 && this.pHeight > 0) {
         BufferedImage var2 = this.pixelsIm;
         this.pixelsIm = new BufferedImage(this.pWidth * var1, this.pHeight * var1, 2);
         if (var2 != null) {
            double var3 = (double)(var1 / this.scaleFactor);
            int var5 = (int)Math.round((double)var2.getWidth() * var3);
            int var6 = (int)Math.round((double)var2.getHeight() * var3);
            Graphics var7 = this.pixelsIm.getGraphics();

            try {
               var7.drawImage(var2, 0, 0, var5, var6, (ImageObserver)null);
            } finally {
               var7.dispose();
            }
         }
      } else {
         this.pixelsIm = null;
      }

   }

   protected void processInputMethodEvent(InputMethodEvent var1) {
      if (var1.getID() == 1100) {
         this.sendInputMethodEventToFX(var1);
      }

      super.processInputMethodEvent(var1);
   }

   private void sendInputMethodEventToFX(InputMethodEvent var1) {
      String var2 = InputMethodSupport.getTextForEvent(var1);
      this.scenePeer.inputMethodEvent(javafx.scene.input.InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, InputMethodSupport.inputMethodEventComposed(var2, var1.getCommittedCharacterCount()), var2.substring(0, var1.getCommittedCharacterCount()), var1.getCaret().getInsertionIndex());
   }

   protected void paintComponent(Graphics var1) {
      if (this.scenePeer != null && this.pixelsIm != null) {
         DataBufferInt var2 = (DataBufferInt)this.pixelsIm.getRaster().getDataBuffer();
         int[] var3 = var2.getData();
         IntBuffer var4 = IntBuffer.wrap(var3);
         if (!this.scenePeer.getPixels(var4, this.pWidth, this.pHeight)) {
         }

         Graphics var5 = null;

         try {
            var5 = var1.create();
            if (this.opacity < 1.0F && var5 instanceof Graphics2D) {
               Graphics2D var6 = (Graphics2D)var5;
               AlphaComposite var7 = AlphaComposite.getInstance(3, this.opacity);
               var6.setComposite(var7);
            }

            if (this.getBorder() != null) {
               Insets var13 = this.getBorder().getBorderInsets(this);
               var5.translate(var13.left, var13.top);
            }

            var5.drawImage(this.pixelsIm, 0, 0, this.pWidth, this.pHeight, (ImageObserver)null);
            int var14 = this.scaleFactor;
            if (var1 instanceof SunGraphics2D) {
               var14 = ((SunGraphics2D)var1).surfaceData.getDefaultScale();
            }

            if (this.scaleFactor != var14) {
               this.resizePixelBuffer(var14);
               this.scenePeer.setPixelScaleFactors((float)var14, (float)var14);
               this.scaleFactor = var14;
            }
         } catch (Throwable var11) {
            var11.printStackTrace();
         } finally {
            if (var5 != null) {
               var5.dispose();
            }

         }

      }
   }

   public Dimension getPreferredSize() {
      return !this.isPreferredSizeSet() && this.scenePeer != null ? new Dimension(this.pPreferredWidth, this.pPreferredHeight) : super.getPreferredSize();
   }

   private boolean isFxEnabled() {
      return this.disableCount.get() == 0;
   }

   private void setFxEnabled(boolean var1) {
      if (!var1) {
         if (this.disableCount.incrementAndGet() == 1 && this.dnd != null) {
            this.dnd.removeNotify();
         }
      } else {
         if (this.disableCount.get() == 0) {
            return;
         }

         if (this.disableCount.decrementAndGet() == 0 && this.dnd != null) {
            this.dnd.addNotify();
         }
      }

   }

   public void addNotify() {
      super.addNotify();
      this.registerFinishListener();
      AccessController.doPrivileged(() -> {
         this.getToolkit().addAWTEventListener(this.ungrabListener, -2147483632L);
         return null;
      });
      this.updateComponentSize();
      SwingFXUtils.runOnFxThread(() -> {
         if (this.stage != null && !this.stage.isShowing()) {
            this.stage.show();
            this.sendMoveEventToFX();
         }

      });
   }

   public InputMethodRequests getInputMethodRequests() {
      return this.scenePeer == null ? null : new InputMethodSupport.InputMethodRequestsAdapter(this.scenePeer.getInputMethodRequests());
   }

   public void removeNotify() {
      SwingFXUtils.runOnFxThread(() -> {
         if (this.stage != null && this.stage.isShowing()) {
            this.stage.hide();
         }

      });
      this.pixelsIm = null;
      this.pWidth = 0;
      this.pHeight = 0;
      super.removeNotify();
      AccessController.doPrivileged(() -> {
         this.getToolkit().removeAWTEventListener(this.ungrabListener);
         return null;
      });
      this.getInputContext().removeNotify(this);
      this.deregisterFinishListener();
   }

   private void invokeOnClientEDT(Runnable var1) {
      AppContext var2 = SunToolkit.targetToAppContext(this);
      if (var2 == null) {
         if (log.isLoggable(Level.FINE)) {
            log.fine("null AppContext encountered!");
         }

      } else {
         SunToolkit.postEvent(var2, new InvocationEvent(this, var1));
      }
   }

   private class HostContainer implements HostInterface {
      private HostContainer() {
      }

      public void setEmbeddedStage(EmbeddedStageInterface var1) {
         JFXPanel.this.stagePeer = var1;
         if (JFXPanel.this.stagePeer != null) {
            if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
               JFXPanel.this.stagePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
            }

            JFXPanel.this.invokeOnClientEDT(() -> {
               if (JFXPanel.this.stagePeer != null && JFXPanel.this.isFocusOwner()) {
                  JFXPanel.this.stagePeer.setFocused(true, 0);
               }

            });
            JFXPanel.this.sendMoveEventToFX();
         }
      }

      public void setEmbeddedScene(EmbeddedSceneInterface var1) {
         if (JFXPanel.this.scenePeer != var1) {
            JFXPanel.this.scenePeer = var1;
            if (JFXPanel.this.scenePeer == null) {
               JFXPanel.this.invokeOnClientEDT(() -> {
                  if (JFXPanel.this.dnd != null) {
                     JFXPanel.this.dnd.removeNotify();
                     JFXPanel.this.dnd = null;
                  }

               });
            } else {
               if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
                  JFXPanel.this.scenePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
               }

               JFXPanel.this.scenePeer.setPixelScaleFactors((float)JFXPanel.this.scaleFactor, (float)JFXPanel.this.scaleFactor);
               JFXPanel.this.invokeOnClientEDT(() -> {
                  JFXPanel.this.dnd = new SwingDnD(JFXPanel.this, JFXPanel.this.scenePeer);
                  JFXPanel.this.dnd.addNotify();
                  if (JFXPanel.this.scenePeer != null) {
                     JFXPanel.this.scenePeer.setDragStartListener(JFXPanel.this.dnd.getDragStartListener());
                  }

               });
            }
         }
      }

      public boolean requestFocus() {
         return JFXPanel.this.requestFocusInWindow();
      }

      public boolean traverseFocusOut(boolean var1) {
         KeyboardFocusManager var2 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
         if (var1) {
            var2.focusNextComponent(JFXPanel.this);
         } else {
            var2.focusPreviousComponent(JFXPanel.this);
         }

         return true;
      }

      public void setPreferredSize(int var1, int var2) {
         JFXPanel.this.invokeOnClientEDT(() -> {
            JFXPanel.this.pPreferredWidth = var1;
            JFXPanel.this.pPreferredHeight = var2;
            JFXPanel.this.revalidate();
         });
      }

      public void repaint() {
         JFXPanel.this.invokeOnClientEDT(() -> {
            JFXPanel.this.repaint();
         });
      }

      public void setEnabled(boolean var1) {
         JFXPanel.this.setFxEnabled(var1);
      }

      public void setCursor(CursorFrame var1) {
         Cursor var2 = this.getPlatformCursor(var1);
         JFXPanel.this.invokeOnClientEDT(() -> {
            JFXPanel.this.setCursor(var2);
         });
      }

      private Cursor getPlatformCursor(CursorFrame var1) {
         Cursor var2 = (Cursor)var1.getPlatformCursor(Cursor.class);
         if (var2 != null) {
            return var2;
         } else {
            Cursor var3 = SwingCursors.embedCursorToCursor(var1);
            var1.setPlatforCursor(Cursor.class, var3);
            return var3;
         }
      }

      public boolean grabFocus() {
         if (PlatformUtil.isLinux()) {
            return true;
         } else {
            JFXPanel.this.invokeOnClientEDT(() -> {
               Window var1 = SwingUtilities.getWindowAncestor(JFXPanel.this);
               if (var1 != null && JFXPanel.this.getToolkit() instanceof SunToolkit) {
                  ((SunToolkit)JFXPanel.this.getToolkit()).grab(var1);
               }

            });
            return true;
         }
      }

      public void ungrabFocus() {
         if (!PlatformUtil.isLinux()) {
            JFXPanel.this.invokeOnClientEDT(() -> {
               Window var1 = SwingUtilities.getWindowAncestor(JFXPanel.this);
               if (var1 != null && JFXPanel.this.getToolkit() instanceof SunToolkit) {
                  ((SunToolkit)JFXPanel.this.getToolkit()).ungrab(var1);
               }

            });
         }
      }

      // $FF: synthetic method
      HostContainer(Object var2) {
         this();
      }
   }
}
