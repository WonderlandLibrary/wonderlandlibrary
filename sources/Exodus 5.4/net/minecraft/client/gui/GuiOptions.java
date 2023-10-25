/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.client.gui.stream.GuiStreamOptions;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class GuiOptions
extends GuiScreen
implements GuiYesNoCallback {
    private final GameSettings game_settings_1;
    private GuiLockIconButton field_175356_r;
    protected String field_146442_a = "Options";
    private GuiButton field_175357_i;
    private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[]{GameSettings.Options.FOV};
    private final GuiScreen field_146441_g;

    public GuiOptions(GuiScreen guiScreen, GameSettings gameSettings) {
        this.field_146441_g = guiScreen;
        this.game_settings_1 = gameSettings;
    }

    @Override
    public void initGui() {
        Enum enum_;
        int n = 0;
        this.field_146442_a = I18n.format("options.title", new Object[0]);
        GameSettings.Options[] optionsArray = field_146440_f;
        int n2 = field_146440_f.length;
        int n3 = 0;
        while (n3 < n2) {
            enum_ = optionsArray[n3];
            if (((GameSettings.Options)enum_).getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(((GameSettings.Options)enum_).returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 - 12 + 24 * (n >> 1), (GameSettings.Options)enum_));
            } else {
                GuiOptionButton guiOptionButton = new GuiOptionButton(((GameSettings.Options)enum_).returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 - 12 + 24 * (n >> 1), (GameSettings.Options)enum_, this.game_settings_1.getKeyBinding((GameSettings.Options)enum_));
                this.buttonList.add(guiOptionButton);
            }
            ++n;
            ++n3;
        }
        if (Minecraft.theWorld != null) {
            enum_ = Minecraft.theWorld.getDifficulty();
            this.field_175357_i = new GuiButton(108, width / 2 - 155 + n % 2 * 160, height / 6 - 12 + 24 * (n >> 1), 150, 20, this.func_175355_a((EnumDifficulty)enum_));
            this.buttonList.add(this.field_175357_i);
            if (this.mc.isSingleplayer() && !Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                this.field_175357_i.setWidth(this.field_175357_i.getButtonWidth() - 20);
                this.field_175356_r = new GuiLockIconButton(109, this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(), this.field_175357_i.yPosition);
                this.buttonList.add(this.field_175356_r);
                this.field_175356_r.func_175229_b(Minecraft.theWorld.getWorldInfo().isDifficultyLocked());
                this.field_175356_r.enabled = !this.field_175356_r.func_175230_c();
                this.field_175357_i.enabled = !this.field_175356_r.func_175230_c();
            } else {
                this.field_175357_i.enabled = false;
            }
        }
        this.buttonList.add(new GuiButton(110, width / 2 - 155, height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
        this.buttonList.add(new GuiButton(8675309, width / 2 + 5, height / 6 + 48 - 6, 150, 20, "Super Secret Settings..."){

            @Override
            public void playPressSound(SoundHandler soundHandler) {
                SoundEventAccessorComposite soundEventAccessorComposite = soundHandler.getRandomSoundFromCategories(SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER);
                if (soundEventAccessorComposite != null) {
                    soundHandler.playSound(PositionedSoundRecord.create(soundEventAccessorComposite.getSoundEventLocation(), 0.5f));
                }
            }
        });
        this.buttonList.add(new GuiButton(106, width / 2 - 155, height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
        this.buttonList.add(new GuiButton(107, width / 2 + 5, height / 6 + 72 - 6, 150, 20, I18n.format("options.stream", new Object[0])));
        this.buttonList.add(new GuiButton(101, width / 2 - 155, height / 6 + 96 - 6, 150, 20, I18n.format("options.video", new Object[0])));
        this.buttonList.add(new GuiButton(100, width / 2 + 5, height / 6 + 96 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
        this.buttonList.add(new GuiButton(102, width / 2 - 155, height / 6 + 120 - 6, 150, 20, I18n.format("options.language", new Object[0])));
        this.buttonList.add(new GuiButton(103, width / 2 + 5, height / 6 + 120 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
        this.buttonList.add(new GuiButton(105, width / 2 - 155, height / 6 + 144 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
        this.buttonList.add(new GuiButton(104, width / 2 + 5, height / 6 + 144 - 6, 150, 20, I18n.format("options.snooper.view", new Object[0])));
        this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146442_a, width / 2, 15, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    public String func_175355_a(EnumDifficulty enumDifficulty) {
        ChatComponentText chatComponentText = new ChatComponentText("");
        chatComponentText.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
        chatComponentText.appendText(": ");
        chatComponentText.appendSibling(new ChatComponentTranslation(enumDifficulty.getDifficultyResourceKey(), new Object[0]));
        return chatComponentText.getFormattedText();
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        this.mc.displayGuiScreen(this);
        if (n == 109 && bl && Minecraft.theWorld != null) {
            Minecraft.theWorld.getWorldInfo().setDifficultyLocked(true);
            this.field_175356_r.func_175229_b(true);
            this.field_175356_r.enabled = false;
            this.field_175357_i.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            Object object;
            if (guiButton.id < 100 && guiButton instanceof GuiOptionButton) {
                object = ((GuiOptionButton)guiButton).returnEnumOptions();
                this.game_settings_1.setOptionValue((GameSettings.Options)((Object)object), 1);
                guiButton.displayString = this.game_settings_1.getKeyBinding(GameSettings.Options.getEnumOptions(guiButton.id));
            }
            if (guiButton.id == 108) {
                Minecraft.theWorld.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(Minecraft.theWorld.getDifficulty().getDifficultyId() + 1));
                this.field_175357_i.displayString = this.func_175355_a(Minecraft.theWorld.getDifficulty());
            }
            if (guiButton.id == 109) {
                this.mc.displayGuiScreen(new GuiYesNo(this, new ChatComponentTranslation("difficulty.lock.title", new Object[0]).getFormattedText(), new ChatComponentTranslation("difficulty.lock.question", new ChatComponentTranslation(Minecraft.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0])).getFormattedText(), 109));
            }
            if (guiButton.id == 110) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
            }
            if (guiButton.id == 8675309) {
                this.mc.entityRenderer.activateNextShader();
            }
            if (guiButton.id == 101) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.game_settings_1));
            }
            if (guiButton.id == 100) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiControls(this, this.game_settings_1));
            }
            if (guiButton.id == 102) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiLanguage(this, this.game_settings_1, this.mc.getLanguageManager()));
            }
            if (guiButton.id == 103) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
            }
            if (guiButton.id == 104) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiSnooper(this, this.game_settings_1));
            }
            if (guiButton.id == 200) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.field_146441_g);
            }
            if (guiButton.id == 105) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
            }
            if (guiButton.id == 106) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.game_settings_1));
            }
            if (guiButton.id == 107) {
                Minecraft.gameSettings.saveOptions();
                object = this.mc.getTwitchStream();
                if (object.func_152936_l() && object.func_152928_D()) {
                    this.mc.displayGuiScreen(new GuiStreamOptions(this, this.game_settings_1));
                } else {
                    GuiStreamUnavailable.func_152321_a(this);
                }
            }
        }
    }
}

