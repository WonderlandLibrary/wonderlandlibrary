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
 *  javafx.scene.input.RotateEvent
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
import javafx.scene.input.RotateEvent;
import javafx.util.Duration;

class RotateGestureRecognizer
implements GestureRecognizer {
    private ViewScene scene;
    private static double ROTATATION_THRESHOLD = 5.0;
    private static boolean ROTATION_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 500.0;
    private static double ROTATION_INERTIA_MILLIS = 1500.0;
    private RotateRecognitionState state = RotateRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaRotationVelocity = new SimpleDoubleProperty();
    private double initialInertiaRotationVelocity = 0.0;
    private double rotationStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    int modifiers;
    boolean direct;
    private int currentTouchCount = 0;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    int touchPointsInEvent;
    long touchPointID1 = -1L;
    long touchPointID2 = -1L;
    double centerX;
    double centerY;
    double centerAbsX;
    double centerAbsY;
    double currentRotation;
    double angleReference;
    double totalRotation = 0.0;
    double inertiaLastTime = 0.0;

    RotateGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaRotationVelocity.addListener(observable -> {
            double d = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d2 = d - this.inertiaLastTime;
            this.inertiaLastTime = d;
            this.currentRotation = d2 * this.inertiaRotationVelocity.get();
            this.totalRotation += this.currentRotation;
            this.sendRotateEvent(true);
        });
    }

    @Override
    public void notifyBeginTouchEvent(long l, int n, boolean bl, int n2) {
        this.params(n, bl);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
        this.touchPointsInEvent = 0;
    }

    @Override
    public void notifyNextTouchEvent(long l, int n, long l2, int n2, int n3, int n4, int n5) {
        ++this.touchPointsInEvent;
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
                throw new RuntimeException("Error in Rotate gesture recognition: unknown touch state: " + this.state);
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is zero!");
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

    private double getAngle(TouchPointTracker touchPointTracker, TouchPointTracker touchPointTracker2) {
        double d = touchPointTracker2.getAbsX() - touchPointTracker.getAbsX();
        double d2 = -(touchPointTracker2.getAbsY() - touchPointTracker.getAbsY());
        double d3 = Math.toDegrees(Math.atan2(d2, d));
        return d3;
    }

    private double getNormalizedDelta(double d, double d2) {
        double d3 = -(d2 - d);
        if (d3 > 180.0) {
            d3 -= 360.0;
        } else if (d3 < -180.0) {
            d3 += 360.0;
        }
        return d3;
    }

    private void assignActiveTouchpoints() {
        boolean bl = false;
        if (!this.trackers.containsKey(this.touchPointID1)) {
            this.touchPointID1 = -1L;
            bl = true;
        }
        if (!this.trackers.containsKey(this.touchPointID2)) {
            this.touchPointID2 = -1L;
            bl = true;
        }
        if (bl) {
            for (Long l : this.trackers.keySet()) {
                if (l == this.touchPointID1 || l == this.touchPointID2) continue;
                if (this.touchPointID1 == -1L) {
                    this.touchPointID1 = l;
                    continue;
                }
                if (this.touchPointID2 != -1L) break;
                this.touchPointID2 = l;
            }
        }
    }

    @Override
    public void notifyEndTouchEvent(long l) {
        this.lastTouchEventTime = l;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == RotateRecognitionState.ACTIVE) {
                this.sendRotateFinishedEvent();
            }
            if (ROTATION_INERTIA_ENABLED && (this.state == RotateRecognitionState.PRE_INERTIA || this.state == RotateRecognitionState.ACTIVE)) {
                double d = ((double)l - this.rotationStartTime) / 1000000.0;
                if (d < 300.0) {
                    this.state = RotateRecognitionState.INERTIA;
                    this.inertiaLastTime = 0.0;
                    if (this.initialInertiaRotationVelocity > MAX_INITIAL_VELOCITY) {
                        this.initialInertiaRotationVelocity = MAX_INITIAL_VELOCITY;
                    } else if (this.initialInertiaRotationVelocity < -MAX_INITIAL_VELOCITY) {
                        this.initialInertiaRotationVelocity = -MAX_INITIAL_VELOCITY;
                    }
                    this.inertiaTimeline.getKeyFrames().setAll((Object[])new KeyFrame[]{new KeyFrame(Duration.millis((double)0.0), new KeyValue[]{new KeyValue((WritableValue)this.inertiaRotationVelocity, (Object)this.initialInertiaRotationVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.millis((double)(ROTATION_INERTIA_MILLIS * Math.abs(this.initialInertiaRotationVelocity) / MAX_INITIAL_VELOCITY)), actionEvent -> this.reset(), new KeyValue[]{new KeyValue((WritableValue)this.inertiaRotationVelocity, (Object)0, Interpolator.LINEAR)})});
                    this.inertiaTimeline.playFromStart();
                } else {
                    this.reset();
                }
            }
        } else {
            if (this.touchPointsPressed && this.state == RotateRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.currentTouchCount == 1) {
                if (this.state == RotateRecognitionState.ACTIVE) {
                    this.sendRotateFinishedEvent();
                    if (ROTATION_INERTIA_ENABLED) {
                        this.state = RotateRecognitionState.PRE_INERTIA;
                    } else {
                        this.reset();
                    }
                }
            } else {
                if (this.state == RotateRecognitionState.IDLE) {
                    this.state = RotateRecognitionState.TRACKING;
                    this.assignActiveTouchpoints();
                }
                this.calculateCenter();
                if (this.touchPointsSetChanged) {
                    this.assignActiveTouchpoints();
                }
                TouchPointTracker touchPointTracker = this.trackers.get(this.touchPointID1);
                TouchPointTracker touchPointTracker2 = this.trackers.get(this.touchPointID2);
                double d = this.getAngle(touchPointTracker, touchPointTracker2);
                if (this.touchPointsSetChanged) {
                    this.angleReference = d;
                } else {
                    this.currentRotation = this.getNormalizedDelta(this.angleReference, d);
                    if (this.state == RotateRecognitionState.TRACKING && Math.abs(this.currentRotation) > ROTATATION_THRESHOLD) {
                        this.state = RotateRecognitionState.ACTIVE;
                        this.sendRotateStartedEvent();
                    }
                    if (this.state == RotateRecognitionState.ACTIVE) {
                        this.totalRotation += this.currentRotation;
                        this.sendRotateEvent(false);
                        this.angleReference = d;
                        double d2 = ((double)l - this.rotationStartTime) / 1.0E9;
                        if (d2 > 1.0E-4) {
                            this.initialInertiaRotationVelocity = this.currentRotation / d2;
                            this.rotationStartTime = l;
                        }
                    }
                }
            }
        }
    }

    private void sendRotateStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent((EventType<RotateEvent>)RotateEvent.ROTATION_STARTED, 0.0, 0.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateEvent(boolean bl) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent((EventType<RotateEvent>)RotateEvent.ROTATE, this.currentRotation, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent((EventType<RotateEvent>)RotateEvent.ROTATION_FINISHED, 0.0, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
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
        if (this.state != RotateRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l);
            if (touchPointTracker == null) {
                this.state = RotateRecognitionState.FAILURE;
                throw new RuntimeException("Error in Rotate gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l, long l2, int n, int n2, int n3, int n4) {
        if (this.state == RotateRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l);
        if (touchPointTracker == null) {
            this.state = RotateRecognitionState.FAILURE;
            throw new RuntimeException("Error in rotate gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l2, n, n2, n3, n4);
    }

    void reset() {
        this.state = RotateRecognitionState.IDLE;
        this.touchPointID1 = -1L;
        this.touchPointID2 = -1L;
        this.currentRotation = 0.0;
        this.totalRotation = 0.0;
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.rotate.threshold");
            if (string != null) {
                ROTATATION_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.rotate.inertia")) != null) {
                ROTATION_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static final class RotateRecognitionState
    extends Enum<RotateRecognitionState> {
        public static final /* enum */ RotateRecognitionState IDLE = new RotateRecognitionState();
        public static final /* enum */ RotateRecognitionState TRACKING = new RotateRecognitionState();
        public static final /* enum */ RotateRecognitionState ACTIVE = new RotateRecognitionState();
        public static final /* enum */ RotateRecognitionState PRE_INERTIA = new RotateRecognitionState();
        public static final /* enum */ RotateRecognitionState INERTIA = new RotateRecognitionState();
        public static final /* enum */ RotateRecognitionState FAILURE = new RotateRecognitionState();
        private static final /* synthetic */ RotateRecognitionState[] $VALUES;

        public static RotateRecognitionState[] values() {
            return (RotateRecognitionState[])$VALUES.clone();
        }

        public static RotateRecognitionState valueOf(String string) {
            return Enum.valueOf(RotateRecognitionState.class, string);
        }

        private static /* synthetic */ RotateRecognitionState[] $values() {
            return new RotateRecognitionState[]{IDLE, TRACKING, ACTIVE, PRE_INERTIA, INERTIA, FAILURE};
        }

        static {
            $VALUES = RotateRecognitionState.$values();
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

