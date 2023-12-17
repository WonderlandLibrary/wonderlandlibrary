// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.optifine.gui.GuiScreenCapeOF;
import java.io.IOException;
import net.optifine.gui.GuiButtonOF;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.resources.I18n;

public class GuiCustomizeSkin extends GuiScreen
{
    private final GuiScreen parentScreen;
    private String title;
    
    public GuiCustomizeSkin(final GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        this.title = I18n.format("options.skinCustomisation.title", new Object[0]);
        for (final EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
            this.buttonList.add(new ButtonPart(enumplayermodelparts.getPartId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts));
            ++i;
        }
        this.buttonList.add(new GuiOptionButton(199, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), GameSettings.Options.MAIN_HAND, GuiCustomizeSkin.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)));
        if (++i % 2 == 1) {
            ++i;
        }
        this.buttonList.add(new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("of.options.skinCustomisation.ofCape", new Object[0])));
        i += 2;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            GuiCustomizeSkin.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 210) {
                GuiCustomizeSkin.mc.displayGuiScreen(new GuiScreenCapeOF(this));
            }
            if (button.id == 200) {
                GuiCustomizeSkin.mc.gameSettings.saveOptions();
                GuiCustomizeSkin.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 199) {
                GuiCustomizeSkin.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
                button.displayString = GuiCustomizeSkin.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
                GuiCustomizeSkin.mc.gameSettings.sendSettingsToServer();
            }
            else if (button instanceof ButtonPart) {
                final EnumPlayerModelParts enumplayermodelparts = ((ButtonPart)button).playerModelParts;
                GuiCustomizeSkin.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
                button.displayString = this.getMessage(enumplayermodelparts);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private String getMessage(final EnumPlayerModelParts playerModelParts) {
        String s;
        if (GuiCustomizeSkin.mc.gameSettings.getModelParts().contains(playerModelParts)) {
            s = I18n.format("options.on", new Object[0]);
        }
        else {
            s = I18n.format("options.off", new Object[0]);
        }
        return playerModelParts.getName().getFormattedText() + ": " + s;
    }
    
    class ButtonPart extends GuiButton
    {
        private final EnumPlayerModelParts playerModelParts;
        
        private ButtonPart(final int p_i45514_2_, final int p_i45514_3_, final int p_i45514_4_, final int p_i45514_5_, final int p_i45514_6_, final EnumPlayerModelParts playerModelParts) {
            super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.this.getMessage(playerModelParts));
            this.playerModelParts = playerModelParts;
        }
    }
}
