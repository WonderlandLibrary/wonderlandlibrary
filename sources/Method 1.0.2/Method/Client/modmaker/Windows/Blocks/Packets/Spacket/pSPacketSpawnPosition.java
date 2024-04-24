package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.util.math.BlockPos;

public class pSPacketSpawnPosition extends Block {
    public pSPacketSpawnPosition() {
        super( "SpawnPosition", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnPosition);
        this.packetclasses.add(SPacketSpawnPosition.class);
    }

    public static class getSpawnPos extends Block {
        public getSpawnPos() {
            super( "getSpawnPos", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPosition);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPosition) event).getSpawnPos();

        }
    }

    public static class setspawnBlockPos extends Block {
        public setspawnBlockPos() {
            super( "setspawnBlockPos", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPosition, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPosition) event).spawnBlockPos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
