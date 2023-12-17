// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    protected GuiScreen parentScreen;
    private List list;
    private final GameSettings game_settings_3;
    private final LanguageManager languageManager;
    private GuiOptionButton forceUnicodeFontBtn;
    private GuiOptionButton confirmSettingsBtn;
    
    public GuiLanguage(final GuiScreen screen, final GameSettings gameSettingsObj, final LanguageManager manager) {
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }
    
    @Override
    public void initGui() {
        this.forceUnicodeFontBtn = this.addButton(new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.confirmSettingsBtn = this.addButton(new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        (this.list = new List(GuiLanguage.mc)).registerScrollButtons(7, 8);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            switch (button.id) {
                case 5: {
                    break;
                }
                case 6: {
                    GuiLanguage.mc.displayGuiScreen(this.parentScreen);
                    break;
                }
                case 100: {
                    if (button instanceof GuiOptionButton) {
                        this.game_settings_3.setOptionValue(((GuiOptionButton)button).getOption(), 1);
                        button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        final ScaledResolution scaledresolution = new ScaledResolution(GuiLanguage.mc);
                        final int i = scaledresolution.getScaledWidth();
                        final int j = scaledresolution.getScaledHeight();
                        this.setWorldAndResolution(GuiLanguage.mc, i, j);
                        break;
                    }
                    break;
                }
                default: {
                    this.list.actionPerformed(button);
                    break;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRenderer, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class List extends GuiSlot
    {
        private final java.util.List<String> langCodeList;
        private final Map<String, Language> languageMap;
        
        public List(final Minecraft mcIn) {
            super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
            this.langCodeList = (java.util.List<String>)Lists.newArrayList();
            this.languageMap = (Map<String, Language>)Maps.newHashMap();
            for (final Language language : GuiLanguage.this.languageManager.getLanguages()) {
                this.languageMap.put(language.getLanguageCode(), language);
                this.langCodeList.add(language.getLanguageCode());
            }
        }
        
        @Override
        protected int getSize() {
            return this.langCodeList.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            final Language language = this.languageMap.get(this.langCodeList.get(slotIndex));
            GuiLanguage.this.languageManager.setCurrentLanguage(language);
            GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRenderer.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRenderer.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
            GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
            GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.game_settings_3.saveOptions();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return this.langCodeList.get(slotIndex).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
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
        protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
            GuiLanguage.this.fontRenderer.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRenderer, this.languageMap.get(this.langCodeList.get(slotIndex)).toString(), this.width / 2, yPos + 1, 16777215);
            GuiLanguage.this.fontRenderer.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}
