package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketKeepAlive;

public class pCPacketKeepAlive extends Block {
    public pCPacketKeepAlive() {
        super( "ClientKeepAlive", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketKeepAlive);
        this.packetclasses.add(CPacketKeepAlive.class);
        this.description = "Sends a keep alive packet to the server.";
    }


    public static class getKey extends Block {
        public getKey() {
            super( "getKey", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketKeepAlive);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketKeepAlive) event).getKey();

        }
    }

    public static class SendKeepAlive extends Block {
        public SendKeepAlive() {
            super( "SendKeepAlive", MainBlockType.Default, Tabs.Sub, Headers.CPacketKeepAlive, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send KeepAlive id:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // long idIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketKeepAlive((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
