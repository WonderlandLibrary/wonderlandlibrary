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
    private EnumAnimationState state;
    private long startTime;
    private final EaseUtils.EnumEasingType type;
    private final EaseUtils.EnumEasingOrder order;
    private final double from;
    private final double to;
    private final long duration;

    public final EnumAnimationState getState() {
        return this.state;
    }

    public final void setState(EnumAnimationState enumAnimationState) {
        this.state = enumAnimationState;
    }

    public final Animation start() {
        if (this.state != EnumAnimationState.NOT_STARTED) {
            throw (Throwable)new IllegalStateException("Animation already started!");
        }
        this.startTime = System.currentTimeMillis();
        this.state = EnumAnimationState.DURING;
        return this;
    }

    public final double getValue() {
        double d;
        switch (Animation$WhenMappings.$EnumSwitchMapping$0[this.state.ordinal()]) {
            case 1: {
                d = this.from;
                break;
            }
            case 2: {
                double percent = (double)(System.currentTimeMillis() - this.startTime) / (double)this.duration;
                if (percent > 1.0) {
                    this.state = EnumAnimationState.STOPPED;
                    d = this.to;
                    break;
                }
                d = this.from + (this.to - this.from) * EaseUtils.INSTANCE.apply(this.type, this.order, percent);
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

    public final EaseUtils.EnumEasingType getType() {
        return this.type;
    }

    public final EaseUtils.EnumEasingOrder getOrder() {
        return this.order;
    }

    public final double getFrom() {
        return this.from;
    }

    public final double getTo() {
        return this.to;
    }

    public final long getDuration() {
        return this.duration;
    }

    public Animation(EaseUtils.EnumEasingType type, EaseUtils.EnumEasingOrder order, double from, double to, long duration) {
        this.type = type;
        this.order = order;
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.state = EnumAnimationState.NOT_STARTED;
    }

    public static final class EnumAnimationState
    extends Enum<EnumAnimationState> {
        public static final /* enum */ EnumAnimationState NOT_STARTED;
        public static final /* enum */ EnumAnimationState DURING;
        public static final /* enum */ EnumAnimationState STOPPED;
        private static final /* synthetic */ EnumAnimationState[] $VALUES;

        static {
            EnumAnimationState[] enumAnimationStateArray = new EnumAnimationState[3];
            EnumAnimationState[] enumAnimationStateArray2 = enumAnimationStateArray;
            enumAnimationStateArray[0] = NOT_STARTED = new EnumAnimationState();
            enumAnimationStateArray[1] = DURING = new EnumAnimationState();
            enumAnimationStateArray[2] = STOPPED = new EnumAnimationState();
            $VALUES = enumAnimationStateArray;
        }

        public static EnumAnimationState[] values() {
            return (EnumAnimationState[])$VALUES.clone();
        }

        public static EnumAnimationState valueOf(String string) {
            return Enum.valueOf(EnumAnimationState.class, string);
        }
    }
}

