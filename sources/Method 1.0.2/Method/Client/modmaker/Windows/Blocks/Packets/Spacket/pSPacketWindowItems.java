package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketWindowItems;

import java.util.ArrayList;
import java.util.Collections;

public class pSPacketWindowItems extends Block {
    public pSPacketWindowItems() {
        super( "WindowItems", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketWindowItems);
        this.packetclasses.add(SPacketWindowItems.class);
    }

    public static class getWindowIdems extends Block {
        public getWindowIdems() {
            super( "getWindowIdems", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowItems);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketWindowItems) event).getWindowId();

        }
    }

    public static class getItemStacks extends Block {
        public getItemStacks() {
            super( "getItemStacks", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketWindowItems);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return new ArrayList<>(Collections.singletonList(((SPacketWindowItems) event).getItemStacks()));
        }
    }

    public static class setWindowIdems extends Block {
        public setWindowIdems() {
            super( "setWindowIdems", MainBlockType.Default, Tabs.Sub, Headers.SPacketWindowItems, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketWindowItems) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
