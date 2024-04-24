package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketResourcePackStatus;

public class pCPacketResourcePackStatus extends Block {
    public pCPacketResourcePackStatus() {
        super("ResourcePackStatus", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketResourcePackStatus);
        this.packetclasses.add(CPacketResourcePackStatus.class);
        this.description = "Sends the status of the resource pack to the server.";
    }

    public static class action extends Block {
        public action() {
            super("action", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketResourcePackStatus);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketResourcePackStatus) event).action;

        }
    }

    public static class SendResourcePackStatus extends Block {
        public SendResourcePackStatus() {
            super("SendResourcePackStatus", MainBlockType.Default, Tabs.Sub, Headers.CPacketResourcePackStatus, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake action:";
            this.typesAccepted.add(typesCollapse(MainBlockType.PackStatus));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // CPacketResourcePackStatus.Action action
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketResourcePackStatus((CPacketResourcePackStatus.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
