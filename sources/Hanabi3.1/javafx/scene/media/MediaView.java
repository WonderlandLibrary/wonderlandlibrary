package javafx.scene.media;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmediaimpl.HostUtils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;

public class MediaView extends Node {
   private static final String VIDEO_FRAME_RATE_PROPERTY_NAME = "jfxmedia.decodedVideoFPS";
   private static final String DEFAULT_STYLE_CLASS = "media-view";
   private InvalidationListener errorListener;
   private InvalidationListener mediaDimensionListener;
   private VideoFrameRateListener decodedFrameRateListener;
   private boolean registerVideoFrameRateListener;
   private MediaPlayerOverlay mediaPlayerOverlay;
   private ChangeListener parentListener;
   private ChangeListener treeVisibleListener;
   private ChangeListener opacityListener;
   private ObjectProperty mediaPlayer;
   private ObjectProperty onError;
   private BooleanProperty preserveRatio;
   private BooleanProperty smooth;
   private DoubleProperty x;
   private DoubleProperty y;
   private DoubleProperty fitWidth;
   private DoubleProperty fitHeight;
   private ObjectProperty viewport;
   private int decodedFrameCount;
   private int renderedFrameCount;

   private VideoFrameRateListener createVideoFrameRateListener() {
      String var1 = null;

      try {
         var1 = System.getProperty("jfxmedia.decodedVideoFPS");
      } catch (Throwable var3) {
      }

      return var1 != null && Boolean.getBoolean("jfxmedia.decodedVideoFPS") ? (var1x) -> {
         Platform.runLater(() -> {
            ObservableMap var3 = this.getProperties();
            var3.put("jfxmedia.decodedVideoFPS", var1x);
         });
      } : null;
   }

   private void createListeners() {
      this.parentListener = (var1, var2, var3) -> {
         this.updateOverlayVisibility();
      };
      this.treeVisibleListener = (var1, var2, var3) -> {
         this.updateOverlayVisibility();
      };
      this.opacityListener = (var1, var2, var3) -> {
         this.updateOverlayOpacity();
      };
   }

   private boolean determineVisibility() {
      return this.getParent() != null && this.isVisible();
   }

   private synchronized void updateOverlayVisibility() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayVisible(this.determineVisibility());
      }

   }

   private synchronized void updateOverlayOpacity() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayOpacity(this.getOpacity());
      }

   }

   private synchronized void updateOverlayX() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayX(this.getX());
      }

   }

   private synchronized void updateOverlayY() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayY(this.getY());
      }

   }

   private synchronized void updateOverlayWidth() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayWidth(this.getFitWidth());
      }

   }

   private synchronized void updateOverlayHeight() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayHeight(this.getFitHeight());
      }

   }

   private synchronized void updateOverlayPreserveRatio() {
      if (this.mediaPlayerOverlay != null) {
         this.mediaPlayerOverlay.setOverlayPreserveRatio(this.isPreserveRatio());
      }

   }

   private static Affine3D calculateNodeToSceneTransform(Node var0) {
      Affine3D var1 = new Affine3D();

      do {
         var1.preConcatenate(((Node)var0).impl_getLeafTransform());
         var0 = ((Node)var0).getParent();
      } while(var0 != null);

      return var1;
   }

   private void updateOverlayTransform() {
      if (this.mediaPlayerOverlay != null) {
         Affine3D var1 = calculateNodeToSceneTransform(this);
         this.mediaPlayerOverlay.setOverlayTransform(var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getMxt(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getMyt(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getMzt());
      }

   }

   private void updateMediaPlayerOverlay() {
      this.mediaPlayerOverlay.setOverlayX(this.getX());
      this.mediaPlayerOverlay.setOverlayY(this.getY());
      this.mediaPlayerOverlay.setOverlayPreserveRatio(this.isPreserveRatio());
      this.mediaPlayerOverlay.setOverlayWidth(this.getFitWidth());
      this.mediaPlayerOverlay.setOverlayHeight(this.getFitHeight());
      this.mediaPlayerOverlay.setOverlayOpacity(this.getOpacity());
      this.mediaPlayerOverlay.setOverlayVisible(this.determineVisibility());
      this.updateOverlayTransform();
   }

   /** @deprecated */
   @Deprecated
   public void impl_transformsChanged() {
      super.impl_transformsChanged();
      if (this.mediaPlayerOverlay != null) {
         this.updateOverlayTransform();
      }

   }

   private MediaView getMediaView() {
      return this;
   }

   public MediaView() {
      this.errorListener = new MediaErrorInvalidationListener();
      this.mediaDimensionListener = (var1) -> {
         this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
         this.impl_geomChanged();
      };
      this.registerVideoFrameRateListener = false;
      this.mediaPlayerOverlay = null;
      this.getStyleClass().add("media-view");
      this.setSmooth(Toolkit.getToolkit().getDefaultImageSmooth());
      this.decodedFrameRateListener = this.createVideoFrameRateListener();
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
   }

   public MediaView(MediaPlayer var1) {
      this();
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      this.setMediaPlayer(var1);
   }

   public final void setMediaPlayer(MediaPlayer var1) {
      this.mediaPlayerProperty().set(var1);
   }

   public final MediaPlayer getMediaPlayer() {
      return this.mediaPlayer == null ? null : (MediaPlayer)this.mediaPlayer.get();
   }

   public final ObjectProperty mediaPlayerProperty() {
      if (this.mediaPlayer == null) {
         this.mediaPlayer = new ObjectPropertyBase() {
            MediaPlayer oldValue = null;

            protected void invalidated() {
               if (this.oldValue != null) {
                  Media var1 = this.oldValue.getMedia();
                  if (var1 != null) {
                     var1.widthProperty().removeListener(MediaView.this.mediaDimensionListener);
                     var1.heightProperty().removeListener(MediaView.this.mediaDimensionListener);
                  }

                  if (MediaView.this.decodedFrameRateListener != null && MediaView.this.getMediaPlayer().retrieveJfxPlayer() != null) {
                     MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().removeVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                  }

                  this.oldValue.errorProperty().removeListener(MediaView.this.errorListener);
                  this.oldValue.removeView(MediaView.this.getMediaView());
               }

               MediaPlayer var3 = (MediaPlayer)this.get();
               if (var3 != null) {
                  var3.addView(MediaView.this.getMediaView());
                  var3.errorProperty().addListener(MediaView.this.errorListener);
                  if (MediaView.this.decodedFrameRateListener != null && MediaView.this.getMediaPlayer().retrieveJfxPlayer() != null) {
                     MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().addVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                  } else if (MediaView.this.decodedFrameRateListener != null) {
                     MediaView.this.registerVideoFrameRateListener = true;
                  }

                  Media var2 = var3.getMedia();
                  if (var2 != null) {
                     var2.widthProperty().addListener(MediaView.this.mediaDimensionListener);
                     var2.heightProperty().addListener(MediaView.this.mediaDimensionListener);
                  }
               }

               MediaView.this.impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
               MediaView.this.impl_geomChanged();
               this.oldValue = var3;
            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "mediaPlayer";
            }
         };
      }

      return this.mediaPlayer;
   }

   public final void setOnError(EventHandler var1) {
      this.onErrorProperty().set(var1);
   }

   public final EventHandler getOnError() {
      return this.onError == null ? null : (EventHandler)this.onError.get();
   }

   public final ObjectProperty onErrorProperty() {
      if (this.onError == null) {
         this.onError = new ObjectPropertyBase() {
            protected void invalidated() {
               MediaView.this.setEventHandler(MediaErrorEvent.MEDIA_ERROR, (EventHandler)this.get());
            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "onError";
            }
         };
      }

      return this.onError;
   }

   public final void setPreserveRatio(boolean var1) {
      this.preserveRatioProperty().set(var1);
   }

   public final boolean isPreserveRatio() {
      return this.preserveRatio == null ? true : this.preserveRatio.get();
   }

   public final BooleanProperty preserveRatioProperty() {
      if (this.preserveRatio == null) {
         this.preserveRatio = new BooleanPropertyBase(true) {
            protected void invalidated() {
               if (HostUtils.isIOS()) {
                  MediaView.this.updateOverlayPreserveRatio();
               } else {
                  MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                  MediaView.this.impl_geomChanged();
               }

            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "preserveRatio";
            }
         };
      }

      return this.preserveRatio;
   }

   public final void setSmooth(boolean var1) {
      this.smoothProperty().set(var1);
   }

   public final boolean isSmooth() {
      return this.smooth == null ? false : this.smooth.get();
   }

   public final BooleanProperty smoothProperty() {
      if (this.smooth == null) {
         this.smooth = new BooleanPropertyBase() {
            protected void invalidated() {
               MediaView.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "smooth";
            }
         };
      }

      return this.smooth;
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            protected void invalidated() {
               if (HostUtils.isIOS()) {
                  MediaView.this.updateOverlayX();
               } else {
                  MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                  MediaView.this.impl_geomChanged();
               }

            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      this.yProperty().set(var1);
   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            protected void invalidated() {
               if (HostUtils.isIOS()) {
                  MediaView.this.updateOverlayY();
               } else {
                  MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                  MediaView.this.impl_geomChanged();
               }

            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   public final void setFitWidth(double var1) {
      this.fitWidthProperty().set(var1);
   }

   public final double getFitWidth() {
      return this.fitWidth == null ? 0.0 : this.fitWidth.get();
   }

   public final DoubleProperty fitWidthProperty() {
      if (this.fitWidth == null) {
         this.fitWidth = new DoublePropertyBase() {
            protected void invalidated() {
               if (HostUtils.isIOS()) {
                  MediaView.this.updateOverlayWidth();
               } else {
                  MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                  MediaView.this.impl_geomChanged();
               }

            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "fitWidth";
            }
         };
      }

      return this.fitWidth;
   }

   public final void setFitHeight(double var1) {
      this.fitHeightProperty().set(var1);
   }

   public final double getFitHeight() {
      return this.fitHeight == null ? 0.0 : this.fitHeight.get();
   }

   public final DoubleProperty fitHeightProperty() {
      if (this.fitHeight == null) {
         this.fitHeight = new DoublePropertyBase() {
            protected void invalidated() {
               if (HostUtils.isIOS()) {
                  MediaView.this.updateOverlayHeight();
               } else {
                  MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                  MediaView.this.impl_geomChanged();
               }

            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "fitHeight";
            }
         };
      }

      return this.fitHeight;
   }

   public final void setViewport(Rectangle2D var1) {
      this.viewportProperty().set(var1);
   }

   public final Rectangle2D getViewport() {
      return this.viewport == null ? null : (Rectangle2D)this.viewport.get();
   }

   public final ObjectProperty viewportProperty() {
      if (this.viewport == null) {
         this.viewport = new ObjectPropertyBase() {
            protected void invalidated() {
               MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
               MediaView.this.impl_geomChanged();
            }

            public Object getBean() {
               return MediaView.this;
            }

            public String getName() {
               return "viewport";
            }
         };
      }

      return this.viewport;
   }

   void notifyMediaChange() {
      MediaPlayer var1 = this.getMediaPlayer();
      if (var1 != null) {
         NGMediaView var2 = (NGMediaView)this.impl_getPeer();
         var2.setMediaProvider(var1);
      }

      this.impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
      this.impl_geomChanged();
   }

   void notifyMediaSizeChange() {
      this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
      this.impl_geomChanged();
   }

   void notifyMediaFrameUpdated() {
      ++this.decodedFrameCount;
      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      NGMediaView var1 = new NGMediaView();
      var1.setFrameTracker(new MediaViewFrameTracker());
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      Media var3 = this.getMediaPlayer() == null ? null : this.getMediaPlayer().getMedia();
      double var4 = var3 != null ? (double)var3.getWidth() : 0.0;
      double var6 = var3 != null ? (double)var3.getHeight() : 0.0;
      double var8 = this.getFitWidth();
      double var10 = this.getFitHeight();
      double var12 = this.getViewport() != null ? this.getViewport().getWidth() : 0.0;
      double var14 = this.getViewport() != null ? this.getViewport().getHeight() : 0.0;
      if (var12 > 0.0 && var14 > 0.0) {
         var4 = var12;
         var6 = var14;
      }

      if (this.getFitWidth() <= 0.0 && this.getFitHeight() <= 0.0) {
         var8 = var4;
         var10 = var6;
      } else if (this.isPreserveRatio()) {
         if (this.getFitWidth() <= 0.0) {
            var8 = var6 > 0.0 ? var4 * (this.getFitHeight() / var6) : 0.0;
            var10 = this.getFitHeight();
         } else if (this.getFitHeight() <= 0.0) {
            var8 = this.getFitWidth();
            var10 = var4 > 0.0 ? var6 * (this.getFitWidth() / var4) : 0.0;
         } else {
            if (var4 == 0.0) {
               var4 = this.getFitWidth();
            }

            if (var6 == 0.0) {
               var6 = this.getFitHeight();
            }

            double var16 = Math.min(this.getFitWidth() / var4, this.getFitHeight() / var6);
            var8 = var4 * var16;
            var10 = var6 * var16;
         }
      } else if (this.getFitHeight() <= 0.0) {
         var10 = var6;
      } else if (this.getFitWidth() <= 0.0) {
         var8 = var4;
      }

      if (var10 < 1.0) {
         var10 = 1.0;
      }

      if (var8 < 1.0) {
         var8 = 1.0;
      }

      if (!(var8 <= 0.0) && !(var10 <= 0.0)) {
         var1 = var1.deriveWithNewBounds((float)this.getX(), (float)this.getY(), 0.0F, (float)(this.getX() + var8), (float)(this.getY() + var10), 0.0F);
         var1 = var2.transform(var1, var1);
         return var1;
      } else {
         return var1.makeEmpty();
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return true;
   }

   void updateViewport() {
      if (this.getMediaPlayer() != null) {
         NGMediaView var1 = (NGMediaView)this.impl_getPeer();
         if (this.getViewport() != null) {
            var1.setViewport((float)this.getFitWidth(), (float)this.getFitHeight(), (float)this.getViewport().getMinX(), (float)this.getViewport().getMinY(), (float)this.getViewport().getWidth(), (float)this.getViewport().getHeight(), this.isPreserveRatio());
         } else {
            var1.setViewport((float)this.getFitWidth(), (float)this.getFitHeight(), 0.0F, 0.0F, 0.0F, 0.0F, this.isPreserveRatio());
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGMediaView var1 = (NGMediaView)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         var1.setX((float)this.getX());
         var1.setY((float)this.getY());
      }

      if (this.impl_isDirty(DirtyBits.NODE_SMOOTH)) {
         var1.setSmooth(this.isSmooth());
      }

      if (this.impl_isDirty(DirtyBits.NODE_VIEWPORT)) {
         this.updateViewport();
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1.renderNextFrame();
      }

      if (this.impl_isDirty(DirtyBits.MEDIAVIEW_MEDIA)) {
         MediaPlayer var2 = this.getMediaPlayer();
         if (var2 != null) {
            var1.setMediaProvider(var2);
            this.updateViewport();
         } else {
            var1.setMediaProvider((Object)null);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_perfReset() {
      this.decodedFrameCount = 0;
      this.renderedFrameCount = 0;
   }

   /** @deprecated */
   @Deprecated
   public int impl_perfGetDecodedFrameCount() {
      return this.decodedFrameCount;
   }

   /** @deprecated */
   @Deprecated
   public int impl_perfGetRenderedFrameCount() {
      return this.renderedFrameCount;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processLeafNode(this, var2);
   }

   void _mediaPlayerOnReady() {
      com.sun.media.jfxmedia.MediaPlayer var1 = this.getMediaPlayer().retrieveJfxPlayer();
      if (var1 != null) {
         if (this.decodedFrameRateListener != null && this.registerVideoFrameRateListener) {
            var1.getVideoRenderControl().addVideoFrameRateListener(this.decodedFrameRateListener);
            this.registerVideoFrameRateListener = false;
         }

         this.mediaPlayerOverlay = var1.getMediaPlayerOverlay();
         if (this.mediaPlayerOverlay != null) {
            this.createListeners();
            this.parentProperty().addListener(this.parentListener);
            this.impl_treeVisibleProperty().addListener(this.treeVisibleListener);
            this.opacityProperty().addListener(this.opacityListener);
            synchronized(this) {
               this.updateMediaPlayerOverlay();
            }
         }
      }

   }

   private class MediaViewFrameTracker implements MediaFrameTracker {
      private MediaViewFrameTracker() {
      }

      public void incrementDecodedFrameCount(int var1) {
         MediaView.this.decodedFrameCount = MediaView.this.decodedFrameCount + var1;
      }

      public void incrementRenderedFrameCount(int var1) {
         MediaView.this.renderedFrameCount = MediaView.this.renderedFrameCount + var1;
      }

      // $FF: synthetic method
      MediaViewFrameTracker(Object var2) {
         this();
      }
   }

   private class MediaErrorInvalidationListener implements InvalidationListener {
      private MediaErrorInvalidationListener() {
      }

      public void invalidated(Observable var1) {
         ObservableObjectValue var2 = (ObservableObjectValue)var1;
         MediaView.this.fireEvent(new MediaErrorEvent(MediaView.this.getMediaPlayer(), MediaView.this.getMediaView(), (MediaException)var2.get()));
      }

      // $FF: synthetic method
      MediaErrorInvalidationListener(Object var2) {
         this();
      }
   }
}
