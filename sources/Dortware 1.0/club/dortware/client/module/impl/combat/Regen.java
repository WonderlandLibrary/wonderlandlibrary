package club.dortware.client.module.impl.combat;

import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.util.impl.networking.PacketUtil;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleData(name = "Regeneration", category = ModuleCategory.COMBAT)
public class Regen extends Module {

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isCollidedVertically && mc.thePlayer.getHealth() < mc.thePlayer.getHealth() - 3) {
            for (int i = 0; i < 20; ++i) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
            }
        }
    }

}
