package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketConfirmTransaction;

public class pSPacketConfirmTransaction extends Block {
    public pSPacketConfirmTransaction() {
        super( "ServerConfirmTransaction", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketConfirmTransaction);
        this.packetclasses.add(SPacketConfirmTransaction.class);
    }

    public static class pSPgetWindowId extends Block {
        public pSPgetWindowId() {
            super( "pSPgetWindowId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketConfirmTransaction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketConfirmTransaction) event).getWindowId();
        }
    }

    public static class getActionNumber extends Block {
        public getActionNumber() {
            super( "getActionNumber", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketConfirmTransaction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketConfirmTransaction) event).getActionNumber();
        }
    }

    public static class wasAccepted extends Block {
        public wasAccepted() {
            super( "wasAccepted", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketConfirmTransaction);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketConfirmTransaction) event).wasAccepted();
        }
    }

    public static class setWindowId extends Block {
        public setWindowId() {
            super( "setWindowId", MainBlockType.Default, Tabs.Sub, Headers.SPacketConfirmTransaction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketConfirmTransaction) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setActionNumber extends Block {
        public setActionNumber() {
            super( "setActionNumber", MainBlockType.Default, Tabs.Sub, Headers.SPacketConfirmTransaction, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketConfirmTransaction) event).actionNumber = (short) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setWasAccepted extends Block {
        public setWasAccepted() {
            super( "setWasAccepted", MainBlockType.Default, Tabs.Sub, Headers.SPacketConfirmTransaction, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketConfirmTransaction) event).accepted = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
