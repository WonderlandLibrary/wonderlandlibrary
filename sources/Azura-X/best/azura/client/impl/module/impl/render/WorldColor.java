package best.azura.client.impl.module.impl.render;

import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.color.HSBColor;
import best.azura.client.impl.value.ColorValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;

import java.awt.*;

@ModuleInfo(name = "World Color", description = "Color your world", category = Category.RENDER)
public class WorldColor extends Module {

    public static final ColorValue color = new ColorValue("Color", "Color for world", (value) -> {
        if (value != WorldColor.color) return;
        updateColor = true;
    }, new HSBColor(1.0f, 0.0f, 0.4f));

    public static Color getColor() {
        return color.getObject().getColor();
    }

    private static boolean updateColor = false;

    @Override
    public void onEnable() {
        super.onEnable();
        Tessellator.getInstance().getWorldRenderer().noColor();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Tessellator.getInstance().getWorldRenderer().noColor();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    @EventHandler
    public void handle(Event event) {
        if (event instanceof EventUpdate) {
            if (updateColor && !Mouse.isButtonDown(0)) {
                Tessellator.getInstance().getWorldRenderer().noColor();
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
                updateColor = false;
            }
        }
    }
}