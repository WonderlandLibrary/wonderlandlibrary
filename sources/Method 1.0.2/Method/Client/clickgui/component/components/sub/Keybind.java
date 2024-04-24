package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class Keybind extends Component {

    private boolean binding;
    public static boolean PublicBinding;
    private boolean LControl = false;
    private boolean LShift = false;
    private boolean LAlt = false;

    public Keybind(Button button, int offset) {
        this.parent = button;
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xA6111111);
        GlStateManager.pushMatrix();
        StringBuilder Keys = new StringBuilder();
        for (Integer key : this.parent.mod.getKeys()) {
            Keys.append(" ").append(Keyboard.getKeyName(key));
        }
        Button.fontSelectButton((this.binding) ? "Press a key..." : ("Key: " + Keys),
                (parent.parent.getX() + 7), (parent.parent.getY() + offset + 2), -1);
        GlStateManager.popMatrix();
    }

    @Override
    public void RenderTooltip() {
        if (this.hovered && this.parent.open) {
            Button.fontSelect("Press End To Clear", 0, (float) (Minecraft.getMinecraft().displayHeight / 2.085), -1);
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0 && this.parent.open) {
            this.parent.mod.settingChanged(ClickType.Keybind);
            this.binding = !this.binding;
            PublicBinding = !PublicBinding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.binding) {
            if (key == Keyboard.KEY_LCONTROL) {
                LControl = !LControl;
                return;
            }
            if (key == Keyboard.KEY_LSHIFT) {
                LShift = !LShift;
                return;
            }
            if (key == Keyboard.KEY_LMENU) {
                LAlt = !LAlt;
                return;
            }
            this.parent.mod.setKey(key, LControl, LShift, LAlt);
            this.binding = false;
            this.LAlt = false;
            this.LControl = false;
            this.LShift = false;
            PublicBinding = false;
            if (key == Keyboard.KEY_END) {
                this.parent.mod.setKey(key, false, false, false);
            }
        }
    }

}
