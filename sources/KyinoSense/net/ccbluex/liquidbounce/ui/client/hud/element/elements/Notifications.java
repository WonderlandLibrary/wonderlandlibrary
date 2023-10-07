/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ElementInfo(name="Notifications", single=true)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notifications;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "animationSpeed", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "exampleNotification", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification;", "newAnimValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "smoothYTransition", "styleValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class Notifications
extends Element {
    private final BoolValue smoothYTransition;
    private final ListValue styleValue;
    private final BoolValue newAnimValue;
    private final FloatValue animationSpeed;
    private final Notification exampleNotification;

    @Override
    @Nullable
    public Border drawElement() {
        Color bgColor = new Color(0, 0, 0, 190);
        float animationY = 30.0f;
        boolean bl = false;
        List notifications = new ArrayList();
        for (Notification i : LiquidBounce.INSTANCE.getHud().getNotifications()) {
            notifications.add(i);
        }
        if (!(Notifications.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner) || !notifications.isEmpty()) {
            for (Notification i : notifications) {
                int n;
                String string;
                i.drawNotification(animationY, (Boolean)this.smoothYTransition.get(), (Boolean)this.newAnimValue.get(), ((Number)this.animationSpeed.get()).floatValue(), bgColor, this.getSide(), (String)this.styleValue.get(), false, 0.0f, (float)this.getRenderX(), (float)this.getRenderY());
                Unit unit = Unit.INSTANCE;
                boolean bl2 = false;
                boolean bl3 = false;
                Unit it = unit;
                boolean bl4 = false;
                String string2 = (String)this.styleValue.get();
                float f = animationY;
                boolean bl5 = false;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                switch (string) {
                    case "compact": {
                        n = 20;
                        break;
                    }
                    case "full": {
                        n = 30;
                        break;
                    }
                    default: {
                        n = 30;
                    }
                }
                animationY = f + (float)n * (this.getSide().getVertical() == Side.Vertical.DOWN ? 1.0f : -1.0f);
            }
        } else {
            this.exampleNotification.drawNotification(animationY, (Boolean)this.smoothYTransition.get(), (Boolean)this.newAnimValue.get(), ((Number)this.animationSpeed.get()).floatValue(), bgColor, this.getSide(), (String)this.styleValue.get(), false, 0.0f, (float)this.getRenderX(), (float)this.getRenderY());
        }
        if (Notifications.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner) {
            this.exampleNotification.setFadeState(Notification.FadeState.STAY);
            this.exampleNotification.setX((float)this.exampleNotification.getTextLength() + 8.0f);
            if (this.exampleNotification.getStayTimer().hasTimePassed(this.exampleNotification.getDisplayTime())) {
                this.exampleNotification.getStayTimer().reset();
            }
            return StringsKt.equals((String)this.styleValue.get(), "compact", true) ? new Border(-102.0f, -48.0f, 0.0f, -30.0f) : new Border(-130.0f, -58.0f, 0.0f, -30.0f);
        }
        return null;
    }

    public Notifications(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.smoothYTransition = new BoolValue("Smooth-YTransition", true);
        this.styleValue = new ListValue("Style", new String[]{"IntelliJ"}, "IntelliJ");
        this.newAnimValue = new BoolValue("UseNewAnim", true);
        this.animationSpeed = new FloatValue("Animation-Speed", 0.5f, 0.01f, 1.0f);
        this.exampleNotification = new Notification("This is an example notification.", Notification.Type.INFO);
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

    public Notifications() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

