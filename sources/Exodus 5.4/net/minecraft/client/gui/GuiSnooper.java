/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper
extends GuiScreen {
    private final java.util.List<String> field_146609_h;
    private String field_146610_i;
    private GuiButton field_146605_t;
    private final GameSettings game_settings_2;
    private final GuiScreen field_146608_a;
    private final java.util.List<String> field_146604_g = Lists.newArrayList();
    private List field_146606_s;
    private String[] field_146607_r;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.field_146606_s.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.field_146610_i, width / 2, 8, 0xFFFFFF);
        int n3 = 22;
        String[] stringArray = this.field_146607_r;
        int n4 = this.field_146607_r.length;
        int n5 = 0;
        while (n5 < n4) {
            String string = stringArray[n5];
            this.drawCenteredString(this.fontRendererObj, string, width / 2, n3, 0x808080);
            n3 += this.fontRendererObj.FONT_HEIGHT;
            ++n5;
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146606_s.handleMouseInput();
    }

    public GuiSnooper(GuiScreen guiScreen, GameSettings gameSettings) {
        this.field_146609_h = Lists.newArrayList();
        this.field_146608_a = guiScreen;
        this.game_settings_2 = gameSettings;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 2) {
                this.game_settings_2.saveOptions();
                this.game_settings_2.saveOptions();
                this.mc.displayGuiScreen(this.field_146608_a);
            }
            if (guiButton.id == 1) {
                this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
                this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
            }
        }
    }

    @Override
    public void initGui() {
        this.field_146610_i = I18n.format("options.snooper.title", new Object[0]);
        String string = I18n.format("options.snooper.desc", new Object[0]);
        ArrayList arrayList = Lists.newArrayList();
        for (String string2 : this.fontRendererObj.listFormattedStringToWidth(string, width - 30)) {
            arrayList.add(string2);
        }
        this.field_146607_r = arrayList.toArray(new String[arrayList.size()]);
        this.field_146604_g.clear();
        this.field_146609_h.clear();
        this.field_146605_t = new GuiButton(1, width / 2 - 152, height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED));
        this.buttonList.add(this.field_146605_t);
        this.buttonList.add(new GuiButton(2, width / 2 + 2, height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
        boolean bl = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        for (Map.Entry<String, String> entry : new TreeMap<String, String>(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
            this.field_146604_g.add(String.valueOf(bl ? "C " : "") + (String)entry.getKey());
            this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), width - 220));
        }
        if (bl) {
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
                this.field_146604_g.add("S " + entry.getKey());
                this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), width - 220));
            }
        }
        this.field_146606_s = new List();
    }

    class List
    extends GuiSlot {
        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
        }

        @Override
        protected boolean isSelected(int n) {
            return false;
        }

        public List() {
            super(GuiSnooper.this.mc, width, height, 80, height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
        }

        @Override
        protected int getScrollBarX() {
            return this.width - 10;
        }

        @Override
        protected int getSize() {
            return GuiSnooper.this.field_146604_g.size();
        }

        @Override
        protected void drawBackground() {
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146604_g.get(n), 10.0, n3, 0xFFFFFF);
            GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146609_h.get(n), 230.0, n3, 0xFFFFFF);
        }
    }
}

