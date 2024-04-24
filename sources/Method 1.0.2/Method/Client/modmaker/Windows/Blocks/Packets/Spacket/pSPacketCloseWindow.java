package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCloseWindow;

public class pSPacketCloseWindow extends Block {
    public pSPacketCloseWindow() {
        super( "ServerCloseWindow", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCloseWindow);
        this.packetclasses.add(SPacketCloseWindow.class);
    }

    public static class windowId extends Block {
        public windowId() {
            super( "windowId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCloseWindow);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCloseWindow) event).windowId;

        }
    }

    public static class setwindowId extends Block {
        public setwindowId() {
            super( "setwindowId", MainBlockType.Default, Tabs.Sub, Headers.SPacketCloseWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCloseWindow) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
