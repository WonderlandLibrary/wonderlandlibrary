package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage
extends GuiScreen {
    protected GuiScreen parentScreen;
    private List list;
    private final GameSettings game_settings_3;
    private final LanguageManager languageManager;
    private GuiOptionButton forceUnicodeFontBtn;
    private GuiOptionButton confirmSettingsBtn;

    public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager) {
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    public void initGui() {
        this.forceUnicodeFontBtn = new GuiOptionButton(100, width / 2 - 155, height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT));
        this.buttonList.add((Object)this.forceUnicodeFontBtn);
        this.confirmSettingsBtn = new GuiOptionButton(6, width / 2 - 155 + 160, height - 38, I18n.format((String)"gui.done", (Object[])new Object[0]));
        this.buttonList.add((Object)this.confirmSettingsBtn);
        this.list = new List(this, this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            switch (button.id) {
                case 5: {
                    break;
                }
                case 6: {
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                }
                case 100: {
                    if (!(button instanceof GuiOptionButton)) break;
                    this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                    button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                    ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                    int i = scaledresolution.getScaledWidth();
                    int j = scaledresolution.getScaledHeight();
                    this.setWorldAndResolution(this.mc, i, j);
                    break;
                }
                default: {
                    this.list.actionPerformed(button);
                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"options.language", (Object[])new Object[0]), width / 2, 16, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format((String)"options.languageWarning", (Object[])new Object[0]) + ")", width / 2, height - 56, 0x808080);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    static /* synthetic */ LanguageManager access$000(GuiLanguage x0) {
        return x0.languageManager;
    }

    static /* synthetic */ GameSettings access$100(GuiLanguage x0) {
        return x0.game_settings_3;
    }

    static /* synthetic */ GuiOptionButton access$200(GuiLanguage x0) {
        return x0.confirmSettingsBtn;
    }

    static /* synthetic */ GuiOptionButton access$300(GuiLanguage x0) {
        return x0.forceUnicodeFontBtn;
    }
}
