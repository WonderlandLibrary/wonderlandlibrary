/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.gui.client.clickgui.ClickGui;
import net.dev.important.gui.client.clickgui.style.styles.BlackStyle;
import net.dev.important.gui.client.clickgui.style.styles.LiquidBounceStyle;
import net.dev.important.gui.client.clickgui.style.styles.NullStyle;
import net.dev.important.gui.client.clickgui.style.styles.SlowlyStyle;
import net.dev.important.gui.client.clickgui.style.styles.WhiteStyle;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@Info(name="ClickGUI", description="Opens the ClickGUI.", category=Category.RENDER, cnName="\u529f\u80fd\u7edf\u8ba1\u8868", keyBind=54)
public class ClickGUI
extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[]{"LiquidBounce", "Null", "Slowly", "Black", "White"}, "Null"){

        @Override
        protected void onChanged(String oldValue, String newValue) {
            ClickGUI.this.updateStyle();
        }
    };
    public final FloatValue scaleValue = new FloatValue("Scale", 1.0f, 0.7f, 2.0f);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);
    private static final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Sky", "Rainbow", "LiquidSlowly", "Fade", "Mixer"}, "Custom");
    private static final IntegerValue colorRedValue = new IntegerValue("Red", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("Green", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private static final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private static final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private static final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    public final ListValue backgroundValue = new ListValue("Background", new String[]{"Default", "None"}, "Default");
    public final ListValue animationValue = new ListValue("Animation", new String[]{"Azura", "Slide", "SlideBounce", "Zoom", "ZoomBounce", "None"}, "ZoomBounce");

    public static Color generateColor() {
        Color c = new Color(255, 255, 255, 255);
        switch (((String)colorModeValue.get()).toLowerCase()) {
            case "custom": {
                c = new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get());
                break;
            }
            case "rainbow": {
                c = new Color(RenderUtils.getRainbowOpaque((Integer)mixerSecondsValue.get(), ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue(), 0));
                break;
            }
            case "sky": {
                c = RenderUtils.skyRainbow(0, ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue());
                break;
            }
            case "liquidslowly": {
                c = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue());
                break;
            }
            case "fade": {
                c = ColorUtils.fade(new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get()), 0, 100);
                break;
            }
            case "mixer": {
                c = ColorMixer.getMixedColor(0, (Integer)mixerSecondsValue.get());
            }
        }
        return c;
    }

    @Override
    public void onEnable() {
        this.updateStyle();
        Client.clickGui.progress = 0.0;
        Client.clickGui.slide = 0.0;
        Client.clickGui.lastMS = System.currentTimeMillis();
        mc.func_147108_a((GuiScreen)Client.clickGui);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        this.setState(false);
    }

    private void updateStyle() {
        switch (((String)this.styleValue.get()).toLowerCase()) {
            case "liquidbounce": {
                Client.clickGui.style = new LiquidBounceStyle();
                break;
            }
            case "null": {
                Client.clickGui.style = new NullStyle();
                break;
            }
            case "slowly": {
                Client.clickGui.style = new SlowlyStyle();
                break;
            }
            case "black": {
                Client.clickGui.style = new BlackStyle();
                break;
            }
            case "white": {
                Client.clickGui.style = new WhiteStyle();
            }
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S2EPacketCloseWindow && ClickGUI.mc.field_71462_r instanceof ClickGui) {
            event.cancelEvent();
        }
    }
}

