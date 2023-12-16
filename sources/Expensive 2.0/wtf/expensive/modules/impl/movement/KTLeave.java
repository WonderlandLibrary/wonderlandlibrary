package wtf.expensive.modules.impl.movement;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SConfirmTransactionPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.Heightmap;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.ServerUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FunctionAnnotation(name = "KTLeave", type = Type.Movement)
public class KTLeave extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            if (ServerUtil.isHW()) {
                if (e.getPacket() instanceof SEntityVelocityPacket p) {
                    if (p.getEntityID() == mc.player.getEntityId()) {
                        leaveHoly();
                    }
                }
            } else {
                ClientUtil.sendMesage(TextFormatting.RED + "Данная функция работает только на Holyworld!");
                toggle();
            }
        }

    }


    private void leaveHoly() {
        float x = (float) mc.player.getPosX() + ThreadLocalRandom.current().nextFloat(-50, 50);
        float z = (float) mc.player.getPosZ() + ThreadLocalRandom.current().nextFloat(-50, 50);

        float y = mc.world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) x, (int) z) - 1;
        ClientUtil.sendMesage("Телепортирую...");

        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), -180, 0, false));
        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), -180, 0, false));

        for (int i = 0; i <= 10; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
        }
        toggle();
    }
}
