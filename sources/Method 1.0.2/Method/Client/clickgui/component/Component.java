package Method.Client.clickgui.component;

import Method.Client.clickgui.component.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class Component {

    public void renderComponent() {
    }

    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX()
                && x < this.parent.parent.getX() + this.parent.parent.getWidth()
                && y > this.parent.parent.getY() + this.offset
                && y < this.parent.parent.getY() + this.offset + this.parent.parent.getBarHeight();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
    }

    public void RenderTooltip() {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public int getParentHeight() {
        return 0;
    }

    public void keyTyped(char typedChar, int key) {
    }

    public void setOff(int newOff) {
        offset = newOff;
    }

    public int getHeight() {
        return 0;
    }

    public void runAnimation() {
    }

    public int gety() {
        return 0;
    }

    public String getName() {
        return "";
    }

    public String getCategory() {
        return null;
    }

    public static FontRenderer FontRend = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), Minecraft.getMinecraft().renderEngine, true);

    public boolean hovered;
    public int offset;
    public Button parent;


    public enum ClickType {
        Visual,
        Slider,
        Mode,
        Keybind,
        Color,
        Checkbox,
        Gui
    }

}
