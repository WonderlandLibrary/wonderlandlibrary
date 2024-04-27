package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="ClickGUI", description="Opens the ClickGUI.", category=ModuleCategory.RENDER, keyBind=54, canEnable=false)
public class ClickGUI
extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[]{"New", "LiquidBounce", "Null", "Slowly"}, "Null"){

        @Override
        protected void onChanged(String oldValue, String newValue) {
            ClickGUI.this.updateStyle();
        }
    };
    public final FloatValue scaleValue = new FloatValue("Scale", 1.0f, 0.7f, 2.0f);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);
    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public final ListValue backgroundValue = new ListValue("Background", new String[]{"Default", "None"}, "Default");
    public final ListValue animationValue = new ListValue("Animation", new String[]{"Azura", "Slide", "SlideBounce", "Zoom", "ZoomBounce", "None"}, "Azura");

    public static Color generateColor() {
        return (Boolean)colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        this.updateStyle();
        mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
    }

    private void updateStyle() {
        switch (((String)this.styleValue.get()).toLowerCase()) {
            case "new": {
                break;
            }
            case "liquidbounce": {
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            }
            case "null": {
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            }
            case "slowly": {
                LiquidBounce.clickGui.style = new SlowlyStyle();
            }
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (classProvider.isSPacketCloseWindow(packet) && classProvider.isClickGui(mc.getCurrentScreen())) {
            event.cancelEvent();
        }
    }
}
