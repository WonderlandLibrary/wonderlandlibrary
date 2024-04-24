package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class pCPacketHeldItemChange extends Block {
    public pCPacketHeldItemChange() {
        super( "ClientHeldItemChange", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketHeldItemChange);
        this.packetclasses.add(CPacketHeldItemChange.class);
        this.description = "Sends a packet to change the currently held item";
    }

    public static class getSlotIdnge extends Block {
        public getSlotIdnge() {
            super( "getSlotIdnge", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketHeldItemChange);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketHeldItemChange) event).getSlotId();

        }
    }

    public static class SendHeldItemChange extends Block {
        public SendHeldItemChange() {
            super( "SendHeldItemChange", MainBlockType.Default, Tabs.Sub, Headers.CPacketHeldItemChange, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake slotId:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int slotIdIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
