package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.status.server.SPacketServerInfo;

public class pSPacketServerInfo extends Block {
    public pSPacketServerInfo() {
        super( "ServerInfo", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketServerInfo);
        this.packetclasses.add(SPacketServerInfo.class);
    }

}
