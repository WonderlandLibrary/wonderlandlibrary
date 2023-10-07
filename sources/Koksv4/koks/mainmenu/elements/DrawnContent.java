package koks.mainmenu.elements;

import koks.mainmenu.builder.WindowBuilder;
import net.minecraft.client.Minecraft;

public abstract class DrawnContent {
    public WindowBuilder.Window window;
    final Minecraft mc = Minecraft.getMinecraft();

    public abstract void draw(int mouseX, int mouseY, int x, int y, int width, int height);
    public void click(int mouseX, int mouseY, int button) {}
    public void input(char typedChar, int keyCode) {}
    public void init() {}
}
