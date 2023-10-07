package best.azura.client.impl.module.impl.render;

import best.azura.client.impl.Client;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.impl.events.EventFastTick;
import best.azura.client.impl.ui.customhud.GuiEditHUD;
import best.azura.client.impl.events.EventRender2D;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.*;
import best.azura.client.impl.value.dependency.ModeDependency;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

@SuppressWarnings({"unused"})
@ModuleInfo(name = "HUD", description = "Draws helpful information to the screen.", category = Category.RENDER)
public class HUD extends Module {

    //Category for the array
    private final CategoryValue array = new CategoryValue("Array List");
    public final NumberValue<Double> colorStep = new NumberValue<>("Color step",
            "Change the step of the Color when using Fade as the color mode", 150., 1., 10., 150.);

    //Speed of the color
    public final NumberValue<Float> colorSpeed = new NumberValue<>("Color speed",
            "Change the step of the Color when using Fade or Pulsate", 2.0F, 1F, 1.0F, 10F);

    //Color mode
    public final ModeValue colorValue = new ModeValue("Color mode", "Change the HUD Color",
            "Static", "Static", "Gradient", "Rainbow");

    //Array list color
    public final ColorValue arrayListColor = new ColorValue("Array list color",
            "Change the color of the Array List", HSBColor.fromColor(new Color(0xFFFFFF)));

    public final NumberValue<Integer> alphaValue = new NumberValue<Integer>("Text Alpha In", "Alpha of text", () -> arrayListColor.collapsed, 255, 0, 255);

    //Array list end color
    public final ColorValue arrayListColorEnd = new ColorValue("Array list color end",
            "Change the color of the Array List", new ModeDependency(colorValue, "Gradient"),
            HSBColor.fromColor(new Color(0xFFFFFF)));

    public final NumberValue<Integer> alphaValueOut = new NumberValue<Integer>("Text Alpha Out", "Alpha of text", () -> arrayListColorEnd.collapsed, 255, 0, 255);

    public final ColorValue suffixColorValue = new ColorValue("Suffix Color",
            "Change the color of the Suffix",
            HSBColor.fromColor(new Color(0xAAAAAA)));

    public final NumberValue<Integer> suffixAlpha = new NumberValue<Integer>("Suffix alpha",
            "Alpha of suffix", () -> suffixColorValue.collapsed && suffixColorValue.checkDependency(), 255, 0, 255);


    public final BooleanValue useClientFont = new BooleanValue("C-Font", "Use client font", true);

    @EventHandler
    public final Listener<EventRender2D> render2DListener = event -> {
        if (mc.currentScreen instanceof GuiEditHUD) {
            return;
        }
        RenderUtil.INSTANCE.scaleFix(1.0);
        for (Element e : Client.INSTANCE.getElementManager().getElements()) {
            if (!e.isEnabled()) continue;
            GlStateManager.scale(e.getScale(), e.getScale(), 1);
            e.render();
            GlStateManager.scale(1. / e.getScale(), 1. / e.getScale(), 1);
        }
        GlStateManager.resetColor();
        RenderUtil.INSTANCE.invertScaleFix(1.0);
    };

    @EventHandler
    public final Listener<EventFastTick> fastTickListener = event -> {
        for (Element e : Client.INSTANCE.getElementManager().getElements()) {
            if (!e.isEnabled()) continue;
            e.onFastTick();
        }
    };
}
