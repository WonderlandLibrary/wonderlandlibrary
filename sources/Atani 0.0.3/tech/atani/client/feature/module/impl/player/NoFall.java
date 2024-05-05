package tech.atani.client.feature.module.impl.player;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.network.play.client.C03PacketPlayer;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import com.google.common.base.Supplier;

@Native
@ModuleData(name = "NoFall", description = "Reduces fall damage", category = Category.PLAYER)
public class NoFall extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[] {"Edit", "Smart", "Vulcan", "Verus"}),
            vulcanMode = new StringBoxValue("Vulcan Mode", "Which mode will the vulcan mode use?", this, new String[] {"Instant Motion"}, new Supplier[] {() -> mode.is("Vulcan")});
    private final CheckBoxValue modulo = new CheckBoxValue("Modulo", "Set on ground only every 3 blocks?", this, true, new Supplier[] {() -> mode.is("Edit")});

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer != null && mc.theWorld != null) {
            float modulo =  mc.thePlayer.fallDistance % 3;
            boolean correctModulo = modulo < 1f && mc.thePlayer.fallDistance > 3;
            boolean editGround = this.modulo.getValue() ? correctModulo : mc.thePlayer.fallDistance > 3;

            switch(mode.getValue()) {
                case "Edit":
                    if(packetEvent.getPacket() instanceof C03PacketPlayer && editGround) {
                        ((C03PacketPlayer) packetEvent.getPacket()).setOnGround(true);
                    }
                    break;
                case "Smart":
                    if (mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3f && packetEvent.getPacket() instanceof C03PacketPlayer) {
                        ((C03PacketPlayer) packetEvent.getPacket()).setOnGround(true);
                        mc.thePlayer.fallDistance = 0f;
                    }
                    break;
                case "Vulcan":
                    if(packetEvent.getPacket() instanceof C03PacketPlayer) {
                        C03PacketPlayer packet = (C03PacketPlayer) packetEvent.getPacket();
                        if(correctModulo) {
                            switch(this.vulcanMode.getValue()) {
                            case "Instant Motion":
                                mc.thePlayer.motionY = -500;
                                packet.setOnGround(true);
                            	break;
                            }
                        } else {
                        	mc.timer.timerSpeed = 1f;
                        }
                    }
                    break;
                case "Verus":
                    if(mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3) {
                        mc.thePlayer.motionY = 0.0;
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                        sendPacketUnlogged(new C03PacketPlayer(true));
                    }
                    break;

            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
