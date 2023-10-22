package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

final class EmbeddedScene extends GlassScene implements EmbeddedSceneInterface {
   private HostInterface host;
   private UploadingPainter painter;
   private PaintRenderJob paintRenderJob;
   private float renderScaleX;
   private float renderScaleY;
   private final EmbeddedSceneDnD embeddedDnD;
   private volatile IntBuffer texBits;
   private volatile int texLineStride;
   private volatile float texScaleFactorX = 1.0F;
   private volatile float texScaleFactorY = 1.0F;

   public EmbeddedScene(HostInterface var1, boolean var2, boolean var3) {
      super(var2, var3);
      this.sceneState = new EmbeddedState(this);
      this.host = var1;
      this.embeddedDnD = new EmbeddedSceneDnD(this);
      PaintCollector var4 = PaintCollector.getInstance();
      this.painter = new UploadingPainter(this);
      this.paintRenderJob = new PaintRenderJob(this, var4.getRendered(), this.painter);
   }

   public void dispose() {
      assert this.host != null;

      QuantumToolkit.runWithRenderLock(() -> {
         this.host.setEmbeddedScene((EmbeddedSceneInterface)null);
         this.host = null;
         this.updateSceneState();
         this.painter = null;
         this.paintRenderJob = null;
         this.texBits = null;
         return null;
      });
      super.dispose();
   }

   void setStage(GlassStage var1) {
      super.setStage(var1);

      assert this.host != null;

      this.host.setEmbeddedScene(var1 != null ? this : null);
   }

   protected boolean isSynchronous() {
      return false;
   }

   public void setRoot(NGNode var1) {
      super.setRoot(var1);
      this.painter.setRoot(var1);
   }

   public TKClipboard createDragboard(boolean var1) {
      return this.embeddedDnD.createDragboard(var1);
   }

   public void enableInputMethodEvents(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedScene.enableInputMethodEvents " + var1);
      }

   }

   public void finishInputMethodComposition() {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedScene.finishInputMethodComposition");
      }

   }

   public void setPixelScaleFactors(float var1, float var2) {
      this.renderScaleX = var1;
      this.renderScaleY = var2;
      this.entireSceneNeedsRepaint();
   }

   public float getRenderScaleX() {
      return this.renderScaleX;
   }

   public float getRenderScaleY() {
      return this.renderScaleY;
   }

   void uploadPixels(Pixels var1) {
      this.texBits = (IntBuffer)var1.getPixels();
      this.texLineStride = var1.getWidthUnsafe();
      this.texScaleFactorX = var1.getScaleXUnsafe();
      this.texScaleFactorY = var1.getScaleYUnsafe();
      if (this.host != null) {
         this.host.repaint();
      }

   }

   public void repaint() {
      Toolkit var1 = Toolkit.getToolkit();
      var1.addRenderJob(this.paintRenderJob);
   }

   public boolean traverseOut(Direction var1) {
      if (var1 == Direction.NEXT) {
         return this.host.traverseFocusOut(true);
      } else {
         return var1 == Direction.PREVIOUS ? this.host.traverseFocusOut(false) : false;
      }
   }

   public void setSize(int var1, int var2) {
      Platform.runLater(() -> {
         AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
               this.sceneListener.changedSize((float)var1, (float)var2);
            }

            return null;
         }, this.getAccessControlContext());
      });
   }

   public boolean getPixels(IntBuffer var1, int var2, int var3) {
      return (Boolean)QuantumToolkit.runWithRenderLock(() -> {
         if (this.getRenderScaleX() == this.texScaleFactorX && this.getRenderScaleY() == this.texScaleFactorY && this.texBits != null) {
            int var4 = Math.round((float)var2 * this.texScaleFactorX);
            int var5 = Math.round((float)var3 * this.texScaleFactorY);
            var1.rewind();
            this.texBits.rewind();
            if (var1.capacity() == this.texBits.capacity()) {
               var1.put(this.texBits);
               return true;
            } else {
               int var6 = Math.min(var4, this.texLineStride);
               int var7 = Math.min(var5, this.texBits.capacity() / this.texLineStride);
               int[] var8 = new int[var6];

               for(int var9 = 0; var9 < var7; ++var9) {
                  this.texBits.position(var9 * this.texLineStride);
                  this.texBits.get(var8, 0, var6);
                  var1.position(var9 * var4);
                  var1.put(var8);
               }

               return true;
            }
         } else {
            return false;
         }
      });
   }

   protected Color getClearColor() {
      return this.fillPaint != null && this.fillPaint.getType() == Paint.Type.COLOR && ((Color)this.fillPaint).getAlpha() == 0.0F ? (Color)this.fillPaint : super.getClearColor();
   }

   public void mouseEvent(int var1, int var2, boolean var3, boolean var4, boolean var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, int var14, boolean var15) {
      Platform.runLater(() -> {
         AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
               return null;
            } else {
               assert var1 != 2;

               if (var1 == 7) {
                  this.sceneListener.scrollEvent(ScrollEvent.SCROLL, 0.0, (double)(-var14), 0.0, 0.0, 40.0, 40.0, 0, 0, 0, 0, 0, (double)var6, (double)var7, (double)var8, (double)var9, var10, var11, var12, var13, false, false);
               } else {
                  EventType var16 = AbstractEvents.mouseIDToFXEventID(var1);
                  this.sceneListener.mouseEvent(var16, (double)var6, (double)var7, (double)var8, (double)var9, AbstractEvents.mouseButtonToFXMouseButton(var2), var15, false, var10, var11, var12, var13, var3, var4, var5);
               }

               return null;
            }
         }, this.getAccessControlContext());
      });
   }

   public void inputMethodEvent(EventType var1, ObservableList var2, String var3, int var4) {
      Platform.runLater(() -> {
         AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
               this.sceneListener.inputMethodEvent(var1, var2, var3, var4);
            }

            return null;
         });
      });
   }

   public void menuEvent(int var1, int var2, int var3, int var4, boolean var5) {
      Platform.runLater(() -> {
         AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
               this.sceneListener.menuEvent((double)var1, (double)var2, (double)var3, (double)var4, var5);
            }

            return null;
         }, this.getAccessControlContext());
      });
   }

   public void keyEvent(int var1, int var2, char[] var3, int var4) {
      Platform.runLater(() -> {
         AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
               boolean var5 = (var4 & 1) != 0;
               boolean var6 = (var4 & 2) != 0;
               boolean var7 = (var4 & 4) != 0;
               boolean var8 = (var4 & 8) != 0;
               String var9 = new String(var3);
               KeyEvent var11 = new KeyEvent(AbstractEvents.keyIDToFXEventType(var1), var9, var9, KeyCodeMap.valueOf(var2), var5, var6, var7, var8);
               this.sceneListener.keyEvent(var11);
            }

            return null;
         }, this.getAccessControlContext());
      });
   }

   public void setCursor(Object var1) {
      super.setCursor(var1);
      this.host.setCursor((CursorFrame)var1);
   }

   public void setDragStartListener(HostDragStartListener var1) {
      this.embeddedDnD.setDragStartListener(var1);
   }

   public EmbeddedSceneDTInterface createDropTarget() {
      return this.embeddedDnD.createDropTarget();
   }

   public InputMethodRequests getInputMethodRequests() {
      return this.inputMethodRequests;
   }
}
