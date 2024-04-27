package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.*;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.CLIENT, array = false)
@SideOnly(Side.CLIENT)
public class HUD extends Module {

    public final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    public final IntegerValue hotbarSpeed = new IntegerValue("HotbarSpeed",95,0,100);
    public final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    public final BoolValue moreinventory = new BoolValue("MoreInventory",false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    public final FontValue chatFont = new FontValue("ChatFont", mc.fontRendererObj) {
        @Override
        protected void onChanged(FontRenderer oldValue, FontRenderer newValue) {
            mc.ingameGUI.getChatGUI().refreshChat();
        }
    };
    public static final ListValue fontShadow = new ListValue("FontShadow", new String[] {
            "LiquidBounce",
            "Outline",
            "Default",
            "Autumn"
    }, "Default");
    public static final IntegerValue fontWidth = new IntegerValue("FontWidth", 8, 5, 10);
    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    public HUD() {
        setState(true);
        setArray(false);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (mc.currentScreen instanceof GuiHudDesigner) return;

        LiquidSense.hud.render(false);
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        LiquidSense.hud.update();
    }

    @EventTarget
    public void onLivingUpdate(final LivingUpdateEvent event){
        LiquidSense.hud.livingupdate();
    }

    @EventTarget
    public void onKey(final KeyEvent event) {
        LiquidSense.hud.handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition = true)
    public void onScreen(final ScreenEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        if (getState() && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.getGuiScreen() != null &&
                !(event.getGuiScreen() instanceof GuiChat || event.getGuiScreen() instanceof GuiHudDesigner))
            mc.entityRenderer.loadShader(new ResourceLocation(LiquidSense.CLIENT_NAME.toLowerCase()+"/blur.json"));
        else if (mc.entityRenderer.getShaderGroup() != null &&
                mc.entityRenderer.getShaderGroup().getShaderGroupName().contains(LiquidSense.CLIENT_NAME.toLowerCase()+"/blur.json"))
            mc.entityRenderer.stopUseShader();
    }
}
