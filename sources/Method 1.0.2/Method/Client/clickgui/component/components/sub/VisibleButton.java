package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.module.Module;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;


public class VisibleButton extends Component {
    private final Module mod;

    public VisibleButton(Button button, Module mod, int offset) {
        this.parent = button;
        this.mod = mod;
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xA6111111);
        GlStateManager.pushMatrix();
        Button.fontSelectButton("Visible: " + mod.visible, (parent.parent.getX() + 7), (parent.parent.getY() + offset + 2), -1);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0 && this.parent.open) {
            this.parent.mod.settingChanged(ClickType.Visual);
            mod.visible = (!mod.visible);
        }
    }

}
