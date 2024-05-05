/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.DropdownGUI;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.NewUi;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.AstolfoStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.GwangStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.WhiteStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.ClickyUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ColorValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@ModuleInfo(name="ClickGUI", description="Opens the ClickGUI.", category=ModuleCategory.RENDER, keyBind=54, canEnable=false)
public class ClickGUI
extends Module {
    public ModuleCategory moduleCategory = ModuleCategory.COMBAT;
    public float animationHeight = 0.0f;
    public String configName = "Basic";
    private final ListValue styleValue = new ListValue("Style", new String[]{"Null", "Slowly", "White", "Astolfo", "Gwang"}, "Slowly"){

        @Override
        protected void onChanged(String oldValue, String newValue) {
            ClickGUI.this.updateStyle();
        }
    };
    public static final ListValue modeValue = new ListValue("Mode", new String[]{"Style", "Novoline", "OneTapBeta", "FluxParodyBeta", "Novoline2", "NewGui"}, "OneTapBeta");
    public static final BoolValue fastRenderValue = new BoolValue("FastRender-NewGui", false);
    public static final ColorValue colorValue = new ColorValue("Color", -8350465);
    public static final FloatValue scaleValue = new FloatValue("Scale", 1.0f, 0.7f, 2.0f);
    public final IntegerValue maxElementsValue = new IntegerValue("Max-Elements", 15, 1, 20);
    public static final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    public static final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    public static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public final BoolValue disp = new BoolValue("DisplayValue", false);
    public final BoolValue getClosePrevious = new BoolValue("ClosePrevious", true);

    public static Color generateColor() {
        return (Boolean)colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        String s;
        switch (s = (String)modeValue.get()) {
            case "Style": {
                this.updateStyle();
                mc.func_147108_a((GuiScreen)LiquidBounce.clickGui);
                break;
            }
            case "Novoline": {
                mc.func_147108_a((GuiScreen)new ClickyUI());
                break;
            }
            case "OneTapBeta": {
                mc.func_147108_a((GuiScreen)new OtcClickGUi());
                break;
            }
            case "FluxParodyBeta": {
                mc.func_147108_a((GuiScreen)new net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody.ClickGui());
                break;
            }
            case "Novoline2": {
                mc.func_147108_a((GuiScreen)new DropdownGUI());
                break;
            }
            case "NewGui": {
                this.updateStyle();
                mc.func_147108_a((GuiScreen)NewUi.getInstance());
            }
        }
    }

    private void updateStyle() {
        switch (((String)this.styleValue.get()).toLowerCase()) {
            case "null": {
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            }
            case "slowly": {
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
            }
            case "white": {
                LiquidBounce.clickGui.style = new WhiteStyle();
                break;
            }
            case "gwang": {
                LiquidBounce.clickGui.style = new GwangStyle();
                break;
            }
            case "astolfo": {
                LiquidBounce.clickGui.style = new AstolfoStyle();
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

