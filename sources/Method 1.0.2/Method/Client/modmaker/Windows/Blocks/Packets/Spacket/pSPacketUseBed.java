package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.util.math.BlockPos;

public class pSPacketUseBed extends Block {
    public pSPacketUseBed() {
        super( "UseBed", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUseBed);
        this.packetclasses.add(SPacketUseBed.class);
    }

    public static class getBedPosition extends Block {
        public getBedPosition() {
            super( "getBedPosition", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketUseBed);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUseBed) event).getBedPosition();

        }
    }

    public static class getPlayer extends Block {
        public getPlayer() {
            super( "getPlayer", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketUseBed);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUseBed) event).getPlayer(Minecraft.getMinecraft().world);

        }
    }

    public static class setPlayerID extends Block {
        public setPlayerID() {
            super( "setPlayerID", MainBlockType.Default, Tabs.Sub, Headers.SPacketUseBed, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUseBed) event).playerID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPosBed extends Block {
        public setPosBed() {
            super( "setPosBed", MainBlockType.Default, Tabs.Sub, Headers.SPacketUseBed, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUseBed) event).bedPos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
