package rosegoldclient.features;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.utils.FontUtils;

public class Watermark {
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if(!Main.configFile.watermark) return;
        String str = "§bPowered by: §aRoseGoldClient";
        FontUtils.drawString(str, Main.mc.displayWidth / 4 - FontUtils.getStringWidth(str) / 2, (int) (Main.mc.displayHeight * 0.1), true);
    }
}
