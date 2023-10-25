/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.Animation$Status
 *  javafx.animation.KeyFrame
 *  javafx.animation.Timeline
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.util.Duration
 */
package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipInterpolator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class TimelineClipCore {
    private static final int UNDEFINED_KEYFRAME = -1;
    private static final Comparator<KeyFrame> KEY_FRAME_COMPARATOR = (keyFrame, keyFrame2) -> keyFrame.getTime().compareTo(keyFrame2.getTime());
    Timeline timeline;
    private KeyFrame[] keyFrames = new KeyFrame[0];
    private long[] keyFrameTicks = new long[0];
    private boolean canSkipFrames = true;
    private ClipInterpolator clipInterpolator;
    private boolean aborted = false;
    private int lastKF = -1;
    private long curTicks = 0L;

    public TimelineClipCore(Timeline timeline) {
        this.timeline = timeline;
        this.clipInterpolator = ClipInterpolator.create(this.keyFrames, this.keyFrameTicks);
    }

    public Duration setKeyFrames(Collection<KeyFrame> collection) {
        int n = collection.size();
        KeyFrame[] arrkeyFrame = new KeyFrame[n];
        collection.toArray((T[])arrkeyFrame);
        Arrays.sort(arrkeyFrame, KEY_FRAME_COMPARATOR);
        this.canSkipFrames = true;
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = new long[n];
        for (int i = 0; i < n; ++i) {
            this.keyFrameTicks[i] = TickCalculation.fromDuration(this.keyFrames[i].getTime());
            if (!this.canSkipFrames || this.keyFrames[i].getOnFinished() == null) continue;
            this.canSkipFrames = false;
        }
        this.clipInterpolator = this.clipInterpolator.setKeyFrames(arrkeyFrame, this.keyFrameTicks);
        return n == 0 ? Duration.ZERO : arrkeyFrame[n - 1].getTime();
    }

    public void notifyCurrentRateChanged() {
        if (this.timeline.getStatus() != Animation.Status.RUNNING) {
            this.clearLastKeyFrame();
        }
    }

    public void abort() {
        this.aborted = true;
    }

    private void clearLastKeyFrame() {
        this.lastKF = -1;
    }

    public void jumpTo(long l, boolean bl) {
        this.lastKF = -1;
        this.curTicks = l;
        if (this.timeline.getStatus() != Animation.Status.STOPPED || bl) {
            if (bl) {
                this.clipInterpolator.validate(false);
            }
            this.clipInterpolator.interpolate(l);
        }
    }

    public void start(boolean bl) {
        this.clearLastKeyFrame();
        this.clipInterpolator.validate(bl);
        if (this.curTicks > 0L) {
            this.clipInterpolator.interpolate(this.curTicks);
        }
    }

    public void playTo(long l) {
        boolean bl;
        if (this.canSkipFrames) {
            this.clearLastKeyFrame();
            this.setTime(l);
            this.clipInterpolator.interpolate(l);
            return;
        }
        this.aborted = false;
        boolean bl2 = bl = this.curTicks <= l;
        if (bl) {
            int n = this.lastKF == -1 ? 0 : (this.keyFrameTicks[this.lastKF] <= this.curTicks ? this.lastKF + 1 : this.lastKF);
            int n2 = this.keyFrames.length;
            for (int i = n; i < n2; ++i) {
                long l2 = this.keyFrameTicks[i];
                if (l2 > l) {
                    this.lastKF = i - 1;
                } else {
                    if (l2 < this.curTicks) continue;
                    this.visitKeyFrame(i, l2);
                    if (!this.aborted) {
                        continue;
                    }
                }
                break;
            }
        } else {
            int n;
            for (int i = n = this.lastKF == -1 ? this.keyFrames.length - 1 : (this.keyFrameTicks[this.lastKF] >= this.curTicks ? this.lastKF - 1 : this.lastKF); i >= 0; --i) {
                long l3 = this.keyFrameTicks[i];
                if (l3 < l) {
                    this.lastKF = i + 1;
                } else {
                    if (l3 > this.curTicks) continue;
                    this.visitKeyFrame(i, l3);
                    if (!this.aborted) {
                        continue;
                    }
                }
                break;
            }
        }
        if (!(this.aborted || this.lastKF != -1 && this.keyFrameTicks[this.lastKF] == l && this.keyFrames[this.lastKF].getOnFinished() != null)) {
            this.setTime(l);
            this.clipInterpolator.interpolate(l);
        }
    }

    private void setTime(long l) {
        this.curTicks = l;
        AnimationAccessor.getDefault().setCurrentTicks((Animation)this.timeline, l);
    }

    private void visitKeyFrame(int n, long l) {
        if (n != this.lastKF) {
            this.lastKF = n;
            KeyFrame keyFrame = this.keyFrames[n];
            EventHandler eventHandler = keyFrame.getOnFinished();
            if (eventHandler != null) {
                this.setTime(l);
                this.clipInterpolator.interpolate(l);
                try {
                    eventHandler.handle((Event)new ActionEvent((Object)keyFrame, null));
                }
                catch (Throwable throwable) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), throwable);
                }
            }
        }
    }
}

