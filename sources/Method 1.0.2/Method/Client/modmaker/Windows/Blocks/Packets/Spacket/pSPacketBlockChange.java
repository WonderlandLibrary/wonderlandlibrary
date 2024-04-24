package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;

public class pSPacketBlockChange extends Block {
    public pSPacketBlockChange() {
        super( "BlockChange", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketBlockChange);
        this.packetclasses.add(SPacketBlockChange.class);
    }

    public static class getBlockPositionnge extends Block {
        public getBlockPositionnge() {
            super( "getBlockPositionnge", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockChange);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockChange) event).getBlockPosition();

        }
    }

    public static class getBlockState extends Block {
        public getBlockState() {
            super( "getBlockState", MainBlockType.IBlockState, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockChange);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockChange) event).getBlockState();

        }
    }

    public static class setblockState extends Block {
        public setblockState() {
            super( "setblockState", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockChange, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.IBlockState));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockChange) event).blockState = (IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setBlockPos extends Block {
        public setBlockPos() {
            super( "setBlockPos", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockChange, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockChange) event).blockPosition = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
