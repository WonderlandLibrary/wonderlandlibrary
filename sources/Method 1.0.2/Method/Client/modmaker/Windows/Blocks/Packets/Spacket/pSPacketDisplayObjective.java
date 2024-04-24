package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketDisplayObjective;

public class pSPacketDisplayObjective extends Block {
    public pSPacketDisplayObjective() {
        super( "DisplayObjective", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketDisplayObjective);
        this.packetclasses.add(SPacketDisplayObjective.class);
    }

    public static class getPositionive extends Block {
        public getPositionive() {
            super( "getPositionive", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketDisplayObjective);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketDisplayObjective) event).getPosition();
        }
    }
    public static class getName extends Block {
        public getName() {
            super( "getName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketDisplayObjective);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketDisplayObjective) event).getName();
        }
    }

    public static class setposition extends Block {
        public setposition() {
            super( "setposition", MainBlockType.Default, Tabs.Sub,Headers.SPacketDisplayObjective, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketDisplayObjective) event).position = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setscoreName extends Block {
        public setscoreName() {
            super( "setscoreName", MainBlockType.Default, Tabs.Sub,Headers.SPacketDisplayObjective, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketDisplayObjective) event).scoreName = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
