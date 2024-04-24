package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketPlayerListItem;

public class pSPacketPlayerListItem extends Block {
    public pSPacketPlayerListItem() {
        super("PlayerListItem", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketPlayerListItem);
        this.packetclasses.add(SPacketPlayerListItem.class);
    }


    public static class getActiontem extends Block {
        public getActiontem() {
            super("getActiontem", MainBlockType.ListItem, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerListItem);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerListItem) event).getAction();
        }
    }

    public static class ListItem extends Block {
        public ListItem() {
            super("ListItem", MainBlockType.ListItem, Tabs.Sub, Headers.SPacketPlayerListItem, BlockObjects.Name, BlockObjects.DropDown);
            for (SPacketPlayerListItem.Action value : SPacketPlayerListItem.Action.values()) {
                this.ddOptions.add(value.toString());
            }
        }
    }

    public static class setaction extends Block {
        public setaction() {
            super("setaction", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerListItem, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.ListItem));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerListItem) event).action = (SPacketPlayerListItem.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
