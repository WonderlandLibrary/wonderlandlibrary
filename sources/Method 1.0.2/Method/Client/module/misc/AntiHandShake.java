
package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import com.google.common.eventbus.Subscribe;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.lwjgl.input.Keyboard;

public class AntiHandShake extends Module {

    /////////////////////

    public AntiHandShake() {
        super("AntiHandShake", Keyboard.KEY_NONE, Category.MISC, "No Mod List sent on login");
    }


    @Override
    public boolean onDisablePacket(Object packet, Connection.Side side) {
        return cancelHandshake(packet, side);
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        return cancelHandshake(packet, side);
    }


    private boolean cancelHandshake(Object packet, Connection.Side side) {
        if ((packet instanceof CPacketResourcePackStatus)) {
            ((CPacketResourcePackStatus) packet).action = CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
        }
        if (side == Connection.Side.OUT) {
            if (packet instanceof FMLProxyPacket && !mc.isSingleplayer()) {
                return false;
            }
            if (packet instanceof CPacketCustomPayload && !mc.isSingleplayer()) {
                final CPacketCustomPayload packet2 = (CPacketCustomPayload) packet;
                if (packet2.getChannelName().equals("MC|Brand")) {
                    packet2.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
                }

            }
        }
        return true;
    }


}
