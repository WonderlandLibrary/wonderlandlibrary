/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage
extends GuiScreen {
    private final LanguageManager languageManager;
    private GuiOptionButton forceUnicodeFontBtn;
    private final GameSettings game_settings_3;
    private List list;
    protected GuiScreen parentScreen;
    private GuiOptionButton confirmSettingsBtn;

    @Override
    public void initGui() {
        this.forceUnicodeFontBtn = new GuiOptionButton(100, width / 2 - 155, height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT));
        this.buttonList.add(this.forceUnicodeFontBtn);
        this.confirmSettingsBtn = new GuiOptionButton(6, width / 2 - 155 + 160, height - 38, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.confirmSettingsBtn);
        this.list = new List(this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            switch (guiButton.id) {
                case 5: {
                    break;
                }
                case 6: {
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                }
                case 100: {
                    if (!(guiButton instanceof GuiOptionButton)) break;
                    this.game_settings_3.setOptionValue(((GuiOptionButton)guiButton).returnEnumOptions(), 1);
                    guiButton.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                    ScaledResolution scaledResolution = new ScaledResolution(this.mc);
                    int n = scaledResolution.getScaledWidth();
                    int n2 = scaledResolution.getScaledHeight();
                    this.setWorldAndResolution(this.mc, n, n2);
                    break;
                }
                default: {
                    this.list.actionPerformed(guiButton);
                }
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.list.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), width / 2, 16, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", width / 2, height - 56, 0x808080);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    public GuiLanguage(GuiScreen guiScreen, GameSettings gameSettings, LanguageManager languageManager) {
        this.parentScreen = guiScreen;
        this.game_settings_3 = gameSettings;
        this.languageManager = languageManager;
    }

    class List
    extends GuiSlot {
        private final Map<String, Language> languageMap;
        private final java.util.List<String> langCodeList;

        @Override
        protected int getSize() {
            return this.langCodeList.size();
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
            Language language = this.languageMap.get(this.langCodeList.get(n));
            GuiLanguage.this.languageManager.setCurrentLanguage(language);
            ((GuiLanguage)GuiLanguage.this).game_settings_3.language = language.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || ((GuiLanguage)GuiLanguage.this).game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
            ((GuiLanguage)GuiLanguage.this).confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
            ((GuiLanguage)GuiLanguage.this).forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.game_settings_3.saveOptions();
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, this.languageMap.get(this.langCodeList.get(n)).toString(), this.width / 2, n3 + 1, 0xFFFFFF);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
        }

        @Override
        protected int getContentHeight() {
            return this.getSize() * 18;
        }

        @Override
        protected void drawBackground() {
            GuiLanguage.this.drawDefaultBackground();
        }

        @Override
        protected boolean isSelected(int n) {
            return this.langCodeList.get(n).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
        }

        public List(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 65 + 4, 18);
            this.langCodeList = Lists.newArrayList();
            this.languageMap = Maps.newHashMap();
            for (Language language : GuiLanguage.this.languageManager.getLanguages()) {
                this.languageMap.put(language.getLanguageCode(), language);
                this.langCodeList.add(language.getLanguageCode());
            }
        }
    }
}

