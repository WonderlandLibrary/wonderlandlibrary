package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

final class EmbeddedSceneDnD {
   private final GlassSceneDnDEventHandler dndHandler;
   private HostDragStartListener dragStartListener;
   private EmbeddedSceneDSInterface fxDragSource;
   private EmbeddedSceneDTInterface fxDropTarget;
   private Thread hostThread;

   public EmbeddedSceneDnD(GlassScene var1) {
      this.dndHandler = new GlassSceneDnDEventHandler(var1);
   }

   private void startDrag() {
      assert Platform.isFxApplicationThread();

      assert this.fxDragSource != null;

      this.dragStartListener.dragStarted(this.fxDragSource, TransferMode.COPY);
   }

   private void setHostThread() {
      if (this.hostThread == null) {
         this.hostThread = Thread.currentThread();
      }

   }

   public boolean isHostThread() {
      return Thread.currentThread() == this.hostThread;
   }

   public void onDragSourceReleased(EmbeddedSceneDSInterface var1) {
      assert this.fxDragSource == var1;

      this.fxDragSource = null;
      Toolkit.getToolkit().exitNestedEventLoop(this, (Object)null);
   }

   public void onDropTargetReleased(EmbeddedSceneDTInterface var1) {
      assert this.fxDropTarget == var1;

      this.fxDropTarget = null;
   }

   Object executeOnFXThread(Callable var1) {
      if (Platform.isFxApplicationThread()) {
         try {
            return var1.call();
         } catch (Exception var5) {
            return null;
         }
      } else {
         AtomicReference var2 = new AtomicReference();
         CountDownLatch var3 = new CountDownLatch(1);
         Platform.runLater(() -> {
            try {
               var2.set(var1.call());
            } catch (Exception var7) {
            } finally {
               var3.countDown();
            }

         });

         try {
            var3.await();
         } catch (Exception var6) {
         }

         return var2.get();
      }
   }

   public TKClipboard createDragboard(boolean var1) {
      assert Platform.isFxApplicationThread();

      assert this.fxDragSource == null;

      assert var1;

      ClipboardAssistance var2 = new ClipboardAssistance("DND-Embedded") {
         public void flush() {
            super.flush();
            EmbeddedSceneDnD.this.startDrag();
            Toolkit.getToolkit().enterNestedEventLoop(EmbeddedSceneDnD.this);
         }
      };
      this.fxDragSource = new EmbeddedSceneDS(this, var2, this.dndHandler);
      return QuantumClipboard.getDragboardInstance(var2, var1);
   }

   public void setDragStartListener(HostDragStartListener var1) {
      this.setHostThread();
      this.dragStartListener = var1;
   }

   public EmbeddedSceneDTInterface createDropTarget() {
      this.setHostThread();
      return (EmbeddedSceneDTInterface)this.executeOnFXThread(() -> {
         assert this.fxDropTarget == null;

         this.fxDropTarget = new EmbeddedSceneDT(this, this.dndHandler);
         return this.fxDropTarget;
      });
   }
}
