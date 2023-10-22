package javafx.scene.media;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public final class EqualizerBand {
   public static final double MIN_GAIN = -24.0;
   public static final double MAX_GAIN = 12.0;
   private final Object disposeLock = new Object();
   private com.sun.media.jfxmedia.effects.EqualizerBand jfxBand;
   private DoubleProperty centerFrequency;
   private DoubleProperty bandwidth;
   private DoubleProperty gain;

   public EqualizerBand() {
   }

   public EqualizerBand(double var1, double var3, double var5) {
      this.setCenterFrequency(var1);
      this.setBandwidth(var3);
      this.setGain(var5);
   }

   void setJfxBand(com.sun.media.jfxmedia.effects.EqualizerBand var1) {
      synchronized(this.disposeLock) {
         this.jfxBand = var1;
      }
   }

   public final void setCenterFrequency(double var1) {
      this.centerFrequencyProperty().set(var1);
   }

   public final double getCenterFrequency() {
      return this.centerFrequency == null ? 0.0 : this.centerFrequency.get();
   }

   public DoubleProperty centerFrequencyProperty() {
      if (this.centerFrequency == null) {
         this.centerFrequency = new DoublePropertyBase() {
            protected void invalidated() {
               synchronized(EqualizerBand.this.disposeLock) {
                  double var2 = EqualizerBand.this.centerFrequency.get();
                  if (EqualizerBand.this.jfxBand != null && var2 > 0.0) {
                     EqualizerBand.this.jfxBand.setCenterFrequency(var2);
                  }

               }
            }

            public Object getBean() {
               return EqualizerBand.this;
            }

            public String getName() {
               return "centerFrequency";
            }
         };
      }

      return this.centerFrequency;
   }

   public final void setBandwidth(double var1) {
      this.bandwidthProperty().set(var1);
   }

   public final double getBandwidth() {
      return this.bandwidth == null ? 0.0 : this.bandwidth.get();
   }

   public DoubleProperty bandwidthProperty() {
      if (this.bandwidth == null) {
         this.bandwidth = new DoublePropertyBase() {
            protected void invalidated() {
               synchronized(EqualizerBand.this.disposeLock) {
                  double var2 = EqualizerBand.this.bandwidth.get();
                  if (EqualizerBand.this.jfxBand != null && var2 > 0.0) {
                     EqualizerBand.this.jfxBand.setBandwidth(var2);
                  }

               }
            }

            public Object getBean() {
               return EqualizerBand.this;
            }

            public String getName() {
               return "bandwidth";
            }
         };
      }

      return this.bandwidth;
   }

   public final void setGain(double var1) {
      this.gainProperty().set(var1);
   }

   public final double getGain() {
      return this.gain == null ? 0.0 : this.gain.get();
   }

   public DoubleProperty gainProperty() {
      if (this.gain == null) {
         this.gain = new DoublePropertyBase() {
            protected void invalidated() {
               synchronized(EqualizerBand.this.disposeLock) {
                  if (EqualizerBand.this.jfxBand != null) {
                     EqualizerBand.this.jfxBand.setGain(EqualizerBand.this.gain.get());
                  }

               }
            }

            public Object getBean() {
               return EqualizerBand.this;
            }

            public String getName() {
               return "gain";
            }
         };
      }

      return this.gain;
   }
}
