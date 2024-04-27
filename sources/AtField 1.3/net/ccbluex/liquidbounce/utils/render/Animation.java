/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 */
package net.ccbluex.liquidbounce.utils.render;

import kotlin.NoWhenBranchMatchedException;
import net.ccbluex.liquidbounce.utils.render.Animation$WhenMappings;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;

public final class Animation {
    private final double from;
    private final long duration;
    private final EaseUtils.EnumEasingType type;
    private final double to;
    private long startTime;
    private final EaseUtils.EnumEasingOrder order;
    private EnumAnimationState state;

    public final Animation start() {
        if (this.state != EnumAnimationState.NOT_STARTED) {
            throw (Throwable)new IllegalStateException("Animation already started!");
        }
        this.startTime = System.currentTimeMillis();
        this.state = EnumAnimationState.DURING;
        return this;
    }

    public final void setState(EnumAnimationState enumAnimationState) {
        this.state = enumAnimationState;
    }

    public final double getTo() {
        return this.to;
    }

    public final long getDuration() {
        return this.duration;
    }

    public final double getValue() {
        double d;
        switch (Animation$WhenMappings.$EnumSwitchMapping$0[this.state.ordinal()]) {
            case 1: {
                d = this.from;
                break;
            }
            case 2: {
                double d2 = (double)(System.currentTimeMillis() - this.startTime) / (double)this.duration;
                if (d2 > 1.0) {
                    this.state = EnumAnimationState.STOPPED;
                    d = this.to;
                    break;
                }
                d = this.from + (this.to - this.from) * EaseUtils.INSTANCE.apply(this.type, this.order, d2);
                break;
            }
            case 3: {
                d = this.to;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return d;
    }

    public final EaseUtils.EnumEasingOrder getOrder() {
        return this.order;
    }

    public final EnumAnimationState getState() {
        return this.state;
    }

    public final EaseUtils.EnumEasingType getType() {
        return this.type;
    }

    public final double getFrom() {
        return this.from;
    }

    public Animation(EaseUtils.EnumEasingType enumEasingType, EaseUtils.EnumEasingOrder enumEasingOrder, double d, double d2, long l) {
        this.type = enumEasingType;
        this.order = enumEasingOrder;
        this.from = d;
        this.to = d2;
        this.duration = l;
        this.state = EnumAnimationState.NOT_STARTED;
    }

    public static final class EnumAnimationState
    extends Enum {
        public static final /* enum */ EnumAnimationState DURING;
        private static final EnumAnimationState[] $VALUES;
        public static final /* enum */ EnumAnimationState STOPPED;
        public static final /* enum */ EnumAnimationState NOT_STARTED;

        public static EnumAnimationState valueOf(String string) {
            return Enum.valueOf(EnumAnimationState.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private EnumAnimationState() {
            void var2_-1;
            void var1_-1;
        }

        public static EnumAnimationState[] values() {
            return (EnumAnimationState[])$VALUES.clone();
        }

        static {
            EnumAnimationState[] enumAnimationStateArray = new EnumAnimationState[3];
            EnumAnimationState[] enumAnimationStateArray2 = enumAnimationStateArray;
            enumAnimationStateArray[0] = NOT_STARTED = new EnumAnimationState("NOT_STARTED", 0);
            enumAnimationStateArray[1] = DURING = new EnumAnimationState("DURING", 1);
            enumAnimationStateArray[2] = STOPPED = new EnumAnimationState("STOPPED", 2);
            $VALUES = enumAnimationStateArray;
        }
    }
}

