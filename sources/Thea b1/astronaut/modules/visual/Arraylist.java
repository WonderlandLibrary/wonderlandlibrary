package astronaut.modules.visual;


import astronaut.Duckware;
import astronaut.events.EventRender2D;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventManager;
import eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Comparator;

public class Arraylist extends Module {
    public Arraylist() {
        super("Arraylist", Type.Visual, Keyboard.KEY_RSHIFT, Category.VISUAL, Color.green, "Displays some client features");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @EventTarget
    public void onUpdate(EventRender2D e) {
        final ScaledResolution sr = new ScaledResolution(mc);
        int moduleCount = 0;
        final Color rectColor = new Color(0, 0, 0, 150);
        final Color lineColor = new Color(200, 140, 244, 255);

        Duckware.moduleManager.modules.sort(Comparator.comparingDouble(m -> -Duckware.arial19.getWidth(getModuleText(m))));

        for (final Module m : Duckware.moduleManager.modules) {
            if (!m.isEnabled()) continue;

            final float offset = moduleCount * (Duckware.arial19.FONT_HEIGHT);
            final float textWidth = Duckware.arial19.getWidth(getModuleText(m));
            final float rectStart = sr.getScaledWidth() - textWidth - 5;

            Gui.drawRect((int) (rectStart + 1), (int) offset, sr.getScaledWidth(), (int) (offset + 10) - 1, rectColor.getRGB());
            Gui.drawRect((int) (rectStart - 1), (int) offset, (int) (rectStart + 1), (int) (offset + 10) - 1, lineColor.getRGB());

            Duckware.arial19.drawString(getModuleText(m), (int) (rectStart + 1), (int) (offset) - 1, Color.white.getRGB());

            moduleCount++;
        }
    }

    private String getModuleText(final Module m) {
        return m.getMode().isEmpty() ? m.getName() : String.format("%s %s%s", m.getName(), EnumChatFormatting.GRAY, m.getMode());
    }

}
