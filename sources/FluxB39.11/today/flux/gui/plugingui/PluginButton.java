package today.flux.gui.plugingui;

import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PluginButton {
    public int buttonID;

    public float x;
    public float y;
    public float width;
    public float height;
    public String displayString;
    public Consumer<Integer> action;
    public Supplier<Boolean> isEnabled;

    public boolean isHovered = false;

    public PluginButton(int buttonID, String displayStr, float x, float y, float width, float height, Consumer<Integer> action, Supplier<Boolean> isEnabled) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = displayStr;
        this.action = action;
        this.isEnabled = isEnabled;
    }

    public void drawButton(int mouseX, int mouseY) {
        isHovered = RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + height);
        boolean enabled = this.isEnabled.get();
        int buttonColor = !enabled ? GuiRenderUtils.darker(0xff058669, 45) : isHovered ? GuiRenderUtils.darker(0xff058669, 30) : 0xff058669;
        GuiRenderUtils.drawRoundedRect(x, y, width, height, 2f, buttonColor, .5f, buttonColor);
        FontManager.sans18.drawString(displayString, x + width / 2f - (FontManager.sans18.getStringWidth(displayString) / 2f), y + height / 2 - 7f, !enabled ? 0xffa19c9c : 0xffffffff);
    }

    public void onClick(int mouseButton) {
        if(isHovered && this.action != null && this.isEnabled.get()) {
            this.action.accept(mouseButton);
        }
    }

}
