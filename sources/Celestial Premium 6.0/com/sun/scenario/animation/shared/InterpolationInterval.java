/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 *  javafx.animation.KeyValue
 *  javafx.beans.value.WritableBooleanValue
 *  javafx.beans.value.WritableDoubleValue
 *  javafx.beans.value.WritableFloatValue
 *  javafx.beans.value.WritableIntegerValue
 *  javafx.beans.value.WritableLongValue
 *  javafx.beans.value.WritableValue
 */
package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.KeyValueHelper;
import com.sun.scenario.animation.NumberTangentInterpolator;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableValue;

public abstract class InterpolationInterval {
    protected final long ticks;
    protected final Interpolator rightInterpolator;

    protected InterpolationInterval(long l, Interpolator interpolator) {
        this.ticks = l;
        this.rightInterpolator = interpolator;
    }

    public abstract void interpolate(double var1);

    public abstract void recalculateStartValue();

    public static InterpolationInterval create(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
        switch (KeyValueHelper.getType(keyValue)) {
            case BOOLEAN: {
                return new BooleanInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
            case DOUBLE: {
                return keyValue2.getInterpolator() instanceof NumberTangentInterpolator || keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentDoubleInterpolationInterval(keyValue, l, keyValue2, l2) : new DoubleInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
            case FLOAT: {
                return keyValue2.getInterpolator() instanceof NumberTangentInterpolator || keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentFloatInterpolationInterval(keyValue, l, keyValue2, l2) : new FloatInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
            case INTEGER: {
                return keyValue2.getInterpolator() instanceof NumberTangentInterpolator || keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentIntegerInterpolationInterval(keyValue, l, keyValue2, l2) : new IntegerInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
            case LONG: {
                return keyValue2.getInterpolator() instanceof NumberTangentInterpolator || keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentLongInterpolationInterval(keyValue, l, keyValue2, l2) : new LongInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
            case OBJECT: {
                return new ObjectInterpolationInterval(keyValue, l, keyValue2.getEndValue());
            }
        }
        throw new RuntimeException("Should not reach here");
    }

    public static InterpolationInterval create(KeyValue keyValue, long l) {
        switch (KeyValueHelper.getType(keyValue)) {
            case BOOLEAN: {
                return new BooleanInterpolationInterval(keyValue, l);
            }
            case DOUBLE: {
                return keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentDoubleInterpolationInterval(keyValue, l) : new DoubleInterpolationInterval(keyValue, l);
            }
            case FLOAT: {
                return keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentFloatInterpolationInterval(keyValue, l) : new FloatInterpolationInterval(keyValue, l);
            }
            case INTEGER: {
                return keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentIntegerInterpolationInterval(keyValue, l) : new IntegerInterpolationInterval(keyValue, l);
            }
            case LONG: {
                return keyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentLongInterpolationInterval(keyValue, l) : new LongInterpolationInterval(keyValue, l);
            }
            case OBJECT: {
                return new ObjectInterpolationInterval(keyValue, l);
            }
        }
        throw new RuntimeException("Should not reach here");
    }

    private static class BooleanInterpolationInterval
    extends InterpolationInterval {
        private final WritableBooleanValue target;
        private boolean leftValue;
        private final boolean rightValue;

        private BooleanInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableBooleanValue && keyValue.getEndValue() instanceof Boolean && object instanceof Boolean);
            this.target = (WritableBooleanValue)keyValue.getTarget();
            this.rightValue = (Boolean)keyValue.getEndValue();
            this.leftValue = (Boolean)object;
        }

        private BooleanInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableBooleanValue && keyValue.getEndValue() instanceof Boolean);
            this.target = (WritableBooleanValue)keyValue.getTarget();
            this.rightValue = (Boolean)keyValue.getEndValue();
            this.leftValue = this.target.get();
        }

        @Override
        public void interpolate(double d) {
            boolean bl = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, d);
            this.target.set(bl);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    private static class TangentDoubleInterpolationInterval
    extends TangentInterpolationInterval {
        private final WritableDoubleValue target;

        private TangentDoubleInterpolationInterval(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
            super(keyValue, l, keyValue2, l2);
            assert (keyValue.getTarget() instanceof WritableDoubleValue);
            this.target = (WritableDoubleValue)keyValue.getTarget();
        }

        private TangentDoubleInterpolationInterval(KeyValue keyValue, long l) {
            super(keyValue, l);
            assert (keyValue.getTarget() instanceof WritableDoubleValue);
            this.target = (WritableDoubleValue)keyValue.getTarget();
            this.recalculateStartValue(this.target.get());
        }

        @Override
        public void interpolate(double d) {
            this.target.set(this.calculate(d));
        }

        @Override
        public void recalculateStartValue() {
            this.recalculateStartValue(this.target.get());
        }
    }

    private static class DoubleInterpolationInterval
    extends InterpolationInterval {
        private final WritableDoubleValue target;
        private double leftValue;
        private final double rightValue;

        private DoubleInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableDoubleValue && keyValue.getEndValue() instanceof Number && object instanceof Number);
            this.target = (WritableDoubleValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).doubleValue();
            this.leftValue = ((Number)object).doubleValue();
        }

        private DoubleInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableDoubleValue && keyValue.getEndValue() instanceof Number);
            this.target = (WritableDoubleValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).doubleValue();
            this.leftValue = this.target.get();
        }

        @Override
        public void interpolate(double d) {
            double d2 = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, d);
            this.target.set(d2);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    private static class TangentFloatInterpolationInterval
    extends TangentInterpolationInterval {
        private final WritableFloatValue target;

        private TangentFloatInterpolationInterval(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
            super(keyValue, l, keyValue2, l2);
            assert (keyValue.getTarget() instanceof WritableFloatValue);
            this.target = (WritableFloatValue)keyValue.getTarget();
        }

        private TangentFloatInterpolationInterval(KeyValue keyValue, long l) {
            super(keyValue, l);
            assert (keyValue.getTarget() instanceof WritableFloatValue);
            this.target = (WritableFloatValue)keyValue.getTarget();
            this.recalculateStartValue(this.target.get());
        }

        @Override
        public void interpolate(double d) {
            this.target.set((float)this.calculate(d));
        }

        @Override
        public void recalculateStartValue() {
            this.recalculateStartValue(this.target.get());
        }
    }

    private static class FloatInterpolationInterval
    extends InterpolationInterval {
        private final WritableFloatValue target;
        private float leftValue;
        private final float rightValue;

        private FloatInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableFloatValue && keyValue.getEndValue() instanceof Number && object instanceof Number);
            this.target = (WritableFloatValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).floatValue();
            this.leftValue = ((Number)object).floatValue();
        }

        private FloatInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableFloatValue && keyValue.getEndValue() instanceof Number);
            this.target = (WritableFloatValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).floatValue();
            this.leftValue = this.target.get();
        }

        @Override
        public void interpolate(double d) {
            float f = (float)this.rightInterpolator.interpolate((double)this.leftValue, (double)this.rightValue, d);
            this.target.set(f);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    private static class TangentIntegerInterpolationInterval
    extends TangentInterpolationInterval {
        private final WritableIntegerValue target;

        private TangentIntegerInterpolationInterval(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
            super(keyValue, l, keyValue2, l2);
            assert (keyValue.getTarget() instanceof WritableIntegerValue);
            this.target = (WritableIntegerValue)keyValue.getTarget();
        }

        private TangentIntegerInterpolationInterval(KeyValue keyValue, long l) {
            super(keyValue, l);
            assert (keyValue.getTarget() instanceof WritableIntegerValue);
            this.target = (WritableIntegerValue)keyValue.getTarget();
            this.recalculateStartValue(this.target.get());
        }

        @Override
        public void interpolate(double d) {
            this.target.set((int)Math.round(this.calculate(d)));
        }

        @Override
        public void recalculateStartValue() {
            this.recalculateStartValue(this.target.get());
        }
    }

    private static class IntegerInterpolationInterval
    extends InterpolationInterval {
        private final WritableIntegerValue target;
        private int leftValue;
        private final int rightValue;

        private IntegerInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableIntegerValue && keyValue.getEndValue() instanceof Number && object instanceof Number);
            this.target = (WritableIntegerValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).intValue();
            this.leftValue = ((Number)object).intValue();
        }

        private IntegerInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableIntegerValue && keyValue.getEndValue() instanceof Number);
            this.target = (WritableIntegerValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).intValue();
            this.leftValue = this.target.get();
        }

        @Override
        public void interpolate(double d) {
            int n = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, d);
            this.target.set(n);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    private static class TangentLongInterpolationInterval
    extends TangentInterpolationInterval {
        private final WritableLongValue target;

        private TangentLongInterpolationInterval(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
            super(keyValue, l, keyValue2, l2);
            assert (keyValue.getTarget() instanceof WritableLongValue);
            this.target = (WritableLongValue)keyValue.getTarget();
        }

        private TangentLongInterpolationInterval(KeyValue keyValue, long l) {
            super(keyValue, l);
            assert (keyValue.getTarget() instanceof WritableLongValue);
            this.target = (WritableLongValue)keyValue.getTarget();
            this.recalculateStartValue(this.target.get());
        }

        @Override
        public void interpolate(double d) {
            this.target.set(Math.round(this.calculate(d)));
        }

        @Override
        public void recalculateStartValue() {
            this.recalculateStartValue(this.target.get());
        }
    }

    private static class LongInterpolationInterval
    extends InterpolationInterval {
        private final WritableLongValue target;
        private long leftValue;
        private final long rightValue;

        private LongInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableLongValue && keyValue.getEndValue() instanceof Number && object instanceof Number);
            this.target = (WritableLongValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).longValue();
            this.leftValue = ((Number)object).longValue();
        }

        private LongInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getTarget() instanceof WritableLongValue && keyValue.getEndValue() instanceof Number);
            this.target = (WritableLongValue)keyValue.getTarget();
            this.rightValue = ((Number)keyValue.getEndValue()).longValue();
            this.leftValue = this.target.get();
        }

        @Override
        public void interpolate(double d) {
            long l = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, d);
            this.target.set(l);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    private static class ObjectInterpolationInterval
    extends InterpolationInterval {
        private final WritableValue target;
        private Object leftValue;
        private final Object rightValue;

        private ObjectInterpolationInterval(KeyValue keyValue, long l, Object object) {
            super(l, keyValue.getInterpolator());
            this.target = keyValue.getTarget();
            this.rightValue = keyValue.getEndValue();
            this.leftValue = object;
        }

        private ObjectInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            this.target = keyValue.getTarget();
            this.rightValue = keyValue.getEndValue();
            this.leftValue = this.target.getValue();
        }

        @Override
        public void interpolate(double d) {
            Object object = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, d);
            this.target.setValue(object);
        }

        @Override
        public void recalculateStartValue() {
            this.leftValue = this.target.getValue();
        }
    }

    private static abstract class TangentInterpolationInterval
    extends InterpolationInterval {
        private final double duration;
        private final double p2;
        protected final double p3;
        private final NumberTangentInterpolator leftInterpolator;
        protected double p0;
        private double p1;

        private TangentInterpolationInterval(KeyValue keyValue, long l, KeyValue keyValue2, long l2) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getEndValue() instanceof Number && keyValue2.getEndValue() instanceof Number);
            this.duration = l2;
            Interpolator interpolator = keyValue2.getInterpolator();
            this.leftInterpolator = interpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator)interpolator : null;
            this.recalculateStartValue(((Number)keyValue2.getEndValue()).doubleValue());
            NumberTangentInterpolator numberTangentInterpolator = this.rightInterpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator)this.rightInterpolator : null;
            this.p3 = ((Number)keyValue.getEndValue()).doubleValue();
            double d = numberTangentInterpolator == null ? 0.0 : (numberTangentInterpolator.getInValue() - this.p3) * (double)l2 / numberTangentInterpolator.getInTicks() / 3.0;
            this.p2 = this.p3 + d;
        }

        private TangentInterpolationInterval(KeyValue keyValue, long l) {
            super(l, keyValue.getInterpolator());
            assert (keyValue.getEndValue() instanceof Number);
            this.duration = l;
            this.leftInterpolator = null;
            NumberTangentInterpolator numberTangentInterpolator = this.rightInterpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator)this.rightInterpolator : null;
            this.p3 = ((Number)keyValue.getEndValue()).doubleValue();
            double d = numberTangentInterpolator == null ? 0.0 : (numberTangentInterpolator.getInValue() - this.p3) * this.duration / numberTangentInterpolator.getInTicks() / 3.0;
            this.p2 = this.p3 + d;
        }

        protected double calculate(double d) {
            double d2 = 1.0 - d;
            double d3 = d * d;
            double d4 = d2 * d2;
            return d4 * d2 * this.p0 + 3.0 * d4 * d * this.p1 + 3.0 * d2 * d3 * this.p2 + d3 * d * this.p3;
        }

        protected final void recalculateStartValue(double d) {
            this.p0 = d;
            double d2 = this.leftInterpolator == null ? 0.0 : (this.leftInterpolator.getOutValue() - this.p0) * this.duration / this.leftInterpolator.getOutTicks() / 3.0;
            this.p1 = this.p0 + d2;
        }
    }
}

