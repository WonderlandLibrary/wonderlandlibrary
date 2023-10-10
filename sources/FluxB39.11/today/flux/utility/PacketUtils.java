package today.flux.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * Created by John on 2017/05/18.
 */
public class PacketUtils {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static String getServerIP() {
        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().serverIP.toLowerCase();
    }

    public static boolean isSinglePlayer(){
        return mc.isIntegratedServerRunning();
    }

    public static void sendPacketNoEvent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
    }
}
