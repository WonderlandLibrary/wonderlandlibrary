package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketTabComplete;

import java.util.ArrayList;
import java.util.Arrays;

public class pSPacketTabComplete extends Block {
    public pSPacketTabComplete() {
        super( "ServerTabComplete", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketTabComplete);
        this.packetclasses.add(SPacketTabComplete.class);
    }

    public static class getMatches extends Block {
        public getMatches() {
            super( "getMatches", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketTabComplete);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return new ArrayList<>(Arrays.asList(((SPacketTabComplete) event).getMatches()));
        }
    }

    public static class addmatches extends Block {
        public addmatches() {
            super( "addmatches", MainBlockType.Default, Tabs.Sub, Headers.SPacketTabComplete, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTabComplete) event).matches = arrayAdd(((SPacketTabComplete) event).matches, (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class removematches extends Block {
        public removematches() {
            super( "removematches", MainBlockType.Default, Tabs.Sub, Headers.SPacketTabComplete, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTabComplete) event).matches = arrayRemove(((SPacketTabComplete) event).matches, (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }
}
