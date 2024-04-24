package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.util.text.TextComponentString;

public class pSPacketPlayerListHeaderFooter extends Block {
    public pSPacketPlayerListHeaderFooter() {
        super( "PlayerListHeaderFooter", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketPlayerListHeaderFooter);
        this.packetclasses.add(SPacketPlayerListHeaderFooter.class);
    }

    public static class getFooter extends Block {
        public getFooter() {
            super( "getFooter", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerListHeaderFooter);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerListHeaderFooter) event).getFooter().toString();
        }
    }

    public static class getHeader extends Block {
        public getHeader() {
            super( "getHeader", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerListHeaderFooter);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerListHeaderFooter) event).getHeader().toString();
        }
    }


    public static class setHeader extends Block {
        public setHeader() {
            super( "setHeader", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerListHeaderFooter, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerListHeaderFooter) event).header = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFooter extends Block {
        public setFooter() {
            super( "setFooter", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerListHeaderFooter, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerListHeaderFooter) event).footer = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

}
