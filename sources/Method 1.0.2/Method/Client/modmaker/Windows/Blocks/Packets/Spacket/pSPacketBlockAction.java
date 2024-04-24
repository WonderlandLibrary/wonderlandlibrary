package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.util.math.BlockPos;

public class pSPacketBlockAction extends Block {
    public pSPacketBlockAction() {
        super( "BlockAction", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketBlockAction);
        this.packetclasses.add(SPacketBlockAction.class);
    }

    public static class getBlockPosition extends Block {
        public getBlockPosition() {
            super( "getBlockPosition", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockAction);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockAction) event).getBlockPosition();

        }
    }

    public static class getinstrument extends Block {
        public getinstrument() {
            super( "getinstrument", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockAction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockAction) event).getData1();

        }
    }

    public static class getpitch extends Block {
        public getpitch() {
            super( "getpitch", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockAction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockAction) event).getData2();

        }
    }

    public static class getBlockType extends Block {
        public getBlockType() {
            super( "getBlockType", MainBlockType.Blocks, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockAction);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockAction) event).getBlockType();

        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class setBlockType extends Block {
        public setBlockType() {
            super( "setBlockType", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockAction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Blocks));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockAction) event).block = (net.minecraft.block.Block) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setpitch extends Block {
        public setpitch() {
            super( "setpitch", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockAction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockAction) event).pitch = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setinstrument extends Block {
        public setinstrument() {
            super( "setinstrument", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockAction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockAction) event).instrument = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setBlockPosition extends Block {
        public setBlockPosition() {
            super( "setBlockPosition", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockAction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockAction) event).blockPosition = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}

