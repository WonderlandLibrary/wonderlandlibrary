package javafx.embed.swing;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.SecondaryLoop;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javax.swing.SwingUtilities;
import sun.awt.AWTAccessor;
import sun.awt.FwDispatcher;
import sun.awt.image.IntegerComponentRaster;

public class SwingFXUtils {
   private static final Set eventLoopKeys = new HashSet();

   private SwingFXUtils() {
   }

   public static WritableImage toFXImage(BufferedImage var0, WritableImage var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      switch (var0.getType()) {
         case 2:
         case 3:
            break;
         default:
            BufferedImage var4 = new BufferedImage(var2, var3, 3);
            Graphics2D var5 = var4.createGraphics();
            var5.drawImage(var0, 0, 0, (ImageObserver)null);
            var5.dispose();
            var0 = var4;
      }

      int[] var6;
      if (var1 != null) {
         int var10 = (int)var1.getWidth();
         int var12 = (int)var1.getHeight();
         if (var10 >= var2 && var12 >= var3) {
            if (var2 < var10 || var3 < var12) {
               var6 = new int[var10];
               PixelWriter var7 = var1.getPixelWriter();
               WritablePixelFormat var8 = PixelFormat.getIntArgbPreInstance();
               if (var2 < var10) {
                  var7.setPixels(var2, 0, var10 - var2, var3, var8, (int[])var6, 0, 0);
               }

               if (var3 < var12) {
                  var7.setPixels(0, var3, var10, var12 - var3, var8, (int[])var6, 0, 0);
               }
            }
         } else {
            var1 = null;
         }
      }

      if (var1 == null) {
         var1 = new WritableImage(var2, var3);
      }

      PixelWriter var11 = var1.getPixelWriter();
      IntegerComponentRaster var13 = (IntegerComponentRaster)var0.getRaster();
      var6 = var13.getDataStorage();
      int var14 = var13.getDataOffset(0);
      int var15 = var13.getScanlineStride();
      WritablePixelFormat var9 = var0.isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance() : PixelFormat.getIntArgbInstance();
      var11.setPixels(0, 0, var2, var3, var9, (int[])var6, var14, var15);
      return var1;
   }

   private static int getBestBufferedImageType(PixelFormat var0, BufferedImage var1) {
      if (var1 != null) {
         int var2 = var1.getType();
         if (var2 == 2 || var2 == 3) {
            return var2;
         }
      }

      switch (var0.getType()) {
         case BYTE_BGRA_PRE:
         case INT_ARGB_PRE:
         default:
            return 3;
         case BYTE_BGRA:
         case INT_ARGB:
            return 2;
         case BYTE_RGB:
            return 1;
         case BYTE_INDEXED:
            return var0.isPremultiplied() ? 3 : 2;
      }
   }

   private static WritablePixelFormat getAssociatedPixelFormat(BufferedImage var0) {
      switch (var0.getType()) {
         case 1:
         case 3:
            return PixelFormat.getIntArgbPreInstance();
         case 2:
            return PixelFormat.getIntArgbInstance();
         default:
            throw new InternalError("Failed to validate BufferedImage type");
      }
   }

   public static BufferedImage fromFXImage(Image var0, BufferedImage var1) {
      PixelReader var2 = var0.getPixelReader();
      if (var2 == null) {
         return null;
      } else {
         int var3 = (int)var0.getWidth();
         int var4 = (int)var0.getHeight();
         int var5 = getBestBufferedImageType(var2.getPixelFormat(), var1);
         int var7;
         if (var1 != null) {
            int var6 = var1.getWidth();
            var7 = var1.getHeight();
            if (var6 >= var3 && var7 >= var4 && var1.getType() == var5) {
               if (var3 < var6 || var4 < var7) {
                  Graphics2D var8 = var1.createGraphics();
                  var8.setComposite(AlphaComposite.Clear);
                  var8.fillRect(0, 0, var6, var7);
                  var8.dispose();
               }
            } else {
               var1 = null;
            }
         }

         if (var1 == null) {
            var1 = new BufferedImage(var3, var4, var5);
         }

         IntegerComponentRaster var11 = (IntegerComponentRaster)var1.getRaster();
         var7 = var11.getDataOffset(0);
         int var12 = var11.getScanlineStride();
         int[] var9 = var11.getDataStorage();
         WritablePixelFormat var10 = getAssociatedPixelFormat(var1);
         var2.getPixels(0, 0, var3, var4, var10, (int[])var9, var7, var12);
         return var1;
      }
   }

   static void runOnFxThread(Runnable var0) {
      if (Platform.isFxApplicationThread()) {
         var0.run();
      } else {
         Platform.runLater(var0);
      }

   }

   static void runOnEDT(Runnable var0) {
      if (SwingUtilities.isEventDispatchThread()) {
         var0.run();
      } else {
         SwingUtilities.invokeLater(var0);
      }

   }

   static void runOnEDTAndWait(Object var0, Runnable var1) {
      Toolkit.getToolkit().checkFxUserThread();
      if (SwingUtilities.isEventDispatchThread()) {
         var1.run();
      } else {
         eventLoopKeys.add(var0);
         SwingUtilities.invokeLater(var1);
         Toolkit.getToolkit().enterNestedEventLoop(var0);
      }

   }

   static void leaveFXNestedLoop(Object var0) {
      if (eventLoopKeys.contains(var0)) {
         if (Platform.isFxApplicationThread()) {
            Toolkit.getToolkit().exitNestedEventLoop(var0, (Object)null);
         } else {
            Platform.runLater(() -> {
               Toolkit.getToolkit().exitNestedEventLoop(var0, (Object)null);
            });
         }

         eventLoopKeys.remove(var0);
      }
   }

   private static EventQueue getEventQueue() {
      return (EventQueue)AccessController.doPrivileged(() -> {
         return java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue();
      });
   }

   private static void installFwEventQueue() {
      AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(), new FXDispatcher());
   }

   private static void removeFwEventQueue() {
      AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(), (FwDispatcher)null);
   }

   private static class FXDispatcher implements FwDispatcher {
      private FXDispatcher() {
      }

      public boolean isDispatchThread() {
         return Platform.isFxApplicationThread();
      }

      public void scheduleDispatch(Runnable var1) {
         Platform.runLater(var1);
      }

      public SecondaryLoop createSecondaryLoop() {
         return new FwSecondaryLoop();
      }

      // $FF: synthetic method
      FXDispatcher(Object var1) {
         this();
      }
   }

   private static class FwSecondaryLoop implements SecondaryLoop {
      private final AtomicBoolean isRunning;

      private FwSecondaryLoop() {
         this.isRunning = new AtomicBoolean(false);
      }

      public boolean enter() {
         if (this.isRunning.compareAndSet(false, true)) {
            PlatformImpl.runAndWait(() -> {
               Toolkit.getToolkit().enterNestedEventLoop(this);
            });
            return true;
         } else {
            return false;
         }
      }

      public boolean exit() {
         if (this.isRunning.compareAndSet(true, false)) {
            PlatformImpl.runAndWait(() -> {
               Toolkit.getToolkit().exitNestedEventLoop(this, (Object)null);
            });
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      FwSecondaryLoop(Object var1) {
         this();
      }
   }
}
