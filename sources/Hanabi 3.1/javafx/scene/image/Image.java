package javafx.scene.image;

import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Image {
   private static final Pattern URL_QUICKMATCH;
   private final String url;
   private final InputStream impl_source;
   private ReadOnlyDoubleWrapper progress;
   private final double requestedWidth;
   private final double requestedHeight;
   private DoublePropertyImpl width;
   private DoublePropertyImpl height;
   private final boolean preserveRatio;
   private final boolean smooth;
   private final boolean backgroundLoading;
   private ReadOnlyBooleanWrapper error;
   private ReadOnlyObjectWrapper exception;
   /** @deprecated */
   private ObjectPropertyImpl platformImage;
   private ImageTask backgroundTask;
   private Animation animation;
   private PlatformImage[] animFrames;
   private static final int MAX_RUNNING_TASKS = 4;
   private static int runningTasks;
   private static final Queue pendingTasks;
   private PixelReader reader;

   /** @deprecated */
   @Deprecated
   public final String impl_getUrl() {
      return this.url;
   }

   final InputStream getImpl_source() {
      return this.impl_source;
   }

   final void setProgress(double var1) {
      this.progressPropertyImpl().set(var1);
   }

   public final double getProgress() {
      return this.progress == null ? 0.0 : this.progress.get();
   }

   public final ReadOnlyDoubleProperty progressProperty() {
      return this.progressPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper progressPropertyImpl() {
      if (this.progress == null) {
         this.progress = new ReadOnlyDoubleWrapper(this, "progress");
      }

      return this.progress;
   }

   public final double getRequestedWidth() {
      return this.requestedWidth;
   }

   public final double getRequestedHeight() {
      return this.requestedHeight;
   }

   public final double getWidth() {
      return this.width == null ? 0.0 : this.width.get();
   }

   public final ReadOnlyDoubleProperty widthProperty() {
      return this.widthPropertyImpl();
   }

   private DoublePropertyImpl widthPropertyImpl() {
      if (this.width == null) {
         this.width = new DoublePropertyImpl("width");
      }

      return this.width;
   }

   public final double getHeight() {
      return this.height == null ? 0.0 : this.height.get();
   }

   public final ReadOnlyDoubleProperty heightProperty() {
      return this.heightPropertyImpl();
   }

   private DoublePropertyImpl heightPropertyImpl() {
      if (this.height == null) {
         this.height = new DoublePropertyImpl("height");
      }

      return this.height;
   }

   public final boolean isPreserveRatio() {
      return this.preserveRatio;
   }

   public final boolean isSmooth() {
      return this.smooth;
   }

   public final boolean isBackgroundLoading() {
      return this.backgroundLoading;
   }

   private void setError(boolean var1) {
      this.errorPropertyImpl().set(var1);
   }

   public final boolean isError() {
      return this.error == null ? false : this.error.get();
   }

   public final ReadOnlyBooleanProperty errorProperty() {
      return this.errorPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper errorPropertyImpl() {
      if (this.error == null) {
         this.error = new ReadOnlyBooleanWrapper(this, "error");
      }

      return this.error;
   }

   private void setException(Exception var1) {
      this.exceptionPropertyImpl().set(var1);
   }

   public final Exception getException() {
      return this.exception == null ? null : (Exception)this.exception.get();
   }

   public final ReadOnlyObjectProperty exceptionProperty() {
      return this.exceptionPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper exceptionPropertyImpl() {
      if (this.exception == null) {
         this.exception = new ReadOnlyObjectWrapper(this, "exception");
      }

      return this.exception;
   }

   /** @deprecated */
   @Deprecated
   public final Object impl_getPlatformImage() {
      return this.platformImage == null ? null : this.platformImage.get();
   }

   final ReadOnlyObjectProperty acc_platformImageProperty() {
      return this.platformImagePropertyImpl();
   }

   private ObjectPropertyImpl platformImagePropertyImpl() {
      if (this.platformImage == null) {
         this.platformImage = new ObjectPropertyImpl("platformImage");
      }

      return this.platformImage;
   }

   void pixelsDirty() {
      this.platformImagePropertyImpl().fireValueChangedEvent();
   }

   public Image(@NamedArg("url") String var1) {
      this(validateUrl(var1), (InputStream)null, 0.0, 0.0, false, false, false);
      this.initialize((Object)null);
   }

   public Image(@NamedArg("url") String var1, @NamedArg("backgroundLoading") boolean var2) {
      this(validateUrl(var1), (InputStream)null, 0.0, 0.0, false, false, var2);
      this.initialize((Object)null);
   }

   public Image(@NamedArg("url") String var1, @NamedArg("requestedWidth") double var2, @NamedArg("requestedHeight") double var4, @NamedArg("preserveRatio") boolean var6, @NamedArg("smooth") boolean var7) {
      this(validateUrl(var1), (InputStream)null, var2, var4, var6, var7, false);
      this.initialize((Object)null);
   }

   public Image(@NamedArg(value = "url",defaultValue = "\"\"") String var1, @NamedArg("requestedWidth") double var2, @NamedArg("requestedHeight") double var4, @NamedArg("preserveRatio") boolean var6, @NamedArg(value = "smooth",defaultValue = "true") boolean var7, @NamedArg("backgroundLoading") boolean var8) {
      this(validateUrl(var1), (InputStream)null, var2, var4, var6, var7, var8);
      this.initialize((Object)null);
   }

   public Image(@NamedArg("is") InputStream var1) {
      this((String)null, validateInputStream(var1), 0.0, 0.0, false, false, false);
      this.initialize((Object)null);
   }

   public Image(@NamedArg("is") InputStream var1, @NamedArg("requestedWidth") double var2, @NamedArg("requestedHeight") double var4, @NamedArg("preserveRatio") boolean var6, @NamedArg("smooth") boolean var7) {
      this((String)null, validateInputStream(var1), var2, var4, var6, var7, false);
      this.initialize((Object)null);
   }

   Image(int var1, int var2) {
      this((String)null, (InputStream)null, (double)var1, (double)var2, false, false, false);
      if (var1 > 0 && var2 > 0) {
         this.initialize(Toolkit.getToolkit().createPlatformImage(var1, var2));
      } else {
         throw new IllegalArgumentException("Image dimensions must be positive (w,h > 0)");
      }
   }

   private Image(Object var1) {
      this((String)null, (InputStream)null, 0.0, 0.0, false, false, false);
      this.initialize(var1);
   }

   private Image(String var1, InputStream var2, double var3, double var5, boolean var7, boolean var8, boolean var9) {
      this.url = var1;
      this.impl_source = var2;
      this.requestedWidth = var3;
      this.requestedHeight = var5;
      this.preserveRatio = var7;
      this.smooth = var8;
      this.backgroundLoading = var9;
   }

   public void cancel() {
      if (this.backgroundTask != null) {
         this.backgroundTask.cancel();
      }

   }

   void dispose() {
      this.cancel();
      if (this.animation != null) {
         this.animation.stop();
      }

   }

   private void initialize(Object var1) {
      ImageLoader var2;
      if (var1 != null) {
         var2 = loadPlatformImage(var1);
         this.finishImage(var2);
      } else if (this.isBackgroundLoading() && this.impl_source == null) {
         this.loadInBackground();
      } else {
         if (this.impl_source != null) {
            var2 = loadImage(this.impl_source, this.getRequestedWidth(), this.getRequestedHeight(), this.isPreserveRatio(), this.isSmooth());
         } else {
            var2 = loadImage(this.impl_getUrl(), this.getRequestedWidth(), this.getRequestedHeight(), this.isPreserveRatio(), this.isSmooth());
         }

         this.finishImage(var2);
      }

   }

   private void finishImage(ImageLoader var1) {
      Exception var2 = var1.getException();
      if (var2 != null) {
         this.finishImage(var2);
      } else {
         if (var1.getFrameCount() > 1) {
            this.initializeAnimatedImage(var1);
         } else {
            PlatformImage var3 = var1.getFrame(0);
            double var4 = (double)((float)var1.getWidth() / var3.getPixelScale());
            double var6 = (double)((float)var1.getHeight() / var3.getPixelScale());
            this.setPlatformImageWH(var3, var4, var6);
         }

         this.setProgress(1.0);
      }
   }

   private void finishImage(Exception var1) {
      this.setException(var1);
      this.setError(true);
      this.setPlatformImageWH((PlatformImage)null, 0.0, 0.0);
      this.setProgress(1.0);
   }

   private void initializeAnimatedImage(ImageLoader var1) {
      int var2 = var1.getFrameCount();
      this.animFrames = new PlatformImage[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.animFrames[var3] = var1.getFrame(var3);
      }

      PlatformImage var8 = var1.getFrame(0);
      double var4 = (double)((float)var1.getWidth() / var8.getPixelScale());
      double var6 = (double)((float)var1.getHeight() / var8.getPixelScale());
      this.setPlatformImageWH(var8, var4, var6);
      this.animation = new Animation(this, var1);
      this.animation.start();
   }

   private void cycleTasks() {
      synchronized(pendingTasks) {
         --runningTasks;
         ImageTask var2 = (ImageTask)pendingTasks.poll();
         if (var2 != null) {
            ++runningTasks;
            var2.start();
         }

      }
   }

   private void loadInBackground() {
      this.backgroundTask = new ImageTask();
      synchronized(pendingTasks) {
         if (runningTasks >= 4) {
            pendingTasks.offer(this.backgroundTask);
         } else {
            ++runningTasks;
            this.backgroundTask.start();
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public static Image impl_fromPlatformImage(Object var0) {
      return new Image(var0);
   }

   private void setPlatformImageWH(PlatformImage var1, double var2, double var4) {
      if (this.impl_getPlatformImage() != var1 || this.getWidth() != var2 || this.getHeight() != var4) {
         Object var6 = this.impl_getPlatformImage();
         double var7 = this.getWidth();
         double var9 = this.getHeight();
         this.storePlatformImageWH(var1, var2, var4);
         if (var6 != var1) {
            this.platformImagePropertyImpl().fireValueChangedEvent();
         }

         if (var7 != var2) {
            this.widthPropertyImpl().fireValueChangedEvent();
         }

         if (var9 != var4) {
            this.heightPropertyImpl().fireValueChangedEvent();
         }

      }
   }

   private void storePlatformImageWH(PlatformImage var1, double var2, double var4) {
      this.platformImagePropertyImpl().store(var1);
      this.widthPropertyImpl().store(var2);
      this.heightPropertyImpl().store(var4);
   }

   void setPlatformImage(PlatformImage var1) {
      this.platformImage.set(var1);
   }

   private static ImageLoader loadImage(String var0, double var1, double var3, boolean var5, boolean var6) {
      return Toolkit.getToolkit().loadImage(var0, (int)var1, (int)var3, var5, var6);
   }

   private static ImageLoader loadImage(InputStream var0, double var1, double var3, boolean var5, boolean var6) {
      return Toolkit.getToolkit().loadImage(var0, (int)var1, (int)var3, var5, var6);
   }

   private static AsyncOperation loadImageAsync(AsyncOperationListener var0, String var1, double var2, double var4, boolean var6, boolean var7) {
      return Toolkit.getToolkit().loadImageAsync(var0, var1, (int)var2, (int)var4, var6, var7);
   }

   private static ImageLoader loadPlatformImage(Object var0) {
      return Toolkit.getToolkit().loadPlatformImage(var0);
   }

   private static String validateUrl(String var0) {
      if (var0 == null) {
         throw new NullPointerException("URL must not be null");
      } else if (var0.trim().isEmpty()) {
         throw new IllegalArgumentException("URL must not be empty");
      } else {
         try {
            if (!URL_QUICKMATCH.matcher(var0).matches()) {
               ClassLoader var1 = Thread.currentThread().getContextClassLoader();
               URL var2;
               if (var0.charAt(0) == '/') {
                  var2 = var1.getResource(var0.substring(1));
               } else {
                  var2 = var1.getResource(var0);
               }

               if (var2 == null) {
                  throw new IllegalArgumentException("Invalid URL or resource not found");
               } else {
                  return var2.toString();
               }
            } else {
               return (new URL(var0)).toString();
            }
         } catch (IllegalArgumentException var3) {
            throw new IllegalArgumentException(constructDetailedExceptionMessage("Invalid URL", var3), var3);
         } catch (MalformedURLException var4) {
            throw new IllegalArgumentException(constructDetailedExceptionMessage("Invalid URL", var4), var4);
         }
      }
   }

   private static InputStream validateInputStream(InputStream var0) {
      if (var0 == null) {
         throw new NullPointerException("Input stream must not be null");
      } else {
         return var0;
      }
   }

   private static String constructDetailedExceptionMessage(String var0, Throwable var1) {
      if (var1 == null) {
         return var0;
      } else {
         String var2 = var1.getMessage();
         return constructDetailedExceptionMessage(var2 != null ? var0 + ": " + var2 : var0, var1.getCause());
      }
   }

   boolean isAnimation() {
      return this.animation != null;
   }

   boolean pixelsReadable() {
      return this.getProgress() >= 1.0 && !this.isAnimation() && !this.isError();
   }

   public final PixelReader getPixelReader() {
      if (!this.pixelsReadable()) {
         return null;
      } else {
         if (this.reader == null) {
            this.reader = new PixelReader() {
               public PixelFormat getPixelFormat() {
                  PlatformImage var1 = (PlatformImage)Image.this.platformImage.get();
                  return var1.getPlatformPixelFormat();
               }

               public int getArgb(int var1, int var2) {
                  PlatformImage var3 = (PlatformImage)Image.this.platformImage.get();
                  return var3.getArgb(var1, var2);
               }

               public Color getColor(int var1, int var2) {
                  int var3 = this.getArgb(var1, var2);
                  int var4 = var3 >>> 24;
                  int var5 = var3 >> 16 & 255;
                  int var6 = var3 >> 8 & 255;
                  int var7 = var3 & 255;
                  return Color.rgb(var5, var6, var7, (double)var4 / 255.0);
               }

               public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7) {
                  PlatformImage var8 = (PlatformImage)Image.this.platformImage.get();
                  var8.getPixels(var1, var2, var3, var4, var5, var6, var7);
               }

               public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8) {
                  PlatformImage var9 = (PlatformImage)Image.this.platformImage.get();
                  var9.getPixels(var1, var2, var3, var4, var5, var6, var7, var8);
               }

               public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8) {
                  PlatformImage var9 = (PlatformImage)Image.this.platformImage.get();
                  var9.getPixels(var1, var2, var3, var4, var5, var6, var7, var8);
               }
            };
         }

         return this.reader;
      }
   }

   PlatformImage getWritablePlatformImage() {
      PlatformImage var1 = (PlatformImage)this.platformImage.get();
      if (!var1.isWritable()) {
         var1 = var1.promoteToWritableImage();
         this.platformImage.set(var1);
      }

      return var1;
   }

   static {
      Toolkit.setImageAccessor(new Toolkit.ImageAccessor() {
         public boolean isAnimation(Image var1) {
            return var1.isAnimation();
         }

         public ReadOnlyObjectProperty getImageProperty(Image var1) {
            return var1.acc_platformImageProperty();
         }

         public int[] getPreColors(PixelFormat var1) {
            return ((PixelFormat.IndexedPixelFormat)var1).getPreColors();
         }

         public int[] getNonPreColors(PixelFormat var1) {
            return ((PixelFormat.IndexedPixelFormat)var1).getNonPreColors();
         }
      });
      URL_QUICKMATCH = Pattern.compile("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$");
      runningTasks = 0;
      pendingTasks = new LinkedList();
   }

   private final class ImageTask implements AsyncOperationListener {
      private final AsyncOperation peer = this.constructPeer();

      public ImageTask() {
      }

      public void onCancel() {
         Image.this.finishImage((Exception)(new CancellationException("Loading cancelled")));
         Image.this.cycleTasks();
      }

      public void onException(Exception var1) {
         Image.this.finishImage(var1);
         Image.this.cycleTasks();
      }

      public void onCompletion(ImageLoader var1) {
         Image.this.finishImage(var1);
         Image.this.cycleTasks();
      }

      public void onProgress(int var1, int var2) {
         if (var2 > 0) {
            double var3 = (double)var1 / (double)var2;
            if (var3 < 1.0 && var3 >= Image.this.getProgress() + 0.1) {
               Image.this.setProgress(var3);
            }
         }

      }

      public void start() {
         this.peer.start();
      }

      public void cancel() {
         this.peer.cancel();
      }

      private AsyncOperation constructPeer() {
         return Image.loadImageAsync(this, Image.this.url, Image.this.requestedWidth, Image.this.requestedHeight, Image.this.preserveRatio, Image.this.smooth);
      }
   }

   private static final class Animation {
      final WeakReference imageRef;
      final Timeline timeline;
      final SimpleIntegerProperty frameIndex = new SimpleIntegerProperty() {
         protected void invalidated() {
            Animation.this.updateImage(this.get());
         }
      };

      public Animation(Image var1, ImageLoader var2) {
         this.imageRef = new WeakReference(var1);
         this.timeline = new Timeline();
         int var3 = var2.getLoopCount();
         this.timeline.setCycleCount(var3 == 0 ? -1 : var3);
         int var4 = var2.getFrameCount();
         int var5 = 0;

         for(int var6 = 0; var6 < var4; ++var6) {
            this.addKeyFrame(var6, (double)var5);
            var5 += var2.getFrameDelay(var6);
         }

         this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis((double)var5), new KeyValue[0]));
      }

      public void start() {
         this.timeline.play();
      }

      public void stop() {
         this.timeline.stop();
      }

      private void updateImage(int var1) {
         Image var2 = (Image)this.imageRef.get();
         if (var2 != null) {
            var2.platformImagePropertyImpl().set(var2.animFrames[var1]);
         } else {
            this.timeline.stop();
         }

      }

      private void addKeyFrame(int var1, double var2) {
         this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(var2), new KeyValue[]{new KeyValue(this.frameIndex, var1, Interpolator.DISCRETE)}));
      }
   }

   private final class ObjectPropertyImpl extends ReadOnlyObjectPropertyBase {
      private final String name;
      private Object value;
      private boolean valid = true;

      public ObjectPropertyImpl(String var2) {
         this.name = var2;
      }

      public void store(Object var1) {
         this.value = var1;
      }

      public void set(Object var1) {
         if (this.value != var1) {
            this.value = var1;
            this.markInvalid();
         }

      }

      public void fireValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      private void markInvalid() {
         if (this.valid) {
            this.valid = false;
            this.fireValueChangedEvent();
         }

      }

      public Object get() {
         this.valid = true;
         return this.value;
      }

      public Object getBean() {
         return Image.this;
      }

      public String getName() {
         return this.name;
      }
   }

   private final class DoublePropertyImpl extends ReadOnlyDoublePropertyBase {
      private final String name;
      private double value;

      public DoublePropertyImpl(String var2) {
         this.name = var2;
      }

      public void store(double var1) {
         this.value = var1;
      }

      public void fireValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      public double get() {
         return this.value;
      }

      public Object getBean() {
         return Image.this;
      }

      public String getName() {
         return this.name;
      }
   }
}
