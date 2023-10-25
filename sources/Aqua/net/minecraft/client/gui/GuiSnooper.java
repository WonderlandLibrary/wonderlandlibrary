package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper
extends GuiScreen {
    private final GuiScreen field_146608_a;
    private final GameSettings game_settings_2;
    private final java.util.List<String> field_146604_g = Lists.newArrayList();
    private final java.util.List<String> field_146609_h = Lists.newArrayList();
    private String field_146610_i;
    private String[] field_146607_r;
    private List field_146606_s;
    private GuiButton field_146605_t;

    public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_) {
        this.field_146608_a = p_i1061_1_;
        this.game_settings_2 = p_i1061_2_;
    }

    public void initGui() {
        this.field_146610_i = I18n.format((String)"options.snooper.title", (Object[])new Object[0]);
        String s = I18n.format((String)"options.snooper.desc", (Object[])new Object[0]);
        ArrayList list = Lists.newArrayList();
        for (String s1 : this.fontRendererObj.listFormattedStringToWidth(s, width - 30)) {
            list.add((Object)s1);
        }
        this.field_146607_r = (String[])list.toArray((Object[])new String[list.size()]);
        this.field_146604_g.clear();
        this.field_146609_h.clear();
        this.field_146605_t = new GuiButton(1, width / 2 - 152, height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED));
        this.buttonList.add((Object)this.field_146605_t);
        this.buttonList.add((Object)new GuiButton(2, width / 2 + 2, height - 30, 150, 20, I18n.format((String)"gui.done", (Object[])new Object[0])));
        boolean flag = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        for (Map.Entry entry : new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
            this.field_146604_g.add((Object)((flag ? "C " : "") + (String)entry.getKey()));
            this.field_146609_h.add((Object)this.fontRendererObj.trimStringToWidth((String)entry.getValue(), width - 220));
        }
        if (flag) {
            for (Map.Entry entry1 : new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
                this.field_146604_g.add((Object)("S " + (String)entry1.getKey()));
                this.field_146609_h.add((Object)this.fontRendererObj.trimStringToWidth((String)entry1.getValue(), width - 220));
            }
        }
        this.field_146606_s = new List(this);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146606_s.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                this.game_settings_2.saveOptions();
                this.game_settings_2.saveOptions();
                this.mc.displayGuiScreen(this.field_146608_a);
            }
            if (button.id == 1) {
                this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
                this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.field_146606_s.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_146610_i, width / 2, 8, 0xFFFFFF);
        int i = 22;
        for (String s : this.field_146607_r) {
            this.drawCenteredString(this.fontRendererObj, s, width / 2, i, 0x808080);
            i += FontRenderer.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    static /* synthetic */ java.util.List access$000(GuiSnooper x0) {
        return x0.field_146604_g;
    }

    static /* synthetic */ java.util.List access$100(GuiSnooper x0) {
        return x0.field_146609_h;
    }
}
