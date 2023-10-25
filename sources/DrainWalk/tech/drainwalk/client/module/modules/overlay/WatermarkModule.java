package tech.drainwalk.client.module.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.utility.render.RenderUtility;

import java.util.Objects;


public class WatermarkModule extends Module {

    private final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Coords", false), new MultiOptionValue("Speed", false));

    public WatermarkModule() {
        super("Watermark", Category.OVERLAY);
        register(widgets);
    }

    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {
        String coords = "XYZ:  " + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ();
        String bps = "BPS: " + String.format("%.2f", Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ) * (double) mc.timer.timerSpeed * 20.0D);

        // Watermark
        RenderUtility.drawRoundedShadow(3, 2f, FontManager.SEMI_BOLD_12.getStringWidth(DrainWalk.CLIENT_NAME + "    " + DrainWalk.RELEASE_TYPE + "    " + DrainWalk.USERNAME + "    fps: " + Minecraft.getDebugFPS() + 10), 29 / 2f, 8, 3, ClientColor.panel);
        RenderUtility.drawRoundedRect(3, 3, FontManager.SEMI_BOLD_12.getStringWidth(DrainWalk.CLIENT_NAME + "    " + DrainWalk.RELEASE_TYPE + "    " + DrainWalk.USERNAME + "    fps: " + Minecraft.getDebugFPS() + 10), 25 / 2f, 3, ClientColor.panel);
        FontManager.SEMI_BOLD_12.drawString(DrainWalk.CLIENT_NAME + "    " + DrainWalk.RELEASE_TYPE + "    " + DrainWalk.USERNAME + "    fps: " + Minecraft.getDebugFPS(), 6.5f, 8f, ClientColor.textMain);


        boolean coordsEnabled = widgets.isSelected("Coords") ;
        if (coordsEnabled) {
            RenderUtility.drawRoundedShadow(3, 17, FontManager.SEMI_BOLD_12.getStringWidth(coords + 10), 14, 8, 3, ClientColor.panelMain);
            RenderUtility.drawRoundedRect(3, 18, FontManager.SEMI_BOLD_12.getStringWidth(coords + 10), 12, 3, ClientColor.panelMain);
            FontManager.SEMI_BOLD_12.drawString(coords, 6.5f, 23, ClientColor.textMain);
        }


        if (widgets.isSelected("Speed")) {
            RenderUtility.drawRoundedShadow(3 + FontManager.SEMI_BOLD_12.getStringWidth(coordsEnabled ? coords + 100 : ""), 17, FontManager.SEMI_BOLD_12.getStringWidth(bps + 30) - .5f, 14, 8, 3, ClientColor.panelMain);
            RenderUtility.drawRoundedRect(3 + FontManager.SEMI_BOLD_12.getStringWidth(coordsEnabled ? coords + 100 : ""), 18, FontManager.SEMI_BOLD_12.getStringWidth(bps + 30)- .5f, 12, 3, ClientColor.panelMain);
            FontManager.SEMI_BOLD_12.drawString(bps, 6.5f+ FontManager.SEMI_BOLD_12.getStringWidth(coordsEnabled ? coords + 100 : ""), 23, ClientColor.textMain);
        }
    }

}