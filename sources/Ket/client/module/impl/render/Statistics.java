package client.module.impl.render;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.render.statistics.MushStatistics;
import client.value.impl.ModeValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

@ModuleInfo(name = "Statistics", description = "", category = Category.RENDER)
public class Statistics extends Module {

    public int kills, deaths, wins, gamesPlayed;

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new MushStatistics("Mush", this))
            .setDefault("Mush");

    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        final String s = EnumChatFormatting.GRAY + ": " + EnumChatFormatting.RESET;
        final String[] strings = {
                "Kills" + s + kills,
                "Deaths" + s + deaths,
                "Wins" + s + wins,
                "Games Played" + s + gamesPlayed
        };
        for (int i = 0; i < strings.length; i++) {
            Gui.drawRect(0, 11 + 11 * i, mc.fontRendererObj.getStringWidth(strings[i]) + 3, 11 + 11 * i + 11, Integer.MIN_VALUE);
            mc.fontRendererObj.drawString(strings[i], 2, 13 + 11 * i, getRainbow(i));
        }
    };

    private int getRainbow(int i) {
        return Color.getHSBColor((System.currentTimeMillis() - 83L * i) % 1000 / (float) 1000, 0.5F, 1).getRGB();
    }

}
