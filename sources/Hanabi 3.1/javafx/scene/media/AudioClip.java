package javafx.scene.media;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;

public final class AudioClip {
   private String sourceURL;
   private com.sun.media.jfxmedia.AudioClip audioClip;
   private DoubleProperty volume;
   private DoubleProperty balance;
   private DoubleProperty rate;
   private DoubleProperty pan;
   private IntegerProperty priority;
   public static final int INDEFINITE = -1;
   private IntegerProperty cycleCount;

   public AudioClip(@NamedArg("source") String var1) {
      URI var2 = URI.create(var1);
      this.sourceURL = var1;

      try {
         this.audioClip = com.sun.media.jfxmedia.AudioClip.load(var2);
      } catch (URISyntaxException var4) {
         throw new IllegalArgumentException(var4);
      } catch (FileNotFoundException var5) {
         throw new MediaException(MediaException.Type.MEDIA_UNAVAILABLE, var5.getMessage());
      } catch (IOException var6) {
         throw new MediaException(MediaException.Type.MEDIA_INACCESSIBLE, var6.getMessage());
      } catch (com.sun.media.jfxmedia.MediaException var7) {
         throw new MediaException(MediaException.Type.MEDIA_UNSUPPORTED, var7.getMessage());
      }
   }

   public String getSource() {
      return this.sourceURL;
   }

   public final void setVolume(double var1) {
      this.volumeProperty().set(var1);
   }

   public final double getVolume() {
      return null == this.volume ? 1.0 : this.volume.get();
   }

   public DoubleProperty volumeProperty() {
      if (this.volume == null) {
         this.volume = new DoublePropertyBase(1.0) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  AudioClip.this.audioClip.setVolume(AudioClip.this.volume.get());
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "volume";
            }
         };
      }

      return this.volume;
   }

   public void setBalance(double var1) {
      this.balanceProperty().set(var1);
   }

   public double getBalance() {
      return null != this.balance ? this.balance.get() : 0.0;
   }

   public DoubleProperty balanceProperty() {
      if (null == this.balance) {
         this.balance = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  AudioClip.this.audioClip.setBalance(AudioClip.this.balance.get());
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "balance";
            }
         };
      }

      return this.balance;
   }

   public void setRate(double var1) {
      this.rateProperty().set(var1);
   }

   public double getRate() {
      return null != this.rate ? this.rate.get() : 1.0;
   }

   public DoubleProperty rateProperty() {
      if (null == this.rate) {
         this.rate = new DoublePropertyBase(1.0) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  AudioClip.this.audioClip.setPlaybackRate(AudioClip.this.rate.get());
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "rate";
            }
         };
      }

      return this.rate;
   }

   public void setPan(double var1) {
      this.panProperty().set(var1);
   }

   public double getPan() {
      return null != this.pan ? this.pan.get() : 0.0;
   }

   public DoubleProperty panProperty() {
      if (null == this.pan) {
         this.pan = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  AudioClip.this.audioClip.setPan(AudioClip.this.pan.get());
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "pan";
            }
         };
      }

      return this.pan;
   }

   public void setPriority(int var1) {
      this.priorityProperty().set(var1);
   }

   public int getPriority() {
      return null != this.priority ? this.priority.get() : 0;
   }

   public IntegerProperty priorityProperty() {
      if (null == this.priority) {
         this.priority = new IntegerPropertyBase(0) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  AudioClip.this.audioClip.setPriority(AudioClip.this.priority.get());
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "priority";
            }
         };
      }

      return this.priority;
   }

   public void setCycleCount(int var1) {
      this.cycleCountProperty().set(var1);
   }

   public int getCycleCount() {
      return null != this.cycleCount ? this.cycleCount.get() : 1;
   }

   public IntegerProperty cycleCountProperty() {
      if (null == this.cycleCount) {
         this.cycleCount = new IntegerPropertyBase(1) {
            protected void invalidated() {
               if (null != AudioClip.this.audioClip) {
                  int var1 = AudioClip.this.cycleCount.get();
                  if (-1 != var1) {
                     var1 = Math.max(1, var1);
                     AudioClip.this.audioClip.setLoopCount(var1 - 1);
                  } else {
                     AudioClip.this.audioClip.setLoopCount(var1);
                  }
               }

            }

            public Object getBean() {
               return AudioClip.this;
            }

            public String getName() {
               return "cycleCount";
            }
         };
      }

      return this.cycleCount;
   }

   public void play() {
      if (null != this.audioClip) {
         this.audioClip.play();
      }

   }

   public void play(double var1) {
      if (null != this.audioClip) {
         this.audioClip.play(var1);
      }

   }

   public void play(double var1, double var3, double var5, double var7, int var9) {
      if (null != this.audioClip) {
         this.audioClip.play(var1, var3, var5, var7, this.audioClip.loopCount(), var9);
      }

   }

   public boolean isPlaying() {
      return null != this.audioClip && this.audioClip.isPlaying();
   }

   public void stop() {
      if (null != this.audioClip) {
         this.audioClip.stop();
      }

   }
}
