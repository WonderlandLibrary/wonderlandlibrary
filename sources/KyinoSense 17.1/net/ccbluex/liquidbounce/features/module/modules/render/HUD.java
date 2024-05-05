/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.boss.BossStatus
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import me.report.liquidware.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.ConnectingEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.render.ColorUtil;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ModuleInfo(name="HUD", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
@SideOnly(value=Side.CLIENT)
public class HUD
extends Module {
    public final BoolValue containerBackground = new BoolValue("Gui-Background", true);
    public final BoolValue playTimeValue = new BoolValue("PlayTime", true);
    public final BoolValue invEffectOffset = new BoolValue("InventoryEffect-Offset", true);
    public final BoolValue InvesionTab = new BoolValue("InvesionTab", true);
    public final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    public final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    public static final BoolValue hueInterpolation = new BoolValue("hueInterpolation", false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    public final BoolValue chatClearValue = new BoolValue("NoChatClear", true);
    public final BoolValue chatCombineValue = new BoolValue("ChatCombine", true);
    public final BoolValue fontChatValue = new BoolValue("FontChat", false);
    public final BoolValue chatRectValue = new BoolValue("ChatRect", false);
    public final BoolValue chatRectBlurValue = new BoolValue("ChatRectBlur", false);
    public final BoolValue chatRectRoundValue = new BoolValue("ChatRectRound", false);
    public final BoolValue betterChatRectValue = new BoolValue("BetterChatRect", false);
    public final BoolValue chatAnimValue = new BoolValue("ChatAnimation", false);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    public static final ListValue clolormode = new ListValue("ColorMode", new String[]{"Rainbow", "Light Rainbow", "Static", "Double Color", "Default"}, "Light Rainbow");
    public static final BoolValue hotbarof = new BoolValue("DisableHotbar", false);
    public static final BoolValue notify = new BoolValue("Notifications(Notifications)", false);
    public static final BoolValue notify233 = new BoolValue("Notifications(Sound)", false);
    private final Map<String, String> bottomLeftText = new LinkedHashMap<String, String>();
    final MSTimer timer = new MSTimer();
    private int startTime;

    public HUD() {
        this.setState(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (((Boolean)this.playTimeValue.get()).booleanValue()) {
            int endTime = (int)System.currentTimeMillis();
            int pasted = endTime - this.startTime;
            String timed = String.format("%dh %dm %ds", TimeUnit.MILLISECONDS.toHours(pasted), TimeUnit.MILLISECONDS.toMinutes(pasted) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(pasted)), TimeUnit.MILLISECONDS.toSeconds(pasted) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pasted)));
            HUD.mc.field_71466_p.func_175063_a(timed, (float)sr.func_78326_a() / 2.0f - (float)HUD.mc.field_71466_p.func_78256_a(timed) / 2.0f, BossStatus.field_82827_c != null && BossStatus.field_82826_b > 0 ? 47.0f : 30.0f, -1);
        }
        if (((Boolean)this.InvesionTab.get()).booleanValue()) {
            HUD.mc.field_71466_p.func_175063_a("KyinoSense Build 17", (float)sr.func_78326_a() / 2.0f - (float)HUD.mc.field_71466_p.func_78256_a("KyinoSense Build 17") / 2.0f, BossStatus.field_82827_c != null && BossStatus.field_82826_b > 0 ? 47.0f : 40.0f, -1);
        }
        EnchantEffect EnchantEffect2 = (EnchantEffect)LiquidBounce.moduleManager.getModule(EnchantEffect.class);
        if (HUD.mc.field_71462_r instanceof GuiHudDesigner) {
            return;
        }
        LiquidBounce.hud.render(false);
    }

    public Color[] getClientColors() {
        Color secondColor;
        Color firstColor;
        switch (((String)clolormode.get()).toLowerCase()) {
            case "lightrainbow": {
                firstColor = ColorUtil.rainbow(15, 1, 0.6f, 1.0f, 1.0f);
                secondColor = ColorUtil.rainbow(15, 40, 0.6f, 1.0f, 1.0f);
                break;
            }
            case "rainbow": {
                firstColor = ColorUtil.rainbow(15, 1, 1.0f, 1.0f, 1.0f);
                secondColor = ColorUtil.rainbow(15, 40, 1.0f, 1.0f, 1.0f);
                break;
            }
            case "double color": {
                firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, Color.PINK, Color.BLUE, (Boolean)hueInterpolation.get());
                secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, Color.PINK, Color.BLUE, (Boolean)hueInterpolation.get());
                break;
            }
            case "static": {
                secondColor = firstColor = new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
                break;
            }
            default: {
                firstColor = new Color(-1);
                secondColor = new Color(-1);
            }
        }
        return new Color[]{firstColor, secondColor};
    }

    @EventTarget
    public void onConnecting(ConnectingEvent e) {
        this.startTime = (int)System.currentTimeMillis();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        LiquidBounce.hud.update();
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        LiquidBounce.hud.handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition=true)
    public void onScreen(ScreenEvent event) {
        if (HUD.mc.field_71441_e == null || HUD.mc.field_71439_g == null) {
            return;
        }
        if (this.getState() && ((Boolean)this.blurValue.get()).booleanValue() && !HUD.mc.field_71460_t.func_147702_a() && event.getGuiScreen() != null && !(event.getGuiScreen() instanceof GuiChat) && !(event.getGuiScreen() instanceof GuiHudDesigner)) {
            HUD.mc.field_71460_t.func_175069_a(new ResourceLocation("LiquidBounce".toLowerCase() + "/blur.json"));
        } else if (HUD.mc.field_71460_t.func_147706_e() != null && HUD.mc.field_71460_t.func_147706_e().func_148022_b().contains("liquidbounce/blur.json")) {
            HUD.mc.field_71460_t.func_181022_b();
        }
    }
}

