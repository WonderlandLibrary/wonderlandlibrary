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
 *  javafx.scene.input.ZoomEvent
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
import javafx.scene.input.ZoomEvent;
import javafx.util.Duration;

class ZoomGestureRecognizer
implements GestureRecognizer {
    private static double ZOOM_FACTOR_THRESHOLD = 0.1;
    private static boolean ZOOM_INERTIA_ENABLED = true;
    private static double MAX_ZOOMIN_VELOCITY = 3.0;
    private static double MAX_ZOOMOUT_VELOCITY = 0.3333;
    private static double ZOOM_INERTIA_MILLIS = 500.0;
    private static double MAX_ZOOM_IN_FACTOR = 10.0;
    private static double MAX_ZOOM_OUT_FACTOR = 0.1;
    private ViewScene scene;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaZoomVelocity = new SimpleDoubleProperty();
    private double initialInertiaZoomVelocity = 0.0;
    private double zoomStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    private ZoomRecognitionState state = ZoomRecognitionState.IDLE;
    private Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    private int modifiers;
    private boolean direct;
    private int currentTouchCount = 0;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double currentDistance;
    private double distanceReference;
    private double zoomFactor = 1.0;
    private double totalZoomFactor = 1.0;
    double inertiaLastTime = 0.0;

    ZoomGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaZoomVelocity.addListener(observable -> {
            double d = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d2 = d - this.inertiaLastTime;
            this.inertiaLastTime = d;
            double d3 = this.totalZoomFactor;
            this.totalZoomFactor += d2 * this.inertiaZoomVelocity.get();
            this.zoomFactor = this.totalZoomFactor / d3;
            this.sendZoomEvent(true);
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
                throw new RuntimeException("Error in Zoom gesture recognition: unknown touch state: " + this.state);
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is zero!");
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

    private double calculateMaxDistance() {
        double d = 0.0;
        for (TouchPointTracker touchPointTracker : this.trackers.values()) {
            double d2;
            double d3 = touchPointTracker.getAbsX() - this.centerAbsX;
            double d4 = d3 * d3 + (d2 = touchPointTracker.getAbsY() - this.centerAbsY) * d2;
            if (!(d4 > d)) continue;
            d = d4;
        }
        return Math.sqrt(d);
    }

    @Override
    public void notifyEndTouchEvent(long l) {
        this.lastTouchEventTime = l;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == ZoomRecognitionState.ACTIVE) {
                this.sendZoomFinishedEvent();
            }
            if (ZOOM_INERTIA_ENABLED && (this.state == ZoomRecognitionState.PRE_INERTIA || this.state == ZoomRecognitionState.ACTIVE)) {
                double d = ((double)l - this.zoomStartTime) / 1000000.0;
                if (this.initialInertiaZoomVelocity != 0.0 && d < 200.0) {
                    this.state = ZoomRecognitionState.INERTIA;
                    this.inertiaLastTime = 0.0;
                    double d2 = ZOOM_INERTIA_MILLIS / 1000.0;
                    double d3 = this.totalZoomFactor + this.initialInertiaZoomVelocity * d2;
                    if (this.initialInertiaZoomVelocity > 0.0) {
                        if (d3 / this.totalZoomFactor > MAX_ZOOM_IN_FACTOR) {
                            d3 = this.totalZoomFactor * MAX_ZOOM_IN_FACTOR;
                            d2 = (d3 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                        }
                    } else if (d3 / this.totalZoomFactor < MAX_ZOOM_OUT_FACTOR) {
                        d3 = this.totalZoomFactor * MAX_ZOOM_OUT_FACTOR;
                        d2 = (d3 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                    }
                    this.inertiaTimeline.getKeyFrames().setAll((Object[])new KeyFrame[]{new KeyFrame(Duration.millis((double)0.0), new KeyValue[]{new KeyValue((WritableValue)this.inertiaZoomVelocity, (Object)this.initialInertiaZoomVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.seconds((double)d2), actionEvent -> this.reset(), new KeyValue[]{new KeyValue((WritableValue)this.inertiaZoomVelocity, (Object)0, Interpolator.LINEAR)})});
                    this.inertiaTimeline.playFromStart();
                } else {
                    this.reset();
                }
            } else {
                this.reset();
            }
        } else {
            if (this.touchPointsPressed && this.state == ZoomRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.currentTouchCount == 1) {
                if (this.state == ZoomRecognitionState.ACTIVE) {
                    this.sendZoomFinishedEvent();
                    if (ZOOM_INERTIA_ENABLED) {
                        this.state = ZoomRecognitionState.PRE_INERTIA;
                    } else {
                        this.reset();
                    }
                }
            } else {
                if (this.state == ZoomRecognitionState.IDLE) {
                    this.state = ZoomRecognitionState.TRACKING;
                    this.zoomStartTime = l;
                }
                this.calculateCenter();
                double d = this.calculateMaxDistance();
                if (this.touchPointsSetChanged) {
                    this.distanceReference = d;
                } else {
                    this.zoomFactor = d / this.distanceReference;
                    if (this.state == ZoomRecognitionState.TRACKING && Math.abs(this.zoomFactor - 1.0) > ZOOM_FACTOR_THRESHOLD) {
                        this.state = ZoomRecognitionState.ACTIVE;
                        this.sendZoomStartedEvent();
                    }
                    if (this.state == ZoomRecognitionState.ACTIVE) {
                        double d4 = this.totalZoomFactor;
                        this.totalZoomFactor *= this.zoomFactor;
                        this.sendZoomEvent(false);
                        this.distanceReference = d;
                        double d5 = ((double)l - this.zoomStartTime) / 1.0E9;
                        if (d5 > 1.0E-4) {
                            this.initialInertiaZoomVelocity = (this.totalZoomFactor - d4) / d5;
                            this.zoomStartTime = l;
                        } else {
                            this.initialInertiaZoomVelocity = 0.0;
                        }
                    }
                }
            }
        }
    }

    private void sendZoomStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent((EventType<ZoomEvent>)ZoomEvent.ZOOM_STARTED, 1.0, 1.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomEvent(boolean bl) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent((EventType<ZoomEvent>)ZoomEvent.ZOOM, this.zoomFactor, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent((EventType<ZoomEvent>)ZoomEvent.ZOOM_FINISHED, 1.0, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
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
        if (this.state != ZoomRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l);
            if (touchPointTracker == null) {
                this.state = ZoomRecognitionState.FAILURE;
                throw new RuntimeException("Error in Zoom gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l, long l2, int n, int n2, int n3, int n4) {
        if (this.state == ZoomRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l);
        if (touchPointTracker == null) {
            this.state = ZoomRecognitionState.FAILURE;
            throw new RuntimeException("Error in zoom gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l2, n, n2, n3, n4);
    }

    void reset() {
        this.state = ZoomRecognitionState.IDLE;
        this.zoomFactor = 1.0;
        this.totalZoomFactor = 1.0;
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.zoom.threshold");
            if (string != null) {
                ZOOM_FACTOR_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.zoom.inertia")) != null) {
                ZOOM_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static final class ZoomRecognitionState
    extends Enum<ZoomRecognitionState> {
        public static final /* enum */ ZoomRecognitionState IDLE = new ZoomRecognitionState();
        public static final /* enum */ ZoomRecognitionState TRACKING = new ZoomRecognitionState();
        public static final /* enum */ ZoomRecognitionState ACTIVE = new ZoomRecognitionState();
        public static final /* enum */ ZoomRecognitionState PRE_INERTIA = new ZoomRecognitionState();
        public static final /* enum */ ZoomRecognitionState INERTIA = new ZoomRecognitionState();
        public static final /* enum */ ZoomRecognitionState FAILURE = new ZoomRecognitionState();
        private static final /* synthetic */ ZoomRecognitionState[] $VALUES;

        public static ZoomRecognitionState[] values() {
            return (ZoomRecognitionState[])$VALUES.clone();
        }

        public static ZoomRecognitionState valueOf(String string) {
            return Enum.valueOf(ZoomRecognitionState.class, string);
        }

        private static /* synthetic */ ZoomRecognitionState[] $values() {
            return new ZoomRecognitionState[]{IDLE, TRACKING, ACTIVE, PRE_INERTIA, INERTIA, FAILURE};
        }

        static {
            $VALUES = ZoomRecognitionState.$values();
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

