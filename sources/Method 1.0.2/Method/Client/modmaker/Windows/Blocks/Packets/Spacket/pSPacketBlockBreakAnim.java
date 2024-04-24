package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;

public class pSPacketBlockBreakAnim extends Block {
    public pSPacketBlockBreakAnim() {
        super( "BlockBreakAnim", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketBlockBreakAnim);
        this.packetclasses.add(SPacketBlockBreakAnim.class);
    }

    public static class getBreakerId extends Block {
        public getBreakerId() {
            super( "getBreakerId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockBreakAnim);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockBreakAnim) event).getBreakerId();

        }
    }

    public static class getPositionnim extends Block {
        public getPositionnim() {
            super( "getPositionnim", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockBreakAnim);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockBreakAnim) event).getPosition();

        }
    }

    public static class getProgress extends Block {
        public getProgress() {
            super( "getProgress", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketBlockBreakAnim);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketBlockBreakAnim) event).getProgress();

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public static class setProgress extends Block {
        public setProgress() {
            super( "setProgress", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockBreakAnim, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockBreakAnim) event).progress = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPosition extends Block {
        public setPosition() {
            super( "setPosition", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockBreakAnim, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockBreakAnim) event).position = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setbreakerId extends Block {
        public setbreakerId() {
            super( "setbreakerId", MainBlockType.Default, Tabs.Sub, Headers.SPacketBlockBreakAnim, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketBlockBreakAnim) event).breakerId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
