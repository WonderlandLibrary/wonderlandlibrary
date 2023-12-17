// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiOptions extends GuiScreen
{
    private static final GameSettings.Options[] SCREEN_OPTIONS;
    private final GuiScreen lastScreen;
    private final GameSettings settings;
    private GuiButton difficultyButton;
    private GuiLockIconButton lockButton;
    protected String title;
    
    public GuiOptions(final GuiScreen p_i1046_1_, final GameSettings p_i1046_2_) {
        this.title = "Options";
        this.lastScreen = p_i1046_1_;
        this.settings = p_i1046_2_;
    }
    
    @Override
    public void initGui() {
        this.title = I18n.format("options.title", new Object[0]);
        int i = 0;
        for (final GameSettings.Options gamesettings$options : GuiOptions.SCREEN_OPTIONS) {
            if (gamesettings$options.isFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            }
            else {
                final GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.settings.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }
            ++i;
        }
        if (GuiOptions.mc.world != null) {
            final EnumDifficulty enumdifficulty = GuiOptions.mc.world.getDifficulty();
            this.difficultyButton = new GuiButton(108, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.getDifficultyText(enumdifficulty));
            this.buttonList.add(this.difficultyButton);
            if (GuiOptions.mc.isSingleplayer() && !GuiOptions.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
                this.difficultyButton.setWidth(this.difficultyButton.getButtonWidth() - 20);
                this.lockButton = new GuiLockIconButton(109, this.difficultyButton.x + this.difficultyButton.getButtonWidth(), this.difficultyButton.y);
                this.buttonList.add(this.lockButton);
                this.lockButton.setLocked(GuiOptions.mc.world.getWorldInfo().isDifficultyLocked());
                this.lockButton.enabled = !this.lockButton.isLocked();
                this.difficultyButton.enabled = !this.lockButton.isLocked();
            }
            else {
                this.difficultyButton.enabled = false;
            }
        }
        else {
            this.buttonList.add(new GuiOptionButton(GameSettings.Options.REALMS_NOTIFICATIONS.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), GameSettings.Options.REALMS_NOTIFICATIONS, this.settings.getKeyBinding(GameSettings.Options.REALMS_NOTIFICATIONS)));
        }
        this.buttonList.add(new GuiButton(110, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
        this.buttonList.add(new GuiButton(106, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.video", new Object[0])));
        this.buttonList.add(new GuiButton(100, this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.language", new Object[0])));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
        this.buttonList.add(new GuiButton(105, this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
        this.buttonList.add(new GuiButton(104, this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.snooper.view", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }
    
    public String getDifficultyText(final EnumDifficulty p_175355_1_) {
        final ITextComponent itextcomponent = new TextComponentString("");
        itextcomponent.appendSibling(new TextComponentTranslation("options.difficulty", new Object[0]));
        itextcomponent.appendText(": ");
        itextcomponent.appendSibling(new TextComponentTranslation(p_175355_1_.getTranslationKey(), new Object[0]));
        return itextcomponent.getFormattedText();
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        GuiOptions.mc.displayGuiScreen(this);
        if (id == 109 && result && GuiOptions.mc.world != null) {
            GuiOptions.mc.world.getWorldInfo().setDifficultyLocked(true);
            this.lockButton.setLocked(true);
            this.lockButton.enabled = false;
            this.difficultyButton.enabled = false;
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            GuiOptions.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id < 100 && button instanceof GuiOptionButton) {
                final GameSettings.Options gamesettings$options = ((GuiOptionButton)button).getOption();
                this.settings.setOptionValue(gamesettings$options, 1);
                button.displayString = this.settings.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
            }
            if (button.id == 108) {
                GuiOptions.mc.world.getWorldInfo().setDifficulty(EnumDifficulty.byId(GuiOptions.mc.world.getDifficulty().getId() + 1));
                this.difficultyButton.displayString = this.getDifficultyText(GuiOptions.mc.world.getDifficulty());
            }
            if (button.id == 109) {
                GuiOptions.mc.displayGuiScreen(new GuiYesNo(this, new TextComponentTranslation("difficulty.lock.title", new Object[0]).getFormattedText(), new TextComponentTranslation("difficulty.lock.question", new Object[] { new TextComponentTranslation(GuiOptions.mc.world.getWorldInfo().getDifficulty().getTranslationKey(), new Object[0]) }).getFormattedText(), 109));
            }
            if (button.id == 110) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiCustomizeSkin(this));
            }
            if (button.id == 101) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiVideoSettings(this, this.settings));
            }
            if (button.id == 100) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiControls(this, this.settings));
            }
            if (button.id == 102) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiLanguage(this, this.settings, GuiOptions.mc.getLanguageManager()));
            }
            if (button.id == 103) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new ScreenChatOptions(this, this.settings));
            }
            if (button.id == 104) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiSnooper(this, this.settings));
            }
            if (button.id == 200) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(this.lastScreen);
            }
            if (button.id == 105) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
            }
            if (button.id == 106) {
                GuiOptions.mc.gameSettings.saveOptions();
                GuiOptions.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.settings));
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        SCREEN_OPTIONS = new GameSettings.Options[] { GameSettings.Options.FOV };
    }
}
