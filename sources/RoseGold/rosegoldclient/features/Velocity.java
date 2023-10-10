package rosegoldclient.features;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import rosegoldclient.Main;

import java.lang.reflect.Field;

public class Velocity extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet && msg.getClass().getName().contains("SPacketEntityVelocity")) {
            SPacketEntityVelocity packet = (SPacketEntityVelocity)msg;
            Field entityID = packet.getClass().getDeclaredField("field_149417_a");
            Field entityX = packet.getClass().getDeclaredField("field_149415_b");
            Field entityY = packet.getClass().getDeclaredField("field_149416_c");
            Field entityZ = packet.getClass().getDeclaredField("field_149414_d");
            entityX.setAccessible(true);
            entityY.setAccessible(true);
            entityZ.setAccessible(true);
            entityID.setAccessible(true);
            int reflectID = (Integer)entityID.get(packet);
            if (reflectID == Main.mc.player.getEntityId()) {
                int velocityX = (int)((double) (Integer) entityX.get(packet) * Main.configFile.velocityX);
                int velocityY = (int)((double) (Integer) entityY.get(packet) * Main.configFile.velocityY);
                int velocityZ = (int)((double) (Integer) entityZ.get(packet) * Main.configFile.velocityZ);
                entityX.set(packet, velocityX);
                entityY.set(packet, velocityY);
                entityZ.set(packet, velocityZ);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }
}
