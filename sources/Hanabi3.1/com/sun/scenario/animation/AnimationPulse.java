package com.sun.scenario.animation;

import com.sun.javafx.tk.Toolkit;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AnimationPulse implements AnimationPulseMBean {
   private final Queue pulseDataQueue = new ConcurrentLinkedQueue();
   private PulseData pulseData = null;
   private volatile boolean isEnabled = false;
   private final AtomicLong pulseCounter = new AtomicLong();
   private final AtomicLong startMax = new AtomicLong();
   private final AtomicLong startSum = new AtomicLong();
   private final AtomicLong startAv = new AtomicLong();
   private final AtomicLong endMax = new AtomicLong();
   private final AtomicLong endSum = new AtomicLong();
   private final AtomicLong endAv = new AtomicLong();
   private final AtomicLong animationDurationMax = new AtomicLong();
   private final AtomicLong animationDurationSum = new AtomicLong();
   private final AtomicLong animationDurationAv = new AtomicLong();
   private final AtomicLong paintingDurationMax = new AtomicLong();
   private final AtomicLong paintingDurationSum = new AtomicLong();
   private final AtomicLong paintingDurationAv = new AtomicLong();
   private final AtomicLong pulseDurationMax = new AtomicLong();
   private final AtomicLong pulseDurationSum = new AtomicLong();
   private final AtomicLong pulseDurationAv = new AtomicLong();
   private final AtomicLong[] maxAndAv;
   private final PulseData.Accessor[] maxAndAvAccessors;
   private final AtomicLong skippedPulses;
   private int skipPulses;

   public AnimationPulse() {
      this.maxAndAv = new AtomicLong[]{this.startMax, this.startSum, this.startAv, this.endMax, this.endSum, this.endAv, this.animationDurationMax, this.animationDurationSum, this.animationDurationAv, this.paintingDurationMax, this.paintingDurationSum, this.paintingDurationAv, this.pulseDurationMax, this.pulseDurationSum, this.pulseDurationAv};
      this.maxAndAvAccessors = new PulseData.Accessor[]{AnimationPulse.PulseData.PulseStartAccessor, AnimationPulse.PulseData.PulseEndAccessor, AnimationPulse.PulseData.AnimationDurationAccessor, AnimationPulse.PulseData.PaintingDurationAccessor, AnimationPulse.PulseData.PulseDurationAccessor};
      this.skippedPulses = new AtomicLong();
      this.skipPulses = 100;
   }

   public static AnimationPulse getDefaultBean() {
      return AnimationPulse.AnimationPulseHolder.holder;
   }

   public boolean getEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(boolean var1) {
      if (var1 != this.isEnabled) {
         this.isEnabled = var1;
      }
   }

   public long getPULSE_DURATION() {
      return (long)Toolkit.getToolkit().getPrimaryTimer().getPulseDuration(1000);
   }

   public long getSkippedPulses() {
      return this.skippedPulses.get();
   }

   public long getSkippedPulsesIn1Sec() {
      long var1 = 0L;
      Iterator var3 = this.pulseDataQueue.iterator();

      while(var3.hasNext()) {
         PulseData var4 = (PulseData)var3.next();
         if (var4.getPulseStartFromNow(TimeUnit.SECONDS) == 0L) {
            var1 += var4.getSkippedPulses();
         }
      }

      return var1;
   }

   public void recordStart(long var1) {
      if (this.getEnabled()) {
         this.pulseData = new PulseData(TimeUnit.MILLISECONDS.toNanos(var1));
      }
   }

   private void purgeOldPulseData() {
      Iterator var1 = this.pulseDataQueue.iterator();

      while(var1.hasNext() && ((PulseData)var1.next()).getPulseStartFromNow(TimeUnit.SECONDS) > 1L) {
         var1.remove();
      }

   }

   private void updateMaxAndAv() {
      long var1 = this.pulseCounter.incrementAndGet();

      for(int var3 = 0; var3 < this.maxAndAvAccessors.length; ++var3) {
         int var4 = var3 * 3;
         long var5 = this.maxAndAvAccessors[var3].get(this.pulseData, TimeUnit.MILLISECONDS);
         this.maxAndAv[var4].set(Math.max(this.maxAndAv[var4].get(), var5));
         this.maxAndAv[var4 + 1].addAndGet(var5);
         this.maxAndAv[var4 + 2].set(this.maxAndAv[var4 + 1].get() / var1);
      }

   }

   public void recordEnd() {
      if (this.getEnabled()) {
         if (this.skipPulses > 0) {
            --this.skipPulses;
            this.pulseData = null;
         } else {
            this.pulseData.recordEnd();
            this.purgeOldPulseData();
            this.updateMaxAndAv();
            this.skippedPulses.addAndGet(this.pulseData.getSkippedPulses());
            this.pulseDataQueue.add(this.pulseData);
            this.pulseData = null;
         }
      }
   }

   private long getAv(PulseData.Accessor var1, long var2, TimeUnit var4) {
      if (!this.getEnabled()) {
         return 0L;
      } else {
         long var5 = 0L;
         long var7 = 0L;
         Iterator var9 = this.pulseDataQueue.iterator();

         while(var9.hasNext()) {
            PulseData var10 = (PulseData)var9.next();
            if (var10.getPulseStartFromNow(var4) <= var2) {
               var5 += var1.get(var10, var4);
               ++var7;
            }
         }

         return var7 == 0L ? 0L : var5 / var7;
      }
   }

   private long getMax(PulseData.Accessor var1, long var2, TimeUnit var4) {
      if (!this.getEnabled()) {
         return 0L;
      } else {
         long var5 = 0L;
         Iterator var7 = this.pulseDataQueue.iterator();

         while(var7.hasNext()) {
            PulseData var8 = (PulseData)var7.next();
            if (var8.getPulseStartFromNow(var4) <= var2) {
               var5 = Math.max(var1.get(var8, var4), var5);
            }
         }

         return var5;
      }
   }

   public long getStartMax() {
      return this.startMax.get();
   }

   public long getStartAv() {
      return this.startAv.get();
   }

   public long getStartMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PulseStartAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getStartAvIn100Millis() {
      return this.getAv(AnimationPulse.PulseData.PulseStartAccessor, 100L, TimeUnit.MILLISECONDS);
   }

   public long getEndMax() {
      return this.endMax.get();
   }

   public long getEndMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PulseEndAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getEndAv() {
      return this.endAv.get();
   }

   public long getEndAvIn100Millis() {
      return this.getAv(AnimationPulse.PulseData.PulseEndAccessor, 100L, TimeUnit.MILLISECONDS);
   }

   public void recordAnimationEnd() {
      if (this.getEnabled() && this.pulseData != null) {
         this.pulseData.recordAnimationEnd();
      }

   }

   public long getAnimationDurationMax() {
      return this.animationDurationMax.get();
   }

   public long getAnimationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.AnimationDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getAnimationDurationAv() {
      return this.animationDurationAv.get();
   }

   public long getAnimationDurationAvIn100Millis() {
      return this.getAv(AnimationPulse.PulseData.AnimationDurationAccessor, 100L, TimeUnit.MILLISECONDS);
   }

   public long getPaintingDurationMax() {
      return this.paintingDurationMax.get();
   }

   public long getPaintingDurationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getPaintingDurationAv() {
      return this.paintingDurationAv.get();
   }

   public long getPaintingDurationAvIn100Millis() {
      return this.getAv(AnimationPulse.PulseData.PaintingDurationAccessor, 100L, TimeUnit.MILLISECONDS);
   }

   public long getScenePaintingDurationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.ScenePaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getPulseDurationMax() {
      return this.pulseDurationMax.get();
   }

   public long getPulseDurationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PulseDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getPulseDurationAv() {
      return this.pulseDurationAv.get();
   }

   public long getPulseDurationAvIn100Millis() {
      return this.getAv(AnimationPulse.PulseData.PulseDurationAccessor, 100L, TimeUnit.MILLISECONDS);
   }

   public long getPaintingPreparationDurationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PaintingPreparationDuration, 1000L, TimeUnit.MILLISECONDS);
   }

   public long getPaintingFinalizationDurationMaxIn1Sec() {
      return this.getMax(AnimationPulse.PulseData.PaintingFinalizationDuration, 1000L, TimeUnit.MILLISECONDS);
   }

   private static class PulseData {
      private final long startNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
      private final long scheduledNanos;
      private long animationEndNanos = Long.MIN_VALUE;
      private long paintingStartNanos = Long.MIN_VALUE;
      private long paintingEndNanos = Long.MIN_VALUE;
      private long scenePaintingStartNanos = Long.MIN_VALUE;
      private long scenePaintingEndNanos = Long.MIN_VALUE;
      private long endNanos = Long.MIN_VALUE;
      static final Accessor PulseStartAccessor = (var0, var1) -> {
         return var0.getPulseStart(var1);
      };
      static final Accessor AnimationDurationAccessor = (var0, var1) -> {
         return var0.getAnimationDuration(var1);
      };
      static final Accessor PaintingDurationAccessor = (var0, var1) -> {
         return var0.getPaintingDuration(var1);
      };
      static final Accessor ScenePaintingDurationAccessor = (var0, var1) -> {
         return var0.getScenePaintingDuration(var1);
      };
      static final Accessor PulseDurationAccessor = (var0, var1) -> {
         return var0.getPulseDuration(var1);
      };
      static final Accessor PulseEndAccessor = (var0, var1) -> {
         return var0.getPulseEnd(var1);
      };
      static final Accessor PaintingPreparationDuration = (var0, var1) -> {
         return var0.getPaintingDuration(var1);
      };
      static final Accessor PaintingFinalizationDuration = (var0, var1) -> {
         return var0.getPaintingFinalizationDuration(var1);
      };

      PulseData(long var1) {
         this.scheduledNanos = this.startNanos + var1;
      }

      long getPulseStart(TimeUnit var1) {
         return var1.convert(this.startNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
      }

      void recordAnimationEnd() {
         this.animationEndNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
      }

      long getAnimationDuration(TimeUnit var1) {
         return this.animationEndNanos > Long.MIN_VALUE ? var1.convert(this.animationEndNanos - this.startNanos, TimeUnit.NANOSECONDS) : 0L;
      }

      long getPaintingDuration(TimeUnit var1) {
         return this.paintingEndNanos > Long.MIN_VALUE && this.paintingStartNanos > Long.MIN_VALUE ? var1.convert(this.paintingEndNanos - this.paintingStartNanos, TimeUnit.NANOSECONDS) : 0L;
      }

      long getScenePaintingDuration(TimeUnit var1) {
         return this.scenePaintingEndNanos > Long.MIN_VALUE && this.scenePaintingStartNanos > Long.MIN_VALUE ? var1.convert(this.scenePaintingEndNanos - this.scenePaintingStartNanos, TimeUnit.NANOSECONDS) : 0L;
      }

      long getPaintingFinalizationDuration(TimeUnit var1) {
         return this.scenePaintingEndNanos > Long.MIN_VALUE && this.paintingEndNanos > Long.MIN_VALUE ? var1.convert(this.paintingEndNanos - this.scenePaintingEndNanos, TimeUnit.NANOSECONDS) : 0L;
      }

      void recordEnd() {
         this.endNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
      }

      long getPulseDuration(TimeUnit var1) {
         return var1.convert(this.endNanos - this.startNanos, TimeUnit.NANOSECONDS);
      }

      long getPulseEnd(TimeUnit var1) {
         return var1.convert(this.endNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
      }

      long getPulseStartFromNow(TimeUnit var1) {
         return var1.convert(Toolkit.getToolkit().getPrimaryTimer().nanos() - this.startNanos, TimeUnit.NANOSECONDS);
      }

      long getSkippedPulses() {
         return this.getPulseEnd(TimeUnit.MILLISECONDS) / AnimationPulse.getDefaultBean().getPULSE_DURATION();
      }

      interface Accessor {
         long get(PulseData var1, TimeUnit var2);
      }
   }

   private static class AnimationPulseHolder {
      private static final AnimationPulse holder = new AnimationPulse();
   }
}
