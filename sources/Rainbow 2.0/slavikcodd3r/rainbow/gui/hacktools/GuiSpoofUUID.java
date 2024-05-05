package slavikcodd3r.rainbow.gui.hacktools;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiTextField;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.gui.hacktools.utils.HackPack;
import net.minecraft.client.gui.GuiScreen;

public class GuiSpoofUUID extends GuiScreen
{
    private GuiScreen parentScreen;
    private GuiTextField usernameTextField;
    private GuiTextField usernameTextField2;
    private String error;
    
    public GuiSpoofUUID(final GuiScreen parentScreen2) {
        this.parentScreen = parentScreen2;
    }
    
    @Override
    public void updateScreen() {
        this.usernameTextField.updateCursorCounter();
        this.usernameTextField2.updateCursorCounter();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (guibutton.id == 0) {
            Rainbow.getHackPack();
            HackPack.setFakeUUID(this.usernameTextField2.getText());
            Rainbow.getHackPack();
            HackPack.setFakeIP(this.usernameTextField.getText());
        }
        this.mc.displayGuiScreen(this.parentScreen);
    }
    
    @Override
    protected void keyTyped(final char c, final int i) {
        this.usernameTextField.textboxKeyTyped(c, i);
        this.usernameTextField2.textboxKeyTyped(c, i);
        if (c == '\t' && this.usernameTextField.isFocused()) {
            this.usernameTextField.setFocused(false);
        }
        if (c == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        this.usernameTextField.mouseClicked(i, j, k);
        this.usernameTextField2.mouseClicked(i, j, k);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Done"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.usernameTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
        this.usernameTextField2 = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, 96, 200, 20);
        this.usernameTextField.setMaxStringLength(500);
        this.usernameTextField2.setMaxStringLength(500);
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        this.usernameTextField.drawTextBox();
        this.drawCenteredString(this.fontRendererObj, "\u2191 UUID, \u2193 IP.", this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.usernameTextField2.drawTextBox();
        super.drawScreen(i, j, f);
    }
}
