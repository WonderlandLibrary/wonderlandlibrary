package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

final class PaintCollector implements CompletionListener {
   private static volatile PaintCollector collector;
   private static final Comparator DIRTY_SCENE_SORTER = (var0, var1) -> {
      int var2 = var0.isSynchronous() ? 1 : 0;
      int var3 = var1.isSynchronous() ? 1 : 0;
      return var2 - var3;
   };
   private final List dirtyScenes = new ArrayList();
   private volatile CountDownLatch allWorkCompletedLatch = new CountDownLatch(0);
   private volatile boolean hasDirty;
   private final QuantumToolkit toolkit;
   private volatile boolean needsHint;

   static PaintCollector createInstance(QuantumToolkit var0) {
      return collector = new PaintCollector(var0);
   }

   static PaintCollector getInstance() {
      return collector;
   }

   private PaintCollector(QuantumToolkit var1) {
      this.toolkit = var1;
   }

   void waitForRenderingToComplete() {
      while(true) {
         try {
            this.allWorkCompletedLatch.await();
            return;
         } catch (InterruptedException var2) {
         }
      }
   }

   final boolean hasDirty() {
      return this.hasDirty;
   }

   private final void setDirty(boolean var1) {
      this.hasDirty = var1;
      if (this.hasDirty) {
         QuantumToolkit.getToolkit().requestNextPulse();
      }

   }

   final void addDirtyScene(GlassScene var1) {
      assert Thread.currentThread() == QuantumToolkit.getFxUserThread();

      assert var1 != null;

      if (QuantumToolkit.verbose) {
         System.err.println("PC.addDirtyScene: " + System.nanoTime() + var1);
      }

      if (!this.dirtyScenes.contains(var1)) {
         this.dirtyScenes.add(var1);
         this.setDirty(true);
      }

   }

   final void removeDirtyScene(GlassScene var1) {
      assert Thread.currentThread() == QuantumToolkit.getFxUserThread();

      assert var1 != null;

      if (QuantumToolkit.verbose) {
         System.err.println("PC.removeDirtyScene: " + var1);
      }

      this.dirtyScenes.remove(var1);
      this.setDirty(!this.dirtyScenes.isEmpty());
   }

   final CompletionListener getRendered() {
      return this;
   }

   public void done(RenderJob var1) {
      assert Thread.currentThread() != QuantumToolkit.getFxUserThread();

      if (!(var1 instanceof PaintRenderJob)) {
         throw new IllegalArgumentException("PaintCollector: invalid RenderJob");
      } else {
         PaintRenderJob var2 = (PaintRenderJob)var1;
         GlassScene var3 = var2.getScene();
         if (var3 == null) {
            throw new IllegalArgumentException("PaintCollector: null scene");
         } else {
            var3.frameRendered();
            if (this.allWorkCompletedLatch.getCount() == 1L) {
               if (this.needsHint && !this.toolkit.hasNativeSystemVsync()) {
                  this.toolkit.vsyncHint();
               }

               Application.GetApplication().notifyRenderingFinished();
               if (PulseLogger.PULSE_LOGGING_ENABLED) {
                  PulseLogger.renderEnd();
               }
            }

            this.allWorkCompletedLatch.countDown();
         }
      }
   }

   final void liveRepaintRenderJob(ViewScene var1) {
      ViewPainter var2 = var1.getPainter();
      QuantumToolkit var3 = (QuantumToolkit)QuantumToolkit.getToolkit();
      var3.pulse(false);
      CountDownLatch var4 = new CountDownLatch(1);
      QuantumToolkit.runWithoutRenderLock(() -> {
         var3.addRenderJob(new RenderJob(var2, (var1) -> {
            var4.countDown();
         }));

         try {
            var4.await();
         } catch (InterruptedException var4x) {
         }

         return null;
      });
   }

   final void renderAll() {
      assert Thread.currentThread() == QuantumToolkit.getFxUserThread();

      if (QuantumToolkit.pulseDebug) {
         System.err.println("PC.renderAll(" + this.dirtyScenes.size() + "): " + System.nanoTime());
      }

      if (this.hasDirty) {
         assert !this.dirtyScenes.isEmpty();

         Collections.sort(this.dirtyScenes, DIRTY_SCENE_SORTER);
         this.setDirty(false);
         this.needsHint = false;
         if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.renderStart();
         }

         if (!Application.GetApplication().hasWindowManager()) {
            List var1 = Window.getWindows();
            this.allWorkCompletedLatch = new CountDownLatch(var1.size());
            int var2 = 0;

            for(int var3 = var1.size(); var2 < var3; ++var2) {
               Window var4 = (Window)var1.get(var2);
               WindowStage var5 = WindowStage.findWindowStage(var4);
               if (var5 != null) {
                  ViewScene var6 = var5.getViewScene();
                  if (this.dirtyScenes.indexOf(var6) != -1 && !this.needsHint) {
                     this.needsHint = var6.isSynchronous();
                  }

                  if (PlatformUtil.useEGL() && var2 != var3 - 1) {
                     var6.setDoPresent(false);
                  } else {
                     var6.setDoPresent(true);
                  }

                  try {
                     var6.repaint();
                  } catch (Throwable var9) {
                     var9.printStackTrace();
                  }
               }
            }
         } else {
            this.allWorkCompletedLatch = new CountDownLatch(this.dirtyScenes.size());
            Iterator var10 = this.dirtyScenes.iterator();

            while(var10.hasNext()) {
               GlassScene var11 = (GlassScene)var10.next();
               if (!this.needsHint) {
                  this.needsHint = var11.isSynchronous();
               }

               var11.setDoPresent(true);

               try {
                  var11.repaint();
               } catch (Throwable var8) {
                  var8.printStackTrace();
               }
            }
         }

         this.dirtyScenes.clear();
         if (this.toolkit.shouldWaitForRenderingToComplete()) {
            this.waitForRenderingToComplete();
         }

      }
   }
}
