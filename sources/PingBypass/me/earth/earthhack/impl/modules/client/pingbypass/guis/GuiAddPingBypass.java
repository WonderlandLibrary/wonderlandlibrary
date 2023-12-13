package me.earth.earthhack.impl.modules.client.pingbypass.guis;

import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Gui for configuring PingBypass Port and IP,
 * Code mostly from GuiAddServer.
 */
public class GuiAddPingBypass extends GuiScreen
{
    private final PingBypass module = PingBypass.getInstance();
    private final GuiScreen parentScreen;
    private GuiTextField serverPortField;
    private GuiTextField serverIPField;

    public GuiAddPingBypass(GuiScreen parentScreenIn)
    {
        this.parentScreen = parentScreenIn;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.serverIPField.updateCursorCounter();
        this.serverPortField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Done"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, "Cancel"));
        this.serverIPField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
        this.serverIPField.setFocused(true);
        this.serverIPField.setText(module.getIp());
        this.serverPortField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
        this.serverPortField.setMaxStringLength(128);
        this.serverPortField.setText(module.getPortAsString());
        (this.buttonList.get(0)).enabled = !this.serverPortField.getText().isEmpty() && this.serverPortField.getText().split(":").length > 0 && !this.serverIPField.getText().isEmpty();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.parentScreen.confirmClicked(false, 1337);
            }
            else if (button.id == 0)
            {
                module.setIp(this.serverIPField.getText());
                module.setPort(this.serverPortField.getText());
                this.parentScreen.confirmClicked(true, 1337);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.serverIPField.textboxKeyTyped(typedChar, keyCode);
        this.serverPortField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15)
        {
            this.serverIPField.setFocused(!this.serverIPField.isFocused());
            this.serverPortField.setFocused(!this.serverPortField.isFocused());
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }

        if (keyCode == 1)
        {
            mc.displayGuiScreen(parentScreen);
        }

        (this.buttonList.get(0)).enabled = !this.serverPortField.getText().isEmpty() && this.serverPortField.getText().split(":").length > 0 && !this.serverIPField.getText().isEmpty();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverIPField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Edit PingBypass", this.width / 2, 17, 16777215);
        this.drawString(this.fontRenderer, "Proxy-IP", this.width / 2 - 100, 53, 10526880);
        this.drawString(this.fontRenderer, "Proxy-Port", this.width / 2 - 100, 94, 10526880);
        this.serverIPField.drawTextBox();
        this.serverPortField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
