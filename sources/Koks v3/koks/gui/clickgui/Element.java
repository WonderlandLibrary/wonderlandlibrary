package koks.gui.clickgui;

import koks.api.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public abstract class Element {

    public final Minecraft mc = Minecraft.getMinecraft();
    public final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    public Setting setting;
    public int x, y, width, height;
    public boolean extended;

    public void updatePosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void keyTyped(char typedChar, int keyCode);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

}