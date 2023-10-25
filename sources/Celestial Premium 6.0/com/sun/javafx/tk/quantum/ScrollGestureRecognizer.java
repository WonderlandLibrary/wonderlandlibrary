/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.value.WritableValue
 *  javafx.event.EventType
 *  javafx.scene.input.ScrollEvent
 *  javafx.util.Duration
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.WritableValue;
import javafx.event.EventType;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

class ScrollGestureRecognizer
implements GestureRecognizer {
    private static double SCROLL_THRESHOLD = 10.0;
    private static boolean SCROLL_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 1000.0;
    private static double SCROLL_INERTIA_MILLIS = 1500.0;
    private ViewScene scene;
    private ScrollRecognitionState state = ScrollRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaScrollVelocity = new SimpleDoubleProperty();
    private double initialInertiaScrollVelocity = 0.0;
    private double scrollStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    private Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    private int modifiers;
    private boolean direct;
    private int currentTouchCount = 0;
    private int lastTouchCount;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double lastCenterAbsX;
    private double lastCenterAbsY;
    private double deltaX;
    private double deltaY;
    private double totalDeltaX;
    private double totalDeltaY;
    private double factorX;
    private double factorY;
    double inertiaLastTime = 0.0;

    ScrollGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaScrollVelocity.addListener(observable -> {
            double d = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d2 = d - this.inertiaLastTime;
            this.inertiaLastTime = d;
            double d3 = d2 * this.inertiaScrollVelocity.get();
            this.deltaX = d3 * this.factorX;
            this.totalDeltaX += this.deltaX;
            this.deltaY = d3 * this.factorY;
            this.totalDeltaY += this.deltaY;
            this.sendScrollEvent(true, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
        });
    }

    @Override
    public void notifyBeginTouchEvent(long l, int n, boolean bl, int n2) {
        this.params(n, bl);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
    }

    @Override
    public void notifyNextTouchEvent(long l, int n, long l2, int n2, int n3, int n4, int n5) {
        switch (n) {
            case 811: {
                this.touchPointsSetChanged = true;
                this.touchPointsPressed = true;
                this.touchPressed(l2, l, n2, n3, n4, n5);
                break;
            }
            case 814: {
                break;
            }
            case 812: {
                this.touchMoved(l2, l, n2, n3, n4, n5);
                break;
            }
            case 813: {
                this.touchPointsSetChanged = true;
                this.touchReleased(l2, l, n2, n3, n4, n5);
                break;
            }
            default: {
                throw new RuntimeException("Error in Scroll gesture recognition: unknown touch state: " + this.state);
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is zero!");
        }
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        for (TouchPointTracker touchPointTracker : this.trackers.values()) {
            d += touchPointTracker.getX();
            d2 += touchPointTracker.getY();
            d3 += touchPointTracker.getAbsX();
            d4 += touchPointTracker.getAbsY();
        }
        this.centerX = d / (double)this.currentTouchCount;
        this.centerY = d2 / (double)this.currentTouchCount;
        this.centerAbsX = d3 / (double)this.currentTouchCount;
        this.centerAbsY = d4 / (double)this.currentTouchCount;
    }

    @Override
    public void notifyEndTouchEvent(long l) {
        this.lastTouchEventTime = l;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount < 1) {
            if (this.state == ScrollRecognitionState.ACTIVE) {
                this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                if (SCROLL_INERTIA_ENABLED) {
                    double d = ((double)l - this.scrollStartTime) / 1000000.0;
                    if (d < 300.0) {
                        this.state = ScrollRecognitionState.INERTIA;
                        this.inertiaLastTime = 0.0;
                        if (this.initialInertiaScrollVelocity > MAX_INITIAL_VELOCITY) {
                            this.initialInertiaScrollVelocity = MAX_INITIAL_VELOCITY;
                        }
                        this.inertiaTimeline.getKeyFrames().setAll((Object[])new KeyFrame[]{new KeyFrame(Duration.millis((double)0.0), new KeyValue[]{new KeyValue((WritableValue)this.inertiaScrollVelocity, (Object)this.initialInertiaScrollVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.millis((double)(SCROLL_INERTIA_MILLIS * Math.abs(this.initialInertiaScrollVelocity) / MAX_INITIAL_VELOCITY)), actionEvent -> this.reset(), new KeyValue[]{new KeyValue((WritableValue)this.inertiaScrollVelocity, (Object)0, Interpolator.LINEAR)})});
                        this.inertiaTimeline.playFromStart();
                    } else {
                        this.reset();
                    }
                } else {
                    this.reset();
                }
            }
        } else {
            this.calculateCenter();
            if (this.touchPointsPressed && this.state == ScrollRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.touchPointsSetChanged) {
                if (this.state == ScrollRecognitionState.IDLE) {
                    this.state = ScrollRecognitionState.TRACKING;
                }
                if (this.state == ScrollRecognitionState.ACTIVE) {
                    this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                    this.totalDeltaX = 0.0;
                    this.totalDeltaY = 0.0;
                    this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                }
                this.lastTouchCount = this.currentTouchCount;
                this.lastCenterAbsX = this.centerAbsX;
                this.lastCenterAbsY = this.centerAbsY;
            } else {
                this.deltaX = this.centerAbsX - this.lastCenterAbsX;
                this.deltaY = this.centerAbsY - this.lastCenterAbsY;
                if (this.state == ScrollRecognitionState.TRACKING && (Math.abs(this.deltaX) > SCROLL_THRESHOLD || Math.abs(this.deltaY) > SCROLL_THRESHOLD)) {
                    this.state = ScrollRecognitionState.ACTIVE;
                    this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                }
                if (this.state == ScrollRecognitionState.ACTIVE) {
                    this.totalDeltaX += this.deltaX;
                    this.totalDeltaY += this.deltaY;
                    this.sendScrollEvent(false, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                    double d = ((double)l - this.scrollStartTime) / 1.0E9;
                    if (d > 1.0E-4) {
                        double d2 = Math.sqrt(this.deltaX * this.deltaX + this.deltaY * this.deltaY);
                        this.factorX = this.deltaX / d2;
                        this.factorY = this.deltaY / d2;
                        this.initialInertiaScrollVelocity = d2 / d;
                        this.scrollStartTime = l;
                    }
                    this.lastCenterAbsX = this.centerAbsX;
                    this.lastCenterAbsY = this.centerAbsY;
                }
            }
        }
    }

    private void sendScrollStartedEvent(double d, double d2, int n) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent((EventType<ScrollEvent>)ScrollEvent.SCROLL_STARTED, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, n, 0, 0, 0, 0, this.centerX, this.centerY, d, d2, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollEvent(boolean bl, double d, double d2, int n) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent((EventType<ScrollEvent>)ScrollEvent.SCROLL, this.deltaX, this.deltaY, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, n, 0, 0, 0, 0, this.centerX, this.centerY, d, d2, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollFinishedEvent(double d, double d2, int n) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent((EventType<ScrollEvent>)ScrollEvent.SCROLL_FINISHED, 0.0, 0.0, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, n, 0, 0, 0, 0, this.centerX, this.centerY, d, d2, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void params(int n, boolean bl) {
        this.modifiers = n;
        this.direct = bl;
    }

    public void touchPressed(long l, long l2, int n, int n2, int n3, int n4) {
        ++this.currentTouchCount;
        TouchPointTracker touchPointTracker = new TouchPointTracker();
        touchPointTracker.update(l2, n, n2, n3, n4);
        this.trackers.put(l, touchPointTracker);
    }

    public void touchReleased(long l, long l2, int n, int n2, int n3, int n4) {
        if (this.state != ScrollRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l);
            if (touchPointTracker == null) {
                this.state = ScrollRecognitionState.FAILURE;
                throw new RuntimeException("Error in Scroll gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l, long l2, int n, int n2, int n3, int n4) {
        if (this.state == ScrollRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l);
        if (touchPointTracker == null) {
            this.state = ScrollRecognitionState.FAILURE;
            throw new RuntimeException("Error in scroll gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l2, n, n2, n3, n4);
    }

    void reset() {
        this.state = ScrollRecognitionState.IDLE;
        this.totalDeltaX = 0.0;
        this.totalDeltaY = 0.0;
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.scroll.threshold");
            if (string != null) {
                SCROLL_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.scroll.inertia")) != null) {
                SCROLL_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static final class ScrollRecognitionState
    extends Enum<ScrollRecognitionState> {
        public static final /* enum */ ScrollRecognitionState IDLE = new ScrollRecognitionState();
        public static final /* enum */ ScrollRecognitionState TRACKING = new ScrollRecognitionState();
        public static final /* enum */ ScrollRecognitionState ACTIVE = new ScrollRecognitionState();
        public static final /* enum */ ScrollRecognitionState INERTIA = new ScrollRecognitionState();
        public static final /* enum */ ScrollRecognitionState FAILURE = new ScrollRecognitionState();
        private static final /* synthetic */ ScrollRecognitionState[] $VALUES;

        public static ScrollRecognitionState[] values() {
            return (ScrollRecognitionState[])$VALUES.clone();
        }

        public static ScrollRecognitionState valueOf(String string) {
            return Enum.valueOf(ScrollRecognitionState.class, string);
        }

        private static /* synthetic */ ScrollRecognitionState[] $values() {
            return new ScrollRecognitionState[]{IDLE, TRACKING, ACTIVE, INERTIA, FAILURE};
        }

        static {
            $VALUES = ScrollRecognitionState.$values();
        }
    }

    private static class TouchPointTracker {
        double x;
        double y;
        double absX;
        double absY;

        private TouchPointTracker() {
        }

        public void update(long l, double d, double d2, double d3, double d4) {
            this.x = d;
            this.y = d2;
            this.absX = d3;
            this.absY = d4;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getAbsX() {
            return this.absX;
        }

        public double getAbsY() {
            return this.absY;
        }
    }
}

