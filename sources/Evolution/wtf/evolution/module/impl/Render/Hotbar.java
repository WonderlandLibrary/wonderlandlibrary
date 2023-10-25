package wtf.evolution.module.impl.Render;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "Hotbar", type = Category.Render)
public class Hotbar extends Module {

    @EventTarget
    public void onRender(EventDisplay e) {
        ScaleUtil.scale_pre();
        double blockpersecord = Math.hypot(mc.player.prevPosX - mc.player.posX, mc.player.prevPosZ - mc.player.posZ) * 20;
        double height = mc.currentScreen instanceof GuiChat ? 13 : 0;
        Fonts.pix.drawStringWithShadow(String.format("bps: " + "%.2f", blockpersecord), 1, ScaleUtil.calc(e.sr.getScaledHeight()) - 3 - height, 0xFFFFFF);
        Fonts.pix.drawStringWithShadow(String.format("tps: " + "%.1f", getTps()), 1, ScaleUtil.calc(e.sr.getScaledHeight()) - 9 - height, 0xFFFFFF);
        ScaleUtil.scale_post();
    }

    private float tps;

    private double lastTimePacketReceived;

    public float getTps() {
        return tps;
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            float tmp = (float) (calculateTps(System.currentTimeMillis() - lastTimePacketReceived) * 100);
            tps = tmp / 100f;
            lastTimePacketReceived = System.currentTimeMillis();
        }

    }

    private double calculateTps(double n) {
        return (20.0 / Math.max((n - 1000.0) / (500.0), 1.0));
    }

}
