package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.play.server.SPacketMultiBlockChange;

public class pSPacketMultiBlockChange extends Block {
    public pSPacketMultiBlockChange() {
        super( "MultiBlockChange", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketMultiBlockChange);
        this.packetclasses.add(SPacketMultiBlockChange.class);
    }


}
