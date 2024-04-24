package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketChunkData;

public class pSPacketChunkData extends Block {
    public pSPacketChunkData() {
        super( "ChunkData", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketChunkData);
        this.packetclasses.add(SPacketChunkData.class);
    }

    public static class getChunkX extends Block {
        public getChunkX() {
            super( "getChunkX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketChunkData);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketChunkData) event).getChunkX();

        }
    }
    public static class getChunkZ extends Block {
        public getChunkZ() {
            super( "getChunkZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketChunkData);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketChunkData) event).getChunkZ();

        }
    }
    public static class getExtractedSize extends Block {
        public getExtractedSize() {
            super( "getExtractedSize", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketChunkData);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketChunkData) event).getExtractedSize();

        }
    }
    public static class isFullChunk extends Block {
        public isFullChunk() {
            super( "isFullChunk", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketChunkData);
        }
        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketChunkData) event).isFullChunk();
        }
    }
    public static class getTileEntityTags extends Block {
        public getTileEntityTags() {
            super( "getTileEntityTags", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketChunkData);
        }
        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketChunkData) event).getTileEntityTags();
        }
    }
    ////////////////////////////////////////////////////////////////////////////


    public static class setchunkX extends Block {
        public setchunkX() {
            super( "setchunkX", MainBlockType.Default, Tabs.Sub,Headers.SPacketChunkData, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChunkData) event).chunkX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setchunkZ extends Block {
        public setchunkZ() {
            super( "setchunkZ", MainBlockType.Default, Tabs.Sub,Headers.SPacketChunkData, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChunkData) event).chunkZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    
    public static class setavailableSections extends Block {
        public setavailableSections() {
            super( "setavailableSections", MainBlockType.Default, Tabs.Sub,Headers.SPacketChunkData, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChunkData) event).availableSections = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setfullChunk extends Block {
        public setfullChunk() {
            super( "setfullChunk", MainBlockType.Default, Tabs.Sub,Headers.SPacketChunkData, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChunkData) event).fullChunk = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
