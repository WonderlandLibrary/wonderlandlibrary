/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Notifications", single=true)
public final class Notifications
extends Element {
    private final Notification exampleNotification;

    /*
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        void $this$mapTo$iv$iv;
        Iterable $this$map$iv = LiquidBounce.INSTANCE.getHud().getNotifications();
        boolean $i$f$map = false;
        Iterable iterable = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void it;
            Notification notification = (Notification)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl2 = false;
            void var14_18 = it;
            collection.add(var14_18);
        }
        Iterable $this$forEachIndexed$iv = (List)destination$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            void notify;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            Notification bl2 = (Notification)item$iv;
            int index = n2;
            boolean bl3 = false;
            GL11.glPushMatrix();
            IFontRenderer font = Fonts.font40;
            if (notify.drawNotification(index, font)) {
                LiquidBounce.INSTANCE.getHud().getNotifications().remove(notify);
            }
            GL11.glPopMatrix();
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        Function0<Border> $fun$getTBorder$3 = new Function0<Border>(this, hud){
            final /* synthetic */ Notifications this$0;
            final /* synthetic */ HUD $hud;

            /*
             * Unable to fully structure code
             */
            public final Border invoke() {
                block4: {
                    var1_1 = (String)this.$hud.getNotificationStyle().get();
                    switch (var1_1.hashCode()) {
                        case 1786983363: {
                            if (!var1_1.equals("Windows11")) ** break;
                            break;
                        }
                        case 2612601: {
                            if (!var1_1.equals("Tomk")) ** break;
                            v0 = new Border(-((float)Notifications.access$getExampleNotification$p(this.this$0).getWidth()), -((float)Notifications.access$getExampleNotification$p(this.this$0).getHeight()), 0.0f, 0.0f);
                            break block4;
                        }
                    }
                    v0 = new Border(-((float)Notifications.access$getExampleNotification$p(this.this$0).getWidth2()), -((float)Notifications.access$getExampleNotification$p(this.this$0).getHeight()), 0.0f, 0.0f);
                    break block4;
                    v0 = new Border(-((float)Notifications.access$getExampleNotification$p(this.this$0).getWidth()), -((float)Notifications.access$getExampleNotification$p(this.this$0).getHeight()), 0.0f, 0.0f);
                }
                return v0;
            }
            {
                this.this$0 = notifications;
                this.$hud = hUD;
                super(0);
            }
        };
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            if (!LiquidBounce.INSTANCE.getHud().getNotifications().contains(this.exampleNotification)) {
                LiquidBounce.INSTANCE.getHud().addNotification(this.exampleNotification);
            }
            this.exampleNotification.setFadeState(FadeState.STAY);
            this.exampleNotification.setDisplayTime(System.currentTimeMillis());
            return $fun$getTBorder$3.invoke();
        }
        return null;
    }

    public Notifications(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
        this.exampleNotification = new Notification("\u6a21\u5757\u5f00\u542f\u901a\u77e5", "\u8fd9\u662f\u4e00\u4e2a\u4f8b\u5b50.", NotifyType.INFO, 0, 0, 24, null);
    }

    public /* synthetic */ Notifications(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 3.0;
        }
        if ((n & 2) != 0) {
            d2 = 11.0;
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

    public static final /* synthetic */ Notification access$getExampleNotification$p(Notifications $this) {
        return $this.exampleNotification;
    }
}

