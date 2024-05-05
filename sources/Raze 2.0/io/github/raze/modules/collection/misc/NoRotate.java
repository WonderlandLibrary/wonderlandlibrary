package io.github.raze.modules.collection.misc;

import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends AbstractModule {

    public NoRotate() {
        super("NoRotate", "Modifies the rotation packets.", ModuleCategory.MISC);
    }

    @Listen
    public void onPacketReceive(EventPacketReceive event) {
        if(event.getPacket() instanceof S08PacketPlayerPosLook && mc.theWorld != null) {
            ((S08PacketPlayerPosLook) event.getPacket()).yaw = mc.thePlayer.rotationYaw;
            ((S08PacketPlayerPosLook) event.getPacket()).pitch = mc.thePlayer.rotationPitch;
        }
    }

}