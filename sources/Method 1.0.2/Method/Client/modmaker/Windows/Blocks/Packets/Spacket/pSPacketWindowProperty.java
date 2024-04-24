package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketWindowProperty;

public class pSPacketWindowProperty extends Block {
    public pSPacketWindowProperty() {
        super( "WindowProperty", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketWindowProperty);
        this.packetclasses.add(SPacketWindowProperty.class);
    }

    public static class getWindowIdrty extends Block {
        public getWindowIdrty() {
            super( "getWindowIdrty", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketWindowProperty) event).getWindowId();
        }
    }

    public static class getProperty extends Block {
        public getProperty() {
            super( "getProperty", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketWindowProperty) event).getProperty();
        }
    }

    public static class getValuerty extends Block {
        public getValuerty() {
            super( "getValuerty", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketWindowProperty) event).getValue();
        }
    }

    public static class setWindowIdrty extends Block {
        public setWindowIdrty() {
            super( "setWindowIdrty", MainBlockType.Default, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketWindowProperty) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setProperty extends Block {
        public setProperty() {
            super( "setProperty", MainBlockType.Default, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketWindowProperty) event).property = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setValue extends Block {
        public setValue() {
            super( "setValue", MainBlockType.Default, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowProperty);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketWindowProperty) event).value = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
