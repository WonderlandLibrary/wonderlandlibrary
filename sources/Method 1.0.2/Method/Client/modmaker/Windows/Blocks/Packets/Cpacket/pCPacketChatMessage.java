package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;

public class pCPacketChatMessage extends Block {
    public pCPacketChatMessage() {
        super( "ChatMessage", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketChatMessage);
        this.packetclasses.add(CPacketChatMessage.class);
        this.description = "Sends a chat message to the server" ;
    }

    public static class getMessage extends Block {
        public getMessage() {
            super( "getMessage", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketChatMessage);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketChatMessage) event).getMessage();
        }
    }

    public static class SendpCPacketChatMessage extends Block {
        public SendpCPacketChatMessage() {
            super( "SendpCPacketChatMessage", MainBlockType.Default, Tabs.Sub, Headers.CPacketChatMessage, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send ChatMessage Message:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
