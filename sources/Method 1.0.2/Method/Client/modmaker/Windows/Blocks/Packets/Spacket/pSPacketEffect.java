package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.util.math.BlockPos;

public class pSPacketEffect extends Block {
    public pSPacketEffect() {
        super( "Effect", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEffect);
        this.packetclasses.add(SPacketEffect.class);
    }

    public static class isSoundServerwide extends Block {
        public isSoundServerwide() {
            super( "isSoundServerwide", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEffect);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEffect) event).isSoundServerwide();

        }
    }

    public static class getSoundPos extends Block {
        public getSoundPos() {
            super( "getSoundPos", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketEffect);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEffect) event).getSoundPos();

        }
    }

    public static class getSoundData extends Block {
        public getSoundData() {
            super( "getSoundData", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEffect) event).getSoundData();

        }
    }

    public static class getSoundType extends Block {
        public getSoundType() {
            super( "getSoundType", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEffect) event).getSoundType();

        }
    }

    public static class setSoundPos extends Block {
        public setSoundPos() {
            super( "setSoundPos", MainBlockType.Default, Tabs.Sub,Headers.SPacketEffect, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEffect) event).soundPos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setSoundData extends Block {
        public setSoundData() {
            super( "setSoundData", MainBlockType.Default, Tabs.Sub,Headers.SPacketEffect, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEffect) event).soundData = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setSoundType extends Block {
        public setSoundType() {
            super( "setSoundType", MainBlockType.Default, Tabs.Sub,Headers.SPacketEffect, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEffect) event).soundType = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setSoundServerwide extends Block {
        public setSoundServerwide() {
            super( "setSoundServerwide", MainBlockType.Default, Tabs.Sub,Headers.SPacketEffect, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEffect) event).serverWide = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
