package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketChangeGameState;

public class pSPacketChangeGameState extends Block {
    public pSPacketChangeGameState() {
        super( "ChangeGameState", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketChangeGameState);
        this.packetclasses.add(SPacketChangeGameState.class);
    }

    public static class getGameState extends Block {
        public getGameState() {
            super( "getGameState", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketChangeGameState);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketChangeGameState) event).getGameState();

        }
    }

    public static class getValue extends Block {
        public getValue() {
            super( "getValue", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketChangeGameState);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketChangeGameState) event).getValue();

        }
    }

    public static class setstate extends Block {
        public setstate() {
            super( "setstate", MainBlockType.Default, Tabs.Sub, Headers.SPacketChangeGameState, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChangeGameState) event).state = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setvalue extends Block {
        public setvalue() {
            super( "setvalue", MainBlockType.Default, Tabs.Sub, Headers.SPacketChangeGameState, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketChangeGameState) event).value = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
