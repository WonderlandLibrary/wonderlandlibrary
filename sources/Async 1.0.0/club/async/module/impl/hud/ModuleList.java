package club.async.module.impl.hud;

import club.async.Async;
import club.async.event.impl.Event2D;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.util.ColorUtil;
import club.async.util.TimeUtil;
import net.minecraft.client.gui.Gui;
import rip.hippo.lwjeb.annotation.Handler;

import java.awt.Color;
import java.util.Comparator;

@ModuleInfo(name = "ModuleList", description = "Enabled module list", category = Category.HUD)
public class ModuleList extends Module {

    private final TimeUtil timeUtil = new TimeUtil();

    @Handler
    public final void on2D(Event2D event2D) {
        if (timeUtil.hasTimePassed(2000)) {
            Async.INSTANCE.getModuleManager().getModuleListMods().sort(new ModuleComparator());
            timeUtil.reset();
        }

        double offset = 3;
        int index = 0;
        for (Module module : Async.INSTANCE.getModuleManager().getModuleListMods())
        {
            if (module.isDisabled() || module == this)
                continue;

            Gui.drawRect(event2D.getScaledResolution().getScaledWidth() - Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(module.getDisplayName()) - 3, offset - 2, event2D.getScaledResolution().getScaledWidth(), offset + 11, new Color(1,1,1,180));
            Gui.drawRect(event2D.getScaledResolution().getScaledWidth() - 2, offset - 2, event2D.getScaledResolution().getScaledWidth(), offset + 11, ColorUtil.getMainColor().darker());
            Async.INSTANCE.getFontManager().getFont("Arial 20").drawString(module.getDisplayName(), event2D.getScaledResolution().getScaledWidth() - 1 - Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(module.getDisplayName()), offset, ColorUtil.getGradientOffset(ColorUtil.getMainColor().brighter(),ColorUtil.getMainColor().darker(), index * 10.5D));
            offset += 13;
            index++;
        }
    }

    public static class ModuleComparator implements Comparator<Module> {
        public int compare(Module arg0, Module arg1) {
            return Float.compare(Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(arg1.getDisplayName()), Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(arg0.getDisplayName()));
        }
    }

}
