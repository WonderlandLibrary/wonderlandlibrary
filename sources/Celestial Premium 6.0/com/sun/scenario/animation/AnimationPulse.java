/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.animation.AnimationPulseMBean;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AnimationPulse
implements AnimationPulseMBean {
    private final Queue<PulseData> pulseDataQueue = new ConcurrentLinkedQueue<PulseData>();
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
    private final AtomicLong[] maxAndAv = new AtomicLong[]{this.startMax, this.startSum, this.startAv, this.endMax, this.endSum, this.endAv, this.animationDurationMax, this.animationDurationSum, this.animationDurationAv, this.paintingDurationMax, this.paintingDurationSum, this.paintingDurationAv, this.pulseDurationMax, this.pulseDurationSum, this.pulseDurationAv};
    private final PulseData.Accessor[] maxAndAvAccessors = new PulseData.Accessor[]{PulseData.PulseStartAccessor, PulseData.PulseEndAccessor, PulseData.AnimationDurationAccessor, PulseData.PaintingDurationAccessor, PulseData.PulseDurationAccessor};
    private final AtomicLong skippedPulses = new AtomicLong();
    private int skipPulses = 100;

    public static AnimationPulse getDefaultBean() {
        return AnimationPulseHolder.holder;
    }

    @Override
    public boolean getEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setEnabled(boolean bl) {
        if (bl == this.isEnabled) {
            return;
        }
        this.isEnabled = bl;
    }

    @Override
    public long getPULSE_DURATION() {
        return Toolkit.getToolkit().getPrimaryTimer().getPulseDuration(1000);
    }

    @Override
    public long getSkippedPulses() {
        return this.skippedPulses.get();
    }

    @Override
    public long getSkippedPulsesIn1Sec() {
        long l = 0L;
        for (PulseData pulseData : this.pulseDataQueue) {
            if (pulseData.getPulseStartFromNow(TimeUnit.SECONDS) != 0L) continue;
            l += pulseData.getSkippedPulses();
        }
        return l;
    }

    public void recordStart(long l) {
        if (!this.getEnabled()) {
            return;
        }
        this.pulseData = new PulseData(TimeUnit.MILLISECONDS.toNanos(l));
    }

    private void purgeOldPulseData() {
        Iterator iterator = this.pulseDataQueue.iterator();
        while (iterator.hasNext() && ((PulseData)iterator.next()).getPulseStartFromNow(TimeUnit.SECONDS) > 1L) {
            iterator.remove();
        }
    }

    private void updateMaxAndAv() {
        long l = this.pulseCounter.incrementAndGet();
        for (int i = 0; i < this.maxAndAvAccessors.length; ++i) {
            int n = i * 3;
            long l2 = this.maxAndAvAccessors[i].get(this.pulseData, TimeUnit.MILLISECONDS);
            this.maxAndAv[n].set(Math.max(this.maxAndAv[n].get(), l2));
            this.maxAndAv[n + 1].addAndGet(l2);
            this.maxAndAv[n + 2].set(this.maxAndAv[n + 1].get() / l);
        }
    }

    public void recordEnd() {
        if (!this.getEnabled()) {
            return;
        }
        if (this.skipPulses > 0) {
            --this.skipPulses;
            this.pulseData = null;
            return;
        }
        this.pulseData.recordEnd();
        this.purgeOldPulseData();
        this.updateMaxAndAv();
        this.skippedPulses.addAndGet(this.pulseData.getSkippedPulses());
        this.pulseDataQueue.add(this.pulseData);
        this.pulseData = null;
    }

    private long getAv(PulseData.Accessor accessor, long l, TimeUnit timeUnit) {
        if (!this.getEnabled()) {
            return 0L;
        }
        long l2 = 0L;
        long l3 = 0L;
        for (PulseData pulseData : this.pulseDataQueue) {
            if (pulseData.getPulseStartFromNow(timeUnit) > l) continue;
            l2 += accessor.get(pulseData, timeUnit);
            ++l3;
        }
        return l3 == 0L ? 0L : l2 / l3;
    }

    private long getMax(PulseData.Accessor accessor, long l, TimeUnit timeUnit) {
        if (!this.getEnabled()) {
            return 0L;
        }
        long l2 = 0L;
        for (PulseData pulseData : this.pulseDataQueue) {
            if (pulseData.getPulseStartFromNow(timeUnit) > l) continue;
            l2 = Math.max(accessor.get(pulseData, timeUnit), l2);
        }
        return l2;
    }

    @Override
    public long getStartMax() {
        return this.startMax.get();
    }

    @Override
    public long getStartAv() {
        return this.startAv.get();
    }

    @Override
    public long getStartMaxIn1Sec() {
        return this.getMax(PulseData.PulseStartAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getStartAvIn100Millis() {
        return this.getAv(PulseData.PulseStartAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getEndMax() {
        return this.endMax.get();
    }

    @Override
    public long getEndMaxIn1Sec() {
        return this.getMax(PulseData.PulseEndAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getEndAv() {
        return this.endAv.get();
    }

    @Override
    public long getEndAvIn100Millis() {
        return this.getAv(PulseData.PulseEndAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    public void recordAnimationEnd() {
        if (this.getEnabled() && this.pulseData != null) {
            this.pulseData.recordAnimationEnd();
        }
    }

    @Override
    public long getAnimationDurationMax() {
        return this.animationDurationMax.get();
    }

    @Override
    public long getAnimationMaxIn1Sec() {
        return this.getMax(PulseData.AnimationDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getAnimationDurationAv() {
        return this.animationDurationAv.get();
    }

    @Override
    public long getAnimationDurationAvIn100Millis() {
        return this.getAv(PulseData.AnimationDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPaintingDurationMax() {
        return this.paintingDurationMax.get();
    }

    @Override
    public long getPaintingDurationMaxIn1Sec() {
        return this.getMax(PulseData.PaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPaintingDurationAv() {
        return this.paintingDurationAv.get();
    }

    @Override
    public long getPaintingDurationAvIn100Millis() {
        return this.getAv(PulseData.PaintingDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getScenePaintingDurationMaxIn1Sec() {
        return this.getMax(PulseData.ScenePaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPulseDurationMax() {
        return this.pulseDurationMax.get();
    }

    @Override
    public long getPulseDurationMaxIn1Sec() {
        return this.getMax(PulseData.PulseDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPulseDurationAv() {
        return this.pulseDurationAv.get();
    }

    @Override
    public long getPulseDurationAvIn100Millis() {
        return this.getAv(PulseData.PulseDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPaintingPreparationDurationMaxIn1Sec() {
        return this.getMax(PulseData.PaintingPreparationDuration, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getPaintingFinalizationDurationMaxIn1Sec() {
        return this.getMax(PulseData.PaintingFinalizationDuration, 1000L, TimeUnit.MILLISECONDS);
    }

    private static class PulseData {
        private final long startNanos;
        private final long scheduledNanos;
        private long animationEndNanos = Long.MIN_VALUE;
        private long paintingStartNanos = Long.MIN_VALUE;
        private long paintingEndNanos = Long.MIN_VALUE;
        private long scenePaintingStartNanos = Long.MIN_VALUE;
        private long scenePaintingEndNanos = Long.MIN_VALUE;
        private long endNanos = Long.MIN_VALUE;
        static final Accessor PulseStartAccessor = (pulseData, timeUnit) -> pulseData.getPulseStart(timeUnit);
        static final Accessor AnimationDurationAccessor = (pulseData, timeUnit) -> pulseData.getAnimationDuration(timeUnit);
        static final Accessor PaintingDurationAccessor = (pulseData, timeUnit) -> pulseData.getPaintingDuration(timeUnit);
        static final Accessor ScenePaintingDurationAccessor = (pulseData, timeUnit) -> pulseData.getScenePaintingDuration(timeUnit);
        static final Accessor PulseDurationAccessor = (pulseData, timeUnit) -> pulseData.getPulseDuration(timeUnit);
        static final Accessor PulseEndAccessor = (pulseData, timeUnit) -> pulseData.getPulseEnd(timeUnit);
        static final Accessor PaintingPreparationDuration = (pulseData, timeUnit) -> pulseData.getPaintingDuration(timeUnit);
        static final Accessor PaintingFinalizationDuration = (pulseData, timeUnit) -> pulseData.getPaintingFinalizationDuration(timeUnit);

        PulseData(long l) {
            this.startNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
            this.scheduledNanos = this.startNanos + l;
        }

        long getPulseStart(TimeUnit timeUnit) {
            return timeUnit.convert(this.startNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
        }

        void recordAnimationEnd() {
            this.animationEndNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
        }

        long getAnimationDuration(TimeUnit timeUnit) {
            return this.animationEndNanos > Long.MIN_VALUE ? timeUnit.convert(this.animationEndNanos - this.startNanos, TimeUnit.NANOSECONDS) : 0L;
        }

        long getPaintingDuration(TimeUnit timeUnit) {
            return this.paintingEndNanos > Long.MIN_VALUE && this.paintingStartNanos > Long.MIN_VALUE ? timeUnit.convert(this.paintingEndNanos - this.paintingStartNanos, TimeUnit.NANOSECONDS) : 0L;
        }

        long getScenePaintingDuration(TimeUnit timeUnit) {
            return this.scenePaintingEndNanos > Long.MIN_VALUE && this.scenePaintingStartNanos > Long.MIN_VALUE ? timeUnit.convert(this.scenePaintingEndNanos - this.scenePaintingStartNanos, TimeUnit.NANOSECONDS) : 0L;
        }

        long getPaintingFinalizationDuration(TimeUnit timeUnit) {
            return this.scenePaintingEndNanos > Long.MIN_VALUE && this.paintingEndNanos > Long.MIN_VALUE ? timeUnit.convert(this.paintingEndNanos - this.scenePaintingEndNanos, TimeUnit.NANOSECONDS) : 0L;
        }

        void recordEnd() {
            this.endNanos = Toolkit.getToolkit().getPrimaryTimer().nanos();
        }

        long getPulseDuration(TimeUnit timeUnit) {
            return timeUnit.convert(this.endNanos - this.startNanos, TimeUnit.NANOSECONDS);
        }

        long getPulseEnd(TimeUnit timeUnit) {
            return timeUnit.convert(this.endNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
        }

        long getPulseStartFromNow(TimeUnit timeUnit) {
            return timeUnit.convert(Toolkit.getToolkit().getPrimaryTimer().nanos() - this.startNanos, TimeUnit.NANOSECONDS);
        }

        long getSkippedPulses() {
            return this.getPulseEnd(TimeUnit.MILLISECONDS) / AnimationPulse.getDefaultBean().getPULSE_DURATION();
        }

        static interface Accessor {
            public long get(PulseData var1, TimeUnit var2);
        }
    }

    private static class AnimationPulseHolder {
        private static final AnimationPulse holder = new AnimationPulse();

        private AnimationPulseHolder() {
        }
    }
}

