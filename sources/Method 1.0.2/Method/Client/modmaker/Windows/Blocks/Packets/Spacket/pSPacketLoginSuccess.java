package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.login.server.SPacketLoginSuccess;

public class pSPacketLoginSuccess extends Block {
    public pSPacketLoginSuccess() {
        super( "LoginSuccess", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketLoginSuccess);
        this.packetclasses.add(SPacketLoginSuccess.class);
    }


}
