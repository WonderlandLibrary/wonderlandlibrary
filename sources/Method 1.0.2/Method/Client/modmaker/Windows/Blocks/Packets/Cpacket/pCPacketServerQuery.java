package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.status.client.CPacketServerQuery;

import static Method.Client.modmaker.Headers.CPacketServerQuery;

public class pCPacketServerQuery extends Block {
    public pCPacketServerQuery() {
        super( "ServerQuery", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, CPacketServerQuery);
        this.packetclasses.add(CPacketServerQuery.class);
        this.description = "Sends a query to the server to get the server's MOTD, player count, and other information.";
    }
    public static class SendServerQuery extends Block {
        public SendServerQuery() {
            super( "SendServerQuery", MainBlockType.Default, Tabs.Sub, Headers.CPacketServerQuery, BlockObjects.Words);
            this.words[0] = "Send ServerQuery :";
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketServerQuery());
            super.runCode(dragableBlock, event);
        }
    }

}
