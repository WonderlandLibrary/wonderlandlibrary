package club.async.module.impl.player;

import club.async.event.impl.EventPreUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "AntiVoid", description = "dumb fuck dont fall", category = Category.PLAYER)
public class AntiVoid extends Module {

    public NumberSetting dist = new NumberSetting("Falldistance", this, 1, 15, 10, 1);

    @Handler
    public void update(EventPreUpdate event) {
        if(PlayerUtil.checkVoid() && mc.thePlayer.fallDistance > dist.getFloat()) {
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ, mc.thePlayer.onGround));
        }
    }

}
