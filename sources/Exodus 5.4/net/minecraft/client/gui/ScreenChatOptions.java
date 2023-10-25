/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions
extends GuiScreen {
    private final GuiScreen parentScreen;
    private final GameSettings game_settings;
    private String field_146401_i;
    private static final GameSettings.Options[] field_146399_a = new GameSettings.Options[]{GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO};

    @Override
    public void initGui() {
        int n = 0;
        this.field_146401_i = I18n.format("options.chat.title", new Object[0]);
        GameSettings.Options[] optionsArray = field_146399_a;
        int n2 = field_146399_a.length;
        int n3 = 0;
        while (n3 < n2) {
            GameSettings.Options options = optionsArray[n3];
            if (options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), options));
            } else {
                this.buttonList.add(new GuiOptionButton(options.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), options, this.game_settings.getKeyBinding(options)));
            }
            ++n;
            ++n3;
        }
        this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 120, I18n.format("gui.done", new Object[0])));
    }

    public ScreenChatOptions(GuiScreen guiScreen, GameSettings gameSettings) {
        this.parentScreen = guiScreen;
        this.game_settings = gameSettings;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id < 100 && guiButton instanceof GuiOptionButton) {
                this.game_settings.setOptionValue(((GuiOptionButton)guiButton).returnEnumOptions(), 1);
                guiButton.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(guiButton.id));
            }
            if (guiButton.id == 200) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146401_i, width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }
}

