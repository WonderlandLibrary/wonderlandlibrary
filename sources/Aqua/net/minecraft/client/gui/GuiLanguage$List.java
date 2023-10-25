package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.settings.GameSettings;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiLanguage.List
extends GuiSlot {
    private final List<String> langCodeList;
    private final Map<String, Language> languageMap;

    public GuiLanguage.List(Minecraft mcIn) {
        super(mcIn, GuiLanguage.width, GuiLanguage.height, 32, GuiLanguage.height - 65 + 4, 18);
        this.langCodeList = Lists.newArrayList();
        this.languageMap = Maps.newHashMap();
        for (Language language : GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).getLanguages()) {
            this.languageMap.put((Object)language.getLanguageCode(), (Object)language);
            this.langCodeList.add((Object)language.getLanguageCode());
        }
    }

    protected int getSize() {
        return this.langCodeList.size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        Language language = (Language)this.languageMap.get(this.langCodeList.get(slotIndex));
        GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).setCurrentLanguage(language);
        GuiLanguage.access$100((GuiLanguage)GuiLanguage.this).language = language.getLanguageCode();
        this.mc.refreshResources();
        GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).isCurrentLocaleUnicode() || GuiLanguage.access$100((GuiLanguage)GuiLanguage.this).forceUnicodeFont);
        GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).isCurrentLanguageBidirectional());
        GuiLanguage.access$200((GuiLanguage)GuiLanguage.this).displayString = I18n.format((String)"gui.done", (Object[])new Object[0]);
        GuiLanguage.access$300((GuiLanguage)GuiLanguage.this).displayString = GuiLanguage.access$100((GuiLanguage)GuiLanguage.this).getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
        GuiLanguage.access$100((GuiLanguage)GuiLanguage.this).saveOptions();
    }

    protected boolean isSelected(int slotIndex) {
        return ((String)this.langCodeList.get(slotIndex)).equals((Object)GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).getCurrentLanguage().getLanguageCode());
    }

    protected int getContentHeight() {
        return this.getSize() * 18;
    }

    protected void drawBackground() {
        GuiLanguage.this.drawDefaultBackground();
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        GuiLanguage.this.fontRendererObj.setBidiFlag(true);
        GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(entryID))).toString(), this.width / 2, p_180791_3_ + 1, 0xFFFFFF);
        GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.access$000((GuiLanguage)GuiLanguage.this).getCurrentLanguage().isBidirectional());
    }
}
