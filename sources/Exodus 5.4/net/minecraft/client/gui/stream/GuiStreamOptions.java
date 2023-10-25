/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.stream.GuiIngestServers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;

public class GuiStreamOptions
extends GuiScreen {
    private static final GameSettings.Options[] field_152312_a = new GameSettings.Options[]{GameSettings.Options.STREAM_BYTES_PER_PIXEL, GameSettings.Options.STREAM_FPS, GameSettings.Options.STREAM_KBPS, GameSettings.Options.STREAM_SEND_METADATA, GameSettings.Options.STREAM_VOLUME_MIC, GameSettings.Options.STREAM_VOLUME_SYSTEM, GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR, GameSettings.Options.STREAM_COMPRESSION};
    private final GuiScreen parentScreen;
    private String field_152319_i;
    private String field_152313_r;
    private static final GameSettings.Options[] field_152316_f = new GameSettings.Options[]{GameSettings.Options.STREAM_CHAT_ENABLED, GameSettings.Options.STREAM_CHAT_USER_FILTER};
    private int field_152314_s;
    private boolean field_152315_t = false;
    private final GameSettings field_152318_h;

    public GuiStreamOptions(GuiScreen guiScreen, GameSettings gameSettings) {
        this.parentScreen = guiScreen;
        this.field_152318_h = gameSettings;
    }

    @Override
    public void initGui() {
        Object object;
        int n = 0;
        this.field_152319_i = I18n.format("options.stream.title", new Object[0]);
        this.field_152313_r = I18n.format("options.stream.chat.title", new Object[0]);
        GameSettings.Options[] optionsArray = field_152312_a;
        int n2 = field_152312_a.length;
        int n3 = 0;
        while (n3 < n2) {
            object = optionsArray[n3];
            if (object.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(object.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), (GameSettings.Options)((Object)object)));
            } else {
                this.buttonList.add(new GuiOptionButton(object.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), (GameSettings.Options)((Object)object), this.field_152318_h.getKeyBinding((GameSettings.Options)((Object)object))));
            }
            ++n;
            ++n3;
        }
        if (n % 2 == 1) {
            ++n;
        }
        this.field_152314_s = height / 6 + 24 * (n >> 1) + 6;
        n += 2;
        optionsArray = field_152316_f;
        n2 = field_152316_f.length;
        n3 = 0;
        while (n3 < n2) {
            object = optionsArray[n3];
            if (object.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(object.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), (GameSettings.Options)((Object)object)));
            } else {
                this.buttonList.add(new GuiOptionButton(object.returnEnumOrdinal(), width / 2 - 155 + n % 2 * 160, height / 6 + 24 * (n >> 1), (GameSettings.Options)((Object)object), this.field_152318_h.getKeyBinding((GameSettings.Options)((Object)object))));
            }
            ++n;
            ++n3;
        }
        this.buttonList.add(new GuiButton(200, width / 2 - 155, height / 6 + 168, 150, 20, I18n.format("gui.done", new Object[0])));
        object = new GuiButton(201, width / 2 + 5, height / 6 + 168, 150, 20, I18n.format("options.stream.ingestSelection", new Object[0]));
        ((GuiButton)object).enabled = this.mc.getTwitchStream().isReadyToBroadcast() && this.mc.getTwitchStream().func_152925_v().length > 0 || this.mc.getTwitchStream().func_152908_z();
        this.buttonList.add(object);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id < 100 && guiButton instanceof GuiOptionButton) {
                GameSettings.Options options = ((GuiOptionButton)guiButton).returnEnumOptions();
                this.field_152318_h.setOptionValue(options, 1);
                guiButton.displayString = this.field_152318_h.getKeyBinding(GameSettings.Options.getEnumOptions(guiButton.id));
                if (this.mc.getTwitchStream().isBroadcasting() && options != GameSettings.Options.STREAM_CHAT_ENABLED && options != GameSettings.Options.STREAM_CHAT_USER_FILTER) {
                    this.field_152315_t = true;
                }
            } else if (guiButton instanceof GuiOptionSlider) {
                if (guiButton.id == GameSettings.Options.STREAM_VOLUME_MIC.returnEnumOrdinal()) {
                    this.mc.getTwitchStream().updateStreamVolume();
                } else if (guiButton.id == GameSettings.Options.STREAM_VOLUME_SYSTEM.returnEnumOrdinal()) {
                    this.mc.getTwitchStream().updateStreamVolume();
                } else if (this.mc.getTwitchStream().isBroadcasting()) {
                    this.field_152315_t = true;
                }
            }
            if (guiButton.id == 200) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 201) {
                Minecraft.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiIngestServers(this));
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152319_i, width / 2, 20, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, this.field_152313_r, width / 2, this.field_152314_s, 0xFFFFFF);
        if (this.field_152315_t) {
            this.drawCenteredString(this.fontRendererObj, (Object)((Object)EnumChatFormatting.RED) + I18n.format("options.stream.changes", new Object[0]), width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
        }
        super.drawScreen(n, n2, f);
    }
}

