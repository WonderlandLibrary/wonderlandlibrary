/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import dev.sakura_starring.util.render.Screen;
import dev.sakura_starring.util.time.Timer;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.event.BlurEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HUD", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
public final class HUD
extends Module {
    private int count;
    private final IntegerValue widgetY;
    private final Timer timer = new Timer();
    private final IntegerValue widgetHeight;
    private final BoolValue blurValue;
    private final IntegerValue widgetX;
    private final BoolValue chatRect;
    private final BoolValue chatAnimValue;
    private final BoolValue fontChatValue = new BoolValue("FontChat", false);
    private final IntegerValue widgetWidth;
    private final Map bottomLeftText;
    private final IntegerValue blurStrength;
    private final BoolValue inventoryParticle;
    private final IntegerValue Radius;
    private final ListValue widget;

    public final ListValue getWidget() {
        return this.widget;
    }

    @EventTarget
    public final void onRender2D(@Nullable Render2DEvent render2DEvent) {
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        this.drawWidget();
        LiquidBounce.INSTANCE.getHud().render(false);
        this.draw();
    }

    private final Color mixColors(Color color, Color color2) {
        return ColorUtil.interpolateColorsBackAndForth(15, 1, color, color2, (Boolean)CustomColor.hueInterpolation.get());
    }

    private final void draw() {
        GlStateManager.func_179117_G();
    }

    /*
     * Enabled aggressive block sorting
     */
    private final void drawWidget() {
        int n;
        String string;
        int n2 = ((Number)this.widgetWidth.get()).intValue();
        int n3 = ((Number)this.widgetHeight.get()).intValue();
        int n4 = Screen.getWidth() - 100 - n2 - ((Number)this.widgetX.get()).intValue();
        int n5 = Screen.getHeight() - n3 - (MinecraftInstance.mc2.field_71456_v.func_146158_b().func_146241_e() ? 14 : 0 - ((Number)this.widgetY.get()).intValue());
        if (Objects.equals(this.widget.getValue(), "None")) return;
        if (Objects.equals(this.widget.getValue(), "PaiMon")) {
            if (this.timer.hasTimeElapsed(25L, true)) {
                if (this.count > 51) {
                    this.count = 0;
                } else {
                    int n6 = this.count;
                    this.count = n6 + 1;
                }
            }
            String string2 = "atfield/widgets/genshinimpact/paimon/paimon-" + this.count + ".png";
            IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.toLowerCase();
            RenderUtils.drawImage(iClassProvider.createResourceLocation(string4), n4, n5, n2, n3);
            return;
        }
        if (Objects.equals(this.widget.getValue(), "Fubuki")) {
            if (this.timer.hasTimeElapsed(50L, true)) {
                if (this.count <= 0 || this.count > 15) {
                    this.count = 1;
                } else {
                    int n7 = this.count;
                    this.count = n7 + 1;
                }
            }
            String string5 = "atfield/widgets/fubuki/" + this.count + ".png";
            IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
            boolean bl = false;
            String string6 = string5;
            if (string6 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string7 = string6.toLowerCase();
            RenderUtils.drawImage(iClassProvider.createResourceLocation(string7), n4, n5, n2, n3);
            return;
        }
        if (Objects.equals(this.widget.getValue(), "Lumine")) {
            String string8 = "atfield/widgets/genshinimpact/" + (String)this.widget.getValue() + ".png";
            IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
            boolean bl = false;
            String string9 = string8;
            if (string9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string10 = string9.toLowerCase();
            RenderUtils.drawImage(iClassProvider.createResourceLocation(string10), n4, n5, n2, n3);
            return;
        }
        if (((String)this.widget.getValue()).length() >= 6) {
            string = (String)this.widget.getValue();
            n = 0;
            int n8 = 6;
            boolean bl = false;
            String string11 = string;
            if (string11 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            if (string11.substring(n, n8).equals("MaoMao")) {
                string = "atfield/widgets/maomao/" + (String)this.widget.getValue() + ".png";
                IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
                n = 0;
                String string12 = string;
                if (string12 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string13 = string12.toLowerCase();
                RenderUtils.drawImage(iClassProvider.createResourceLocation(string13), n4, n5, n2, n3);
                return;
            }
        }
        string = "atfield/widgets/daydream/" + (String)this.widget.getValue() + ".png";
        IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
        n = 0;
        String string14 = string;
        if (string14 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string15 = string14.toLowerCase();
        RenderUtils.drawImage(iClassProvider.createResourceLocation(string15), n4, n5, n2, n3);
    }

    public final BoolValue getBlurValue() {
        return this.blurValue;
    }

    @EventTarget
    public final void onKey(KeyEvent keyEvent) {
        LiquidBounce.INSTANCE.getHud().handleKey('a', keyEvent.getKey());
    }

    @EventTarget(ignoreCondition=true)
    public final void onScreen(ScreenEvent screenEvent) {
        if (MinecraftInstance.mc.getTheWorld() == null || MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (this.getState() && ((Boolean)this.blurValue.get()).booleanValue() && !MinecraftInstance.mc.getEntityRenderer().isShaderActive() && screenEvent.getGuiScreen() != null && !MinecraftInstance.classProvider.isGuiChat(screenEvent.getGuiScreen()) && !MinecraftInstance.classProvider.isGuiHudDesigner(screenEvent.getGuiScreen())) {
            MinecraftInstance.mc.getEntityRenderer().loadShader(MinecraftInstance.classProvider.createResourceLocation("More/blur.json"));
        } else if (MinecraftInstance.mc.getEntityRenderer().getShaderGroup() != null) {
            IShaderGroup iShaderGroup = MinecraftInstance.mc.getEntityRenderer().getShaderGroup();
            if (iShaderGroup == null) {
                Intrinsics.throwNpe();
            }
            if (iShaderGroup.getShaderGroupName().equals("More/blur.json")) {
                MinecraftInstance.mc.getEntityRenderer().stopUseShader();
            }
        }
    }

    public HUD() {
        this.chatRect = new BoolValue("ChatRect", false);
        this.chatAnimValue = new BoolValue("ChatAnimation", true);
        this.blurStrength = new IntegerValue("GlobalBlurStrength", 1, 1, 20);
        this.inventoryParticle = new BoolValue("InventoryParticle", false);
        this.blurValue = new BoolValue("GuiBlur", false);
        this.Radius = new IntegerValue("BlurRadius", 10, 1, 50);
        this.bottomLeftText = new LinkedHashMap();
        this.widget = new ListValue("Widget", new String[]{"None", "MaoMao1", "MaoMao2", "MaoMao3", "MaoMao4", "MaoMao5", "MaoMao6", "MaoMao7", "MaoMao8", "MaoMao9", "MaoMao10", "MaoMao11", "MaoMao11", "MaoMao12", "MaoMao13", "MaoMao14", "MaoMao15", "MaoMao16", "MaoMao17", "MaoMao18", "MaoMao19", "MaoMao20", "MaoMao21", "Lumine", "Fubuki", "PaiMon", "LingLan", "ShiYuan_QianXia", "XingYe_Xun", "YingJing_JieYi"}, "None");
        this.widgetWidth = new IntegerValue("Widget Width", 100, 50, 400);
        this.widgetHeight = new IntegerValue("Widget Height", 100, 50, 400);
        this.widgetX = new IntegerValue("Widget X", 0, 0, 2000);
        this.widgetY = new IntegerValue("Widget Y", 0, -200, 200);
        this.setState(true);
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        LiquidBounce.INSTANCE.getHud().update();
        this.drawWidget();
    }

    private final double calculateBPS() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = d - iEntityPlayerSP2.getPrevPosX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP3.getPosZ();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = d3 - iEntityPlayerSP4.getPrevPosZ();
        boolean bl = false;
        double d5 = Math.hypot(d2, d4) * (double)MinecraftInstance.mc.getTimer().getTimerSpeed() * (double)20;
        return (double)MathKt.roundToLong((double)(d5 * 100.0)) / 100.0;
    }

    public final BoolValue getFontChatValue() {
        return this.fontChatValue;
    }

    public final BoolValue getInventoryParticle() {
        return this.inventoryParticle;
    }

    @EventTarget
    public final void shader(BlurEvent blurEvent) {
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        this.drawWidget();
        this.draw();
    }

    public final IntegerValue getBlurStrength() {
        return this.blurStrength;
    }

    private final Color getClientColor(HUD hUD) {
        return new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue());
    }

    public final IntegerValue getRadius() {
        return this.Radius;
    }

    private final Color[] getClientColors(HUD hUD) {
        Color color = this.mixColors(this.getClientColor(hUD), this.getAlternateClientColor(hUD));
        Color color2 = this.mixColors(this.getAlternateClientColor(hUD), this.getClientColor(hUD));
        return new Color[]{color, color2};
    }

    public final BoolValue getChatRect() {
        return this.chatRect;
    }

    public final BoolValue getChatAnimValue() {
        return this.chatAnimValue;
    }

    private final Color getAlternateClientColor(HUD hUD) {
        return new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue());
    }
}

