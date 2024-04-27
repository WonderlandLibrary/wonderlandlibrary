/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.AstolfoStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.client.newdropdown.DropdownClickGui;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="ClickGUI", description="Opens the ClickGUI.", category=ModuleCategory.RENDER, keyBind=54, canEnable=false)
public class ClickGUI
extends Module {
    private static final BoolValue colorRainbow;
    public final FloatValue scaleValue;
    private static final IntegerValue colorRedValue;
    public static final IntegerValue clickHeight;
    private static final IntegerValue colorGreenValue;
    private static final IntegerValue colorBlueValue;
    private final ListValue clickguimodeValue;
    private final ListValue styleValue = new ListValue(this, "Style", new String[]{"LiquidBounce", "Null", "Slowly", "Astolfo"}, "Slowly"){
        final ClickGUI this$0;

        @Override
        protected void onChanged(Object object, Object object2) {
            this.onChanged((String)object, (String)object2);
        }
        {
            this.this$0 = clickGUI;
            super(string, stringArray, string2);
        }

        protected void onChanged(String string, String string2) {
            ClickGUI.access$000(this.this$0);
        }
    };
    public static final ListValue scrollMode;
    public final IntegerValue maxElementsValue;
    public static final BoolValue backback;
    public static final ListValue colormode;

    @EventTarget(ignoreCondition=true)
    public void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (classProvider.isSPacketCloseWindow(iPacket) && classProvider.isClickGui(mc.getCurrentScreen())) {
            packetEvent.cancelEvent();
        }
    }

    @Override
    public void onEnable() {
        if (((String)this.clickguimodeValue.get()).equalsIgnoreCase("LiquidBounce")) {
            this.updateStyle();
            mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
        }
        if (((String)this.clickguimodeValue.get()).equalsIgnoreCase("Tenacity")) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new DropdownClickGui()));
        }
    }

    static {
        colormode = new ListValue("Setting Accent", new String[]{"White", "Color"}, "Color");
        scrollMode = new ListValue("Scroll Mode", new String[]{"Screen Height", "Value"}, "Value");
        clickHeight = new IntegerValue("Tab Height", 250, 100, 500);
        colorRedValue = new IntegerValue("R", 0, 0, 255);
        colorGreenValue = new IntegerValue("G", 160, 0, 255);
        colorBlueValue = new IntegerValue("B", 255, 0, 255);
        colorRainbow = new BoolValue("Rainbow", false);
        backback = new BoolValue("Background Accent", true);
    }

    public static Color generateColor() {
        return (Boolean)colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get());
    }

    private void updateStyle() {
        switch (((String)this.styleValue.get()).toLowerCase()) {
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
                break;
            }
            case "astolfo": {
                LiquidBounce.clickGui.style = new AstolfoStyle();
            }
        }
    }

    public ClickGUI() {
        this.clickguimodeValue = new ListValue("Mode", new String[]{"LiquidBounce", "Tenacity", "Temple"}, "Tenacity");
        this.scaleValue = new FloatValue("Scale", 1.0f, 0.7f, 2.0f);
        this.maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);
    }

    public static int generateRGB() {
        return (Boolean)colorRainbow.get() != false ? ColorUtils.rainbow().getRGB() : new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get()).getRGB();
    }

    static void access$000(ClickGUI clickGUI) {
        clickGUI.updateStyle();
    }
}

