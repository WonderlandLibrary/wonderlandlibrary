/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HUD", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
public final class HUD
extends Module {
    private final ListValue notificationStyle = new ListValue("notificationStyle", new String[]{"None", "Windows11"}, "None");
    private final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    private final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    private final BoolValue fontChatValue = new BoolValue("FontChat", false);
    private final FloatValue BlurStrength = new FloatValue("BlurStrength", 15.0f, 0.0f, 30.0f);
    private final IntegerValue Radius = new IntegerValue("BlurRadius", 10, 1, 50);
    private final IntegerValue gradientSpeed = new IntegerValue("DoubleColor-Speed", 100, 10, 1000);
    private final FloatValue rainbowStartValue = new FloatValue("RainbowStart", 0.41f, 0.0f, 1.0f);
    private final FloatValue rainbowStopValue = new FloatValue("RainbowStop", 0.58f, 0.0f, 1.0f);
    private final FloatValue rainbowSaturationValue = new FloatValue("RainbowSaturation", 0.7f, 0.0f, 1.0f);
    private final FloatValue rainbowBrightnessValue = new FloatValue("RainbowBrightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue rainbowSpeedValue = new IntegerValue("RainbowSpeed", 1500, 500, 7000);

    public final ListValue getNotificationStyle() {
        return this.notificationStyle;
    }

    public final BoolValue getBlackHotbarValue() {
        return this.blackHotbarValue;
    }

    public final BoolValue getInventoryParticle() {
        return this.inventoryParticle;
    }

    public final BoolValue getFontChatValue() {
        return this.fontChatValue;
    }

    public final FloatValue getBlurStrength() {
        return this.BlurStrength;
    }

    public final IntegerValue getRadius() {
        return this.Radius;
    }

    public final IntegerValue getGradientSpeed() {
        return this.gradientSpeed;
    }

    public final FloatValue getRainbowStartValue() {
        return this.rainbowStartValue;
    }

    public final FloatValue getRainbowStopValue() {
        return this.rainbowStopValue;
    }

    public final FloatValue getRainbowSaturationValue() {
        return this.rainbowSaturationValue;
    }

    public final FloatValue getRainbowBrightnessValue() {
        return this.rainbowBrightnessValue;
    }

    public final IntegerValue getRainbowSpeedValue() {
        return this.rainbowSpeedValue;
    }

    @EventTarget
    public final void onRender2D(@Nullable Render2DEvent event) {
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        LiquidBounce.INSTANCE.getHud().render(false);
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        LiquidBounce.INSTANCE.getHud().update();
    }

    @EventTarget
    public final void onKey(KeyEvent event) {
        LiquidBounce.INSTANCE.getHud().handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition=true)
    public final void onScreen(ScreenEvent event) {
        if (MinecraftInstance.mc.getTheWorld() == null || MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (this.getState() && ((Boolean)this.blurValue.get()).booleanValue() && !MinecraftInstance.mc.getEntityRenderer().isShaderActive() && event.getGuiScreen() != null && !MinecraftInstance.classProvider.isGuiChat(event.getGuiScreen()) && !MinecraftInstance.classProvider.isGuiHudDesigner(event.getGuiScreen())) {
            MinecraftInstance.mc.getEntityRenderer().loadShader(MinecraftInstance.classProvider.createResourceLocation("liquidbounce/blur.json"));
        } else if (MinecraftInstance.mc.getEntityRenderer().getShaderGroup() != null) {
            IShaderGroup iShaderGroup = MinecraftInstance.mc.getEntityRenderer().getShaderGroup();
            if (iShaderGroup == null) {
                Intrinsics.throwNpe();
            }
            if (iShaderGroup.getShaderGroupName().equals("liquidbounce/blur.json")) {
                MinecraftInstance.mc.getEntityRenderer().stopUseShader();
            }
        }
    }

    public HUD() {
        this.setState(true);
    }
}

