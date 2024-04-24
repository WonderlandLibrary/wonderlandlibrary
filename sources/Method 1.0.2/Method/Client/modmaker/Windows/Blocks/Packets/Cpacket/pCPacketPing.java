package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.status.client.CPacketPing;

public class pCPacketPing extends Block {
    public pCPacketPing() {
        super( "Ping", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPing);
        this.packetclasses.add(CPacketPing.class);
        this.description = "Sends a ping packet to the server.";
    }

    public static class getClientTime extends Block {
        public getClientTime() {
            super( "getClientTime", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketPing);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPing) event).getClientTime();

        }
    }
    public static class SendPing extends Block {
        public SendPing() {
            super( "SendPing", MainBlockType.Default, Tabs.Sub, Headers.CPacketPing, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send SendPing clientTime:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // long clientTimeIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPing((long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
