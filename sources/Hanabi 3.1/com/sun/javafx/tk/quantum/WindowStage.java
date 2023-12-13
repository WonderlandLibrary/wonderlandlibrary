package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class WindowStage extends GlassStage {
   protected Window platformWindow;
   protected Stage fxStage;
   private StageStyle style;
   private GlassStage owner = null;
   private Modality modality;
   private final boolean securityDialog;
   private OverlayWarning warning;
   private boolean rtl;
   private boolean transparent;
   private boolean isPrimaryStage;
   private boolean isAppletStage;
   private boolean isPopupStage;
   private boolean isInFullScreen;
   private boolean isAlwaysOnTop;
   private boolean inAllowedEventHandler;
   private static List activeWindows = new LinkedList();
   private static Map platformWindows = new HashMap();
   private static GlassAppletWindow appletWindow = null;
   private static final Locale LOCALE = Locale.getDefault();
   private static final ResourceBundle RESOURCES;
   private static final Permission fullScreenPermission;
   private static final Permission alwaysOnTopPermission;
   private boolean fullScreenFromUserEvent;
   private KeyCombination savedFullScreenExitKey;
   private boolean isClosePostponed;
   private Window deadWindow;

   static void setAppletWindow(GlassAppletWindow var0) {
      appletWindow = var0;
   }

   static GlassAppletWindow getAppletWindow() {
      return appletWindow;
   }

   public WindowStage(javafx.stage.Window var1, boolean var2, StageStyle var3, Modality var4, TKStage var5) {
      this.modality = Modality.NONE;
      this.warning = null;
      this.rtl = false;
      this.transparent = false;
      this.isPrimaryStage = false;
      this.isAppletStage = false;
      this.isPopupStage = false;
      this.isInFullScreen = false;
      this.isAlwaysOnTop = false;
      this.inAllowedEventHandler = false;
      this.fullScreenFromUserEvent = false;
      this.savedFullScreenExitKey = null;
      this.isClosePostponed = false;
      this.deadWindow = null;
      this.style = var3;
      this.owner = (GlassStage)var5;
      this.modality = var4;
      this.securityDialog = var2;
      if (var1 instanceof Stage) {
         this.fxStage = (Stage)var1;
      } else {
         this.fxStage = null;
      }

      this.transparent = var3 == StageStyle.TRANSPARENT;
      if (var5 == null && this.modality == Modality.WINDOW_MODAL) {
         this.modality = Modality.NONE;
      }

   }

   final void setIsPrimary() {
      this.isPrimaryStage = true;
      if (appletWindow != null) {
         this.isAppletStage = true;
      }

   }

   final void setIsPopup() {
      this.isPopupStage = true;
   }

   final boolean isSecurityDialog() {
      return this.securityDialog;
   }

   public final WindowStage init(GlassSystemMenu var1) {
      this.initPlatformWindow();
      this.platformWindow.setEventHandler(new GlassWindowEventHandler(this));
      if (var1.isSupported()) {
         var1.createMenuBar();
         this.platformWindow.setMenuBar(var1.getMenuBar());
      }

      return this;
   }

   private void initPlatformWindow() {
      if (this.platformWindow == null) {
         Application var1 = Application.GetApplication();
         if (this.isPrimaryStage && null != appletWindow) {
            this.platformWindow = var1.createWindow(appletWindow.getGlassWindow().getNativeWindow());
         } else {
            Window var2 = null;
            if (this.owner instanceof WindowStage) {
               var2 = ((WindowStage)this.owner).platformWindow;
            }

            boolean var3 = false;
            boolean var4 = true;
            int var5 = this.rtl ? 128 : 0;
            if (this.isPopupStage) {
               var5 |= 8;
               if (this.style == StageStyle.TRANSPARENT) {
                  var5 |= 2;
               }

               var4 = false;
            } else {
               label70: {
                  switch (this.style) {
                     case UNIFIED:
                        if (var1.supportsUnifiedWindows()) {
                           var5 |= 256;
                        }
                     case DECORATED:
                        break;
                     case UTILITY:
                        var5 |= 21;
                        break label70;
                     default:
                        var5 |= (this.transparent ? 2 : 0) | 16;
                        break label70;
                  }

                  var5 |= 113;
                  if (var2 != null || this.modality != Modality.NONE) {
                     var5 &= -97;
                  }

                  var3 = true;
               }
            }

            this.platformWindow = var1.createWindow(var2, Screen.getMainScreen(), var5);
            this.platformWindow.setResizable(var3);
            this.platformWindow.setFocusable(var4);
            if (this.securityDialog) {
               this.platformWindow.setLevel(2);
            }
         }
      }

      platformWindows.put(this.platformWindow, this);
   }

   final Window getPlatformWindow() {
      return this.platformWindow;
   }

   static WindowStage findWindowStage(Window var0) {
      return (WindowStage)platformWindows.get(var0);
   }

   protected GlassStage getOwner() {
      return this.owner;
   }

   protected ViewScene getViewScene() {
      return (ViewScene)this.getScene();
   }

   StageStyle getStyle() {
      return this.style;
   }

   public TKScene createTKScene(boolean var1, boolean var2, AccessControlContext var3) {
      ViewScene var4 = new ViewScene(var1, var2);
      var4.setSecurityContext(var3);
      return var4;
   }

   public void setScene(TKScene var1) {
      GlassScene var2 = this.getScene();
      if (var2 != var1) {
         this.exitFullScreen();
         super.setScene(var1);
         if (var1 != null) {
            ViewScene var3 = this.getViewScene();
            View var4 = var3.getPlatformView();
            QuantumToolkit.runWithRenderLock(() -> {
               this.platformWindow.setView(var4);
               if (var2 != null) {
                  var2.updateSceneState();
               }

               var3.updateSceneState();
               return null;
            });
            this.requestFocus();
         } else {
            QuantumToolkit.runWithRenderLock(() -> {
               if (this.platformWindow != null) {
                  this.platformWindow.setView((View)null);
               }

               if (var2 != null) {
                  var2.updateSceneState();
               }

               return null;
            });
         }

         if (var2 != null) {
            ViewPainter var5 = ((ViewScene)var2).getPainter();
            QuantumRenderer.getInstance().disposePresentable(var5.presentable);
         }

      }
   }

   public void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      if ((double)var11 > 0.0 || (double)var12 > 0.0) {
         if ((double)var11 > 0.0) {
            this.platformWindow.setRenderScaleX(var11);
         }

         if ((double)var12 > 0.0) {
            this.platformWindow.setRenderScaleY(var12);
         }

         ViewScene var13 = this.getViewScene();
         if (var13 != null) {
            var13.updateSceneState();
            var13.entireSceneNeedsRepaint();
         }
      }

      if (this.isAppletStage) {
         var4 = false;
         var3 = false;
      }

      if (var3 || var4 || var5 > 0.0F || var6 > 0.0F || var7 > 0.0F || var8 > 0.0F) {
         this.platformWindow.setBounds(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }

   }

   public float getPlatformScaleX() {
      return this.platformWindow.getPlatformScaleX();
   }

   public float getPlatformScaleY() {
      return this.platformWindow.getPlatformScaleY();
   }

   public float getOutputScaleX() {
      return this.platformWindow.getOutputScaleX();
   }

   public float getOutputScaleY() {
      return this.platformWindow.getOutputScaleY();
   }

   public void setMinimumSize(int var1, int var2) {
      var1 = (int)Math.ceil((double)((float)var1 * this.getPlatformScaleX()));
      var2 = (int)Math.ceil((double)((float)var2 * this.getPlatformScaleY()));
      this.platformWindow.setMinimumSize(var1, var2);
   }

   public void setMaximumSize(int var1, int var2) {
      var1 = (int)Math.ceil((double)((float)var1 * this.getPlatformScaleX()));
      var2 = (int)Math.ceil((double)((float)var2 * this.getPlatformScaleY()));
      this.platformWindow.setMaximumSize(var1, var2);
   }

   static Image findBestImage(List var0, int var1, int var2) {
      Image var3 = null;
      double var4 = 3.0;
      Iterator var6 = var0.iterator();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         Image var8 = (Image)var7;
         if (var8 != null && (var8.getPixelFormat() == PixelFormat.BYTE_RGB || var8.getPixelFormat() == PixelFormat.BYTE_BGRA_PRE || var8.getPixelFormat() == PixelFormat.BYTE_GRAY)) {
            int var9 = var8.getWidth();
            int var10 = var8.getHeight();
            if (var9 > 0 && var10 > 0) {
               double var11 = Math.min((double)var1 / (double)var9, (double)var2 / (double)var10);
               double var15 = 1.0;
               int var13;
               int var14;
               double var17;
               if (var11 >= 2.0) {
                  var11 = Math.floor(var11);
                  var13 = var9 * (int)var11;
                  var14 = var10 * (int)var11;
                  var15 = 1.0 - 0.5 / var11;
               } else if (var11 >= 1.0) {
                  var11 = 1.0;
                  var13 = var9;
                  var14 = var10;
                  var15 = 0.0;
               } else if (var11 >= 0.75) {
                  var11 = 0.75;
                  var13 = var9 * 3 / 4;
                  var14 = var10 * 3 / 4;
                  var15 = 0.3;
               } else if (var11 >= 0.6666) {
                  var11 = 0.6666;
                  var13 = var9 * 2 / 3;
                  var14 = var10 * 2 / 3;
                  var15 = 0.33;
               } else {
                  var17 = Math.ceil(1.0 / var11);
                  var11 = 1.0 / var17;
                  var13 = (int)Math.round((double)var9 / var17);
                  var14 = (int)Math.round((double)var10 / var17);
                  var15 = 1.0 - 1.0 / var17;
               }

               var17 = ((double)var1 - (double)var13) / (double)var1 + ((double)var2 - (double)var14) / (double)var2 + var15;
               if (var17 < var4) {
                  var4 = var17;
                  var3 = var8;
               }

               if (var17 == 0.0) {
                  break;
               }
            }
         }
      }

      return var3;
   }

   public void setIcons(List var1) {
      short var2 = 32;
      short var3 = 32;
      if (PlatformUtil.isMac()) {
         var2 = 128;
         var3 = 128;
      } else if (PlatformUtil.isWindows()) {
         var2 = 32;
         var3 = 32;
      } else if (PlatformUtil.isLinux()) {
         var2 = 128;
         var3 = 128;
      }

      if (var1 != null && var1.size() >= 1) {
         Image var4 = findBestImage(var1, var3, var2);
         if (var4 != null) {
            PushbroomScaler var5 = ScalerFactory.createScaler(var4.getWidth(), var4.getHeight(), var4.getBytesPerPixelUnit(), var3, var2, true);
            ByteBuffer var6 = (ByteBuffer)var4.getPixelBuffer();
            byte[] var7 = new byte[var6.limit()];
            int var8 = var4.getHeight();

            for(int var9 = 0; var9 < var8; ++var9) {
               var6.position(var9 * var4.getScanlineStride());
               var6.get(var7, 0, var4.getScanlineStride());
               var5.putSourceScanline(var7, 0);
            }

            var6.rewind();
            Image var10 = var4.iconify(var5.getDestination(), var3, var2);
            this.platformWindow.setIcon(PixelUtils.imageToPixels(var10));
         }
      } else {
         this.platformWindow.setIcon((Pixels)null);
      }
   }

   public void setTitle(String var1) {
      this.platformWindow.setTitle(var1);
   }

   public void setVisible(boolean var1) {
      if (!var1) {
         removeActiveWindow(this);
         if (this.modality == Modality.WINDOW_MODAL) {
            if (this.owner != null && this.owner instanceof WindowStage) {
               ((WindowStage)this.owner).setEnabled(true);
            }
         } else if (this.modality == Modality.APPLICATION_MODAL) {
            this.windowsSetEnabled(true);
         } else if (!this.isPopupStage && this.owner != null && this.owner instanceof WindowStage) {
            WindowStage var2 = (WindowStage)this.owner;
            var2.requestToFront();
         }
      }

      QuantumToolkit.runWithRenderLock(() -> {
         if (this.platformWindow != null) {
            this.platformWindow.setVisible(var1);
         }

         super.setVisible(var1);
         return null;
      });
      if (var1) {
         if (this.modality == Modality.WINDOW_MODAL) {
            if (this.owner != null && this.owner instanceof WindowStage) {
               ((WindowStage)this.owner).setEnabled(false);
            }
         } else if (this.modality == Modality.APPLICATION_MODAL) {
            this.windowsSetEnabled(false);
         }

         if (this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
         }
      }

      this.applyFullScreen();
   }

   boolean isVisible() {
      return this.platformWindow.isVisible();
   }

   public void setOpacity(float var1) {
      this.platformWindow.setAlpha(var1);
      GlassScene var2 = this.getScene();
      if (var2 != null) {
         var2.entireSceneNeedsRepaint();
      }

   }

   public boolean needsUpdateWindow() {
      return this.transparent && Application.GetApplication().shouldUpdateWindow();
   }

   public void setIconified(boolean var1) {
      if (this.platformWindow.isMinimized() != var1) {
         this.platformWindow.minimize(var1);
      }
   }

   public void setMaximized(boolean var1) {
      if (this.platformWindow.isMaximized() != var1) {
         this.platformWindow.maximize(var1);
      }
   }

   public void setAlwaysOnTop(boolean var1) {
      if (!this.securityDialog) {
         if (this.isAlwaysOnTop != var1) {
            if (var1) {
               if (this.hasPermission(alwaysOnTopPermission)) {
                  this.platformWindow.setLevel(2);
               } else {
                  var1 = false;
                  if (this.stageListener != null) {
                     this.stageListener.changedAlwaysOnTop(var1);
                  }
               }
            } else {
               this.platformWindow.setLevel(1);
            }

            this.isAlwaysOnTop = var1;
         }
      }
   }

   public void setResizable(boolean var1) {
      this.platformWindow.setResizable(var1);
   }

   boolean isTrustedFullScreen() {
      return this.hasPermission(fullScreenPermission);
   }

   void exitFullScreen() {
      this.setFullScreen(false);
   }

   boolean isApplet() {
      return this.isPrimaryStage && null != appletWindow;
   }

   private boolean hasPermission(Permission var1) {
      try {
         SecurityManager var2 = System.getSecurityManager();
         if (var2 != null) {
            var2.checkPermission(var1, this.getAccessControlContext());
         }

         return true;
      } catch (SecurityException var3) {
         return false;
      }
   }

   public final KeyCombination getSavedFullScreenExitKey() {
      return this.savedFullScreenExitKey;
   }

   private void applyFullScreen() {
      if (this.platformWindow != null) {
         View var1 = this.platformWindow.getView();
         if (this.isVisible() && var1 != null && var1.isInFullscreen() != this.isInFullScreen) {
            if (!this.isInFullScreen) {
               if (this.warning != null) {
                  this.warning.cancel();
                  this.setWarning((OverlayWarning)null);
               }

               var1.exitFullscreen(false);
            } else {
               boolean var2 = this.isTrustedFullScreen();
               if (!var2 && !this.fullScreenFromUserEvent) {
                  this.exitFullScreen();
                  this.fullscreenChanged(false);
               } else {
                  var1.enterFullscreen(false, false, false);
                  if (this.warning != null && this.warning.inWarningTransition()) {
                     this.warning.setView(this.getViewScene());
                  } else {
                     boolean var3 = true;
                     KeyCombination var4 = null;
                     String var5 = null;
                     if (var2 && this.fxStage != null) {
                        var4 = this.fxStage.getFullScreenExitKeyCombination();
                        var5 = this.fxStage.getFullScreenExitHint();
                     }

                     this.savedFullScreenExitKey = (KeyCombination)(var4 == null ? defaultFullScreenExitKeycombo : var4);
                     if ("".equals(var5) || this.savedFullScreenExitKey.equals(KeyCombination.NO_MATCH)) {
                        var3 = false;
                     }

                     if (var3 && var5 == null) {
                        if (var4 == null) {
                           var5 = RESOURCES.getString("OverlayWarningESC");
                        } else {
                           String var6 = RESOURCES.getString("OverlayWarningKey");
                           var5 = String.format(var6, this.savedFullScreenExitKey.toString());
                        }
                     }

                     if (var3 && this.warning == null) {
                        this.setWarning(new OverlayWarning(this.getViewScene()));
                     }

                     if (var3 && this.warning != null) {
                        this.warning.warn(var5);
                     }
                  }
               }
            }

            this.fullScreenFromUserEvent = false;
         } else if (!this.isVisible() && this.warning != null) {
            this.warning.cancel();
            this.setWarning((OverlayWarning)null);
         }

      }
   }

   void setWarning(OverlayWarning var1) {
      this.warning = var1;
      this.getViewScene().synchroniseOverlayWarning();
   }

   OverlayWarning getWarning() {
      return this.warning;
   }

   public void setFullScreen(boolean var1) {
      if (this.isInFullScreen != var1) {
         if (this.isInAllowedEventHandler()) {
            this.fullScreenFromUserEvent = true;
         }

         GlassStage var2 = (GlassStage)activeFSWindow.get();
         if (var1 && var2 != null) {
            var2.setFullScreen(false);
         }

         this.isInFullScreen = var1;
         this.applyFullScreen();
         if (var1) {
            activeFSWindow.set(this);
         }

      }
   }

   void fullscreenChanged(boolean var1) {
      if (!var1) {
         if (activeFSWindow.compareAndSet(this, (Object)null)) {
            this.isInFullScreen = false;
         }
      } else {
         this.isInFullScreen = true;
         activeFSWindow.set(this);
      }

      AccessController.doPrivileged(() -> {
         if (this.stageListener != null) {
            this.stageListener.changedFullscreen(var1);
         }

         return null;
      }, this.getAccessControlContext());
   }

   public void toBack() {
      this.platformWindow.toBack();
      if (this.isAppletStage && null != appletWindow) {
         appletWindow.assertStageOrder();
      }

   }

   public void toFront() {
      this.platformWindow.requestFocus();
      this.platformWindow.toFront();
      if (this.isAppletStage && null != appletWindow) {
         appletWindow.assertStageOrder();
      }

   }

   public void postponeClose() {
      this.isClosePostponed = true;
   }

   public void closePostponed() {
      if (this.deadWindow != null) {
         this.deadWindow.close();
         this.deadWindow = null;
      }

   }

   public void close() {
      super.close();
      QuantumToolkit.runWithRenderLock(() -> {
         if (this.platformWindow != null) {
            platformWindows.remove(this.platformWindow);
            if (this.isClosePostponed) {
               this.deadWindow = this.platformWindow;
            } else {
               this.platformWindow.close();
            }

            this.platformWindow = null;
         }

         ViewScene var1 = this.getViewScene();
         if (var1 != null) {
            var1.updateSceneState();
         }

         return null;
      });
   }

   void setPlatformWindowClosed() {
      this.platformWindow = null;
   }

   static void addActiveWindow(WindowStage var0) {
      activeWindows.remove(var0);
      activeWindows.add(var0);
   }

   static void removeActiveWindow(WindowStage var0) {
      activeWindows.remove(var0);
   }

   final void handleFocusDisabled() {
      if (!activeWindows.isEmpty()) {
         WindowStage var1 = (WindowStage)activeWindows.get(activeWindows.size() - 1);
         var1.setIconified(false);
         var1.requestToFront();
         var1.requestFocus();
      }
   }

   public boolean grabFocus() {
      return this.platformWindow.grabFocus();
   }

   public void ungrabFocus() {
      this.platformWindow.ungrabFocus();
   }

   public void requestFocus() {
      this.platformWindow.requestFocus();
   }

   public void requestFocus(FocusCause var1) {
      switch (var1) {
         case TRAVERSED_FORWARD:
            this.platformWindow.requestFocus(543);
            break;
         case TRAVERSED_BACKWARD:
            this.platformWindow.requestFocus(544);
            break;
         case ACTIVATED:
            this.platformWindow.requestFocus(542);
            break;
         case DEACTIVATED:
            this.platformWindow.requestFocus(541);
      }

   }

   protected void setPlatformEnabled(boolean var1) {
      super.setPlatformEnabled(var1);
      this.platformWindow.setEnabled(var1);
      if (var1) {
         if (this.platformWindow.isEnabled()) {
            this.requestToFront();
         }
      } else {
         removeActiveWindow(this);
      }

   }

   public void setEnabled(boolean var1) {
      if (this.owner != null && this.owner instanceof WindowStage) {
         ((WindowStage)this.owner).setEnabled(var1);
      }

      if (!var1 || this.platformWindow != null && !this.platformWindow.isClosed()) {
         this.setPlatformEnabled(var1);
         if (var1 && this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
         }

      }
   }

   public long getRawHandle() {
      return this.platformWindow.getRawHandle();
   }

   protected void requestToFront() {
      if (this.platformWindow != null) {
         this.platformWindow.toFront();
         this.platformWindow.requestFocus();
      }

   }

   public void setInAllowedEventHandler(boolean var1) {
      this.inAllowedEventHandler = var1;
   }

   private boolean isInAllowedEventHandler() {
      return this.inAllowedEventHandler;
   }

   public void requestInput(String var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29) {
      this.platformWindow.requestInput(var1, var2, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27, var29);
   }

   public void releaseInput() {
      this.platformWindow.releaseInput();
   }

   public void setRTL(boolean var1) {
      this.rtl = var1;
   }

   static {
      RESOURCES = ResourceBundle.getBundle(WindowStage.class.getPackage().getName() + ".QuantumMessagesBundle", LOCALE);
      fullScreenPermission = new AllPermission();
      alwaysOnTopPermission = new AllPermission();
   }
}
