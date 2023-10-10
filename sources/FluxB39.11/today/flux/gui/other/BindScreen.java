package today.flux.gui.other;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import today.flux.module.Module;

import java.io.IOException;

public class BindScreen extends GuiScreen {
    private final Module target;
    private final GuiScreen parent;

    public BindScreen(Module module, GuiScreen parent) {
        this.target = module;
        this.parent = parent;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == 1) {
            this.mc.displayGuiScreen(parent);
        }

        if (keyCode != 1 && keyCode != Keyboard.KEY_DELETE) {
            this.target.setBind(keyCode);
            this.mc.displayGuiScreen(parent);
        }

        if (keyCode == Keyboard.KEY_DELETE) {
            this.target.setBind(Keyboard.KEY_NONE);
            this.mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Press any key to bind " + EnumChatFormatting.YELLOW + target.getName(), this.width / 2, 150, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Press Delete key to remove the bind.", this.width / 2, 170, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
