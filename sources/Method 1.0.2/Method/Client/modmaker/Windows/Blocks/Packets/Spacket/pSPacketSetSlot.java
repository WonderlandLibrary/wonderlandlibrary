package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

public class pSPacketSetSlot extends Block {
    public pSPacketSetSlot() {
        super( "SetSlot", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSetSlot);
        this.packetclasses.add(SPacketSetSlot.class);
    }

    public static class getStacklot extends Block {
        public getStacklot() {
            super( "getStacklot", MainBlockType.ItemStack, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetSlot);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).getStack();

        }
    }

    public static class getSlot extends Block {
        public getSlot() {
            super( "getSlot", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetSlot);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).getSlot();

        }
    }

    public static class getWindowIdlot extends Block {
        public getWindowIdlot() {
            super( "getWindowIdlot", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetSlot);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).getWindowId();

        }
    }

    public static class setSlot extends Block {
        public setSlot() {
            super( "setSlot", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetSlot, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).slot = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);

        }
    }

    public static class setWindowIdlot extends Block {
        public setWindowIdlot() {
            super( "setWindowIdlot", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetSlot, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);

        }
    }

    public static class setStack extends Block {
        public setStack() {
            super( "setStack", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetSlot, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetSlot) event).item = (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);

        }
    }
}
