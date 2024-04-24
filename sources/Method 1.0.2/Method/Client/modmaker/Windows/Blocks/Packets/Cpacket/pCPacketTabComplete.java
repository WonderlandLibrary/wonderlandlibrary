package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;

public class pCPacketTabComplete extends Block {
    public pCPacketTabComplete() {
        super( "ClientTabComplete", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketTabComplete);
        this.packetclasses.add(CPacketTabComplete.class);
        this.description = "Sends a tab complete packet to the server.";
    }

    public static class getMessageete extends Block {
        public getMessageete() {
            super( "getMessageete", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketTabComplete);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketTabComplete) event).getMessage();

        }
    }

    public static class getTargetBlock extends Block {
        public getTargetBlock() {
            super( "getTargetBlock", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.CPacketTabComplete);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketTabComplete) event).getTargetBlock();

        }
    }

    public static class hasTargetBlock extends Block {
        public hasTargetBlock() {
            super( "hasTargetBlock", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketTabComplete);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketTabComplete) event).hasTargetBlock();

        }
    }

    public static class SendTabComplete extends Block {
        public SendTabComplete() {
            super( "SendTabComplete", MainBlockType.Default, Tabs.Sub, Headers.CPacketTabComplete, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake message:";
            this.words[1] = "targetBlock:";
            this.words[2] = "hasTargetBlock:";

            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // String message, @Nullable BlockPos targetBlock, boolean hasTargetBlock
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketTabComplete((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
