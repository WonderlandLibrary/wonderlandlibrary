package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class pSPacketSpawnPainting extends Block {
    public pSPacketSpawnPainting() {
        super( "SpawnPainting", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnPainting);
        this.packetclasses.add(SPacketSpawnPainting.class);
    }

    public static class getEntityIDing extends Block {
        public getEntityIDing() {
            super( "getEntityIDing", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPainting);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPainting) event).getEntityID();
        }
    }

    public static class getPositioning extends Block {
        public getPositioning() {
            super( "getPositioning", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPainting);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPainting) event).getPosition();
        }
    }

    public static class getFacinging extends Block {
        public getFacinging() {
            super( "getFacinging", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPainting);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPainting) event).getFacing();
        }
    }

    public static class getTitle extends Block {
        public getTitle() {
            super( "getTitle", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPainting);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPainting) event).getTitle();
        }
    }

    public static class setEntityIDing extends Block {
        public setEntityIDing() {
            super( "setEntityIDing", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPainting, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPainting) event).entityID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPositioning extends Block {
        public setPositioning() {
            super( "setPositioning", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPainting, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPainting) event).position = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFacing extends Block {
        public setFacing() {
            super( "setFacing", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPainting, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Facing));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPainting) event).facing = (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setTitle extends Block {
        public setTitle() {
            super( "setTitle", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPainting, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPainting) event).title = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
