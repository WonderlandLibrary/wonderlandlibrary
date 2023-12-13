package today.flux.gui.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import today.flux.config.preset.PresetManager;

import java.io.IOException;

public class SavePresetScreen extends GuiScreen {
    private final GuiScreen parent;
    private boolean saveBinds;

    private GuiTextField nameField;

    public SavePresetScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.nameField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 1) {
            this.mc.displayGuiScreen(parent);
        }

        this.nameField.setText(this.nameField.getText().replace(" ", "").replace("#", "").replace("_NONE", ""));
    }

    public void initGui() {
        this.nameField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, this.width / 2 - 100, this.height / 6 + 20, 200, 20);

        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 55 + 22 * 2, "Save binds: No"));

        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 6 + 40 + 22 * 5, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 6 + 40 + 22 * 6, "Cancel"));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 2) {
            this.saveBinds = !this.saveBinds;
        }

        if(button.id == 3){
            PresetManager.addPreset(this.nameField.getText(), this.saveBinds);
            mc.displayGuiScreen(this.parent);
        }

        if(button.id == 4){
            mc.displayGuiScreen(this.parent);
        }

        this.buttonList.get(0).displayString = "Save binds: " + (saveBinds ? "Yes" : "No");
    }


    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.nameField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Name", this.width / 2 - 89, this.height / 6 + 10, 0xFFFFFF);
        this.nameField.drawTextBox();

        this.drawCenteredString(this.fontRendererObj, "Adding Preset", this.width / 2, 30, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
