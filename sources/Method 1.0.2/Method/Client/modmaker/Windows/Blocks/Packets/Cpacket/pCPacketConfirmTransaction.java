package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTransaction;

public class pCPacketConfirmTransaction extends Block {
    public pCPacketConfirmTransaction() {
        super( "ClientConfirmTransaction", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketConfirmTransaction);
        this.packetclasses.add(CPacketConfirmTransaction.class);
        this.description = "Sends a confirm transaction packet to the server.";
    }

    public static class getWindowIdion extends Block {
        public getWindowIdion() {
            super( "getWindowIdion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketConfirmTransaction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketConfirmTransaction) event).getWindowId();
        }
    }

    public static class getUid extends Block {
        public getUid() {
            super( "getUid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketConfirmTransaction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketConfirmTransaction) event).getUid();
        }
    }

    public static class SendConfirmTransaction extends Block {
        public SendConfirmTransaction() {
            super( "SendConfirmTransaction", MainBlockType.Default, Tabs.Sub, Headers.CPacketConfirmTransaction, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Handshake windowId:";
            this.words[1] = "uid:";
            this.words[2] = "accepted:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int windowIdIn, short uidIn, boolean acceptedIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketConfirmTransaction((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (short) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
