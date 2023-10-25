/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.input.SwipeEvent
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventType;
import javafx.scene.input.SwipeEvent;

class SwipeGestureRecognizer
implements GestureRecognizer {
    private static final double TANGENT_30_DEGREES = 0.577;
    private static final double TANGENT_45_DEGREES = 1.0;
    private static final boolean VERBOSE = false;
    private static final double DISTANCE_THRESHOLD = 10.0;
    private static final double BACKWARD_DISTANCE_THRASHOLD = 5.0;
    private SwipeRecognitionState state = SwipeRecognitionState.IDLE;
    MultiTouchTracker tracker = new MultiTouchTracker();
    private ViewScene scene;

    SwipeGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
    }

    @Override
    public void notifyBeginTouchEvent(long l, int n, boolean bl, int n2) {
        this.tracker.params(n, bl);
    }

    @Override
    public void notifyNextTouchEvent(long l, int n, long l2, int n2, int n3, int n4, int n5) {
        switch (n) {
            case 811: {
                this.tracker.pressed(l2, l, n2, n3, n4, n5);
                break;
            }
            case 812: 
            case 814: {
                this.tracker.progress(l2, l, n4, n5);
                break;
            }
            case 813: {
                this.tracker.released(l2, l, n2, n3, n4, n5);
                break;
            }
            default: {
                throw new RuntimeException("Error in swipe gesture recognition: unknown touch state: " + this.state);
            }
        }
    }

    @Override
    public void notifyEndTouchEvent(long l) {
    }

    private EventType<SwipeEvent> calcSwipeType(TouchPointTracker touchPointTracker) {
        double d;
        double d2;
        double d3 = touchPointTracker.getDistanceX();
        double d4 = touchPointTracker.getDistanceY();
        double d5 = Math.abs(d3);
        boolean bl = d5 > (d2 = Math.abs(d4));
        double d6 = bl ? d3 : d4;
        double d7 = bl ? d5 : d2;
        double d8 = bl ? d2 : d5;
        double d9 = bl ? touchPointTracker.lengthX : touchPointTracker.lengthY;
        double d10 = bl ? touchPointTracker.maxDeviationY : touchPointTracker.maxDeviationX;
        double d11 = d = bl ? touchPointTracker.lastXMovement : touchPointTracker.lastYMovement;
        if (d7 <= 10.0) {
            return null;
        }
        if (d8 > d7 * 0.577) {
            return null;
        }
        if (d10 > d7 * 1.0) {
            return null;
        }
        int n = Integer.getInteger("com.sun.javafx.gestures.swipe.maxduration", 300);
        if (touchPointTracker.getDuration() > (long)n) {
            return null;
        }
        if (d9 > d7 * 1.5) {
            return null;
        }
        if (Math.signum(d6) != Math.signum(d) && Math.abs(d) > 5.0) {
            return null;
        }
        if (bl) {
            return touchPointTracker.getDistanceX() < 0.0 ? SwipeEvent.SWIPE_LEFT : SwipeEvent.SWIPE_RIGHT;
        }
        return touchPointTracker.getDistanceY() < 0.0 ? SwipeEvent.SWIPE_UP : SwipeEvent.SWIPE_DOWN;
    }

    private void handleSwipeType(EventType<SwipeEvent> eventType, CenterComputer centerComputer, int n, int n2, boolean bl) {
        if (eventType == null) {
            return;
        }
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.swipeEvent(eventType, n, centerComputer.getX(), centerComputer.getY(), centerComputer.getAbsX(), centerComputer.getAbsY(), (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private static final class SwipeRecognitionState
    extends Enum<SwipeRecognitionState> {
        public static final /* enum */ SwipeRecognitionState IDLE = new SwipeRecognitionState();
        public static final /* enum */ SwipeRecognitionState ADDING = new SwipeRecognitionState();
        public static final /* enum */ SwipeRecognitionState REMOVING = new SwipeRecognitionState();
        public static final /* enum */ SwipeRecognitionState FAILURE = new SwipeRecognitionState();
        private static final /* synthetic */ SwipeRecognitionState[] $VALUES;

        public static SwipeRecognitionState[] values() {
            return (SwipeRecognitionState[])$VALUES.clone();
        }

        public static SwipeRecognitionState valueOf(String string) {
            return Enum.valueOf(SwipeRecognitionState.class, string);
        }

        private static /* synthetic */ SwipeRecognitionState[] $values() {
            return new SwipeRecognitionState[]{IDLE, ADDING, REMOVING, FAILURE};
        }

        static {
            $VALUES = SwipeRecognitionState.$values();
        }
    }

    private class MultiTouchTracker {
        SwipeRecognitionState state = SwipeRecognitionState.IDLE;
        Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
        CenterComputer cc = new CenterComputer();
        int modifiers;
        boolean direct;
        private int touchCount;
        private int currentTouchCount;
        private EventType<SwipeEvent> type;

        private MultiTouchTracker() {
        }

        public void params(int n, boolean bl) {
            this.modifiers = n;
            this.direct = bl;
        }

        public void pressed(long l, long l2, int n, int n2, int n3, int n4) {
            ++this.currentTouchCount;
            switch (this.state) {
                case IDLE: {
                    this.currentTouchCount = 1;
                    this.state = SwipeRecognitionState.ADDING;
                }
                case ADDING: {
                    TouchPointTracker touchPointTracker = new TouchPointTracker();
                    touchPointTracker.start(l2, n, n2, n3, n4);
                    this.trackers.put(l, touchPointTracker);
                    break;
                }
                case REMOVING: {
                    this.state = SwipeRecognitionState.FAILURE;
                    break;
                }
            }
        }

        public void released(long l, long l2, int n, int n2, int n3, int n4) {
            if (this.state != SwipeRecognitionState.FAILURE) {
                TouchPointTracker touchPointTracker = this.trackers.get(l);
                if (touchPointTracker == null) {
                    this.state = SwipeRecognitionState.FAILURE;
                    throw new RuntimeException("Error in swipe gesture recognition: released unknown touch point");
                }
                touchPointTracker.end(l2, n, n2, n3, n4);
                this.cc.add(touchPointTracker.beginX, touchPointTracker.beginY, touchPointTracker.beginAbsX, touchPointTracker.beginAbsY);
                this.cc.add(touchPointTracker.endX, touchPointTracker.endY, touchPointTracker.endAbsX, touchPointTracker.endAbsY);
                EventType<SwipeEvent> eventType = SwipeGestureRecognizer.this.calcSwipeType(touchPointTracker);
                switch (this.state) {
                    case IDLE: {
                        this.reset();
                        throw new RuntimeException("Error in swipe gesture recognition: released touch point outside of gesture");
                    }
                    case ADDING: {
                        this.state = SwipeRecognitionState.REMOVING;
                        this.touchCount = this.currentTouchCount;
                        this.type = eventType;
                        break;
                    }
                    case REMOVING: {
                        if (this.type == eventType) break;
                        this.state = SwipeRecognitionState.FAILURE;
                        break;
                    }
                }
                this.trackers.remove(l);
            }
            --this.currentTouchCount;
            if (this.currentTouchCount == 0) {
                if (this.state == SwipeRecognitionState.REMOVING) {
                    SwipeGestureRecognizer.this.handleSwipeType(this.type, this.cc, this.touchCount, this.modifiers, this.direct);
                }
                this.state = SwipeRecognitionState.IDLE;
                this.reset();
            }
        }

        public void progress(long l, long l2, int n, int n2) {
            if (this.state == SwipeRecognitionState.FAILURE) {
                return;
            }
            TouchPointTracker touchPointTracker = this.trackers.get(l);
            if (touchPointTracker == null) {
                this.state = SwipeRecognitionState.FAILURE;
                throw new RuntimeException("Error in swipe gesture recognition: reported unknown touch point");
            }
            touchPointTracker.progress(l2, n, n2);
        }

        void reset() {
            this.trackers.clear();
            this.cc.reset();
            this.state = SwipeRecognitionState.IDLE;
        }
    }

    private static class TouchPointTracker {
        long beginTime;
        long endTime;
        double beginX;
        double beginY;
        double endX;
        double endY;
        double beginAbsX;
        double beginAbsY;
        double endAbsX;
        double endAbsY;
        double lengthX;
        double lengthY;
        double maxDeviationX;
        double maxDeviationY;
        double lastXMovement;
        double lastYMovement;
        double lastX;
        double lastY;

        private TouchPointTracker() {
        }

        public void start(long l, double d, double d2, double d3, double d4) {
            this.beginX = d;
            this.beginY = d2;
            this.beginAbsX = d3;
            this.beginAbsY = d4;
            this.lastX = d3;
            this.lastY = d4;
            this.beginTime = l / 1000000L;
        }

        public void end(long l, double d, double d2, double d3, double d4) {
            this.progress(l, d3, d4);
            this.endX = d;
            this.endY = d2;
            this.endAbsX = d3;
            this.endAbsY = d4;
            this.endTime = l / 1000000L;
        }

        public void progress(long l, double d, double d2) {
            double d3;
            double d4 = d - this.lastX;
            double d5 = d2 - this.lastY;
            this.lengthX += Math.abs(d4);
            this.lengthY += Math.abs(d5);
            this.lastX = d;
            this.lastY = d2;
            double d6 = Math.abs(d - this.beginAbsX);
            if (d6 > this.maxDeviationX) {
                this.maxDeviationX = d6;
            }
            if ((d3 = Math.abs(d2 - this.beginAbsY)) > this.maxDeviationY) {
                this.maxDeviationY = d3;
            }
            this.lastXMovement = Math.signum(d4) == Math.signum(this.lastXMovement) ? (this.lastXMovement += d4) : d4;
            this.lastYMovement = Math.signum(d5) == Math.signum(this.lastYMovement) ? (this.lastYMovement += d5) : d5;
        }

        public double getDistanceX() {
            return this.endX - this.beginX;
        }

        public double getDistanceY() {
            return this.endY - this.beginY;
        }

        public long getDuration() {
            return this.endTime - this.beginTime;
        }
    }

    private static class CenterComputer {
        double totalAbsX = 0.0;
        double totalAbsY = 0.0;
        double totalX = 0.0;
        double totalY = 0.0;
        int count = 0;

        private CenterComputer() {
        }

        public void add(double d, double d2, double d3, double d4) {
            this.totalAbsX += d3;
            this.totalAbsY += d4;
            this.totalX += d;
            this.totalY += d2;
            ++this.count;
        }

        public double getX() {
            return this.count == 0 ? 0.0 : this.totalX / (double)this.count;
        }

        public double getY() {
            return this.count == 0 ? 0.0 : this.totalY / (double)this.count;
        }

        public double getAbsX() {
            return this.count == 0 ? 0.0 : this.totalAbsX / (double)this.count;
        }

        public double getAbsY() {
            return this.count == 0 ? 0.0 : this.totalAbsY / (double)this.count;
        }

        public void reset() {
            this.totalX = 0.0;
            this.totalY = 0.0;
            this.totalAbsX = 0.0;
            this.totalAbsY = 0.0;
            this.count = 0;
        }
    }
}

