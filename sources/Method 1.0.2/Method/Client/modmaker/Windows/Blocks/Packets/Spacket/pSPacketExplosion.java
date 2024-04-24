package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketExplosion;

import java.util.ArrayList;
import java.util.Arrays;

public class pSPacketExplosion extends Block {
    public pSPacketExplosion() {
        super( "Explosion", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketExplosion);
        this.packetclasses.add(SPacketExplosion.class);
    }

    public static class getXion extends Block {
        public getXion() {
            super( "getXion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getX();

        }
    }

    public static class getYion extends Block {
        public getYion() {
            super( "getYion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getY();

        }
    }

    public static class getZion extends Block {
        public getZion() {
            super( "getZion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getZ();

        }
    }


    public static class getMotionXion extends Block {
        public getMotionXion() {
            super( "getMotionXion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getMotionX();

        }
    }

    public static class getMotionYion extends Block {
        public getMotionYion() {
            super( "getMotionYion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getMotionY();

        }
    }

    public static class getMotionZion extends Block {
        public getMotionZion() {
            super( "getMotionZion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getMotionZ();

        }
    }

    public static class getStrength extends Block {
        public getStrength() {
            super( "getStrength", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketExplosion) event).getStrength();

        }
    }

    public static class BlockPositions extends Block {
        public BlockPositions() {
            super( "BlockPositions", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketExplosion);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return new ArrayList<>(Arrays.asList(((SPacketExplosion) event).getAffectedBlockPositions()));

        }
    }

    public static class setXion extends Block {
        public setXion() {
            super( "setXion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).posX = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYion extends Block {
        public setYion() {
            super( "setYion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).posY = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZion extends Block {
        public setZion() {
            super( "setZion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).posZ = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionXion extends Block {
        public setMotionXion() {
            super( "setMotionXion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).motionX = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionYion extends Block {
        public setMotionYion() {
            super( "setMotionYion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).motionY = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionZion extends Block {
        public setMotionZion() {
            super( "setMotionZion", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).motionZ = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setStrength extends Block {
        public setStrength() {
            super( "setStrength", MainBlockType.Default, Tabs.Sub, Headers.SPacketExplosion, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketExplosion) event).strength = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }


}
