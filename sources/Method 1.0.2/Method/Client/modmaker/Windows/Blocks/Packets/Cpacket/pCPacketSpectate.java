package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketSpectate;

import java.util.UUID;

public class pCPacketSpectate extends Block {
    public pCPacketSpectate() {
        super( "Spectate", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketSpectate);
        this.packetclasses.add(CPacketSpectate.class);
        this.description = "Sends a packet to the server to spectate a player.";
    }
    public static class SendPacketSpectate extends Block {
        public SendPacketSpectate() {
            super( "SendPacketSpectate", MainBlockType.Default, Tabs.Sub, Headers.CPacketSpectate, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake UUID:";

            this.typesAccepted.add(typesCollapse(MainBlockType.UUID));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // UUid uuid
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketSpectate((UUID) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
