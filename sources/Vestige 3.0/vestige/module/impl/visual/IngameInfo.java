package vestige.module.impl.visual;

import net.minecraft.client.gui.ScaledResolution;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.render.FontUtil;

public class IngameInfo extends Module {

    private final ModeSetting font = FontUtil.getFontSetting();
    private final BooleanSetting bps = new BooleanSetting("BPS", true);
    private final BooleanSetting balance = new BooleanSetting("Balance", true);

    public IngameInfo() {
        super("Ingame Info", Category.VISUAL);
        this.addSettings(font, bps, balance);
    }

    @Listener
    public void onRender(RenderEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);

        if(mc.gameSettings.showDebugInfo) return;

        float x = 4;
        float y = sr.getScaledHeight() - 13;

        if(balance.isEnabled()) {
            FontUtil.drawStringWithShadow(font.getMode(), "Balance : " + Vestige.instance.getBalanceHandler().getBalanceInMS(), x, y, -1);

            y -= 10;
        }

        if(bps.isEnabled()) {
            double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.timerSpeed;
            double bps = bpt * 20;

            double roundedBPS = Math.round(bps * 100) / 100.0;

            FontUtil.drawStringWithShadow(font.getMode(), "BPS : " + roundedBPS, x, y, -1);
        }
    }

}
