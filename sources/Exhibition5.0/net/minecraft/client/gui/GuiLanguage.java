// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.resources.Language;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    protected GuiScreen field_146453_a;
    private List field_146450_f;
    private final GameSettings game_settings_3;
    private final LanguageManager field_146454_h;
    private GuiOptionButton field_146455_i;
    private GuiOptionButton field_146452_r;
    private static final String __OBFID = "CL_00000698";
    
    public GuiLanguage(final GuiScreen p_i1043_1_, final GameSettings p_i1043_2_, final LanguageManager p_i1043_3_) {
        this.field_146453_a = p_i1043_1_;
        this.game_settings_3 = p_i1043_2_;
        this.field_146454_h = p_i1043_3_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(this.field_146455_i = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.field_146452_r = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        (this.field_146450_f = new List(this.mc)).registerScrollButtons(7, 8);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146450_f.handleMouseScrolling();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            switch (button.id) {
                case 5: {
                    break;
                }
                case 6: {
                    this.mc.displayGuiScreen(this.field_146453_a);
                    break;
                }
                case 100: {
                    if (button instanceof GuiOptionButton) {
                        this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                        button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        final ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                        final int var3 = var2.getScaledWidth();
                        final int var4 = var2.getScaledHeight();
                        this.setWorldAndResolution(this.mc, var3, var4);
                        break;
                    }
                    break;
                }
                default: {
                    this.field_146450_f.actionPerformed(button);
                    break;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.field_146450_f.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class List extends GuiSlot
    {
        private final java.util.List field_148176_l;
        private final Map field_148177_m;
        private static final String __OBFID = "CL_00000699";
        
        public List(final Minecraft mcIn) {
            super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
            this.field_148176_l = Lists.newArrayList();
            this.field_148177_m = Maps.newHashMap();
            for (final Language var4 : GuiLanguage.this.field_146454_h.getLanguages()) {
                this.field_148177_m.put(var4.getLanguageCode(), var4);
                this.field_148176_l.add(var4.getLanguageCode());
            }
        }
        
        @Override
        protected int getSize() {
            return this.field_148176_l.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            final Language var5 = this.field_148177_m.get(this.field_148176_l.get(slotIndex));
            GuiLanguage.this.field_146454_h.setCurrentLanguage(var5);
            GuiLanguage.this.game_settings_3.language = var5.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.field_146454_h.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.isCurrentLanguageBidirectional());
            GuiLanguage.this.field_146452_r.displayString = I18n.format("gui.done", new Object[0]);
            GuiLanguage.this.field_146455_i.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.game_settings_3.saveOptions();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return this.field_148176_l.get(slotIndex).equals(GuiLanguage.this.field_146454_h.getCurrentLanguage().getLanguageCode());
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
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, this.field_148177_m.get(this.field_148176_l.get(p_180791_1_)).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.getCurrentLanguage().isBidirectional());
        }
    }
}
