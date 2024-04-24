package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketClientStatus;

public class pCPacketClientStatus extends Block {
    public pCPacketClientStatus() {
        super( "pCPacketClientStatus", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketClientStatus);
        this.packetclasses.add(CPacketClientStatus.class);
        this.description = "Sends a client status packet to the server.";
    }

    public static class getStatus extends Block {
        public getStatus() {
            super( "getStatus", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientStatus);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientStatus) event).getStatus();
        }
    }
    public static class SendCPacketClientStatus extends Block {
        public SendCPacketClientStatus() {
            super( "SendCPacketClientStatus", MainBlockType.Default, Tabs.Sub, Headers.CPacketClientStatus, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send ClientStatus ClientStatus:";
            this.typesAccepted.add(typesCollapse(MainBlockType.ClientStatus));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // String address, int port, EnumConnectionState state, boolean addFMLMarker
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClientStatus((CPacketClientStatus.State) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
