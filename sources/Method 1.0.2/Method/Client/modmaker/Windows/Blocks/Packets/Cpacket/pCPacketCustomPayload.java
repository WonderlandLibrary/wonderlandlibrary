package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class pCPacketCustomPayload extends Block {
    public pCPacketCustomPayload() {
        super( "CCustomPayload", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketCustomPayload);
        this.packetclasses.add(CPacketCustomPayload.class);
        this.description = "Sends a custom payload to the server.";
    }


    public static class getChannelName extends Block {
        public getChannelName() {
            super( "getChannelName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketCustomPayload);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketCustomPayload) event).getChannelName();

        }
    }

    public static class SendgetChannelName extends Block {
        public SendgetChannelName() {
            super( "SendgetChannelName", MainBlockType.Default, Tabs.Sub, Headers.CPacketCustomPayload, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake channelIn:";
            this.words[1] = "PacketBuffer:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.Wild));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // String channelIn, PacketBuffer bufIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (PacketBuffer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);

        }
    }
}
