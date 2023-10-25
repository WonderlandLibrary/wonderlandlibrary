/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiScreenOptionsSounds
extends GuiScreen {
    private final GameSettings game_settings_4;
    private final GuiScreen field_146505_f;
    protected String field_146507_a = "Options";
    private String field_146508_h;

    protected String getSoundVolume(SoundCategory soundCategory) {
        float f = this.game_settings_4.getSoundLevel(soundCategory);
        return f == 0.0f ? this.field_146508_h : String.valueOf((int)(f * 100.0f)) + "%";
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146507_a, width / 2, 15, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    public GuiScreenOptionsSounds(GuiScreen guiScreen, GameSettings gameSettings) {
        this.field_146505_f = guiScreen;
        this.game_settings_4 = gameSettings;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled && guiButton.id == 200) {
            Minecraft.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.field_146505_f);
        }
    }

    @Override
    public void initGui() {
        int n = 0;
        this.field_146507_a = I18n.format("options.sounds.title", new Object[0]);
        this.field_146508_h = I18n.format("options.off", new Object[0]);
        this.buttonList.add(new Button(SoundCategory.MASTER.getCategoryId(), width / 2 - 155 + n % 2 * 160, height / 6 - 12 + 24 * (n >> 1), SoundCategory.MASTER, true));
        n += 2;
        SoundCategory[] soundCategoryArray = SoundCategory.values();
        int n2 = soundCategoryArray.length;
        int n3 = 0;
        while (n3 < n2) {
            SoundCategory soundCategory = soundCategoryArray[n3];
            if (soundCategory != SoundCategory.MASTER) {
                this.buttonList.add(new Button(soundCategory.getCategoryId(), width / 2 - 155 + n % 2 * 160, height / 6 - 12 + 24 * (n >> 1), soundCategory, false));
                ++n;
            }
            ++n3;
        }
        this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }

    class Button
    extends GuiButton {
        public float field_146156_o;
        public boolean field_146155_p;
        private final SoundCategory field_146153_r;
        private final String field_146152_s;

        public Button(int n, int n2, int n3, SoundCategory soundCategory, boolean bl) {
            super(n, n2, n3, bl ? 310 : 150, 20, "");
            this.field_146156_o = 1.0f;
            this.field_146153_r = soundCategory;
            this.field_146152_s = I18n.format("soundCategory." + soundCategory.getCategoryName(), new Object[0]);
            this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(soundCategory);
            this.field_146156_o = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(soundCategory);
        }

        @Override
        protected void mouseDragged(Minecraft minecraft, int n, int n2) {
            if (this.visible) {
                if (this.field_146155_p) {
                    this.field_146156_o = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
                    this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0f, 1.0f);
                    Minecraft.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                    Minecraft.gameSettings.saveOptions();
                    this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }

        @Override
        public void playPressSound(SoundHandler soundHandler) {
        }

        @Override
        protected int getHoverState(boolean bl) {
            return 0;
        }

        @Override
        public boolean mousePressed(Minecraft minecraft, int n, int n2) {
            if (super.mousePressed(minecraft, n, n2)) {
                this.field_146156_o = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
                this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0f, 1.0f);
                Minecraft.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                Minecraft.gameSettings.saveOptions();
                this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r);
                this.field_146155_p = true;
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int n, int n2) {
            if (this.field_146155_p) {
                if (this.field_146153_r == SoundCategory.MASTER) {
                    float f = 1.0f;
                } else {
                    GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(this.field_146153_r);
                }
                GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            }
            this.field_146155_p = false;
        }
    }
}

