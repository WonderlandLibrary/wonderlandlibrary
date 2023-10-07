// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.hud;

import java.util.HashMap;
import exhibition.module.impl.other.ChatCommands;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import java.util.List;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import exhibition.Client;
import exhibition.util.render.Colors;
import exhibition.management.ColorManager;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Enabled extends Module
{
    private String SPEED;
    private String RAINBOW;
    private float hue;
    
    public Enabled(final ModuleData data) {
        super(data);
        this.SPEED = "SPEED";
        this.RAINBOW = "RAINBOW";
        this.hue = 0.0f;
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.RAINBOW, new Setting<Boolean>(this.RAINBOW, false, "Does the rainbow array like MemeValk has done."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.SPEED, new Setting<Integer>(this.SPEED, 15, "The speed colors will alternate from.", 1.0, 1.0, 25.0));
    }
    
    @RegisterEvent(events = { EventRenderGui.class })
    @Override
    public void onEvent(final Event event) {
        if (Enabled.mc.gameSettings.showDebugInfo) {
            return;
        }
        final EventRenderGui e = (EventRenderGui)event;
        Enabled.mc.fontRendererObj.drawStringWithShadow("E", 3.0f, 3.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
        Enabled.mc.fontRendererObj.drawStringWithShadow("xhibition", 3 + Enabled.mc.fontRendererObj.getStringWidth("E"), 3.0f, Colors.getColor(255));
        int y = Client.getModuleManager().get(TabGUI.class).isEnabled() ? (Client.getModuleManager().get(Radar.class).isEnabled() ? (2 + Enabled.mc.fontRendererObj.FONT_HEIGHT / 4 + 83) : 2) : (Client.getModuleManager().get(Radar.class).isEnabled() ? (2 + Enabled.mc.fontRendererObj.FONT_HEIGHT / 4 + 10) : 2);
        final double radius = Math.tan(25.0) / Math.tan(Enabled.mc.gameSettings.fovSetting / 2.0f);
        final List<Module> modules = new CopyOnWriteArrayList<Module>();
        for (final Module module : Client.getModuleManager().getArray()) {
            if (module.isEnabled() && !this.shouldHide(module)) {
                modules.add(module);
            }
        }
        final FontRenderer fontRendererObj;
        String text;
        final float w1;
        final FontRenderer fontRendererObj2;
        String text2;
        final float w2;
        modules.sort((o1, o2) -> {
            fontRendererObj = Enabled.mc.fontRendererObj;
            if (o1.getSuffix() != null) {
                text = o1.getName() + " " + o1.getSuffix();
            }
            else {
                text = o1.getName();
            }
            w1 = fontRendererObj.getStringWidth(text);
            fontRendererObj2 = Enabled.mc.fontRendererObj;
            if (o2.getSuffix() != null) {
                text2 = o2.getName() + " " + o2.getSuffix();
            }
            else {
                text2 = o2.getName();
            }
            w2 = fontRendererObj2.getStringWidth(text2);
            return (int)(w2 - w1);
        });
        this.hue += ((HashMap<K, Setting<Number>>)this.settings).get(this.SPEED).getValue().floatValue() / 5.0f;
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        for (final Module module2 : modules) {
            if (h > 255.0f) {
                h = 0.0f;
            }
            final Color color = Color.getHSBColor(h / 255.0f, 0.9f, 0.9f);
            final int c = color.getRGB();
            final String suffix = (module2.getSuffix() != null) ? (" §7" + module2.getSuffix()) : "";
            final int x = Client.getModuleManager().get(Radar.class).isEnabled() ? 2 : (e.getResolution().getScaledWidth() - Enabled.mc.fontRendererObj.getStringWidth(module2.getName() + suffix) - 2);
            final boolean rainbow = ((HashMap<K, Setting<Boolean>>)this.settings).get(this.RAINBOW).getValue();
            Enabled.mc.fontRendererObj.drawStringWithShadow(module2.getName() + suffix, x, y, rainbow ? c : -1);
            h += 9.0f;
            y += 9;
        }
    }
    
    private boolean shouldHide(final Module module) {
        final ModuleData.Type type = module.getType();
        return this.isBlacklisted(module.getClass());
    }
    
    private boolean isBlacklisted(final Class<? extends Module> clazz) {
        return clazz.equals(ChatCommands.class) || clazz.equals(TabGUI.class) || clazz.equals(BubbleGui.class) || clazz.equals(Enabled.class);
    }
}
