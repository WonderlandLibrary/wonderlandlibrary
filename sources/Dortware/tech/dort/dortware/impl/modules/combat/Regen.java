package tech.dort.dortware.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.networking.PacketUtil;

public class Regen extends Module {

    private final NumberValue packets = new NumberValue("Packets", this, 20, 5, 100, true);
    private final NumberValue ticks = new NumberValue("Ticks", this, 1, 1, 20, true);

    public Regen(ModuleData moduleData) {
        super(moduleData);
        register(packets, ticks);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() - 1 && mc.thePlayer.ticksExisted % ticks.getValue() == 0) {
            for (int i = 0; i < packets.getValue().intValue(); ++i) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            }
        }
    }
}
