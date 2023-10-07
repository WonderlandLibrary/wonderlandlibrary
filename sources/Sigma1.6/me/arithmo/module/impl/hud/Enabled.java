/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.hud;

import java.awt.Color;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.management.ColorManager;
import me.arithmo.management.ColorObject;
import me.arithmo.management.FontManager;
import me.arithmo.management.animate.Opacity;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.module.impl.hud.BubbleGui;
import me.arithmo.module.impl.hud.TabGUI;
import me.arithmo.module.impl.other.ChatCommands;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;

public class Enabled
extends Module {
    private String RAINBOW = "RAINBOW";
    private Opacity hue = new Opacity(0);

    public Enabled(ModuleData data) {
        super(data);
        this.settings.put(this.RAINBOW, new Setting<Boolean>(this.RAINBOW, false, "Rainbow array list."));
    }

    @RegisterEvent(events={EventRenderGui.class})
    public void onEvent(Event event) {
        TTFFontRenderer normal = Client.fm.getFont("SFB 8");
        if (Enabled.mc.gameSettings.showDebugInfo) {
            return;
        }
        EventRenderGui e = (EventRenderGui)event;
        boolean rainbow = (Boolean)((Setting)this.settings.get(this.RAINBOW)).getValue();
        boolean t = Client.getModuleManager().isEnabled(TabGUI.class);
        RenderingUtil.rectangle(2.0, 1.0, 56.0, 13.0, Colors.getColor(0, t ? TabGUI.opacity : 200));
        RenderingUtil.rectangle(2.3, 1.3, 4.0, 12.7, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, t ? TabGUI.opacity + 40 : 232));
        Client.fm.getFont("SFR 11").drawStringWithShadow("Sigma", 5.0f, 3.0f, Colors.getColor(255, t ? TabGUI.opacity + 64 : 232));
        float offset = Client.fm.getFont("SFR 11").getWidth("Sigma");
        Client.fm.getFont("SFB 7").drawStringWithShadow(Client.version, 4.0f + offset, 3.0f, rainbow ? Color.getHSBColor(this.hue.getOpacity() / 255.0f, 0.6f, 1.0f).getRGB() : Colors.getColor(232, 100, 80, t ? TabGUI.opacity + 64 : 255));
        int y = 4;
        CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
        for (Module module : Client.getModuleManager().getArray()) {
            if (!module.isEnabled() || this.shouldHide(module)) continue;
            modules.add(module);
        }
        modules.sort(Comparator.comparingDouble(m -> - normal.getWidth(m.getSuffix() != null ? m.getName() + " " + m.getSuffix() : m.getName())));
        this.hue.interp(256, 1);
        if (this.hue.getOpacity() > 255.0f) {
            this.hue.setOpacity(0.0f);
        }
        float h = this.hue.getOpacity();
        for (Module module : modules) {
            if (h > 255.0f) {
                h = 0.0f;
            }
            Color color = Color.getHSBColor(h / 255.0f, 0.6f, 1.0f);
            int c = color.getRGB();
            String suffix = module.getSuffix() != null ? " " + module.getSuffix() : "";
            float x = (float)e.getResolution().getScaledWidth() - normal.getWidth(module.getName() + suffix) - 2.0f;
            RenderingUtil.rectangle(x - 1.0f, (double)y - 4.3, e.getResolution().getScaledWidth(), (double)y + 5.5, Colors.getColor(0, 160));
            RenderingUtil.rectangle((double)e.getResolution().getScaledWidth() - 1.6, (double)y - 4.3, e.getResolution().getScaledWidth(), (double)y + 5.5, rainbow ? c : -1);
            normal.drawStringWithShadow(module.getName(), x, y - 1, rainbow ? c : -1);
            if (!Objects.equals(suffix, "")) {
                normal.drawStringWithShadow(suffix, x + normal.getWidth(module.getName()) - 2.0f, y - 1, Colors.getColor(Colors.getColor(150)));
            }
            h += 5.0f;
            y += 10;
        }
        Enabled.mc.fontRendererObj.drawString("", 0.0f, 0.0f, -1);
    }

    private boolean shouldHide(Module module) {
        ModuleData.Type type = module.getType();
        if (this.isBlacklisted(module.getClass())) {
            return true;
        }
        return false;
    }

    private boolean isBlacklisted(Class<? extends Module> clazz) {
        if (clazz.equals(ChatCommands.class) || clazz.equals(TabGUI.class) || clazz.equals(BubbleGui.class) || clazz.equals(Enabled.class)) {
            return true;
        }
        return false;
    }
}

