package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;


public class Checkbox extends Component {
    private final Setting op;

    public Checkbox(Setting option, Button button, int offset) {
        this.op = option;
        this.parent = button;
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xA6111111);
        GlStateManager.pushMatrix();
        Button.fontSelectButton(this.op.getName(), (parent.parent.getX() + 10 + 4), (parent.parent.getY() + offset + 2), -1);
        GlStateManager.popMatrix();
        Gui.drawRect(parent.parent.getX() + 3 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 9, 0xA6999999);
        if (this.op.getValBoolean())
            Gui.drawRect(parent.parent.getX() + 4 + 4, parent.parent.getY() + offset + 4, parent.parent.getX() + 8 + 4, parent.parent.getY() + offset + 8, 0xFFf5b727);
    }

    @Override
    public String getName() {
        return this.op.getName();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0 && this.parent.open) {
            this.parent.mod.settingChanged(ClickType.Checkbox);
            this.op.setValBoolean(!op.getValBoolean());
        }
    }

}
