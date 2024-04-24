package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketAdvancementInfo;

public class pSPacketAdvancementInfo extends Block {
    public pSPacketAdvancementInfo() {
        super( "AdvancementInfo", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketAdvancementInfo);
        this.packetclasses.add(SPacketAdvancementInfo.class);
    }

    public static class isFirstSync extends Block {
        public isFirstSync() {
            super( "isFirstSync", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketAdvancementInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketAdvancementInfo) event).isFirstSync();
        }
    }

    public static class setFirstSync extends Block {
        public setFirstSync() {
            super( "setFirstSync", MainBlockType.Default, Tabs.Sub,Headers.SPacketAdvancementInfo, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketAdvancementInfo) event).firstSync = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
