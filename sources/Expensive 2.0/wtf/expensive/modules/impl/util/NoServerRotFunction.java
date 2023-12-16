package wtf.expensive.modules.impl.util;

import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventTeleport;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.util.ClientUtil;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "No Server Rot", type = Type.Util)
public class NoServerRotFunction extends Function {
    private ModeSetting serverRotMode = new ModeSetting("Тип", "Обычный", "Обычный", "RW");

    public NoServerRotFunction() {
        addSettings(serverRotMode);
    }

    @Override
    public void onEvent(final Event event) {
        if (!serverRotMode.is("RW")) {
            if (event instanceof EventPacket packet) {
                if (packet.isReceivePacket()) {
                    if (packet.getPacket() instanceof SPlayerPositionLookPacket packet1) {
                        packet1.yaw = mc.player.rotationYaw;
                        packet1.pitch = mc.player.rotationPitch;
                    }
                }
            }
        }
    }
}
