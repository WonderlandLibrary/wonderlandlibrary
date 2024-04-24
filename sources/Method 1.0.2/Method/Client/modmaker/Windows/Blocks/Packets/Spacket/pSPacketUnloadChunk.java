package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUnloadChunk;

public class pSPacketUnloadChunk extends Block {
    public pSPacketUnloadChunk() {
        super( "UnloadChunk", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUnloadChunk);
        this.packetclasses.add(SPacketUnloadChunk.class);
    }

    public static class getXunk extends Block {
        public getXunk() {
            super( "getXunk", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUnloadChunk);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUnloadChunk) event).getX();

        }
    }

    public static class getZunk extends Block {
        public getZunk() {
            super( "getZunk", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUnloadChunk);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUnloadChunk) event).getZ();

        }
    }

    public static class setChunkX extends Block {
        public setChunkX() {
            super( "setChunkX", MainBlockType.Default, Tabs.Sub, Headers.SPacketUnloadChunk, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUnloadChunk) event).x = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setChunkZ extends Block {
        public setChunkZ() {
            super( "setChunkZ", MainBlockType.Default, Tabs.Sub, Headers.SPacketUnloadChunk, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUnloadChunk) event).z = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
