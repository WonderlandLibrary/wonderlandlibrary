package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketTimeUpdate;

public class pSPacketTimeUpdate extends Block {
    public pSPacketTimeUpdate() {
        super( "TimeUpdate", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketTimeUpdate);
        this.packetclasses.add(SPacketTimeUpdate.class);
    }

    public static class getWorldTime extends Block {
        public getWorldTime() {
            super( "getWorldTime", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTimeUpdate);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTimeUpdate) event).getWorldTime();

        }
    }

    public static class getTotalWorldTime extends Block {
        public getTotalWorldTime() {
            super( "getTotalWorldTime", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTimeUpdate);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTimeUpdate) event).getTotalWorldTime();

        }
    }

    public static class setWorldTimepacket extends Block {
        public setWorldTimepacket() {
            super( "setWorldTimepacket", MainBlockType.Default, Tabs.Sub, Headers.SPacketTimeUpdate, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTimeUpdate) event).worldTime = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setTotalWorldTime extends Block {
        public setTotalWorldTime() {
            super( "setTotalWorldTime", MainBlockType.Default, Tabs.Sub, Headers.SPacketTimeUpdate, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTimeUpdate) event).totalWorldTime = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
