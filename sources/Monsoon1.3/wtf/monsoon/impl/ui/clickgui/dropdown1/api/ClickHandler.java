package wtf.monsoon.impl.ui.clickgui.dropdown1.api;



import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;
import net.minecraft.client.Minecraft;

public class ClickHandler
{
    private int width;
    private int height;
    private CGUI cgui;

    public final ClickHandler withWidth(int width)
    {
        this.width = width;
        return this;
    }

    public final ClickHandler withHeight(int height)
    {
        this.height = height;
        return this;
    }

    public final void display()
    {
        if (cgui == null) cgui = new CGUI(width, height);
        Minecraft.getMinecraft().displayGuiScreen(cgui);
    }

    public final void hide()
    {
        if (Minecraft.getMinecraft().currentScreen instanceof CGUI) Minecraft.getMinecraft().displayGuiScreen(null);
    }

    public void onOpen() { }
    public void onClose() { }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) { }
    public void drawCategory(int x, int y, int w, int h, int mouseX, int mouseY, Category category) { }
    public void drawModule(int x, int y, int w, int h, int mouseX, int mouseY, Module module) { }
    public void drawSetting(int x, int y, int w, int h, int mouseX, int mouseY, Setting setting) { }
    public void clickCategory(int mouseX, int mouseY, int mouseButton, Category category) { }
    public void clickModule(int mouseX, int mouseY, int mouseButton, Module module) { }
    public void clickSetting(int mouseX, int mouseY, int mouseButton, Setting setting) { }
    public void mouseReleased(int mouseX, int mouseY, int state) { }
    public void keyTyped(char typedChar, int keyCode) { }
    public boolean doesPauseGame() { return false; }

    public final int getWidth()
    {
        return width;
    }
    public final int getHeight()
    {
        return height;
    }

    public final CGUI getCgui()
    {
        return cgui;
    }
}
