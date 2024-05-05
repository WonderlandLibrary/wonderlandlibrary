package client.module.impl.render;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.Value;
import client.value.impl.ModeValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleInfo(name = "Array List", description = "Displays modules list", category = Category.RENDER)
public class ArrayList extends Module {

    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        //   GlStateManager.rotate(4,52,25,53);
        final String splitter = EnumChatFormatting.GRAY + " | " + EnumChatFormatting.RESET;
        final AtomicInteger i = new AtomicInteger();
        Client.INSTANCE.getModuleManager().getAll().stream().filter(Module::isEnabled).sorted(Comparator.comparingInt(o -> {
            String s = o.getInfo().name();
            for (final Value<?> value : o.getValues()) {
                if (value instanceof ModeValue) {
                    s += splitter + ((ModeValue) value).getValue().getName();
                    break;
                }
            }
            return -mc.fontRendererObj.getStringWidth(s);
        })).forEach(module -> {
            String s = module.getInfo().name();
            for (final Value<?> value : module.getValues()) {
                if (value instanceof ModeValue) {
                    s += splitter + ((ModeValue) value).getValue().getName();
                    break;
                }
            }
            final int j = event.getScaledResolution().getScaledWidth();
            final int k = mc.fontRendererObj.getStringWidth(s);
            Gui.drawRect(j - k - 3, 11 * i.get(), j, 11 * i.get() + 11, Integer.MIN_VALUE);
            mc.fontRendererObj.drawString(s, j - k - 1, 2 + 11 * i.get(), Color.GREEN.getRGB());
            i.getAndIncrement();
        });
    };

    private int getRainbow(int i) {
        return Color.getHSBColor((System.currentTimeMillis() - 83 * i) % 1000 / 1000f, 0.5f, 1).getRGB();
    }
}
