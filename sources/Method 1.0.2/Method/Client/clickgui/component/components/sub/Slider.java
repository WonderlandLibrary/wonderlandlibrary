package Method.Client.clickgui.component.components.sub;


import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.lwjgl.input.Keyboard.KEY_LCONTROL;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;


public class Slider extends Component {
    protected Minecraft mc = Minecraft.getMinecraft();
    private final Setting set;
    private boolean dragging = false;
    private GuiTextField Inputbox;
    public double renderWidth;

    public Slider(Setting value, Button button, int offset) {
        this.set = value;
        this.parent = button;
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12, this.hovered ? 0xBF555555 : 0xBF444444);
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, (set.getValDouble() > set.getMax() || set.getValDouble() < set.getMin()) ? 0xA6911111 : 0xA6919191);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1f, 1f, 0.5f);
        Button.fontSelectButton(this.set.getName() + ": " + this.set.getValDouble(), (float) ((parent.parent.getX() + 8)), (float) ((parent.parent.getY() + offset + 2)), -1);
        GlStateManager.popMatrix();
        if (this.hovered)
            Button.fontSelectButton(this.set.getTooltip(), 0, (float) (mc.displayHeight / 2.085), 0xA6999999);
        if (Inputbox != null) {
            Inputbox.x = parent.parent.getX() + 2;
            Inputbox.y = parent.parent.getY() + 2 + offset;
            Inputbox.drawTextBox();
        }
    }

    @Override
    public void RenderTooltip() {
        if (this.hovered && this.parent.open) {
            Button.fontSelect("Press Ctrl Click For Exact Input.", 0, (float) (mc.displayHeight / 2.085), -1);
        }
    }

    @Override
    public String getName() {
        return this.set.getName();
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        if (Inputbox != null) Inputbox.updateCursorCounter();

        double diff = Math.min(88, Math.max(0, mouseX - this.parent.parent.getX()));

        double min = set.getMin();
        double max = set.getMax();

        renderWidth = (88) * (set.getValDouble() - min) / (max - min);
        if (set.getValDouble() > max || set.getValDouble() < min) {
            renderWidth = .1;
        }
        if (dragging && (mc.currentScreen instanceof ClickGui || mc.currentScreen instanceof OnscreenGUI)) {
            if (diff == 0) {
                set.setValDouble(set.getMin());
            } else {
                double newValue = roundToPlace(((diff / 88) * (max - min) + min));
                set.setValDouble(newValue);
            }
        }
        if (dragging && !(mc.currentScreen instanceof ClickGui || mc.currentScreen instanceof OnscreenGUI))
            dragging = false;
        super.updateComponent(mouseX, mouseY);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (Inputbox != null) {
            this.Inputbox.setFocused(false);
        }
        if (this.hovered) {
            if (button == 0 && this.parent.open) {
                if (Inputbox == null) {
                    this.dragging = true;
                } else Inputbox.setFocused(true);
            }
            if (Keyboard.isKeyDown(KEY_LCONTROL) && Inputbox == null) {
                Inputbox = new GuiTextField(0, FontRend, this.parent.parent.getX() + 5, this.parent.parent.getY(), 64, 10);
                Inputbox.setFocused(true);
                Inputbox.writeText(String.valueOf(set.getValDouble()));
                this.dragging = false;
            }
        }


    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (Inputbox != null) {
            Inputbox.textboxKeyTyped(typedChar, keyCode);
            if (Keyboard.getEventKey() == 28) {
                try {
                    set.setValDouble(Double.parseDouble(Inputbox.getText()));
                    Inputbox = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (dragging)
            this.parent.mod.settingChanged(ClickType.Slider);
        dragging = false;
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
