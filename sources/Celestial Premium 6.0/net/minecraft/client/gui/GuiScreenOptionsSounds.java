/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class GuiScreenOptionsSounds
extends GuiScreen {
    private final GuiScreen parent;
    private final GameSettings game_settings_4;
    protected String title = "Options";
    private String offDisplayString;

    public GuiScreenOptionsSounds(GuiScreen parentIn, GameSettings settingsIn) {
        this.parent = parentIn;
        this.game_settings_4 = settingsIn;
    }

    @Override
    public void initGui() {
        this.title = I18n.format("options.sounds.title", new Object[0]);
        this.offDisplayString = I18n.format("options.off", new Object[0]);
        int i = 0;
        this.buttonList.add(new Button(SoundCategory.MASTER.ordinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
        i += 2;
        for (SoundCategory soundcategory : SoundCategory.values()) {
            if (soundcategory == SoundCategory.MASTER) continue;
            this.buttonList.add(new Button(soundcategory.ordinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
            ++i;
        }
        int j = this.width / 2 - 75;
        int k = this.height / 6 - 12;
        this.buttonList.add(new GuiOptionButton(201, j, k + 24 * (++i >> 1), GameSettings.Options.SHOW_SUBTITLES, this.game_settings_4.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES)));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parent);
            } else if (button.id == 201) {
                this.mc.gameSettings.setOptionValue(GameSettings.Options.SHOW_SUBTITLES, 1);
                button.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES);
                this.mc.gameSettings.saveOptions();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected String getDisplayString(SoundCategory category) {
        float f = this.game_settings_4.getSoundLevel(category);
        return f == 0.0f ? this.offDisplayString : (int)(f * 100.0f) + "%";
    }

    class Button
    extends GuiButton {
        private final SoundCategory category;
        private final String categoryName;
        public float volume;
        public boolean pressed;

        public Button(int p_i46744_2_, int x, int y, SoundCategory categoryIn, boolean master) {
            super(p_i46744_2_, x, y, master ? 310 : 150, 20, "");
            this.volume = 1.0f;
            this.category = categoryIn;
            this.categoryName = I18n.format("soundCategory." + categoryIn.getName(), new Object[0]);
            this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(categoryIn);
            this.volume = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(categoryIn);
        }

        @Override
        protected int getHoverState(boolean mouseOver) {
            return 0;
        }

        @Override
        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                if (this.pressed) {
                    this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                    this.volume = MathHelper.clamp(this.volume, 0.0f, 1.0f);
                    mc.gameSettings.setSoundLevel(this.category, this.volume);
                    mc.gameSettings.saveOptions();
                    this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.volume = MathHelper.clamp(this.volume, 0.0f, 1.0f);
                mc.gameSettings.setSoundLevel(this.category, this.volume);
                mc.gameSettings.saveOptions();
                this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
                this.pressed = true;
                return true;
            }
            return false;
        }

        @Override
        public void playPressSound(SoundHandler soundHandlerIn) {
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            if (this.pressed) {
                GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            this.pressed = false;
        }
    }
}

