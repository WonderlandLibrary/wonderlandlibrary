/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls
extends GuiScreen {
    private static final GameSettings.Options[] optionsArr = new GameSettings.Options[]{GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};
    private GuiScreen parentScreen;
    protected String screenTitle = "Controls";
    private GameSettings options;
    public KeyBinding buttonId = null;
    public long time;
    private GuiKeyBindingList keyBindingList;
    private GuiButton buttonReset;

    public GuiControls(GuiScreen screen, GameSettings settings) {
        this.parentScreen = screen;
        this.options = settings;
    }

    @Override
    public void initGui() {
        this.keyBindingList = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonReset = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0]));
        this.buttonList.add(this.buttonReset);
        this.screenTitle = I18n.format("controls.title", new Object[0]);
        int i = 0;
        GameSettings.Options[] optionsArray = optionsArr;
        int n = optionsArray.length;
        int n2 = 0;
        while (n2 < n) {
            GameSettings.Options gamesettings$options = optionsArray[n2];
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
            } else {
                this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
            }
            ++i;
            ++n2;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.keyBindingList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }
        if (button.id != 201) {
            if (button.id >= 100) return;
            if (!(button instanceof GuiOptionButton)) return;
            this.options.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
            button.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            return;
        }
        KeyBinding[] keyBindingArray = this.mc.gameSettings.keyBindings;
        int n = keyBindingArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                KeyBinding.resetKeyBindingArrayAndHash();
                return;
            }
            KeyBinding keybinding = keyBindingArray[n2];
            keybinding.setKeyCode(keybinding.getKeyCodeDefault());
            ++n2;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.buttonId != null) {
            this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
            return;
        }
        if (mouseButton == 0) {
            if (this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            if (this.keyBindingList.mouseReleased(mouseX, mouseY, state)) return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.buttonId == null) {
            super.keyTyped(typedChar, keyCode);
            return;
        }
        if (keyCode == 1) {
            this.options.setOptionKeyBinding(this.buttonId, 0);
        } else if (keyCode != 0) {
            this.options.setOptionKeyBinding(this.buttonId, keyCode);
        } else if (typedChar > '\u0000') {
            this.options.setOptionKeyBinding(this.buttonId, typedChar + 256);
        }
        this.buttonId = null;
        this.time = Minecraft.getSystemTime();
        KeyBinding.resetKeyBindingArrayAndHash();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 0xFFFFFF);
        boolean flag = true;
        for (KeyBinding keybinding : this.options.keyBindings) {
            if (keybinding.getKeyCode() == keybinding.getKeyCodeDefault()) continue;
            flag = false;
            break;
        }
        this.buttonReset.enabled = !flag;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

