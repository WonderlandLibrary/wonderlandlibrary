package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class pCPacketPlayerTryUseItemOnBlock extends Block {
    public pCPacketPlayerTryUseItemOnBlock() {
        super( "PlayerTryUseItemOnBlock", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        this.packetclasses.add(CPacketPlayerTryUseItemOnBlock.class);
        this.description = "Sends a packet to the server to use an item on a block";
    }

    public static class getPosock extends Block {
        public getPosock() {
            super( "getPosock", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getPos();

        }
    }

    public static class getDirection extends Block {
        public getDirection() {
            super( "getDirection", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getDirection();
        }
    }

    public static class pCPgetHand extends Block {
        public pCPgetHand() {
            super( "pCPgetHand", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getHand();

        }
    }

    public static class getFacingX extends Block {
        public getFacingX() {
            super( "getFacingX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getFacingX();

        }
    }

    public static class getFacingY extends Block {
        public getFacingY() {
            super( "getFacingY", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getFacingY();

        }
    }

    public static class getFacingZ extends Block {
        public getFacingZ() {
            super( "getFacingZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItemOnBlock);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItemOnBlock) event).getFacingZ();

        }
    }

    public static class SendUseItemOnBlock extends Block {
        public SendUseItemOnBlock() {
            super( "SendUseItemOnBlock", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayerTryUseItemOnBlock, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words,
                    BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send UseItemOnBlock BlockPos:";
            this.words[1] = "Facing:";
            this.words[2] = "EnumHand:";
            this.words[3] = "facingX:";
            this.words[4] = "facingY:";
            this.words[5] = "facingZ:";
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
            this.typesAccepted.add(typesCollapse(MainBlockType.Facing));
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // BlockPos blockPos, EnumFacing enumFacing, EnumHand enumHand, float facingX, float facingY, float facingZ
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event),
                    (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event), (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event),
                    (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
