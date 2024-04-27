/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER,array = false)
@SideOnly(Side.CLIENT)
public class HUD extends Module {

    public final ListValue bkvalue = new ListValue("BackGroundColor", new String[] {"None","Color"}, "None");
    public final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    public final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    public final BoolValue no = new BoolValue("Notification", false);
    public final BoolValue fontChatValue = new BoolValue("FontChat", false);
    public final IntegerValue rd = new IntegerValue("Red", 0, 0, 255);
    public final IntegerValue bl = new IntegerValue("Blue", 255, 0, 255);
    public final IntegerValue gn = new IntegerValue("Green", 0, 0, 255);
    public final IntegerValue ap = new IntegerValue("Alpha", 150, 0, 255);

    public HUD() {
        setState(true);
        setArray(false);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (mc.currentScreen instanceof GuiHudDesigner)
            return;

        LiquidBounce.hud.render(false);
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        LiquidBounce.hud.update();
    }

    @EventTarget
    public void Render2DEvent(final UpdateEvent event) {
        LiquidBounce.hud.Render2D();
    }

    @EventTarget
    public void onKey(final KeyEvent event) {
        LiquidBounce.hud.handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition = true)
    public void onScreen(final ScreenEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        if (getState() && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.getGuiScreen() != null &&
                !(event.getGuiScreen() instanceof GuiChat || event.getGuiScreen() instanceof GuiHudDesigner))
            mc.entityRenderer.loadShader(new ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/blur.json"));
        else if (mc.entityRenderer.getShaderGroup() != null &&
                mc.entityRenderer.getShaderGroup().getShaderGroupName().contains("liquidbounce/blur.json"))
            mc.entityRenderer.stopUseShader();
    }
}
