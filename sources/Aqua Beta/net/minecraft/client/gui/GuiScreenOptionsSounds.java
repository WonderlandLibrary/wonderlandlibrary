package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiScreenOptionsSounds
extends GuiScreen {
    private final GuiScreen field_146505_f;
    private final GameSettings game_settings_4;
    protected String field_146507_a = "Options";
    private String field_146508_h;

    public GuiScreenOptionsSounds(GuiScreen p_i45025_1_, GameSettings p_i45025_2_) {
        this.field_146505_f = p_i45025_1_;
        this.game_settings_4 = p_i45025_2_;
    }

    public void initGui() {
        int i = 0;
        this.field_146507_a = I18n.format((String)"options.sounds.title", (Object[])new Object[0]);
        this.field_146508_h = I18n.format((String)"options.off", (Object[])new Object[0]);
        this.buttonList.add((Object)new Button(this, SoundCategory.MASTER.getCategoryId(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
        i += 2;
        for (SoundCategory soundcategory : SoundCategory.values()) {
            if (soundcategory == SoundCategory.MASTER) continue;
            this.buttonList.add((Object)new Button(this, soundcategory.getCategoryId(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
            ++i;
        }
        this.buttonList.add((Object)new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled && button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.field_146505_f);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146507_a, width / 2, 15, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected String getSoundVolume(SoundCategory p_146504_1_) {
        float f = this.game_settings_4.getSoundLevel(p_146504_1_);
        return f == 0.0f ? this.field_146508_h : (int)(f * 100.0f) + "%";
    }

    static /* synthetic */ GameSettings access$000(GuiScreenOptionsSounds x0) {
        return x0.game_settings_4;
    }
}
