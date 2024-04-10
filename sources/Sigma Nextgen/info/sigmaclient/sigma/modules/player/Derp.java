package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.RandomUtil;
import net.minecraft.util.math.MathHelper;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Derp extends Module {

    public Derp() {
        super("Derp", Category.Player, "Spin bot");
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            event.yaw = MathHelper.wrapAngleTo180_float(mc.player.lastReportedYaw + 80 + RandomUtil.nextFloat(
                    0, 2));
            event.pitch = 87 + RandomUtil.nextFloat(0, 2);
        }
        super.onUpdateEvent(event);
    }
}
