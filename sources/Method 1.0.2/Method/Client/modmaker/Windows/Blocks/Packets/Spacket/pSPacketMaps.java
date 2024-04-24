package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketMaps;

public class pSPacketMaps extends Block {
    public pSPacketMaps() {
        super( "Maps", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketMaps);
        this.packetclasses.add(SPacketMaps.class);
    }

    public static class getMapId extends Block {
        public getMapId() {
            super( "getMapId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).getMapId();

        }
    }

    public static class getmapScale extends Block {
        public getmapScale() {
            super( "getmapScale", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).mapScale;

        }
    }

    public static class gettrackingPosition extends Block {
        public gettrackingPosition() {
            super( "gettrackingPosition", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).trackingPosition;

        }
    }

    public static class getminX extends Block {
        public getminX() {
            super( "getminX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).minX;

        }
    }

    public static class getminZ extends Block {
        public getminZ() {
            super( "getminZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).minZ;

        }
    }

    public static class getcolumns extends Block {
        public getcolumns() {
            super( "getcolumns", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).columns;

        }
    }

    public static class getrows extends Block {
        public getrows() {
            super( "getrows", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMaps);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMaps) event).rows;

        }
    }

    public static class setmapId extends Block {
        public setmapId() {
            super( "setmapId", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).mapId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setmapScale extends Block {
        public setmapScale() {
            super( "setmapScale", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).mapScale = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class settrackingPosition extends Block {
        public settrackingPosition() {
            super( "settrackingPosition", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).trackingPosition = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setminX extends Block {
        public setminX() {
            super( "setminX", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).minX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setminZ extends Block {
        public setminZ() {
            super( "setminZ", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).minZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setcolumns extends Block {
        public setcolumns() {
            super( "setcolumns", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).columns = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setrows extends Block {
        public setrows() {
            super( "setrows", MainBlockType.Default, Tabs.Sub, Headers.SPacketMaps, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMaps) event).rows = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
