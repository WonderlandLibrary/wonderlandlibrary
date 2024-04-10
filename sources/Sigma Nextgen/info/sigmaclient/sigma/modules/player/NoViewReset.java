package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoViewReset extends Module {

    public NoViewReset() {
        super("NoViewReset", Category.Player, "Dont set my rotation.");
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof SPlayerPositionLookPacket){
            lastRotation = new float[]{0, 0};
            lastRotation[0] = mc.player.rotationYaw;
            lastRotation[1] = mc.player.rotationPitch;
//            ((SPlayerPositionLookPacket) event.packet).yaw = mc.player.rotationYaw;
//            ((SPlayerPositionLookPacket) event.packet).pitch = mc.player.rotationPitch;
        }
        super.onPacketEvent(event);
    }
    float[] lastRotation = null;
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(lastRotation != null){
                mc.player.rotationYaw = lastRotation[0];
                mc.player.rotationPitch = lastRotation[1];
                lastRotation = null;
            }
        }
        super.onUpdateEvent(event);
    }
}
