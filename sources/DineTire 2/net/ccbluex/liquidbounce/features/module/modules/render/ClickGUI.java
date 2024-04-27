/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.ClickyUI;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"Slowly"}, "Slowly") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 6, 1, 6);
    public static final IntegerValue colorRedValueModules = new IntegerValue("RedModules", 0, 0, 255);
    public static final IntegerValue colorGreenValueModules = new IntegerValue("GreenModules", 160, 0, 255);
    public static final IntegerValue colorBlueValueModules = new IntegerValue("BlueModules", 255, 0, 255);
    private static final IntegerValue colorRedValue = new IntegerValue("Red", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("Green", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        updateStyle();

        mc.displayGuiScreen(new ClickyUI());
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {

        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }
}
