package javafx.scene.media;

import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MetadataListener;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.util.Duration;

public final class Media {
   private ReadOnlyObjectWrapper error;
   private ObjectProperty onError;
   private MetadataListener metadataListener = new _MetadataListener();
   private ObservableMap metadata;
   private final ObservableMap metadataBacking = FXCollections.observableMap(new HashMap());
   private ReadOnlyIntegerWrapper width;
   private ReadOnlyIntegerWrapper height;
   private ReadOnlyObjectWrapper duration;
   private ObservableList tracks;
   private final ObservableList tracksBacking = FXCollections.observableArrayList();
   private ObservableMap markers = FXCollections.observableMap(new HashMap());
   private final String source;
   private final Locator jfxLocator;
   private MetadataParser jfxParser;

   private void setError(MediaException var1) {
      if (this.getError() == null) {
         this.errorPropertyImpl().set(var1);
      }

   }

   public final MediaException getError() {
      return this.error == null ? null : (MediaException)this.error.get();
   }

   public ReadOnlyObjectProperty errorProperty() {
      return this.errorPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper errorPropertyImpl() {
      if (this.error == null) {
         this.error = new ReadOnlyObjectWrapper() {
            protected void invalidated() {
               if (Media.this.getOnError() != null) {
                  Platform.runLater(Media.this.getOnError());
               }

            }

            public Object getBean() {
               return Media.this;
            }

            public String getName() {
               return "error";
            }
         };
      }

      return this.error;
   }

   public final void setOnError(Runnable var1) {
      this.onErrorProperty().set(var1);
   }

   public final Runnable getOnError() {
      return this.onError == null ? null : (Runnable)this.onError.get();
   }

   public ObjectProperty onErrorProperty() {
      if (this.onError == null) {
         this.onError = new ObjectPropertyBase() {
            protected void invalidated() {
               if (this.get() != null && Media.this.getError() != null) {
                  Platform.runLater((Runnable)this.get());
               }

            }

            public Object getBean() {
               return Media.this;
            }

            public String getName() {
               return "onError";
            }
         };
      }

      return this.onError;
   }

   public final ObservableMap getMetadata() {
      return this.metadata;
   }

   final void setWidth(int var1) {
      this.widthPropertyImpl().set(var1);
   }

   public final int getWidth() {
      return this.width == null ? 0 : this.width.get();
   }

   public ReadOnlyIntegerProperty widthProperty() {
      return this.widthPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyIntegerWrapper widthPropertyImpl() {
      if (this.width == null) {
         this.width = new ReadOnlyIntegerWrapper(this, "width");
      }

      return this.width;
   }

   final void setHeight(int var1) {
      this.heightPropertyImpl().set(var1);
   }

   public final int getHeight() {
      return this.height == null ? 0 : this.height.get();
   }

   public ReadOnlyIntegerProperty heightProperty() {
      return this.heightPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyIntegerWrapper heightPropertyImpl() {
      if (this.height == null) {
         this.height = new ReadOnlyIntegerWrapper(this, "height");
      }

      return this.height;
   }

   final void setDuration(Duration var1) {
      this.durationPropertyImpl().set(var1);
   }

   public final Duration getDuration() {
      return this.duration != null && this.duration.get() != null ? (Duration)this.duration.get() : Duration.UNKNOWN;
   }

   public ReadOnlyObjectProperty durationProperty() {
      return this.durationPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper durationPropertyImpl() {
      if (this.duration == null) {
         this.duration = new ReadOnlyObjectWrapper(this, "duration");
      }

      return this.duration;
   }

   public final ObservableList getTracks() {
      return this.tracks;
   }

   public final ObservableMap getMarkers() {
      return this.markers;
   }

   public Media(@NamedArg("source") String var1) {
      this.source = var1;
      URI var2 = null;

      try {
         var2 = new URI(var1);
      } catch (URISyntaxException var10) {
         throw new IllegalArgumentException(var10);
      }

      this.metadata = FXCollections.unmodifiableObservableMap(this.metadataBacking);
      this.tracks = FXCollections.unmodifiableObservableList(this.tracksBacking);
      Locator var3 = null;

      try {
         var3 = new Locator(var2);
         this.jfxLocator = var3;
         if (var3.canBlock()) {
            InitLocator var4 = new InitLocator();
            Thread var5 = new Thread(var4);
            var5.setDaemon(true);
            var5.start();
         } else {
            var3.init();
            this.runMetadataParser();
         }

      } catch (URISyntaxException var6) {
         throw new IllegalArgumentException(var6);
      } catch (FileNotFoundException var7) {
         throw new MediaException(MediaException.Type.MEDIA_UNAVAILABLE, var7.getMessage());
      } catch (IOException var8) {
         throw new MediaException(MediaException.Type.MEDIA_INACCESSIBLE, var8.getMessage());
      } catch (com.sun.media.jfxmedia.MediaException var9) {
         throw new MediaException(MediaException.Type.MEDIA_UNSUPPORTED, var9.getMessage());
      }
   }

   private void runMetadataParser() {
      try {
         this.jfxParser = MediaManager.getMetadataParser(this.jfxLocator);
         this.jfxParser.addListener(this.metadataListener);
         this.jfxParser.startParser();
      } catch (Exception var2) {
         this.jfxParser = null;
      }

   }

   public String getSource() {
      return this.source;
   }

   Locator retrieveJfxLocator() {
      return this.jfxLocator;
   }

   private Track getTrackWithID(long var1) {
      Iterator var3 = this.tracksBacking.iterator();

      Track var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (Track)var3.next();
      } while(var4.getTrackID() != var1);

      return var4;
   }

   void _updateMedia(com.sun.media.jfxmedia.Media var1) {
      try {
         List var2 = var1.getTracks();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               com.sun.media.jfxmedia.track.Track var4 = (com.sun.media.jfxmedia.track.Track)var3.next();
               long var5 = var4.getTrackID();
               if (this.getTrackWithID(var5) == null) {
                  Object var7 = null;
                  HashMap var8 = new HashMap();
                  if (null != var4.getName()) {
                     var8.put("name", var4.getName());
                  }

                  if (null != var4.getLocale()) {
                     var8.put("locale", var4.getLocale());
                  }

                  var8.put("encoding", var4.getEncodingType().toString());
                  var8.put("enabled", var4.isEnabled());
                  if (var4 instanceof com.sun.media.jfxmedia.track.VideoTrack) {
                     com.sun.media.jfxmedia.track.VideoTrack var9 = (com.sun.media.jfxmedia.track.VideoTrack)var4;
                     int var10 = var9.getFrameSize().getWidth();
                     int var11 = var9.getFrameSize().getHeight();
                     this.setWidth(var10);
                     this.setHeight(var11);
                     var8.put("video width", var10);
                     var8.put("video height", var11);
                     var7 = new VideoTrack(var4.getTrackID(), var8);
                  } else if (var4 instanceof com.sun.media.jfxmedia.track.AudioTrack) {
                     var7 = new AudioTrack(var4.getTrackID(), var8);
                  } else if (var4 instanceof com.sun.media.jfxmedia.track.SubtitleTrack) {
                     var7 = new SubtitleTrack(var5, var8);
                  }

                  if (null != var7) {
                     this.tracksBacking.add(var7);
                  }
               }
            }
         }
      } catch (Exception var12) {
         this.setError(new MediaException(MediaException.Type.UNKNOWN, var12));
      }

   }

   void _setError(MediaException.Type var1, String var2) {
      this.setError(new MediaException(var1, var2));
   }

   private synchronized void updateMetadata(Map var1) {
      if (var1 != null) {
         Iterator var2 = var1.entrySet().iterator();

         while(true) {
            while(var2.hasNext()) {
               Map.Entry var3 = (Map.Entry)var2.next();
               String var4 = (String)var3.getKey();
               Object var5 = var3.getValue();
               if (var4.equals("image") && var5 instanceof byte[]) {
                  byte[] var8 = (byte[])((byte[])var5);
                  Image var7 = new Image(new ByteArrayInputStream(var8));
                  if (!var7.isError()) {
                     this.metadataBacking.put("image", var7);
                  }
               } else if (var4.equals("duration") && var5 instanceof Long) {
                  Duration var6 = new Duration((double)(Long)var5);
                  if (var6 != null) {
                     this.metadataBacking.put("duration", var6);
                  }
               } else {
                  this.metadataBacking.put(var4, var5);
               }
            }

            return;
         }
      }
   }

   private class InitLocator implements Runnable {
      private InitLocator() {
      }

      public void run() {
         try {
            Media.this.jfxLocator.init();
            Media.this.runMetadataParser();
         } catch (URISyntaxException var2) {
            Media.this._setError(MediaException.Type.OPERATION_UNSUPPORTED, var2.getMessage());
         } catch (FileNotFoundException var3) {
            Media.this._setError(MediaException.Type.MEDIA_UNAVAILABLE, var3.getMessage());
         } catch (IOException var4) {
            Media.this._setError(MediaException.Type.MEDIA_INACCESSIBLE, var4.getMessage());
         } catch (com.sun.media.jfxmedia.MediaException var5) {
            Media.this._setError(MediaException.Type.MEDIA_UNSUPPORTED, var5.getMessage());
         } catch (Exception var6) {
            Media.this._setError(MediaException.Type.UNKNOWN, var6.getMessage());
         }

      }

      // $FF: synthetic method
      InitLocator(Object var2) {
         this();
      }
   }

   private class _MetadataListener implements MetadataListener {
      private _MetadataListener() {
      }

      public void onMetadata(Map var1) {
         Platform.runLater(() -> {
            Media.this.updateMetadata(var1);
            Media.this.jfxParser.removeListener(Media.this.metadataListener);
            Media.this.jfxParser.stopParser();
            Media.this.jfxParser = null;
         });
      }

      // $FF: synthetic method
      _MetadataListener(Object var2) {
         this();
      }
   }
}
