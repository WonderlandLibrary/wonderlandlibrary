/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.utils.render;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;

public final class AnimatedValue {
    private Animation animation;
    private double value;
    private EaseUtils.EnumEasingType type = EaseUtils.EnumEasingType.NONE;
    private EaseUtils.EnumEasingOrder order = EaseUtils.EnumEasingOrder.FAST_AT_START;
    private long duration = 300L;

    public final double getValue() {
        if (this.animation != null) {
            Animation animation = this.animation;
            if (animation == null) {
                Intrinsics.throwNpe();
            }
            this.value = animation.getValue();
            Animation animation2 = this.animation;
            if (animation2 == null) {
                Intrinsics.throwNpe();
            }
            if (animation2.getState() == Animation.EnumAnimationState.STOPPED) {
                this.animation = null;
            }
        }
        return this.value;
    }

    public final void setValue(double value) {
        block5: {
            block4: {
                if (this.animation == null) break block4;
                if (this.animation == null) break block5;
                Animation animation = this.animation;
                if (animation == null) {
                    Intrinsics.throwNpe();
                }
                if (animation.getTo() == value) break block5;
            }
            this.animation = new Animation(this.type, this.order, this.value, value, this.duration).start();
        }
    }

    public final EaseUtils.EnumEasingType getType() {
        return this.type;
    }

    public final void setType(EaseUtils.EnumEasingType enumEasingType) {
        this.type = enumEasingType;
    }

    public final EaseUtils.EnumEasingOrder getOrder() {
        return this.order;
    }

    public final void setOrder(EaseUtils.EnumEasingOrder enumEasingOrder) {
        this.order = enumEasingOrder;
    }

    public final long getDuration() {
        return this.duration;
    }

    public final void setDuration(long l) {
        this.duration = l;
    }

    public final double sync(double valueIn) {
        this.setValue(valueIn);
        return this.getValue();
    }
}

