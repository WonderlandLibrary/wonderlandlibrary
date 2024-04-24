package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;

public class pSPacketSpawnExperienceOrb extends Block {
    public pSPacketSpawnExperienceOrb() {
        super( "SpawnExperienceOrb", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        this.packetclasses.add(SPacketSpawnExperienceOrb.class);
    }

    public static class getXOrb extends Block {
        public getXOrb() {
            super( "getXOrb", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnExperienceOrb) event).getX();
        }
    }

    public static class getYOrb extends Block {
        public getYOrb() {
            super( "getYOrb", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnExperienceOrb) event).getY();
        }
    }

    public static class getZOrb extends Block {
        public getZOrb() {
            super( "getZOrb", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnExperienceOrb) event).getZ();
        }
    }

    public static class getEntityIDOrb extends Block {
        public getEntityIDOrb() {
            super( "getEntityIDOrb", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnExperienceOrb) event).getEntityID();
        }
    }

    public static class getXPValue extends Block {
        public getXPValue() {
            super( "getXPValue", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnExperienceOrb);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnExperienceOrb) event).getXPValue();
        }
    }

    public static class setXOrb extends Block {
        public setXOrb() {
            super( "setXOrb", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnExperienceOrb, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnExperienceOrb) event).posX = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYOrb extends Block {
        public setYOrb() {
            super( "setYOrb", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnExperienceOrb, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnExperienceOrb) event).posY = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZOrb extends Block {
        public setZOrb() {
            super( "setZOrb", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnExperienceOrb, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnExperienceOrb) event).posZ = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIDOrb extends Block {
        public setEntityIDOrb() {
            super( "setEntityIDOrb", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnExperienceOrb, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnExperienceOrb) event).entityID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXPValue extends Block {
        public setXPValue() {
            super( "setXPValue", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnExperienceOrb, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnExperienceOrb) event).xpValue = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
