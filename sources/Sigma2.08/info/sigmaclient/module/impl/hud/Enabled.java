package info.sigmaclient.module.impl.hud;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRenderHUD;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.other.ChatCommands;
import info.sigmaclient.module.impl.other.ClickGui;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Enabled extends Module {

    private String RAINBOW = "RAINBOW";
    private String POTIONS = "POTIONS";

    public Enabled(ModuleData data) {
        super(data);
        settings.put(RAINBOW, new Setting<>(RAINBOW, false, "Rainbow array list."));
        settings.put(POTIONS, new Setting<>(POTIONS, true, "Show active potion effects."));
    }

    private Opacity hue = new Opacity(0);

    @Override
    @RegisterEvent(events = {EventRenderHUD.class})
    public void onEvent(Event event) {
        TTFFontRenderer normal = Client.fm.getFont("SFB 8");
        if (mc.gameSettings.showDebugInfo) {
            return;
        }

        EventRenderHUD e = (EventRenderHUD) event;
        drawPotionStatus(e.getResolution());


        boolean rainbow = ((Boolean) settings.get(RAINBOW).getValue());
        boolean t = Client.getModuleManager().isEnabled(TabGUI.class);
        RenderingUtil.rectangle(2, 1, 55, 14, Colors.getColor(0, t ? TabGUI.opacity : 200));
        Client.fm.getFont("SFR 11").drawStringWithShadow(Client.clientName, 3, 4, Colors.getColor(255, t ? TabGUI.opacity + 64 : 232));
        float offset = Client.fm.getFont("SFR 11").getWidth(Client.clientName);
        Client.fm.getFont("SFB 7").drawStringWithShadow(Client.version, 1.5f + offset, 3.5F, rainbow ? Color.getHSBColor(hue.getOpacity() / 255.0f, 0.6f, 1f).getRGB() : Colors.getColor(ColorManager.hudColor.red,ColorManager.hudColor.green,ColorManager.hudColor.blue, t ? TabGUI.opacity + 64 : 255));
        int y = 4;
        List<Module> modules = new CopyOnWriteArrayList<>();
        for (Module module : Client.getModuleManager().getArray()) {
            if (module.isEnabled() && !shouldHide(module)) {
                modules.add(module);
            }
        }
        modules.sort(Comparator.comparingDouble(m -> -normal.getWidth(m.getSuffix() != null ? m.getName() + " " + m.getSuffix() : m.getName())));
        hue.interp(256, 2);
        if (hue.getOpacity() > 255) {
            hue.setOpacity(0);
        }
        float h = hue.getOpacity();
        for (Module module : modules) {
            if (h > 255) {
                h = 0;
            }
            final Color color = Color.getHSBColor(h / 255.0f, 0.6f, 1.0f);
            final int c = color.getRGB();
            String suffix = module.getSuffix() != null ? " " + module.getSuffix() : "";
            float x = e.getResolution().getScaledWidth() - normal.getWidth(module.getName() + suffix) - 2;
            RenderingUtil.rectangle(x - 1, y - 4.3, e.getResolution().getScaledWidth(), y + 5.5, Colors.getColor(0, 160));
            RenderingUtil.rectangle(e.getResolution().getScaledWidth() - 1.6, y - 4.3, e.getResolution().getScaledWidth(), y + 5.5, rainbow ? c : Colors.getColor(ColorManager.hudColor.red,ColorManager.hudColor.green,ColorManager.hudColor.blue, t ? TabGUI.opacity + 64 : 255));
            normal.drawStringWithShadow(module.getName(), x, y - 1, rainbow ? c : Colors.getColor(ColorManager.hudColor.red,ColorManager.hudColor.green,ColorManager.hudColor.blue, t ? TabGUI.opacity + 64 : 255));
            if (!Objects.equals(suffix, "")) {
                normal.drawStringWithShadow(suffix, x + normal.getWidth(module.getName()) - 2, y - 1, Colors.getColor(Colors.getColor(150)));
            }
            h += 5;
            y += 10;
        }
    }

    private boolean shouldHide(Module module) {
        ModuleData.Type type = module.getType();
        return isBlacklisted(module);
    }

    private boolean isBlacklisted(Module module) {
        Class<? extends Module> clazz = module.getClass();
        return clazz.equals(ChatCommands.class) || clazz.equals(TabGUI.class) || clazz.equals(ClickGui.class) || clazz.equals(Enabled.class) || module.isHidden();
    }

    private static void drawPotionStatus(ScaledResolution sr)
    {
        List<PotionEffect> potions = new ArrayList<>();
        for(Object o : mc.thePlayer.getActivePotionEffects())
            potions.add((PotionEffect)o);
        potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj.getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

        float pY = (mc.currentScreen != null && mc.currentScreen instanceof GuiChat) ? -15 : -2;
        for (PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            String PType = "";
            if (effect.getAmplifier() == 1) {
                name = name + " II";
            } else if (effect.getAmplifier() == 2) {
                name = name + " III";
            } else if (effect.getAmplifier() == 3) {
                name = name + " IV";
            }
            if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                PType = PType + "\2476 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "\247c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + "\2477 " + Potion.getDurationString(effect);
            }
            mc.fontRendererObj.drawStringWithShadow(name,
                    sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType),
                    sr.getScaledHeight() - 9 + pY, potion.getLiquidColor());
            mc.fontRendererObj.drawStringWithShadow(PType,
                    sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType),
                    sr.getScaledHeight() - 9 + pY, -1);
            pY -= 9;
        }
    }

}
