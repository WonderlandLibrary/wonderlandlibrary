package javafx.scene.media;

import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.media.jfxmedia.logging.Logger;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class AudioEqualizer {
   public static final int MAX_NUM_BANDS = 64;
   private com.sun.media.jfxmedia.effects.AudioEqualizer jfxEqualizer = null;
   private final ObservableList bands = new Bands();
   private final Object disposeLock = new Object();
   private BooleanProperty enabled;

   public final ObservableList getBands() {
      return this.bands;
   }

   AudioEqualizer() {
      this.bands.addAll(new EqualizerBand(32.0, 19.0, 0.0), new EqualizerBand(64.0, 39.0, 0.0), new EqualizerBand(125.0, 78.0, 0.0), new EqualizerBand(250.0, 156.0, 0.0), new EqualizerBand(500.0, 312.0, 0.0), new EqualizerBand(1000.0, 625.0, 0.0), new EqualizerBand(2000.0, 1250.0, 0.0), new EqualizerBand(4000.0, 2500.0, 0.0), new EqualizerBand(8000.0, 5000.0, 0.0), new EqualizerBand(16000.0, 10000.0, 0.0));
   }

   void setAudioEqualizer(com.sun.media.jfxmedia.effects.AudioEqualizer var1) {
      synchronized(this.disposeLock) {
         if (this.jfxEqualizer != var1) {
            Iterator var3;
            EqualizerBand var4;
            if (this.jfxEqualizer != null && var1 == null) {
               this.jfxEqualizer.setEnabled(false);
               var3 = this.bands.iterator();

               while(var3.hasNext()) {
                  var4 = (EqualizerBand)var3.next();
                  var4.setJfxBand((com.sun.media.jfxmedia.effects.EqualizerBand)null);
               }

               this.jfxEqualizer = null;
            } else {
               this.jfxEqualizer = var1;
               var1.setEnabled(this.isEnabled());
               var3 = this.bands.iterator();

               while(true) {
                  while(var3.hasNext()) {
                     var4 = (EqualizerBand)var3.next();
                     if (var4.getCenterFrequency() > 0.0 && var4.getBandwidth() > 0.0) {
                        com.sun.media.jfxmedia.effects.EqualizerBand var5 = var1.addBand(var4.getCenterFrequency(), var4.getBandwidth(), var4.getGain());
                        var4.setJfxBand(var5);
                     } else {
                        Logger.logMsg(4, "Center frequency [" + var4.getCenterFrequency() + "] and bandwidth [" + var4.getBandwidth() + "] must be greater than 0.");
                     }
                  }

                  return;
               }
            }
         }
      }
   }

   public final void setEnabled(boolean var1) {
      this.enabledProperty().set(var1);
   }

   public final boolean isEnabled() {
      return this.enabled == null ? false : this.enabled.get();
   }

   public BooleanProperty enabledProperty() {
      if (this.enabled == null) {
         this.enabled = new BooleanPropertyBase() {
            protected void invalidated() {
               synchronized(AudioEqualizer.this.disposeLock) {
                  if (AudioEqualizer.this.jfxEqualizer != null) {
                     AudioEqualizer.this.jfxEqualizer.setEnabled(AudioEqualizer.this.enabled.get());
                  }

               }
            }

            public Object getBean() {
               return AudioEqualizer.this;
            }

            public String getName() {
               return "enabled";
            }
         };
      }

      return this.enabled;
   }

   private class Bands extends VetoableListDecorator {
      public Bands() {
         super(FXCollections.observableArrayList());
      }

      protected void onProposedChange(List var1, int[] var2) {
         synchronized(AudioEqualizer.this.disposeLock) {
            if (AudioEqualizer.this.jfxEqualizer != null) {
               for(int var4 = 0; var4 < var2.length; var4 += 2) {
                  Iterator var5 = this.subList(var2[var4], var2[var4 + 1]).iterator();

                  while(var5.hasNext()) {
                     EqualizerBand var6 = (EqualizerBand)var5.next();
                     AudioEqualizer.this.jfxEqualizer.removeBand(var6.getCenterFrequency());
                  }
               }

               Iterator var9 = var1.iterator();

               while(true) {
                  while(var9.hasNext()) {
                     EqualizerBand var10 = (EqualizerBand)var9.next();
                     if (var10.getCenterFrequency() > 0.0 && var10.getBandwidth() > 0.0) {
                        com.sun.media.jfxmedia.effects.EqualizerBand var11 = AudioEqualizer.this.jfxEqualizer.addBand(var10.getCenterFrequency(), var10.getBandwidth(), var10.getGain());
                        var10.setJfxBand(var11);
                     } else {
                        Logger.logMsg(4, "Center frequency [" + var10.getCenterFrequency() + "] and bandwidth [" + var10.getBandwidth() + "] must be greater than 0.");
                     }
                  }

                  return;
               }
            }
         }
      }
   }
}
