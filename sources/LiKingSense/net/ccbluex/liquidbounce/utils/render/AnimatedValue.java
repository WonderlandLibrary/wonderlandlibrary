/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u0018R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R&\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0017\u001a\u00020\u00188F@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u0006\u001f"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/AnimatedValue;", "", "()V", "animation", "Lnet/ccbluex/liquidbounce/utils/render/Animation;", "duration", "", "getDuration", "()J", "setDuration", "(J)V", "order", "Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingOrder;", "getOrder", "()Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingOrder;", "setOrder", "(Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingOrder;)V", "type", "Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingType;", "getType", "()Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingType;", "setType", "(Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingType;)V", "value", "", "getValue", "()D", "setValue", "(D)V", "sync", "valueIn", "LiKingSense"})
public final class AnimatedValue {
    private Animation animation;
    private double value;
    @NotNull
    private EaseUtils.EnumEasingType type = EaseUtils.EnumEasingType.NONE;
    @NotNull
    private EaseUtils.EnumEasingOrder order = EaseUtils.EnumEasingOrder.FAST_AT_START;
    private long duration = 300L;

    public final double getValue() {
        if (this.animation != null) {
            this.value = this.animation.getValue();
            if (this.animation.getState() == Animation.EnumAnimationState.STOPPED) {
                this.animation = null;
            }
        }
        return this.value;
    }

    public final void setValue(double value) {
        if (this.animation == null || this.animation != null && this.animation.getTo() != value) {
            this.animation = new Animation(this.type, this.order, this.value, value, this.duration).start();
        }
    }

    @NotNull
    public final EaseUtils.EnumEasingType getType() {
        return this.type;
    }

    public final void setType(@NotNull EaseUtils.EnumEasingType enumEasingType) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)enumEasingType), (String)"<set-?>");
        this.type = enumEasingType;
    }

    @NotNull
    public final EaseUtils.EnumEasingOrder getOrder() {
        return this.order;
    }

    public final void setOrder(@NotNull EaseUtils.EnumEasingOrder enumEasingOrder) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)enumEasingOrder), (String)"<set-?>");
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

