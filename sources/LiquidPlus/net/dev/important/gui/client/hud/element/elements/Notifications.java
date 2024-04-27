/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.gui.client.hud.element.elements;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ElementInfo(name="Notifications", single=true)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010(\u001a\u0004\u0018\u00010)H\u0016R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0014\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u0016\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\u0018\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\rR\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010 \u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001dR\u0011\u0010\"\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001dR\u0011\u0010$\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'\u00a8\u0006*"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Notifications;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "animationSpeed", "Lnet/dev/important/value/FloatValue;", "getAnimationSpeed", "()Lnet/dev/important/value/FloatValue;", "bgAlphaValue", "Lnet/dev/important/value/IntegerValue;", "getBgAlphaValue", "()Lnet/dev/important/value/IntegerValue;", "bgBlueValue", "getBgBlueValue", "bgGreenValue", "getBgGreenValue", "bgRedValue", "getBgRedValue", "blurStrength", "getBlurStrength", "blurValue", "Lnet/dev/important/value/BoolValue;", "getBlurValue", "()Lnet/dev/important/value/BoolValue;", "exampleNotification", "Lnet/dev/important/gui/client/hud/element/elements/Notification;", "newAnimValue", "getNewAnimValue", "smoothYTransition", "getSmoothYTransition", "styleValue", "Lnet/dev/important/value/ListValue;", "getStyleValue", "()Lnet/dev/important/value/ListValue;", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "LiquidBounce"})
public final class Notifications
extends Element {
    @NotNull
    private final BoolValue smoothYTransition;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final ListValue styleValue;
    @NotNull
    private final BoolValue newAnimValue;
    @NotNull
    private final FloatValue animationSpeed;
    @NotNull
    private final IntegerValue bgRedValue;
    @NotNull
    private final IntegerValue bgGreenValue;
    @NotNull
    private final IntegerValue bgBlueValue;
    @NotNull
    private final IntegerValue bgAlphaValue;
    @NotNull
    private final Notification exampleNotification;

    public Notifications(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        super(x, y, scale, side);
        this.smoothYTransition = new BoolValue("Smooth-YTransition", true);
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        String[] stringArray = new String[]{"Compact", "Full", "New"};
        this.styleValue = new ListValue("Style", stringArray, "Compact");
        this.newAnimValue = new BoolValue("UseNewAnim", true);
        this.animationSpeed = new FloatValue("Anim-Speed", 0.5f, 0.01f, 1.0f, new Function0<Boolean>(this){
            final /* synthetic */ Notifications this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getNewAnimValue().get();
            }
        });
        this.bgRedValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.bgGreenValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.bgBlueValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.bgAlphaValue = new IntegerValue("Background-Alpha", 190, 0, 255);
        this.exampleNotification = new Notification("Example Notification", Notification.Type.INFO);
    }

    public /* synthetic */ Notifications(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 0.0;
        }
        if ((n & 2) != 0) {
            d2 = 30.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN);
        }
        this(d, d2, f, side);
    }

    @NotNull
    public final BoolValue getSmoothYTransition() {
        return this.smoothYTransition;
    }

    @NotNull
    public final BoolValue getBlurValue() {
        return this.blurValue;
    }

    @NotNull
    public final FloatValue getBlurStrength() {
        return this.blurStrength;
    }

    @NotNull
    public final ListValue getStyleValue() {
        return this.styleValue;
    }

    @NotNull
    public final BoolValue getNewAnimValue() {
        return this.newAnimValue;
    }

    @NotNull
    public final FloatValue getAnimationSpeed() {
        return this.animationSpeed;
    }

    @NotNull
    public final IntegerValue getBgRedValue() {
        return this.bgRedValue;
    }

    @NotNull
    public final IntegerValue getBgGreenValue() {
        return this.bgGreenValue;
    }

    @NotNull
    public final IntegerValue getBgBlueValue() {
        return this.bgBlueValue;
    }

    @NotNull
    public final IntegerValue getBgAlphaValue() {
        return this.bgAlphaValue;
    }

    @Override
    @Nullable
    public Border drawElement() {
        float animationY = 0.0f;
        animationY = 30.0f;
        List notifications = new ArrayList();
        for (Notification i : Client.INSTANCE.getHud().getNotifications()) {
            notifications.add(i);
        }
        if (!(MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner) || !notifications.isEmpty()) {
            for (Notification i : notifications) {
                Unit unit;
                i.drawNotification(animationY, this);
                Unit it = unit = Unit.INSTANCE;
                boolean bl = false;
                String string = ((String)this.getStyleValue().get()).toLowerCase();
                Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                String string2 = string;
                animationY += (float)(Intrinsics.areEqual(string2, "compact") ? 20 : (Intrinsics.areEqual(string2, "full") ? 30 : 30)) * (this.getSide().getVertical() == Side.Vertical.DOWN ? 1.0f : -1.0f);
            }
        } else {
            this.exampleNotification.drawNotification(animationY, this);
        }
        if (MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner) {
            this.exampleNotification.setFadeState(Notification.FadeState.STAY);
            this.exampleNotification.setX((float)this.exampleNotification.getTextLength() + 8.0f);
            if (this.exampleNotification.getStayTimer().hasTimePassed(this.exampleNotification.getDisplayTime())) {
                this.exampleNotification.getStayTimer().reset();
            }
            return StringsKt.equals((String)this.styleValue.get(), "compact", true) ? new Border(-102.0f, -48.0f, 0.0f, -30.0f) : new Border(-130.0f, -58.0f, 0.0f, -30.0f);
        }
        return null;
    }

    public Notifications() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

