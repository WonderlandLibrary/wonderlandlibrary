package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import javafx.scene.input.TransferMode;

final class EmbeddedSceneDT implements EmbeddedSceneDTInterface {
   private final EmbeddedSceneDnD dnd;
   private final GlassSceneDnDEventHandler dndHandler;
   private EmbeddedSceneDSInterface dragSource;
   private ClipboardAssistance assistant;

   public EmbeddedSceneDT(EmbeddedSceneDnD var1, GlassSceneDnDEventHandler var2) {
      this.dnd = var1;
      this.dndHandler = var2;
   }

   private void close() {
      this.dnd.onDropTargetReleased(this);
      this.assistant = null;
   }

   public TransferMode handleDragEnter(int var1, int var2, int var3, int var4, TransferMode var5, EmbeddedSceneDSInterface var6) {
      assert this.dnd.isHostThread();

      return (TransferMode)this.dnd.executeOnFXThread(() -> {
         assert this.dragSource == null;

         assert this.assistant == null;

         this.dragSource = var6;
         this.assistant = new EmbeddedDTAssistant(this.dragSource);
         return this.dndHandler.handleDragEnter(var1, var2, var3, var4, var5, this.assistant);
      });
   }

   public void handleDragLeave() {
      assert this.dnd.isHostThread();

      this.dnd.executeOnFXThread(() -> {
         assert this.assistant != null;

         try {
            this.dndHandler.handleDragLeave(this.assistant);
         } finally {
            this.close();
         }

         return null;
      });
   }

   public TransferMode handleDragDrop(int var1, int var2, int var3, int var4, TransferMode var5) {
      assert this.dnd.isHostThread();

      return (TransferMode)this.dnd.executeOnFXThread(() -> {
         assert this.assistant != null;

         TransferMode var6;
         try {
            var6 = this.dndHandler.handleDragDrop(var1, var2, var3, var4, var5, this.assistant);
         } finally {
            this.close();
         }

         return var6;
      });
   }

   public TransferMode handleDragOver(int var1, int var2, int var3, int var4, TransferMode var5) {
      assert this.dnd.isHostThread();

      return (TransferMode)this.dnd.executeOnFXThread(() -> {
         assert this.assistant != null;

         return this.dndHandler.handleDragOver(var1, var2, var3, var4, var5, this.assistant);
      });
   }

   private static class EmbeddedDTAssistant extends ClipboardAssistance {
      private EmbeddedSceneDSInterface dragSource;

      EmbeddedDTAssistant(EmbeddedSceneDSInterface var1) {
         super("DND-Embedded");
         this.dragSource = var1;
      }

      public void flush() {
         throw new UnsupportedOperationException();
      }

      public Object getData(String var1) {
         return this.dragSource.getData(var1);
      }

      public int getSupportedSourceActions() {
         return QuantumClipboard.transferModesToClipboardActions(this.dragSource.getSupportedActions());
      }

      public void setTargetAction(int var1) {
         throw new UnsupportedOperationException();
      }

      public String[] getMimeTypes() {
         return this.dragSource.getMimeTypes();
      }
   }
}
