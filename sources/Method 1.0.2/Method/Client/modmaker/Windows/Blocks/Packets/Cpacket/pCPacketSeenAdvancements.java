package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.play.client.CPacketSeenAdvancements;

public class pCPacketSeenAdvancements extends Block {
    public pCPacketSeenAdvancements() {
        super( "SeenAdvancements", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketSeenAdvancements);
        this.packetclasses.add(CPacketSeenAdvancements.class);
        this.description = "Sends the seen advancements to the server.";
    }


}
