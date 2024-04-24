package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;

public class pSPacketOpenWindow extends Block {
    public pSPacketOpenWindow() {
        super( "OpenWindow", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketOpenWindow);
        this.packetclasses.add(SPacketOpenWindow.class);
    }

    public static class getWindowIddow extends Block {
        public getWindowIddow() {
            super( "getWindowIddow", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketOpenWindow);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketOpenWindow) event).getWindowId();

        }
    }

    public static class getEntityIddow extends Block {
        public getEntityIddow() {
            super( "getEntityIddow", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketOpenWindow);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketOpenWindow) event).getEntityId();

        }
    }

    public static class getSlotCount extends Block {
        public getSlotCount() {
            super( "getSlotCount", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketOpenWindow);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketOpenWindow) event).getSlotCount();

        }
    }

    public static class getGuiId extends Block {
        public getGuiId() {
            super( "getGuiId", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketOpenWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketOpenWindow) event).getGuiId();

        }
    }

    public static class getWindowTitle extends Block {
        public getWindowTitle() {
            super( "getWindowTitle", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketOpenWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketOpenWindow) event).getWindowTitle().toString();

        }
    }

    public static class setWindowIddow extends Block {
        public setWindowIddow() {
            super( "setWindowIddow", MainBlockType.Default, Tabs.Sub, Headers.SPacketOpenWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketOpenWindow) event).windowId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIddow extends Block {
        public setEntityIddow() {
            super( "setEntityIddow", MainBlockType.Default, Tabs.Sub, Headers.SPacketOpenWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketOpenWindow) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setSlotCount extends Block {
        public setSlotCount() {
            super( "setSlotCount", MainBlockType.Default, Tabs.Sub, Headers.SPacketOpenWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketOpenWindow) event).slotCount = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setWindowTitle extends Block {
        public setWindowTitle() {
            super( "setWindowTitle", MainBlockType.Default, Tabs.Sub, Headers.SPacketOpenWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketOpenWindow) event).windowTitle = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setinventoryType extends Block {
        public setinventoryType() {
            super( "setinventoryType", MainBlockType.Default, Tabs.Sub, Headers.SPacketOpenWindow, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketOpenWindow) event).inventoryType = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }



}
