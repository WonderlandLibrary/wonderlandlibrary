package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class ModeButton extends Component {
    private final Setting set;
    private int modeIndex;

    public ModeButton(Setting set, Button button, int offset) {
        this.set = set;
        this.parent = button;
        this.offset = offset;
        this.modeIndex = 0;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xA6111111);
        GlStateManager.pushMatrix();
        Button.fontSelectButton(this.set.getName() + ": " + this.set.getValString(), (float) ((parent.parent.getX() + 7)), (float) ((parent.parent.getY() + offset + 3)), -1);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0 && this.parent.open) {
            modeIndex = (modeIndex + 1 >= set.getOptions().size()) ? 0 : modeIndex + 1;
            set.setValString(set.getOptions().get(modeIndex));
            this.parent.mod.settingChanged(ClickType.Mode);
        }
        if (this.hovered && button == 1 && this.parent.open) {
            modeIndex = (modeIndex - 1 < 0) ? set.getOptions().size() - 1 : modeIndex - 1;
            set.setValString(set.getOptions().get(modeIndex));
            this.parent.mod.settingChanged(ClickType.Mode);
        }
    }
    @Override
    public String getName() {
        return this.set.getName();
    }

}
