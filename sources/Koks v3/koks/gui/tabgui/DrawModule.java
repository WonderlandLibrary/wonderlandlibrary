package koks.gui.tabgui;

import koks.Koks;
import koks.manager.event.impl.EventKeyPress;
import koks.manager.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 12:41
 */
public class DrawModule {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = mc.fontRendererObj;
    private int x, y, width, height, category;
    private boolean currentModule;
    public Module module;

    public DrawModule(Module module) {
        this.module = module;
    }

    public void drawScreen(int x, int y, int width, int height, boolean currentModule) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentModule = currentModule;

        fr.drawStringWithShadow(module.getName(), x + 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, module.isToggled() ? currentModule ? 0xFFFFFFFF : Koks.getKoks().clientColor.getRGB() : Color.gray.getRGB());
    }

    public void manageKeys(EventKeyPress eventKeyPress) {
        int key = eventKeyPress.getKey();

    }

}