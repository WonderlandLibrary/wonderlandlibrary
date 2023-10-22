package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.Disposer;

final class PresentingPainter extends ViewPainter {
   PresentingPainter(ViewScene var1) {
      super(var1);
   }

   public void run() {
      renderLock.lock();
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      boolean var11 = false;

      label285: {
         ViewScene var14;
         label300: {
            label301: {
               label288: {
                  try {
                     var11 = true;
                     var2 = this.validateStageGraphics();
                     if (var2) {
                        this.sceneState.lock();
                        var1 = true;
                        if (this.factory == null) {
                           this.factory = GraphicsPipeline.getDefaultResourceFactory();
                        }

                        if (this.factory != null && this.factory.isDeviceReady()) {
                           if (this.presentable != null && this.presentable.lockResources(this.sceneState)) {
                              this.disposePresentable();
                           }

                           if (this.presentable == null) {
                              this.presentable = this.factory.createPresentable(this.sceneState);
                              this.penWidth = this.viewWidth;
                              this.penHeight = this.viewHeight;
                              this.freshBackBuffer = true;
                           }

                           if (this.presentable != null) {
                              Graphics var4 = this.presentable.createGraphics();
                              ViewScene var5 = (ViewScene)this.sceneState.getScene();
                              if (var4 != null) {
                                 this.paintImpl(var4);
                                 this.freshBackBuffer = false;
                              }

                              if (PulseLogger.PULSE_LOGGING_ENABLED) {
                                 PulseLogger.newPhase("Presenting");
                              }

                              if (!this.presentable.prepare((Rectangle)null)) {
                                 this.disposePresentable();
                                 this.sceneState.getScene().entireSceneNeedsRepaint();
                                 var11 = false;
                                 break label285;
                              }

                              if (var5.getDoPresent()) {
                                 if (!this.presentable.present()) {
                                    this.disposePresentable();
                                    this.sceneState.getScene().entireSceneNeedsRepaint();
                                    var11 = false;
                                 } else {
                                    var11 = false;
                                 }
                              } else {
                                 var11 = false;
                              }
                           } else {
                              var11 = false;
                           }
                           break label301;
                        }

                        this.sceneState.getScene().entireSceneNeedsRepaint();
                        var11 = false;
                        break label300;
                     }

                     if (QuantumToolkit.verbose) {
                        System.err.println("PresentingPainter: validateStageGraphics failed");
                     }

                     this.paintImpl((Graphics)null);
                     var11 = false;
                  } catch (Throwable var12) {
                     var3 = true;
                     var12.printStackTrace(System.err);
                     var11 = false;
                     break label288;
                  } finally {
                     if (var11) {
                        Disposer.cleanUp();
                        if (var1) {
                           this.sceneState.unlock();
                        }

                        ViewScene var8 = (ViewScene)this.sceneState.getScene();
                        var8.setPainting(false);
                        if (this.factory != null) {
                           this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
                        }

                        renderLock.unlock();
                     }
                  }

                  Disposer.cleanUp();
                  if (var1) {
                     this.sceneState.unlock();
                  }

                  var14 = (ViewScene)this.sceneState.getScene();
                  var14.setPainting(false);
                  if (this.factory != null) {
                     this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
                  }

                  renderLock.unlock();
                  return;
               }

               Disposer.cleanUp();
               if (var1) {
                  this.sceneState.unlock();
               }

               var14 = (ViewScene)this.sceneState.getScene();
               var14.setPainting(false);
               if (this.factory != null) {
                  this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
               }

               renderLock.unlock();
               return;
            }

            Disposer.cleanUp();
            if (var1) {
               this.sceneState.unlock();
            }

            var14 = (ViewScene)this.sceneState.getScene();
            var14.setPainting(false);
            if (this.factory != null) {
               this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
            }

            renderLock.unlock();
            return;
         }

         Disposer.cleanUp();
         if (var1) {
            this.sceneState.unlock();
         }

         var14 = (ViewScene)this.sceneState.getScene();
         var14.setPainting(false);
         if (this.factory != null) {
            this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
         }

         renderLock.unlock();
         return;
      }

      Disposer.cleanUp();
      if (var1) {
         this.sceneState.unlock();
      }

      ViewScene var6 = (ViewScene)this.sceneState.getScene();
      var6.setPainting(false);
      if (this.factory != null) {
         this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var3);
      }

      renderLock.unlock();
   }
}
